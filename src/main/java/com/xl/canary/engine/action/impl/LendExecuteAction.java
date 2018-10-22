package com.xl.canary.engine.action.impl;

import com.xl.canary.engine.action.IAction;
import com.xl.canary.engine.event.loan.LendResponseEvent;
import com.xl.canary.engine.launcher.IEventLauncher;
import com.xl.canary.entity.LoanOrderEntity;
import com.xl.canary.enums.ExecuteActionMethodEnum;
import com.xl.canary.service.LoanOrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;

/**
 * Created by gqwu on 2018/4/4.
 * 实际执行放款操作的行为
 */
public class LendExecuteAction implements IAction {

    private static final Logger logger = LoggerFactory.getLogger(LendExecuteAction.class);

    private String orderId;

    private LoanOrderService loanOrderService;

    private IEventLauncher loanOrderEventLauncher;

    public LendExecuteAction(String orderId, LoanOrderService loanOrderService, IEventLauncher loanOrderEventLauncher) {
        this.orderId = orderId;
        this.loanOrderService = loanOrderService;
        this.loanOrderEventLauncher = loanOrderEventLauncher;
    }

    @Override
    public void run() {
        try {
            LoanOrderEntity loanOrder = loanOrderService.getByOrderId(orderId);
            // 放款, 实际应该调用其他方法或第三方系统来获得此参数
            // 如果第三方系统时异步回调, 则接受的地方再调用此方法即可
            Boolean lendResult = true;
            LendResponseEvent event = null;
            if (lendResult) {
                // 放款成功
                event = new LendResponseEvent(orderId, loanOrder.getEquivalentAmount(), true, System.currentTimeMillis(), "放款成功", "放款成功");
            } else {
                // 放款失败
                event = new LendResponseEvent(orderId, BigDecimal.ZERO, false, System.currentTimeMillis(), "放款失败", "放款失败");
            }
            loanOrderEventLauncher.launch(event);
        } catch (Exception e) {
            logger.error("下单后, orderId: {}放款时发生错误:", this.orderId, e);
        }
    }

    @Override
    public String getActionName() {
        return this.getClass().getName();
    }

    @Override
    public String getActionType() {
        return ExecuteActionMethodEnum.CONTINUE.name();
    }

    @Override
    public String getUniqueId() {
        return orderId;
    }
}
