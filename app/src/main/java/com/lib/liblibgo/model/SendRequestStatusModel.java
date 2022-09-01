package com.lib.liblibgo.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SendRequestStatusModel {
    @SerializedName("response")
    @Expose
    public ResModelData response;

    public ResModelData getResponse() {
        return response;
    }

    public class ResModelData {
        @SerializedName("code")
        @Expose
        public String code;

        @SerializedName("request_status")
        @Expose
        public String request_status;

        @SerializedName("message")
        @Expose
        public String message;

        @SerializedName("apartment_id")
        @Expose
        public String apartment_id;

        @SerializedName("apartment_name")
        @Expose
        public String apartment_name;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getRequest_status() {
            return request_status;
        }

        public void setRequest_status(String request_status) {
            this.request_status = request_status;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }


        public String getApartment_id() {
            return apartment_id;
        }

        public void setApartment_id(String apartment_id) {
            this.apartment_id = apartment_id;
        }

        public String getApartment_name() {
            return apartment_name;
        }

        public void setApartment_name(String apartment_name) {
            this.apartment_name = apartment_name;
        }
    }
}
