package com.lnet.tmsapp.model;

import com.lnet.tmsapp.util.DateUtils;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * Created by admin on 2015/5/13.
 */
public class OtdCarrierOrderBean implements Serializable{
    private UUID carrierOrderId;

    private UUID startCityId;
    private String startCity;
    private UUID destCityId;
    private String destCity;

    //send_date              DATE,
    private Date sendDate;
    private Integer wrapType;
    private String goodsName;
    private Integer handoverType;
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
    private Set<OtdCarrierOrderDetailView> detailViews=new HashSet<>();
    List<String> numbers = new ArrayList<>();
    String orderCount;

    public OtdCarrierOrderBean() {
    }

    public void update(Integer paymentType, String consignee, String consigneeAddress, String consigneePhone, String goodsName, String remark) {
        this.paymentType = paymentType;
        this.consignee = consignee;
        this.consigneeAddress = consigneeAddress;
        this.consigneePhone = consigneePhone;
        this.goodsName = goodsName;
        this.remark = remark;
    }

    public void update(UUID carrierId,UUID destCityId, String carrierOrderNumber, Integer calculateType, Integer transportType,UUID createUserId,Boolean isUpstairs) {
        this.carrierId = carrierId;
        this.destCityId = destCityId;
        this.carrierOrderNumber = carrierOrderNumber;
        this.calculateType = calculateType;
        this.transportType = transportType;
        this.createUserId = createUserId;
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

    //transfer_Organization_id
    private UUID transferOrganizationId ;
    private Integer organizationIdPosition;
    public UUID getTransferOrganizationId() {
        return transferOrganizationId;
    }

    public void setTransferOrganizationId(UUID transferOrganizationId) {
        this.transferOrganizationId = transferOrganizationId;
    }

    public Integer getOrganizationIdPosition() {
        return organizationIdPosition;
    }

    public void setOrganizationIdPosition(Integer organizationIdPosition) {
        this.organizationIdPosition = organizationIdPosition;
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

    private String carrierOrderNumber;
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

    private Date expectedDate;

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

    private UUID carrierId;

    public UUID getCarrierId() {
        return carrierId;
    }

    public void setCarrierId(UUID carrierId) {
        this.carrierId = carrierId;
    }

    private String driver;

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    private String driverPhone;

    public String getDriverPhone() {
        return driverPhone;
    }

    public void setDriverPhone(String driverPhone) {
        this.driverPhone = driverPhone;
    }

    private String carType;

    public String getCarType() {
        return carType;
    }

    public void setCarType(String carType) {
        this.carType = carType;
    }

    private String carNumber;

    public String getCarNumber() {
        return carNumber;
    }

    public void setCarNumber(String carNumber) {
        this.carNumber = carNumber;
    }

    private Integer totalItemQuantity;

    public Integer getTotalItemQuantity() {
        return totalItemQuantity;
    }

    public void setTotalItemQuantity(Integer totalItemQuantity) {
        this.totalItemQuantity = totalItemQuantity;
    }

    private Integer totalPackageQuantity;

    public Integer getTotalPackageQuantity() {
        return totalPackageQuantity;
    }

    public void setTotalPackageQuantity(Integer totalPackageQuantity) {
        this.totalPackageQuantity = totalPackageQuantity;
    }

    private Double totalVolume;

    public Double getTotalVolume() {
        return totalVolume;
    }

    public void setTotalVolume(Double totalVolume) {
        this.totalVolume = totalVolume;
    }

    private Double totalWeight;

    public Double getTotalWeight() {
        return totalWeight;
    }

    public void setTotalWeight(Double totalWeight) {
        this.totalWeight = totalWeight;
    }

    private Integer paymentType;
    private Integer paymentTypePosition;

    public Integer getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(Integer paymentType) {
        this.paymentType = paymentType;
    }

    public Integer getPaymentTypePosition() {
        return paymentTypePosition;
    }

    public void setPaymentTypePosition(Integer paymentTypePosition) {
        this.paymentTypePosition = paymentTypePosition;
    }

    private Integer calculateType;

    public Integer getCalculateType() {
        return calculateType;
    }

    public void setCalculateType(Integer calculateType) {
        this.calculateType = calculateType;
    }

    private Integer transportType;

    public Integer getTransportType() {
        return transportType;
    }

    public void setTransportType(Integer transportType) {
        this.transportType = transportType;
    }

    private String remark;

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    private UUID createUserId;

    public UUID getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(UUID createUserId) {
        this.createUserId = createUserId;
    }

    private Timestamp createDate;

    public Timestamp getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Timestamp createDate) {
        this.createDate = createDate;
    }

    private UUID modifyUserId;

    public UUID getModifyUserId() {
        return modifyUserId;
    }

    public void setModifyUserId(UUID modifyUserId) {
        this.modifyUserId = modifyUserId;
    }

    private Timestamp modifyDate;

    public Timestamp getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(Timestamp modifyDate) {
        this.modifyDate = modifyDate;
    }

    private Integer status;

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

    private Integer billingCycle;
    public Integer getBillingCycle() {
        return billingCycle;
    }

    public void setBillingCycle(Integer billingCycle) {
        this.billingCycle = billingCycle;
    }

    public void update(OtdCarrierOrderBean bean) {
        this.transferOrganizationId = bean.getTransferOrganizationId();
        this.paymentType = bean.getPaymentType();
        this.consignee = bean.getConsignee();
        this.consigneePhone = bean.getConsigneePhone();
        this.consigneeAddress = bean.getConsigneeAddress();
        this.goodsName = bean.getGoodsName();
        this.remark = bean.getRemark();
    }

    public void update(String receiveMan, String receiveManPhone, String receiveManAddress) {
        this.consignee = receiveMan;
        this.consigneePhone = receiveManPhone;
        this.consigneeAddress = receiveManAddress;
    }

    public Set<OtdCarrierOrderDetailView> getDetailViews() {
        return detailViews;
    }

    public void setDetailViews(Set<OtdCarrierOrderDetailView> detailViews) {
        this.detailViews = detailViews;
    }

    public List<String> getNumbers() {
        return numbers;
    }

    public void setNumbers(List<String> numbers) {
        this.numbers = numbers;
    }

    public String getOrderCount() {
        return orderCount;
    }

    public void setOrderCount(String orderCount) {
        this.orderCount = orderCount;
    }
}
