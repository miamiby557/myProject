package com.lnet.tmsapp.model;


import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * Created by admin on 2015/5/14.
 */
public class FeeOrderPayable {
    private UUID orderPayableId;
    private UUID carrierId;
    private UUID sourceOrderId;
    private Integer sourceOrderType;
    private Double totalAmount;
    private Integer billingCycle;
    private Integer paymentType;
    private Integer calculateType;
    private Integer status;
    private Integer backStatus;
    private UUID createUserId;
    private Timestamp createDate;
    private UUID modifyUserId;
    private Timestamp modifyDate;
    private Integer version;
    private Boolean feeSaveMark;
    private Set<FeeOrderPayableDetail> details=new HashSet<>();
    public UUID getOrderPayableId() {
        return orderPayableId;
    }

    public void setOrderPayableId(UUID orderPayableId) {
        this.orderPayableId = orderPayableId;
    }

    public UUID getCarrierId() {
        return carrierId;
    }

    public void setCarrierId(UUID carrierId) {
        this.carrierId = carrierId;
    }
    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
    public Integer getBackStatus() {
        return backStatus;
    }

    public void setBackStatus(Integer backStatus) {
        this.backStatus = backStatus;
    }

    public UUID getSourceOrderId() {
        return sourceOrderId;
    }

    public void setSourceOrderId(UUID sourceOrderId) {
        this.sourceOrderId = sourceOrderId;
    }

    public Integer getSourceOrderType() {
        return sourceOrderType;
    }

    public void setSourceOrderType(Integer sourceOrderType) {
        this.sourceOrderType = sourceOrderType;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Integer getBillingCycle() {
        return billingCycle;
    }

    public void setBillingCycle(Integer billingCycle) {
        this.billingCycle = billingCycle;
    }

    public Integer getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(Integer paymentType) {
        this.paymentType = paymentType;
    }

    public Integer getCalculateType() {
        return calculateType;
    }

    public void setCalculateType(Integer calculateType) {
        this.calculateType = calculateType;
    }

    public UUID getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(UUID createUserId) {
        this.createUserId = createUserId;
    }

    public Timestamp getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Timestamp createDate) {
        this.createDate = createDate;
    }

    public UUID getModifyUserId() {
        return modifyUserId;
    }

    public void setModifyUserId(UUID modifyUserId) {
        this.modifyUserId = modifyUserId;
    }

    public Timestamp getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(Timestamp modifyDate) {
        this.modifyDate = modifyDate;
    }

    public Set<FeeOrderPayableDetail> getDetails() {
        return details;
    }

    public void setDetails(Set<FeeOrderPayableDetail> details) {
        this.details = details;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Boolean getFeeSaveMark() {
        return feeSaveMark;
    }

    public void setFeeSaveMark(Boolean feeSaveMark) {
        this.feeSaveMark = feeSaveMark;
    }
}
