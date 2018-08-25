package com.xl.canary.engine.state.pay;

import com.xl.canary.engine.action.IActionExecutor;
import com.xl.canary.engine.action.impl.PayAuditAction;
import com.xl.canary.engine.event.IEvent;
import com.xl.canary.engine.event.order.AuditLaunchEvent;
import com.xl.canary.engine.event.order.CancelEvent;
import com.xl.canary.engine.event.order.OrderExpireEvent;
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

import javax.annotation.Resource;

/**
 * Created by gqwu on 2018/4/4.
 */
@Component
@StateHandler(name = StatusEnum.PENDING)
public class PendingStateHandler implements IStateHandler<PayOrderEntity> {

    private static final Logger logger = LoggerFactory.getLogger(PendingStateHandler.class);

    @Resource(name = "payOrderEventLauncher")
    private IEventLauncher payOrderEventLauncher;

    @Autowired
    private PayOrderService payOrderService;

    @Override
    public PayOrderEntity handle(PayOrderEntity payOrder, IEvent event, IActionExecutor actionExecutor) throws InvalidEventException {

        if (event instanceof CancelEvent) {
            payOrder.setPayOrderState(StatusEnum.CANCELLED.name());

        } else if (event instanceof AuditLaunchEvent) {

            AuditLaunchEvent auditLaunchEvent = (AuditLaunchEvent) event;

            if (!auditLaunchEvent.getUserCode().equals(payOrder.getUserCode())) {

                logger.error("用户不是该还款订单的拥有者，事件所属用户：[{}]，订单：[{}]，订单所属用户：[{}]",
                        auditLaunchEvent.getUserCode(), payOrder.getPayOrderId(), payOrder.getUserCode());

                throw new InvalidEventException("用户不是该还款订单的拥有者，事件所属用户：" + auditLaunchEvent.getUserCode()
                        + "，订单：" + payOrder.getPayOrderId() + "，订单所属用户：" + payOrder.getUserCode());
            }

            payOrder.setPayOrderState(StatusEnum.AUDITING.name());
            /** 还款订单审核 */
            actionExecutor.append(new PayAuditAction(payOrder, payOrderEventLauncher));
        } else if (event instanceof OrderExpireEvent) {
            payOrder.setPayOrderState(StatusEnum.EXPIRED.name());
        } else {
            throw new InvalidEventException("还款订单状态与事件类型不匹配，状态：" + payOrder.getState() + "，事件：" + event);
        }

        return payOrder;
    }
}
