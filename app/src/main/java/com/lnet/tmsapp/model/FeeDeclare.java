package com.lnet.tmsapp.model;

import com.lnet.tmsapp.util.DataItem;

import java.util.List;

/**
 * Created by Administrator on 2015/8/13.
 */
public class FeeDeclare {
    private List<DataItem> feeDeclares;
    private String imagesString;
    private String declareOrderNumber;

    public List<DataItem> getFeeDeclares() {
        return feeDeclares;
    }

    public void setFeeDeclares(List<DataItem> feeDeclares) {
        this.feeDeclares = feeDeclares;
    }

    public String getImagesString() {
        return imagesString;
    }

    public void setImagesString(String imagesString) {
        this.imagesString = imagesString;
    }

    public String getDeclareOrderNumber() {
        return declareOrderNumber;
    }

    public void setDeclareOrderNumber(String declareOrderNumber) {
        this.declareOrderNumber = declareOrderNumber;
    }
}
