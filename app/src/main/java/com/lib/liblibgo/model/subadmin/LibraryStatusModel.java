package com.lib.liblibgo.model.subadmin;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LibraryStatusModel {
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

        @SerializedName("message")
        @Expose
        public String message;

        @SerializedName("library_status")
        @Expose
        public String library_status;

        @SerializedName("is_active")
        @Expose
        public String is_active;

        @SerializedName("library_type")
        @Expose
        public String library_type;

        @SerializedName("apartment_id")
        @Expose
        public String apartment_id;

        @SerializedName("apartment_name")
        @Expose
        public String apartment_name;

        @SerializedName("flat_id")
        @Expose
        public String flat_id;

        @SerializedName("flat_no")
        @Expose
        public String flat_no;

        @SerializedName("my_accepted_count")
        @Expose
        public String my_accepted_count;

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

        public String getLibrary_status() {
            return library_status;
        }

        public void setLibrary_status(String library_status) {
            this.library_status = library_status;
        }

        public String getIs_active() {
            return is_active;
        }

        public void setIs_active(String is_active) {
            this.is_active = is_active;
        }

        public String getLibrary_type() {
            return library_type;
        }

        public void setLibrary_type(String library_type) {
            this.library_type = library_type;
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

        public String getFlat_id() {
            return flat_id;
        }

        public void setFlat_id(String flat_id) {
            this.flat_id = flat_id;
        }

        public String getFlat_no() {
            return flat_no;
        }

        public void setFlat_no(String flat_no) {
            this.flat_no = flat_no;
        }

        public String getMy_accepted_count() {
            return my_accepted_count;
        }

        public void setMy_accepted_count(String my_accepted_count) {
            this.my_accepted_count = my_accepted_count;
        }
    }
}
