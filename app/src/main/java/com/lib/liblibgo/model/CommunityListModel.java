package com.lib.liblibgo.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class CommunityListModel {
    @SerializedName("response")
    @Expose
    public ResDataBooks response;

    public ResDataBooks getResponse() {
        return response;
    }

    public void setResponse(ResDataBooks response) {
        this.response = response;
    }

    public class ResDataBooks {
        @SerializedName("code")
        @Expose
        public String code;

        @SerializedName("message")
        @Expose
        public String message;

        @SerializedName("library_id")
        @Expose
        public String library_id;

        @SerializedName("own_library_name")
        @Expose
        public String own_library_name;

        @SerializedName("own_library_address")
        @Expose
        public String own_library_address;

        @SerializedName("own_library_image")
        @Expose
        public String own_library_image;

        @SerializedName("community_library_status")
        @Expose
        public String community_library_status;

        @SerializedName("community_list")
        @Expose
        public List<CommunityList> community_list = new ArrayList<>();

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

        public String getLibrary_id() {
            return library_id;
        }

        public void setLibrary_id(String library_id) {
            this.library_id = library_id;
        }

        public String getOwn_library_name() {
            return own_library_name;
        }

        public void setOwn_library_name(String own_library_name) {
            this.own_library_name = own_library_name;
        }

        public String getOwn_library_address() {
            return own_library_address;
        }

        public void setOwn_library_address(String own_library_address) {
            this.own_library_address = own_library_address;
        }

        public String getOwn_library_image() {
            return own_library_image;
        }

        public void setOwn_library_image(String own_library_image) {
            this.own_library_image = own_library_image;
        }

        public String getCommunity_library_status() {
            return community_library_status;
        }

        public void setCommunity_library_status(String community_library_status) {
            this.community_library_status = community_library_status;
        }

        public List<CommunityList> getCommunity_list() {
            return community_list;
        }

        public void setCommunity_list(List<CommunityList> community_list) {
            this.community_list = community_list;
        }

        public class CommunityList{
            @SerializedName("community_id")
            @Expose
            public String community_id;

            @SerializedName("community_library_id")
            @Expose
            public String community_library_id;

            @SerializedName("community_library_name")
            @Expose
            public String community_library_name;

            @SerializedName("community_library_address")
            @Expose
            public String community_library_address;

            @SerializedName("community_library_image")
            @Expose
            public String community_library_image;

            @SerializedName("is_own_library_status")
            @Expose
            public String is_own_library_status;

            @SerializedName("isSelected")
            @Expose
            private boolean isSelected;

            public String getCommunity_id() {
                return community_id;
            }

            public void setCommunity_id(String community_id) {
                this.community_id = community_id;
            }

            public String getCommunity_library_id() {
                return community_library_id;
            }

            public void setCommunity_library_id(String community_library_id) {
                this.community_library_id = community_library_id;
            }

            public String getCommunity_library_name() {
                return community_library_name;
            }

            public void setCommunity_library_name(String community_library_name) {
                this.community_library_name = community_library_name;
            }

            public String getCommunity_library_address() {
                return community_library_address;
            }

            public void setCommunity_library_address(String community_library_address) {
                this.community_library_address = community_library_address;
            }

            public String getCommunity_library_image() {
                return community_library_image;
            }

            public void setCommunity_library_image(String community_library_image) {
                this.community_library_image = community_library_image;
            }

            public String getIs_own_library_status() {
                return is_own_library_status;
            }

            public void setIs_own_library_status(String is_own_library_status) {
                this.is_own_library_status = is_own_library_status;
            }

            public boolean getIsSelected() {
                return isSelected;
            }

            public void setIsSelected(boolean isSelected) {
                this.isSelected = isSelected;
            }
        }

    }
}
