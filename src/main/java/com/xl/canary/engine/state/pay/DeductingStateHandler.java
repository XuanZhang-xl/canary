package com.xl.canary.engine.state.pay;

import com.xl.canary.engine.action.IActionExecutor;
import com.xl.canary.engine.action.impl.EntryLaunchAction;
import com.xl.canary.engine.event.IEvent;
import com.xl.canary.engine.event.pay.DeductResponseEvent;
import com.xl.canary.engine.launcher.IEventLauncher;
import com.xl.canary.engine.state.IStateHandler;
import com.xl.canary.engine.state.StateHandler;
import com.xl.canary.entity.PayOrderEntity;
import com.xl.canary.enums.StateEnum;
import com.xl.canary.exception.InvalidEventException;
import com.xl.canary.service.BillService;
import com.xl.canary.service.LoanOrderService;
import com.xl.canary.service.PayOrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by gqwu on 2018/4/4.
 */
@Component
@StateHandler(name = StateEnum.DEDUCTING)
public class DeductingStateHandler implements IStateHandler<PayOrderEntity> {

    private static final Logger logger = LoggerFactory.getLogger(DeductingStateHandler.class);

    @Autowired
    private IEventLauncher loanOrderEventLauncher;

    @Autowired
    private PayOrderService payOrderService;

    @Autowired
    private LoanOrderService loanOrderService;

    @Autowired
    private BillService billService;

    @Override
    public PayOrderEntity handle(PayOrderEntity payOrder, IEvent event, IActionExecutor actionExecutor) throws InvalidEventException {

        if (event instanceof DeductResponseEvent) {
            DeductResponseEvent deductResponseEvent = (DeductResponseEvent) event;
            if (deductResponseEvent.isSucceeded()) {
                payOrder.setPayOrderState(StateEnum.DEDUCTED.name());
                payOrder.setPayNumber(deductResponseEvent.getActualDeducted());
                payOrder.setPayTime(deductResponseEvent.getEventTime());
                /** 此处相当于在扣款成功后，自动发起入账 */
                actionExecutor.append(new EntryLaunchAction(payOrder.getPayOrderId(), loanOrderEventLauncher, payOrderService, loanOrderService, billService));
            } else {
                payOrder.setPayOrderState(StateEnum.DEDUCT_FAILED.name());
            }
        } else {
            throw new InvalidEventException(
                    "还款订单状态与事件类型不匹配，状态：" + payOrder.getState() + "，事件：" + event);
        }

        return payOrder;
    }
}
