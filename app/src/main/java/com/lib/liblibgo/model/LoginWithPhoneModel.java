package com.lib.liblibgo.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoginWithPhoneModel {
    @SerializedName("response")
    @Expose
    public ResModelData response;

    public ResModelData getResponse() {
        return response;
    }

    public class ResModelData{
        @SerializedName("code")
        @Expose
        public int code;

        @SerializedName("message")
        @Expose
        public String message;

        @SerializedName("otp")
        @Expose
        public String otp;

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

        public String getOtp() {
            return otp;
        }

        public void setOtp(String otp) {
            this.otp = otp;
        }
    }

}
