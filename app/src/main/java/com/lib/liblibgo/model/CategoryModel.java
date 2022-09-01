package com.lib.liblibgo.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CategoryModel {
    @SerializedName("response")
    @Expose
    private CategoryData response;

    public CategoryData getResponse() {
        return response;
    }

    public void setResponse(CategoryData response) {
        this.response = response;
    }
}
