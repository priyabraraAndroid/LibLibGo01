package com.lib.liblibgo.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class BooksModel {
    @SerializedName("response")
    @Expose
    public BookResponse response;

    public BookResponse getResponse() {
        return response;
    }

    public void setResponse(BookResponse response) {
        this.response = response;
    }

    public class BookResponse {
        @SerializedName("code")
        @Expose
        public String code;

        @SerializedName("message")
        @Expose
        public String message;

        @SerializedName("book_list")
        @Expose
        public List<BooksList> book_list = new ArrayList<>();

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

        public List<BooksList> getBook_list() {
            return book_list;
        }

        public void setBook_list(List<BooksList> book_list) {
            this.book_list = book_list;
        }

        public class BooksList{
            @SerializedName("book_id")
            @Expose
            public String book_id;

            @SerializedName("book_number")
            @Expose
            public String book_number;

            @SerializedName("book_name")
            @Expose
            public String book_name;

            @SerializedName("library_name")
            @Expose
            public String library_name;

            @SerializedName("author_name")
            @Expose
            public String author_name;

            @SerializedName("isbn_no")
            @Expose
            public String isbn_no;

            @SerializedName("image_url")
            @Expose
            public String image_url;

            @SerializedName("apartment_id")
            @Expose
            public String apartment_id;

            @SerializedName("apartment_name")
            @Expose
            public String apartment_name;

            @SerializedName("mrp")
            @Expose
            public String mrp;

            @SerializedName("rental_price")
            @Expose
            public String rental_price;

            @SerializedName("sale_price")
            @Expose
            public String sale_price;

            @SerializedName("selling_type")
            @Expose
            public String selling_type;

            @SerializedName("book_condition_type")
            @Expose
            public String book_condition_type;

            @SerializedName("quantity")
            @Expose
            public String quantity;

            public String getBook_id() {
                return book_id;
            }

            public void setBook_id(String book_id) {
                this.book_id = book_id;
            }

            public String getBook_number() {
                return book_number;
            }

            public void setBook_number(String book_number) {
                this.book_number = book_number;
            }

            public String getBook_name() {
                return book_name;
            }

            public void setBook_name(String book_name) {
                this.book_name = book_name;
            }

            public String getLibrary_name() {
                return library_name;
            }

            public void setLibrary_name(String library_name) {
                this.library_name = library_name;
            }

            public String getAuthor_name() {
                return author_name;
            }

            public void setAuthor_name(String author_name) {
                this.author_name = author_name;
            }

            public String getIsbn_no() {
                return isbn_no;
            }

            public void setIsbn_no(String isbn_no) {
                this.isbn_no = isbn_no;
            }

            public String getImage_url() {
                return image_url;
            }

            public void setImage_url(String image_url) {
                this.image_url = image_url;
            }

            public String getApartment_id() {
                return apartment_id;
            }

            public void setApartment_id(String apartment_id) {
                this.apartment_id = apartment_id;
            }

            public String getApartment_name() {
                return apartment_name;
            }

            public void setApartment_name(String apartment_name) {
                this.apartment_name = apartment_name;
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

            public String getSelling_type() {
                return selling_type;
            }

            public void setSelling_type(String selling_type) {
                this.selling_type = selling_type;
            }

            public String getBook_condition_type() {
                return book_condition_type;
            }

            public void setBook_condition_type(String book_condition_type) {
                this.book_condition_type = book_condition_type;
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
