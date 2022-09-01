package com.lib.liblibgo.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class CommunityRequestModel {
    @SerializedName("response")
    @Expose
    public ResDataBooks response;

    public ResDataBooks getResponse() {
        return response;
    }

    public void setResponse(ResDataBooks response) {
        this.response = response;
    }

    public class ResDataBooks {
        @SerializedName("code")
        @Expose
        public String code;

        @SerializedName("message")
        @Expose
        public String message;

        @SerializedName("pending_requests")
        @Expose
        public String pending_request;

        @SerializedName("request_list")
        @Expose
        public List<ReqList> request_list = new ArrayList<>();

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

        public List<ReqList> getRequest_list() {
            return request_list;
        }

        public void setRequest_list(List<ReqList> request_list) {
            this.request_list = request_list;
        }

        public String getPending_request() {
            return pending_request;
        }

        public void setPending_request(String pending_request) {
            this.pending_request = pending_request;
        }

        public class ReqList{
            @SerializedName("community_id")
            @Expose
            public String community_id;

            @SerializedName("request_user_id")
            @Expose
            public String request_user_id;

            @SerializedName("request_user_name")
            @Expose
            public String request_user_name;

            @SerializedName("request_user_mobile")
            @Expose
            public String request_user_mobile;

            @SerializedName("library_id")
            @Expose
            public String library_id;

            @SerializedName("library_name")
            @Expose
            public String library_name;

            @SerializedName("community_status")
            @Expose
            public String community_status;

            @SerializedName("requested_date")
            @Expose
            public String requested_date;

            public String getCommunity_id() {
                return community_id;
            }

            public void setCommunity_id(String community_id) {
                this.community_id = community_id;
            }

            public String getRequest_user_id() {
                return request_user_id;
            }

            public void setRequest_user_id(String request_user_id) {
                this.request_user_id = request_user_id;
            }

            public String getRequest_user_name() {
                return request_user_name;
            }

            public void setRequest_user_name(String request_user_name) {
                this.request_user_name = request_user_name;
            }

            public String getRequest_user_mobile() {
                return request_user_mobile;
            }

            public void setRequest_user_mobile(String request_user_mobile) {
                this.request_user_mobile = request_user_mobile;
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

            public String getCommunity_status() {
                return community_status;
            }

            public void setCommunity_status(String community_status) {
                this.community_status = community_status;
            }

            public String getRequested_date() {
                return requested_date;
            }

            public void setRequested_date(String requested_date) {
                this.requested_date = requested_date;
            }
        }

    }
}
