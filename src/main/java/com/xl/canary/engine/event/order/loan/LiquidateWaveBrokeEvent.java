package com.xl.canary.engine.event.order.loan;

import com.xl.canary.engine.event.AbstractStateEvent;
import com.xl.canary.engine.event.IStateEvent;

/**
 * Created by xzhang on 2018/8/8.
 * 破产清事件
 */
public class LiquidateWaveBrokeEvent extends AbstractStateEvent implements IStateEvent {

    private String payOrderType;

    public LiquidateWaveBrokeEvent(String loanOrderId, String payOrderType) {
        super(loanOrderId);
        this.payOrderType = payOrderType;
    }

    public String getPayOrderType() {
        return payOrderType;
    }

    public void setPayOrderType(String payOrderType) {
        this.payOrderType = payOrderType;
    }
}
