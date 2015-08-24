package com.lnet.tmsapp.model;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.UUID;

/**
 * Created by admin on 2015/5/13.
 */
public class OtdOrderSign{
    private UUID transportOrderId;
    private String transportOrderNumber;
    private String signMan;
    private Timestamp signDate;
    private String signManCard;
    private String agentSignMan;
    private String agentSignManCard;
    private String remark;
    private Boolean isAbnormal;

    private UUID createUserId;
    private Timestamp createDate;
    private UUID modifyUserId;
    private Timestamp modifyDate;
    private String photoString;

    public UUID getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(UUID createUserId) {
        this.createUserId = createUserId;
    }

    public Timestamp getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Timestamp createDate) {
        this.createDate = createDate;
    }

    public UUID getModifyUserId() {
        return modifyUserId;
    }

    public void setModifyUserId(UUID modifyUserId) {
        this.modifyUserId = modifyUserId;
    }

    public Timestamp getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(Timestamp modifyDate) {
        this.modifyDate = modifyDate;
    }
    public Boolean getIsAbnormal() {
        return isAbnormal;
    }

    public void setIsAbnormal(Boolean isAbnormal) {
        this.isAbnormal = isAbnormal;
    }
    public UUID getTransportOrderId() {
        return transportOrderId;
    }

    public void setTransportOrderId(UUID transportOrderId) {
        this.transportOrderId = transportOrderId;
    }

    public String getSignMan() {
        return signMan;
    }

    public void setSignMan(String signMan) {
        this.signMan = signMan;
    }

    public Timestamp getSignDate() {
        return signDate;
    }

    public void setSignDate(Timestamp signDate) {
        this.signDate = signDate;
    }

    public String getSignManCard() {
        return signManCard;
    }

    public void setSignManCard(String signManCard) {
        this.signManCard = signManCard;
    }

    public String getAgentSignMan() {
        return agentSignMan;
    }

    public void setAgentSignMan(String agentSignMan) {
        this.agentSignMan = agentSignMan;
    }

    public String getAgentSignManCard() {
        return agentSignManCard;
    }

    public void setAgentSignManCard(String agentSignManCard) {
        this.agentSignManCard = agentSignManCard;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getPhotoString() {
        return photoString;
    }

    public void setPhotoString(String photoString) {
        this.photoString = photoString;
    }

    public String getTransportOrderNumber() {
        return transportOrderNumber;
    }

    public void setTransportOrderNumber(String transportOrderNumber) {
        this.transportOrderNumber = transportOrderNumber;
    }
}
