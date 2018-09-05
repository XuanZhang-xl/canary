package com.xl.canary.engine.state.loan;

import com.xl.canary.engine.action.IActionExecutor;
import com.xl.canary.engine.action.impl.LendLaunchAction;
import com.xl.canary.engine.event.IEvent;
import com.xl.canary.engine.event.order.AuditResponseEvent;
import com.xl.canary.engine.event.order.CancelEvent;
import com.xl.canary.engine.event.order.OrderExpireEvent;
import com.xl.canary.engine.launcher.IEventLauncher;
import com.xl.canary.engine.state.IStateHandler;
import com.xl.canary.engine.state.StateHandler;
import com.xl.canary.entity.LoanOrderEntity;
import com.xl.canary.enums.loan.LendModeEnum;
import com.xl.canary.enums.StateEnum;
import com.xl.canary.exception.InvalidEventException;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Created by gqwu on 2018/4/4.
 */
@Component("loanAuditingState")
@StateHandler(name = StateEnum.AUDITING)
public class AuditingStateHandler implements IStateHandler<LoanOrderEntity> {

    @Resource(name = "loanOrderEventLauncher")
    private IEventLauncher loanOrderEventLauncher;

    @Override
    public LoanOrderEntity handle(LoanOrderEntity loanOrder, IEvent event, IActionExecutor actionExecutor) throws Exception {

        if (event instanceof CancelEvent) {
            loanOrder.setOrderState(StateEnum.CANCELLED.name());
        } else if (event instanceof AuditResponseEvent) {
            AuditResponseEvent auditResponseEvent = (AuditResponseEvent) event;
            if (auditResponseEvent.isPass()) {
                //审核成功
                loanOrder.setOrderState(StateEnum.PASSED.name());
                if (LendModeEnum.AUTO.name().equals(loanOrder.getLendMode())) {
                    /** 自动放款模式下，则附加发起放款 */
                    actionExecutor.append(new LendLaunchAction(loanOrder.getOrderId(), loanOrderEventLauncher));
                }
            } else {
                loanOrder.setOrderState(StateEnum.REJECTED.name());
            }
        } else if (event instanceof OrderExpireEvent) {
            loanOrder.setOrderState(StateEnum.EXPIRED.name());
        } else {
            throw new InvalidEventException("贷款订单状态与事件类型不匹配，状态：" + loanOrder.getState() + "，事件：" + event);
        }
        return loanOrder;
    }
}
