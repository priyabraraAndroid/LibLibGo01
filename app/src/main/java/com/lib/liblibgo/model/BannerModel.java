package com.lib.liblibgo.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class BannerModel {
    @SerializedName("response")
    @Expose
    private BannerData response;

    public BannerData getResponse() {
        return response;
    }

    public void setResponse(BannerData response) {
        this.response = response;
    }

    public class BannerData {
        @SerializedName("code")
        @Expose
        private String code;

        @SerializedName("message")
        @Expose
        private String message;

        @SerializedName("banner_list")
        @Expose
        private List<BannerListData> banner_list = new ArrayList<>();

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

        public List<BannerListData> getBanner_list() {
            return banner_list;
        }

        public void setBanner_list(List<BannerListData> banner_list) {
            this.banner_list = banner_list;
        }

        public class BannerListData{
            @SerializedName("banner_id")
            @Expose
            private String banner_id;

            @SerializedName("banner")
            @Expose
            private String banner;

            public String getBanner_id() {
                return banner_id;
            }

            public void setBanner_id(String banner_id) {
                this.banner_id = banner_id;
            }

            public String getBanner() {
                return banner;
            }

            public void setBanner(String banner) {
                this.banner = banner;
            }
        }

    }
}
