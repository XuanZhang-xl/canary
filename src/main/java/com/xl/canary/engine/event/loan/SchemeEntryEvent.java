package com.xl.canary.engine.event.loan;

import com.xl.canary.bean.structure.Schema;
import com.xl.canary.engine.event.AbstractStateEvent;
import com.xl.canary.engine.event.IStateEvent;
import com.xl.canary.entity.PayOrderEntity;

/**
 * Created by gqwu on 2018/4/4.
 * 单一订单入账明细事件
 */
public class SchemeEntryEvent extends AbstractStateEvent implements IStateEvent {

    /**
     * 入账schema
     */
    private final Schema entrySchema;

    /**
     * 实际应还schema
     */
    private final Schema realSchema;

    /**
     * 还款订单, 带上它是因为可能订单入账需要用到还款订单的参数
     */
    private final PayOrderEntity payOrder;

    public SchemeEntryEvent(String orderId, PayOrderEntity payOrder, Schema entrySchema, Schema realSchema) {
        super(orderId);
        this.entrySchema = entrySchema;
        this.realSchema = realSchema;
        this.payOrder = payOrder;
    }

    public Schema getEntrySchema() {
        return entrySchema;
    }

    public Schema getRealSchema() {
        return realSchema;
    }

    public PayOrderEntity getPayOrder() {
        return payOrder;
    }
}
