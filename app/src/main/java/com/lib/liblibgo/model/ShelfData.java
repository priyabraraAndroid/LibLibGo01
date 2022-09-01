package com.lib.liblibgo.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class ShelfData {
    @SerializedName("code")
    @Expose
    private Integer code;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("cell_list")
    @Expose
    private List<ShelfListData> cell_list = new ArrayList<>();


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

    public List<ShelfListData> getCell_list() {
        return cell_list;
    }

    public void setCell_list(List<ShelfListData> cell_list) {
        this.cell_list = cell_list;
    }
}
