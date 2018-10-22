package com.xl.canary.engine.state.instalment;

import com.xl.canary.engine.action.IActionExecutor;
import com.xl.canary.engine.event.IEvent;
import com.xl.canary.engine.event.instalmet.InstalmentLentEvent;
import com.xl.canary.engine.event.order.CancelEvent;
import com.xl.canary.engine.event.loan.LendResponseEvent;
import com.xl.canary.engine.state.IStateHandler;
import com.xl.canary.engine.state.StateHandler;
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
@Component("instalmentPendingState")
@StateHandler(name = StateEnum.PENDING)
public class PendingStateHandler implements IStateHandler<LoanInstalmentEntity> {

    private static final Logger logger = LoggerFactory.getLogger(PendingStateHandler.class);

    @Autowired
    private LoanInstalmentService instalmentService;

    @Override
    public LoanInstalmentEntity handle(LoanInstalmentEntity instalment, IEvent event, IActionExecutor actionExecutor) throws InvalidEventException {
        if (event instanceof CancelEvent) {
            instalment.setInstalmentState(StateEnum.CANCELLED.name());
        } else if (event instanceof InstalmentLentEvent) {
            // 订单放宽成功后直接变为LENT
            InstalmentLentEvent instalmentLentEvent = (InstalmentLentEvent) event;
            instalment.setInstalmentState(StateEnum.LENT.name());
            instalmentService.update(instalment);
        } else {
            throw new InvalidEventException("贷款订单状态与事件类型不匹配，状态：" + instalment.getInstalmentState() + "，事件：" + event);
        }
        return instalment;
    }
}
