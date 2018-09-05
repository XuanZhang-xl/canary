package com.xl.canary.engine.state.loan;

import com.xl.canary.engine.action.IActionExecutor;
import com.xl.canary.engine.action.impl.LoanAuditAction;
import com.xl.canary.engine.event.IEvent;
import com.xl.canary.engine.event.order.AuditLaunchEvent;
import com.xl.canary.engine.event.order.CancelEvent;
import com.xl.canary.engine.launcher.IEventLauncher;
import com.xl.canary.engine.state.IStateHandler;
import com.xl.canary.engine.state.StateHandler;
import com.xl.canary.entity.LoanOrderEntity;
import com.xl.canary.enums.StateEnum;
import com.xl.canary.exception.InvalidEventException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Created by gqwu on 2018/4/4.
 */
@Component("loanPendingState")
@StateHandler(name = StateEnum.PENDING)
public class PendingStateHandler implements IStateHandler<LoanOrderEntity> {

    private static final Logger logger = LoggerFactory.getLogger(PendingStateHandler.class);

    @Resource(name = "loanOrderEventLauncher")
    private IEventLauncher loanOrderEventLauncher;

    @Override
    public LoanOrderEntity handle(LoanOrderEntity loanOrder, IEvent event, IActionExecutor actionExecutor) throws InvalidEventException {
        if (event instanceof CancelEvent) {
            loanOrder.setOrderState(StateEnum.CANCELLED.name());
        } else if (event instanceof AuditLaunchEvent) {
            AuditLaunchEvent auditLaunchEvent = (AuditLaunchEvent) event;
            if (!auditLaunchEvent.getUserCode().equals(loanOrder.getUserCode())) {
                logger.error("用户不是该借贷订单的拥有者，事件所属用户：[{}]，订单：[{}]，订单所属用户：[{}]", auditLaunchEvent.getUserCode(), loanOrder.getOrderId(), loanOrder.getUserCode());
                throw new InvalidEventException("用户不是该还款订单的拥有者，事件所属用户：" + auditLaunchEvent.getUserCode() + "，订单：" + loanOrder.getOrderId() + "，订单所属用户：" + loanOrder.getUserCode());
            }
            loanOrder.setOrderState(StateEnum.AUDITING.name());
            /**
             * 附加执行一个审核通过的行为
             * */
            actionExecutor.append(new LoanAuditAction(loanOrder, loanOrderEventLauncher));
        } else {
            throw new InvalidEventException("贷款订单状态与事件类型不匹配，状态：" + loanOrder.getState() + "，事件：" + event);
        }
        return loanOrder;
    }
}
