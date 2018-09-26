package com.xl.canary.bean.structure;

import com.xl.canary.enums.BillTypeEnum;
import com.xl.canary.enums.loan.LoanOrderElementEnum;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by xzhang on 2018/9/6.
 */
public class Element implements Cloneable, Serializable {

    /**
     * 元素
     */
    private LoanOrderElementEnum element;

    /**
     * 来源
     */
    private BillTypeEnum source;

    /**
     * 来源id
     */
    private String sourceId;

    /**
     * 目标
     */
    private BillTypeEnum destination;

    /**
     * 目标id
     */
    private String destinationId;

    /**
     * 销帐数量
     */
    private BigDecimal amount = BigDecimal.ZERO;

    /**
     * 反转
     */
    public void reverse() {
        this.amount = BigDecimal.ZERO.subtract(this.amount);
    }

    public LoanOrderElementEnum getElement() {
        return element;
    }

    public void setElement(LoanOrderElementEnum element) {
        this.element = element;
    }

    public BillTypeEnum getSource() {
        return source;
    }

    public void setSource(BillTypeEnum source) {
        this.source = source;
    }

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public BillTypeEnum getDestination() {
        return destination;
    }

    public void setDestination(BillTypeEnum destination) {
        this.destination = destination;
    }

    public String getDestinationId() {
        return destinationId;
    }

    public void setDestinationId(String destinationId) {
        this.destinationId = destinationId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    @Override
    public Element clone() {
        Element element = new Element();
        BeanUtils.copyProperties(this, element);
        return element;
    }
}
