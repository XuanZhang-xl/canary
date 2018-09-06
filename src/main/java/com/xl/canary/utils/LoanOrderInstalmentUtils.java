package com.xl.canary.utils;

import com.xl.canary.bean.dto.BasicInstalment;
import com.xl.canary.bean.dto.Fee;
import com.xl.canary.enums.loan.FeeAllocateEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * created by XUAN on 2018/08/19
 */
public class LoanOrderInstalmentUtils {

    public static Logger logger = LoggerFactory.getLogger(LoanOrderInstalmentUtils.class);


    /**
     * TODO: 重新分期时如何获得  remainingFees 和 remainingFeesFollowInstalment
     * @param remainingInstalments
     * @param remainingPrincipal
     * @param remainingFees
     * @param instalmentInterestRate
     * @return
     */
    public static List<BasicInstalment> simpleFixedInstallment(
            int remainingInstalments,
            BigDecimal remainingPrincipal,
            List<Fee> remainingFees,
            BigDecimal instalmentInterestRate) {
        List<Fee> fees = new ArrayList<Fee>();
        List<Fee> feesFollowInstalment = new ArrayList<Fee>();
        // 把fee分成两类
        for (Fee remainingFee : remainingFees) {
            String allocate = remainingFee.getAllocate();
            if (FeeAllocateEnum.AVERAGE_IN_INSTALMENT.name().equals(allocate)) {
                fees.add(remainingFee);
            } else if (FeeAllocateEnum.FOLLOW_INSTALMENT.name().equals(allocate)) {
                feesFollowInstalment.add(remainingFee);
            } else {
                logger.error("无法匹配的FeeAllocateEnum[{}]", allocate);
            }
        }
        List<BasicInstalment> instalments = simpleFixedInstallmentRecursion(
                remainingInstalments,
                remainingPrincipal,
                fees,
                instalmentInterestRate,
                new ArrayList<BasicInstalment>());

        for (int i = 0; i < remainingInstalments; i++) {
            // 设置期数
            BasicInstalment basicInstalment = instalments.get(i);
            basicInstalment.setInstalment(i + 1);
            Map<String, BigDecimal> fee = basicInstalment.getFee();
            // 设置跟随期数的费用
            if (feesFollowInstalment != null && feesFollowInstalment.size() > 0) {
                for (Fee feeFollowInstalment : feesFollowInstalment) {
                    Integer instalment = feeFollowInstalment.getInstalment();
                    if (instalment.equals(i + 1)) {
                        fee.put(feeFollowInstalment.getElementName(), feeFollowInstalment.getAmount());
                        break;
                    }
                }
            }
        }
        return instalments;
    }


    /**
     * 得到期平均还款的前提下，递归计算每期当中应还的本金和利息
     *
     * @param remainingInstalments
     * @param remainingInstalments
     * @param remainingPrincipal
     * @param instalmentInterestRate
     * @param basicInstalments 简单分期明细
     * @return List<BasicInstalment> 简单分期明细
     */
    private static List<BasicInstalment> simpleFixedInstallmentRecursion(
            int remainingInstalments,
            BigDecimal remainingPrincipal,
            List<Fee> remainingFees,
            BigDecimal instalmentInterestRate,
            List<BasicInstalment> basicInstalments) {
        if (remainingInstalments == 0) {
            return basicInstalments;
        }
        BigDecimal instalmentAverage = instalmentAverage(remainingPrincipal, remainingInstalments, instalmentInterestRate);
        /**
         * 利息向上取整 = 本金向下取整
         */
        BigDecimal instalmentInterest = remainingPrincipal.multiply(instalmentInterestRate).setScale(LoanLimitation.RESULT_SCALE, BigDecimal.ROUND_UP);
        BigDecimal instalmentPrincipal = instalmentAverage.subtract(instalmentInterest);
        Map<String, BigDecimal> instalmentAverageFee = instalmentAverageFee(remainingFees, remainingInstalments);
        BasicInstalment basicInstalment = new BasicInstalment();
        basicInstalment.setPrincipal(instalmentPrincipal);
        basicInstalment.setInterest(instalmentInterest);
        basicInstalment.setFee(instalmentAverageFee);
        basicInstalments.add(basicInstalment);
        remainingInstalments = remainingInstalments - 1;
        return simpleFixedInstallmentRecursion(
                remainingInstalments,
                remainingPrincipal.subtract(instalmentPrincipal),
                getRemainingFee(remainingFees, instalmentAverageFee),
                instalmentInterestRate,
                basicInstalments);
    }

    private static Map<String, BigDecimal> instalmentAverageFee(List<Fee> remainingFees, int instalments) {
        Map<String, BigDecimal> instalmentFee = new HashMap<String, BigDecimal>();
        for (Fee remainingFee : remainingFees) {
            BigDecimal fee = remainingFee.getAmount();
            instalmentFee.put(remainingFee.getElementName(), fee.divide(new BigDecimal(instalments), LoanLimitation.RESULT_SCALE, BigDecimal.ROUND_UP));
        }
        return instalmentFee;
    }

    private static List<Fee> getRemainingFee (List<Fee> remainingFees, Map<String, BigDecimal> instalmentAverageFee) {
        for (Fee remainingFee : remainingFees) {
            BigDecimal fee = instalmentAverageFee.get(remainingFee.getElementName());
            remainingFee.setAmount(remainingFee.getAmount().subtract(fee));
        }
        return remainingFees;
    }

    /**
     * 计算每期平均应还本息
     *
     * @param loanAmount
     * @param instalments
     * @param instalmentInterestRate
     * @return
     */
    private static BigDecimal instalmentAverage (
            BigDecimal loanAmount,
            int instalments,
            BigDecimal instalmentInterestRate) {

        //总期数所得本金比 = （1＋i）^n
        BigDecimal instalmentsCompoundRate = instalmentInterestRate.add(BigDecimal.ONE).pow(instalments);
        //期平均应还
        BigDecimal instalmentAverage = loanAmount
                .multiply(instalmentInterestRate)
                .multiply(instalmentsCompoundRate)
                .divide(instalmentsCompoundRate.subtract(BigDecimal.ONE), LoanLimitation.RESULT_SCALE, BigDecimal.ROUND_UP);

        return instalmentAverage;
    }
}
