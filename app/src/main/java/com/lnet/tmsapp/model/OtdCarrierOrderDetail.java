package com.lnet.tmsapp.model;


import java.io.Serializable;
import java.util.UUID;

/**
 * Created by admin on 2015/5/14.
 */
public class OtdCarrierOrderDetail implements Serializable{
    private UUID carrierOrderDetailId;
    private UUID carrierOrderId;
    private UUID transportOrderId;
    private String transportOrderNumber;
    private Integer confirmedItemQuantity;
    private Integer confirmedPackageQuantity;
    private Double confirmedVolume;
    private Double confirmedWeight;
    private int receivePageNumber;
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


    public OtdCarrierOrder getOtdCarrierOrder() {
        return otdCarrierOrder;
    }
    public void setOtdCarrierOrder(OtdCarrierOrder otdCarrierOrder) {
        this.otdCarrierOrder = otdCarrierOrder;
    }

    public int getReceivePageNumber() {
        return receivePageNumber;
    }

    public void setReceivePageNumber(int receivePageNumber) {
        this.receivePageNumber = receivePageNumber;
    }

    public String getTransportOrderNumber() {
        return transportOrderNumber;
    }

    public void setTransportOrderNumber(String transportOrderNumber) {
        this.transportOrderNumber = transportOrderNumber;
    }
}
