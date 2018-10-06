package com.xl.canary.engine.event.instalmet;

import com.xl.canary.engine.event.AbstractStateEvent;
import com.xl.canary.engine.event.IStateEvent;

import java.math.BigDecimal;

/**
 * Created by gqwu on 2018/4/4.
 */
public class InstalmentLentEvent extends AbstractStateEvent implements IStateEvent {

    private final String lendSignature;


    private final String explanation;

    public InstalmentLentEvent(String instalmentId, String lendSignature, String explanation) {
        super(instalmentId);
        this.explanation = explanation;
        this.lendSignature = lendSignature;
    }

    public String getExplanation() {
        return explanation;
    }

    public String getLendSignature() {
        return lendSignature;
    }
}
