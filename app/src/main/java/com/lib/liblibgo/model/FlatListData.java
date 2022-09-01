package com.lib.liblibgo.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FlatListData {
    @SerializedName("flat_id")
    @Expose
    private String flat_id;

    @SerializedName("apartment_id")
    @Expose
    private String apartment_id;

    @SerializedName("flat_no")
    @Expose
    private String flat_no;

    public String getFlat_id() {
        return flat_id;
    }

    public void setFlat_id(String flat_id) {
        this.flat_id = flat_id;
    }

    public String getApartment_id() {
        return apartment_id;
    }

    public void setApartment_id(String apartment_id) {
        this.apartment_id = apartment_id;
    }

    public String getFlat_no() {
        return flat_no;
    }

    public void setFlat_no(String flat_no) {
        this.flat_no = flat_no;
    }
}
