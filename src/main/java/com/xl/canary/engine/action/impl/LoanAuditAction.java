package com.xl.canary.engine.action.impl;

import com.xl.canary.engine.action.IAction;
import com.xl.canary.engine.event.IEvent;
import com.xl.canary.engine.event.order.AuditResponseEvent;
import com.xl.canary.engine.launcher.IEventLauncher;
import com.xl.canary.entity.LoanOrderEntity;
import com.xl.canary.enums.ExecuteActionMethodEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by gqwu on 2018/4/4.
 * 贷款订单审核行为
 */
public class LoanAuditAction implements IAction {

    private static final Logger logger = LoggerFactory.getLogger(LoanAuditAction.class);

    private LoanOrderEntity loanOrder;

    private IEventLauncher eventLauncher;

    public LoanAuditAction(LoanOrderEntity loanOrder, IEventLauncher eventLauncher) {
        this.loanOrder = loanOrder;
        this.eventLauncher = eventLauncher;
    }

    @Override
    public void run() {
        /**
         * 目前审核自动通过
         * 可以在这儿编写审核代码
         */
        IEvent event = new AuditResponseEvent(loanOrder.getOrderId(), true, "审核通过");
        try {
            eventLauncher.launch(event);
        } catch (Exception e) {
            logger.error("审核结果事件发送异常，事件：[{}]", event, e);
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
        return loanOrder.getOrderId();
    }

}
