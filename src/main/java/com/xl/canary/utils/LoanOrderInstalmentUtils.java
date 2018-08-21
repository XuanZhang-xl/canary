package com.xl.canary.utils;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * created by XUAN on 2018/08/19
 */
public class LoanOrderInstalmentUtils {

    public static List<BasicInstalment> simpleFixedInstallment(
            int remainingInstalments,
            BigDecimal remainingPrincipal,
            List<Fee> remainingFees,
            List<Fee> remainingFeesFollowInstalment,
            BigDecimal instalmentInterestRate,
            List<BasicInstalment> basicInstalments) {
        List<BasicInstalment> instalments = simpleFixedInstallmentRecursion(
                remainingInstalments,
                remainingPrincipal,
                remainingFees,
                remainingFeesFollowInstalment,
                instalmentInterestRate,
                basicInstalments);
        // 设置期数
        for (int i = 0; i < remainingInstalments; i++) {
            BasicInstalment basicInstalment = instalments.get(i);
            basicInstalment.setInstalment(i + 1);
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
    public static List<BasicInstalment> simpleFixedInstallmentRecursion(
            int remainingInstalments,
            BigDecimal remainingPrincipal,
            List<Fee> remainingFees,
            List<Fee> remainingFeesFollowInstalment,
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
        Map<String, BigDecimal> remainingFeesFollowInstalmentMap = new HashMap<String, BigDecimal>();
        if (remainingFeesFollowInstalment != null && remainingFeesFollowInstalment.size() > 0) {
            for (Fee feeFollowInstalment : remainingFeesFollowInstalment) {
                Integer instalment = feeFollowInstalment.getInstalment();
                if (instalment.equals(remainingInstalments)) {
                    remainingFeesFollowInstalmentMap.put(feeFollowInstalment.getElementName(), feeFollowInstalment.getAmount());
                }
            }
        }
        basicInstalment.setFeeFollowInstalment(remainingFeesFollowInstalmentMap);
        remainingInstalments = remainingInstalments - 1;
        basicInstalments.add(basicInstalment);
        return simpleFixedInstallmentRecursion(
                remainingInstalments,
                remainingPrincipal.subtract(instalmentPrincipal),
                getRemainingFee(remainingFees, instalmentAverageFee),
                remainingFeesFollowInstalment,
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
