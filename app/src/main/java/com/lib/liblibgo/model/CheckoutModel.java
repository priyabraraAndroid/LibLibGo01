package com.lib.liblibgo.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class CheckoutModel {
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

        @SerializedName("TotalCartAmount")
        @Expose
        public String TotalCartAmount;

        @SerializedName("libcoins")
        @Expose
        public String libcoins;

        @SerializedName("cart_list")
        @Expose
        private List<CheckoutList> cart_list = new ArrayList<>();


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

        public String getTotalCartAmount() {
            return TotalCartAmount;
        }

        public void setTotalCartAmount(String totalCartAmount) {
            TotalCartAmount = totalCartAmount;
        }

        public String getLibcoins() {
            return libcoins;
        }

        public void setLibcoins(String libcoins) {
            this.libcoins = libcoins;
        }

        public List<CheckoutList> getCart_list() {
            return cart_list;
        }

        public void setCart_list(List<CheckoutList> cart_list) {
            this.cart_list = cart_list;
        }

        public class CheckoutList{
            @SerializedName("library_id")
            @Expose
            public String library_id;

            @SerializedName("library_name")
            @Expose
            public String library_name;

            @SerializedName("library_pincode")
            @Expose
            public String library_pincode;

            @SerializedName("library_address")
            @Expose
            public String library_address;

            @SerializedName("IsSelfPickup")
            @Expose
            public String IsSelfPickup;

            @SerializedName("library_price")
            @Expose
            public String library_price;

            @SerializedName("self_pickup_status")
            @Expose
            public String self_pickup_status;

            @SerializedName("customer_pincode")
            @Expose
            public String customer_pincode;

            @SerializedName("cart_list")
            @Expose
            private List<ItemList> cart_list = new ArrayList<>();


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

            public String getLibrary_pincode() {
                return library_pincode;
            }

            public void setLibrary_pincode(String library_pincode) {
                this.library_pincode = library_pincode;
            }

            public String getLibrary_address() {
                return library_address;
            }

            public void setLibrary_address(String library_address) {
                this.library_address = library_address;
            }

            public String getIsSelfPickup() {
                return IsSelfPickup;
            }

            public void setIsSelfPickup(String isSelfPickup) {
                IsSelfPickup = isSelfPickup;
            }

            public String getLibrary_price() {
                return library_price;
            }

            public void setLibrary_price(String library_price) {
                this.library_price = library_price;
            }

            public String getSelf_pickup_status() {
                return self_pickup_status;
            }

            public void setSelf_pickup_status(String self_pickup_status) {
                this.self_pickup_status = self_pickup_status;
            }

            public String getCustomer_pincode() {
                return customer_pincode;
            }

            public void setCustomer_pincode(String customer_pincode) {
                this.customer_pincode = customer_pincode;
            }

            public List<ItemList> getCart_list() {
                return cart_list;
            }

            public void setCart_list(List<ItemList> cart_list) {
                this.cart_list = cart_list;
            }

            public class ItemList{
                @SerializedName("cart_id")
                @Expose
                public String cart_id;

                @SerializedName("rent_duration")
                @Expose
                public String rent_duration;

                @SerializedName("cart_for")
                @Expose
                public String cart_for;

                @SerializedName("book_id")
                @Expose
                public String book_id;

                @SerializedName("book_name")
                @Expose
                public String book_name;

                @SerializedName("author_name")
                @Expose
                public String author_name;

                @SerializedName("security_money")
                @Expose
                public String security_money;

                @SerializedName("book_image")
                @Expose
                public String book_image;

                @SerializedName("book_price")
                @Expose
                public String book_price;

                @SerializedName("book_quantity")
                @Expose
                public String book_quantity;

                public String getCart_id() {
                    return cart_id;
                }

                public void setCart_id(String cart_id) {
                    this.cart_id = cart_id;
                }

                public String getRent_duration() {
                    return rent_duration;
                }

                public void setRent_duration(String rent_duration) {
                    this.rent_duration = rent_duration;
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

                public String getSecurity_money() {
                    return security_money;
                }

                public void setSecurity_money(String security_money) {
                    this.security_money = security_money;
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
