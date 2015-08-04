package com.lnet.tmsapp.util;

/**
 * Created by admin on 2015/7/10.
 */
public class DataItem {
    String textValue;
    String textName;

    public DataItem() {
    }

    public DataItem(String textValue, String textName) {
        this.textValue = textValue;
        if(textName.length()>15){
            this.textName = textName.substring(0,15)+"...";
        }else{
            this.textName = textName;
        }
    }

    public String getTextValue() {
        return textValue;
    }

    public void setTextValue(String textValue) {
        this.textValue = textValue;
    }

    public String getTextName() {
        return textName;
    }

    public void setTextName(String textName) {
        this.textName = textName;
    }

    @Override
    public String toString() {
        return textName;
    }
}
