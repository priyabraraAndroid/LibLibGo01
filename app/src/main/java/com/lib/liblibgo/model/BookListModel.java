package com.lib.liblibgo.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class BookListModel {
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

        @SerializedName("book_list")
        @Expose
        private List<BookListData> book_list = new ArrayList<>();

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

        public List<BookListData> getBook_list() {
            return book_list;
        }

        public void setBook_list(List<BookListData> book_list) {
            this.book_list = book_list;
        }

        public class BookListData{
            @SerializedName("book_id")
            @Expose
            private String book_id;

            @SerializedName("book_number")
            @Expose
            private String book_number;

            @SerializedName("book_name")
            @Expose
            private String book_name;

            @SerializedName("author_name")
            @Expose
            private String author_name;

            @SerializedName("issue_status")
            @Expose
            private String issue_status;

            @SerializedName("flag_status")
            @Expose
            private String flag_status;

            @SerializedName("publish_date")
            @Expose
            private String publish_date;

            @SerializedName("isbn_no")
            @Expose
            private String isbn_no;

            @SerializedName("apartment_id")
            @Expose
            private String apartment_id;

            @SerializedName("apartment_name")
            @Expose
            private String apartment_name;

            public String getBook_id() {
                return book_id;
            }

            public void setBook_id(String book_id) {
                this.book_id = book_id;
            }

            public String getBook_number() {
                return book_number;
            }

            public void setBook_number(String book_number) {
                this.book_number = book_number;
            }

            public String getBook_name() {
                return book_name;
            }

            public void setBook_name(String book_name) {
                this.book_name = book_name;
            }

            public String getAuthor_name() {
                return author_name;
            }

            public void setAuthor_name(String author_name) {
                this.author_name = author_name;
            }

            public String getIssue_status() {
                return issue_status;
            }

            public void setIssue_status(String issue_status) {
                this.issue_status = issue_status;
            }

            public String getFlag_status() {
                return flag_status;
            }

            public void setFlag_status(String flag_status) {
                this.flag_status = flag_status;
            }

            public String getPublish_date() {
                return publish_date;
            }

            public void setPublish_date(String publish_date) {
                this.publish_date = publish_date;
            }

            public String getIsbn_no() {
                return isbn_no;
            }

            public void setIsbn_no(String isbn_no) {
                this.isbn_no = isbn_no;
            }

            public String getApartment_id() {
                return apartment_id;
            }

            public void setApartment_id(String apartment_id) {
                this.apartment_id = apartment_id;
            }

            public String getApartment_name() {
                return apartment_name;
            }

            public void setApartment_name(String apartment_name) {
                this.apartment_name = apartment_name;
            }
        }

    }
}
