package com.lnet.tmsapp.model;

/**
 * Created by admin on 2015/7/3.
 */
public class TransportOrder {
    private String carrierOrderId;
    private String clientId;
    private String destCityId;
    private Double confirmedVolume;
    private Double confirmedWeight;
    private String orderType;
    private String transportType;
    private String receiveMan;
    private String receiveManPhone;
    private String receiveManAddress;
    private String handoverType;
    private String billingCycle;
    private String paymentType;
    private String calculateType;
    private String urgencyLevel;

    public TransportOrder() {
    }

    public String getCarrierOrderId() {
        return carrierOrderId;
    }

    public void setCarrierOrderId(String carrierOrderId) {
        this.carrierOrderId = carrierOrderId;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getDestCityId() {
        return destCityId;
    }

    public void setDestCityId(String destCityId) {
        this.destCityId = destCityId;
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

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getTransportType() {
        return transportType;
    }

    public void setTransportType(String transportType) {
        this.transportType = transportType;
    }

    public String getReceiveMan() {
        return receiveMan;
    }

    public void setReceiveMan(String receiveMan) {
        this.receiveMan = receiveMan;
    }

    public String getReceiveManPhone() {
        return receiveManPhone;
    }

    public void setReceiveManPhone(String receiveManPhone) {
        this.receiveManPhone = receiveManPhone;
    }

    public String getReceiveManAddress() {
        return receiveManAddress;
    }

    public void setReceiveManAddress(String receiveManAddress) {
        this.receiveManAddress = receiveManAddress;
    }

    public String getHandoverType() {
        return handoverType;
    }

    public void setHandoverType(String handoverType) {
        this.handoverType = handoverType;
    }

    public String getBillingCycle() {
        return billingCycle;
    }

    public void setBillingCycle(String billingCycle) {
        this.billingCycle = billingCycle;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public String getCalculateType() {
        return calculateType;
    }

    public void setCalculateType(String calculateType) {
        this.calculateType = calculateType;
    }

    public String getUrgencyLevel() {
        return urgencyLevel;
    }

    public void setUrgencyLevel(String urgencyLevel) {
        this.urgencyLevel = urgencyLevel;
    }
}
