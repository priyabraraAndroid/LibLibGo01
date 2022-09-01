package com.lib.liblibgo.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class GiftCardModel {
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

        @SerializedName("request_list")
        @Expose
        public List<RequestList> request_list = new ArrayList<>();

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

        public List<RequestList> getRequest_list() {
            return request_list;
        }

        public void setRequest_list(List<RequestList> request_list) {
            this.request_list = request_list;
        }

        public class RequestList{
            @SerializedName("id")
            @Expose
            public String id;

            @SerializedName("quantity")
            @Expose
            public String quantity;

            @SerializedName("gift_value")
            @Expose
            public String gift_value;

            @SerializedName("coupon_code")
            @Expose
            public String coupon_code;

            @SerializedName("card_status")
            @Expose
            public String card_status;

            @SerializedName("request_date")
            @Expose
            public String request_date;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getQuantity() {
                return quantity;
            }

            public void setQuantity(String quantity) {
                this.quantity = quantity;
            }

            public String getGift_value() {
                return gift_value;
            }

            public void setGift_value(String gift_value) {
                this.gift_value = gift_value;
            }

            public String getCoupon_code() {
                return coupon_code;
            }

            public void setCoupon_code(String coupon_code) {
                this.coupon_code = coupon_code;
            }

            public String getCard_status() {
                return card_status;
            }

            public void setCard_status(String card_status) {
                this.card_status = card_status;
            }

            public String getRequest_date() {
                return request_date;
            }

            public void setRequest_date(String request_date) {
                this.request_date = request_date;
            }
        }
    }
}
