package com.lib.liblibgo.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class BookDetailsModel {
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
        private ResDataRes book_list;


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

        public ResDataRes getBook_list() {
            return book_list;
        }

        public void setBook_list(ResDataRes book_list) {
            this.book_list = book_list;
        }

        public class ResDataRes{
            @SerializedName("book_id")
            @Expose
            private String book_id;

            @SerializedName("book_number")
            @Expose
            private String book_number;

            @SerializedName("book_name")
            @Expose
            private String book_name;

            @SerializedName("library_name")
            @Expose
            private String libraries_name;

            @SerializedName("owner_id")
            @Expose
            private String owner_id;

            @SerializedName("quantity")
            @Expose
            private String quantity;

            @SerializedName("description")
            @Expose
            private String description;

            @SerializedName("author_name")
            @Expose
            private String author_name;

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

            @SerializedName("issue_status")
            @Expose
            private String issue_status;

            @SerializedName("publish_date")
            @Expose
            private String publish_date;

            @SerializedName("isbn_no")
            @Expose
            private String isbn_no;

            @SerializedName("apartment_id")
            @Expose
            private String apartment_id;

            @SerializedName("apartment_name")
            @Expose
            private String apartment_name;

            @SerializedName("cart_status")
            @Expose
            private String cart_status;

            @SerializedName("wishlist_status")
            @Expose
            private String wishlist_status;

            @SerializedName("average_ratings")
            @Expose
            private String average_ratings;

            @SerializedName("giveaway")
            @Expose
            private String giveaway;

            @SerializedName("reviews")
            @Expose
            private List<ReviewListData> reviews = new ArrayList<>();

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

            public String getLibraries_name() {
                return libraries_name;
            }

            public void setLibraries_name(String libraries_name) {
                this.libraries_name = libraries_name;
            }

            public String getOwner_id() {
                return owner_id;
            }

            public void setOwner_id(String owner_id) {
                this.owner_id = owner_id;
            }

            public String getQuantity() {
                return quantity;
            }

            public void setQuantity(String quantity) {
                this.quantity = quantity;
            }

            public String getDescription() {
                return description;
            }

            public void setDescription(String description) {
                this.description = description;
            }

            public String getAuthor_name() {
                return author_name;
            }

            public void setAuthor_name(String author_name) {
                this.author_name = author_name;
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

            public String getIssue_status() {
                return issue_status;
            }

            public void setIssue_status(String issue_status) {
                this.issue_status = issue_status;
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

            public String getCart_status() {
                return cart_status;
            }

            public void setCart_status(String cart_status) {
                this.cart_status = cart_status;
            }

            public String getWishlist_status() {
                return wishlist_status;
            }

            public void setWishlist_status(String wishlist_status) {
                this.wishlist_status = wishlist_status;
            }

            public String getAverage_ratings() {
                return average_ratings;
            }

            public void setAverage_ratings(String average_ratings) {
                this.average_ratings = average_ratings;
            }

            public String getGiveaway() {
                return giveaway;
            }

            public void setGiveaway(String giveaway) {
                this.giveaway = giveaway;
            }

            public List<ReviewListData> getReviews() {
                return reviews;
            }

            public void setReviews(List<ReviewListData> reviews) {
                this.reviews = reviews;
            }

            public class ReviewListData{
                @SerializedName("review_id")
                @Expose
                private String review_id;

                @SerializedName("user_name")
                @Expose
                private String user_name;

                @SerializedName("ratings")
                @Expose
                private String ratings;

                @SerializedName("review")
                @Expose
                private String review;

                @SerializedName("date_time")
                @Expose
                private String date_time;

                public String getReview_id() {
                    return review_id;
                }

                public void setReview_id(String review_id) {
                    this.review_id = review_id;
                }

                public String getUser_name() {
                    return user_name;
                }

                public void setUser_name(String user_name) {
                    this.user_name = user_name;
                }

                public String getRatings() {
                    return ratings;
                }

                public void setRatings(String ratings) {
                    this.ratings = ratings;
                }

                public String getReview() {
                    return review;
                }

                public void setReview(String review) {
                    this.review = review;
                }

                public String getDate_time() {
                    return date_time;
                }

                public void setDate_time(String date_time) {
                    this.date_time = date_time;
                }
            }
        }
    }

}
