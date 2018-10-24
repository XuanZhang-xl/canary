package com.xl.canary.engine.event.pay;

import com.xl.canary.engine.event.AbstractStateEvent;
import com.xl.canary.engine.event.IStateEvent;

import java.math.BigDecimal;

/**
 * Created by gqwu on 2018/4/8.
 */
public class DeductResponseEvent extends AbstractStateEvent implements IStateEvent {

    private final boolean succeeded;

    private final BigDecimal actualDeducted;

    private final String explanation;

    public DeductResponseEvent(String payOrderId, BigDecimal actualDeducted,
                               boolean succeeded, String explanation) {
        super(payOrderId);
        this.actualDeducted = actualDeducted;
        this.succeeded = succeeded;
        this.explanation = explanation;
    }

    public boolean isSucceeded() {
        return succeeded;
    }

    public BigDecimal getActualDeducted() {
        return actualDeducted;
    }

    public String getExplanation() {
        return explanation;
    }
}