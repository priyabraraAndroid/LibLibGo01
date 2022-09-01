package com.lib.liblibgo.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CheckCommunityModel {
    @SerializedName("response")
    @Expose
    public CommunityResponse response;

    public CommunityResponse getResponse() {
        return response;
    }

    public void setResponse(CommunityResponse response) {
        this.response = response;
    }

    public class CommunityResponse {
        @SerializedName("code")
        @Expose
        public String code;

        @SerializedName("message")
        @Expose
        public String message;

        @SerializedName("community_status")
        @Expose
        public String community_status;

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

        public String getCommunity_status() {
            return community_status;
        }

        public void setCommunity_status(String community_status) {
            this.community_status = community_status;
        }
    }
}
