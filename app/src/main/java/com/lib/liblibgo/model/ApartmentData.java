package com.lib.liblibgo.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class ApartmentData {
    @SerializedName("code")
    @Expose
    private Integer code;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("apartment_list")
    @Expose
    private List<ApartmentListData> apartment_list = new ArrayList<>();

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<ApartmentListData> getApartment_list() {
        return apartment_list;
    }

    public void setApartment_list(List<ApartmentListData> apartment_list) {
        this.apartment_list = apartment_list;
    }



}
