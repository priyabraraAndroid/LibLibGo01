package com.lib.liblibgo.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VerifyOTPModel {
    @SerializedName("response")
    @Expose
    public ResModelData response;

    public ResModelData getResponse() {
        return response;
    }

    public class ResModelData{
        @SerializedName("code")
        @Expose
        public String code;

        @SerializedName("message")
        @Expose
        public String message;

        @SerializedName("user_id")
        @Expose
        public String user_id;

        @SerializedName("user_name")
        @Expose
        public String user_name;

        @SerializedName("mobile")
        @Expose
        public String mobile;

        @SerializedName("address")
        @Expose
        public String address;

        @SerializedName("pincode")
        @Expose
        public String pincode;

        @SerializedName("apartment_status")
        @Expose
        public String apartment_status;

        @SerializedName("apartment_name")
        @Expose
        public String apartment_name;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public String getUser_name() {
            return user_name;
        }

        public void setUser_name(String user_name) {
            this.user_name = user_name;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getPincode() {
            return pincode;
        }

        public void setPincode(String pincode) {
            this.pincode = pincode;
        }

        public String getApartment_status() {
            return apartment_status;
        }

        public void setApartment_status(String apartment_status) {
            this.apartment_status = apartment_status;
        }

        public String getApartment_name() {
            return apartment_name;
        }

        public void setApartment_name(String apartment_name) {
            this.apartment_name = apartment_name;
        }
    }

}
