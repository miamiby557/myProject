package com.lnet.tmsapp.model;


import java.util.UUID;

/**
 * Created by admin on 2015/5/14.
 */
public class FeeOrderPayableDetail {
    private UUID orderPayableDetailId;
    private UUID exacctId;
    private Double amount;
    private UUID carrierQuoteId;
    private UUID orderPayableId;

    public UUID getOrderPayableId() {
        return orderPayableId;
    }

    private String remark;
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
    public void setOrderPayableId(UUID orderPayableId) {
        this.orderPayableId = orderPayableId;
    }

    private FeeOrderPayable feeOrderPayable;
    public UUID getOrderPayableDetailId() {
        return orderPayableDetailId;
    }

    public void setOrderPayableDetailId(UUID orderPayableDetailId) {
        this.orderPayableDetailId = orderPayableDetailId;
    }

    public UUID getExacctId() {
        return exacctId;
    }

    public void setExacctId(UUID exacctId) {
        this.exacctId = exacctId;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public UUID getCarrierQuoteId() {
        return carrierQuoteId;
    }

    public void setCarrierQuoteId(UUID carrierQuoteId) {
        this.carrierQuoteId = carrierQuoteId;
    }

    public FeeOrderPayable getFeeOrderPayable() {
        return feeOrderPayable;
    }
    public void setFeeOrderPayable(FeeOrderPayable feeOrderPayable) {
        this.feeOrderPayable = feeOrderPayable;
    }
}
