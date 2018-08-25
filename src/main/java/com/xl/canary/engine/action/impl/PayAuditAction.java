package com.xl.canary.engine.action.impl;

import com.xl.canary.engine.action.IAction;
import com.xl.canary.engine.event.IEvent;
import com.xl.canary.engine.event.order.AuditResponseEvent;
import com.xl.canary.engine.launcher.IEventLauncher;
import com.xl.canary.entity.PayOrderEntity;
import com.xl.canary.enums.ExecuteActionMethodEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PayAuditAction implements IAction {

    private static final Logger logger = LoggerFactory.getLogger(PayAuditAction.class);

    private PayOrderEntity payOrder;

    private IEventLauncher payOrderEventLauncher;

    public PayAuditAction(PayOrderEntity payOrder, IEventLauncher payOrderEventLauncher) {
        this.payOrder = payOrder;
        this.payOrderEventLauncher = payOrderEventLauncher;
    }

    @Override
    public void run() {

        IEvent event = new AuditResponseEvent(payOrder.getPayOrderId(), true, "审核通过");

        try {
            payOrderEventLauncher.launch(event);
        } catch (Exception e) {
            logger.error("还款订单审核结果事件发送异常，事件：[{}]", event, e);
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
        return payOrder.getPayOrderId();
    }
}
