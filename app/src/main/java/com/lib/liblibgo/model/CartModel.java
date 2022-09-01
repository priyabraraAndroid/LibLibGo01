package com.lib.liblibgo.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class CartModel {
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

        @SerializedName("cart_list")
        @Expose
        private List<CartListData> cart_list = new ArrayList<>();

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

        public List<CartListData> getCart_list() {
            return cart_list;
        }

        public void setCart_list(List<CartListData> cart_list) {
            this.cart_list = cart_list;
        }

        public class CartListData{
            @SerializedName("library_id")
            @Expose
            public String library_id;

            @SerializedName("library_name")
            @Expose
            public String library_name;

            @SerializedName("library_image")
            @Expose
            public String library_image;

            @SerializedName("cart_list")
            @Expose
            private List<CartBookList> cart_list = new ArrayList<>();

            public String getLibrary_id() {
                return library_id;
            }

            public void setLibrary_id(String library_id) {
                this.library_id = library_id;
            }

            public String getLibrary_name() {
                return library_name;
            }

            public void setLibrary_name(String library_name) {
                this.library_name = library_name;
            }

            public String getLibrary_image() {
                return library_image;
            }

            public void setLibrary_image(String library_image) {
                this.library_image = library_image;
            }

            public List<CartBookList> getCart_list() {
                return cart_list;
            }

            public void setCart_list(List<CartBookList> cart_list) {
                this.cart_list = cart_list;
            }

            public class CartBookList{
                @SerializedName("cart_id")
                @Expose
                public String cart_id;

                @SerializedName("cart_for")
                @Expose
                public String cart_for;

                @SerializedName("book_id")
                @Expose
                public String book_id;

                @SerializedName("default_price")
                @Expose
                public String default_price;

                @SerializedName("book_name")
                @Expose
                public String book_name;

                @SerializedName("author_name")
                @Expose
                public String author_name;

                @SerializedName("book_image")
                @Expose
                public String book_image;

                @SerializedName("book_price")
                @Expose
                public String book_price;

                @SerializedName("book_rent_duration")
                @Expose
                public String book_rent_duration;

                @SerializedName("cart_rent_duration")
                @Expose
                public String cart_rent_duration;

                @SerializedName("book_quantity")
                @Expose
                public String book_quantity;

                public String getCart_id() {
                    return cart_id;
                }

                public void setCart_id(String cart_id) {
                    this.cart_id = cart_id;
                }

                public String getCart_for() {
                    return cart_for;
                }

                public void setCart_for(String cart_for) {
                    this.cart_for = cart_for;
                }

                public String getBook_id() {
                    return book_id;
                }

                public void setBook_id(String book_id) {
                    this.book_id = book_id;
                }

                public String getDefault_price() {
                    return default_price;
                }

                public void setDefault_price(String default_price) {
                    this.default_price = default_price;
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

                public String getBook_image() {
                    return book_image;
                }

                public void setBook_image(String book_image) {
                    this.book_image = book_image;
                }

                public String getBook_price() {
                    return book_price;
                }

                public void setBook_price(String book_price) {
                    this.book_price = book_price;
                }

                public String getBook_rent_duration() {
                    return book_rent_duration;
                }

                public void setBook_rent_duration(String book_rent_duration) {
                    this.book_rent_duration = book_rent_duration;
                }

                public String getCart_rent_duration() {
                    return cart_rent_duration;
                }

                public void setCart_rent_duration(String cart_rent_duration) {
                    this.cart_rent_duration = cart_rent_duration;
                }

                public String getBook_quantity() {
                    return book_quantity;
                }

                public void setBook_quantity(String book_quantity) {
                    this.book_quantity = book_quantity;
                }
            }
        }
    }

}
