package com.xl.canary.engine.action.impl;

import com.xl.canary.bean.structure.Schema;
import com.xl.canary.engine.action.IAction;
import com.xl.canary.engine.event.loan.SchemeEntryEvent;
import com.xl.canary.engine.event.pay.EntryLaunchEvent;
import com.xl.canary.engine.launcher.IEventLauncher;
import com.xl.canary.entity.LoanOrderEntity;
import com.xl.canary.entity.PayOrderEntity;
import com.xl.canary.enums.BillTypeEnum;
import com.xl.canary.enums.ExecuteActionMethodEnum;
import com.xl.canary.enums.StateEnum;
import com.xl.canary.service.BillService;
import com.xl.canary.service.LoanOrderService;
import com.xl.canary.service.PayOrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class EntryLaunchAction implements IAction {

    private static final Logger logger = LoggerFactory.getLogger(EntryLaunchAction.class);

    private String payOrderId;

    private IEventLauncher loanOrderEventLauncher;

    private PayOrderService payOrderService;

    private LoanOrderService loanOrderService;

    private BillService billService;

    public EntryLaunchAction(String payOrderId, IEventLauncher loanOrderEventLauncher, PayOrderService payOrderService, LoanOrderService loanOrderService, BillService billService) {
        this.payOrderId = payOrderId;
        this.loanOrderEventLauncher = loanOrderEventLauncher;
        this.payOrderService = payOrderService;
        this.loanOrderService = loanOrderService;
        this.billService = billService;
    }

    @Override
    public void run() {
        EntryLaunchEvent entryLaunchEvent = new EntryLaunchEvent(payOrderId);
        try {
            PayOrderEntity payOrder = payOrderService.getByPayOrderId(payOrderId);
            List<LoanOrderEntity> loanOrderEntities = loanOrderService.listByUserCode(payOrder.getUserCode(), StateEnum.lent);
            for (LoanOrderEntity loanOrderEntity : loanOrderEntities) {

                Schema entrySchema = billService.payLoanOrder(loanOrderEntity, payOrder);
                // 获得订单schema, 入账
                Schema orderEntrySchema = entrySchema.distinguishByDestination(BillTypeEnum.LOAN_ORDER);
                Schema realSchema = billService.payOffLoanOrder(loanOrderEntity);
                SchemeEntryEvent event = new SchemeEntryEvent(loanOrderEntity.getOrderId(), payOrder, orderEntrySchema, realSchema);
                loanOrderEventLauncher.launch(event);
            }
        } catch (Exception e) {
            logger.error("自动发送发起入账事件异常，事件：[{}]", entryLaunchEvent, e);
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
