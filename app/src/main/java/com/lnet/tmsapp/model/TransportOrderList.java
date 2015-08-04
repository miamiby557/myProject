package com.lnet.tmsapp.model;

/**
 * Created by Administrator on 2015/7/27.
 */
public class TransportOrderList {

    private String number;
    private String confirmedVolume;
    private String confirmedWeight;

    private String numberSubString;

    public TransportOrderList(String number, String confirmedVolume, String confirmedWeight) {
        this.number = number;
        if(number.length()>5){
            numberSubString = number.substring(0,5)+"...";
        }
        else{
            numberSubString = number;
        }
        this.confirmedVolume = confirmedVolume;
        this.confirmedWeight = confirmedWeight;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getConfirmedVolume() {
        return confirmedVolume;
    }

    public void setConfirmedVolume(String confirmedVolume) {
        this.confirmedVolume = confirmedVolume;
    }

    public String getConfirmedWeight() {
        return confirmedWeight;
    }

    public void setConfirmedWeight(String confirmedWeight) {
        this.confirmedWeight = confirmedWeight;
    }

    public String getNumberSubString() {
        return numberSubString;
    }

    public void setNumberSubString(String numberSubString) {
        this.numberSubString = numberSubString;
    }

    @Override
    public String toString() {
        return numberSubString +"              "+ confirmedVolume +"            "+ confirmedWeight;
    }
}
