package com.lib.liblibgo.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class BookNewModel {
    @SerializedName("response")
    @Expose
    public ResModelData response;

    public ResModelData getResponse() {
        return response;
    }

    public class ResModelData {
        @SerializedName("code")
        @Expose
        public String code;

        @SerializedName("message")
        @Expose
        public String message;

        @SerializedName("book_list")
        @Expose
        public List<NewBookList> book_list = new ArrayList<>();

        @SerializedName("open_books")
        @Expose
        public List<NewBookOpenList> open_books = new ArrayList<>();

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

        public List<NewBookList> getBook_list() {
            return book_list;
        }

        public void setBook_list(List<NewBookList> book_list) {
            this.book_list = book_list;
        }

        public List<NewBookOpenList> getOpen_books() {
            return open_books;
        }

        public void setOpen_books(List<NewBookOpenList> open_books) {
            this.open_books = open_books;
        }

        public class NewBookList {
            @SerializedName("book_id")
            @Expose
            public String book_id;

            @SerializedName("book_number")
            @Expose
            public String book_number;

            @SerializedName("book_name")
            @Expose
            public String book_name;

            @SerializedName("selling_type")
            @Expose
            public String selling_type;

            @SerializedName("author_name")
            @Expose
            public String author_name;

            @SerializedName("isbn_no")
            @Expose
            public String isbn_no;

            @SerializedName("image_url")
            @Expose
            public String image_url;

            @SerializedName("mrp")
            @Expose
            public String mrp;

            @SerializedName("rental_price")
            @Expose
            public String rental_price;

            @SerializedName("sale_price")
            @Expose
            public String sale_price;

            @SerializedName("distance")
            @Expose
            public String distance;

            @SerializedName("library_type")
            @Expose
            public String library_type;

            @SerializedName("library_name")
            @Expose
            public String library_name;

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

            public String getSelling_type() {
                return selling_type;
            }

            public void setSelling_type(String selling_type) {
                this.selling_type = selling_type;
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

            public String getDistance() {
                return distance;
            }

            public void setDistance(String distance) {
                this.distance = distance;
            }

            public String getLibrary_type() {
                return library_type;
            }

            public void setLibrary_type(String library_type) {
                this.library_type = library_type;
            }

            public String getLibrary_name() {
                return library_name;
            }

            public void setLibrary_name(String library_name) {
                this.library_name = library_name;
            }
        }

        public class NewBookOpenList{
            @SerializedName("book_id")
            @Expose
            public String book_id;

            @SerializedName("book_number")
            @Expose
            public String book_number;

            @SerializedName("book_name")
            @Expose
            public String book_name;

            @SerializedName("selling_type")
            @Expose
            public String selling_type;

            @SerializedName("author_name")
            @Expose
            public String author_name;

            @SerializedName("isbn_no")
            @Expose
            public String isbn_no;

            @SerializedName("image_url")
            @Expose
            public String image_url;

            @SerializedName("mrp")
            @Expose
            public String mrp;

            @SerializedName("rental_price")
            @Expose
            public String rental_price;

            @SerializedName("sale_price")
            @Expose
            public String sale_price;

            @SerializedName("distance")
            @Expose
            public String distance;

            @SerializedName("library_type")
            @Expose
            public String library_type;

            @SerializedName("library_name")
            @Expose
            public String library_name;

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

            public String getSelling_type() {
                return selling_type;
            }

            public void setSelling_type(String selling_type) {
                this.selling_type = selling_type;
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

            public String getDistance() {
                return distance;
            }

            public void setDistance(String distance) {
                this.distance = distance;
            }

            public String getLibrary_type() {
                return library_type;
            }

            public void setLibrary_type(String library_type) {
                this.library_type = library_type;
            }

            public String getLibrary_name() {
                return library_name;
            }

            public void setLibrary_name(String library_name) {
                this.library_name = library_name;
            }
        }

    }
}
