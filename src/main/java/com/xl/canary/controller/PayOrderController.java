package com.xl.canary.controller;

import com.xl.canary.bean.BaseResponse;
import com.xl.canary.bean.req.PayOrderReq;
import com.xl.canary.bean.res.ShouldPayRes;
import com.xl.canary.bean.structure.Schema;
import com.xl.canary.entity.LoanOrderEntity;
import com.xl.canary.entity.PayOrderEntity;
import com.xl.canary.entity.UserEntity;
import com.xl.canary.enums.CurrencyEnum;
import com.xl.canary.enums.ResponseNutEnum;
import com.xl.canary.enums.StateEnum;
import com.xl.canary.enums.pay.PayTypeEnum;
import com.xl.canary.lock.Lock;
import com.xl.canary.lock.RedisService;
import com.xl.canary.service.BillService;
import com.xl.canary.service.LoanOrderService;
import com.xl.canary.service.PayOrderService;
import com.xl.canary.service.UserService;
import com.xl.canary.simulator.ExchangeRateSimulator;
import com.xl.canary.utils.IDWorker;
import com.xl.canary.utils.RedisLockKeySuffix;
import com.xl.canary.utils.ReqHeaderParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.concurrent.TimeUnit;

/**
 * created by XUAN on 2018/09/08
 */
@RestController
@RequestMapping("/pay")
public class PayOrderController {

    Logger logger = LoggerFactory.getLogger(PayOrderController.class);

    @Autowired
    private IDWorker idWorker;

    @Autowired
    private LoanOrderService loanOrderService;

    @Autowired
    private PayOrderService payOrderService;

    @Autowired
    private UserService userService;

    @Autowired
    private BillService billService;

    @Autowired
    private RedisService redisService;

    @Value("${equivalent.currency}")
    private String equivalent;


    @GetMapping("/should_pay")
    public BaseResponse<ShouldPayRes> shouldPay (
            @RequestParam("orderId") String orderId,
            HttpServletRequest request) {
        BaseResponse<ShouldPayRes> response = new BaseResponse<ShouldPayRes>();
        String userCode = request.getHeader(ReqHeaderParams.ACCOUNT_CODE);
        UserEntity user = userService.getByUserCode(userCode);
        if (user == null||userCode==null) {
            return response.buildFailedResponse(ResponseNutEnum.NO_USER);
        }

        LoanOrderEntity loanOrder = loanOrderService.getByOrderId(orderId);
        if (loanOrder == null || !StateEnum.LENT.name().equals(loanOrder.getState())) {
            return response;
        }
        try {
            Schema payoffSchema = billService.payOffLoanOrderAndStrategy(loanOrder);
            Schema shouldPaySchema = billService.shouldPayLoanOrderAndStrategy(loanOrder);
            Schema planSchema = billService.planLoanOrderAndStrategy(loanOrder);
            ShouldPayRes res = new ShouldPayRes();
            res.setPayoffAmount(payoffSchema);
            res.setShouldPayAmount(shouldPaySchema);
            res.setPlanAmount(planSchema);
            return response.buildSuccessResponse(res);
        } catch (Exception e) {
            logger.error("计算还清schema出错", e);
            return response.buildFailedResponse(ResponseNutEnum.UNKNOWN_EXCEPTION);
        }
    }

    @PostMapping("/pay_order")
    public BaseResponse<PayOrderReq> repayment (@RequestBody @Validated PayOrderReq req) {
        BaseResponse<PayOrderReq> response = new BaseResponse<PayOrderReq>();

        String userCode = req.getUserCode();
        UserEntity user = userService.getByUserCode(userCode);
        if (user == null) {
            return response.buildFailedResponse(ResponseNutEnum.NO_USER);
        }

        Lock lock = new Lock(RedisLockKeySuffix.PAY_ORDER_KEY + userCode, String.valueOf(idWorker.generateID()));
        try {
            // 强制锁用户30秒, 以防重复下单
            if (redisService.lock(lock.getName(), lock.getValue(), 30000, TimeUnit.MILLISECONDS)) {
                String loanOrderId = req.getLoanOrderId();
                LoanOrderEntity loanOrder = loanOrderService.getByOrderId(loanOrderId);
                if (loanOrder == null) {
                    return response.buildFailedResponse(ResponseNutEnum.NO_ORDER);
                }
                BigDecimal amount = req.getAmount();
                CurrencyEnum applyCurrency = req.getApplyCurrency();
                PayOrderEntity payOrder = new PayOrderEntity();
                // 用户主动还款暂时仅支持换一种币种
                payOrder.setPayOrderId(String.valueOf(idWorker.nextId()));
                payOrder.setPayBatchId(String.valueOf(idWorker.nextId()));
                payOrder.setUserCode(userCode);
                // 再改好了
                payOrder.setPayOrderType(PayTypeEnum.REPAY_AS_PLAN.name());
                payOrder.setPayOrderState(StateEnum.PENDING.name());
                payOrder.setApplyCurrency(applyCurrency.name());
                payOrder.setApplyAmount(amount);
                payOrder.setEquivalent(equivalent);
                BigDecimal ticker = ExchangeRateSimulator.getTicker(applyCurrency.name(), equivalent);
                payOrder.setEquivalentRate(ticker);
                payOrder.setEquivalentAmount(ticker.multiply(amount));

                long now = System.currentTimeMillis();
                payOrder.setCreateTime(now);
                payOrder.setUpdateTime(now);
                payOrder.setIsDeleted(0);
                payOrderService.save(payOrder);
            } else {
                logger.error("用户[{}]下单锁竞争失败", userCode);
                return response.buildFailedResponse(ResponseNutEnum.LOCK_ERROR);
            }
        } catch (Exception e) {
            logger.error("还款下单报错:", e);
        } finally {
            redisService.release(lock);
        }
        return response;
    }
}
