package com.lib.liblibgo.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FlatModel {
    @SerializedName("response")
    @Expose
    private FlatData response;

    public FlatData getResponse() {
        return response;
    }

    public void setResponse(FlatData response) {
        this.response = response;
    }

}
