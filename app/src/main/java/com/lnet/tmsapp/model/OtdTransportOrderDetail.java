package com.lnet.tmsapp.model;

import java.sql.Timestamp;
import java.util.UUID;

public class OtdTransportOrderDetail {

    private UUID transportOrderDetailId;
    private UUID transportOrderId;
    private String goodsCode;
    private String goodsName;
    private String goodsType;
    private Long totalItemQuantity;
    private Long totalPackageQuantity;
    private Double totalWeight;
    private Double totalVolume;

    private String remark;
    private UUID createUserId;
    private Timestamp createDate;
    private UUID modifyUserId;
    private Timestamp modifyDate;

    private OtdTransportOrder otdTransportOrder;

    public UUID getTransportOrderDetailId() {
        return transportOrderDetailId;
    }

    public void setTransportOrderDetailId(UUID transportOrderDetailId) {
        this.transportOrderDetailId = transportOrderDetailId;
    }

    public UUID getTransportOrderId() {
        return transportOrderId;
    }

    public void setTransportOrderId(UUID transportOrderId) {
        this.transportOrderId = transportOrderId;
    }


    public String getGoodsCode() {
        return goodsCode;
    }

    public void setGoodsCode(String goodsCode) {
        this.goodsCode = goodsCode;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getGoodsType() {
        return goodsType;
    }

    public void setGoodsType(String goodsType) {
        this.goodsType = goodsType;
    }

    public Long getTotalItemQuantity() {
        return totalItemQuantity;
    }

    public void setTotalItemQuantity(Long totalItemQuantity) {
        this.totalItemQuantity = totalItemQuantity;
    }

    public Long getTotalPackageQuantity() {
        return totalPackageQuantity;
    }

    public void setTotalPackageQuantity(Long totalPackageQuantity) {
        this.totalPackageQuantity = totalPackageQuantity;
    }

    public Double getTotalWeight() {
        return totalWeight;
    }

    public void setTotalWeight(Double totalWeight) {
        this.totalWeight = totalWeight;
    }

    public Double getTotalVolume() {
        return totalVolume;
    }

    public void setTotalVolume(Double totalVolume) {
        this.totalVolume = totalVolume;
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

    public OtdTransportOrder getOtdTransportOrder() {
        return otdTransportOrder;
    }
    public void setOtdTransportOrder(OtdTransportOrder otdTransportOrder) {
        this.otdTransportOrder = otdTransportOrder;
    }
}
