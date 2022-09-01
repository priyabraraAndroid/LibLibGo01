package com.lib.liblibgo.dashboard.apartment_admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.lib.liblibgo.R;
import com.lib.liblibgo.api.AllApiClass;
import com.lib.liblibgo.api.Holders;
import com.lib.liblibgo.common.Constants;
import com.lib.liblibgo.common.UserDatabase;
import com.lib.liblibgo.dashboard.book_collect.CollectApartmentBooksActivity;
import com.lib.liblibgo.dashboard.library.EditLibraryActivity;
import com.lib.liblibgo.dashboard.library.fragments.CommunityLibraryFragment;
import com.lib.liblibgo.model.subadmin.OwnLibraryProfModel;
import com.lib.liblibgo.views.MyTextView;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SubAdminDashboard extends AppCompatActivity implements View.OnClickListener{
    private MyTextView titleTool;
    private ImageView backTool;
    private UserDatabase preferences;
    private LinearLayout layoutMyBooks, layoutBookHistory, layoutCustomerManagement,
            layoutBookManagement, layoutCustomerHistory, layoutNotification,MenuLayout,layoutAddApartmentAdmin,manageCommunityUser;
    private String apartId;
    private String apartName;
    private CircleImageView imageView;
    private TextView libraryNameTv;
    private TextView libraryAdrTv;
    private TextView tv_withdrawal;
    private TextView tv_lib_coins;
    private CardView cardProfile;
    private ProgressBar progBar;
    private RelativeLayout rl_prof;
    private UserDatabase database;
    private String isOwnLirary = "1";
    private ImageView ivEdit;
    private String LibId = "";
    private String LibName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_admin_dashboard);

        titleTool = findViewById(R.id.titleTool);
        backTool = findViewById(R.id.backTool);
        setTopView(getString(R.string.aprtment_dashboard));

        apartId = Constants.USER_APARTMENT_ID;
        apartName = Constants.USER_APARTMENT_NAME;

        Constants.USER_APARTMENT_ID = apartId;
        Constants.USER_APARTMENT_NAME = apartName;

        database = new UserDatabase(this);

        layoutMyBooks = (LinearLayout) findViewById(R.id.layoutMyBooks);
        layoutBookHistory = (LinearLayout) findViewById(R.id.layoutBookHistory);
        layoutCustomerManagement = (LinearLayout) findViewById(R.id.layoutCustomerManagement);
        layoutBookManagement = (LinearLayout) findViewById(R.id.layoutBookManagement);
        layoutCustomerHistory = (LinearLayout) findViewById(R.id.layoutCustomerHistory);
        layoutNotification = (LinearLayout) findViewById(R.id.layoutNotification);
        layoutAddApartmentAdmin = (LinearLayout) findViewById(R.id.layoutAddApartmentAdmin);
        manageCommunityUser = (LinearLayout) findViewById(R.id.manageCommunityUser);

        layoutMyBooks.setOnClickListener(this);
        layoutBookHistory.setOnClickListener(this);
        layoutCustomerManagement.setOnClickListener(this);
        layoutBookManagement.setOnClickListener(this);
        layoutCustomerHistory.setOnClickListener(this);
        layoutNotification.setOnClickListener(this);
        layoutAddApartmentAdmin.setOnClickListener(this);
        manageCommunityUser.setOnClickListener(this);

        imageView = findViewById(R.id.iv_user);
        libraryNameTv = findViewById(R.id.tv_library_name);
        libraryAdrTv = findViewById(R.id.library_adr);
        tv_withdrawal = findViewById(R.id.tv_withdrawal);
        tv_lib_coins = findViewById(R.id.tv_lib_coins);
        cardProfile = findViewById(R.id.cardProfile);
        progBar = findViewById(R.id.progBar);
        rl_prof = findViewById(R.id.rl_prof);
        ivEdit = findViewById(R.id.ivEdit);

        /*if (Constants.isOwnLibrary == true){
            getLibraryProfile();
            cardProfile.setVisibility(View.VISIBLE);
        }else {
            cardProfile.setVisibility(View.GONE);
        }*/

        if (Constants.isOwnLibrary == true){
            isOwnLirary = "1";
            manageCommunityUser.setVisibility(View.GONE);
        }else {
            isOwnLirary = "0";
            manageCommunityUser.setVisibility(View.VISIBLE);
        }

        getLibraryProfile();

    }

    private void getLibraryProfile() {
        progBar.setVisibility(View.VISIBLE);
        Holders holders = AllApiClass.getInstance().getApi();
        Call<OwnLibraryProfModel> modelCall = holders.getLibraryDetails(database.getUserId(),isOwnLirary);
        modelCall.enqueue(new Callback<OwnLibraryProfModel>() {
            @Override
            public void onResponse(Call<OwnLibraryProfModel> call, Response<OwnLibraryProfModel> response) {
                if (response.isSuccessful()){
                    if (response.body().getResponse().getCode().equals("1")){
                        progBar.setVisibility(View.GONE);
                        rl_prof.setVisibility(View.VISIBLE);

                        libraryNameTv.setText(response.body().getResponse().getLibrary_name());
                        libraryAdrTv.setText(response.body().getResponse().getLibrary_address());
                        LibId = response.body().getResponse().getLibrary_id();
                        LibName = response.body().getResponse().getLibrary_name();
                        Constants.USER_APARTMENT_ID = response.body().getResponse().getApartment_id();
                        Constants.SelectedLibraryProfileImage = response.body().getResponse().getLibrary_image();

                        tv_lib_coins.setText(response.body().getResponse().getLibcoins());

                        tv_withdrawal.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (Integer.parseInt(response.body().getResponse().getLibcoins()) >= 2000){
                                    Toast.makeText(SubAdminDashboard.this, "Amount withdrawal successfully !", Toast.LENGTH_SHORT).show();
                                }else {
                                    Toast.makeText(SubAdminDashboard.this, "You need 2000 LibCoins to withdrawal.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                        ivEdit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Dexter.withContext(SubAdminDashboard.this).withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                        .withListener(new MultiplePermissionsListener() {
                                            @Override
                                            public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                                                Intent intent = new Intent(SubAdminDashboard.this, EditLibraryActivity.class);
                                                intent.putExtra("name",response.body().getResponse().getLibrary_name());
                                                intent.putExtra("adr",response.body().getResponse().getLibrary_address());
                                                intent.putExtra("img",response.body().getResponse().getLibrary_image());
                                                startActivity(intent);
                                            }

                                            @Override
                                            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                                                permissionToken.continuePermissionRequest();
                                            }
                                        }).check();
                            }
                        });

                        if (response.body().getResponse().getLibrary_image().equals("")){
                            if (Constants.SelectedLibraryProfileImage.equals("")){
                                Glide.with(SubAdminDashboard.this).load(R.drawable.no_img).into(imageView);
                            }else {
                                Glide.with(SubAdminDashboard.this).load(Constants.SelectedLibraryProfileImage).into(imageView);
                            }
                        }else {
                            Glide.with(SubAdminDashboard.this).load(response.body().getResponse().getLibrary_image()).into(imageView);
                        }

                    }else {
                        progBar.setVisibility(View.GONE);
                        rl_prof.setVisibility(View.GONE);
                    }
                }else {
                    progBar.setVisibility(View.GONE);
                    rl_prof.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<OwnLibraryProfModel> call, Throwable t) {
                progBar.setVisibility(View.GONE);
                rl_prof.setVisibility(View.GONE);
            }
        });
    }

    private void setTopView(String string) {
        titleTool.setText(string);
        backTool.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                finish();
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layoutMyBooks:
                SharedPreferences sharedpreferences = getSharedPreferences(CommunityLibraryFragment.MyPREFERENCES, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString("community_id","0");
                editor.commit();
                Intent intent = new Intent(SubAdminDashboard.this, CollectApartmentBooksActivity.class);
                intent.putExtra("lib_id",LibId);
                intent.putExtra("community_id","0");
                intent.putExtra("library_user_id",database.getUserId());
                intent.putExtra("library_name",LibName);
                //Toast.makeText(getContext(), ""+response.body().getResponse().getCommunity_id(), Toast.LENGTH_SHORT).show();
                intent.putExtra("isLibrary","true");
                Constants.libraryType = "virtual";
                Constants.isOwnLibrary = false;
                startActivity(intent);
                break;
            case R.id.layoutBookHistory:
                Intent intentBookHistory = new Intent(SubAdminDashboard.this, BookHistoryReport.class);
                startActivity(intentBookHistory);
                break;
            case R.id.layoutCustomerManagement:
                Intent intentCustManagement = new Intent(SubAdminDashboard.this, ManageCutomer.class);
                startActivity(intentCustManagement);
                break;
            case R.id.layoutBookManagement:
                Intent intentUploadBooks = new Intent(SubAdminDashboard.this, UploadBookDetails.class);
                intentUploadBooks.putExtra("name","");
                intentUploadBooks.putExtra("author","");
                intentUploadBooks.putExtra("isbn","");
                intentUploadBooks.putExtra("publish_date","");
                intentUploadBooks.putExtra("description","");
                intentUploadBooks.putExtra("imgUrl","");
                intentUploadBooks.putExtra("rental_price","");
                intentUploadBooks.putExtra("rental_duration","");
                intentUploadBooks.putExtra("book_price","");
                intentUploadBooks.putExtra("category_id","");
                intentUploadBooks.putExtra("category_name","");
                intentUploadBooks.putExtra("shelf_id","");
                intentUploadBooks.putExtra("shelf_name","");
                intentUploadBooks.putExtra("book_id","");
                intentUploadBooks.putExtra("mrp","");
                intentUploadBooks.putExtra("quantity","");
                intentUploadBooks.putExtra("sale_type","");
                intentUploadBooks.putExtra("book_condition","");
                intentUploadBooks.putExtra("security_money","");
                intentUploadBooks.putExtra("giveaway_status","no");
                startActivity(intentUploadBooks);
                break;
            case R.id.layoutCustomerHistory:
                Intent intentCustomerHistory = new Intent(SubAdminDashboard.this, CutomerBookHistoryReport.class);
                startActivity(intentCustomerHistory);
                break;
            case R.id.layoutNotification:
                Intent intentNotifcation = new Intent(SubAdminDashboard.this, BookHistoryActivity.class);
                startActivity(intentNotifcation);
                break;
            case R.id.manageCommunityUser:
                Intent intentCommunityUser = new Intent(SubAdminDashboard.this, ManageCommunityUsersActivity.class);
                startActivity(intentCommunityUser);
                break;
        }
    }

    @Override
    protected void onResume() {
        if (EditLibraryActivity.editLibFlag == 1){
            getLibraryProfile();
            EditLibraryActivity.editLibFlag = 0;
        }
        super.onResume();
    }
}