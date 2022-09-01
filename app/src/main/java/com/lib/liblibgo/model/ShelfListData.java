package com.lib.liblibgo.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ShelfListData {
    @SerializedName("$shelf_id")
    @Expose
    private String $shelf_id;
    @SerializedName("shelf_no")
    @Expose
    private String shelf_no;

    public String get$shelf_id() {
        return $shelf_id;
    }

    public void set$shelf_id(String $shelf_id) {
        this.$shelf_id = $shelf_id;
    }

    public String getShelf_no() {
        return shelf_no;
    }

    public void setShelf_no(String shelf_no) {
        this.shelf_no = shelf_no;
    }
}