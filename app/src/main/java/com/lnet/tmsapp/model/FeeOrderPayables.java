package com.lnet.tmsapp.model;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by admin on 2015/7/16.
 */
public class FeeOrderPayables {

    private Set<FeeOrderPayableJson> payableJsonSet = new HashSet<>();

    public FeeOrderPayables(Set<FeeOrderPayableJson> payableJsonSet) {
        this.payableJsonSet = payableJsonSet;
    }

    public Set<FeeOrderPayableJson> getPayableJsonSet() {
        return payableJsonSet;
    }

    public void setPayableJsonSet(Set<FeeOrderPayableJson> payableJsonSet) {
        this.payableJsonSet = payableJsonSet;
    }
}
