package com.xl.canary.engine.launcher.impl;

import com.xl.canary.engine.event.IStateEvent;
import com.xl.canary.engine.launcher.AbstractStateEventLauncher;
import com.xl.canary.engine.launcher.IEventLauncher;
import com.xl.canary.entity.PayOrderEntity;
import com.xl.canary.service.PayOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by gqwu on 2018/4/4.
 */
@Component("payOrderEventLauncher")
public class PayOrderEventLauncherImpl extends AbstractStateEventLauncher<PayOrderEntity, IStateEvent> implements IEventLauncher<IStateEvent> {

    @Autowired
    private PayOrderService payOrderService;

    @Override
    public PayOrderEntity selectEntity(String orderId) {
        return this.payOrderService.getByOrderId(orderId);
    }

    @Override
    public void saveEntity(PayOrderEntity entity) {
        entity.setUpdateTime(System.currentTimeMillis());
        this.payOrderService.save(entity);
    }
}
