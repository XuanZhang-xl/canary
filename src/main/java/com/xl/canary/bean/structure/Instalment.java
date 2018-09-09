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
public class Instalment implements Map<LoanOrderElementEnum, Element>, Cloneable, Serializable {

    private final Map<LoanOrderElementEnum, Element> instalmentMap;

    /**
     * 当期的还款日
     */
    private long repaymentDate = -1L;

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
    public Element get(Object key) {
        return this.instalmentMap.get(key);
    }

    @Override
    public Element put(LoanOrderElementEnum key, Element value) {
        return this.instalmentMap.put(key, value);
    }

    @Override
    public Element remove(Object key) {
        return this.instalmentMap.remove(key);
    }

    @Override
    public void putAll(Map<? extends LoanOrderElementEnum, ? extends Element> m) {
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
    public Collection<Element> values() {
        return this.instalmentMap.values();
    }

    @Override
    public Set<Entry<LoanOrderElementEnum, Element>> entrySet() {
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
        this.instalmentMap = new HashMap<LoanOrderElementEnum, Element>();
    }

    public Instalment(Map<LoanOrderElementEnum, Element> instalmentMap) {
        this.instalmentMap = instalmentMap;
    }

    public long getRepaymentDate() {
        return repaymentDate;
    }

    public void setRepaymentDate(long repaymentDate) {
        this.repaymentDate = repaymentDate;
    }
}
