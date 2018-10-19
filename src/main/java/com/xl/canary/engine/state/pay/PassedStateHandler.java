package com.xl.canary.engine.state.pay;

import com.xl.canary.engine.action.IActionExecutor;
import com.xl.canary.engine.action.impl.DeductExecuteAction;
import com.xl.canary.engine.event.IEvent;
import com.xl.canary.engine.event.order.CancelEvent;
import com.xl.canary.engine.event.pay.DeductLaunchEvent;
import com.xl.canary.engine.launcher.IEventLauncher;
import com.xl.canary.engine.state.IStateHandler;
import com.xl.canary.engine.state.StateHandler;
import com.xl.canary.entity.PayOrderEntity;
import com.xl.canary.enums.StateEnum;
import com.xl.canary.exception.InvalidEventException;
import com.xl.canary.service.PayOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by gqwu on 2018/4/4.
 */
@Component
@StateHandler(name = StateEnum.PASSED)
public class PassedStateHandler implements IStateHandler<PayOrderEntity> {

    @Autowired
    private PayOrderService payOrderService;

    @Autowired
    private IEventLauncher payOrderEventLauncher;

    @Override
    public PayOrderEntity handle(PayOrderEntity payOrder, IEvent event, IActionExecutor actionExecutor) throws InvalidEventException {

        if (event instanceof CancelEvent) {
            payOrder.setPayOrderState(StateEnum.CANCELLED.name());

        } else if (event instanceof DeductLaunchEvent) {
            payOrder.setPayOrderState(StateEnum.DEDUCTING.name());
            actionExecutor.append(new DeductExecuteAction(payOrder.getPayOrderId(), payOrderService, payOrderEventLauncher));

        } else {
            throw new InvalidEventException("还款订单状态与事件类型不匹配，状态：" + payOrder.getState() + "，事件：" + event);
        }

        return payOrder;
    }
}
