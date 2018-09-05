package com.xl.canary.engine.state.pay;

import com.xl.canary.engine.action.IActionExecutor;
import com.xl.canary.engine.action.impl.EntryExecuteAction;
import com.xl.canary.engine.event.IEvent;
import com.xl.canary.engine.event.order.pay.EntryLaunchEvent;
import com.xl.canary.engine.launcher.IEventLauncher;
import com.xl.canary.engine.state.IStateHandler;
import com.xl.canary.engine.state.StateHandler;
import com.xl.canary.entity.PayOrderEntity;
import com.xl.canary.enums.pay.PayTypeEnum;
import com.xl.canary.enums.StateEnum;
import com.xl.canary.exception.InvalidEventException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by gqwu on 2018/4/4.
 */
@Component
@StateHandler(name = StateEnum.ENTRY_FAILED)
public class EntryFailedStateHandler implements IStateHandler<PayOrderEntity> {

    @Autowired
    private IEventLauncher entryEventLauncher;

    @Override
    public PayOrderEntity handle(PayOrderEntity payOrder, IEvent event, IActionExecutor actionExecutor) throws InvalidEventException {

        if (event instanceof EntryLaunchEvent) {
            payOrder.setPayOrderState(StateEnum.ENTRY_DOING.name());
            PayTypeEnum payType = PayTypeEnum.valueOf(payOrder.getPayOrderType());
            actionExecutor.append(new EntryExecuteAction(payOrder.getUserCode(), payOrder.getPayOrderId(), payType, entryEventLauncher));

        } else {
            throw new InvalidEventException("还款订单状态与事件类型不匹配，状态：" + payOrder.getState() + "，事件：" + event);
        }

        return payOrder;
    }
}
