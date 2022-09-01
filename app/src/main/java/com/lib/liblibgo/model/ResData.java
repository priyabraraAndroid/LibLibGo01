package com.lib.liblibgo.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class ResData {
        @SerializedName("code")
        @Expose
        public Integer code;
        @SerializedName("message")
        @Expose
        public String message;

        @SerializedName("book_list")
        @Expose
        private List<BookModel> bookModels;

        @SerializedName("book_issue_list")
        @Expose
        private List<BookModel> book_issue_list;

        @SerializedName("apartment_list")
        @Expose
        private List<ApartmentModel> apartmentModel = null;
        @SerializedName("return_history")
        @Expose
        private ArrayList<BookModel> return_history = null;

        public List<BookModel> getBook_issue_list() {
            return book_issue_list;
        }

        public void setBook_issue_list(List<BookModel> book_issue_list) {
            this.book_issue_list = book_issue_list;
        }

        @SerializedName("user_id")
        @Expose
        private String userId;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("email")
        @Expose
        private String email;
        @SerializedName("mobile")
        @Expose
        private String mobile;
        @SerializedName("flat_no")
        @Expose
        private String flatNo;

        @SerializedName("address")
        @Expose
        public String address;

        @SerializedName("pincode")
        @Expose
        public String pincode;

        @SerializedName("apartment_id")
        @Expose
        private String apartmentId;
        @SerializedName("apartment_name")
        @Expose
        private String apartmentName;

        public ArrayList<BookModel> getReturn_history() {
            return return_history;
        }

        public void setReturn_history(ArrayList<BookModel> return_history) {
            this.return_history = return_history;
        }

        public List<BookModel> getBookModels() {
            return bookModels;
        }

        public void setBookModels(List<BookModel> bookModels) {
            this.bookModels = bookModels;
        }

        public Integer getCode() {
            return code;
        }

        public void setCode(Integer code) {
            this.code = code;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getFlatNo() {
            return flatNo;
        }

        public void setFlatNo(String flatNo) {
            this.flatNo = flatNo;
        }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getApartmentId() {
            return apartmentId;
        }

        public void setApartmentId(String apartmentId) {
            this.apartmentId = apartmentId;
        }

        public String getApartmentName() {
            return apartmentName;
        }

        public void setApartmentName(String apartmentName) {
            this.apartmentName = apartmentName;
        }

        public List<ApartmentModel> getApartmentModel() {
            return apartmentModel;
        }

        public void setApartmentModel(List<ApartmentModel> apartmentModel) {
            this.apartmentModel = apartmentModel;
        }
    }

