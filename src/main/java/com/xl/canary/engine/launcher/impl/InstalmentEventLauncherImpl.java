package com.xl.canary.engine.launcher.impl;

import com.xl.canary.engine.event.IStateEvent;
import com.xl.canary.engine.launcher.AbstractStateEventLauncher;
import com.xl.canary.engine.launcher.IEventLauncher;
import com.xl.canary.entity.LoanInstalmentEntity;
import com.xl.canary.entity.PayOrderEntity;
import com.xl.canary.mapper.LoanInstalmentMapper;
import com.xl.canary.mapper.PayOrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by xzhang on 2018/10/06.
 */
@Component("instalmentEventLauncher")
public class InstalmentEventLauncherImpl extends AbstractStateEventLauncher<LoanInstalmentEntity, IStateEvent> implements IEventLauncher<IStateEvent> {

    @Autowired
    private LoanInstalmentMapper instalmentMapper;

    @Override
    public LoanInstalmentEntity selectEntity(String instalmentId) {
        return this.instalmentMapper.getByInstalmentId(instalmentId);
    }

    @Override
    public void saveEntity(LoanInstalmentEntity entity) {
        entity.setUpdateTime(System.currentTimeMillis());
        this.instalmentMapper.updateByPrimaryKey(entity);
    }
}
