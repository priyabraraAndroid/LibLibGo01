package com.lib.liblibgo.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class MyOwnBooksModel {
    @SerializedName("response")
    @Expose
    public ResModelData response;

    public ResModelData getResponse() {
        return response;
    }

    public class ResModelData {
        @SerializedName("code")
        @Expose
        public Integer code;

        @SerializedName("message")
        @Expose
        public String message;

        @SerializedName("book_list")
        @Expose
        public List<MyBookList> book_list = new ArrayList<>();

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

        public List<MyBookList> getBook_list() {
            return book_list;
        }

        public void setBook_list(List<MyBookList> book_list) {
            this.book_list = book_list;
        }

        public class MyBookList{
            @SerializedName("book_id")
            @Expose
            public String book_id;

            @SerializedName("book_status")
            @Expose
            public String book_status;

            @SerializedName("category_id")
            @Expose
            public String category_id;

            @SerializedName("category_name")
            @Expose
            public String category_name;

            @SerializedName("book_number")
            @Expose
            public String book_number;

            @SerializedName("book_name")
            @Expose
            public String book_name;

            @SerializedName("author_name")
            @Expose
            public String author_name;

            @SerializedName("publish_date")
            @Expose
            public String publish_date;

            @SerializedName("image_url")
            @Expose
            public String image_url;

            @SerializedName("apartment_id")
            @Expose
            public String apartment_id;

            @SerializedName("apartment_name")
            @Expose
            public String apartment_name;

            @SerializedName("shelf_id")
            @Expose
            public String shelf_id;

            @SerializedName("shelf_no")
            @Expose
            public String shelf_no;

            @SerializedName("description")
            @Expose
            public String description;

            @SerializedName("approval_status")
            @Expose
            public String approval_status;

            @SerializedName("buying_status")
            @Expose
            public String buying_status;

            @SerializedName("quantity")
            @Expose
            public String quantity;

            @SerializedName("mrp")
            @Expose
            public String mrp;

            @SerializedName("sale_price")
            @Expose
            public String sale_price;

            @SerializedName("rental_price")
            @Expose
            public String rental_price;

            @SerializedName("security_money")
            @Expose
            public String security_money;

            @SerializedName("rent_duration")
            @Expose
            public String rent_duration;

            @SerializedName("giveaway")
            @Expose
            public String giveaway;

            @SerializedName("book_condition_type")
            @Expose
            public String book_condition_type;

            @SerializedName("selling_type")
            @Expose
            public String selling_type;

            @SerializedName("isbn_no")
            @Expose
            public String isbn_no;

            @SerializedName("community_id")
            @Expose
            public String community_id;

            @SerializedName("recent_book_issue_id")
            @Expose
            public String recent_book_issue_id;

            @SerializedName("issued_user_id")
            @Expose
            public String recent_issued_user_id;

            @SerializedName("issued_user_name")
            @Expose
            public String recent_issued_user_name;

            @SerializedName("expected_return_date")
            @Expose
            public String recent_expected_return_date;

            @SerializedName("recent_return_date")
            @Expose
            public String recent_return_date;

            @SerializedName("book_issue_info")
            @Expose
            public List<BookIssueInfo> book_issue_info = new ArrayList<>();

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

            public String getBuying_status() {
                return buying_status;
            }

            public void setBuying_status(String buying_status) {
                this.buying_status = buying_status;
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

            public String getSecurity_money() {
                return security_money;
            }

            public void setSecurity_money(String security_money) {
                this.security_money = security_money;
            }

            public String getRent_duration() {
                return rent_duration;
            }

            public void setRent_duration(String rent_duration) {
                this.rent_duration = rent_duration;
            }

            public String getGiveaway() {
                return giveaway;
            }

            public void setGiveaway(String giveaway) {
                this.giveaway = giveaway;
            }

            public String getBook_condition_type() {
                return book_condition_type;
            }

            public void setBook_condition_type(String book_condition_type) {
                this.book_condition_type = book_condition_type;
            }

            public String getSelling_type() {
                return selling_type;
            }

            public void setSelling_type(String selling_type) {
                this.selling_type = selling_type;
            }

            public String getIsbn_no() {
                return isbn_no;
            }

            public void setIsbn_no(String isbn_no) {
                this.isbn_no = isbn_no;
            }

            public String getCommunity_id() {
                return community_id;
            }

            public void setCommunity_id(String community_id) {
                this.community_id = community_id;
            }

            public String getRecent_book_issue_id() {
                return recent_book_issue_id;
            }

            public void setRecent_book_issue_id(String recent_book_issue_id) {
                this.recent_book_issue_id = recent_book_issue_id;
            }

            public String getRecent_issued_user_id() {
                return recent_issued_user_id;
            }

            public void setRecent_issued_user_id(String recent_issued_user_id) {
                this.recent_issued_user_id = recent_issued_user_id;
            }

            public String getRecent_issued_user_name() {
                return recent_issued_user_name;
            }

            public void setRecent_issued_user_name(String recent_issued_user_name) {
                this.recent_issued_user_name = recent_issued_user_name;
            }

            public String getRecent_expected_return_date() {
                return recent_expected_return_date;
            }

            public void setRecent_expected_return_date(String recent_expected_return_date) {
                this.recent_expected_return_date = recent_expected_return_date;
            }

            public String getRecent_return_date() {
                return recent_return_date;
            }

            public void setRecent_return_date(String recent_return_date) {
                this.recent_return_date = recent_return_date;
            }

            public List<BookIssueInfo> getBook_issue_info() {
                return book_issue_info;
            }

            public void setBook_issue_info(List<BookIssueInfo> book_issue_info) {
                this.book_issue_info = book_issue_info;
            }

            public class BookIssueInfo{
                @SerializedName("book_issue_id")
                @Expose
                public String book_issue_id;

                @SerializedName("user_id")
                @Expose
                public String user_id;

                @SerializedName("user_name")
                @Expose
                public String user_name;

                @SerializedName("expected_return_date")
                @Expose
                public String expected_return_date;

                @SerializedName("return_date")
                @Expose
                public String return_date;

                public String getBook_issue_id() {
                    return book_issue_id;
                }

                public void setBook_issue_id(String book_issue_id) {
                    this.book_issue_id = book_issue_id;
                }

                public String getUser_id() {
                    return user_id;
                }

                public void setUser_id(String user_id) {
                    this.user_id = user_id;
                }

                public String getUser_name() {
                    return user_name;
                }

                public void setUser_name(String user_name) {
                    this.user_name = user_name;
                }

                public String getExpected_return_date() {
                    return expected_return_date;
                }

                public void setExpected_return_date(String expected_return_date) {
                    this.expected_return_date = expected_return_date;
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
}
