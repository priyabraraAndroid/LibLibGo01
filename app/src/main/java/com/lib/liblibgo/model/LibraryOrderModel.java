package com.lib.liblibgo.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class LibraryOrderModel {
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

        @SerializedName("order_details")
        @Expose
        private List<MyOrderList> order_details = new ArrayList<>();

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

        public List<MyOrderList> getOrder_details() {
            return order_details;
        }

        public void setOrder_details(List<MyOrderList> order_details) {
            this.order_details = order_details;
        }

        public class MyOrderList{
            @SerializedName("order_detail_id")
            @Expose
            private String order_detail_id;

            @SerializedName("order_id")
            @Expose
            private String order_id;

            @SerializedName("order_number")
            @Expose
            private String order_number;

            @SerializedName("customer_name")
            @Expose
            private String customer_name;

            @SerializedName("customer_id")
            @Expose
            private String customer_id;

            @SerializedName("customer_mobile")
            @Expose
            private String customer_mobile;

            @SerializedName("customer_address")
            @Expose
            private String customer_address;

            @SerializedName("book_name")
            @Expose
            private String book_name;

            @SerializedName("amount")
            @Expose
            private String amount;

            @SerializedName("quantity")
            @Expose
            private String quantity;

            @SerializedName("order_for")
            @Expose
            private String order_for;

            @SerializedName("order_status")
            @Expose
            private String order_status;

            @SerializedName("order_date")
            @Expose
            private String order_date;

            public String getOrder_detail_id() {
                return order_detail_id;
            }

            public void setOrder_detail_id(String order_detail_id) {
                this.order_detail_id = order_detail_id;
            }

            public String getOrder_id() {
                return order_id;
            }

            public void setOrder_id(String order_id) {
                this.order_id = order_id;
            }

            public String getOrder_number() {
                return order_number;
            }

            public void setOrder_number(String order_number) {
                this.order_number = order_number;
            }

            public String getCustomer_name() {
                return customer_name;
            }

            public void setCustomer_name(String customer_name) {
                this.customer_name = customer_name;
            }

            public String getCustomer_id() {
                return customer_id;
            }

            public void setCustomer_id(String customer_id) {
                this.customer_id = customer_id;
            }

            public String getCustomer_mobile() {
                return customer_mobile;
            }

            public void setCustomer_mobile(String customer_mobile) {
                this.customer_mobile = customer_mobile;
            }

            public String getCustomer_address() {
                return customer_address;
            }

            public void setCustomer_address(String customer_address) {
                this.customer_address = customer_address;
            }

            public String getBook_name() {
                return book_name;
            }

            public void setBook_name(String book_name) {
                this.book_name = book_name;
            }

            public String getAmount() {
                return amount;
            }

            public void setAmount(String amount) {
                this.amount = amount;
            }

            public String getQuantity() {
                return quantity;
            }

            public void setQuantity(String quantity) {
                this.quantity = quantity;
            }

            public String getOrder_for() {
                return order_for;
            }

            public void setOrder_for(String order_for) {
                this.order_for = order_for;
            }

            public String getOrder_status() {
                return order_status;
            }

            public void setOrder_status(String order_status) {
                this.order_status = order_status;
            }

            public String getOrder_date() {
                return order_date;
            }

            public void setOrder_date(String order_date) {
                this.order_date = order_date;
            }
        }
    }
}
