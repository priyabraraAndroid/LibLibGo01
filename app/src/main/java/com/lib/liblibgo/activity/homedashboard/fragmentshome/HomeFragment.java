package com.lib.liblibgo.activity.homedashboard.fragmentshome;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.JsonObject;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.lib.liblibgo.R;
import com.lib.liblibgo.activity.ContactUsActivity;
import com.lib.liblibgo.activity.LoginWithPhoneNumber;
import com.lib.liblibgo.activity.MyGiftCardActivity;
import com.lib.liblibgo.activity.PrivacyPolicyActivity;
import com.lib.liblibgo.activity.SearchActivity;
import com.lib.liblibgo.activity.SettingsActivity;
import com.lib.liblibgo.activity.UserProfileActivity;
import com.lib.liblibgo.activity.homedashboard.HomeActivity;
import com.lib.liblibgo.adapter.SliderAdapter;
import com.lib.liblibgo.api.AllApiClass;
import com.lib.liblibgo.api.Holders;
import com.lib.liblibgo.common.Constants;
import com.lib.liblibgo.common.UserDatabase;
import com.lib.liblibgo.dashboard.book_collect.CollectApartmentBooksActivity;
import com.lib.liblibgo.dashboard.book_details.BookCatActivity;
import com.lib.liblibgo.dashboard.library.fragments.CommunityLibraryFragment;
import com.lib.liblibgo.dashboard.trackingbooks.TrackMyBooksActivity;
import com.lib.liblibgo.dashboard.book_details.WishListActivity;
import com.lib.liblibgo.dashboard.library.LibraryActivity;
import com.lib.liblibgo.dashboard.notification.NotificationActivity;
import com.lib.liblibgo.model.BannerModel;
import com.lib.liblibgo.model.CommunityStatusModel;
import com.lib.liblibgo.model.SubmitDataResModel;
import com.lib.liblibgo.notification.MyFirebaseMessagingService;
import com.lib.liblibgo.views.MyTextView;
import com.smarteist.autoimageslider.SliderView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.lib.liblibgo.notification.MyFirebaseMessagingService.badge;

public class HomeFragment extends Fragment implements View.OnClickListener {
    List<BannerModel.BannerData.BannerListData> sliderDataArrayList = new ArrayList<>();
    private SliderView sliderView;
    private UserDatabase database;
    private NavigationView navigationView;
    private DrawerLayout drawer;
    private ImageView iv_menu,iv_notification;
    private MyTextView menuProfile,menuLogout,menuEditProfile,menuGiftCard,tvUserName,menuPrivacyPolicy,menuAboutUs,menuTermCondition,menuContactUs,menuSettings,wishList;
    private LinearLayout llSetting;
    private View viewSetting;
    public static int flag = 0;
    private Intent intentHome;
    private int option = 0;
    private EditText et_search;
    private LinearLayout menuOne;
    private LinearLayout layoutBooks;
    private LinearLayout layoutTrackBooks;
    private SharedPreferences sharedpreferences;
    private SharedPreferences.Editor editor;
    public static View viewBadge;
    private View btnTrack;
    private View btnJoin;
    private String pfrLibUserId = "";

    //private int cartCount;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        database = new UserDatabase(getContext());
        navigationView = view.findViewById(R.id.nav_view);

        sharedpreferences = getContext().getSharedPreferences(HomeActivity.MyPREFERENCES, Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();
        //cartCount = sharedpreferences.getInt("cart_count",0);

        sliderView = view.findViewById(R.id.slider);
        drawer = (DrawerLayout)view.findViewById(R.id.drawer);

        tvUserName = view.findViewById(R.id.UserName);

        et_search = view.findViewById(R.id.et_search);
        menuProfile = view.findViewById(R.id.profile_details);
        menuEditProfile = view.findViewById(R.id.edit_profile);
        menuGiftCard = view.findViewById(R.id.gift_card);
        menuLogout = view.findViewById(R.id.log_out);
        menuPrivacyPolicy = view.findViewById(R.id.privacyPolicy);
        menuAboutUs = view.findViewById(R.id.aboutUs);
        menuTermCondition = view.findViewById(R.id.termCondition);
        menuContactUs = view.findViewById(R.id.contactUs);
        menuSettings = view.findViewById(R.id.settings);
        iv_menu = view.findViewById(R.id.iv_menu);
        iv_notification = view.findViewById(R.id.iv_notification);
        llSetting = view.findViewById(R.id.llSetting);
        viewSetting = view.findViewById(R.id.viewSetting);
        wishList = view.findViewById(R.id.wishList);
        menuOne = view.findViewById(R.id.menuOne);
        layoutBooks = view.findViewById(R.id.layoutBooks);
        layoutTrackBooks = view.findViewById(R.id.layoutTrackBooks);
        viewBadge = view.findViewById(R.id.viewBadge);

        btnTrack = view.findViewById(R.id.btnTrack);
        btnJoin = view.findViewById(R.id.btnJoin);

        getSliderData();

        if (database.getUserId().equals("")){
            tvUserName.setText("Guest");
            menuEditProfile.setVisibility(View.GONE);
            llSetting.setVisibility(View.GONE);
            viewSetting.setVisibility(View.GONE);
            menuGiftCard.setVisibility(View.GONE);
            wishList.setVisibility(View.GONE);
            menuLogout.setText("Login");
        }else {
            menuEditProfile.setVisibility(View.VISIBLE);
            llSetting.setVisibility(View.VISIBLE);
            viewSetting.setVisibility(View.VISIBLE);
            menuGiftCard.setVisibility(View.GONE);
            wishList.setVisibility(View.VISIBLE);
            menuLogout.setText("Logout");
            if (database.getNAME().equals("")){
                tvUserName.setText("+91 "+database.getPHONE());
            }else {
                tvUserName.setText(database.getNAME());
            }
        }

        iv_notification.setOnClickListener(this);
        menuProfile.setOnClickListener(this);
        menuLogout.setOnClickListener(this);
        menuEditProfile.setOnClickListener(this);
        iv_menu.setOnClickListener(this);
        et_search.setOnClickListener(this);
        //layoutSearchBook.setOnClickListener(this);
        //layoutCart.setOnClickListener(this);
        //layoutFav.setOnClickListener(this);
        //layoutManageApartment.setOnClickListener(this);
        //layoutDashboard.setOnClickListener(this);
        //layoutAddBook.setOnClickListener(this);
        //layoutOutOrder.setOnClickListener(this);
        menuPrivacyPolicy.setOnClickListener(this);
        menuAboutUs.setOnClickListener(this);
        menuTermCondition.setOnClickListener(this);
        menuContactUs.setOnClickListener(this);
        menuSettings.setOnClickListener(this);
        menuGiftCard.setOnClickListener(this);
        wishList.setOnClickListener(this);
        menuOne.setOnClickListener(this);
        layoutBooks.setOnClickListener(this);
        layoutTrackBooks.setOnClickListener(this);
        btnJoin.setOnClickListener(this);
        btnTrack.setOnClickListener(this);

        if (!database.getUserId().equals("")){
            getUserDetails();
        }

        return view;
    }

    private void getUserDetails() {
        Holders holders = AllApiClass.getInstance().getApi();
        Call<JsonObject> call = holders.userDetails(database.getUserId());
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()){
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().toString());
                        JSONObject object = jsonObject.getJSONObject("response");
                        if (object.getString("code").equals("1")){

                            String apartmentName = object.getString("apartment_name");
                            String flatId = object.getString("flat_no");
                            // String adr = object.getString("address");

                            pfrLibUserId = object.getString("pfr_user_id");
                            database.createLogin(database.getUserId(),
                                    object.getString("username"),
                                    object.getString("mobile"),object.getString("pincode"),
                                    object.getString("area_name")+","+object.getString("landmark")+","+object.getString("city")+","+object.getString("state")+","+object.getString("pincode"),
                                    apartmentName,flatId,object.getString("email"));

                            if (database.getUserId().equals("")){
                                tvUserName.setText("Guest");
                                menuEditProfile.setVisibility(View.GONE);
                                llSetting.setVisibility(View.GONE);
                                viewSetting.setVisibility(View.GONE);
                                menuGiftCard.setVisibility(View.GONE);
                                wishList.setVisibility(View.GONE);
                                menuLogout.setText("Login");
                            }else {
                                menuEditProfile.setVisibility(View.VISIBLE);
                                llSetting.setVisibility(View.VISIBLE);
                                viewSetting.setVisibility(View.VISIBLE);
                                menuGiftCard.setVisibility(View.GONE);
                                wishList.setVisibility(View.VISIBLE);
                                menuLogout.setText("Logout");
                                if (database.getNAME().equals("")){
                                    tvUserName.setText("+91 "+database.getPHONE());
                                }else {
                                    tvUserName.setText(database.getNAME());
                                }
                            }

                            Constants.myLibCoins = object.getString("libcoins");
                            Constants.myProfileImage = object.getString("profile_image");

                        }else {
                            //Toast.makeText(getContext(), ""+object.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else {
                   // Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                //Toast.makeText(getContext(), ""+t.getMessage(), Toast.LENGTH_SHORT).show();
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
                }else {
                    //Toast.makeText(getContext(), "Error in banner.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<BannerModel> call, Throwable t) {
                //sliderView.set
                Toast.makeText(getContext(), "No Internet connection.Please check internet permission.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.edit_profile:
                intentHome = new Intent(getContext(), UserProfileActivity.class);
                startActivity(intentHome);
                break;
            case R.id.iv_menu:
                drawer.openDrawer(GravityCompat.START);
                break;
            case R.id.log_out:
                if (database.getUserId().equals("")){
                    intentHome = new Intent(getContext(), LoginWithPhoneNumber.class);
                    startActivity(intentHome);
                }else {
                    userLogOut();
                }
                break;
            case R.id.wishList:
                if (database.getUserId().equals("")){
                    intentHome = new Intent(getContext(),LoginWithPhoneNumber.class);
                    startActivity(intentHome);
                }else {
                    intentHome = new Intent(getContext(), WishListActivity.class);
                    startActivity(intentHome);
                }
                break;
            case R.id.gift_card:
                if (database.getUserId().equals("")){
                    intentHome = new Intent(getContext(),LoginWithPhoneNumber.class);
                    startActivity(intentHome);
                }else {
                    intentHome = new Intent(getContext(), MyGiftCardActivity.class);
                    startActivity(intentHome);
                }
                break;
            case R.id.privacyPolicy:
                intentHome = new Intent(getContext(), PrivacyPolicyActivity.class);
                intentHome.putExtra("value","0");
                startActivity(intentHome);
                break;
            case R.id.termCondition:
                intentHome = new Intent(getContext(),PrivacyPolicyActivity.class);
                intentHome.putExtra("value","3");
                startActivity(intentHome);
                break;
            case R.id.aboutUs:
                intentHome = new Intent(getContext(),PrivacyPolicyActivity.class);
                intentHome.putExtra("value","1");
                startActivity(intentHome);
                break;
            case R.id.contactUs:
                intentHome = new Intent(getContext(), ContactUsActivity.class);
                startActivity(intentHome);
                break;
            case R.id.settings:
                intentHome = new Intent(getContext(), SettingsActivity.class);
                startActivity(intentHome);
                break;
            case R.id.iv_notification:
                if (database.getUserId().equals("")){
                    intentHome = new Intent(getContext(),LoginWithPhoneNumber.class);
                    startActivity(intentHome);
                }else {
                    badge = "0";
                    viewBadge.setVisibility(View.GONE);
                    intentHome = new Intent(getContext(), NotificationActivity.class);
                    startActivity(intentHome);
                }
                break;
            case R.id.btnTrack:
                if (database.getUserId().equals("")){
                    intentHome = new Intent(getContext(),LoginWithPhoneNumber.class);
                    startActivity(intentHome);
                }else {
                    intentHome = new Intent(getContext(), TrackMyBooksActivity.class);
                    startActivity(intentHome);
                }
                break;
            case R.id.et_search:
                if (database.getUserId().equals("")){
                    intentHome = new Intent(getContext(),LoginWithPhoneNumber.class);
                    startActivity(intentHome);
                }else {
                    Dexter.withContext(getContext()).withPermissions(Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION)
                            .withListener(new MultiplePermissionsListener() {
                                @Override
                                public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                                    Constants.searchFrom = "community";
                                    intentHome = new Intent(getContext(), SearchActivity.class);
                                    startActivity(intentHome);
                                }

                                @Override
                                public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                                    permissionToken.continuePermissionRequest();
                                }
                            }).check();
                }

                break;
            case R.id.btnJoin:
                Dexter.withContext(getContext()).withPermissions(Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION)
                        .withListener(new MultiplePermissionsListener() {
                            @Override
                            public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                                if (database.getUserId().equals("")){
                                    intentHome = new Intent(getContext(), LibraryActivity.class);
                                    startActivity(intentHome);
                                }else {
                                    //checkRequest();
                                    Constants.isOwnLibrary = false;
                                    if (database.getUserId().equals(pfrLibUserId)){
                                        SharedPreferences sharedpreferences = getActivity().getSharedPreferences(CommunityLibraryFragment.MyPREFERENCES, Context.MODE_PRIVATE);
                                        SharedPreferences.Editor editor = sharedpreferences.edit();
                                        editor.putString("community_id","0");
                                        editor.commit();
                                        Intent intent = new Intent(getContext(), CollectApartmentBooksActivity.class);
                                        intent.putExtra("lib_id","66");
                                        intent.putExtra("community_id","0");
                                        intent.putExtra("library_user_id",pfrLibUserId);
                                        intent.putExtra("library_name","pfrLib");
                                        //Toast.makeText(getContext(), ""+response.body().getResponse().getCommunity_id(), Toast.LENGTH_SHORT).show();
                                        intent.putExtra("isLibrary","true");
                                        Constants.libraryType = "virtual";
                                        startActivity(intent);
                                    }else {
                                        checkRequest();
                                    }
                                }
                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                                permissionToken.continuePermissionRequest();
                            }
                        }).check();
                break;
            case R.id.layoutBooks:
                if (database.getUserId().equals("")){
                    intentHome = new Intent(getContext(),LoginWithPhoneNumber.class);
                    startActivity(intentHome);
                }else {
                    Dexter.withContext(getContext()).withPermissions(Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION)
                            .withListener(new MultiplePermissionsListener() {
                                @Override
                                public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                                    intentHome = new Intent(getContext(), BookCatActivity.class);
                                    startActivity(intentHome);
                                }

                                @Override
                                public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                                    permissionToken.continuePermissionRequest();
                                }
                            }).check();
                }
                break;
        }
    }

    private void checkRequest() {
        final ProgressDialog progressBar = new ProgressDialog(getContext());
        progressBar.setMessage("Please wait...");
        progressBar.setCancelable(false);
        progressBar.show();
        Holders holders = AllApiClass.getInstance().getApi();
        Call<CommunityStatusModel> resCall = holders.checkCommunityRequestStatus("66",database.getUserId());
        resCall.enqueue(new Callback<CommunityStatusModel>() {
            @Override
            public void onResponse(Call<CommunityStatusModel> call, Response<CommunityStatusModel> response) {
                if (response.isSuccessful()){
                    if (response.body().getResponse().getCode() == 1){
                        progressBar.dismiss();
                        if (response.body().getResponse().getStatus().equals("1")){
                            SharedPreferences sharedpreferences = getActivity().getSharedPreferences(CommunityLibraryFragment.MyPREFERENCES, Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedpreferences.edit();
                            editor.putString("community_id",response.body().getResponse().getCommunity_id());
                            editor.commit();
                            Intent intent = new Intent(getContext(), CollectApartmentBooksActivity.class);
                            intent.putExtra("lib_id","66");
                            intent.putExtra("community_id",response.body().getResponse().getCommunity_id());
                            intent.putExtra("library_user_id",pfrLibUserId);
                            intent.putExtra("library_name","pfrLib");
                            //Toast.makeText(getContext(), ""+response.body().getResponse().getCommunity_id(), Toast.LENGTH_SHORT).show();
                            intent.putExtra("isLibrary","true");
                            Constants.libraryType = "virtual";
                            startActivity(intent);
                        }else if (response.body().getResponse().getStatus().equals("2")){
                            intentHome = new Intent(getContext(), LibraryActivity.class);
                            startActivity(intentHome);
                            Toast.makeText(getContext(), "Your request is rejected by the community.", Toast.LENGTH_SHORT).show();
                        }else if (response.body().getResponse().getStatus().equals("0")){
                            intentHome = new Intent(getContext(), LibraryActivity.class);
                            startActivity(intentHome);
                            Toast.makeText(getContext(), "Your request is pending yet.", Toast.LENGTH_SHORT).show();
                        }else {
                            intentHome = new Intent(getContext(), LibraryActivity.class);
                            startActivity(intentHome);
                            Toast.makeText(getContext(), "Join first to see books of this community.", Toast.LENGTH_SHORT).show();
                        }
                        //Toast.makeText(getContext(), "", Toast.LENGTH_SHORT).show();
                    }else {
                        progressBar.dismiss();
                        intentHome = new Intent(getContext(), LibraryActivity.class);
                        startActivity(intentHome);
                        Toast.makeText(getContext(), "Join first to see books of this community.", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    progressBar.dismiss();
                    Toast.makeText(getContext(), "Something went wrong !", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CommunityStatusModel> call, Throwable t) {
                progressBar.dismiss();
                Toast.makeText(getContext(), ""+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void userLogOut() {
        final ProgressDialog loginDialog = new ProgressDialog(getContext());
        loginDialog.setMessage("Please wait..");
        loginDialog.setCancelable(false);
        loginDialog.show();
        Holders holders = AllApiClass.getInstance().getApi();
        Call<SubmitDataResModel> call = holders.userLogout(database.getUserId());
        call.enqueue(new Callback<SubmitDataResModel>() {
            @Override
            public void onResponse(Call<SubmitDataResModel> call, Response<SubmitDataResModel> response) {
                if (response.isSuccessful()){
                    if (response.body().getResponse().getCode() == 1){
                        loginDialog.dismiss();
                        database.clearSession();
                        editor.clear();
                        editor.commit();
                        LoginManager.getInstance().logOut();
                        GoogleSignInOptions gso = new GoogleSignInOptions.
                                Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).
                                build();
                        GoogleSignInClient googleSignInClient= GoogleSignIn.getClient(getContext(),gso);
                        googleSignInClient.signOut();
                        intentHome = new Intent(getContext(), HomeActivity.class);
                        intentHome.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intentHome);
                        getActivity().finish();
                    }else {
                        loginDialog.dismiss();
                        Toast.makeText(getContext(), response.body().getResponse().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }else {
                    loginDialog.dismiss();
                    Toast.makeText(getContext(), "Something went wrong !", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<SubmitDataResModel> call, Throwable t) {
                loginDialog.dismiss();
                Toast.makeText(getContext(), "Check Your Internet Connection.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onResume() {
        if (flag == 1){
            database = new UserDatabase(getContext());
            if (!database.getUserId().equals("")){
                getUserDetails();
                flag = 0;
            }
        }

        if (badge.equals("1")){
            HomeFragment.viewBadge.setVisibility(View.VISIBLE);
        }else {
            HomeFragment.viewBadge.setVisibility(View.GONE);
        }

        super.onResume();
    }
}