package com.xl.canary.engine.state.pay;

import com.xl.canary.engine.action.IActionExecutor;
import com.xl.canary.engine.action.impl.PayAuditAction;
import com.xl.canary.engine.event.IEvent;
import com.xl.canary.engine.event.order.AuditLaunchEvent;
import com.xl.canary.engine.launcher.IEventLauncher;
import com.xl.canary.engine.state.IStateHandler;
import com.xl.canary.engine.state.StateHandler;
import com.xl.canary.entity.PayOrderEntity;
import com.xl.canary.enums.StateEnum;
import com.xl.canary.exception.InvalidEventException;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Created by gqwu on 2018/4/4.
 */
@Component
@StateHandler(name = StateEnum.REJECTED)
public class RejectedStateHandler implements IStateHandler<PayOrderEntity> {

    @Resource(name = "payOrderEventLauncher")
    private IEventLauncher payOrderEventLauncher;

    @Override
    public PayOrderEntity handle(PayOrderEntity payOrder, IEvent event, IActionExecutor actionExecutor) throws InvalidEventException {

        if (event instanceof AuditLaunchEvent) {
            payOrder.setPayOrderState(StateEnum.AUDITING.name());
            /** 还款订单审核 */
            actionExecutor.append(new PayAuditAction(payOrder, payOrderEventLauncher));
        } else {
            throw new InvalidEventException("还款订单状态与事件类型不匹配，状态：" + payOrder.getState() + "，事件：" + event);
        }

        return payOrder;
    }
}
