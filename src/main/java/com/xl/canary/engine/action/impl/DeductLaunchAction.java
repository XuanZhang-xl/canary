package com.xl.canary.engine.action.impl;

import com.xl.canary.engine.action.IAction;
import com.xl.canary.engine.event.order.pay.DeductLaunchEvent;
import com.xl.canary.engine.launcher.IEventLauncher;
import com.xl.canary.enums.ExecuteActionMethodEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DeductLaunchAction implements IAction {

    private static final Logger logger = LoggerFactory.getLogger(DeductLaunchAction.class);

    private String payOrderId;

    private IEventLauncher payOrderEventLauncher;

    public DeductLaunchAction(String payOrderId, IEventLauncher payOrderEventLauncher) {
        this.payOrderId = payOrderId;
        this.payOrderEventLauncher = payOrderEventLauncher;
    }

    @Override
    public void run() {
        DeductLaunchEvent deductLaunchEvent = new DeductLaunchEvent(payOrderId);

        try {
            this.payOrderEventLauncher.launch(deductLaunchEvent);
        } catch (Exception e) {
            logger.error("自动发送发起扣款事件异常，事件：[{}]", deductLaunchEvent, e);
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
        return payOrderId;
    }

}
