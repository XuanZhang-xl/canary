package com.xl.canary.engine.event.pay;

import com.xl.canary.engine.event.AbstractStateEvent;
import com.xl.canary.engine.event.IStateEvent;

/**
 * Created by gqwu on 2018/4/8.
 */
public class EntryLaunchEvent extends AbstractStateEvent implements IStateEvent {

    public EntryLaunchEvent(String payOrderId) {
        super(payOrderId);
    }
}
