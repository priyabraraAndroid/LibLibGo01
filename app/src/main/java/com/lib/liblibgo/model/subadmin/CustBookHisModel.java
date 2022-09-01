package com.lib.liblibgo.model.subadmin;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class CustBookHisModel {

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

        @SerializedName("book_history")
        @Expose
        private List<BookList> book_history = new ArrayList<>();

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

        public List<BookList> getBook_history() {
            return book_history;
        }

        public void setBook_history(List<BookList> book_history) {
            this.book_history = book_history;
        }

        public class BookList{
            @SerializedName("book_issue_id")
            @Expose
            private String book_issue_id;

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

            @SerializedName("book_issue_date")
            @Expose
            private String book_issue_date;

            @SerializedName("book_return_date")
            @Expose
            private String book_return_date;

            public String getBook_issue_id() {
                return book_issue_id;
            }

            public void setBook_issue_id(String book_issue_id) {
                this.book_issue_id = book_issue_id;
            }

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

            public String getBook_issue_date() {
                return book_issue_date;
            }

            public void setBook_issue_date(String book_issue_date) {
                this.book_issue_date = book_issue_date;
            }

            public String getBook_return_date() {
                return book_return_date;
            }

            public void setBook_return_date(String book_return_date) {
                this.book_return_date = book_return_date;
            }
        }

    }
}
