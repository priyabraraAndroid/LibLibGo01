package com.lib.liblibgo.model.subadmin;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OwnLibraryProfModel {
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

        @SerializedName("user_id")
        @Expose
        public String user_id;

        @SerializedName("name")
        @Expose
        public String name;

        @SerializedName("username")
        @Expose
        public String username;

        @SerializedName("email")
        @Expose
        public String email;

        @SerializedName("mobile")
        @Expose
        public String mobile;

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

        @SerializedName("library_id")
        @Expose
        public String library_id;

        @SerializedName("library_name")
        @Expose
        public String library_name;

        @SerializedName("library_address")
        @Expose
        public String library_address;

        @SerializedName("libcoins")
        @Expose
        public String libcoins;

        @SerializedName("library_image")
        @Expose
        public String library_image;

        @SerializedName("my_accepted_count")
        @Expose
        public String my_accepted_count;

        @SerializedName("my_accepted_user")
        @Expose
        public String my_accepted_user;

        @SerializedName("my_pending_user")
        @Expose
        public String my_pending_user;

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

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
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

        public String getLibrary_id() {
            return library_id;
        }

        public void setLibrary_id(String library_id) {
            this.library_id = library_id;
        }

        public String getLibrary_name() {
            return library_name;
        }

        public void setLibrary_name(String library_name) {
            this.library_name = library_name;
        }

        public String getLibrary_address() {
            return library_address;
        }

        public void setLibrary_address(String library_address) {
            this.library_address = library_address;
        }

        public String getLibcoins() {
            return libcoins;
        }

        public void setLibcoins(String libcoins) {
            this.libcoins = libcoins;
        }

        public String getLibrary_image() {
            return library_image;
        }

        public void setLibrary_image(String library_image) {
            this.library_image = library_image;
        }

        public String getMy_accepted_count() {
            return my_accepted_count;
        }

        public void setMy_accepted_count(String my_accepted_count) {
            this.my_accepted_count = my_accepted_count;
        }

        public String getMy_accepted_user() {
            return my_accepted_user;
        }

        public void setMy_accepted_user(String my_accepted_user) {
            this.my_accepted_user = my_accepted_user;
        }

        public String getMy_pending_user() {
            return my_pending_user;
        }

        public void setMy_pending_user(String my_pending_user) {
            this.my_pending_user = my_pending_user;
        }
    }
}
