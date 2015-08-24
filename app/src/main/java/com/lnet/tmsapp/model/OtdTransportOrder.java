package com.lnet.tmsapp.model;

import com.lnet.tmsapp.util.DateUtils;

import java.sql.Timestamp;
import java.util.Date;
import java.util.UUID;

public class OtdTransportOrder {

    private UUID transportOrderId;
    private UUID pickupOrderId;
    private String clientOrderNumber;
    private String viceClientOrderNumber;
    private String lnetOrderNumber;
    private UUID branchId;
    private UUID clientId;
    private String clientName;
    private Date orderDate;
    private Integer orderType;
    private UUID startCityId;
    private String startCity;
    private UUID destCityId;
    private String destCity;
    private String receiveCompany;
    private String receiveMan;
    private String receivePhone;
    private String receiveAddress;
    private Integer handoverType;
    private Integer totalItemQuantity;
    private Integer totalPackageQuantity;
    private Double totalVolume;
    private Double totalWeight;
    private Integer confirmedItemQuantity;
    private Integer confirmedPackageQuantity;
    private Double confirmedVolume;
    private Double confirmedWeight;
    private String specialRequest;
    private Integer billingCycle;
    private String billingCycleName;
    private Integer paymentType;
    private String paymentTypeName;
    private Integer calculateType;
    private String calculateTypeName;
    private Integer transportType;
    private String transportTypeName;
    private Date sentDate;
    private String remark;
    private UUID createUserId;
    private Timestamp createDate;
    private UUID modifyUserId;
    private Timestamp modifyDate;
    private Integer status;
    private Integer receiptPageNumber;
    private Integer urgencyLevel;
    private String urgencyLevelName;
    private Date expectedDate;
    private Integer wrapType;
    private String wrapTypeName;
    private UUID mergeTransportOrderId;
    private Integer mergeStatus;
    private Integer orderDispatchType;

    public OtdTransportOrder() {
    }

    public OtdTransportOrder(UUID transportOrderId, String clientOrderNumber) {
        this.transportOrderId = transportOrderId;
        this.clientOrderNumber = clientOrderNumber;
    }

    public void update(String receiveCompany, String receiveMan, String receivePhone, String receiveAddress) {
        this.receiveCompany = receiveCompany;
        this.receiveMan = receiveMan;
        this.receivePhone = receivePhone;
        this.receiveAddress = receiveAddress;
    }

    public void update(String clientOrderNumber, UUID clientId, UUID destCityId, UUID createUserId) {
        this.clientOrderNumber = clientOrderNumber;
        this.clientId = clientId;
        this.destCityId = destCityId;
        this.createUserId = createUserId;
        this.modifyDate = DateUtils.getTimestampNow();
        this.createDate = DateUtils.getTimestampNow();
        this.mergeStatus = 1;
        this.orderDate = DateUtils.getTimestampNow();
    }

    public void updateExpectDate(String expectDate){
        Timestamp date = DateUtils.toTimestamp(expectDate);
        this.expectedDate = date;
    }

    public UUID getTransportOrderId() {
        return transportOrderId;
    }

    public void setTransportOrderId(UUID transportOrderId) {
        this.transportOrderId = transportOrderId;
    }

    public UUID getPickupOrderId() {
        return pickupOrderId;
    }

    public void setPickupOrderId(UUID pickupOrderId) {
        this.pickupOrderId = pickupOrderId;
    }

    public String getClientOrderNumber() {
        return clientOrderNumber;
    }

    public void setClientOrderNumber(String clientOrderNumber) {
        this.clientOrderNumber = clientOrderNumber;
    }

    public String getLnetOrderNumber() {
        return lnetOrderNumber;
    }

    public void setLnetOrderNumber(String lnetOrderNumber) {
        this.lnetOrderNumber = lnetOrderNumber;
    }

    public UUID getBranchId() {
        return branchId;
    }

    public void setBranchId(UUID branchId) {
        this.branchId = branchId;
    }

    public UUID getClientId() {
        return clientId;
    }

    public void setClientId(UUID clientId) {
        this.clientId = clientId;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public Integer getOrderType() {
        return orderType;
    }

    public void setOrderType(Integer orderType) {
        this.orderType = orderType;
    }

    public UUID getStartCityId() {
        return startCityId;
    }

    public void setStartCityId(UUID startCityId) {
        this.startCityId = startCityId;
    }

    public String getStartCity() {
        return startCity;
    }

    public void setStartCity(String startCity) {
        this.startCity = startCity;
    }

    public UUID getDestCityId() {
        return destCityId;
    }

    public void setDestCityId(UUID destCityId) {
        this.destCityId = destCityId;
    }

    public String getDestCity() {
        return destCity;
    }

    public void setDestCity(String destCity) {
        this.destCity = destCity;
    }

    public String getReceiveCompany() {
        return receiveCompany;
    }

    public void setReceiveCompany(String receiveCompany) {
        this.receiveCompany = receiveCompany;
    }

    public String getReceiveMan() {
        return receiveMan;
    }

    public void setReceiveMan(String receiveMan) {
        this.receiveMan = receiveMan;
    }

    public String getReceivePhone() {
        return receivePhone;
    }

    public void setReceivePhone(String receivePhone) {
        this.receivePhone = receivePhone;
    }

    public String getReceiveAddress() {
        return receiveAddress;
    }

    public void setReceiveAddress(String receiveAddress) {
        this.receiveAddress = receiveAddress;
    }

    public Integer getHandoverType() {
        return handoverType;
    }

    public void setHandoverType(Integer handoverType) {
        this.handoverType = handoverType;
    }

    public Integer getTotalItemQuantity() {
        return totalItemQuantity;
    }

    public void setTotalItemQuantity(Integer totalItemQuantity) {
        this.totalItemQuantity = totalItemQuantity;
    }

    public Integer getTotalPackageQuantity() {
        return totalPackageQuantity;
    }

    public void setTotalPackageQuantity(Integer totalPackageQuantity) {
        this.totalPackageQuantity = totalPackageQuantity;
    }

    public Double getTotalVolume() {
        return totalVolume;
    }

    public void setTotalVolume(Double totalVolume) {
        this.totalVolume = totalVolume;
    }

    public Double getTotalWeight() {
        return totalWeight;
    }

    public void setTotalWeight(Double totalWeight) {
        this.totalWeight = totalWeight;
    }

    public Integer getConfirmedItemQuantity() {
        return confirmedItemQuantity;
    }

    public void setConfirmedItemQuantity(Integer confirmedItemQuantity) {
        this.confirmedItemQuantity = confirmedItemQuantity;
    }

    public Integer getConfirmedPackageQuantity() {
        return confirmedPackageQuantity;
    }

    public void setConfirmedPackageQuantity(Integer confirmedPackageQuantity) {
        this.confirmedPackageQuantity = confirmedPackageQuantity;
    }

    public Double getConfirmedVolume() {
        return confirmedVolume;
    }

    public void setConfirmedVolume(Double confirmedVolume) {
        this.confirmedVolume = confirmedVolume;
    }

    public Double getConfirmedWeight() {
        return confirmedWeight;
    }

    public void setConfirmedWeight(Double confirmedWeight) {
        this.confirmedWeight = confirmedWeight;
    }

    public String getSpecialRequest() {
        return specialRequest;
    }

    public void setSpecialRequest(String specialRequest) {
        this.specialRequest = specialRequest;
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

    public Integer getTransportType() {
        return transportType;
    }

    public void setTransportType(Integer transportType) {
        this.transportType = transportType;
    }

    public Date getSentDate() {
        return sentDate;
    }

    public void setSentDate(Date sentDate) {
        this.sentDate = sentDate;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getReceiptPageNumber() {
        return receiptPageNumber;
    }

    public void setReceiptPageNumber(Integer receiptPageNumber) {
        this.receiptPageNumber = receiptPageNumber;
    }

    public Integer getUrgencyLevel() {
        return urgencyLevel;
    }

    public void setUrgencyLevel(Integer urgencyLevel) {
        this.urgencyLevel = urgencyLevel;
    }

    public Date getExpectedDate() {
        return expectedDate;
    }

    public void setExpectedDate(Date expectedDate) {
        this.expectedDate = expectedDate;
    }

    public Integer getWrapType() {
        return wrapType;
    }

    public void setWrapType(Integer wrapType) {
        this.wrapType = wrapType;
    }

    public UUID getMergeTransportOrderId() {
        return mergeTransportOrderId;
    }

    public void setMergeTransportOrderId(UUID mergeTransportOrderId) {
        this.mergeTransportOrderId = mergeTransportOrderId;
    }

    public Integer getMergeStatus() {
        return mergeStatus;
    }

    public void setMergeStatus(Integer mergeStatus) {
        this.mergeStatus = mergeStatus;
    }

    public String getViceClientOrderNumber() {
        return viceClientOrderNumber;
    }

    public void setViceClientOrderNumber(String viceClientOrderNumber) {
        this.viceClientOrderNumber = viceClientOrderNumber;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getBillingCycleName() {
        return billingCycleName;
    }

    public void setBillingCycleName(String billingCycleName) {
        this.billingCycleName = billingCycleName;
    }

    public String getPaymentTypeName() {
        return paymentTypeName;
    }

    public void setPaymentTypeName(String paymentTypeName) {
        this.paymentTypeName = paymentTypeName;
    }

    public String getCalculateTypeName() {
        return calculateTypeName;
    }

    public void setCalculateTypeName(String calculateTypeName) {
        this.calculateTypeName = calculateTypeName;
    }

    public String getTransportTypeName() {
        return transportTypeName;
    }

    public void setTransportTypeName(String transportTypeName) {
        this.transportTypeName = transportTypeName;
    }

    public String getUrgencyLevelName() {
        return urgencyLevelName;
    }

    public void setUrgencyLevelName(String urgencyLevelName) {
        this.urgencyLevelName = urgencyLevelName;
    }

    public String getWrapTypeName() {
        return wrapTypeName;
    }

    public void setWrapTypeName(String wrapTypeName) {
        this.wrapTypeName = wrapTypeName;
    }

    @Override
    public String toString() {
        return clientOrderNumber;
    }

    public Integer getOrderDispatchType() {
        return orderDispatchType;
    }

    public void setOrderDispatchType(Integer orderDispatchType) {
        this.orderDispatchType = orderDispatchType;
    }
}
