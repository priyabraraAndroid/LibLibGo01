package com.lib.liblibgo.model.subadmin;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class BookHisModel {
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

        @SerializedName("customer_list")
        @Expose
        private List<CustomerList> customer_list = new ArrayList<>();

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

        public List<CustomerList> getCustomer_list() {
            return customer_list;
        }

        public void setCustomer_list(List<CustomerList> customer_list) {
            this.customer_list = customer_list;
        }

        public class CustomerList{
            @SerializedName("book_id")
            @Expose
            private String book_id;

            @SerializedName("book_name")
            @Expose
            private String book_name;

            @SerializedName("book_number")
            @Expose
            private String book_number;

            @SerializedName("author_name")
            @Expose
            private String author_name;

            @SerializedName("isbn_no")
            @Expose
            private String isbn_no;

            @SerializedName("issue_date")
            @Expose
            private String issue_date;

            @SerializedName("return_date")
            @Expose
            private String return_date;

            @SerializedName("return_status")
            @Expose
            private String return_status;

            @SerializedName("customer_name")
            @Expose
            private String customer_name;

            @SerializedName("customer_email")
            @Expose
            private String customer_email;

            @SerializedName("customer_mobile")
            @Expose
            private String customer_mobile;

            @SerializedName("apartment")
            @Expose
            private String apartment;

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

            public String getBook_number() {
                return book_number;
            }

            public void setBook_number(String book_number) {
                this.book_number = book_number;
            }

            public String getAuthor_name() {
                return author_name;
            }

            public void setAuthor_name(String author_name) {
                this.author_name = author_name;
            }

            public String getIsbn_no() {
                return isbn_no;
            }

            public void setIsbn_no(String isbn_no) {
                this.isbn_no = isbn_no;
            }

            public String getIssue_date() {
                return issue_date;
            }

            public void setIssue_date(String issue_date) {
                this.issue_date = issue_date;
            }

            public String getReturn_date() {
                return return_date;
            }

            public void setReturn_date(String return_date) {
                this.return_date = return_date;
            }

            public String getReturn_status() {
                return return_status;
            }

            public void setReturn_status(String return_status) {
                this.return_status = return_status;
            }

            public String getCustomer_name() {
                return customer_name;
            }

            public void setCustomer_name(String customer_name) {
                this.customer_name = customer_name;
            }

            public String getCustomer_email() {
                return customer_email;
            }

            public void setCustomer_email(String customer_email) {
                this.customer_email = customer_email;
            }

            public String getCustomer_mobile() {
                return customer_mobile;
            }

            public void setCustomer_mobile(String customer_mobile) {
                this.customer_mobile = customer_mobile;
            }

            public String getApartment() {
                return apartment;
            }

            public void setApartment(String apartment) {
                this.apartment = apartment;
            }
        }
    }
}
