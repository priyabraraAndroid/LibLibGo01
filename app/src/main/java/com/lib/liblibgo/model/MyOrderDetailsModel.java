package com.lib.liblibgo.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class MyOrderDetailsModel {
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

        @SerializedName("library_list")
        @Expose
        private List<LibraryList> library_list = new ArrayList<>();

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

        public List<LibraryList> getLibrary_list() {
            return library_list;
        }

        public void setLibrary_list(List<LibraryList> library_list) {
            this.library_list = library_list;
        }

        public class LibraryList{
            @SerializedName("library_id")
            @Expose
            private String library_id;

            @SerializedName("library_name")
            @Expose
            private String library_name;

            @SerializedName("order_status")
            @Expose
            private String order_status;

            @SerializedName("order_list")
            @Expose
            private List<MyOrderItemList> order_details = new ArrayList<>();

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

            public String getOrder_status() {
                return order_status;
            }

            public void setOrder_status(String order_status) {
                this.order_status = order_status;
            }

            public List<MyOrderItemList> getOrder_details() {
                return order_details;
            }

            public void setOrder_details(List<MyOrderItemList> order_details) {
                this.order_details = order_details;
            }

            public class MyOrderItemList{

                @SerializedName("order_id")
                @Expose
                public String order_id;

                @SerializedName("order_details_id")
                @Expose
                public String order_details_id;

                @SerializedName("book_id")
                @Expose
                public String book_id;

                @SerializedName("book_name")
                @Expose
                public String book_name;

                @SerializedName("product_status")
                @Expose
                private String product_status;

                @SerializedName("return_status")
                @Expose
                private String return_status;

                @SerializedName("author_name")
                @Expose
                public String author_name;

                @SerializedName("image_url")
                @Expose
                public String book_image;

                @SerializedName("price")
                @Expose
                public String price;

                @SerializedName("order_for")
                @Expose
                public String order_for;

                @SerializedName("quantity")
                @Expose
                public String quantity;

                public String getOrder_id() {
                    return order_id;
                }

                public void setOrder_id(String order_id) {
                    this.order_id = order_id;
                }

                public String getBook_id() {
                    return book_id;
                }

                public String getOrder_details_id() {
                    return order_details_id;
                }

                public void setOrder_details_id(String order_details_id) {
                    this.order_details_id = order_details_id;
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

                public String getProduct_status() {
                    return product_status;
                }

                public void setProduct_status(String product_status) {
                    this.product_status = product_status;
                }

                public String getReturn_status() {
                    return return_status;
                }

                public void setReturn_status(String return_status) {
                    this.return_status = return_status;
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

                public String getPrice() {
                    return price;
                }

                public void setPrice(String price) {
                    this.price = price;
                }

                public String getOrder_for() {
                    return order_for;
                }

                public void setOrder_for(String order_for) {
                    this.order_for = order_for;
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
}
