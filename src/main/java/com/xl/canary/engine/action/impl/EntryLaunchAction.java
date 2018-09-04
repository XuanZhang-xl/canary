package com.xl.canary.engine.action.impl;

import com.xl.canary.engine.action.IAction;
import com.xl.canary.engine.event.order.pay.EntryLaunchEvent;
import com.xl.canary.engine.launcher.IEventLauncher;
import com.xl.canary.entity.PayOrderEntity;
import com.xl.canary.enums.ExecuteActionMethodEnum;
import com.xl.canary.service.PayOrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EntryLaunchAction implements IAction {

    private static final Logger logger = LoggerFactory.getLogger(EntryLaunchAction.class);

    private String payOrderId;

    private IEventLauncher payOrderEventLauncher;

    private PayOrderService payOrderService;

    public EntryLaunchAction(String payOrderId, IEventLauncher payOrderEventLauncher, PayOrderService payOrderService) {
        this.payOrderId = payOrderId;
        this.payOrderEventLauncher = payOrderEventLauncher;
        this.payOrderService = payOrderService;
    }

    @Override
    public void run() {
        EntryLaunchEvent entryLaunchEvent = new EntryLaunchEvent(payOrderId);
        try {
            PayOrderEntity payOrder = payOrderService.getByOrderId(payOrderId);

            this.payOrderEventLauncher.launch(entryLaunchEvent);
        } catch (Exception e) {
            logger.error("自动发送发起入账事件异常，事件：[{}]", entryLaunchEvent, e);
        }
    }

    @Override
    public String getActionName() {
        return this.getClass().getName();    }

    @Override
    public String getActionType() {
        return ExecuteActionMethodEnum.NEW_THREAD.name();
    }

    @Override
    public String getUniqueId() {
        return payOrderId;
    }
}
