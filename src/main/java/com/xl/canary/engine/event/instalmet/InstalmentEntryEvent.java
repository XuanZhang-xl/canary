package com.xl.canary.engine.event.instalmet;

import com.xl.canary.bean.structure.Instalment;
import com.xl.canary.engine.event.AbstractStateEvent;
import com.xl.canary.engine.event.IStateEvent;
import com.xl.canary.entity.PayOrderEntity;

/**
 * Created by gqwu on 2018/4/4.
 */
public class InstalmentEntryEvent extends AbstractStateEvent implements IStateEvent {

    /**
     * 入账
     */
    private final Instalment entryInstalment;

    /**
     * 实际应还
     */
    private final Instalment realInstalment;

    /**
     * 还款订单
     */
    private final PayOrderEntity payOrder;

    public InstalmentEntryEvent(String instalmentId, PayOrderEntity payOrder, Instalment entryInstalment, Instalment realInstalment) {
        super(instalmentId);
        this.entryInstalment = entryInstalment;
        this.realInstalment = realInstalment;
        this.payOrder = payOrder;
    }

    public Instalment getEntryInstalment() {
        return entryInstalment;
    }

    public Instalment getRealInstalment() {
        return realInstalment;
    }

    public PayOrderEntity getPayOrder() {
        return payOrder;
    }
}
