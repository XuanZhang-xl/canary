package com.xl.canary.engine.action.impl;

import com.xl.canary.bean.structure.Instalment;
import com.xl.canary.bean.structure.Schema;
import com.xl.canary.engine.action.IAction;
import com.xl.canary.engine.event.instalmet.InstalmentEntryEvent;
import com.xl.canary.engine.event.loan.LoanOrderEntryEvent;
import com.xl.canary.engine.event.pay.EntryLaunchEvent;
import com.xl.canary.engine.launcher.IEventLauncher;
import com.xl.canary.entity.LoanInstalmentEntity;
import com.xl.canary.entity.LoanOrderEntity;
import com.xl.canary.entity.PayOrderEntity;
import com.xl.canary.enums.BillTypeEnum;
import com.xl.canary.enums.ExecuteActionMethodEnum;
import com.xl.canary.enums.StateEnum;
import com.xl.canary.service.BillService;
import com.xl.canary.service.LoanInstalmentService;
import com.xl.canary.service.LoanOrderService;
import com.xl.canary.service.PayOrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class EntryLaunchAction implements IAction {

    private static final Logger logger = LoggerFactory.getLogger(EntryLaunchAction.class);

    private String payOrderId;

    private BillService billService;

    public EntryLaunchAction(String payOrderId, BillService billService) {
        this.payOrderId = payOrderId;
        this.billService = billService;
    }

    @Override
    public void run() {
        try {
            billService.batchEntry(payOrderId);
        } catch (Exception e) {
            logger.error("自动发送发起入账事件异常，payOrderId：[{}]", payOrderId, e);
        }
    }

    @Override
    public String getActionName() {
        return this.getClass().getName();    }

    @Override
    public String getActionType() {
        return ExecuteActionMethodEnum.NEW_THREAD.name();
    }

    @Override
    public String getUniqueId() {
        return payOrderId;
    }
}
