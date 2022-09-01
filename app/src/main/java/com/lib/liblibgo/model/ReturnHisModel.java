package com.lib.liblibgo.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class ReturnHisModel {
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

        @SerializedName("return_history")
        @Expose
        private List<DataList> return_history = new ArrayList<>();

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

        public List<DataList> getReturn_history() {
            return return_history;
        }

        public void setReturn_history(List<DataList> return_history) {
            this.return_history = return_history;
        }

        public class DataList{
            @SerializedName("issue_id")
            @Expose
            private String issue_id;

            @SerializedName("book_id")
            @Expose
            private String book_id;

            @SerializedName("shelf_no")
            @Expose
            private String shelf_no;

            @SerializedName("book_name")
            @Expose
            private String book_name;

            @SerializedName("image_url")
            @Expose
            private String image_url;

            @SerializedName("book_number")
            @Expose
            private String book_number;

            @SerializedName("author_name")
            @Expose
            private String author_name;

            @SerializedName("issue_date")
            @Expose
            private String issue_date;

            @SerializedName("return_date")
            @Expose
            private String return_date;

            public String getIssue_id() {
                return issue_id;
            }

            public void setIssue_id(String issue_id) {
                this.issue_id = issue_id;
            }

            public String getBook_id() {
                return book_id;
            }

            public void setBook_id(String book_id) {
                this.book_id = book_id;
            }

            public String getShelf_no() {
                return shelf_no;
            }

            public void setShelf_no(String shelf_no) {
                this.shelf_no = shelf_no;
            }

            public String getBook_name() {
                return book_name;
            }

            public void setBook_name(String book_name) {
                this.book_name = book_name;
            }

            public String getImage_url() {
                return image_url;
            }

            public void setImage_url(String image_url) {
                this.image_url = image_url;
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
        }

    }
}
