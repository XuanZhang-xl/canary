package com.xl.canary.service;

import com.xl.canary.bean.structure.Schema;
import com.xl.canary.entity.CouponEntity;
import com.xl.canary.entity.LoanOrderEntity;
import com.xl.canary.entity.PayOrderEntity;
import com.xl.canary.exception.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * 账单service
 * created by XUAN on 2018/09/22
 */
public interface BillService {

    /**
     * 还清一个订单所要的schema
     * 不计算策略
     * 可以获得应还总额
     * @param loanOrder
     * @return
     */
    Schema payOffLoanOrder(LoanOrderEntity loanOrder) throws SchemaException;

    /**
     * 还清一个订单及策略所要的schema
     * 可以获得应还总额
     * @param loanOrder
     * @return
     */
    Schema payOffLoanOrderAndStrategy(LoanOrderEntity loanOrder) throws BaseException;

    /**
     * 用了优惠券后还清一个订单所要的schema
     * 计算策略
     * @param loanOrder         订单
     * @param couponEntities    优惠券
     * @return
     */
    Schema payOffAll(LoanOrderEntity loanOrder, List<CouponEntity> couponEntities) throws BaseException;

    /**
     * 应还账单
     * @param loanOrder
     * @return
     * @throws BaseException
     */
    Schema shouldPayLoanOrderAndStrategy(LoanOrderEntity loanOrder) throws BaseException;

    /**
     * 计划账单
     * @param loanOrder
     * @return
     * @throws BaseException
     */
    Schema planLoanOrderAndStrategy(LoanOrderEntity loanOrder) throws BaseException;

    /**
     * 还款订单入账获得的schema
     * 计算策略
     * @param loanOrder
     * @param payOrder
     * @return
     */
    Schema payLoanOrder(LoanOrderEntity loanOrder, PayOrderEntity payOrder) throws BaseException;

    /**
     * 还款订单入账获得的schema, 包括优惠券
     * 计算策略
     * @param loanOrder
     * @param couponEntities    优惠券
     * @param payOrder
     * @return
     */
    Schema payLoanOrder(LoanOrderEntity loanOrder, List<CouponEntity> couponEntities, PayOrderEntity payOrder) throws BaseException;
}
