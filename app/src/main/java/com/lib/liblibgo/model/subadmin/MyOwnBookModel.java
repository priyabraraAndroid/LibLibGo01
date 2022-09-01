package com.lib.liblibgo.model.subadmin;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class MyOwnBookModel {
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

        @SerializedName("book_list")
        @Expose
        private List<BookList> book_list = new ArrayList<>();


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

        public List<BookList> getBook_list() {
            return book_list;
        }

        public void setBook_list(List<BookList> book_list) {
            this.book_list = book_list;
        }

        public class BookList {
            @SerializedName("book_id")
            @Expose
            private String book_id;

            @SerializedName("category_id")
            @Expose
            private String category_id;

            @SerializedName("category_name")
            @Expose
            private String category_name;

            @SerializedName("book_number")
            @Expose
            private String book_number;

            @SerializedName("book_name")
            @Expose
            private String book_name;

            @SerializedName("quantity")
            @Expose
            private String quantity;

            @SerializedName("mrp")
            @Expose
            private String mrp;

            @SerializedName("sale_price")
            @Expose
            private String sale_price;

            @SerializedName("rental_price")
            @Expose
            private String rental_price;

            @SerializedName("rent_duration")
            @Expose
            private String rent_duration;

            @SerializedName("selling_type")
            @Expose
            private String selling_type;

            @SerializedName("book_condition_type")
            @Expose
            private String book_condition_type;

            @SerializedName("security_money")
            @Expose
            private String security_money;

            @SerializedName("giveaway")
            @Expose
            private String giveaway;

            @SerializedName("author_name")
            @Expose
            private String author_name;

            @SerializedName("publish_date")
            @Expose
            private String publish_date;

            @SerializedName("isbn_no")
            @Expose
            private String isbn_no;

            @SerializedName("image_url")
            @Expose
            private String image_url;

            @SerializedName("apartment_id")
            @Expose
            private String apartment_id;

            @SerializedName("apartment_name")
            @Expose
            private String apartment_name;

            @SerializedName("shelf_id")
            @Expose
            private String shelf_id;

            @SerializedName("shelf_no")
            @Expose
            private String shelf_no;

            @SerializedName("description")
            @Expose
            private String description;

            @SerializedName("approval_status")
            @Expose
            private String approval_status;

            public String getBook_id() {
                return book_id;
            }

            public void setBook_id(String book_id) {
                this.book_id = book_id;
            }

            public String getCategory_id() {
                return category_id;
            }

            public void setCategory_id(String category_id) {
                this.category_id = category_id;
            }

            public String getCategory_name() {
                return category_name;
            }

            public void setCategory_name(String category_name) {
                this.category_name = category_name;
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

            public String getQuantity() {
                return quantity;
            }

            public void setQuantity(String quantity) {
                this.quantity = quantity;
            }

            public String getMrp() {
                return mrp;
            }

            public void setMrp(String mrp) {
                this.mrp = mrp;
            }

            public String getSale_price() {
                return sale_price;
            }

            public void setSale_price(String sale_price) {
                this.sale_price = sale_price;
            }

            public String getRental_price() {
                return rental_price;
            }

            public void setRental_price(String rental_price) {
                this.rental_price = rental_price;
            }

            public String getRent_duration() {
                return rent_duration;
            }

            public void setRent_duration(String rent_duration) {
                this.rent_duration = rent_duration;
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

            public String getSecurity_money() {
                return security_money;
            }

            public void setSecurity_money(String security_money) {
                this.security_money = security_money;
            }

            public String getGiveaway() {
                return giveaway;
            }

            public void setGiveaway(String giveaway) {
                this.giveaway = giveaway;
            }

            public String getAuthor_name() {
                return author_name;
            }

            public void setAuthor_name(String author_name) {
                this.author_name = author_name;
            }

            public String getPublish_date() {
                return publish_date;
            }

            public void setPublish_date(String publish_date) {
                this.publish_date = publish_date;
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

            public String getShelf_id() {
                return shelf_id;
            }

            public void setShelf_id(String shelf_id) {
                this.shelf_id = shelf_id;
            }

            public String getShelf_no() {
                return shelf_no;
            }

            public void setShelf_no(String shelf_no) {
                this.shelf_no = shelf_no;
            }

            public String getDescription() {
                return description;
            }

            public void setDescription(String description) {
                this.description = description;
            }

            public String getApproval_status() {
                return approval_status;
            }

            public void setApproval_status(String approval_status) {
                this.approval_status = approval_status;
            }
        }
    }
}
