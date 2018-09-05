package com.xl.canary.engine.launcher.impl;

import com.xl.canary.engine.event.IStateEvent;
import com.xl.canary.engine.launcher.AbstractStateEventLauncher;
import com.xl.canary.engine.launcher.IEventLauncher;
import com.xl.canary.entity.LoanOrderEntity;
import com.xl.canary.mapper.LoanOrderMapper;
import com.xl.canary.service.LoanOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by gqwu on 2018/4/4.
 */
@Component("loanOrderEventLauncher")
public class LoanOrderEventLauncherImpl extends AbstractStateEventLauncher<LoanOrderEntity, IStateEvent> implements IEventLauncher<IStateEvent> {

    @Autowired
    private LoanOrderMapper loanOrderMapper;

    @Override
    public LoanOrderEntity selectEntity(String entityUniqueId) {
        return this.loanOrderMapper.getByOrderId(entityUniqueId);
    }

    @Override
    public void saveEntity(LoanOrderEntity entity) {
        entity.setUpdateTime(System.currentTimeMillis());
        loanOrderMapper.updateByPrimaryKey(entity);
    }
}
