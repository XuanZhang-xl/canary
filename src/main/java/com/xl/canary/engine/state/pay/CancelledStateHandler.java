package com.xl.canary.engine.state.pay;

import com.xl.canary.engine.action.IActionExecutor;
import com.xl.canary.engine.event.IEvent;
import com.xl.canary.engine.event.order.RecoveringEvent;
import com.xl.canary.engine.state.IStateHandler;
import com.xl.canary.engine.state.StateHandler;
import com.xl.canary.entity.PayOrderEntity;
import com.xl.canary.enums.StateEnum;
import com.xl.canary.exception.InvalidEventException;
import org.springframework.stereotype.Component;

/**
 * Created by gqwu on 2018/4/4.
 */
@Component
@StateHandler(name = StateEnum.CANCELLED)
public class CancelledStateHandler implements IStateHandler<PayOrderEntity> {

    @Override
    public PayOrderEntity handle(PayOrderEntity payOrder, IEvent event, IActionExecutor actionExecutor) throws InvalidEventException {

        /** 恢复订单 */
        if (event instanceof RecoveringEvent) {
            payOrder.setPayOrderState(StateEnum.PENDING.name());

        } else {
            throw new InvalidEventException("还款订单状态与事件类型不匹配，状态：" + payOrder.getState() + "，事件：" + event);
        }

        return payOrder;
    }
}
