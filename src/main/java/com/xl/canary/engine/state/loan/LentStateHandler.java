package com.xl.canary.engine.state.loan;

import com.xl.canary.engine.action.IActionExecutor;
import com.xl.canary.engine.event.IEvent;
import com.xl.canary.engine.event.order.loan.LiquidateWaveBrokeEvent;
import com.xl.canary.engine.event.order.loan.SchemeEntryEvent;
import com.xl.canary.engine.state.IStateHandler;
import com.xl.canary.engine.state.StateHandler;
import com.xl.canary.entity.LoanOrderEntity;
import com.xl.canary.enums.StateEnum;
import com.xl.canary.exception.InvalidEventException;
import org.springframework.stereotype.Component;

/**
 * Created by gqwu on 2018/4/4.
 */
@Component
@StateHandler(name = StateEnum.LENT)
public class LentStateHandler implements IStateHandler<LoanOrderEntity> {


    @Override
    public LoanOrderEntity handle (LoanOrderEntity loanOrder, IEvent event, IActionExecutor actionExecutor) throws InvalidEventException {

        if (event instanceof SchemeEntryEvent) {

        } else if (event instanceof LiquidateWaveBrokeEvent) {

        } else {
            throw new InvalidEventException("贷款订单状态与事件类型不匹配，状态：" + loanOrder.getState() + "，事件：" + event);
        }
        return loanOrder;
    }
}
