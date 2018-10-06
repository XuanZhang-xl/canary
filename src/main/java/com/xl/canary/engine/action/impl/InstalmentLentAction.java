package com.xl.canary.engine.action.impl;

import com.xl.canary.engine.action.IAction;
import com.xl.canary.engine.event.instalmet.InstalmentLentEvent;
import com.xl.canary.engine.launcher.IEventLauncher;
import com.xl.canary.entity.LoanInstalmentEntity;
import com.xl.canary.enums.ExecuteActionMethodEnum;
import com.xl.canary.service.LoanInstalmentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by gqwu on 2018/4/4.
 * 放款成功后, 更改分期状态
 */
public class InstalmentLentAction implements IAction {

    private static final Logger logger = LoggerFactory.getLogger(InstalmentLentAction.class);

    private String orderId;

    private LoanInstalmentService instalmentService;

    private IEventLauncher instalmentEventLauncher;

    public InstalmentLentAction(String orderId, LoanInstalmentService instalmentService, IEventLauncher instalmentEventLauncher) {
        this.orderId = orderId;
        this.instalmentService = instalmentService;
        this.instalmentEventLauncher = instalmentEventLauncher;
    }

    @Override
    public void run() {
        try {
            List<LoanInstalmentEntity> instalmentEntities = instalmentService.listInstalments(orderId);
            // 更改分期状态
            for (LoanInstalmentEntity instalmentEntity : instalmentEntities) {
                InstalmentLentEvent event = new InstalmentLentEvent(instalmentEntity.getInstalmentId(), "", "");
                instalmentEventLauncher.launch(event);
            }
        } catch (Exception e) {
            logger.error("下单后, orderId: {}放款后, 更新分期状态发生错误:", this.orderId, e);
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
        return orderId;
    }
}
