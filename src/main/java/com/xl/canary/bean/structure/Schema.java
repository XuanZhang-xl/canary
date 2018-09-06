package com.xl.canary.bean.structure;

import com.alibaba.fastjson.JSONObject;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 账单基础结构
 * Created by xzhang on 2018/9/6.
 */
public class Schema implements Map<Integer, Instalment>, Cloneable, Serializable {

    private final Map<Integer, Instalment> schemaMap;

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

    public Schema() {
        this.schemaMap = new HashMap<Integer, Instalment>();
    }

    public Schema(Map<Integer, Instalment> schemaMap) {
        this.schemaMap = schemaMap;
    }
}
