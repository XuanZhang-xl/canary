package com.xl.canary.engine.event.order.loan;

import com.xl.canary.engine.event.AbstractStateEvent;
import com.xl.canary.engine.event.IStateEvent;

/**
 * Created by gqwu on 2018/4/4.
 * 单一订单入账明细事件
 */
public class SchemeEntryEvent extends AbstractStateEvent implements IStateEvent {
    // TODO: 入账事件
    public SchemeEntryEvent(String entityUniqueId) {
        super(entityUniqueId);
    }

    //private final Scheme entryScheme;
    //
    //private final String entryType;
    //
    //public SchemeEntryEvent(String loanOrderId, String entryType, Scheme entryScheme) {
    //    super(loanOrderId);
    //    this.entryType = entryType;
    //    this.entryScheme = entryScheme;
    //}
    //
    //public String getEntryType () {
    //    return this.entryType;
    //}
    //
    //public Scheme getEntryScheme() {
    //    return entryScheme;
    //}
}
