package com.xl.canary.engine.state.instalment;

import com.xl.canary.engine.action.IActionExecutor;
import com.xl.canary.engine.calculate.SchemaCalculator;
import com.xl.canary.engine.calculate.impl.InstalmentCalculatorImpl;
import com.xl.canary.engine.event.IEvent;
import com.xl.canary.engine.event.instalmet.InstalmentEntryEvent;
import com.xl.canary.engine.event.instalmet.InstalmentLentEvent;
import com.xl.canary.engine.event.order.CancelEvent;
import com.xl.canary.engine.state.IStateHandler;
import com.xl.canary.engine.state.StateHandler;
import com.xl.canary.entity.ISchemaEntity;
import com.xl.canary.entity.LoanInstalmentEntity;
import com.xl.canary.enums.StateEnum;
import com.xl.canary.exception.InvalidEventException;
import com.xl.canary.service.LoanInstalmentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by gqwu on 2018/4/4.
 */
@Component("instalmentLentState")
@StateHandler(name = StateEnum.LENT)
public class LentStateHandler implements IStateHandler<LoanInstalmentEntity> {

    private static final Logger logger = LoggerFactory.getLogger(LentStateHandler.class);

    @Autowired
    private InstalmentCalculatorImpl instalmentCalculatorImpl;

    @Override
    public LoanInstalmentEntity handle(LoanInstalmentEntity instalment, IEvent event, IActionExecutor actionExecutor) throws Exception {
        if (event instanceof InstalmentEntryEvent) {
            InstalmentEntryEvent instalmentEntryEvent = (InstalmentEntryEvent) event;
            ISchemaEntity entryInstalment = instalmentCalculatorImpl.entryInstalment(instalment, instalmentEntryEvent.getPayOrder(), instalmentEntryEvent.getEntryInstalment(), instalmentEntryEvent.getRealInstalment());
        } else {
            throw new InvalidEventException("贷款订单状态与事件类型不匹配，状态：" + instalment.getInstalmentState() + "，事件：" + event);
        }
        return instalment;
    }
}
