package com.lib.liblibgo.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class NotificationModel {
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

        @SerializedName("notifications")
        @Expose
        private List<NotificationList> notifications = new ArrayList<>();

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

        public List<NotificationList> getNotifications() {
            return notifications;
        }

        public void setNotifications(List<NotificationList> notifications) {
            this.notifications = notifications;
        }

        public class NotificationList {
            @SerializedName("notification_id")
            @Expose
            private String notification_id;

            @SerializedName("username")
            @Expose
            private String username;

            @SerializedName("title")
            @Expose
            private String title;

            @SerializedName("read_status")
            @Expose
            private String read_status;

            @SerializedName("message")
            @Expose
            private String message;

            @SerializedName("order_id")
            @Expose
            private String order_id;

            @SerializedName("notification_status")
            @Expose
            private String notification_status;

            @SerializedName("community_id")
            @Expose
            private String community_id;

            @SerializedName("library_id")
            @Expose
            private String library_id;

            @SerializedName("library_name")
            @Expose
            private String library_name;

            @SerializedName("dated")
            @Expose
            private String dated;

            public String getNotification_id() {
                return notification_id;
            }

            public void setNotification_id(String notification_id) {
                this.notification_id = notification_id;
            }

            public String getUsername() {
                return username;
            }

            public void setUsername(String username) {
                this.username = username;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getRead_status() {
                return read_status;
            }

            public void setRead_status(String read_status) {
                this.read_status = read_status;
            }

            public String getMessage() {
                return message;
            }

            public void setMessage(String message) {
                this.message = message;
            }

            public String getOrder_id() {
                return order_id;
            }

            public void setOrder_id(String order_id) {
                this.order_id = order_id;
            }

            public String getNotification_status() {
                return notification_status;
            }

            public void setNotification_status(String notification_status) {
                this.notification_status = notification_status;
            }

            public String getCommunity_id() {
                return community_id;
            }

            public void setCommunity_id(String community_id) {
                this.community_id = community_id;
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

            public String getDated() {
                return dated;
            }

            public void setDated(String dated) {
                this.dated = dated;
            }
        }
    }
}
