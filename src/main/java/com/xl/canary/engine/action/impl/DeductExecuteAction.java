package com.xl.canary.engine.action.impl;
import com.xl.canary.engine.action.IAction;
import com.xl.canary.engine.event.pay.DeductResponseEvent;
import com.xl.canary.engine.launcher.IEventLauncher;
import com.xl.canary.entity.PayOrderEntity;
import com.xl.canary.enums.ExecuteActionMethodEnum;
import com.xl.canary.service.PayOrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DeductExecuteAction implements IAction {

    private static final Logger logger = LoggerFactory.getLogger(DeductExecuteAction.class);

    private String payOrderId;

    private PayOrderService payOrderService;

    private IEventLauncher payOrderEventLauncher;

    public DeductExecuteAction(String payOrderId, PayOrderService payOrderService, IEventLauncher payOrderEventLauncher) {
        this.payOrderId = payOrderId;
        this.payOrderService = payOrderService;
        this.payOrderEventLauncher = payOrderEventLauncher;
    }

    @Override
    public void run() {
        /**
         * 进入真实扣款逻辑
         * 发送mq, 等待对方mq
         */
        PayOrderEntity payOrder = payOrderService.getByPayOrderId(payOrderId);

        try {
            /**
             * 直接模拟扣款成功事件
             */
            DeductResponseEvent event = new DeductResponseEvent(payOrderId, payOrder.getApplyAmount(), true, "扣款成功");
            payOrderEventLauncher.launch(event);
        } catch (Exception e) {
            logger.error("扣款结果事件失败  ", e);
        }
    }

    @Override
    public String getActionName() {
        return this.getClass().getName();
    }

    @Override
    public String getActionType() {
        return ExecuteActionMethodEnum.CONTINUE.name();
    }

    @Override
    public String getUniqueId() {
        return payOrderId;
    }
}
