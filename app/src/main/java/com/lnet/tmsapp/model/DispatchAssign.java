package com.lnet.tmsapp.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class DispatchAssign{
    private UUID dispatchAssignId;

    private String dispatchAssignNumber;

    private Integer vehicleType;

    private String vehicleCode;

    private String dirver;

    private String dirverPhone;

    private Double totalFee;

    private Integer status;

    private Integer dispatchType;

    private Integer totalItemQuantity;

    private Integer totalPackageQuantity;

    private Double totalVolume;

    private Double totalWeight;

    private String startAddress;

    private String destAddress;

    private Date expectFinishTime;

    private String remark;
    private Set<DispatchAssignDetail> details=new HashSet<DispatchAssignDetail>();

    public DispatchAssign() {
    }

    public DispatchAssign(Integer dispatchType) {
        this.dispatchType = dispatchType;
    }

    public Set<DispatchAssignDetail> getDetails() {
        return details;
    }

    public void setDetails(Set<DispatchAssignDetail> details) {
        this.details = details;
    }

    public UUID getDispatchAssignId() {
        return dispatchAssignId;
    }

    public void setDispatchAssignId(UUID dispatchAssignId) {
        this.dispatchAssignId = dispatchAssignId;
    }

    public String getDispatchAssignNumber() {
        return dispatchAssignNumber;
    }

    public void setDispatchAssignNumber(String dispatchAssignNumber) {
        this.dispatchAssignNumber = dispatchAssignNumber;
    }

    public Integer getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(Integer vehicleType) {
        this.vehicleType = vehicleType;
    }

    public String getVehicleCode() {
        return vehicleCode;
    }

    public void setVehicleCode(String vehicleCode) {
        this.vehicleCode = vehicleCode;
    }

    public String getDirver() {
        return dirver;
    }

    public void setDirver(String dirver) {
        this.dirver = dirver;
    }

    public String getDirverPhone() {
        return dirverPhone;
    }

    public void setDirverPhone(String dirverPhone) {
        this.dirverPhone = dirverPhone;
    }

    public Double getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(Double totalFee) {
        this.totalFee = totalFee;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getDispatchType() {
        return dispatchType;
    }

    public void setDispatchType(Integer dispatchType) {
        this.dispatchType = dispatchType;
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

    public String getStartAddress() {
        return startAddress;
    }

    public void setStartAddress(String startAddress) {
        this.startAddress = startAddress;
    }

    public String getDestAddress() {
        return destAddress;
    }

    public void setDestAddress(String destAddress) {
        this.destAddress = destAddress;
    }

    public Date getExpectFinishTime() {
        return expectFinishTime;
    }

    public void setExpectFinishTime(Date expectFinishTime) {
        this.expectFinishTime = expectFinishTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
