package com.lnet.tmsapp.application;

import android.app.Application;

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

    private String cookie;

    private List<DataItem> provinces = new ArrayList<>();

    private List<DataItem> organizationList = new ArrayList<>();

    private String SERVICE_ADDRESS;


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
}
