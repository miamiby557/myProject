package com.lnet.tmsapp.model;


import com.lnet.tmsapp.util.DateUtils;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * Created by admin on 2015/5/13.
 */
public class OtdCarrierOrder{
    private UUID carrierOrderId;
    private UUID carrierId;
    private String carrierName;

    //transfer_Organization_id
    private UUID transferOrganizationId ;
    private String transferOrganizationValue;
    private UUID startCityId;
    private String startCity;
    private UUID destCityId;
    private String destCity;
    private String carrierOrderNumber;
    private Date expectedDate;
    private String driver;
    private String driverPhone;
    private String carType;
    private String carNumber;
    private Integer totalItemQuantity;
    private Integer totalPackageQuantity;
    private Double totalVolume;
    private Double totalWeight;
    private Integer paymentType;
    private String paymentTypeName;
    private Integer calculateType;
    private String calculateTypeName;
    private Integer transportType;
    private String transportTypeName;
    private String remark;
    private UUID createUserId;
    private Timestamp createDate;
    private UUID modifyUserId;
    private Timestamp modifyDate;
    private Integer status;
    private Integer billingCycle;
    private String billingCycleName;


    //send_date              DATE,
    private Date sendDate;
    private Integer wrapType;
    private String wrapTypeName;
    private String goodsName;
    private Integer handoverType;
    private String handoverTypeName;
    private Boolean isSign;
    private Boolean isUpstairs;
    /*consignee              NVARCHAR2(50),
    consignee_address      NVARCHAR2(50),
    consignee_phone        NVARCHAR2(50)*/
    private String consignee;
    private String consigneeAddress;
    private String consigneePhone;
    /*wrap_type              INTEGER,
    goods_name             NVARCHAR2(50),
    handover_type          INTEGER*/
    private Integer receiptPageNumber;

    private Set<OtdCarrierOrderDetail> details=new HashSet<>();

    //private Set<OtdCarrierOrderDetailView> detailViews=new HashSet<>();


    public OtdCarrierOrder() {
    }



    public OtdCarrierOrder(UUID carrierId, UUID destCityId, String carrierOrderNumber, Integer calculateType, Integer transportType,UUID createUserId,Integer totalPackageQuantity,Double totalVolume,Double totalWeight,Boolean isUpstairs) {
        this.carrierId = carrierId;
        this.destCityId = destCityId;
        this.carrierOrderNumber = carrierOrderNumber;
        this.calculateType = calculateType;
        this.transportType = transportType;
        this.createUserId = createUserId;
        this.totalPackageQuantity = totalPackageQuantity;
        this.totalVolume = totalVolume;
        this.totalWeight = totalWeight;
        this.modifyDate = DateUtils.getTimestampNow();
        this.createDate = DateUtils.getTimestampNow();
        this.isUpstairs = isUpstairs;
        this.status = 1;
    }

    public UUID getCarrierOrderId() {
        return carrierOrderId;
    }

    public void setCarrierOrderId(UUID carrierOrderId) {
        this.carrierOrderId = carrierOrderId;
    }


    public UUID getTransferOrganizationId() {
        return transferOrganizationId;
    }

    public void setTransferOrganizationId(UUID transferOrganizationId) {
        this.transferOrganizationId = transferOrganizationId;
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

    public Boolean getIsSign() {
        return isSign;
    }

    public void setIsSign(Boolean isSign) {
        this.isSign = isSign;
    }

    public Boolean getIsUpstairs() {
        return isUpstairs;
    }

    public void setIsUpstairs(Boolean isUpstairs) {
        this.isUpstairs = isUpstairs;
    }

    public Date getSendDate() {
        return sendDate;
    }

    public void setSendDate(Date sendDate) {
        this.sendDate = sendDate;
    }
    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }
    public Integer getWrapType() {
        return wrapType;
    }

    public void setWrapType(Integer wrapType) {
        this.wrapType = wrapType;
    }
    public Integer getHandoverType() {
        return handoverType;
    }

    public void setHandoverType(Integer handoverType) {
        this.handoverType = handoverType;
    }

    public Integer getReceiptPageNumber() {
        return receiptPageNumber;
    }

    public void setReceiptPageNumber(Integer receiptPageNumber) {
        this.receiptPageNumber = receiptPageNumber;
    }


    public Date getExpectedDate() {
        return expectedDate;
    }

    public void setExpectedDate(Date expectedDate) {
        this.expectedDate = expectedDate;
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

    public String getCarrierOrderNumber() {
        return carrierOrderNumber;
    }

    public void setCarrierOrderNumber(String carrierOrderNumber) {
        this.carrierOrderNumber = carrierOrderNumber;
    }


    public UUID getCarrierId() {
        return carrierId;
    }

    public void setCarrierId(UUID carrierId) {
        this.carrierId = carrierId;
    }



    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }


    public String getDriverPhone() {
        return driverPhone;
    }

    public void setDriverPhone(String driverPhone) {
        this.driverPhone = driverPhone;
    }


    public String getCarType() {
        return carType;
    }

    public void setCarType(String carType) {
        this.carType = carType;
    }


    public String getCarNumber() {
        return carNumber;
    }

    public void setCarNumber(String carNumber) {
        this.carNumber = carNumber;
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

    public Set<OtdCarrierOrderDetail> getDetails() {
        return details;
    }

    public void setDetails(Set<OtdCarrierOrderDetail> details) {
        this.details = details;
    }


    public Integer getBillingCycle() {
        return billingCycle;
    }

    public void setBillingCycle(Integer billingCycle) {
        this.billingCycle = billingCycle;
    }

    public String getCarrierName() {
        return carrierName;
    }

    public void setCarrierName(String carrierName) {
        this.carrierName = carrierName;
    }

    public String getTransferOrganizationValue() {
        return transferOrganizationValue;
    }

    public void setTransferOrganizationValue(String transferOrganizationValue) {
        this.transferOrganizationValue = transferOrganizationValue;
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

    public String getBillingCycleName() {
        return billingCycleName;
    }

    public void setBillingCycleName(String billingCycleName) {
        this.billingCycleName = billingCycleName;
    }

    public String getWrapTypeName() {
        return wrapTypeName;
    }

    public void setWrapTypeName(String wrapTypeName) {
        this.wrapTypeName = wrapTypeName;
    }

    public String getHandoverTypeName() {
        return handoverTypeName;
    }

    public void setHandoverTypeName(String handoverTypeName) {
        this.handoverTypeName = handoverTypeName;
    }
}
