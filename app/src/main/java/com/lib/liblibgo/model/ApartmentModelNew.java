package com.lib.liblibgo.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ApartmentModelNew {

    @SerializedName("response")
    @Expose
    private ApartmentData response;

    public ApartmentData getResponse() {
        return response;
    }

    public void setResponse(ApartmentData response) {
        this.response = response;
    }


}
