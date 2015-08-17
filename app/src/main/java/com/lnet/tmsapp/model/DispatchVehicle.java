package com.lnet.tmsapp.model;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.UUID;

/**
 * Created by Administrator on 2015/7/31.
 */
public class DispatchVehicle{
    private UUID vehicleId;

    public UUID getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(UUID vehicleId) {
        this.vehicleId = vehicleId;
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

    private Integer vehicleType;

    public Integer getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(Integer vehicleType) {
        this.vehicleType = vehicleType;
    }

    private String vehicleSize;

    public String getVehicleSize() {
        return vehicleSize;
    }

    public void setVehicleSize(String vehicleSize) {
        this.vehicleSize = vehicleSize;
    }

    private String maxWeight;

    public String getMaxWeight() {
        return maxWeight;
    }

    public void setMaxWeight(String maxWeight) {
        this.maxWeight = maxWeight;
    }

    private String vehicleNumber;

    public String getVehicleNumber() {
        return vehicleNumber;
    }

    public void setVehicleNumber(String vehicleNumber) {
        this.vehicleNumber = vehicleNumber;
    }

    private String vehicleContent;

    public String getVehicleContent() {
        return vehicleContent;
    }

    public void setVehicleContent(String vehicleContent) {
        this.vehicleContent = vehicleContent;
    }

    private Timestamp compactStartDate;

    public Timestamp getCompactStartDate() {
        return compactStartDate;
    }

    public void setCompactStartDate(Timestamp compactStartDate) {
        this.compactStartDate = compactStartDate;
    }

    private Timestamp compactEndDate;

    public Timestamp getCompactEndDate() {
        return compactEndDate;
    }

    public void setCompactEndDate(Timestamp compactEndDate) {
        this.compactEndDate = compactEndDate;
    }

    private Integer status;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    private String remark;

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public String toString() {
        return vehicleNumber;
    }

    public DispatchVehicle(String vehicleNumber) {
        this.vehicleNumber = vehicleNumber;
    }

    public DispatchVehicle() {
    }
}
