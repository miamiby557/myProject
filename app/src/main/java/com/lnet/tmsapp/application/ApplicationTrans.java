package com.lnet.tmsapp.application;

import android.app.Application;

import com.lnet.tmsapp.model.DispatchVehicle;
import com.lnet.tmsapp.model.OtdCarrierOrderBean;
import com.lnet.tmsapp.util.DataItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2015/7/4.
 */
public class ApplicationTrans extends Application{
    private String loginName;
    private String userId;

    private String currentCity;
    private String currentCityId;

    private Long nextTime;

    private OtdCarrierOrderBean temp;

    private String cookie;

    private List<DataItem> provinces;

    private List<DataItem> organizationList;

    private List<DataItem> clientDatas;

    private List<DataItem> carrierDatas;

    private List<DataItem> transportOrderItem;

    private List<DataItem> carrierOrderItem;

    private List<DataItem> dispatchAssignList;

    private List<DispatchVehicle> cars;

    private String SERVICE_ADDRESS;

    private String FILENAME = "info";
    private int MODE = 0x0000;


    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }


    public String getSERVICE_ADDRESS() {
        return SERVICE_ADDRESS;
    }

    public void setSERVICE_ADDRESS(String SERVICE_ADDRESS) {
        this.SERVICE_ADDRESS = "http://"+ SERVICE_ADDRESS +"/service/rest";
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<DataItem> getProvinces() {
        return provinces;
    }

    public void setProvinces(List<DataItem> provinces) {
        this.provinces = provinces;
    }

    public String getCurrentCityId() {
        return currentCityId;
    }

    public void setCurrentCityId(String currentCityId) {
        this.currentCityId = currentCityId;
    }

    public String getCurrentCity() {
        return currentCity;
    }

    public void setCurrentCity(String currentCity) {
        this.currentCity = currentCity;
    }

    public String getCookie() {
        return cookie;
    }

    public void setCookie(String cookie) {
        this.cookie = cookie;
    }

    public List<DataItem> getOrganizationList() {
        return organizationList;
    }

    public void setOrganizationList(List<DataItem> organizationList) {
        this.organizationList = organizationList;
    }

    public String getFILENAME() {
        return FILENAME;
    }

    public void setFILENAME(String FILENAME) {
        this.FILENAME = FILENAME;
    }

    public int getMODE() {
        return MODE;
    }

    public void setMODE(int MODE) {
        this.MODE = MODE;
    }

    public OtdCarrierOrderBean getTemp() {
        return temp;
    }

    public void setTemp(OtdCarrierOrderBean temp) {
        this.temp = temp;
    }

    public List<DataItem> getTransportOrderItem() {
        return transportOrderItem;
    }

    public void setTransportOrderItem(List<DataItem> transportOrderItem) {
        this.transportOrderItem = transportOrderItem;
    }

    public List<DataItem> getCarrierOrderItem() {
        return carrierOrderItem;
    }

    public void setCarrierOrderItem(List<DataItem> carrierOrderItem) {
        this.carrierOrderItem = carrierOrderItem;
    }

    public List<DataItem> getClientDatas() {
        return clientDatas;
    }

    public void setClientDatas(List<DataItem> clientDatas) {
        this.clientDatas = clientDatas;
    }

    public List<DataItem> getCarrierDatas() {
        return carrierDatas;
    }

    public void setCarrierDatas(List<DataItem> carrierDatas) {
        this.carrierDatas = carrierDatas;
    }

    public List<DispatchVehicle> getCars() {
        return cars;
    }

    public void setCars(List<DispatchVehicle> cars) {
        this.cars = cars;
    }

    public List<DataItem> getDispatchAssignList() {
        return dispatchAssignList;
    }

    public void setDispatchAssignList(List<DataItem> dispatchAssignList) {
        this.dispatchAssignList = dispatchAssignList;
    }

    public Long getNextTime() {
        return nextTime;
    }

    public void setNextTime(Long nextTime) {
        this.nextTime = nextTime;
    }
}
