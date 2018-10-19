package com.xl.canary.engine.state.loan;

import com.xl.canary.bean.structure.Instalment;
import com.xl.canary.bean.structure.Schema;
import com.xl.canary.engine.action.IActionExecutor;
import com.xl.canary.engine.event.IEvent;
import com.xl.canary.engine.event.instalmet.InstalmentEntryEvent;
import com.xl.canary.engine.event.loan.LiquidateWaveBrokeEvent;
import com.xl.canary.engine.event.loan.SchemeEntryEvent;
import com.xl.canary.engine.launcher.IEventLauncher;
import com.xl.canary.engine.state.IStateHandler;
import com.xl.canary.engine.state.StateHandler;
import com.xl.canary.entity.LoanInstalmentEntity;
import com.xl.canary.entity.LoanOrderEntity;
import com.xl.canary.enums.StateEnum;
import com.xl.canary.exception.InvalidEventException;
import com.xl.canary.service.LoanInstalmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * Created by gqwu on 2018/4/4.
 */
@Component
@StateHandler(name = StateEnum.LENT)
public class LentStateHandler implements IStateHandler<LoanOrderEntity> {

    @Autowired
    private LoanInstalmentService instalmentService;

    @Autowired
    private IEventLauncher instalmentEventLauncher;

    @Override
    public LoanOrderEntity handle (LoanOrderEntity loanOrder, IEvent event, IActionExecutor actionExecutor) throws Exception {

        if (event instanceof SchemeEntryEvent) {
            SchemeEntryEvent entryEvent = (SchemeEntryEvent) event;
            String orderId = entryEvent.getUniqueId();
            Schema entrySchema = entryEvent.getEntrySchema();
            Schema realSchema = entryEvent.getRealSchema();
            List<LoanInstalmentEntity> instalmentEntities = instalmentService.listInstalments(orderId);
            for (LoanInstalmentEntity instalmentEntity : instalmentEntities) {
                Integer instalment = instalmentEntity.getInstalment();
                Instalment entryInstalment = entrySchema.get(instalment);
                if (entryInstalment != null) {
                    Instalment realInstalment = realSchema.get(instalment);
                    InstalmentEntryEvent instalmentEntryEvent = new InstalmentEntryEvent(instalmentEntity.getInstalmentId(), entryEvent.getPayOrder(), entryInstalment, realInstalment);
                    instalmentEventLauncher.launch(instalmentEntryEvent);
                }
            }
        } else if (event instanceof LiquidateWaveBrokeEvent) {

        } else {
            throw new InvalidEventException("贷款订单状态与事件类型不匹配，状态：" + loanOrder.getState() + "，事件：" + event);
        }
        return loanOrder;
    }
}
