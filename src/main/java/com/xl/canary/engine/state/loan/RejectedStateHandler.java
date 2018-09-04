package com.xl.canary.engine.state.loan;

import com.xl.canary.engine.action.IActionExecutor;
import com.xl.canary.engine.action.impl.LoanAuditAction;
import com.xl.canary.engine.event.IEvent;
import com.xl.canary.engine.event.order.AuditLaunchEvent;
import com.xl.canary.engine.launcher.IEventLauncher;
import com.xl.canary.engine.state.IStateHandler;
import com.xl.canary.engine.state.StateHandler;
import com.xl.canary.entity.LoanOrderEntity;
import com.xl.canary.enums.StatusEnum;
import com.xl.canary.exception.InvalidEventException;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Created by gqwu on 2018/4/4.
 */
@Component("loanRejectedState")
@StateHandler(name = StatusEnum.REJECTED)
public class RejectedStateHandler implements IStateHandler<LoanOrderEntity> {

    @Resource(name = "loanOrderEventLauncher")
    private IEventLauncher loanOrderEventLauncher;

    @Override
    public LoanOrderEntity handle(LoanOrderEntity loanOrder, IEvent event, IActionExecutor actionExecutor) throws Exception {

        if (event instanceof AuditLaunchEvent) {
            loanOrder.setOrderState(StatusEnum.AUDITING.name());
            /** 自动审核模式的订单，则自动审核通过（目前没有业务审核），所以，附加执行一个审核通过的行为 */
            actionExecutor.append(new LoanAuditAction(loanOrder, loanOrderEventLauncher));
        } else {
            throw new InvalidEventException("贷款订单状态与事件类型不匹配，状态：" + loanOrder.getState() + "，事件：" + event);
        }

        return loanOrder;
    }
}