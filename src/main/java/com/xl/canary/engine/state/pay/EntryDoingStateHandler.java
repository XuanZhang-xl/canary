package com.xl.canary.engine.state.pay;

import com.xl.canary.engine.action.IActionExecutor;
import com.xl.canary.engine.event.IEvent;
import com.xl.canary.engine.event.order.pay.EntryResponseEvent;
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
@StateHandler(name = StateEnum.ENTRY_DOING)
public class EntryDoingStateHandler implements IStateHandler<PayOrderEntity> {

    @Override
    public PayOrderEntity handle(PayOrderEntity payOrder, IEvent event, IActionExecutor actionExecutor) throws InvalidEventException {

        if (event instanceof EntryResponseEvent) {
            EntryResponseEvent entryResponseEvent = (EntryResponseEvent) event;
            payOrder.setEntryNumber(payOrder.getEntryNumber().add(entryResponseEvent.getActualEntry()));
            if (payOrder.getEntryNumber().compareTo(payOrder.getEquivalentAmount()) == 0) {
                payOrder.setPayOrderState(StateEnum.ENTRY_DONE.name());
            } else {
                payOrder.setPayOrderState(StateEnum.ENTRY_FAILED.name());
            }
            payOrder.setEntryOverTime(entryResponseEvent.getEventTime());
        }  else {
            throw new InvalidEventException(
                    "还款订单状态与事件类型不匹配，状态：" + payOrder.getState() + "，事件：" + event);
        }

        return payOrder;
    }
}
