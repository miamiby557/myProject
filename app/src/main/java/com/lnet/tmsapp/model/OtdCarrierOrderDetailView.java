package com.lnet.tmsapp.model;


import java.util.UUID;

public class OtdCarrierOrderDetailView {
    private UUID carrierOrderDetailId;
    private UUID carrierOrderId;
    private UUID transportOrderId;
    private Integer confirmedItemQuantity;
    private Integer confirmedPackageQuantity;
    private Double confirmedVolume;
    private Double confirmedWeight;
    private String clientOrderNumber;
    private String clientName;
    private Integer receiptPageNumber;
    private Integer wrapType;
    private String receiveCompany;
    private String receiveMan;
    private String destCity;

    private OtdCarrierOrder otdCarrierOrder;
    public UUID getCarrierOrderDetailId() {
        return carrierOrderDetailId;
    }

    public void setCarrierOrderDetailId(UUID carrierOrderDetailId) {
        this.carrierOrderDetailId = carrierOrderDetailId;
    }

    public UUID getCarrierOrderId() {
        return carrierOrderId;
    }

    public void setCarrierOrderId(UUID carrierOrderId) {
        this.carrierOrderId = carrierOrderId;
    }
    public Integer getReceiptPageNumber() {
        return receiptPageNumber;
    }

    public void setReceiptPageNumber(Integer receiptPageNumber) {
        this.receiptPageNumber = receiptPageNumber;
    }

    public Integer getWrapType() {
        return wrapType;
    }

    public void setWrapType(Integer wrapType) {
        this.wrapType = wrapType;
    }
    public UUID getTransportOrderId() {
        return transportOrderId;
    }

    public void setTransportOrderId(UUID transportOrderId) {
        this.transportOrderId = transportOrderId;
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

    public String getClientOrderNumber() {
        return clientOrderNumber;
    }

    public void setClientOrderNumber(String clientOrderNumber) {
        this.clientOrderNumber = clientOrderNumber;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
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
}
