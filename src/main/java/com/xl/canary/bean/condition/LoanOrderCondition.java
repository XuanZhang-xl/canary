package com.xl.canary.bean.condition;

/**
 * 订单条件查询
 * created by XUAN on 2018/08/09
 */
public class LoanOrderCondition {

    /**
     * 订单号
     */
    private String orderId;

    /**
     * 用户编号
     */
    private String userCode;

    /**
     * 放款时间开始
     */
    private Long lentTimeBegin;

    /**
     * 放款时间结束
     */
    private Long lentTimeEnd;

    private Integer pageSize;

    private Integer pageNumber;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public Long getLentTimeBegin() {
        return lentTimeBegin;
    }

    public void setLentTimeBegin(Long lentTimeBegin) {
        this.lentTimeBegin = lentTimeBegin;
    }

    public Long getLentTimeEnd() {
        return lentTimeEnd;
    }

    public void setLentTimeEnd(Long lentTimeEnd) {
        this.lentTimeEnd = lentTimeEnd;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
    }
}
