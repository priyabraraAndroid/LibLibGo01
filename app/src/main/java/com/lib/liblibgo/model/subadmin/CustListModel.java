package com.lib.liblibgo.model.subadmin;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class CustListModel {
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
        private List<ListData> customer_list = new ArrayList<>();

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

        public List<ListData> getCustomer_list() {
            return customer_list;
        }

        public void setCustomer_list(List<ListData> customer_list) {
            this.customer_list = customer_list;
        }

        public class ListData{
            @SerializedName("customer_id")
            @Expose
            private String customer_id;

            @SerializedName("user_name")
            @Expose
            private String user_name;

            @SerializedName("apartment_id")
            @Expose
            private String apartment_id;

            @SerializedName("apartment_name")
            @Expose
            private String apartment_name;

            @SerializedName("mobile_number")
            @Expose
            private String mobile_number;

            @SerializedName("email")
            @Expose
            private String email;

            @SerializedName("flat_no")
            @Expose
            private String flat_no;

            @SerializedName("book_return_status")
            @Expose
            private String book_return_status;

            public String getCustomer_id() {
                return customer_id;
            }

            public void setCustomer_id(String customer_id) {
                this.customer_id = customer_id;
            }

            public String getApartment_name() {
                return apartment_name;
            }

            public void setApartment_name(String apartment_name) {
                this.apartment_name = apartment_name;
            }

            public String getUser_name() {
                return user_name;
            }

            public void setUser_name(String user_name) {
                this.user_name = user_name;
            }

            public String getApartment_id() {
                return apartment_id;
            }

            public void setApartment_id(String apartment_id) {
                this.apartment_id = apartment_id;
            }

            public String getMobile_number() {
                return mobile_number;
            }

            public void setMobile_number(String mobile_number) {
                this.mobile_number = mobile_number;
            }

            public String getEmail() {
                return email;
            }

            public void setEmail(String email) {
                this.email = email;
            }

            public String getFlat_no() {
                return flat_no;
            }

            public void setFlat_no(String flat_no) {
                this.flat_no = flat_no;
            }

            public String getBook_return_status() {
                return book_return_status;
            }

            public void setBook_return_status(String book_return_status) {
                this.book_return_status = book_return_status;
            }
        }

    }
}
