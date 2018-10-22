package com.xl.canary.engine.state.loan;

import com.xl.canary.engine.action.IActionExecutor;
import com.xl.canary.engine.action.impl.LendExecuteAction;
import com.xl.canary.engine.event.IEvent;
import com.xl.canary.engine.event.loan.LendLaunchEvent;
import com.xl.canary.engine.launcher.IEventLauncher;
import com.xl.canary.engine.state.IStateHandler;
import com.xl.canary.engine.state.StateHandler;
import com.xl.canary.entity.LoanOrderEntity;
import com.xl.canary.enums.loan.LendModeEnum;
import com.xl.canary.enums.StateEnum;
import com.xl.canary.exception.InvalidEventException;
import com.xl.canary.service.LoanOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by gqwu on 2018/4/4.
 */
@Component
@StateHandler(name = StateEnum.FAILED)
public class FailedStateHandler implements IStateHandler<LoanOrderEntity> {

    @Autowired
    LoanOrderService loanOrderService;

    @Autowired
    IEventLauncher loanOrderEventLauncher;

    @Override
    public LoanOrderEntity handle(LoanOrderEntity loanOrder, IEvent event, IActionExecutor actionExecutor) throws InvalidEventException {

        if (event instanceof LendLaunchEvent) {
            loanOrder.setOrderState(StateEnum.LENDING.name());
            /** 自动放款模式订单，附加执行实际放款行为（通知钱包放款）*/
            if (loanOrder.getLendMode().equals(LendModeEnum.AUTO.name())) {
                actionExecutor.append(new LendExecuteAction(loanOrder.getOrderId(), loanOrderService, loanOrderEventLauncher));
            }
        } else {
            throw new InvalidEventException("贷款订单状态与事件类型不匹配，状态：" + loanOrder.getState() + "，事件：" + event);
        }

        return loanOrder;
    }
}
