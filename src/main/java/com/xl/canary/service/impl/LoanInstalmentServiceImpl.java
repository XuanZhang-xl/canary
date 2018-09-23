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
                    String jsonFee = JSONObject.toJSONString(basicInstalment.getFee());
                    loanInstalment.setOriginalFee(jsonFee);
                    // 应还信息
                    loanInstalment.setPrincipal(basicInstalment.getPrincipal());
                    loanInstalment.setInterest(basicInstalment.getInterest());
                    loanInstalment.setPenalty(BigDecimal.ZERO);
                    loanInstalment.setFee(jsonFee);
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
        //TODO: Example是这样用吗?
        Example example = new Example(LoanInstalmentEntity.class);
        example.createCriteria().andEqualTo("orderId", orderId);
        return loanInstalmentMapper.selectByExample(example);
    }
}
