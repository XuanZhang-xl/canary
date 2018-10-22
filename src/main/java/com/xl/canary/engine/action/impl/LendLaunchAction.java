package com.xl.canary.engine.action.impl;

import com.xl.canary.engine.action.IAction;
import com.xl.canary.engine.event.loan.LendLaunchEvent;
import com.xl.canary.engine.launcher.IEventLauncher;
import com.xl.canary.enums.ExecuteActionMethodEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by gqwu on 2018/4/4.
 * 发起放款
 */
public class LendLaunchAction implements IAction {

    private static final Logger logger = LoggerFactory.getLogger(LendLaunchAction.class);

    private String loanOrderId;

    private IEventLauncher loanOrderEventLauncher;

    public LendLaunchAction(String loanOrderId, IEventLauncher loanOrderEventLauncher) {
        this.loanOrderId = loanOrderId;
        this.loanOrderEventLauncher = loanOrderEventLauncher;
    }

    @Override
    public void run(){
        LendLaunchEvent lendLaunchEvent = new LendLaunchEvent(loanOrderId);
        try {
            loanOrderEventLauncher.launch(lendLaunchEvent);
        } catch (Exception e) {
            logger.error("自动发送发起放款事件异常，事件：[{}]", lendLaunchEvent, e);
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
        return loanOrderId;
    }
}
