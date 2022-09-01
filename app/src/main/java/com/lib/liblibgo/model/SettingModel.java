package com.lib.liblibgo.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SettingModel {
    @SerializedName("response")
    @Expose
    private ResData response;

    public ResData getResponse() {
        return response;
    }

    public void setResponse(ResData response) {
        this.response = response;
    }

    public class ResData {
        @SerializedName("code")
        @Expose
        private String code;

        @SerializedName("message")
        @Expose
        private String message;

        @SerializedName("add_book_notification")
        @Expose
        private String add_book_notification;

        @SerializedName("add_book_community_notification")
        @Expose
        private String add_book_community_notification;

        @SerializedName("notify_notification")
        @Expose
        private String notify_notification;

        @SerializedName("demanded_book_nofification")
        @Expose
        private String demanded_book_nofification;

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

        public String getAdd_book_notification() {
            return add_book_notification;
        }

        public void setAdd_book_notification(String add_book_notification) {
            this.add_book_notification = add_book_notification;
        }

        public String getAdd_book_community_notification() {
            return add_book_community_notification;
        }

        public void setAdd_book_community_notification(String add_book_community_notification) {
            this.add_book_community_notification = add_book_community_notification;
        }

        public String getNotify_notification() {
            return notify_notification;
        }

        public void setNotify_notification(String notify_notification) {
            this.notify_notification = notify_notification;
        }

        public String getDemanded_book_nofification() {
            return demanded_book_nofification;
        }

        public void setDemanded_book_nofification(String demanded_book_nofification) {
            this.demanded_book_nofification = demanded_book_nofification;
        }
    }
}
