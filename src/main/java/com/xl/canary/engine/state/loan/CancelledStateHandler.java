package com.xl.canary.engine.state.loan;

import com.xl.canary.engine.action.IActionExecutor;
import com.xl.canary.engine.event.IEvent;
import com.xl.canary.engine.event.order.RecoveringEvent;
import com.xl.canary.engine.state.IStateHandler;
import com.xl.canary.engine.state.StateHandler;
import com.xl.canary.entity.LoanOrderEntity;
import com.xl.canary.enums.StatusEnum;
import com.xl.canary.exception.InvalidEventException;
import org.springframework.stereotype.Component;

/**
 * Created by gqwu on 2018/4/4.
 */
@Component("loanCancelledState")
@StateHandler(name = StatusEnum.CANCELLED)
public class CancelledStateHandler implements IStateHandler<LoanOrderEntity> {
    @Override
    public LoanOrderEntity handle(LoanOrderEntity loanOrder, IEvent event, IActionExecutor actionExecutor) throws Exception {

        if (event instanceof RecoveringEvent) {
            loanOrder.setOrderState(StatusEnum.PENDING.name());
        } else {
            throw new InvalidEventException("贷款订单状态与事件类型不匹配，状态：" + loanOrder.getState() + "，事件：" + event);
        }

        return loanOrder;
    }
}
