package com.lib.liblibgo.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserStatusModel {

    @SerializedName("response")
    @Expose
    public ResModelData response;

    public ResModelData getResponse() {
        return response;
    }

    public class ResModelData{
        @SerializedName("code")
        @Expose
        public Integer code;

        @SerializedName("message")
        @Expose
        public String message;

        @SerializedName("status")
        @Expose
        public String status;

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

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }
}
