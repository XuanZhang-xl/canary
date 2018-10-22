package com.xl.canary.engine.event.loan;

import com.xl.canary.engine.event.AbstractStateEvent;
import com.xl.canary.engine.event.IStateEvent;

/**
 * Created by gqwu on 2018/4/4.
 * 放款命令
 */
public class LendLaunchEvent extends AbstractStateEvent implements IStateEvent {
    public LendLaunchEvent(String loanOrderId) {
        super(loanOrderId);
    }
}
