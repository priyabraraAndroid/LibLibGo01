package com.lib.liblibgo.common;

import android.graphics.Bitmap;

import com.lib.liblibgo.model.CategoryListData;
import com.lib.liblibgo.model.CommunityListModel;
import com.lib.liblibgo.model.FlatListData;
import com.lib.liblibgo.model.MyOwnBooksModel;
import com.lib.liblibgo.model.NearMeBookModel;
import com.lib.liblibgo.model.NearMeFilterModel;

import java.util.ArrayList;
import java.util.List;

public class Constants {
    public static final int REQUEST_CREATE=100;
    public static final int REQUEST_UPDATE=200;
    public static final String APARTMENT_NAME = "apartment_name";
    public static String LOG_ID = "";
    public static String LOG_PASS = "";
    public static boolean SAVED = false;
    public static final int DB_VERSION = 1;
    public static final String DB_NAME = "bookList";
    public static final String TABLE_NAME = "book_tbl";

    //Table column
    public static final String KEY_ID = "id";
    public static final String BOOK_ID = "book_id";
    public static final String BOOK_NUMBER = "book_number";
    public static final String BOOK_NAME = "book_name";
    public static final String BOOK_AUTHOR = "book_author";
    public static final String PUBLISH_DATE = "publish_date";
    public static final String ISBN_NO = "isbn_no";
    public static final String APART_ID = "apart_id";
    public static final String APART_NAME = "apart_name";
    public static final String ISSUE_DATE = "issue_date";
    public static final String RETURN_DATE = "return_date";

    public static String SOCIAL_USER_NAME = "";
    public static String SOCIAL_USER_EMAIL = "";
    public static String SOCIAL_USER_TYPE = "";
    public static String SOCIAL_ID = "";


    public static String USER_APARTMENT_ID = "";
    public static String USER_APARTMENT_NAME = "";

    public static String USER_FLAT_ID = "";
    public static String USER_FLAT_NAME = "";

    public static String USER_DETAIL_PAGE_TYPE = "0";
    public static String AprtmentId = "";

    public static List<FlatListData> flatList = new ArrayList<>();

    public static String latitude = "";
    public static String longitude = "";

    public static boolean isOwnLibrary = false;
    public static String libraryType = "";
    public static String  SelectedProfileImage = "";
    public static String  SelectedLibraryProfileImage = "";
    public static String  SelectedLibraryEditImage = "";
    public static String  SelectedBookImage = "";
    public static String  openPageFrom = "";

    public static String  fullAddress = "";
    public static String  bookListType = "";

    public static List<CategoryListData> catList = new ArrayList<>();
    public static String selectedCategories = "";
    public static List<String> selectedCatList = new ArrayList<>();

    public static List<NearMeFilterModel> myNearbyList = new ArrayList<>();
    public static List<NearMeBookModel.ResModelData.NewrmeBookList> fakeSearchBookList = new ArrayList<>();


    public static String selectedNearByCity = "";
    public static String selectedNearByArea = "";
    public static String selectedNearByApartment = "";
    public static String selectedNearByDistance = "";

    public static String selectedFilterValue = "";
    public static String giveAway = "";
    public static String userId = "";

    public static String myLibCoins = "0";
    public static String myProfileImage = "";

    public static String myBookId = "";
    public static String isOwnBooks = "1";

    public static List<MyOwnBooksModel.ResModelData.MyBookList.BookIssueInfo> mInfoDetailList = new ArrayList<>();

    public static Bitmap cropedImage = null;
    public static String searchFrom = "community";

    //For Community Books Filter
    public static List<CategoryListData> catListCommunity = new ArrayList<>();
    public static String selectedCategoriesCommunity = "";
    public static List<String> selectedCatListCommunity = new ArrayList<>();
    public static String selectedNearByCityCommunity = "";
    public static String selectedNearByAreaCommunity = "";
    public static String selectedNearByApartmentCommunity = "";
    public static String selectedNearByDistanceCommunity = "";

    public static String selectedFilterValueCommunity = "";
    public static String giveAwayCommunity = "";
    public static String sortingValueCommunity = "";


    public static List<NearMeFilterModel> myNearbyListCommunity = new ArrayList<>();

    public static String orderId = "";
    public static String isStackUpload = "";

    public static List<CommunityListModel.ResDataBooks.CommunityList> communityListIds = new ArrayList<>();
    public static String selectedCommunityIds = "";
    public static List<String> selectedCommunityList = new ArrayList<>();

    public static String selectedStackCommunityIds = "";
    public static List<String> selectedStackCommunityList = new ArrayList<>();

    public static String selectedBulkCommunityIds = "";
    public static List<String> selectedBulkCommunityList = new ArrayList<>();

    public static String showCatPopup = "false";
    public static String setCurrentLoc = "false";

}
