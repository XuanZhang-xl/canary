package com.xl.canary.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.xl.canary.bean.dto.BasicInstalment;
import com.xl.canary.bean.dto.Fee;
import com.xl.canary.entity.LoanInstalmentEntity;
import com.xl.canary.entity.LoanOrderEntity;
import com.xl.canary.enums.loan.InstallmentModeEnum;
import com.xl.canary.enums.loan.LoanOrderTypeEnum;
import com.xl.canary.enums.StateEnum;
import com.xl.canary.mapper.LoanInstalmentMapper;
import com.xl.canary.service.LoanInstalmentService;
import com.xl.canary.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * created by XUAN on 2018/08/21
 */
@Service("loanInstalmentServiceImpl")
public class LoanInstalmentServiceImpl implements LoanInstalmentService {

    @Autowired
    private IDWorker idWorker;

    @Autowired
    private LoanInstalmentMapper loanInstalmentMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveLoanInstalments (List<LoanInstalmentEntity> loanInstalments) {
        for (LoanInstalmentEntity loanInstalment : loanInstalments) {
            loanInstalmentMapper.insert(loanInstalment);
        }
    }

    @Override
    public void update(LoanInstalmentEntity instalment) {
        loanInstalmentMapper.updateByPrimaryKey(instalment);
    }

    @Override
    public List<LoanInstalmentEntity> generateInstalments(LoanOrderEntity loanOrder) {
        List<LoanInstalmentEntity> loanInstalmentEntities = new ArrayList<LoanInstalmentEntity>();
        LoanOrderTypeEnum loanOrderType = LoanOrderTypeEnum.valueOf(loanOrder.getOrderType());
        InstallmentModeEnum installmentMode = loanOrderType.getInstallmentMode();
        switch (installmentMode) {
            case FIXED_INSTALLMENT:{
                Integer instalment = loanOrder.getInstalment();
                BigDecimal equivalentAmount = loanOrder.getEquivalentAmount();
                List<Fee> deserializeFee = loanOrder.getDeserializeFee();
                BigDecimal instalmentRate = loanOrder.getInstalmentRate();
                Map<Integer, Long> repaymentDates = LoanOrderDateUtils.listRepaymentDates(loanOrder);
                List<BasicInstalment> basicInstalments = LoanOrderInstalmentUtils.simpleFixedInstallment(
                        instalment,
                        equivalentAmount,
                        deserializeFee,
                        instalmentRate);
                for (BasicInstalment basicInstalment : basicInstalments) {
                    LoanInstalmentEntity loanInstalment = new LoanInstalmentEntity();
                    long instalmentId = idWorker.nextId();
                    loanInstalment.setInstalmentId(String.valueOf(instalmentId));
                    loanInstalment.setOrderId(loanOrder.getOrderId());
                    loanInstalment.setUserCode(loanOrder.getUserCode());
                    loanInstalment.setOrderType(loanOrderType.name());
                    loanInstalment.setInstalmentState(StateEnum.PENDING.name());
                    Integer instalmentDate = basicInstalment.getInstalment();
                    loanInstalment.setInstalment(instalmentDate);
                    loanInstalment.setEquivalent(loanOrder.getEquivalent());
                    // 原始应还信息
                    loanInstalment.setOriginalPrincipal(basicInstalment.getPrincipal());
                    loanInstalment.setOriginalInterest(basicInstalment.getInterest());
                    Map<String, BigDecimal> feeMap = basicInstalment.getFee();
                    String jsonFee = JSONObject.toJSONString(feeMap);
                    loanInstalment.setOriginalFee(jsonFee);
                    // 应还信息
                    loanInstalment.setPaidPrincipal(BigDecimal.ZERO);
                    // 当天不算利息, 从第二天开始算利息
                    loanInstalment.setPaidInterest(BigDecimal.ZERO);
                    loanInstalment.setPaidPenalty(BigDecimal.ZERO);
                    // 初始化已还服务费
                    for (Map.Entry<String, BigDecimal> entry : feeMap.entrySet()) {
                        feeMap.put(entry.getKey(), BigDecimal.ZERO);
                    }
                    loanInstalment.setPaidFee(JSONObject.toJSONString(feeMap));
                    loanInstalment.setLastPaidPrincipalDate(-1L);
                    // 还款日
                    loanInstalment.setShouldPayTime(repaymentDates.get(instalmentDate));

                    Long now = System.currentTimeMillis();
                    loanInstalment.setCreateTime(now);
                    loanInstalment.setUpdateTime(now);
                    loanInstalment.setTimeZone(loanOrder.getTimeZone());
                    loanInstalment.setIsDeleted(0);
                    loanInstalmentEntities.add(loanInstalment);
                }
                break;
            }
            case FIXED_PRINCIPAL:{
                break;
            }
            default:{

            }
        }
        return loanInstalmentEntities;
    }

    @Override
    public List<LoanInstalmentEntity> listInstalments(String orderId) {
        Example example = new Example(LoanInstalmentEntity.class);
        example.createCriteria().andEqualTo("orderId", orderId);
        return loanInstalmentMapper.selectByExample(example);
    }

    @Override
    public LoanInstalmentEntity getByInstalmentId(String instalmentId) {
        Example example = new Example(LoanInstalmentEntity.class);
        example.createCriteria().andEqualTo("instalmentId", instalmentId);
        return loanInstalmentMapper.selectOneByExample(example);
    }
}
