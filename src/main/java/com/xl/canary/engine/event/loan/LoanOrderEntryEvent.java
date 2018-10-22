package com.xl.canary.engine.event.loan;

import com.xl.canary.bean.structure.Schema;
import com.xl.canary.engine.event.AbstractStateEvent;
import com.xl.canary.engine.event.IStateEvent;
import com.xl.canary.entity.PayOrderEntity;

/**
 * Created by gqwu on 2018/4/4.
 * 单一订单入账明细事件
 */
public class LoanOrderEntryEvent extends AbstractStateEvent implements IStateEvent {

    /**
     * 入账schema
     */
    private final Boolean isPayoff;

    // 需要的话再添加

    public LoanOrderEntryEvent(String orderId, Boolean isPayoff) {
        super(orderId);
        this.isPayoff = isPayoff;
    }

    public Boolean getPayoff() {
        return isPayoff;
    }
}
