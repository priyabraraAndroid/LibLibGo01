package com.lib.liblibgo.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class NotifyListModel {
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

        @SerializedName("nitify_list")
        @Expose
        private List<WishListData> wishlist_data = new ArrayList<>();

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

        public List<WishListData> getWishlist_data() {
            return wishlist_data;
        }

        public void setWishlist_data(List<WishListData> wishlist_data) {
            this.wishlist_data = wishlist_data;
        }

        public class WishListData{
            @SerializedName("notify_id")
            @Expose
            public String notify_id;

            @SerializedName("book_id")
            @Expose
            public String book_id;

            @SerializedName("book_name")
            @Expose
            public String book_name;

            @SerializedName("author_name")
            @Expose
            private String authorName;

            @SerializedName("image_url")
            @Expose
            public String image_url;

            @SerializedName("quantity")
            @Expose
            public String quantity;

            public String getNotify_id() {
                return notify_id;
            }

            public void setNotify_id(String notify_id) {
                this.notify_id = notify_id;
            }

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

            public String getAuthorName() {
                return authorName;
            }

            public void setAuthorName(String authorName) {
                this.authorName = authorName;
            }

            public String getImage_url() {
                return image_url;
            }

            public void setImage_url(String image_url) {
                this.image_url = image_url;
            }

            public String getQuantity() {
                return quantity;
            }

            public void setQuantity(String quantity) {
                this.quantity = quantity;
            }
        }
    }
}
