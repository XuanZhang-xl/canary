package com.xl.canary.service.impl;

import com.xl.canary.bean.structure.Element;
import com.xl.canary.bean.structure.Instalment;
import com.xl.canary.bean.structure.Schema;
import com.xl.canary.bean.structure.Unit;
import com.xl.canary.entity.PayOrderDetailEntity;
import com.xl.canary.entity.PayOrderEntity;
import com.xl.canary.enums.BillTypeEnum;
import com.xl.canary.enums.loan.LoanOrderElementEnum;
import com.xl.canary.exception.SchemaException;
import com.xl.canary.mapper.PayOrderDetailMapper;
import com.xl.canary.service.PayOrderDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * Created by xzhang on 2018/9/25.
 */
@Service("payOrderDetailServiceImpl")
public class PayOrderDetailServiceImpl implements PayOrderDetailService {

    @Autowired
    private PayOrderDetailMapper payOrderDetailMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveScheme(PayOrderEntity payOrder, Schema orderSchema, Schema paySchema, Schema couponSchema, Schema strategySchema) {
        createSchemaDetail(payOrder, orderSchema, paySchema);
        createSchemaDetail(payOrder, orderSchema, couponSchema);
        createSchemaDetail(payOrder, orderSchema, strategySchema);
    }

    @Override
    public List<PayOrderDetailEntity> getByPayOrderId(String payOrderId) {
        Example example =new Example(PayOrderDetailEntity.class);
        example.createCriteria().andEqualTo("payOrderId", payOrderId);
        return payOrderDetailMapper.selectByExample(example);
    }

    @Override
    public List<PayOrderDetailEntity> getByOrderId(String orderId) {
        Example example =new Example(PayOrderDetailEntity.class);
        example.createCriteria().andEqualTo("orderId", orderId);
        return payOrderDetailMapper.selectByExample(example);
    }

    @Override
    public Schema recoverSchemaByOrderId(String orderId, BillTypeEnum billType) throws SchemaException {
        Example example =new Example(PayOrderDetailEntity.class);
        example.createCriteria().andEqualTo("orderId", orderId).andEqualTo("source", billType);
        List<PayOrderDetailEntity> payOrderDetailEntities = payOrderDetailMapper.selectByExample(example);
        // 仅限制   还款与优惠券
        if (!BillTypeEnum.RECOVERABLE_TYPE.contains(billType)) {
            throw new SchemaException("仅支持[还款]与[优惠券]还原schema");
        }
        return getSchemaFromDetailEntities(payOrderDetailEntities, billType);
    }

    @Override
    public Schema recoverSchemaByPayOrderId(String payOrderId) throws SchemaException {
        Example example =new Example(PayOrderDetailEntity.class);
        example.createCriteria().andEqualTo("payOrderId", payOrderId);
        List<PayOrderDetailEntity> payOrderDetailEntities = payOrderDetailMapper.selectByExample(example);
        return getSchemaFromDetailEntities(payOrderDetailEntities, BillTypeEnum.PAY_ORDER);
    }

    private Schema getSchemaFromDetailEntities(List<PayOrderDetailEntity> payOrderDetailEntities, BillTypeEnum source) throws SchemaException {
        Schema schema = new Schema();
        for (PayOrderDetailEntity detail : payOrderDetailEntities) {
            Instalment instalment = schema.get(detail.getInstalment());
            if (instalment == null) {
                instalment = new Instalment();
                schema.put(detail.getInstalment(), instalment);
                instalment.setRepaymentDate(detail.getRepaymentDate());
            } else {
                if (instalment.getRepaymentDate() != detail.getRepaymentDate()) {
                    // TODO: 如果有延期, 需要特殊处理: 查到是延期还款订单, 直接跳过即可
                    throw new SchemaException("同一个分期, 还款日不一致! 暂不支持延期!");
                }
            }
            LoanOrderElementEnum detailElement = LoanOrderElementEnum.valueOf(detail.getElement());
            Unit unit = instalment.get(detailElement);
            if (unit == null) {
                unit = new Unit();
                instalment.put(detailElement, unit);
            }
            Element element = new Element();
            element.setAmount(detail.getPaid());
            element.setSource(source);
            element.setSourceId(detail.getSourceId());
            element.setDestination(BillTypeEnum.valueOf(detail.getDestination()));
            element.setDestinationId(detail.getDestinationId());
            element.setElement(detailElement);
            element.setInstalment(detail.getInstalment());
            unit.add(element);
        }
        return schema;
    }

    private void createSchemaDetail(PayOrderEntity payOrder, Schema orderScheme, Schema schema) {
        for (Map.Entry<Integer, Instalment> instalmentEntry : schema.entrySet()) {
            Integer instalmentKey = instalmentEntry.getKey();
            Instalment instalment = instalmentEntry.getValue();
            Instalment orderInstalment = orderScheme.get(instalmentKey);
            for (Map.Entry<LoanOrderElementEnum, Unit> unitEntry : instalment.entrySet()) {
                LoanOrderElementEnum unitKey = unitEntry.getKey();
                Unit unit = unitEntry.getValue();
                Unit orderUnit = orderInstalment.get(unitKey);
                // 订单schema中的unit内元素有且仅有一个
                Element orderElement = orderUnit.get(0);
                for (Element element : unit) {
                    BigDecimal amount = element.getAmount();
                    if (BigDecimal.ZERO.compareTo(amount) == 0) {
                        continue;
                    }
                    PayOrderDetailEntity detail = new PayOrderDetailEntity();
                    detail.setSource(element.getSource().name());
                    detail.setSourceId(element.getSourceId());
                    detail.setDestination(element.getDestination().name());
                    detail.setDestinationId(element.getDestinationId());
                    detail.setUserCode(payOrder.getUserCode());
                    detail.setInstalment(instalmentKey);
                    detail.setRepaymentDate(orderInstalment.getRepaymentDate());
                    detail.setEquivalent(payOrder.getEquivalent());
                    detail.setElement(unitKey.name());
                    detail.setShouldPay(orderElement.getAmount());
                    detail.setPaid(element.getAmount());
                    payOrderDetailMapper.insert(detail);
                }
            }
        }
    }
}
