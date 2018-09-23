package com.xl.canary.bean.structure;

import com.alibaba.fastjson.JSONObject;
import com.xl.canary.enums.BillTypeEnum;
import com.xl.canary.enums.pay.PayTypeEnum;
import com.xl.canary.utils.TimeUtils;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 账单基础结构
 * 销账目标, 值都为正
 * 销账来源, 值都为负
 *
 * 一个schema的值可能有正有负, 比如策略和优惠券
 *
 * Created by xzhang on 2018/9/6.
 */
public class Schema implements Map<Integer, Instalment>, Cloneable, Serializable {

    private final Map<Integer, Instalment> schemaMap;

    /**
     * schema的类型
     */
    private BillTypeEnum schemaType;

    /**
     * 获取当前用户还款时的还款类型
     * TODO: HashMap 可能是乱排的, 这样获取对不对还要测试下
     * @return
     */
    public PayTypeEnum getPayType () {
        for (Entry<Integer, Instalment> entry : this.schemaMap.entrySet()) {
            long repaymentDate = entry.getValue().getRepaymentDate();
            long now = TimeUtils.truncateToDay(System.currentTimeMillis());
            if (repaymentDate < now) {
                return PayTypeEnum.REPAY_OVERDUE;
            } else if (repaymentDate == now) {
                return PayTypeEnum.REPAY_AS_PLAN;
            }
        }
        return PayTypeEnum.REPAY_IN_ADVANCE;
    }

    public Schema() {
        this.schemaMap = new HashMap<Integer, Instalment>();
    }

    public Schema(Map<Integer, Instalment> schemaMap) {
        this.schemaMap = schemaMap;
    }

    @Override
    public int size() {
        return this.schemaMap.size();
    }

    @Override
    public boolean isEmpty() {
        return this.schemaMap.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return this.schemaMap.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return this.schemaMap.containsValue(value);
    }

    @Override
    public Instalment get(Object key) {
        return this.schemaMap.get(key);
    }

    @Override
    public Instalment put(Integer key, Instalment value) {
        return this.schemaMap.put(key, value);
    }

    @Override
    public Instalment remove(Object key) {
        return this.schemaMap.remove(key);
    }

    @Override
    public void putAll(Map<? extends Integer, ? extends Instalment> m) {
        this.schemaMap.putAll(m);
    }

    @Override
    public void clear() {
        this.schemaMap.clear();
    }

    @Override
    public Set<Integer> keySet() {
        return this.schemaMap.keySet();
    }

    @Override
    public Collection<Instalment> values() {
        return this.schemaMap.values();
    }

    @Override
    public Set<Entry<Integer, Instalment>> entrySet() {
        return this.schemaMap.entrySet();
    }

    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }

    @Override
    public Schema clone() {
        return new Schema(this.schemaMap);
    }

    @Override
    public boolean equals(Object obj) {
        return this.schemaMap.equals(obj);
    }

    @Override
    public int hashCode() {
        return this.schemaMap.hashCode();
    }

    public BillTypeEnum getSchemaType() {
        return schemaType;
    }

    public void setSchemaType(BillTypeEnum schemaType) {
        this.schemaType = schemaType;
    }
}
