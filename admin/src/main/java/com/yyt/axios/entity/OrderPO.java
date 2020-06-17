package com.yyt.axios.entity;

import java.math.BigDecimal;

public class OrderPO {
    private Integer orderId;

    private Integer userId;

    private String orderNumber;

    private BigDecimal orderPrice;

    private String orderPay;

    private String isSend;

    private String tradeNo;

    private String orderFapiaoTitle;

    private String orderFapiaoCompany;

    private String orderFapiaoContent;

    private String payStatus;

    private Integer createTime;

    private Integer updateTime;

    private String consigneeAddr;

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber == null ? null : orderNumber.trim();
    }

    public BigDecimal getOrderPrice() {
        return orderPrice;
    }

    public void setOrderPrice(BigDecimal orderPrice) {
        this.orderPrice = orderPrice;
    }

    public String getOrderPay() {
        return orderPay;
    }

    public void setOrderPay(String orderPay) {
        this.orderPay = orderPay == null ? null : orderPay.trim();
    }

    public String getIsSend() {
        return isSend;
    }

    public void setIsSend(String isSend) {
        this.isSend = isSend == null ? null : isSend.trim();
    }

    public String getTradeNo() {
        return tradeNo;
    }

    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo == null ? null : tradeNo.trim();
    }

    public String getOrderFapiaoTitle() {
        return orderFapiaoTitle;
    }

    public void setOrderFapiaoTitle(String orderFapiaoTitle) {
        this.orderFapiaoTitle = orderFapiaoTitle == null ? null : orderFapiaoTitle.trim();
    }

    public String getOrderFapiaoCompany() {
        return orderFapiaoCompany;
    }

    public void setOrderFapiaoCompany(String orderFapiaoCompany) {
        this.orderFapiaoCompany = orderFapiaoCompany == null ? null : orderFapiaoCompany.trim();
    }

    public String getOrderFapiaoContent() {
        return orderFapiaoContent;
    }

    public void setOrderFapiaoContent(String orderFapiaoContent) {
        this.orderFapiaoContent = orderFapiaoContent == null ? null : orderFapiaoContent.trim();
    }

    public String getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(String payStatus) {
        this.payStatus = payStatus == null ? null : payStatus.trim();
    }

    public Integer getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Integer createTime) {
        this.createTime = createTime;
    }

    public Integer getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Integer updateTime) {
        this.updateTime = updateTime;
    }

    public String getConsigneeAddr() {
        return consigneeAddr;
    }

    public void setConsigneeAddr(String consigneeAddr) {
        this.consigneeAddr = consigneeAddr == null ? null : consigneeAddr.trim();
    }
}