package com.lnet.tmsapp.model;

/**
 * Created by Administrator on 2015/7/27.
 */
public class CarrierOrderList {

    private String number;
    private String numberString;
    private String totalPackageQuantity;
    private String totalVolume;
    private String totalWeight;

    public CarrierOrderList(String number, String totalPackageQuantity, String totalVolume, String totalWeight) {
        this.number = number;
        if(number.length()>5){
            numberString = number.substring(0,5)+"...";
        }
        else{
            numberString = number;
        }
        this.totalPackageQuantity = totalPackageQuantity;
        this.totalVolume = totalVolume;
        this.totalWeight = totalWeight;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getTotalPackageQuantity() {
        return totalPackageQuantity;
    }

    public void setTotalPackageQuantity(String totalPackageQuantity) {
        this.totalPackageQuantity = totalPackageQuantity;
    }

    public String getTotalVolume() {
        return totalVolume;
    }

    public void setTotalVolume(String totalVolume) {
        this.totalVolume = totalVolume;
    }

    public String getTotalWeight() {
        return totalWeight;
    }

    public void setTotalWeight(String totalWeight) {
        this.totalWeight = totalWeight;
    }

    @Override
    public String toString() {
        return numberString +
                "     " + totalPackageQuantity +
                "     " + totalVolume +
                "     " + totalWeight;
    }
}
