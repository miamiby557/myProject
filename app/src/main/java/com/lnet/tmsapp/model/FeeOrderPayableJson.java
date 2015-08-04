package com.lnet.tmsapp.model;

/**
 * Created by admin on 2015/7/15.
 */
public class FeeOrderPayableJson {
    private int index;
    private String exacctName;
    private Double exacctMoney;
    private String exacctId;

    public FeeOrderPayableJson() {
    }

    public FeeOrderPayableJson(int index, String exacctName, Double exacctMoney, String exacctId) {
        this.index = index;
        this.exacctName = exacctName;
        this.exacctMoney = exacctMoney;
        this.exacctId = exacctId;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getExacctName() {
        return exacctName;
    }

    public void setExacctName(String exacctName) {
        this.exacctName = exacctName;
    }

    public Double getExacctMoney() {
        return exacctMoney;
    }

    public void setExacctMoney(Double exacctMoney) {
        this.exacctMoney = exacctMoney;
    }

    public String getExacctId() {
        return exacctId;
    }

    public void setExacctId(String exacctId) {
        this.exacctId = exacctId;
    }
}
