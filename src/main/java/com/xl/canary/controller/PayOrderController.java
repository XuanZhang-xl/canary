package com.xl.canary.controller;

import com.xl.canary.bean.BaseResponse;
import com.xl.canary.bean.req.PayOrderReq;
import com.xl.canary.entity.LoanOrderEntity;
import com.xl.canary.entity.PayOrderEntity;
import com.xl.canary.entity.UserEntity;
import com.xl.canary.enums.CurrencyEnum;
import com.xl.canary.enums.ResponseNutEnum;
import com.xl.canary.enums.StateEnum;
import com.xl.canary.lock.RedisService;
import com.xl.canary.service.LoanOrderService;
import com.xl.canary.service.UserService;
import com.xl.canary.simulator.ExchangeRateSimulator;
import com.xl.canary.utils.IDWorker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    private UserService userService;

    @Autowired
    private RedisService redisService;

    @Value("${equivalent.currency}")
    private String equivalent;

    @PostMapping("/pay_order")
    public BaseResponse<PayOrderReq> repayment (@RequestBody @Validated PayOrderReq req) {
        BaseResponse<PayOrderReq> response = new BaseResponse<PayOrderReq>();

        String userCode = req.getUserCode();
        UserEntity user = userService.getByUserCode(userCode);
        if (user == null) {
            return response.buildFailedResponse(ResponseNutEnum.NO_USER);
        }

        try {
            // 强制锁用户30秒, 以防重复下单
            if (redisService.lock(userCode, userCode, 30000, TimeUnit.MILLISECONDS)) {
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
                // payOrder.setPayOrderType();
                payOrder.setPayOrderState(StateEnum.PENDING.name());
                payOrder.setApplyCurrency(applyCurrency.name());
                payOrder.setApplyAmount(amount);
                payOrder.setEquivalent(equivalent);
                payOrder.setEquivalentRate(ExchangeRateSimulator.getTicker(applyCurrency.name(), equivalent));



            } else {
                logger.error("用户[{}]下单锁竞争失败", userCode);
                return response.buildFailedResponse(ResponseNutEnum.LOCK_ERROR);
            }
        } catch (Exception e) {
            logger.error("下单报错:", e);
        } finally {
            redisService.unlock(userCode);
        }

        return response;
    }

}
