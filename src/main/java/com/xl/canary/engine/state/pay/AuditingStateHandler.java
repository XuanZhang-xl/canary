package com.xl.canary.engine.state.pay;

import com.xl.canary.engine.action.IActionExecutor;
import com.xl.canary.engine.action.impl.DeductLaunchAction;
import com.xl.canary.engine.event.IEvent;
import com.xl.canary.engine.event.order.AuditResponseEvent;
import com.xl.canary.engine.event.order.CancelEvent;
import com.xl.canary.engine.event.order.OrderExpireEvent;
import com.xl.canary.engine.launcher.IEventLauncher;
import com.xl.canary.engine.state.IStateHandler;
import com.xl.canary.engine.state.StateHandler;
import com.xl.canary.entity.PayOrderEntity;
import com.xl.canary.enums.StatusEnum;
import com.xl.canary.exception.InvalidEventException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by gqwu on 2018/4/4.
 */
@Component
@StateHandler(name = StatusEnum.AUDITING)
class AuditingStateHandler implements IStateHandler<PayOrderEntity> {

    @Autowired
    private IEventLauncher payOrderEventLauncher;

    @Override
    public PayOrderEntity handle(PayOrderEntity payOrder, IEvent event, IActionExecutor actionExecutor) throws InvalidEventException {

        if (event instanceof CancelEvent) {
            payOrder.setPayOrderState(StatusEnum.CANCELLED.name());

        } else if (event instanceof AuditResponseEvent) {
            AuditResponseEvent auditResponseEvent = (AuditResponseEvent) event;
            if (auditResponseEvent.isPass()) {
                payOrder.setPayOrderState(StatusEnum.PASSED.name());
                /** 此处，相当于在审核通过后，自动触发扣款 */
                actionExecutor.append(new DeductLaunchAction(payOrder.getPayOrderId(), payOrderEventLauncher));
            } else {
                payOrder.setPayOrderState(StatusEnum.REJECTED.name());
            }
        } else if (event instanceof OrderExpireEvent) {
            payOrder.setPayOrderState(StatusEnum.EXPIRED.name());
        } else {
            throw new InvalidEventException("还款订单状态与事件类型不匹配，状态：" + payOrder.getState() + "，事件：" + event);
        }

        return payOrder;
    }
}
