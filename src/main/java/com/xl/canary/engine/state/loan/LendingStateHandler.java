package com.xl.canary.engine.state.loan;

import com.xl.canary.engine.action.IActionExecutor;
import com.xl.canary.engine.action.impl.InstalmentLentAction;
import com.xl.canary.engine.event.IEvent;
import com.xl.canary.engine.event.loan.LendResponseEvent;
import com.xl.canary.engine.launcher.IEventLauncher;
import com.xl.canary.engine.state.IStateHandler;
import com.xl.canary.engine.state.StateHandler;
import com.xl.canary.entity.LoanInstalmentEntity;
import com.xl.canary.entity.LoanOrderEntity;
import com.xl.canary.enums.StateEnum;
import com.xl.canary.exception.InvalidEventException;
import com.xl.canary.service.LoanInstalmentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by gqwu on 2018/4/4.
 */
@Component
@StateHandler(name = StateEnum.LENDING)
public class LendingStateHandler implements IStateHandler<LoanOrderEntity> {

    private static final Logger logger = LoggerFactory.getLogger(LendingStateHandler.class);

    @Autowired
    private LoanInstalmentService loanInstalmentService;

    @Autowired
    private IEventLauncher instalmentEventLauncher;

    @Override
    public LoanOrderEntity handle(LoanOrderEntity loanOrder, IEvent event, IActionExecutor actionExecutor) throws Exception {

        if (event instanceof LendResponseEvent) {
            LendResponseEvent lendResponseEvent = (LendResponseEvent) event;

            BigDecimal lentNumber = lendResponseEvent.getActualLent();
            if (lendResponseEvent.isSucceeded()) {
                loanOrder.setOrderState(StateEnum.LENT.name());
                loanOrder.setLentAmount(lentNumber);
                loanOrder.setLentTime(lendResponseEvent.getSuccessTime());

                // 放款成功后, 生成账单
                List<LoanInstalmentEntity> loanInstalmentEntities = loanInstalmentService.generateInstalments(loanOrder);
                loanInstalmentService.saveLoanInstalments(loanInstalmentEntities);

                // 更改分期状态
                actionExecutor.append(new InstalmentLentAction(loanOrder.getOrderId(), loanInstalmentService, instalmentEventLauncher));
            } else {
                loanOrder.setOrderState(StateEnum.FAILED.name());
            }
        } else {
            throw new InvalidEventException("贷款订单状态与事件类型不匹配，状态：" + loanOrder.getState() + "，事件：" + event);
        }
        return loanOrder;
    }
}
