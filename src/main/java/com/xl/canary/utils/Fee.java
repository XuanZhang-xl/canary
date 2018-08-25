package com.xl.canary.utils;

import com.xl.canary.enums.FeeAllocateEnum;

import java.math.BigDecimal;

/**
 * 绑定分期费用
 * created by XUAN on 2018/08/20
 */
public class Fee {

    /**
     * 分配方法
     */
    private String allocate;

    /**
     * 绑定到第几期, -1 表示每期都有
     */
    private Integer instalment = -1;

    /**
     * 元素名称
     */
    private String elementName;

    /**
     * 数量
     */
    private BigDecimal amount;

    public Fee(int instalment, FeeAllocateEnum allocate, String elementName, BigDecimal amount) {
        this.instalment = instalment;
        this.allocate = allocate.name();
        this.elementName = elementName;
        this.amount = amount;
    }

    public Fee(FeeAllocateEnum allocate, String elementName, BigDecimal amount) {
        this.allocate = allocate.name();
        this.elementName = elementName;
        this.amount = amount;
    }

    public String getAllocate() {
        return allocate;
    }

    public void setAllocate(String allocate) {
        this.allocate = allocate;
    }

    public Integer getInstalment() {
        return instalment;
    }

    public void setInstalment(Integer instalment) {
        this.instalment = instalment;
    }

    public String getElementName() {
        return elementName;
    }

    public void setElementName(String elementName) {
        this.elementName = elementName;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
