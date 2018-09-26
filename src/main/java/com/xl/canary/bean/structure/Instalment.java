package com.xl.canary.bean.structure;

import com.alibaba.fastjson.JSONObject;
import com.xl.canary.enums.loan.LoanOrderElementEnum;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 每期账单
 * Created by xzhang on 2018/9/6.
 */
public class Instalment implements Map<LoanOrderElementEnum, Unit>, Cloneable, Serializable {

    private final Map<LoanOrderElementEnum, Unit> instalmentMap;

    /**
     * 当期的还款日
     */
    private long repaymentDate = -1L;

    /**
     * 当前Instalment反转
     */
    public void reverse() {
        for (Entry<LoanOrderElementEnum, Unit> entry : this.entrySet()) {
            Unit unit = entry.getValue();
            for (Element element : unit) {
                element.reverse();
            }
        }
    }

    @Override
    public int size() {
        return this.instalmentMap.size();
    }

    @Override
    public boolean isEmpty() {
        return this.instalmentMap.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return this.instalmentMap.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return this.instalmentMap.containsValue(value);
    }

    @Override
    public Unit get(Object key) {
        return this.instalmentMap.get(key);
    }

    @Override
    public Unit put(LoanOrderElementEnum key, Unit value) {
        return this.instalmentMap.put(key, value);
    }

    @Override
    public Unit remove(Object key) {
        return this.instalmentMap.remove(key);
    }

    @Override
    public void putAll(Map<? extends LoanOrderElementEnum, ? extends Unit> m) {
        this.instalmentMap.putAll(m);
    }

    @Override
    public void clear() {
        this.instalmentMap.clear();
    }

    @Override
    public Set<LoanOrderElementEnum> keySet() {
        return this.instalmentMap.keySet();
    }

    @Override
    public Collection<Unit> values() {
        return this.instalmentMap.values();
    }

    @Override
    public Set<Entry<LoanOrderElementEnum, Unit>> entrySet() {
        return this.instalmentMap.entrySet();
    }

    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }

    @Override
    public Instalment clone() {
        return new Instalment(this.instalmentMap);
    }

    @Override
    public boolean equals(Object obj) {
        return this.instalmentMap.equals(obj);
    }

    @Override
    public int hashCode() {
        return this.instalmentMap.hashCode();
    }

    public Instalment() {
        this.instalmentMap = new HashMap<LoanOrderElementEnum, Unit>();
    }

    public Instalment(Map<LoanOrderElementEnum, Unit> instalmentMap) {
        this.instalmentMap = instalmentMap;
    }

    public long getRepaymentDate() {
        return repaymentDate;
    }

    public void setRepaymentDate(long repaymentDate) {
        this.repaymentDate = repaymentDate;
    }
}
