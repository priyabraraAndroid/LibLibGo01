package com.lib.liblibgo.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class WishListModel {
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

        @SerializedName("wishlist_data")
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
            @SerializedName("wishlist_id")
            @Expose
            public String wishlist_id;

            @SerializedName("book_id")
            @Expose
            public String book_id;

            @SerializedName("book_name")
            @Expose
            public String book_name;

            @SerializedName("author_name")
            @Expose
            private String authorName;

            @SerializedName("selling_type")
            @Expose
            private String selling_type;

            @SerializedName("mrp")
            @Expose
            private String mrp;

            @SerializedName("rental_price")
            @Expose
            private String rental_price;

            @SerializedName("sale_price")
            @Expose
            private String sale_price;

            @SerializedName("book_image")
            @Expose
            public String book_image;

            public String getWishlist_id() {
                return wishlist_id;
            }

            public void setWishlist_id(String wishlist_id) {
                this.wishlist_id = wishlist_id;
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

            public String getSelling_type() {
                return selling_type;
            }

            public void setSelling_type(String selling_type) {
                this.selling_type = selling_type;
            }

            public String getMrp() {
                return mrp;
            }

            public void setMrp(String mrp) {
                this.mrp = mrp;
            }

            public String getRental_price() {
                return rental_price;
            }

            public void setRental_price(String rental_price) {
                this.rental_price = rental_price;
            }

            public String getSale_price() {
                return sale_price;
            }

            public void setSale_price(String sale_price) {
                this.sale_price = sale_price;
            }

            public String getBook_image() {
                return book_image;
            }

            public void setBook_image(String book_image) {
                this.book_image = book_image;
            }
        }
    }
}
