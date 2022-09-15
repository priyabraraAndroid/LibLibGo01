package com.lib.liblibgo.activity.homedashboard.fragmentshome;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.OnSuccessListener;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.lib.liblibgo.R;
import com.lib.liblibgo.activity.LoginWithPhoneNumber;
import com.lib.liblibgo.activity.SearchActivity;
import com.lib.liblibgo.activity.homedashboard.HomeActivity;
import com.lib.liblibgo.adapter.BookAdapterNew;
import com.lib.liblibgo.adapter.CategoryAdapter;
import com.lib.liblibgo.adapter.JoinedCommunityAdapter;
import com.lib.liblibgo.adapter.LibraryApartmentAdapter;
import com.lib.liblibgo.adapter.OtherCommunityAdapter;
import com.lib.liblibgo.adapter.OwnCommunityAdapter;
import com.lib.liblibgo.adapter.SaleBackAdapter;
import com.lib.liblibgo.adapter.SliderAdapter;
import com.lib.liblibgo.api.AllApiClass;
import com.lib.liblibgo.api.Holders;
import com.lib.liblibgo.common.Constants;
import com.lib.liblibgo.common.UserCurrentLocation;
import com.lib.liblibgo.common.UserDatabase;
import com.lib.liblibgo.dashboard.book_collect.CollectApartmentBooksActivity;
import com.lib.liblibgo.dashboard.book_details.BookCatActivity;
import com.lib.liblibgo.dashboard.book_details.CartActivity;
import com.lib.liblibgo.dashboard.library.CommunityLibraryRulesActivity;
import com.lib.liblibgo.dashboard.library.CreateLibraryActivity;
import com.lib.liblibgo.dashboard.library.LibraryActivity;
import com.lib.liblibgo.dashboard.library.fragments.CommunityLibraryFragment;
import com.lib.liblibgo.dashboard.notification.NotificationActivity;
import com.lib.liblibgo.model.BannerModel;
import com.lib.liblibgo.model.BooksModel;
import com.lib.liblibgo.model.CategoryListData;
import com.lib.liblibgo.model.CategoryModel;
import com.lib.liblibgo.model.CheckCommunityModel;
import com.lib.liblibgo.model.CommunityModel;
import com.lib.liblibgo.model.CommunityStatusModel;
import com.lib.liblibgo.model.MyOwnBooksModel;
import com.lib.liblibgo.model.subadmin.LibraryStatusModel;
import com.smarteist.autoimageslider.SliderView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executor;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.lib.liblibgo.notification.MyFirebaseMessagingService.badge;

public class NewHomeFragment extends Fragment {

    private ImageView ivChangeLocation;
    private LinearLayout llLocation;

    LocationManager lm;
    boolean gps_enabled = false;
    boolean network_enabled = false;
    private Context mContext;
    public static final int LOCATION_SETTINGS_REQUEST_CODE = 1000;

    private FusedLocationProviderClient fusedLocationClient;
    private int locationRequestCode = 1000;
    private double wayLatitude = 0.0, wayLongitude = 0.0;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;

    private TextView tvApartment;
    private TextView tvAdr;
    private UserDatabase database;

    private String pinCode="";
    private RelativeLayout rlSearch;
    private PopupWindow popupWindow;
    private EditText et_search;
    private ImageView ivNotification;
    private ImageView ivCart;
    public static View viewBadge;

    List<BannerModel.BannerData.BannerListData> sliderDataArrayList = new ArrayList<>();
    private SliderView sliderView;
    private TextView tvCartCount;
    private int cartCount;
    private SharedPreferences sharedpreferences;
    private SharedPreferences.Editor editor;
    private RecyclerView rvGiveAwayBooks;
    private RecyclerView rvOwnCommunity;
    private RecyclerView rvJoinedCommunity;
    private RecyclerView rvOtherCommunity;
    private ProgressBar progBarCommunity;
    private List<CommunityModel.CommunityResponse.OwnCommunityResponse> myCommunityList = new ArrayList<>();
    private List<CommunityModel.CommunityResponse.JoinedCommunityResponse> joinedCommunityList = new ArrayList<>();
    private List<CommunityModel.CommunityResponse.OtherCommunityResponse> otherCommunityList = new ArrayList<>();
    private OwnCommunityAdapter ownCommunityAdapter;
    private OtherCommunityAdapter otherCommunityAdapter;
    private JoinedCommunityAdapter joinedCommunityAdapter;
    private CardView rlAddLib;
    private RecyclerView rvNearMeBooks;
    private ProgressBar progNearMe;
    private List<BooksModel.BookResponse.BooksList> nearMeList = new ArrayList<>();
    private List<BooksModel.BookResponse.BooksList> giveAwayList = new ArrayList<>();
    private List<BooksModel.BookResponse.BooksList> saleBookList = new ArrayList<>();
    private List<BooksModel.BookResponse.BooksList> rentBookList = new ArrayList<>();
    private List<BooksModel.BookResponse.BooksList> popularBookList = new ArrayList<>();
    private BookAdapterNew adapter;
    private TextView tvCommunitySeeAll;
    private TextView tvCommunityTxt;
    private ProgressBar progBarGiveAway;
    private RecyclerView rvSaleBooks;
    private ProgressBar progBarSale;
    private RecyclerView rvRentBooks;
    private ProgressBar progBarRent;
    private RecyclerView rvPopularBooks;
    private ProgressBar progBarPopular;
    private TextView tvSeeAll;
    private LinearLayout llAllBooksFilter;
    private TextView tvPopularBooksSeeAll;
    private TextView tvGivewayBooksSeeAll;
    private TextView tvSaleBooksSeeAll;
    private TextView tvRentBooksSeeAll;
    private TextView tvCatSeeAll;

    private List<CategoryListData> catList = new ArrayList<>();
    private CategoryAdapter adapterCategory;
    private RecyclerView rvCategory;
    private ProgressBar progBarCategory;
    private TextView tvLocationSave;
    private TextView tvSaleBackSeeAll;
    private ImageView ivSaleBackBanner;
    private RelativeLayout rlSaleBack;
    private RecyclerView rvSaleBackBooks;
    private ProgressBar progBarSaleBack;
    List<MyOwnBooksModel.ResModelData.MyBookList> myList = new ArrayList<>();
    private RelativeLayout rlSellBackView;
    private Button btnSaleBack;
    private LinearLayout llCommunityText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_new_home, container, false);

        lm = (LocationManager)mContext.getSystemService(Context.LOCATION_SERVICE);

        database = new UserDatabase(mContext);
        sharedpreferences = mContext.getSharedPreferences(HomeActivity.MyPREFERENCES, Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();
        cartCount = sharedpreferences.getInt("cart_count",0);

        ivChangeLocation = (ImageView)view.findViewById(R.id.ivChangeLocation);
        ivNotification = (ImageView)view.findViewById(R.id.iv_notification);
        ivCart = (ImageView)view.findViewById(R.id.ivCart);
        llLocation = (LinearLayout)view.findViewById(R.id.llLocation);
        tvApartment = (TextView) view.findViewById(R.id.tvApartment);
        tvAdr = (TextView) view.findViewById(R.id.tvAdr);
        rlSearch = (RelativeLayout) view.findViewById(R.id.rlSearch);
        et_search = (EditText) view.findViewById(R.id.et_search);
        sliderView = view.findViewById(R.id.slider);
        viewBadge = view.findViewById(R.id.viewBadge);
        tvCartCount = view.findViewById(R.id.tvCartCount);
        tvCommunitySeeAll = (TextView)view.findViewById(R.id.tvCommunitySeeAll);
        tvCommunityTxt = (TextView)view.findViewById(R.id.tvCommunityTxt);
        tvSeeAll = (TextView)view.findViewById(R.id.tvSeeAll);
        tvPopularBooksSeeAll = (TextView)view.findViewById(R.id.tvPopularBooksSeeAll);
        tvGivewayBooksSeeAll = (TextView)view.findViewById(R.id.tvGivewayBooksSeeAll);
        tvSaleBooksSeeAll = (TextView)view.findViewById(R.id.tvSaleBooksSeeAll);
        tvRentBooksSeeAll = (TextView)view.findViewById(R.id.tvRentBooksSeeAll);
        tvCatSeeAll = (TextView)view.findViewById(R.id.tvCatSeeAll);
        tvLocationSave = (TextView)view.findViewById(R.id.tvLocationSave);
        tvSaleBackSeeAll = (TextView)view.findViewById(R.id.tvSaleBackSeeAll);
        ivSaleBackBanner = (ImageView) view.findViewById(R.id.ivSaleBackBanner);
        rlSaleBack = (RelativeLayout) view.findViewById(R.id.rlSaleBack);
        rlSellBackView = (RelativeLayout) view.findViewById(R.id.rlSellBackView);
        btnSaleBack = (Button) view.findViewById(R.id.btnSaleBack);

        llAllBooksFilter = (LinearLayout)view.findViewById(R.id.llAllBooksFilter);
        llCommunityText = (LinearLayout)view.findViewById(R.id.llCommunityText);

        //Fetch Banner image.....
        getSliderData();


        llLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showChangeLocationPopup(view);
            }
        });

        et_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*if (database.getUserId().equals("")){
                    Intent intent = new Intent(mContext, LoginWithPhoneNumber.class);
                    startActivity(intent);
                }else {
                    Intent intent = new Intent(mContext, SearchActivity.class);
                    startActivity(intent);
                }*/
                Intent intent = new Intent(mContext, SearchActivity.class);
                startActivity(intent);
            }
        });

        ivNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (database.getUserId().equals("")){
                    Intent intent = new Intent(mContext, LoginWithPhoneNumber.class);
                    startActivity(intent);
                }else {
                    badge = "0";
                    viewBadge.setVisibility(View.GONE);
                    Intent intent = new Intent(mContext, NotificationActivity.class);
                    startActivity(intent);
                }
            }
        });

        ivCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (database.getUserId().equals("")){
                    Intent intent = new Intent(mContext, LoginWithPhoneNumber.class);
                    startActivity(intent);
                }else {
                    Intent intent = new Intent(mContext, CartActivity.class);
                    startActivity(intent);
                }
            }
        });


        rvOwnCommunity = (RecyclerView)view.findViewById(R.id.rvOwnCommunity);
        progBarCommunity = (ProgressBar)view.findViewById(R.id.progBarCommunity);
        rvJoinedCommunity = (RecyclerView)view.findViewById(R.id.rvJoinedCommunity);
        rvOtherCommunity = (RecyclerView)view.findViewById(R.id.rvOtherCommunity);
        rlAddLib = (CardView)view.findViewById(R.id.rlAddLib);

        rvNearMeBooks = (RecyclerView)view.findViewById(R.id.rvNearMeBooks);
        progNearMe = (ProgressBar)view.findViewById(R.id.progNearMe);

        rvGiveAwayBooks = (RecyclerView)view.findViewById(R.id.rvGiveAwayBooks);
        progBarGiveAway = (ProgressBar)view.findViewById(R.id.progBarGiveAway);

        rvSaleBooks = (RecyclerView)view.findViewById(R.id.rvSaleBooks);
        progBarSale = (ProgressBar)view.findViewById(R.id.progBarSale);

        rvRentBooks = (RecyclerView)view.findViewById(R.id.rvRentBooks);
        progBarRent = (ProgressBar)view.findViewById(R.id.progBarRent);

        rvPopularBooks = (RecyclerView)view.findViewById(R.id.rvPopularBooks);
        progBarPopular = (ProgressBar)view.findViewById(R.id.progBarPopular);

        rvCategory = (RecyclerView)view.findViewById(R.id.rvCategory);
        progBarCategory = (ProgressBar)view.findViewById(R.id.progBarCategory);

        rvSaleBackBooks = (RecyclerView)view.findViewById(R.id.rvSaleBackBooks);
        progBarSaleBack = (ProgressBar)view.findViewById(R.id.progBarSaleBack);

        getCategoryList();

        rlAddLib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (database.getUserId().equals("")){
                    Intent intent = new Intent(mContext, LoginWithPhoneNumber.class);
                    startActivity(intent);
                }else {
                    Dexter.withContext(getContext()).withPermissions(Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION)
                            .withListener(new MultiplePermissionsListener() {
                                @Override
                                public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                                    createCommunity();
                                }
                                @Override
                                public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                                    permissionToken.continuePermissionRequest();
                                }
                            }).check();
                }
            }
        });

        tvSeeAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), LibraryActivity.class);
                startActivity(intent);
            }
        });

        tvCatSeeAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Constants.selectedFilterValueCommunity = "";
                Constants.giveAwayCommunity = "";
                Constants.showCatPopup = "true";
                Constants.selectedCategoriesCommunity = "";
                Constants.sortingValueCommunity = "";
                Intent intent = new Intent(getContext(), BookCatActivity.class);
                startActivity(intent);
            }
        });

        llAllBooksFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Constants.selectedFilterValueCommunity = "";
                Constants.giveAwayCommunity = "";
                Constants.selectedCategoriesCommunity = "";
                Constants.showCatPopup = "false";
                Constants.sortingValueCommunity = "";
                Intent intent = new Intent(getContext(), BookCatActivity.class);
                startActivity(intent);
            }
        });

        tvCommunitySeeAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Constants.selectedFilterValueCommunity = "";
                Constants.giveAwayCommunity = "";
                Constants.selectedCategoriesCommunity = "";
                Constants.showCatPopup = "false";
                Constants.sortingValueCommunity = "";
                Intent intent = new Intent(getContext(), BookCatActivity.class);
                startActivity(intent);
            }
        });

        tvPopularBooksSeeAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Constants.selectedFilterValueCommunity = "";
                Constants.giveAwayCommunity = "";
                Constants.selectedCategoriesCommunity = "";
                Constants.showCatPopup = "false";
                Constants.sortingValueCommunity = "popular";
                Intent intent = new Intent(getContext(), BookCatActivity.class);
                startActivity(intent);
            }
        });

        tvGivewayBooksSeeAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Constants.selectedFilterValueCommunity = "";
                Constants.giveAwayCommunity = "yes";
                Constants.selectedCategoriesCommunity = "";
                Constants.showCatPopup = "false";
                Constants.sortingValueCommunity = "";
                Intent intent = new Intent(getContext(), BookCatActivity.class);
                startActivity(intent);
            }
        });

        tvSaleBooksSeeAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Constants.giveAwayCommunity = "";
                Constants.selectedFilterValueCommunity = "For Sale";
                Constants.selectedCategoriesCommunity = "";
                Constants.showCatPopup = "false";
                Constants.sortingValueCommunity = "";
                Intent intent = new Intent(getContext(), BookCatActivity.class);
                startActivity(intent);
            }
        });

        tvRentBooksSeeAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Constants.giveAwayCommunity = "";
                Constants.selectedFilterValueCommunity = "For Rent";
                Constants.selectedCategoriesCommunity = "";
                Constants.showCatPopup = "false";
                Constants.sortingValueCommunity = "";
                Intent intent = new Intent(getContext(), BookCatActivity.class);
                startActivity(intent);
            }
        });

        btnSaleBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (database.getUserId().equals("")){
                    Intent intent = new Intent(mContext,LoginWithPhoneNumber.class);
                    startActivity(intent);
                }else {
                    Toast.makeText(mContext, "You are not buy any books yet !", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    private void getCategoryList() {
        progBarCategory.setVisibility(View.VISIBLE);
        Holders holders = AllApiClass.getInstance().getApi();
        Call<CategoryModel> resCall = holders.getCategoryListNew();
        resCall.enqueue(new Callback<CategoryModel>() {
            @Override
            public void onResponse(Call<CategoryModel> call, Response<CategoryModel> response) {
                if (response.isSuccessful()){
                    if (response.body().getResponse().getCode().equals("1")){
                        progBarCategory.setVisibility(View.GONE);
                        catList = response.body().getResponse().getApartment_list();
                        if (catList.size() > 0){
                            adapterCategory = new CategoryAdapter(catList, getContext());
                            GridLayoutManager manager = new GridLayoutManager(mContext,4);
                            rvCategory.setLayoutManager(manager);
                            rvCategory.setAdapter(adapterCategory);
                        }
                    }else {
                        progBarCategory.setVisibility(View.GONE);
                    }
                }else {
                    progBarCategory.setVisibility(View.GONE);
                }
            }
            @Override
            public void onFailure(Call<CategoryModel> call, Throwable t) {
                progBarCategory.setVisibility(View.GONE);
            }
        });
    }


    private void createCommunity() {
        final ProgressDialog progressBar = new ProgressDialog(getContext());
        progressBar.setMessage("Please wait...");
        progressBar.setCancelable(false);
        progressBar.show();
        Holders holders = AllApiClass.getInstance().getApi();
        Call<LibraryStatusModel> resCall = holders.checkApartmentLibraryStatus(database.getUserId());
        resCall.enqueue(new Callback<LibraryStatusModel>() {
            @Override
            public void onResponse(Call<LibraryStatusModel> call, Response<LibraryStatusModel> response) {
                if (response.isSuccessful()){
                    progressBar.dismiss();
                    if (response.body().getResponse().getLibrary_status().equals("no")){
                        UserCurrentLocation location = new UserCurrentLocation(getContext());
                        Log.d("locationnn",""+location.latitude);
                        Log.d("locationnn",""+location.longitude);
                        Constants.latitude = String.valueOf(location.latitude);
                        Constants.longitude = String.valueOf(location.longitude);
                        Constants.SelectedLibraryProfileImage = "";
                        Intent intent = new Intent(getContext(), CreateLibraryActivity.class);
                        Constants.USER_APARTMENT_NAME = response.body().getResponse().getApartment_name();
                        Constants.USER_APARTMENT_ID = response.body().getResponse().getApartment_id();
                        Constants.USER_FLAT_ID = response.body().getResponse().getFlat_id();
                        Constants.USER_FLAT_NAME = response.body().getResponse().getFlat_no();
                        intent.putExtra("count",response.body().getResponse().getMy_accepted_count());
                        Constants.isOwnLibrary = false;
                        Constants.libraryType = "virtual";
                        startActivity(intent);
                    }else {
                        if (response.body().getResponse().getIs_active().equals("0")){
                            Toast.makeText(getContext(), "Your library is created.But not approved yet.", Toast.LENGTH_LONG).show();
                        }else {
                            Toast.makeText(getContext(), "You have already created your community library.One user can create only one community library.", Toast.LENGTH_LONG).show();
                        }

                    }
                }else {
                    progressBar.dismiss();
                    Toast.makeText(getContext(), "Something went wrong !", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LibraryStatusModel> call, Throwable t) {
                progressBar.dismiss();
                Toast.makeText(getContext(), "Check your internet !", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getCommunityList() {
        progBarCommunity.setVisibility(View.VISIBLE);
        Holders holders = AllApiClass.getInstance().getApi();
        Call<CommunityModel> call = holders.communityList(database.getUserId(),pinCode,"","");

        call.enqueue(new Callback<CommunityModel>() {
            @Override
            public void onResponse(Call<CommunityModel> call, Response<CommunityModel> response) {
                if (response.isSuccessful()){
                    if (response.body().getResponse().getCode().equals("1")){
                        progBarCommunity.setVisibility(View.GONE);

                        myCommunityList.clear();
                        joinedCommunityList.clear();
                        otherCommunityList.clear();

                        myCommunityList = new ArrayList<>();
                        joinedCommunityList = new ArrayList<>();
                        otherCommunityList = new ArrayList<>();

                        myCommunityList = response.body().getResponse().getOwn_library();
                        joinedCommunityList = response.body().getResponse().getMy_join_library();
                        otherCommunityList = response.body().getResponse().getAll_library_list();

                        int listSize = myCommunityList.size() + joinedCommunityList.size() + otherCommunityList.size();

                        if (myCommunityList.size() > 0){
                            rvOwnCommunity.setVisibility(View.VISIBLE);
                            ownCommunityAdapter = new OwnCommunityAdapter(myCommunityList, getContext());
                            LinearLayoutManager verticalManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
                            rvOwnCommunity.setLayoutManager(verticalManager);
                            rvOwnCommunity.setAdapter(ownCommunityAdapter);
                        }else {
                            rvOwnCommunity.setVisibility(View.GONE);
                        }

                        if (joinedCommunityList.size() > 0){
                            rvJoinedCommunity.setVisibility(View.VISIBLE);
                            joinedCommunityAdapter = new JoinedCommunityAdapter(joinedCommunityList, getContext());
                            LinearLayoutManager verticalManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
                            rvJoinedCommunity.setLayoutManager(verticalManager);
                            rvJoinedCommunity.setAdapter(joinedCommunityAdapter);
                        }else {
                            rvJoinedCommunity.setVisibility(View.GONE);
                        }

                        if (otherCommunityList.size() > 0 ){
                            rvOtherCommunity.setVisibility(View.VISIBLE);
                            otherCommunityAdapter = new OtherCommunityAdapter(otherCommunityList, getContext());
                            LinearLayoutManager verticalManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
                            rvOtherCommunity.setLayoutManager(verticalManager);
                            rvOtherCommunity.setAdapter(otherCommunityAdapter);
                        }else {
                            rvOtherCommunity.setVisibility(View.GONE);
                        }

                        /*if (listSize < 3){
                            tvSeeAll.setVisibility(View.INVISIBLE);
                        }else {
                            tvSeeAll.setVisibility(View.VISIBLE);
                        }*/

                    }
                }else {
                    progBarCommunity.setVisibility(View.GONE);
                    Toast.makeText(mContext, "Something went wrong.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CommunityModel> call, Throwable t) {
                progBarCommunity.setVisibility(View.GONE);
                Toast.makeText(mContext, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void getSliderData() {
        Holders holders = AllApiClass.getInstance().getApi();
        Call<BannerModel> call = holders.getBanners();
        call.enqueue(new Callback<BannerModel>() {
            @Override
            public void onResponse(Call<BannerModel> call, Response<BannerModel> response) {
                if (response.isSuccessful()){
                    if (response.body().getResponse().getCode().equals("1")){
                        sliderDataArrayList = response.body().getResponse().getBanner_list();
                        SliderAdapter adapter = new SliderAdapter(getContext(), sliderDataArrayList);
                        sliderView.setAutoCycleDirection(SliderView.LAYOUT_DIRECTION_LTR);
                        sliderView.setSliderAdapter(adapter);
                        sliderView.setScrollTimeInSec(5);
                        sliderView.setAutoCycle(true);
                        sliderView.startAutoCycle();
                    }
                }
            }

            @Override
            public void onFailure(Call<BannerModel> call, Throwable t) {
            }
        });
    }

    private void showChangeLocationPopup(View view) {
        LayoutInflater layoutInflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View popupView = layoutInflater.inflate(R.layout.layout_location_change, null);

        popupWindow = new PopupWindow(
                popupView,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);

        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setOutsideTouchable(true);

        final TextView tvCurrentLocation = popupView.findViewById(R.id.tvCurrentLocation);
        final TextView tvRegisteredLocation = popupView.findViewById(R.id.tvRegisteredLocation);

        if (database.getUserId().equals("")){
            tvCurrentLocation.setVisibility(View.GONE);
            tvRegisteredLocation.setVisibility(View.VISIBLE);
        }else {
            if (Constants.setCurrentLoc.equals("true")){
                tvRegisteredLocation.setVisibility(View.VISIBLE);
                tvCurrentLocation.setVisibility(View.GONE);
            }else {
                tvRegisteredLocation.setVisibility(View.GONE);
                tvCurrentLocation.setVisibility(View.VISIBLE);
            }

        }

        tvCurrentLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
                Constants.setCurrentLoc = "true";
                tvCurrentLocation.setVisibility(View.GONE);
                tvRegisteredLocation.setVisibility(View.VISIBLE);
                tvLocationSave.setVisibility(View.VISIBLE);
                getUserAddressDetails();
            }
        });

        tvRegisteredLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
                if (database.getUserId().equals("")){
                    Intent intent = new Intent(mContext,LoginWithPhoneNumber.class);
                    startActivity(intent);
                }else {
                    Constants.setCurrentLoc = "false";
                    tvRegisteredLocation.setVisibility(View.GONE);
                    tvCurrentLocation.setVisibility(View.VISIBLE);
                    tvLocationSave.setVisibility(View.GONE);
                    if (database.getApartmentName().equals("")){
                        tvApartment.setText(database.getAddress().substring(0, database.getAddress().indexOf(",")));
                    }else {
                        tvApartment.setText(database.getApartmentName());
                    }
                    pinCode = database.getPin();
                    tvAdr.setText(database.getAddress()+", "+database.getPin());
                }
            }
        });

        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                //TODO do sth here on dismiss
                popupWindow.dismiss();
            }
        });

        popupWindow.showAsDropDown(view);
    }

    @Override
    public void onResume() {
        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch(Exception ex) {
            ex.printStackTrace();
        }


        if(!gps_enabled && !network_enabled) {
            displayAlert();
        }else {
            if (database.getUserId().equals("")){
                getUserAddressDetails();
            }else {
                if (Constants.setCurrentLoc.equals("true")){
                    getUserAddressDetails();
                    tvLocationSave.setVisibility(View.VISIBLE);
                }else {
                    tvLocationSave.setVisibility(View.GONE);
                    if (database.getApartmentName().equals("")){
                        tvApartment.setText(database.getAddress().substring(0, database.getAddress().indexOf(",")));
                    }else {
                        tvApartment.setText(database.getApartmentName());
                    }
                    pinCode = database.getPin();
                    tvAdr.setText(database.getAddress()+", "+database.getPin());
                }

            }
        }

        showCartCount();
        showNotificationCount();

        getCommunityList();
        getNearMeCommunityBooks();
        getGiveAwayBooks();
        getForSaleBooks();
        getForRentalBooks();
        getPopularBooks();
        getSaleBackBooks();

        super.onResume();
    }

    private void getSaleBackBooks() {
        progBarSaleBack.setVisibility(View.VISIBLE);
        Holders holders = AllApiClass.getInstance().getApi();
        Call<MyOwnBooksModel> call = holders.myPurchaseBooks(database.getUserId());

        call.enqueue(new Callback<MyOwnBooksModel>() {
            @Override
            public void onResponse(Call<MyOwnBooksModel> call, Response<MyOwnBooksModel> response) {
                if (response.isSuccessful()){
                    if (response.body().getResponse().getCode() == 1){
                        rlSaleBack.setVisibility(View.VISIBLE);
                        rlSellBackView.setVisibility(View.GONE);
                        progBarSaleBack.setVisibility(View.GONE);

                        myList = response.body().getResponse().getBook_list();

                        if (myList.size() > 0){
                            SaleBackAdapter adapter = new SaleBackAdapter(myList, getContext());
                            LinearLayoutManager verticalManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
                            rvSaleBackBooks.setLayoutManager(verticalManager);
                            rvSaleBackBooks.setAdapter(adapter);
                        }

                    }else {
                        rlSaleBack.setVisibility(View.GONE);
                        rlSellBackView.setVisibility(View.VISIBLE);
                        progBarSaleBack.setVisibility(View.GONE);
                        //Toast.makeText(mContext, ""+response.body().getResponse().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }else {
                    rlSaleBack.setVisibility(View.GONE);
                    rlSellBackView.setVisibility(View.VISIBLE);
                    progBarSaleBack.setVisibility(View.GONE);
                    //Toast.makeText(mContext, "Something went wrong.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<MyOwnBooksModel> call, Throwable t) {
                rlSaleBack.setVisibility(View.GONE);
                rlSellBackView.setVisibility(View.VISIBLE);
                progBarSaleBack.setVisibility(View.GONE);
                //Toast.makeText(mContext, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getPopularBooks() {
        progBarPopular.setVisibility(View.VISIBLE);
        Holders holders = AllApiClass.getInstance().getApi();
        Call<BooksModel> call = holders.getPouplarBooks(database.getUserId(),pinCode,"8");

        call.enqueue(new Callback<BooksModel>() {
            @Override
            public void onResponse(Call<BooksModel> call, Response<BooksModel> response) {
                if (response.isSuccessful()){
                    if (response.body().getResponse().getCode().equals("1")){
                        progBarPopular.setVisibility(View.GONE);
                        popularBookList = new ArrayList<>();
                        popularBookList = response.body().getResponse().getBook_list();
                        if (popularBookList.size() > 0){
                            adapter = new BookAdapterNew(popularBookList, getContext());
                            LinearLayoutManager verticalManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
                            rvPopularBooks.setLayoutManager(verticalManager);
                            rvPopularBooks.setAdapter(adapter);
                        }
                    }else {
                        progBarPopular.setVisibility(View.GONE);
                        //Toast.makeText(mContext, ""+response.body().getResponse().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }else {
                    progBarPopular.setVisibility(View.GONE);
                    Toast.makeText(mContext, "Something went wrong!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<BooksModel> call, Throwable t) {
                progBarPopular.setVisibility(View.GONE);
                Toast.makeText(mContext, ""+t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void getForRentalBooks() {
        progBarRent.setVisibility(View.VISIBLE);
        Holders holders = AllApiClass.getInstance().getApi();
        Call<BooksModel> call = holders.getForRentBooks(database.getUserId(),pinCode,"8");

        call.enqueue(new Callback<BooksModel>() {
            @Override
            public void onResponse(Call<BooksModel> call, Response<BooksModel> response) {
                if (response.isSuccessful()){
                    if (response.body().getResponse().getCode().equals("1")){
                        progBarRent.setVisibility(View.GONE);
                        rentBookList = new ArrayList<>();
                        rentBookList = response.body().getResponse().getBook_list();
                        if (rentBookList.size() > 0){
                            adapter = new BookAdapterNew(rentBookList, getContext());
                            LinearLayoutManager verticalManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
                            rvRentBooks.setLayoutManager(verticalManager);
                            rvRentBooks.setAdapter(adapter);
                        }
                    }else {
                        progBarRent.setVisibility(View.GONE);
                        //Toast.makeText(mContext, ""+response.body().getResponse().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }else {
                    progBarRent.setVisibility(View.GONE);
                    Toast.makeText(mContext, "Something went wrong!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<BooksModel> call, Throwable t) {
                progBarRent.setVisibility(View.GONE);
                Toast.makeText(mContext, ""+t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void getForSaleBooks() {
        progBarSale.setVisibility(View.VISIBLE);
        Holders holders = AllApiClass.getInstance().getApi();
        Call<BooksModel> call = holders.getForSaleBooks(database.getUserId(),pinCode,"8");

        call.enqueue(new Callback<BooksModel>() {
            @Override
            public void onResponse(Call<BooksModel> call, Response<BooksModel> response) {
                if (response.isSuccessful()){
                    if (response.body().getResponse().getCode().equals("1")){
                        progBarSale.setVisibility(View.GONE);
                        saleBookList = new ArrayList<>();
                        saleBookList = response.body().getResponse().getBook_list();
                        if (saleBookList.size() > 0){
                            adapter = new BookAdapterNew(saleBookList, getContext());
                            LinearLayoutManager verticalManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
                            rvSaleBooks.setLayoutManager(verticalManager);
                            rvSaleBooks.setAdapter(adapter);
                        }
                    }else {
                        progBarSale.setVisibility(View.GONE);
                        //Toast.makeText(mContext, ""+response.body().getResponse().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }else {
                    progBarSale.setVisibility(View.GONE);
                    Toast.makeText(mContext, "Something went wrong!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<BooksModel> call, Throwable t) {
                progBarSale.setVisibility(View.GONE);
                Toast.makeText(mContext, ""+t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void getGiveAwayBooks() {
        progBarGiveAway.setVisibility(View.VISIBLE);
        Holders holders = AllApiClass.getInstance().getApi();
        Call<BooksModel> call = holders.getGiveAwayBooks(database.getUserId(),pinCode,"8");

        call.enqueue(new Callback<BooksModel>() {
            @Override
            public void onResponse(Call<BooksModel> call, Response<BooksModel> response) {
                if (response.isSuccessful()){
                    if (response.body().getResponse().getCode().equals("1")){
                        progBarGiveAway.setVisibility(View.GONE);
                        giveAwayList = new ArrayList<>();
                        giveAwayList = response.body().getResponse().getBook_list();
                        if (giveAwayList.size() > 0){
                            adapter = new BookAdapterNew(giveAwayList, getContext());
                            LinearLayoutManager verticalManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
                            rvGiveAwayBooks.setLayoutManager(verticalManager);
                            rvGiveAwayBooks.setAdapter(adapter);
                        }
                    }else {
                        progBarGiveAway.setVisibility(View.GONE);
                        //Toast.makeText(mContext, ""+response.body().getResponse().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }else {
                    progBarGiveAway.setVisibility(View.GONE);
                    Toast.makeText(mContext, "Something went wrong!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<BooksModel> call, Throwable t) {
                progBarGiveAway.setVisibility(View.GONE);
                Toast.makeText(mContext, ""+t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void getNearMeCommunityBooks() {
        progNearMe.setVisibility(View.VISIBLE);
        Holders holders = AllApiClass.getInstance().getApi();
        Call<BooksModel> call = holders.nearMeCommunityBooks(database.getUserId(),pinCode,"8");

        call.enqueue(new Callback<BooksModel>() {
            @Override
            public void onResponse(Call<BooksModel> call, Response<BooksModel> response) {
                if (response.isSuccessful()){
                    if (response.body().getResponse().getCode().equals("1")){
                        tvCommunityTxt.setVisibility(View.VISIBLE);
                        llCommunityText.setVisibility(View.VISIBLE);
                        tvCommunitySeeAll.setVisibility(View.VISIBLE);
                        progNearMe.setVisibility(View.GONE);
                        nearMeList = new ArrayList<>();
                        nearMeList = response.body().getResponse().getBook_list();
                        if (nearMeList.size() > 0){
                            tvCommunityTxt.setVisibility(View.VISIBLE);
                            llCommunityText.setVisibility(View.VISIBLE);
                            tvCommunitySeeAll.setVisibility(View.VISIBLE);
                            adapter = new BookAdapterNew(nearMeList, getContext());
                            LinearLayoutManager verticalManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
                            rvNearMeBooks.setLayoutManager(verticalManager);
                            rvNearMeBooks.setAdapter(adapter);
                        }else {
                            tvCommunityTxt.setVisibility(View.GONE);
                            llCommunityText.setVisibility(View.GONE);
                            tvCommunitySeeAll.setVisibility(View.GONE);
                        }
                    }else {
                        tvCommunityTxt.setVisibility(View.GONE);
                        llCommunityText.setVisibility(View.GONE);
                        tvCommunitySeeAll.setVisibility(View.GONE);
                        progNearMe.setVisibility(View.GONE);
                        //Toast.makeText(mContext, ""+response.body().getResponse().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }else {
                    tvCommunityTxt.setVisibility(View.GONE);
                    llCommunityText.setVisibility(View.GONE);
                    tvCommunitySeeAll.setVisibility(View.GONE);
                    progNearMe.setVisibility(View.GONE);
                    Toast.makeText(mContext, "Something went wrong!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<BooksModel> call, Throwable t) {
                tvCommunityTxt.setVisibility(View.GONE);
                llCommunityText.setVisibility(View.GONE);
                tvCommunitySeeAll.setVisibility(View.GONE);
                progNearMe.setVisibility(View.GONE);
                Toast.makeText(mContext, ""+t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void showNotificationCount() {
        if (badge.equals("1")){
            NewHomeFragment.viewBadge.setVisibility(View.VISIBLE);
        }else {
            NewHomeFragment.viewBadge.setVisibility(View.GONE);
        }
    }

    private void showCartCount() {
        cartCount = sharedpreferences.getInt("cart_count",0);
        if (cartCount > 0){
            tvCartCount.setVisibility(View.VISIBLE);
            ivCart.setColorFilter(getResources().getColor(R.color.white));
            if (cartCount < 10){
                tvCartCount.setText(String.valueOf(cartCount));
            }else {
                tvCartCount.setText("9+");
            }
        }else {
            editor.putInt("cart_count",0);
            editor.commit();
            tvCartCount.setVisibility(View.INVISIBLE);
            ivCart.setColorFilter(null);
        }
    }

    private void getUserAddressDetails() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(mContext);
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) mContext, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    locationRequestCode);
        } else {
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener((Activity) mContext, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            // Got last known location. In some rare situations this can be null.
                            if (location != null) {

                                wayLatitude = location.getLatitude();
                                wayLongitude = location.getLongitude();
                                Geocoder geocoder = new Geocoder(mContext, Locale.getDefault());
                                List<Address> addresses = null;
                                try {
                                    addresses = geocoder.getFromLocation(wayLatitude, wayLongitude, 2);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                if (addresses != null){
                                    if (addresses.size() > 0) {
                                        String city = addresses.get(0).getSubAdminArea();
                                        String state = addresses.get(0).getAdminArea();
                                        String postalCode = addresses.get(0).getPostalCode();
                                        String apartmentValue = addresses.get(1).getThoroughfare();
                                        String knownName = addresses.get(1).getLocality();
                                        if (apartmentValue == null){
                                            tvApartment.setText(knownName);
                                        }else {
                                            tvApartment.setText(apartmentValue);
                                        }
                                        tvAdr.setText(city + ", " + state+", "+postalCode);
                                        pinCode = postalCode;
                                    } else {
                                        if (!database.getUserId().equals("")){
                                            tvApartment.setText(database.getApartmentName());
                                            tvAdr.setText(database.getAddress()+", "+database.getPin());
                                            pinCode = database.getPin();
                                        }
                                    }
                                }
                                Log.d("myLocationValue","lat :"+String.valueOf(wayLatitude));
                                Log.d("myLocationValue ","lon :"+String.valueOf(wayLongitude));
                                //txtLat.setText(String.valueOf(wayLatitude));
                                //txtLong.setText(String.valueOf(wayLongitude));
                            }else {
                                locationRequest = LocationRequest.create();
                                locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                                //locationRequest.setInterval(1000);
                                locationRequest.setFastestInterval(1000);
                                locationCallback = new LocationCallback() {
                                    @Override
                                    public void onLocationResult(LocationResult locationResult) {
                                        if (locationResult == null) {
                                            Log.d("myLocationValue","lat :"+String.valueOf(wayLatitude));
                                            Log.d("myLocationValue ","lon :"+String.valueOf(wayLongitude));
                                            return;
                                        }
                                        for (Location location : locationResult.getLocations()) {
                                            if (location != null) {
                                                wayLatitude = location.getLatitude();
                                                wayLongitude = location.getLongitude();
                                                Geocoder geocoder = new Geocoder(mContext, Locale.getDefault());
                                                List<Address> addresses = null;
                                                try {
                                                    addresses = geocoder.getFromLocation(wayLatitude, wayLongitude, 2);
                                                } catch (IOException e) {
                                                    e.printStackTrace();
                                                }
                                                if (!addresses.isEmpty() || addresses.size() > 0) {
                                                    String city = addresses.get(0).getSubAdminArea();
                                                    String state = addresses.get(0).getAdminArea();
                                                    String postalCode = addresses.get(0).getPostalCode();
                                                    String apartmentValue = addresses.get(1).getThoroughfare();
                                                    String knownName = addresses.get(1).getLocality();
                                                    if (apartmentValue == null){
                                                        tvApartment.setText(knownName);
                                                    }else {
                                                        tvApartment.setText(apartmentValue);
                                                    }
                                                    tvAdr.setText(city + ", " + state+", "+postalCode);
                                                    pinCode = postalCode;
                                                } else {
                                                    if (!database.getUserId().equals("")){
                                                        tvApartment.setText(database.getApartmentName());
                                                        tvAdr.setText(database.getAddress()+", "+database.getPin());
                                                        pinCode = database.getPin();
                                                    }
                                                }
                                                //txtLat.setText(String.valueOf(wayLatitude));
                                                //txtLong.setText(String.valueOf(wayLongitude));
                                                Log.d("myLocationValue","lat :"+String.valueOf(wayLatitude));
                                                Log.d("myLocationValue ","lon :"+String.valueOf(wayLongitude));
                                            }
                                        }
                                    }
                                };
                            }
                        }
                    });
        }

    }

    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1000: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    fusedLocationClient.getLastLocation().addOnSuccessListener((Executor) mContext, location -> {
                        if (location != null) {
                            wayLatitude = location.getLatitude();
                            wayLongitude = location.getLongitude();
                            Log.d("myLocationValue","lat :"+String.valueOf(wayLatitude));
                            Log.d("myLocationValue ","lon :"+String.valueOf(wayLongitude));
                            //txtAdr.setText(String.format(Locale.US, "%s -- %s", wayLatitude, wayLongitude));
                        }else {
                            locationRequest = LocationRequest.create();
                            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                            //locationRequest.setInterval(1000);
                            locationRequest.setFastestInterval(1000);
                            locationCallback = new LocationCallback() {
                                @Override
                                public void onLocationResult(LocationResult locationResult) {
                                    if (locationResult == null) {
                                        return;
                                    }
                                    for (Location location : locationResult.getLocations()) {
                                        if (location != null) {
                                            wayLatitude = location.getLatitude();
                                            wayLongitude = location.getLongitude();
                                            Geocoder geocoder = new Geocoder(mContext, Locale.getDefault());
                                            List<Address> addresses = null;
                                            try {
                                                addresses = geocoder.getFromLocation(wayLatitude, wayLongitude, 2);
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }
                                            if (!addresses.isEmpty() || addresses.size() > 0) {
                                                String city = addresses.get(0).getSubAdminArea();
                                                String state = addresses.get(0).getAdminArea();
                                                String postalCode = addresses.get(0).getPostalCode();
                                                String apartmentValue = addresses.get(1).getThoroughfare();
                                                String knownName = addresses.get(1).getLocality();
                                                if (apartmentValue == null){
                                                    tvApartment.setText(knownName);
                                                }else {
                                                    tvApartment.setText(apartmentValue);
                                                }
                                                tvAdr.setText(city + ", " + state+", "+postalCode);
                                                pinCode = postalCode;
                                            } else {
                                                if (!database.getUserId().equals("")){
                                                    tvApartment.setText(database.getApartmentName());
                                                    tvAdr.setText(database.getAddress()+", "+database.getPin());
                                                    pinCode = database.getPin();
                                                }
                                            }
                                            //txtLat.setText(String.valueOf(wayLatitude));
                                            //txtLong.setText(String.valueOf(wayLongitude));
                                            Log.d("myLocationValue","lat :"+String.valueOf(wayLatitude));
                                            Log.d("myLocationValue ","lon :"+String.valueOf(wayLongitude));
                                        }
                                    }
                                }
                            };
                        }
                    });
                } else {
                    Toast.makeText(mContext, "Permission denied", Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }
    }

    @Override
    public void onDestroy() {
        if (fusedLocationClient != null) {
            fusedLocationClient.removeLocationUpdates(locationCallback);
        }
        super.onDestroy();
    }

    private void displayAlert() {
        GoogleApiClient googleApiClient = new GoogleApiClient.Builder(mContext)
                .addApi(LocationServices.API).build();
        googleApiClient.connect();

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(1000);
        //locationRequest.setFastestInterval(1000 / 2);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);

        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        Log.i("TAG", "All location settings are satisfied.");
                        //getLocation();
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        Log.i("TAG", "Location settings are not satisfied. Show the user a dialog to upgrade location settings ");

                        try {
                            // Show the dialog by calling startResolutionForResult(), and check the result
                            // in onActivityResult().
                            status.startResolutionForResult((Activity) mContext, LOCATION_SETTINGS_REQUEST_CODE);
                            if (database.getUserId().equals("")){
                                getUserAddressDetails();
                            }else {
                                if (Constants.setCurrentLoc.equals("true")){
                                    getUserAddressDetails();
                                    tvLocationSave.setVisibility(View.VISIBLE);
                                }else {
                                    tvLocationSave.setVisibility(View.GONE);
                                    if (database.getApartmentName().equals("")){
                                        tvApartment.setText(database.getAddress().substring(0, database.getAddress().indexOf(",")));
                                    }else {
                                        tvApartment.setText(database.getApartmentName());
                                    }
                                    pinCode = database.getPin();
                                    tvAdr.setText(database.getAddress()+", "+database.getPin());
                                }
                            }
                        } catch (IntentSender.SendIntentException e) {
                            Log.i("TAG", "PendingIntent unable to execute request.");
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        Log.i("TAG", "Location settings are inadequate, and cannot be fixed here. Dialog not created.");
                        break;
                }
            }
        });
    }

    @Override
    public void onAttach(@NonNull Context context) {
        mContext = context;
        super.onAttach(context);
    }
}