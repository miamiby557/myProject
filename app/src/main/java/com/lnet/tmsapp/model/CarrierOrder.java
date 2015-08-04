package com.lnet.tmsapp.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2015/7/3.
 */
public class CarrierOrder {
    private String carrierId;
    private String carrierOrderNumber;
    private String consignee;
    private String consigneePhone;
    private String consigneeAddress;
    private Double totalVolume;
    private Double totalWeight;
    private String startCityId;
    private String destCityId;
    private String receiveMan;
    private String receiveManPhone;
    private String receiveManAddress;
    private Integer paymentType;
    private Integer transportType;
    private Integer calculateType;
    private Integer handoverType;
    private Boolean isUpstairs;
    private String transferOrganizationId;

    private Double transportFee;
    private Double upstairsFee;
    private Double otherFee;

    List<String> transportOrderList = new ArrayList<String>();//运输单号

    public CarrierOrder() {
    }

    public String getCarrierId() {
        return carrierId;
    }

    public void setCarrierId(String carrierId) {
        this.carrierId = carrierId;
    }

    public String getCarrierOrderNumber() {
        return carrierOrderNumber;
    }

    public void setCarrierOrderNumber(String carrierOrderNumber) {
        this.carrierOrderNumber = carrierOrderNumber;
    }

    public String getConsignee() {
        return consignee;
    }

    public void setConsignee(String consignee) {
        this.consignee = consignee;
    }

    public String getConsigneePhone() {
        return consigneePhone;
    }

    public void setConsigneePhone(String consigneePhone) {
        this.consigneePhone = consigneePhone;
    }

    public String getConsigneeAddress() {
        return consigneeAddress;
    }

    public void setConsigneeAddress(String consigneeAddress) {
        this.consigneeAddress = consigneeAddress;
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

    public String getStartCityId() {
        return startCityId;
    }

    public void setStartCityId(String startCityId) {
        this.startCityId = startCityId;
    }

    public String getDestCityId() {
        return destCityId;
    }

    public void setDestCityId(String destCityId) {
        this.destCityId = destCityId;
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

    public Integer getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(Integer paymentType) {
        this.paymentType = paymentType;
    }

    public Integer getTransportType() {
        return transportType;
    }

    public void setTransportType(Integer transportType) {
        this.transportType = transportType;
    }

    public Integer getCalculateType() {
        return calculateType;
    }

    public void setCalculateType(Integer calculateType) {
        this.calculateType = calculateType;
    }

    public Integer getHandoverType() {
        return handoverType;
    }

    public void setHandoverType(Integer handoverType) {
        this.handoverType = handoverType;
    }

    public Boolean getIsUpstairs() {
        return isUpstairs;
    }

    public void setIsUpstairs(Boolean isUpstairs) {
        this.isUpstairs = isUpstairs;
    }

    public String getTransferOrganizationId() {
        return transferOrganizationId;
    }

    public void setTransferOrganizationId(String transferOrganizationId) {
        this.transferOrganizationId = transferOrganizationId;
    }

    public Double getTransportFee() {
        return transportFee;
    }

    public void setTransportFee(Double transportFee) {
        this.transportFee = transportFee;
    }

    public Double getUpstairsFee() {
        return upstairsFee;
    }

    public void setUpstairsFee(Double upstairsFee) {
        this.upstairsFee = upstairsFee;
    }

    public Double getOtherFee() {
        return otherFee;
    }

    public void setOtherFee(Double otherFee) {
        this.otherFee = otherFee;
    }

    public List<String> getTransportOrderList() {
        return transportOrderList;
    }

    public void setTransportOrderList(List<String> transportOrderList) {
        this.transportOrderList = transportOrderList;
    }
}
