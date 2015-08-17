package com.lnet.tmsapp.model;


import java.util.UUID;

public class DispatchAssignDetail {
    private UUID dispatchAssignDetailId;
    private UUID dispatchAssignId;
    private UUID orderId;
    private DispatchAssign dispatchAssign;
    public DispatchAssign getDispatchAssign() {
        return dispatchAssign;
    }
    public void setDispatchAssign(DispatchAssign dispatchAssign) {
        this.dispatchAssign = dispatchAssign;
    }

    public UUID getDispatchAssignDetailId() {
        return dispatchAssignDetailId;
    }

    public void setDispatchAssignDetailId(UUID dispatchAssignDetailId) {
        this.dispatchAssignDetailId = dispatchAssignDetailId;
    }
    public UUID getDispatchAssignId() {
        return dispatchAssignId;
    }

    public void setDispatchAssignId(UUID dispatchAssignId) {
        this.dispatchAssignId = dispatchAssignId;
    }

    public UUID getOrderId() {
        return orderId;
    }

    public void setOrderId(UUID orderId) {
        this.orderId = orderId;
    }
}
