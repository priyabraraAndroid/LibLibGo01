package com.lib.liblibgo.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class ApartmentNewModel {
    @SerializedName("response")
    @Expose
    private ApartNewRes response;


    public ApartNewRes getResponse() {
        return response;
    }

    public void setResponse(ApartNewRes response) {
        this.response = response;
    }

    public class ApartNewRes{
        @SerializedName("code")
        @Expose
        private String code;

        @SerializedName("message")
        @Expose
        private String message;

        @SerializedName("apartment_list")
        @Expose
        private List<AprtList> apartment_list = new ArrayList<>();


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

        public List<AprtList> getApartment_list() {
            return apartment_list;
        }

        public void setApartment_list(List<AprtList> apartment_list) {
            this.apartment_list = apartment_list;
        }

        public class AprtList{
            @SerializedName("id")
            @Expose
            private String id;

            @SerializedName("pincode")
            @Expose
            private String pincode;

            @SerializedName("apartment_name")
            @Expose
            private String apartment_name;

            @SerializedName("dated")
            @Expose
            private String dated;

            @SerializedName("status")
            @Expose
            private String status;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getPincode() {
                return pincode;
            }

            public void setPincode(String pincode) {
                this.pincode = pincode;
            }

            public String getApartment_name() {
                return apartment_name;
            }

            public void setApartment_name(String apartment_name) {
                this.apartment_name = apartment_name;
            }

            public String getDated() {
                return dated;
            }

            public void setDated(String dated) {
                this.dated = dated;
            }

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }
        }
    }
}
