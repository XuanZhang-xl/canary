package com.xl.canary.engine.event.order;

import com.xl.canary.engine.event.AbstractStateEvent;
import com.xl.canary.engine.event.IStateEvent;

/**
 * Created by gqwu on 2018/4/4.
 * 审核结果
 */
public class AuditResponseEvent extends AbstractStateEvent implements IStateEvent {

    private final boolean isPass;

    private final String explanation;

    public AuditResponseEvent(String orderId, boolean isPass, String explanation) {
        super(orderId);
        this.isPass = isPass;
        this.explanation = explanation;
    }

    public boolean isPass() {
        return isPass;
    }

    public String getExplanation() {
        return explanation;
    }
}
