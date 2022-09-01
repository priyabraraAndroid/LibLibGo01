package com.lib.liblibgo.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResModel {
    @SerializedName("response")
    @Expose
    public ResData response;

    public ResData getResponse() {
        return response;
    }
}
