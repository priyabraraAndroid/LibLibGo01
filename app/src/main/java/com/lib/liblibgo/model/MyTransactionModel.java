package com.lib.liblibgo.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class MyTransactionModel {
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

        @SerializedName("history_list")
        @Expose
        private List<TransList> history_list = new ArrayList<>();

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

        public List<TransList> getHistory_list() {
            return history_list;
        }

        public void setHistory_list(List<TransList> history_list) {
            this.history_list = history_list;
        }

        public class TransList {
            @SerializedName("user_id")
            @Expose
            private String user_id;

            @SerializedName("transaction_for")
            @Expose
            private String transaction_for;

            @SerializedName("transaction_type")
            @Expose
            private String transaction_type;

            @SerializedName("book_id")
            @Expose
            private String book_id;

            @SerializedName("book_name")
            @Expose
            private String book_name;

            @SerializedName("library_id")
            @Expose
            private String library_id;

            @SerializedName("library_name")
            @Expose
            private String library_name;

            @SerializedName("coins")
            @Expose
            private String coins;

            @SerializedName("order_for")
            @Expose
            private String order_for;

            @SerializedName("iniate_status")
            @Expose
            private String iniate_status;

            @SerializedName("transaction_date")
            @Expose
            private String transaction_date;


            public String getUser_id() {
                return user_id;
            }

            public void setUser_id(String user_id) {
                this.user_id = user_id;
            }

            public String getTransaction_for() {
                return transaction_for;
            }

            public void setTransaction_for(String transaction_for) {
                this.transaction_for = transaction_for;
            }

            public String getTransaction_type() {
                return transaction_type;
            }

            public void setTransaction_type(String transaction_type) {
                this.transaction_type = transaction_type;
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

            public String getCoins() {
                return coins;
            }

            public void setCoins(String coins) {
                this.coins = coins;
            }

            public String getOrder_for() {
                return order_for;
            }

            public void setOrder_for(String order_for) {
                this.order_for = order_for;
            }

            public String getIniate_status() {
                return iniate_status;
            }

            public void setIniate_status(String iniate_status) {
                this.iniate_status = iniate_status;
            }

            public String getTransaction_date() {
                return transaction_date;
            }

            public void setTransaction_date(String transaction_date) {
                this.transaction_date = transaction_date;
            }
        }

    }
}
