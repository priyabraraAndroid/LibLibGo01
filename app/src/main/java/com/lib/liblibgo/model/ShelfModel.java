package com.lib.liblibgo.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ShelfModel {

    @SerializedName("response")
    @Expose
    private ShelfData response;

    public ShelfData getResponse() {
        return response;
    }

    public void setResponse(ShelfData response) {
        this.response = response;
    }


}
