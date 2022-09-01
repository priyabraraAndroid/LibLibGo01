package com.lib.liblibgo.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class MyOederModel {
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

        @SerializedName("order_list")
        @Expose
        private List<MyOrderList> order_list = new ArrayList<>();


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

        public List<MyOrderList> getOrder_list() {
            return order_list;
        }

        public void setOrder_list(List<MyOrderList> order_list) {
            this.order_list = order_list;
        }

        public class MyOrderList {
            @SerializedName("order_id")
            @Expose
            private String order_id;

            @SerializedName("order_number")
            @Expose
            private String order_number;

            @SerializedName("book_id")
            @Expose
            private String book_id;

            @SerializedName("book_name")
            @Expose
            private String book_name;

            @SerializedName("order_date")
            @Expose
            private String order_date;


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

            public String getBook_id() {
                return book_id;
            }

            public void setBook_id(String book_id) {
                this.book_id = book_id;
            }

            public String getBook_name() {
                return book_name;
            }

            public void setBook_name(String book_name) {
                this.book_name = book_name;
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
