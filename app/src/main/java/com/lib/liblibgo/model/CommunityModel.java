package com.lib.liblibgo.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class CommunityModel {
    @SerializedName("response")
    @Expose
    public CommunityResponse response;

    public CommunityResponse getResponse() {
        return response;
    }

    public void setResponse(CommunityResponse response) {
        this.response = response;
    }

    public class CommunityResponse{
        @SerializedName("code")
        @Expose
        public String code;

        @SerializedName("message")
        @Expose
        public String message;

        @SerializedName("own_library")
        @Expose
        public List<OwnCommunityResponse> own_library = new ArrayList<>();

        @SerializedName("my_join_library")
        @Expose
        public List<JoinedCommunityResponse> my_join_library = new ArrayList<>();

        @SerializedName("all_library_list")
        @Expose
        public List<OtherCommunityResponse> all_library_list = new ArrayList<>();

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

        public List<OwnCommunityResponse> getOwn_library() {
            return own_library;
        }

        public void setOwn_library(List<OwnCommunityResponse> own_library) {
            this.own_library = own_library;
        }

        public List<JoinedCommunityResponse> getMy_join_library() {
            return my_join_library;
        }

        public void setMy_join_library(List<JoinedCommunityResponse> my_join_library) {
            this.my_join_library = my_join_library;
        }

        public List<OtherCommunityResponse> getAll_library_list() {
            return all_library_list;
        }

        public void setAll_library_list(List<OtherCommunityResponse> all_library_list) {
            this.all_library_list = all_library_list;
        }

        public class OwnCommunityResponse{
            @SerializedName("library_id")
            @Expose
            public String library_id;

            @SerializedName("library_image")
            @Expose
            public String library_image;

            @SerializedName("library_name")
            @Expose
            public String library_name;

            @SerializedName("library_type")
            @Expose
            public String library_type;

            @SerializedName("user_id")
            @Expose
            public String user_id;

            @SerializedName("community_status")
            @Expose
            public String community_status;

            public String getLibrary_id() {
                return library_id;
            }

            public void setLibrary_id(String library_id) {
                this.library_id = library_id;
            }

            public String getLibrary_image() {
                return library_image;
            }

            public void setLibrary_image(String library_image) {
                this.library_image = library_image;
            }

            public String getLibrary_name() {
                return library_name;
            }

            public void setLibrary_name(String library_name) {
                this.library_name = library_name;
            }

            public String getLibrary_type() {
                return library_type;
            }

            public void setLibrary_type(String library_type) {
                this.library_type = library_type;
            }

            public String getUser_id() {
                return user_id;
            }

            public void setUser_id(String user_id) {
                this.user_id = user_id;
            }

            public String getCommunity_status() {
                return community_status;
            }

            public void setCommunity_status(String community_status) {
                this.community_status = community_status;
            }
        }

        public class JoinedCommunityResponse{
            @SerializedName("library_id")
            @Expose
            public String library_id;

            @SerializedName("library_image")
            @Expose
            public String library_image;

            @SerializedName("library_name")
            @Expose
            public String library_name;

            @SerializedName("library_type")
            @Expose
            public String library_type;

            @SerializedName("user_id")
            @Expose
            public String user_id;

            @SerializedName("community_status")
            @Expose
            public String community_status;

            public String getLibrary_id() {
                return library_id;
            }

            public void setLibrary_id(String library_id) {
                this.library_id = library_id;
            }

            public String getLibrary_image() {
                return library_image;
            }

            public void setLibrary_image(String library_image) {
                this.library_image = library_image;
            }

            public String getLibrary_name() {
                return library_name;
            }

            public void setLibrary_name(String library_name) {
                this.library_name = library_name;
            }

            public String getLibrary_type() {
                return library_type;
            }

            public void setLibrary_type(String library_type) {
                this.library_type = library_type;
            }

            public String getUser_id() {
                return user_id;
            }

            public void setUser_id(String user_id) {
                this.user_id = user_id;
            }

            public String getCommunity_status() {
                return community_status;
            }

            public void setCommunity_status(String community_status) {
                this.community_status = community_status;
            }
        }

        public class OtherCommunityResponse{
            @SerializedName("library_id")
            @Expose
            public String library_id;

            @SerializedName("library_image")
            @Expose
            public String library_image;

            @SerializedName("library_name")
            @Expose
            public String library_name;

            @SerializedName("library_type")
            @Expose
            public String library_type;

            @SerializedName("user_id")
            @Expose
            public String user_id;

            @SerializedName("community_status")
            @Expose
            public String community_status;

            public String getLibrary_id() {
                return library_id;
            }

            public void setLibrary_id(String library_id) {
                this.library_id = library_id;
            }

            public String getLibrary_image() {
                return library_image;
            }

            public void setLibrary_image(String library_image) {
                this.library_image = library_image;
            }

            public String getLibrary_name() {
                return library_name;
            }

            public void setLibrary_name(String library_name) {
                this.library_name = library_name;
            }

            public String getLibrary_type() {
                return library_type;
            }

            public void setLibrary_type(String library_type) {
                this.library_type = library_type;
            }

            public String getUser_id() {
                return user_id;
            }

            public void setUser_id(String user_id) {
                this.user_id = user_id;
            }

            public String getCommunity_status() {
                return community_status;
            }

            public void setCommunity_status(String community_status) {
                this.community_status = community_status;
            }
        }
    }
}
