package com.lib.liblibgo.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class NearMeLibraryModel {
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

        @SerializedName("library_list")
        @Expose
        public List<NewrmeLibraryList> library_list = new ArrayList<>();

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

        public List<NewrmeLibraryList> getLibrary_list() {
            return library_list;
        }

        public void setLibrary_list(List<NewrmeLibraryList> library_list) {
            this.library_list = library_list;
        }

        public class NewrmeLibraryList {
            @SerializedName("library_id")
            @Expose
            public String library_id;

            @SerializedName("user_id")
            @Expose
            public String user_id;

            @SerializedName("library_type")
            @Expose
            public String library_type;

            @SerializedName("library_image")
            @Expose
            public String library_image;

            @SerializedName("library_name")
            @Expose
            public String library_name;

            @SerializedName("library_address")
            @Expose
            public String library_address;

            @SerializedName("library_owner_name")
            @Expose
            public String library_owner;

            @SerializedName("is_own_library")
            @Expose
            public String is_own_library;

            @SerializedName("community_status")
            @Expose
            public String community_status;

            @SerializedName("allow_contact")
            @Expose
            public String allow_contact;

            @SerializedName("contact_no")
            @Expose
            public String contact_no;

            @SerializedName("ratings")
            @Expose
            public String ratings;

            @SerializedName("total_pending_requests")
            @Expose
            public String total_pending_requests;

            @SerializedName("total_accepted_requests")
            @Expose
            public String total_accepted_requests;

            @SerializedName("community_id")
            @Expose
            public String community_id;

            public String getLibrary_id() {
                return library_id;
            }

            public void setLibrary_id(String library_id) {
                this.library_id = library_id;
            }

            public String getUser_id() {
                return user_id;
            }

            public void setUser_id(String user_id) {
                this.user_id = user_id;
            }

            public String getLibrary_type() {
                return library_type;
            }

            public void setLibrary_type(String library_type) {
                this.library_type = library_type;
            }

            public String getLibrary_image() {
                return library_image;
            }

            public void setLibrary_image(String library_image) {
                this.library_image = library_image;
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

            public String getLibrary_owner() {
                return library_owner;
            }

            public void setLibrary_owner(String library_owner) {
                this.library_owner = library_owner;
            }


            public String getIs_own_library() {
                return is_own_library;
            }

            public void setIs_own_library(String is_own_library) {
                this.is_own_library = is_own_library;
            }

            public String getCommunity_status() {
                return community_status;
            }

            public void setCommunity_status(String community_status) {
                this.community_status = community_status;
            }

            public String getAllow_contact() {
                return allow_contact;
            }

            public void setAllow_contact(String allow_contact) {
                this.allow_contact = allow_contact;
            }

            public String getContact_no() {
                return contact_no;
            }

            public void setContact_no(String contact_no) {
                this.contact_no = contact_no;
            }

            public String getRatings() {
                return ratings;
            }

            public void setRatings(String ratings) {
                this.ratings = ratings;
            }

            public String getTotal_pending_requests() {
                return total_pending_requests;
            }

            public void setTotal_pending_requests(String total_pending_requests) {
                this.total_pending_requests = total_pending_requests;
            }

            public String getTotal_accepted_requests() {
                return total_accepted_requests;
            }

            public void setTotal_accepted_requests(String total_accepted_requests) {
                this.total_accepted_requests = total_accepted_requests;
            }

            public String getCommunity_id() {
                return community_id;
            }

            public void setCommunity_id(String community_id) {
                this.community_id = community_id;
            }
        }
    }
}
