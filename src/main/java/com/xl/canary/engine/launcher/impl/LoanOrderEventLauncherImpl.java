package com.xl.canary.engine.launcher.impl;

import com.xl.canary.engine.event.IStateEvent;
import com.xl.canary.engine.launcher.AbstractStateEventLauncher;
import com.xl.canary.engine.launcher.IEventLauncher;
import com.xl.canary.entity.LoanOrderEntity;
import com.xl.canary.service.LoanOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by gqwu on 2018/4/4.
 */
@Component("loanOrderEventLauncher")
public class LoanOrderEventLauncherImpl extends AbstractStateEventLauncher<LoanOrderEntity, IStateEvent> implements IEventLauncher<IStateEvent> {

    @Autowired
    private LoanOrderService loanOrderService;

    @Override
    public LoanOrderEntity selectEntity(String entityUniqueId) {
        return this.loanOrderService.getByOrderId(entityUniqueId);
    }

    @Override
    public void saveEntity(LoanOrderEntity entity) {
        entity.setUpdateTime(System.currentTimeMillis());
        loanOrderService.update(entity);
    }
}
