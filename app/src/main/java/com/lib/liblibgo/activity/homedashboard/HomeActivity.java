package com.lib.liblibgo.activity.homedashboard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.JsonObject;
import com.lib.liblibgo.R;
import com.lib.liblibgo.activity.ContactUsActivity;
import com.lib.liblibgo.activity.LoginWithPhoneNumber;
import com.lib.liblibgo.activity.MyGiftCardActivity;
import com.lib.liblibgo.activity.PrivacyPolicyActivity;
import com.lib.liblibgo.activity.SearchActivity;
import com.lib.liblibgo.activity.SettingsActivity;
import com.lib.liblibgo.activity.UserProfileActivity;
import com.lib.liblibgo.activity.homedashboard.fragmentshome.CartFragment;
import com.lib.liblibgo.activity.homedashboard.fragmentshome.HomeFragment;
import com.lib.liblibgo.activity.homedashboard.fragmentshome.MyAccountFragment;
import com.lib.liblibgo.activity.homedashboard.fragmentshome.MyComLibFragment;
import com.lib.liblibgo.activity.homedashboard.fragmentshome.MyLibraryFragment;
import com.lib.liblibgo.activity.homedashboard.fragmentshome.NewHomeFragment;
import com.lib.liblibgo.activity.homedashboard.fragmentshome.ProfileFragment;
import com.lib.liblibgo.adapter.NearMeBookAdapter;
import com.lib.liblibgo.adapter.SliderAdapter;
import com.lib.liblibgo.api.AllApiClass;
import com.lib.liblibgo.api.Holders;
import com.lib.liblibgo.common.Constants;
import com.lib.liblibgo.common.UserCurrentLocation;
import com.lib.liblibgo.common.UserDatabase;
import com.lib.liblibgo.dashboard.apartment_admin.ManageLibraryActivity;
import com.lib.liblibgo.dashboard.apartment_admin.UploadBookDetails;
import com.lib.liblibgo.dashboard.book_details.BookCatActivity;
import com.lib.liblibgo.dashboard.book_details.CartActivity;
import com.lib.liblibgo.dashboard.book_details.WishListActivity;
import com.lib.liblibgo.dashboard.book_upload.UploadBookActivity;
import com.lib.liblibgo.dashboard.library.CreateLibraryActivity;
import com.lib.liblibgo.dashboard.library.LibraryActivity;
import com.lib.liblibgo.dashboard.notification.NotificationActivity;
import com.lib.liblibgo.dashboard.book_return.ReturnBookActivity;
import com.lib.liblibgo.dashboard.profile.DashboardActivity;
import com.lib.liblibgo.model.BannerModel;
import com.lib.liblibgo.model.BookNewModel;
import com.lib.liblibgo.model.NearMeBookModel;
import com.lib.liblibgo.model.SubmitDataResModel;
import com.lib.liblibgo.model.UserStatusModel;
import com.lib.liblibgo.model.subadmin.LibraryStatusModel;
import com.lib.liblibgo.views.MyTextView;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.smarteist.autoimageslider.SliderView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static androidx.fragment.app.FragmentManager.POP_BACK_STACK_INCLUSIVE;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {
    private LinearLayout MenuLayout,layoutReturnBook,layoutBookCollect,layoutBookHistory,layoutManageApartment,layoutDashboard,layoutAddBook,layoutOutOrder;
    private NavigationView navigationView;
    private Intent intentHome;
    private MyTextView menuProfile,menuLogout,menuEditProfile,menuGiftCard,tvUserName,menuPrivacyPolicy,menuAboutUs,menuTermCondition,menuContactUs,menuSettings;
    private UserDatabase homeData;
    private ImageView iv_menu;
    private ImageView layoutNotification,layoutSearchBook,layoutCart,layoutFav;
    private DrawerLayout drawer;
    private int option = 0;
    UserDatabase database;
    //private String refreshedToken = "";
    List<BannerModel.BannerData.BannerListData> sliderDataArrayList = new ArrayList<>();
    private SliderView sliderView;
    private LinearLayout llSetting;
    private View viewSetting;
    public static int flag = 0;
    private BottomNavigationView mBottomNavigationView;
    List<BookNewModel.ResModelData.NewBookList> bList = new ArrayList<>();
    public static final String MyPREFERENCES = "MyPrefCart";
    private SharedPreferences sharedpreferences;
    private SharedPreferences.Editor editor;
    private int cartCount;
    private ImageView ivAddBook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        sharedpreferences = getSharedPreferences(HomeActivity.MyPREFERENCES, Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();

        initView();
        //getFakeSearchBooks();

    }

    /*private void getFakeSearchBooks() {
        Holders holders = AllApiClass.getInstance().getApi();
        Call<BookNewModel> resCall = holders.getBooksByAllFilters("","","",
                "","","","","","","1","","","0","10");
        resCall.enqueue(new Callback<BookNewModel>() {
            @Override
            public void onResponse(Call<BookNewModel> call, Response<BookNewModel> response) {
                if (response.isSuccessful()){
                    if (response.body().getResponse().getCode().equals("1")){
                        bList = response.body().getResponse().getBook_list();
                        Constants.fakeSearchBookList = bList;
                    }
                }
            }

            @Override
            public void onFailure(Call<BookNewModel> call, Throwable t) {
            }
        });
    }*/

    private void initView() {
        homeData = new UserDatabase(HomeActivity.this);
        navigationView = findViewById(R.id.nav_view);
        MenuLayout = findViewById(R.id.MenuLayout);
        layoutReturnBook = findViewById(R.id.layoutReturnBook);
        layoutBookCollect = findViewById(R.id.layoutBookCollect);
        layoutBookHistory = findViewById(R.id.layoutBookHistory);
        layoutNotification = findViewById(R.id.layoutNotification);
        layoutSearchBook = findViewById(R.id.layoutSearchBook);
        layoutCart = findViewById(R.id.layoutCart);
        layoutFav = findViewById(R.id.layoutFav);
        layoutManageApartment = findViewById(R.id.layoutManageApartment);
        layoutDashboard = findViewById(R.id.layout_dashboard);
        layoutAddBook = findViewById(R.id.layoutAddBook);
        layoutOutOrder = findViewById(R.id.layoutOutOrder);
        ivAddBook = findViewById(R.id.ivAddBook);

        sliderView = findViewById(R.id.slider);

        /*sliderDataArrayList.add(new SliderData(R.drawable.banner));
        sliderDataArrayList.add(new SliderData(R.drawable.banner));
        sliderDataArrayList.add(new SliderData(R.drawable.banner));*/

        mBottomNavigationView = (BottomNavigationView)findViewById(R.id.bottom_navigation);
        viewFragment(new NewHomeFragment(), "home_fragment");
        mBottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.item1:
                        viewFragment(new NewHomeFragment(), "other_fragment");
                        //mAddPostBtn.setVisibility(View.VISIBLE);
                        return true;
                    case R.id.item2:
                        viewFragment(new ProfileFragment(), "other_fragment");
                        return true;
                    case R.id.item3:
                        viewFragment(new MyAccountFragment(), "other_fragment");
                        return true;
                    /*case R.id.item4:
                        viewFragment(new CartFragment(), "other_fragment");
                        //notificationNotificationBadge.setVisibility(View.GONE);
                        //mAddPostBtn.setVisibility(View.GONE);
                        return true;*/
                    case R.id.item5:
                        viewFragment(new MyComLibFragment(), "other_fragment");
                        //notificationChatBadge.setVisibility(View.GONE);
                        //mAddPostBtn.setVisibility(View.GONE);
                        return true;
                }
                return false;
            }
        });
        //getSliderData();

        ivAddBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (database.getUserId().equals("")){
                    Intent intentHome = new Intent(HomeActivity.this, LoginWithPhoneNumber.class);
                    startActivity(intentHome);
                }else {
                    //isApartmentLibraryCreated();
                    editor.putString("community_id","0");
                    editor.commit();
                    Intent intent = new Intent(HomeActivity.this, UploadBookActivity.class);
                    Constants.isOwnLibrary = false;
                    Constants.libraryType = "virtual";
                    intent.putExtra("name","");
                    intent.putExtra("author","");
                    intent.putExtra("isbn","");
                    intent.putExtra("publish_date","");
                    intent.putExtra("description","");
                    intent.putExtra("imgUrl","");
                    intent.putExtra("rental_price","");
                    intent.putExtra("rental_duration","");
                    intent.putExtra("book_price","");
                    intent.putExtra("category_id","");
                    intent.putExtra("category_name","");
                    intent.putExtra("shelf_id","");
                    intent.putExtra("shelf_name","");
                    intent.putExtra("book_id","");
                    intent.putExtra("mrp","");
                    intent.putExtra("quantity","");
                    intent.putExtra("sale_type","");
                    intent.putExtra("book_condition","");
                    intent.putExtra("security_money","");
                    intent.putExtra("giveaway_status","no");
                    startActivity(intent);
                }
            }
        });

        // menu initialize
        tvUserName = findViewById(R.id.UserName);

        menuProfile = findViewById(R.id.profile_details);
        menuEditProfile = findViewById(R.id.edit_profile);
        menuGiftCard = findViewById(R.id.gift_card);
        menuLogout = findViewById(R.id.log_out);
        menuPrivacyPolicy = findViewById(R.id.privacyPolicy);
        menuAboutUs = findViewById(R.id.aboutUs);
        menuTermCondition = findViewById(R.id.termCondition);
        menuContactUs = findViewById(R.id.contactUs);
        menuSettings = findViewById(R.id.settings);
        iv_menu = findViewById(R.id.iv_menu);
        llSetting = findViewById(R.id.llSetting);
        viewSetting = findViewById(R.id.viewSetting);

        //click events
        layoutReturnBook.setOnClickListener(this);
        layoutBookCollect.setOnClickListener(this);
        layoutBookHistory.setOnClickListener(this);
        layoutNotification.setOnClickListener(this);
        menuProfile.setOnClickListener(this);
        menuLogout.setOnClickListener(this);
        menuEditProfile.setOnClickListener(this);
        iv_menu.setOnClickListener(this);
        layoutSearchBook.setOnClickListener(this);
        layoutCart.setOnClickListener(this);
        layoutFav.setOnClickListener(this);
        layoutManageApartment.setOnClickListener(this);
        layoutDashboard.setOnClickListener(this);
        layoutAddBook.setOnClickListener(this);
        layoutOutOrder.setOnClickListener(this);
        menuPrivacyPolicy.setOnClickListener(this);
        menuAboutUs.setOnClickListener(this);
        menuTermCondition.setOnClickListener(this);
        menuContactUs.setOnClickListener(this);
        menuSettings.setOnClickListener(this);
        menuGiftCard.setOnClickListener(this);

        drawer = (DrawerLayout)findViewById(R.id.drawer);
        database = new UserDatabase(HomeActivity.this);

        UserCurrentLocation location = new UserCurrentLocation(this);
        Log.d("myLoc","lat : "+location.latitude);
        Log.d("myLoc","lan : "+location.longitude);

        if (database.getUserId().equals("")){
            tvUserName.setText("Guest");
            menuEditProfile.setVisibility(View.GONE);
            llSetting.setVisibility(View.GONE);
            viewSetting.setVisibility(View.GONE);
            menuLogout.setText("Login");
        }else {
            menuEditProfile.setVisibility(View.VISIBLE);
            llSetting.setVisibility(View.VISIBLE);
            viewSetting.setVisibility(View.VISIBLE);
            menuLogout.setText("Logout");
            if (homeData.getNAME().equals("")){
                tvUserName.setText("+91 "+homeData.getPHONE());
            }else {
                tvUserName.setText(homeData.getNAME());
            }
        }

        /*if (!database.getUserId().equals("")){
            getUserDetails();
        }*/
    }

    private void viewFragment(Fragment fragment, String name){
        final FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.content, fragment);
        // 1. Know how many fragments there are in the stack
        final int count = fragmentManager.getBackStackEntryCount();
        // 2. If the fragment is **not** "home type", save it to the stack
        if( name.equals("other_fragment") ) {
            fragmentTransaction.addToBackStack(name);
        }
        // Commit !
        fragmentTransaction.commit();
        // 3. After the commit, if the fragment is not an "home type" the back stack is changed, triggering the
        // OnBackStackChanged callback
        fragmentManager.addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                // If the stack decreases it means I clicked the back button
                if( fragmentManager.getBackStackEntryCount() <= count){
                    // pop all the fragment and remove the listener
                    fragmentManager.popBackStack("other_fragment", POP_BACK_STACK_INCLUSIVE);
                    fragmentManager.removeOnBackStackChangedListener(this);
                    // set the home button selected
                    mBottomNavigationView.getMenu().getItem(0).setChecked(true);
                    //mAddPostBtn.setVisibility(View.VISIBLE);
                }
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

                        SliderAdapter adapter = new SliderAdapter(getApplicationContext(), sliderDataArrayList);
                        sliderView.setAutoCycleDirection(SliderView.LAYOUT_DIRECTION_LTR);
                        sliderView.setSliderAdapter(adapter);
                        sliderView.setScrollTimeInSec(8);
                        sliderView.setAutoCycle(true);
                        sliderView.startAutoCycle();
                    }
                }else {
                    Toast.makeText(HomeActivity.this, "Error in banner.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<BannerModel> call, Throwable t) {
                Toast.makeText(HomeActivity.this, "Error in banner.", Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.edit_profile:
                intentHome = new Intent(HomeActivity.this, UserProfileActivity.class);
                startActivity(intentHome);
                break;
            case R.id.iv_menu:
                drawer.openDrawer(GravityCompat.START);
                break;
            case R.id.log_out:
                if (database.getUserId().equals("")){
                    intentHome = new Intent(HomeActivity.this, LoginWithPhoneNumber.class);
                    startActivity(intentHome);
                }else {
                    userLogOut();
                }
                break;
            case R.id.gift_card:
                if (database.getUserId().equals("")){
                    intentHome = new Intent(HomeActivity.this,LoginWithPhoneNumber.class);
                    startActivity(intentHome);
                }else {
                    intentHome = new Intent(HomeActivity.this, MyGiftCardActivity.class);
                    startActivity(intentHome);
                }
                break;
            case R.id.privacyPolicy:
                intentHome = new Intent(HomeActivity.this, PrivacyPolicyActivity.class);
                intentHome.putExtra("value","0");
                startActivity(intentHome);
                break;
            case R.id.termCondition:
                intentHome = new Intent(HomeActivity.this,PrivacyPolicyActivity.class);
                intentHome.putExtra("value","3");
                startActivity(intentHome);
                break;
            case R.id.aboutUs:
                intentHome = new Intent(HomeActivity.this,PrivacyPolicyActivity.class);
                intentHome.putExtra("value","1");
                startActivity(intentHome);
                break;
            case R.id.contactUs:
                intentHome = new Intent(HomeActivity.this, ContactUsActivity.class);
                startActivity(intentHome);
                break;
            case R.id.settings:
                intentHome = new Intent(HomeActivity.this, SettingsActivity.class);
                startActivity(intentHome);
                break;
            case R.id.layoutCart:
                if (database.getUserId().equals("")){
                    intentHome = new Intent(HomeActivity.this,LoginWithPhoneNumber.class);
                    startActivity(intentHome);
                }else {
                    intentHome = new Intent(HomeActivity.this, CartActivity.class);
                    startActivity(intentHome);
                }
                break;
            case R.id.layoutFav:
                if (database.getUserId().equals("")){
                    intentHome = new Intent(HomeActivity.this,LoginWithPhoneNumber.class);
                    startActivity(intentHome);
                }else {
                    intentHome = new Intent(HomeActivity.this, WishListActivity.class);
                    startActivity(intentHome);
                }
                break;
            case R.id.layoutReturnBook:
                if (database.getUserId().equals("")){
                    intentHome = new Intent(HomeActivity.this,LoginWithPhoneNumber.class);
                    startActivity(intentHome);
                }else {
                    option = 1;
                    checkUserActiveStatus(option);
                }
                break;
            case R.id.layoutBookCollect:
               // option = 2;
                //checkUserActiveStatus(option);
                Dexter.withContext(HomeActivity.this).withPermissions(Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA)
                        .withListener(new MultiplePermissionsListener() {
                            @Override
                            public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                                intentHome = new Intent(HomeActivity.this, BookCatActivity.class);
                                startActivity(intentHome);
                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                                permissionToken.continuePermissionRequest();
                            }
                        }).check();
                break;
            case R.id.layoutBookHistory:
                //option = 3;
                //checkUserActiveStatus(option);
                intentHome = new Intent(HomeActivity.this, LibraryActivity.class);
                startActivity(intentHome);
                break;
            case R.id.layoutNotification:
                if (database.getUserId().equals("")){
                    intentHome = new Intent(HomeActivity.this,LoginWithPhoneNumber.class);
                    startActivity(intentHome);
                }else {
                    option = 4;
                    checkUserActiveStatus(option);
                }
                break;
            case R.id.layoutSearchBook:
                if (database.getUserId().equals("")){
                    intentHome = new Intent(HomeActivity.this,LoginWithPhoneNumber.class);
                    startActivity(intentHome);
                }else {
                    option = 5;
                    checkUserActiveStatus(option);
                }
                break;
            case R.id.layoutManageApartment:
                intentHome = new Intent(HomeActivity.this, ManageLibraryActivity.class);
                startActivity(intentHome);
                break;
            case R.id.layout_dashboard:
                if (database.getUserId().equals("")){
                    intentHome = new Intent(HomeActivity.this,LoginWithPhoneNumber.class);
                    startActivity(intentHome);
                }else {
                    option = 6;
                    checkUserActiveStatus(option);
                }
                break;
            case R.id.layoutAddBook:
                if (database.getUserId().equals("")){
                    intentHome = new Intent(HomeActivity.this,LoginWithPhoneNumber.class);
                    startActivity(intentHome);
                }else {
                    option = 7;
                    checkUserActiveStatus(option);
                }
                break;
            case R.id.layoutOutOrder:
                if (database.getUserId().equals("")){
                    intentHome = new Intent(HomeActivity.this,LoginWithPhoneNumber.class);
                    startActivity(intentHome);
                }else {
                    option = 6;
                    checkUserActiveStatus(option);
                }
                break;
        }
    }

    private void userLogOut() {
        final ProgressDialog loginDialog = new ProgressDialog(HomeActivity.this);
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
                        homeData.clearSession();
                        LoginManager.getInstance().logOut();
                        GoogleSignInOptions gso = new GoogleSignInOptions.
                                Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).
                                build();
                        GoogleSignInClient googleSignInClient= GoogleSignIn.getClient(HomeActivity.this,gso);
                        googleSignInClient.signOut();
                        intentHome = new Intent(HomeActivity.this, HomeActivity.class);
                        intentHome.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intentHome);
                        finish();
                    }else {
                        loginDialog.dismiss();
                        Toast.makeText(HomeActivity.this, response.body().getResponse().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }else {
                    loginDialog.dismiss();
                    Toast.makeText(HomeActivity.this, "Something went wrong !", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<SubmitDataResModel> call, Throwable t) {
                loginDialog.dismiss();
                Toast.makeText(HomeActivity.this, "Check Your Internet Connection.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void checkUserActiveStatus(int option) {
        final ProgressDialog progressBar = new ProgressDialog(HomeActivity.this);
        progressBar.setMessage("Please wait...");
        progressBar.setCancelable(false);
        progressBar.show();
        Holders holders = AllApiClass.getInstance().getApi();
        Call<UserStatusModel> msgCall = holders.checkActiveStatus(database.getUserId());
        msgCall.enqueue(new Callback<UserStatusModel>() {
            @Override
            public void onResponse(Call<UserStatusModel> call, Response<UserStatusModel> response) {
                if (response.isSuccessful()){
                    progressBar.dismiss();
                        if (response.body().getResponse().getStatus().equals("1")){
                            if (option == 1){
                                intentHome = new Intent(HomeActivity.this, ReturnBookActivity.class);
                                startActivity(intentHome);
                            }else if (option == 2){
                                intentHome = new Intent(HomeActivity.this, BookCatActivity.class);
                                startActivity(intentHome);
                            }else if (option == 3){
                                intentHome = new Intent(HomeActivity.this, LibraryActivity.class);
                                startActivity(intentHome);
                            }else if (option == 4){
                                intentHome = new Intent(HomeActivity.this, NotificationActivity.class);
                                startActivity(intentHome);
                            }else if (option == 5){
                                intentHome = new Intent(HomeActivity.this, SearchActivity.class);
                                startActivity(intentHome);
                            }else if (option == 6){
                                intentHome = new Intent(HomeActivity.this, DashboardActivity.class);
                                startActivity(intentHome);
                            }else if (option == 7){
                                checkUserLibraryStatus();
                            }
                        }else {
                            ShowPopUp(R.layout.contact_admin);
                        }
                }else {
                    progressBar.dismiss();
                    Toast.makeText(HomeActivity.this, "Something went wrong !", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserStatusModel> call, Throwable t) {
                progressBar.dismiss();
                Toast.makeText(HomeActivity.this, ""+t, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void checkUserLibraryStatus() {
        Dexter.withContext(this).withPermissions(Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                        isLibraryCreated();
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                }).check();
    }

    private void isLibraryCreated() {
        final ProgressDialog progressBar = new ProgressDialog(HomeActivity.this);
        progressBar.setMessage("Please wait...");
        progressBar.setCancelable(false);
        progressBar.show();
        Holders holders = AllApiClass.getInstance().getApi();
        Call<LibraryStatusModel> resCall = holders.checkLibraryStatus(database.getUserId());
        resCall.enqueue(new Callback<LibraryStatusModel>() {
            @Override
            public void onResponse(Call<LibraryStatusModel> call, Response<LibraryStatusModel> response) {
                if (response.isSuccessful()){
                    progressBar.dismiss();
                    if (response.body().getResponse().getLibrary_status().equals("no")){
                        UserCurrentLocation location = new UserCurrentLocation(HomeActivity.this);
                        Log.d("locationnn",""+location.latitude);
                        Log.d("locationnn",""+location.longitude);
                        Constants.latitude = String.valueOf(location.latitude);
                        Constants.longitude = String.valueOf(location.longitude);
                        Constants.SelectedLibraryProfileImage = "";
                        Intent intent = new Intent(HomeActivity.this, CreateLibraryActivity.class);
                        Constants.USER_APARTMENT_NAME = response.body().getResponse().getApartment_name();
                        Constants.USER_APARTMENT_ID = response.body().getResponse().getApartment_id();
                        Constants.USER_FLAT_ID = response.body().getResponse().getFlat_id();
                        Constants.USER_FLAT_NAME = response.body().getResponse().getFlat_no();
                        Constants.isOwnLibrary = true;
                        startActivity(intent);
                    }else {
                        Intent intent = new Intent(HomeActivity.this, UploadBookDetails.class);
                        Constants.isOwnLibrary = true;
                        intent.putExtra("name","");
                        intent.putExtra("author","");
                        intent.putExtra("isbn","");
                        intent.putExtra("publish_date","");
                        intent.putExtra("description","");
                        intent.putExtra("imgUrl","");
                        intent.putExtra("rental_price","");
                        intent.putExtra("rental_duration","15");
                        intent.putExtra("book_price","");
                        intent.putExtra("category_id","");
                        intent.putExtra("category_name","");
                        intent.putExtra("shelf_id","");
                        intent.putExtra("shelf_name","");
                        intent.putExtra("book_id","");
                        intent.putExtra("mrp","");
                        intent.putExtra("quantity","");
                        intent.putExtra("sale_type","");
                        intent.putExtra("book_condition","");
                        intent.putExtra("security_money","");
                        intent.putExtra("giveaway_status","no");
                        startActivity(intent);
                    }
                }else {
                    progressBar.dismiss();
                    Toast.makeText(HomeActivity.this, "Something went wrong !", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LibraryStatusModel> call, Throwable t) {
                progressBar.dismiss();
                Toast.makeText(HomeActivity.this, "Check your internet !", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getUserDetails() {
        final ProgressDialog progressBar = new ProgressDialog(HomeActivity.this);
        progressBar.setMessage("Please wait...");
        progressBar.setCancelable(false);
        progressBar.show();
        Holders holders = AllApiClass.getInstance().getApi();
        Call<JsonObject> call = holders.userDetails(database.getUserId());
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()){
                    try {
                        progressBar.dismiss();
                        JSONObject jsonObject = new JSONObject(response.body().toString());
                        JSONObject object = jsonObject.getJSONObject("response");
                        if (object.getString("code").equals("1")){

                            String apartmentName = object.getString("apartment_name");
                            String flatId = object.getString("flat_no");
                           // String adr = object.getString("address");

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
                                menuLogout.setText("Login");
                            }else {
                                menuEditProfile.setVisibility(View.VISIBLE);
                                llSetting.setVisibility(View.VISIBLE);
                                viewSetting.setVisibility(View.VISIBLE);
                                menuLogout.setText("Logout");
                                if (homeData.getNAME().equals("")){
                                    tvUserName.setText("+91 "+homeData.getPHONE());
                                }else {
                                    tvUserName.setText(homeData.getNAME());
                                }
                            }


                        }else {
                            progressBar.dismiss();
                            Toast.makeText(HomeActivity.this, ""+object.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else {
                    progressBar.dismiss();
                    Toast.makeText(HomeActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                progressBar.dismiss();
                Toast.makeText(HomeActivity.this, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void ShowPopUp(int layout) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(HomeActivity.this);
        final View layoutView = LayoutInflater.from(HomeActivity.this).inflate(layout, null);
        dialogBuilder.setView(layoutView);
        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation1;
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();
        alertDialog.setCancelable(false);
        alertDialog.getWindow().setLayout(800, ViewGroup.LayoutParams.WRAP_CONTENT);

        ImageView close = layoutView.findViewById(R.id.close);
        Button tv_call = layoutView.findViewById(R.id.tv_call);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });

        tv_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
                Intent callIntent = new Intent(Intent.ACTION_CALL); //use ACTION_CALL class
                callIntent.setData(Uri.parse("tel:9804940000"));    //this is the phone number calling
                //check permission
                //If the device is running Android 6.0 (API level 23) and the app's targetSdkVersion is 23 or higher,
                //the system asks the user to grant approval.
                if (ActivityCompat.checkSelfPermission(HomeActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    //request permission from user if the app hasn't got the required permission
                    ActivityCompat.requestPermissions(HomeActivity.this,
                            new String[]{Manifest.permission.CALL_PHONE},   //request specific permission from user
                            10);
                    return;
                }else {     //have got permission
                    try{
                        startActivity(callIntent);  //call activity and make phone call
                    }
                    catch (android.content.ActivityNotFoundException ex){
                        Toast.makeText(getApplicationContext(),"yourActivity is not founded",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }

    @Override
    protected void onResume() {
        cartCount = sharedpreferences.getInt("cart_count",0);
        editor.putInt("cart_count",cartCount);
        editor.commit();
        Log.d("myCart", String.valueOf(cartCount));
        super.onResume();
    }
}