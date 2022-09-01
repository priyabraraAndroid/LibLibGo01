package com.lib.liblibgo.api;

import com.google.gson.JsonObject;
import com.lib.liblibgo.model.AddToWishListModel;
import com.lib.liblibgo.model.ApartmentModelNew;
import com.lib.liblibgo.model.ApartmentNewModel;
import com.lib.liblibgo.model.BannerModel;
import com.lib.liblibgo.model.BookDetailsModel;
import com.lib.liblibgo.model.BookEntryModel;
import com.lib.liblibgo.model.BookNewModel;
import com.lib.liblibgo.model.BooksModel;
import com.lib.liblibgo.model.CartModel;
import com.lib.liblibgo.model.CategoryModel;
import com.lib.liblibgo.model.CheckCommunityModel;
import com.lib.liblibgo.model.CheckoutModel;
import com.lib.liblibgo.model.CollectedBookModel;
import com.lib.liblibgo.model.CommunityListModel;
import com.lib.liblibgo.model.CommunityModel;
import com.lib.liblibgo.model.CommunityRequestModel;
import com.lib.liblibgo.model.CommunityStatusModel;
import com.lib.liblibgo.model.FlatModel;
import com.lib.liblibgo.model.GiftCardModel;
import com.lib.liblibgo.model.LibraryOrderModel;
import com.lib.liblibgo.model.LoginWithPhoneModel;
import com.lib.liblibgo.model.MyOederModel;
import com.lib.liblibgo.model.MyOrderDetailsModel;
import com.lib.liblibgo.model.MyOwnBooksModel;
import com.lib.liblibgo.model.MyTransactionModel;
import com.lib.liblibgo.model.NearMeBookModel;
import com.lib.liblibgo.model.NearMeLibraryModel;
import com.lib.liblibgo.model.NearMeOwnLibraryModel;
import com.lib.liblibgo.model.NotificationModel;
import com.lib.liblibgo.model.NotifyListModel;
import com.lib.liblibgo.model.ResModel;
import com.lib.liblibgo.model.ReturnHisModel;
import com.lib.liblibgo.model.SaveDataModel;
import com.lib.liblibgo.model.SearchResModel;
import com.lib.liblibgo.model.SendRequestStatusModel;
import com.lib.liblibgo.model.SettingModel;
import com.lib.liblibgo.model.ShelfModel;
import com.lib.liblibgo.model.SocialLoginModel;
import com.lib.liblibgo.model.SubmitDataResModel;
import com.lib.liblibgo.model.UserStatusModel;
import com.lib.liblibgo.model.VerifyOTPModel;
import com.lib.liblibgo.model.WishListModel;
import com.lib.liblibgo.model.subadmin.BookHisModel;
import com.lib.liblibgo.model.subadmin.CreateApartmentAdminModel;
import com.lib.liblibgo.model.subadmin.CustBookHisModel;
import com.lib.liblibgo.model.subadmin.CustListModel;
import com.lib.liblibgo.model.subadmin.CustomerModel;
import com.lib.liblibgo.model.subadmin.LibraryStatusModel;
import com.lib.liblibgo.model.subadmin.MyOwnBookModel;
import com.lib.liblibgo.model.subadmin.OwnLibraryProfModel;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface Holders {
    //http://library.shikshaexampiad.in/User/UserLogin
    @POST("UserLogin")
    @FormUrlEncoded
    Call<ResModel> serverLogin(@Field("email") String mobile,
                               @Field("password") String password,
                               @Field("device_id") String device_id
                               );

    @POST("ApartmentList")
    Call<ResModel> apartmentLis();

    @POST("ApartmentList")
    Call<ApartmentModelNew> apartmentListNew();

    @POST("CategoryList")
    Call<CategoryModel> categoryList();

    /*@POST("ShelfList")
    @FormUrlEncoded
    Call<ShelfModel> shelfList(@Field("apartment_id") String apartment_id);*/

    @POST("ShelfList")
    Call<ShelfModel> shelfList();

//http://library.shikshaexampiad.in/User/UserRegistration
    @POST("UserRegistration")
    @FormUrlEncoded
    Call<ResModel> register(
            @Field("name") String name,
            @Field("email") String email,
            @Field("mobile") String mobile,
            @Field("apartment_id") String apartment_id,
            @Field("flat_no") String flat_no,
            @Field("password") String password
    );

    @POST("SendVerificationCode")
    @FormUrlEncoded
    Call<SubmitDataResModel> sendVerificationCode(@Field("email") String email);

    @POST("ChangePassword")
    @FormUrlEncoded
    Call<SubmitDataResModel> changePassword(@Field("verification_code") String verification_code,
                                     @Field("new_password") String new_password);

    @POST("UpdateProfile")
    @FormUrlEncoded
    Call<ResModel> updateProfile(@Field("user_id") String user_id,
                                 @Field("name") String name,
                                 @Field("email") String email,
                                 @Field("mobile") String mobile,
                                 @Field("apartment_id") String apartment_id,
                                 @Field("flat_no") String flat_no
                                 );

    @POST("UpdateProfile")
    @FormUrlEncoded
    Call<ResModel> updateProfileNew(@Field("user_id") String user_id,
                                 @Field("name") String name,
                                 @Field("email") String email,
                                 @Field("mobile") String mobile,
                                 @Field("address") String address
    );

    @POST("CreateBookIsssue")
    @FormUrlEncoded
    Call<SubmitDataResModel> collectBook(@Field("user_id") String user_id,
                                         @Field("book_id") String book_id);

    @POST("BookListByUserApartment")
    @FormUrlEncoded
    Call<SearchResModel> getAllBooks(@Field("user_id") String user_id);

    @POST("MyColectedBooks")
    @FormUrlEncoded
    Call<CollectedBookModel> myCollectedBooks(@Field("user_id") String user_id);

    @POST("ReturnBook")
    @FormUrlEncoded
    Call<SubmitDataResModel> returnBook(@Field("user_id") String user_id,
                              @Field("issue_id") String issue_id);

    @POST("MyBookReturnHistory")
    @FormUrlEncoded
    Call<ReturnHisModel> myBookReturnHistory(@Field("user_id") String user_id);


    @POST("GetNotificationList")
    @FormUrlEncoded
    Call<NotificationModel> getNotiList(@Field("user_id") String user_id);

    @POST("GetUserStatus")
    @FormUrlEncoded
    Call<UserStatusModel> checkActiveStatus(@Field("user_id") String user_id);

    @POST("SearchBook")
    @FormUrlEncoded
    Call<SearchResModel> searchResult(@Field("isbn_no") String isbn_no);

    @POST("BookDetails")
    @FormUrlEncoded
    Call<BookDetailsModel> getBookDetails(@Field("user_id") String user_id,@Field("book_id") String book_id);

    @POST("PostReview")
    @FormUrlEncoded
    Call<SubmitDataResModel> sendBookReview(@Field("user_id") String user_id,
                                          @Field("book_id") String book_id,
                                          @Field("ratings") String ratings,
                                          @Field("review") String review);

    @POST("logout")
    @FormUrlEncoded
    Call<SubmitDataResModel> userLogout(@Field("user_id") String user_id);


    //New UserLogin
    @POST("login")
    @FormUrlEncoded
    Call<LoginWithPhoneModel> loginWithPhone(@Field("mobile") String mobile);

    @POST("verifyOtp")
    @FormUrlEncoded
    Call<VerifyOTPModel> verifyOtp(@Field("mobile") String mobile,@Field("otp") String otp,
                                   @Field("device_id") String device_id);


    @POST("GetFlatByApartment")
    @FormUrlEncoded
    Call<FlatModel> getFlatByApartment(@Field("apartment_id") String apartment_id,@Field("search_keyword") String search_keyword);

    @POST("FillUserDetails")
    @FormUrlEncoded
    Call<SaveDataModel> saveUserDetails(@Field("mobile") String mobile,
                                        @Field("name") String name,
                                        @Field("username") String username,
                                        @Field("email") String email,
                                        @Field("apartment_id") String apartment_id,
                                        @Field("flat_no") String flat_no);

    @POST("UserRegistration")
    @FormUrlEncoded
    Call<SubmitDataResModel> newUserRegistration(@Field("name") String name,
                                            @Field("username") String username,
                                            @Field("email") String email,
                                            @Field("apartment_id") String apartment_id,
                                            @Field("flat_no") String flat_no,
                                            @Field("password") String password);


    @POST("FillUserDetailsBySocial")
    @FormUrlEncoded
    Call<SaveDataModel> saveUserDetailsBySocial(@Field("social_id") String mobile,
                                        @Field("name") String name,
                                        @Field("username") String username,
                                        @Field("email") String email,
                                        @Field("apartment_id") String apartment_id,
                                        @Field("flat_no") String flat_no);

    @POST("social_login")
    @FormUrlEncoded
    Call<SocialLoginModel> socialLogin(@Field("social_type") String social_type,
                                       @Field("social_id") String social_id,
                                       @Field("device_id") String device_id);

    @POST("GetUserLibraryStatus")
    @FormUrlEncoded
    Call<SendRequestStatusModel> checkSendRequestStatus(@Field("user_id") String user_id);

    @POST("CheckLibraryStatus")
    @FormUrlEncoded
    Call<LibraryStatusModel> checkLibraryStatus(@Field("user_id") String user_id);

    @POST("CheckApartmentLibraryStatus")
    @FormUrlEncoded
    Call<LibraryStatusModel> checkApartmentLibraryStatus(@Field("user_id") String user_id);

    @POST("SendLibraryRequest")
    @FormUrlEncoded
    Call<SubmitDataResModel> sendSubAdminRequest(@Field("user_id") String user_id);

    @POST("SendBookRequest")
    @FormUrlEncoded
    Call<SubmitDataResModel> sendBookRequest(@Field("user_id") String user_id,
                                             @Field("book_id") String book_id);

    /*@POST("CreateOwnLibrary")
    @FormUrlEncoded
    Call<SubmitDataResModel> createLibrary(@Field("user_id") String user_id,
                                           @Field("library_name") String library_name,
                                             @Field("address") String address,
                                           @Field("latitude") String latitude,
                                           @Field("longitude") String longitude,
                                           @Field("self_pickup_status") String self_pickup_status
                                            );*/

    @Multipart
    @POST("CreateOwnLibrary")
    Call<SubmitDataResModel> createLibrary(@Part("user_id") RequestBody user_id,
                                             @Part("library_name") RequestBody library_name,
                                             @Part("address") RequestBody address,
                                             @Part("latitude") RequestBody latitude,
                                             @Part("longitude") RequestBody longitude,
                                             @Part("self_pickup_status") RequestBody self_pickup_status,
                                             @Part("is_own_library") RequestBody is_own_library,
                                             @Part("self_pickup_for") RequestBody self_pickup_for,
                                             @Part("contact_no") RequestBody contact_no,
                                             @Part("library_type") RequestBody library_type,
                                             @Part MultipartBody.Part image);

    @Multipart
    @POST("EditLibraryProfile")
    Call<SubmitDataResModel> editLibrary(@Part("user_id") RequestBody user_id,
                                         @Part("is_own_library") RequestBody is_own_library,
                                         @Part MultipartBody.Part library_image,
                                         @Part("address") RequestBody address,
                                         @Part("library_name") RequestBody library_name);

    @Multipart
    @POST("CreateApartmentLibrary")
    Call<SubmitDataResModel> createApartmentLibrary(@Part("user_id") RequestBody user_id,
                                           @Part("library_name") RequestBody library_name,
                                           @Part("address") RequestBody address,
                                           @Part("latitude") RequestBody latitude,
                                           @Part("longitude") RequestBody longitude,
                                           @Part("self_pickup_status") RequestBody self_pickup_status,
                                           @Part("is_own_library") RequestBody is_own_library,
                                           @Part("self_pickup_for") RequestBody self_pickup_for,
                                           @Part("contact_no") RequestBody contact_no,
                                           @Part("library_type") RequestBody library_type,
                                           @Part MultipartBody.Part image);

    @POST("DeleteBook")
    @FormUrlEncoded
    Call<SubmitDataResModel> deleteBooks(@Field("book_id") String book_id);

    @POST("UserUnReviewsBooks")
    @FormUrlEncoded
    Call<CollectedBookModel> getUnReviewedBooks(@Field("user_id") String user_id);

    @POST("OwnLibraryBooks")
    @FormUrlEncoded
    Call<MyOwnBookModel> getAllOwnBooks(@Field("user_id") String user_id,@Field("is_own_library") String is_own_library);

    @POST("GetUserDetails")
    @FormUrlEncoded
    Call<JsonObject> userDetails(@Field("user_id") String user_id);

    @POST("BannerList")
    Call<BannerModel> getBanners();

    //SubAdminUserApis
    @POST("BookHistory")
    @FormUrlEncoded
    Call<BookHisModel> getBookHistory(@Field("user_id") String user_id);

    @POST("CustomerHistory")
    @FormUrlEncoded
    Call<CustListModel> showCustomerHistory(@Field("user_id") String user_id);

    @POST("CustomerBookHistory")
    @FormUrlEncoded
    Call<CustBookHisModel> showCustomerBookHistory(@Field("user_id") String user_id);

    @POST("CustomerList")
    @FormUrlEncoded
    Call<CustomerModel> getCustomer(@Field("user_id") String user_id);

    @POST("SendNotification")
    @FormUrlEncoded
    Call<CreateApartmentAdminModel> sendNotificationUser(@Field("user_id") String user_id,
                                                         @Field("title") String title,
                                                         @Field("message") String message);

    @POST("UserActiveDeactive")
    @FormUrlEncoded
    Call<CreateApartmentAdminModel> changeStatusCust(@Field("user_id") String user_id,
                                                     @Field("status") String status);

    @GET("v1/volumes?")
    Call<JsonObject> getBooks(@Query("q") String isbn_no);

    @GET("v1/volumes?")
    Call<JsonObject> getBooksByName(@Query("q") String isbn_no);

    @POST("book_entry")
    @FormUrlEncoded
    Call<BookEntryModel> uploadBooks(@Field("book_name") String book_name,
                                     @Field("user_id") String user_id,
                                     @Field("shelf_id") String shelf_id,
                                     @Field("author_name") String author_name,
                                     @Field("publish_date") String publish_date,
                                     @Field("description") String description,
                                     @Field("image_url") String image_url,
                                     @Field("isbn_no") String isbn_no,
                                     @Field("rent_duration") String rent_duration,
                                     @Field("book_id") String book_id,
                                     @Field("category_id") String category_id,
                                     @Field("mrp") String mrp,
                                     @Field("rental_price") String rental_price,
                                     @Field("sale_price") String sale_price,
                                     @Field("security_money") String security_money,
                                     @Field("quantity") String quantity,
                                     @Field("selling_type") String selling_type,
                                     @Field("book_condition_type") String book_condition_type,
                                     @Field("giveaway") String giveaway,
                                     @Field("is_own_library") String is_own_library,
                                     @Field("is_open_library") String is_open_library,
                                     @Field("community_id") String community_id);


    @POST("GetOwnLibraryProfile")
    @FormUrlEncoded
    Call<OwnLibraryProfModel> getLibraryDetails(@Field("user_id") String user_id,@Field("is_own_library") String is_own_library);

    @POST("BookListNearByMe")
    @FormUrlEncoded
    Call<NearMeBookModel> getNearMeBooks(@Field("latitude") String latitude,
                                         @Field("longitude") String longitude,
                                         @Field("distance") String distance
                                         );

    @POST("LibraryListNearByMe")
    @FormUrlEncoded
    Call<NearMeOwnLibraryModel> getNearMeLibrary(@Field("user_id") String user_id,
                                              @Field("latitude") String latitude,
                                              @Field("longitude") String longitude,
                                              @Field("distance") String distance
    );

    @POST("LibraryListByFliters")
    @FormUrlEncoded
    Call<NearMeOwnLibraryModel> getNearMeLibraryByFilter(@Field("city") String city,
                                              @Field("pincode") String pincode,
                                              @Field("user_id") String user_id
    );

    @POST("BookListByFilters")
    @FormUrlEncoded
    Call<NearMeBookModel> getNearMeBooksByFilter(@Field("city") String city,
                                                      @Field("pincode") String pincode,
                                                      @Field("apartment_name") String apartment_name,
                                                 @Field("latitude") String latitude,
                                                 @Field("longitude") String longitude
    );

    @POST("BookListByCategories")
    @FormUrlEncoded
    Call<NearMeBookModel> getBooksByCategories(@Field("categories") String categories);

    @POST("SearchByBookNameFilter")
    @FormUrlEncoded
    Call<NearMeBookModel> getBookSearchResult(@Field("book_name") String book_name,
                                              @Field("is_own_library") String is_own_library,
                                              @Field("user_id") String user_id);

    @POST("BookListByAllFilters")
    @FormUrlEncoded
    Call<BookNewModel> getBooksByAllFilters(@Field("categories") String categories,
                                            @Field("latitude") String latitude,
                                            @Field("longitude") String longitude,
                                            @Field("distance") String distance,
                                            @Field("city") String city,
                                            @Field("pincode") String pincode,
                                            @Field("book_type") String book_type,
                                            @Field("giveaway") String giveaway,
                                            @Field("user_id") String user_id,
                                            @Field("is_own_library") String is_own_library,
                                            @Field("login_user_id") String login_user_id,
                                            @Field("sort_by") String sort_by,
                                            @Field("start") String start,
                                            @Field("limit") String limit
                                               );

    @POST("BookListByAllFilters")
    @FormUrlEncoded
    Call<JsonObject> getBooksByAllFiltersNew(@Field("categories") String categories,
                                            @Field("latitude") String latitude,
                                            @Field("longitude") String longitude,
                                            @Field("distance") String distance,
                                            @Field("city") String city,
                                            @Field("pincode") String pincode,
                                            @Field("book_type") String book_type,
                                            @Field("giveaway") String giveaway,
                                            @Field("user_id") String user_id,
                                            @Field("is_own_library") String is_own_library,
                                            @Field("login_user_id") String login_user_id,
                                            @Field("sort_by") String sort_by,
                                            @Field("start") String start,
                                            @Field("limit") String limit
    );

    @POST("BookListByOwn")
    @FormUrlEncoded
    Call<NearMeBookModel> getMyOwnBooks(@Field("user_id") String user_id,
                                        @Field("latitude") String latitude,
                                        @Field("longitude") String longitude,
                                        @Field("is_own_library") String is_own_library);

    @POST("BookListByType")
    @FormUrlEncoded
    Call<NearMeBookModel> getBooksByType(@Field("book_type") String book_type);

    @POST("BookListByGiveaway")
    @FormUrlEncoded
    Call<NearMeBookModel> getBooksByGiveaway(@Field("giveaway") String giveaway);

    @POST("LibraryListNearByMe")
    @FormUrlEncoded
    Call<NearMeOwnLibraryModel> getAllLibrary(@Field("user_id") String user_id);

    @POST("SearchByLibraryName")
    @FormUrlEncoded
    Call<NearMeOwnLibraryModel> searchLibrary(@Field("library_name") String library_name,@Field("is_own_library") String is_own_library);

    @POST("ApartmentLibraryListNearByMe")
    @FormUrlEncoded
    Call<NearMeLibraryModel> getAllApartmentLibrary(@Field("user_id") String user_id);

    @POST("MyCommunityJoinedList")
    @FormUrlEncoded
    Call<NearMeLibraryModel> getJoinedLibrary(@Field("user_id") String user_id);

    @POST("GetOwnLibraryDetails")
    @FormUrlEncoded
    Call<NearMeOwnLibraryModel> getMyLibrary(@Field("user_id") String user_id);

    @POST("SearchApartment")
    @FormUrlEncoded
    Call<ApartmentNewModel> getApartList(@Field("pincode") String pincode,@Field("apartment_name") String apartment_name);

    @POST("UserRegistration")
    @FormUrlEncoded
    Call<SubmitDataResModel> registerUser(
            @Field("name") String name,
            @Field("email") String email,
            @Field("apartment") String apartment,
            @Field("flat_no") String flat_no,
            @Field("area") String area,
            @Field("city") String city,
            @Field("state") String state,
            @Field("pincode") String pincode,
            @Field("landmark") String landmark,
            @Field("password") String password
    );

    @POST("FillUserDetailsBySocial")
    @FormUrlEncoded
    Call<SaveDataModel> saveUserDetailsBySocialLogin(@Field("social_id") String social_id,
                                                @Field("name") String name,
                                                @Field("email") String email,
                                                @Field("apartment") String apartment,
                                                @Field("flat_no") String flat_no,
                                                @Field("area") String area,
                                                @Field("city") String city,
                                                @Field("state") String state,
                                                @Field("pincode") String pincode,
                                                @Field("landmark") String landmark);

    @POST("UserRegistrationByMobile")
    @FormUrlEncoded
    Call<SaveDataModel> saveUserDetalsByPhone(
            @Field("name") String name,
            @Field("mobile") String mobile,
            @Field("apartment") String apartment,
            @Field("flat_no") String flat_no,
            @Field("area") String area,
            @Field("city") String city,
            @Field("state") String state,
            @Field("pincode") String pincode,
            @Field("landmark") String landmark
    );

    @POST("UpdateUserProfile")
    @FormUrlEncoded
    Call<SaveDataModel> updateUserProfileDetails(
            @Field("user_id") String user_id,
            @Field("name") String name,
            @Field("email") String email,
            @Field("mobile") String mobile,
            @Field("area") String area,
            @Field("city") String city,
            @Field("state") String state,
            @Field("pincode") String pincode,
            @Field("landmark") String landmark,
            @Field("flat_no") String flat_no,
            @Field("apartment_name") String apartment_name
    );

    @POST("BookListByCategoryOrLibrary")
    @FormUrlEncoded
    Call<SearchResModel> getBooksByLibrary(@Field("library_id") String library_id,
                                           @Field("categories") String categories,
                                           @Field("giveaway") String giveaway,
                                           @Field("selling_type") String selling_type,
                                           @Field("user_id") String user_id,
                                           @Field("login_user_id") String login_user_id,
                                           @Field("page") String page
                                           );

    @POST("BookListByCategoryOrLibrary")
    @FormUrlEncoded
    Call<SearchResModel> getBooksByCategory(@Field("category_id") String category_id);

    @POST("AddToWishList")
    @FormUrlEncoded
    Call<AddToWishListModel> addToWishList(@Field("user_id") String user_id, @Field("book_id") String book_id);

    @POST("MyWishlist")
    @FormUrlEncoded
    Call<WishListModel> getUserWishList(@Field("user_id") String user_id);

    @POST("MyNotifyBooks")
    @FormUrlEncoded
    Call<NotifyListModel> getWishList(@Field("user_id") String user_id);

    @POST("AddToCart")
    @FormUrlEncoded
    Call<SubmitDataResModel> addToCartList(@Field("book_id") String book_id,
                                           @Field("user_id") String user_id,
                                           @Field("quantity") String quantity,
                                           @Field("cart_for") String cart_for);

    @POST("MyCartList")
    @FormUrlEncoded
    Call<CartModel> getCartList(@Field("user_id") String user_id);

    @POST("RemoveCart")
    @FormUrlEncoded
    Call<SubmitDataResModel> removeCart(@Field("remove_for") String remove_for,
                                        @Field("id") String id);

    @POST("Checkout")
    @FormUrlEncoded
    Call<CheckoutModel> getCheckoutDetails(@Field("user_id") String user_id);

    @POST("CreateOrder")
    @FormUrlEncoded
    Call<SubmitDataResModel> createUserOrder(
            @Field("user_id") String user_id,
            @Field("library_ids") String library_ids,
            @Field("txn_id") String txn_id,
            @Field("payment_status") String payment_status,
            @Field("customer_name") String customer_name,
            @Field("mobile") String mobile,
            @Field("address") String address,
            @Field("order_amount") String order_amount,
            @Field("libcoins") String libcoins
    );

    @POST("MyOrders")
    @FormUrlEncoded
    Call<MyOederModel> getMyOrderList(@Field("user_id") String user_id,@Field("filter") String filter);

    @POST("OrderDetails")
    @FormUrlEncoded
    Call<MyOrderDetailsModel> getItemList(@Field("order_id") String order_id);

    @POST("GetOrderListByLibrary")
    @FormUrlEncoded
    Call<LibraryOrderModel> getLibraryOrderList(@Field("user_id") String user_id,@Field("is_own_library") String is_own_library);

    @POST("AcceptOrderByLibrary")
    @FormUrlEncoded
    Call<SubmitDataResModel> orderConfirmationByLibrary(@Field("order_id") String order_id);

    @Multipart
    @POST("UploadBookViaExcel")
    Call<SubmitDataResModel> uploadBookByCsvFile(@Part("user_id") RequestBody user_id,
                                                 @Part MultipartBody.Part uploadFile,
                                                 @Part("is_own_library") RequestBody is_own_library,
                                                 @Part("community_id") RequestBody community_id,
                                                 @Part("is_open_library") RequestBody is_open_library
                                                 );

    @Multipart
    @POST("UpdateProfileImage")
    Call<SubmitDataResModel> uploadProfileImage(@Part("user_id") RequestBody user_id,
                                           @Part MultipartBody.Part image);

    @Multipart
    @POST("UploadBookImage")
    Call<SubmitDataResModel> uploadBookImage(@Part("book_id") RequestBody book_id,
                                                @Part MultipartBody.Part image);

    @POST("SendGiftCardRequest")
    @FormUrlEncoded
    Call<SubmitDataResModel> sendAmazonCardRequest(@Field("user_id") String user_id,@Field("quantity") String quantity);

    @POST("GiftCardRequestList")
    @FormUrlEncoded
    Call<GiftCardModel> getGiftCards(@Field("user_id") String user_id);

    @POST("notify_me")
    @FormUrlEncoded
    Call<SubmitDataResModel> notifyBooksToMe(@Field("user_id") String user_id,@Field("book_id") String book_id);

    @POST("notify_by_isbn_no")
    @FormUrlEncoded
    Call<SubmitDataResModel> notifyBooksFromGoogle(@Field("user_id") String user_id,
                                                   @Field("isbn_no") String isbn_no,
                                                   @Field("book_name") String book_name,
                                                   @Field("author_name") String author_name,
                                                   @Field("image_url") String image_url
                                                   );

    @POST("UpdateCartRentDuration")
    @FormUrlEncoded
    Call<SubmitDataResModel> updateCart(@Field("cart_id") String user_id,@Field("rent_duration") String rent_duration);

    @POST("SentCommunityRequest")
    @FormUrlEncoded
    Call<SubmitDataResModel> sendCommunityLibraryRequest(@Field("library_id") String library_id,@Field("user_id") String user_id);

    @POST("MyCommunityRequestList")
    @FormUrlEncoded
    Call<CommunityRequestModel> getCommunityRequest(@Field("user_id") String user_id);

    @POST("AcceptRejectCommunityRequest")
    @FormUrlEncoded
    Call<SubmitDataResModel> requestAcceptReject(@Field("community_id") String community_id,@Field("request_status") String request_status);

    @POST("CheckLibraryCommunityStatus")
    @FormUrlEncoded
    Call<CommunityStatusModel> checkCommunityRequestStatus(@Field("library_id") String library_id, @Field("user_id") String user_id);

    @POST("OwnLibraryBooks")
    @FormUrlEncoded
    Call<MyOwnBooksModel> getMyAllBooks(@Field("user_id") String user_id, @Field("is_own_library") String is_own_library);

    @POST("AcceptedCommunityList")
    @FormUrlEncoded
    Call<CommunityListModel> getMyAllCommunities(@Field("user_id") String user_id);

    @POST("BookMoveToCommunityLibrary")
    @FormUrlEncoded
    Call<SubmitDataResModel> moveToCommunity(@Field("book_id") String book_id,
                                             @Field("library_id") String library_id,
                                             @Field("community_id") String community_id);

    @POST("BookActiveInactive")
    @FormUrlEncoded
    Call<CreateApartmentAdminModel> chnageBookStatus(@Field("book_id") String book_id,
                                                     @Field("status") String status);

    @POST("ReturnBookFromMyOrders")
    @FormUrlEncoded
    Call<SubmitDataResModel> returnBook(@Field("order_details_id") String order_details_id);

    @Multipart
    @POST("Book_stack_upload")
    Call<SubmitDataResModel> addStackBooks(@Part("user_id") RequestBody user_id,
                                           @Part("book_id") RequestBody book_id,
                                           @Part("is_own_library") RequestBody is_own_library,
                                           @Part("community_id") RequestBody community_id,
                                           @Part("book_name") RequestBody book_name,
                                           @Part("description") RequestBody description,
                                           @Part("book_condition_type") RequestBody book_condition_type,
                                           @Part("category") RequestBody category,
                                           @Part("stack_quantity") RequestBody stack_quantity,
                                           @Part("giveaway") RequestBody giveaway,
                                           @Part("is_open_library") RequestBody is_open_library,
                                           @Part("selling_type") RequestBody selling_type,
                                           @Part("sale_price") RequestBody sale_price,
                                           @Part MultipartBody.Part book_image);

    @POST("PostRedeemLibcoinsRequest")
    @FormUrlEncoded
    Call<SubmitDataResModel> requestForRedeem(@Field("user_id") String user_id,
                                              @Field("redeem_libcoins") String redeem_libcoins,
                                              @Field("transaction_id") String transaction_id);

    @POST("OrderDetails")
    @FormUrlEncoded
    Call<JsonObject> orderDetailsApi(@Field("user_id") String user_id, @Field("order_id") String order_id);

    @POST("ChangeOrderStatus")
    @FormUrlEncoded
    Call<SubmitDataResModel> changeOrderStatus(@Field("order_id") String order_id,
                                              @Field("order_status") String order_status);

    @POST("transaction_history")
    @FormUrlEncoded
    Call<MyTransactionModel> getTransactionHistory(@Field("user_id") String user_id);

    @POST("ChangeReadNotificationStatus")
    @FormUrlEncoded
    Call<SubmitDataResModel> readAllNotifications(@Field("user_id") String user_id);

    @POST("ChangeNotificationSettings")
    @FormUrlEncoded
    Call<SubmitDataResModel> setSettings(@Field("user_id") String user_id,@Field("notification_type") String notification_type);

    @POST("NotificationSettingList")
    @FormUrlEncoded
    Call<SettingModel> getSeetingsNotification(@Field("user_id") String user_id);

    //@Headers({"Content-type: application/x-www-form-urlencoded", "Accept: */*"})
    @GET("book/{isbn}?")
    Call<JsonObject> getBookMrp(@Header("Authorization") String token, @Path("isbn") String isbn);

    @POST("ChangeLibraryOwner")
    @FormUrlEncoded
    Call<SubmitDataResModel> makeOwnerByLibrary(@Field("user_id") String user_id,@Field("library_id") String library_id);

    @POST("DeleteMyAccount")
    @FormUrlEncoded
    Call<SubmitDataResModel> deleteAccount(@Field("user_id") String user_id);

    @GET("BookRuleList")
    Call<JsonObject> getRuleOfBook();

    //New Apis New Flow
    @POST("CommunityLibraryList")
    @FormUrlEncoded
    Call<CommunityModel> communityList(@Field("user_id") String user_id,
                                       @Field("pincode") String pincode,
                                       @Field("limit1") String limit1,
                                       @Field("limit2") String limit2);

    @POST("CheckUserCommunityStatus")
    @FormUrlEncoded
    Call<CheckCommunityModel> checkUserCommunityStatus(@Field("user_id") String user_id);

    @POST("NearByBooks")
    @FormUrlEncoded
    Call<BooksModel> nearMeCommunityBooks(@Field("user_id") String user_id,
                                          @Field("pincode") String pincode,
                                          @Field("limit") String limit);

    @POST("GivewayBooks")
    @FormUrlEncoded
    Call<BooksModel> getGiveAwayBooks(@Field("user_id") String user_id,
                                          @Field("pincode") String pincode,
                                          @Field("limit") String limit);

    @POST("SaleBooksList")
    @FormUrlEncoded
    Call<BooksModel> getForSaleBooks(@Field("user_id") String user_id,
                                      @Field("pincode") String pincode,
                                      @Field("limit") String limit);

    @POST("RentBooksList")
    @FormUrlEncoded
    Call<BooksModel> getForRentBooks(@Field("user_id") String user_id,
                                     @Field("pincode") String pincode,
                                     @Field("limit") String limit);

    @POST("PopularBooksList")
    @FormUrlEncoded
    Call<BooksModel> getPouplarBooks(@Field("user_id") String user_id,
                                     @Field("pincode") String pincode,
                                     @Field("limit") String limit);

    @POST("CategoryListNew")
    Call<CategoryModel> getCategoryListNew();

    @POST("MyPurchaseBook")
    @FormUrlEncoded
    Call<MyOwnBooksModel> myPurchaseBooks(@Field("user_id") String user_id);

    @POST("ExitMyCommunity")
    @FormUrlEncoded
    Call<SubmitDataResModel> leaveCommunity(@Field("user_id") String user_id,
                                            @Field("community_id") String community_id);

}



