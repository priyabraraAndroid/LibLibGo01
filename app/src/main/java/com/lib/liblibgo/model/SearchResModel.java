package com.lib.liblibgo.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class SearchResModel {
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
        public Integer code;

        @SerializedName("message")
        @Expose
        public String message;

        @SerializedName("book_list")
        @Expose
        private List<BookListData> book_list = new ArrayList<>();

        public Integer getCode() {
            return code;
        }

        public void setCode(Integer code) {
            this.code = code;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public List<BookListData> getBook_list() {
            return book_list;
        }

        public void setBook_list(List<BookListData> book_list) {
            this.book_list = book_list;
        }

        public class BookListData {
            @SerializedName("book_id")
            @Expose
            private String book_id;

            @SerializedName("book_status")
            @Expose
            public String book_status;

            @SerializedName("cell_name")
            @Expose
            private String cell_name;

            @SerializedName("book_number")
            @Expose
            private String book_number;

            @SerializedName("book_name")
            @Expose
            private String book_name;

            @SerializedName("author_name")
            @Expose
            private String author_name;

            @SerializedName("description")
            @Expose
            private String description;

            @SerializedName("added_by")
            @Expose
            private String added_by;

            @SerializedName("is_stack_upload")
            @Expose
            private String is_stack_upload;

            @SerializedName("user_name")
            @Expose
            private String user_name;

            @SerializedName("mobile_no")
            @Expose
            private String mobile_no;

            @SerializedName("category_id")
            @Expose
            private String category_id;

            @SerializedName("category_name")
            @Expose
            private String category_name;

            @SerializedName("library_id")
            @Expose
            private String library_id;

            @SerializedName("library_owner_id")
            @Expose
            private String library_owner_id;

            @SerializedName("library_name")
            @Expose
            private String library_name;

            @SerializedName("library_type")
            @Expose
            private String library_type;

            @SerializedName("issue_status")
            @Expose
            private String issue_status;

            @SerializedName("flag_status")
            @Expose
            private String flag_status;

            @SerializedName("publish_date")
            @Expose
            private String publish_date;

            @SerializedName("isbn_no")
            @Expose
            private String isbn_no;

            @SerializedName("rental_price")
            @Expose
            private String rental_price;

            @SerializedName("sale_price")
            @Expose
            private String sale_price;

            @SerializedName("is_own_library")
            @Expose
            private String is_own_library;

            @SerializedName("selling_type")
            @Expose
            private String selling_type;

            @SerializedName("quantity")
            @Expose
            private String quantity;

            @SerializedName("book_condition_type")
            @Expose
            private String book_condition_type;

            @SerializedName("shelve_id")
            @Expose
            private String shelve_id;

            @SerializedName("shelve_name")
            @Expose
            private String shelve_name;

            @SerializedName("image_url")
            @Expose
            private String image_url;

            @SerializedName("apartment_id")
            @Expose
            private String apartment_id;

            @SerializedName("apartment_name")
            @Expose
            private String apartment_name;

            @SerializedName("giveaway")
            @Expose
            private String giveaway;

            @SerializedName("security_money")
            @Expose
            private String security_money;

            @SerializedName("mrp")
            @Expose
            private String mrp;

            @SerializedName("rent_duration")
            @Expose
            private String rent_duration;

            @SerializedName("issued_user")
            @Expose
            private String issued_user;

            @SerializedName("return_date")
            @Expose
            private String return_date;

            public String getBook_id() {
                return book_id;
            }

            public void setBook_id(String book_id) {
                this.book_id = book_id;
            }

            public String getBook_status() {
                return book_status;
            }

            public void setBook_status(String book_status) {
                this.book_status = book_status;
            }

            public String getCell_name() {
                return cell_name;
            }

            public void setCell_name(String cell_name) {
                this.cell_name = cell_name;
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

            public String getAuthor_name() {
                return author_name;
            }

            public void setAuthor_name(String author_name) {
                this.author_name = author_name;
            }

            public String getDescription() {
                return description;
            }

            public void setDescription(String description) {
                this.description = description;
            }

            public String getAdded_by() {
                return added_by;
            }

            public void setAdded_by(String added_by) {
                this.added_by = added_by;
            }

            public String getIs_stack_upload() {
                return is_stack_upload;
            }

            public void setIs_stack_upload(String is_stack_upload) {
                this.is_stack_upload = is_stack_upload;
            }

            public String getUser_name() {
                return user_name;
            }

            public void setUser_name(String user_name) {
                this.user_name = user_name;
            }

            public String getMobile_no() {
                return mobile_no;
            }

            public void setMobile_no(String mobile_no) {
                this.mobile_no = mobile_no;
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

            public String getLibrary_id() {
                return library_id;
            }

            public void setLibrary_id(String library_id) {
                this.library_id = library_id;
            }

            public String getLibrary_owner_id() {
                return library_owner_id;
            }

            public void setLibrary_owner_id(String library_owner_id) {
                this.library_owner_id = library_owner_id;
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

            public String getIssue_status() {
                return issue_status;
            }

            public void setIssue_status(String issue_status) {
                this.issue_status = issue_status;
            }

            public String getFlag_status() {
                return flag_status;
            }

            public void setFlag_status(String flag_status) {
                this.flag_status = flag_status;
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

            public String getIs_own_library() {
                return is_own_library;
            }

            public void setIs_own_library(String is_own_library) {
                this.is_own_library = is_own_library;
            }

            public String getSelling_type() {
                return selling_type;
            }

            public void setSelling_type(String selling_type) {
                this.selling_type = selling_type;
            }

            public String getQuantity() {
                return quantity;
            }

            public void setQuantity(String quantity) {
                this.quantity = quantity;
            }

            public String getBook_condition_type() {
                return book_condition_type;
            }

            public void setBook_condition_type(String book_condition_type) {
                this.book_condition_type = book_condition_type;
            }

            public String getShelve_id() {
                return shelve_id;
            }

            public void setShelve_id(String shelve_id) {
                this.shelve_id = shelve_id;
            }

            public String getShelve_name() {
                return shelve_name;
            }

            public void setShelve_name(String shelve_name) {
                this.shelve_name = shelve_name;
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

            public String getGiveaway() {
                return giveaway;
            }

            public void setGiveaway(String giveaway) {
                this.giveaway = giveaway;
            }

            public String getSecurity_money() {
                return security_money;
            }

            public void setSecurity_money(String security_money) {
                this.security_money = security_money;
            }

            public String getMrp() {
                return mrp;
            }

            public void setMrp(String mrp) {
                this.mrp = mrp;
            }

            public String getRent_duration() {
                return rent_duration;
            }

            public void setRent_duration(String rent_duration) {
                this.rent_duration = rent_duration;
            }

            public String getIssued_user() {
                return issued_user;
            }

            public void setIssued_user(String issued_user) {
                this.issued_user = issued_user;
            }

            public String getReturn_date() {
                return return_date;
            }

            public void setReturn_date(String return_date) {
                this.return_date = return_date;
            }
        }
    }
}
