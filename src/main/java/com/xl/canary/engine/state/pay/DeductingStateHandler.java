package com.xl.canary.engine.state.pay;

import com.xl.canary.engine.action.IActionExecutor;
import com.xl.canary.engine.action.impl.EntryLaunchAction;
import com.xl.canary.engine.event.IEvent;
import com.xl.canary.engine.event.order.pay.DeductResponseEvent;
import com.xl.canary.engine.launcher.IEventLauncher;
import com.xl.canary.engine.state.IStateHandler;
import com.xl.canary.engine.state.StateHandler;
import com.xl.canary.entity.PayOrderEntity;
import com.xl.canary.enums.StatusEnum;
import com.xl.canary.exception.InvalidEventException;
import com.xl.canary.service.PayOrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by gqwu on 2018/4/4.
 */
@Component
@StateHandler(name = StatusEnum.DEDUCTING)
public class DeductingStateHandler implements IStateHandler<PayOrderEntity> {

    private static final Logger logger = LoggerFactory.getLogger(DeductingStateHandler.class);

    @Autowired
    private IEventLauncher payOrderEventLauncher;

    @Autowired
    private PayOrderService payOrderService;

    @Override
    public PayOrderEntity handle(PayOrderEntity payOrder, IEvent event, IActionExecutor actionExecutor) throws InvalidEventException {

        if (event instanceof DeductResponseEvent) {
            DeductResponseEvent deductResponseEvent = (DeductResponseEvent) event;
            if (deductResponseEvent.isSucceeded()) {
                payOrder.setPayOrderState(StatusEnum.DEDUCTED.name());
                payOrder.setPayNumber(deductResponseEvent.getActualDeducted());
                payOrder.setPayTime(deductResponseEvent.getEventTime());
                /** 此处相当于在扣款成功后，自动发起入账 */
                actionExecutor.append(new EntryLaunchAction(payOrder.getPayOrderId(), payOrderEventLauncher, payOrderService));

            } else {
                payOrder.setPayOrderState(StatusEnum.DEDUCT_FAILED.name());
            }
        } else {
            throw new InvalidEventException(
                    "还款订单状态与事件类型不匹配，状态：" + payOrder.getState() + "，事件：" + event);
        }

        return payOrder;
    }
}
