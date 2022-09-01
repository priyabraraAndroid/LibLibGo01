package com.lib.liblibgo.dashboard.apartment_admin;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.lib.liblibgo.R;
import com.lib.liblibgo.activity.LoginWithPhoneNumber;
import com.lib.liblibgo.api.AllApiClass;
import com.lib.liblibgo.api.Holders;
import com.lib.liblibgo.common.Constants;
import com.lib.liblibgo.common.UserCurrentLocation;
import com.lib.liblibgo.common.UserDatabase;
import com.lib.liblibgo.dashboard.library.CommunityLibraryRulesActivity;
import com.lib.liblibgo.dashboard.library.CreateLibraryActivity;
import com.lib.liblibgo.model.SendRequestStatusModel;
import com.lib.liblibgo.model.subadmin.LibraryStatusModel;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ManageLibraryActivity extends AppCompatActivity {

    private UserDatabase dataBase;
    private Intent intentHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_library);

        dataBase = new UserDatabase(this);

    }

    public void onBack(View view) {
        onBackPressed();
    }

    public void onOwnLibrary(View view) {
        Dexter.withContext(this).withPermissions(Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                        if (dataBase.getUserId().equals("")){
                            intentHome = new Intent(ManageLibraryActivity.this, LoginWithPhoneNumber.class);
                            startActivity(intentHome);
                        }else {
                            isLibraryCreated();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                }).check();
    }

    private void isLibraryCreated() {
        final ProgressDialog progressBar = new ProgressDialog(ManageLibraryActivity.this);
        progressBar.setMessage("Please wait...");
        progressBar.setCancelable(false);
        progressBar.show();
        Holders holders = AllApiClass.getInstance().getApi();
        Call<LibraryStatusModel> resCall = holders.checkLibraryStatus(dataBase.getUserId());
        resCall.enqueue(new Callback<LibraryStatusModel>() {
            @Override
            public void onResponse(Call<LibraryStatusModel> call, Response<LibraryStatusModel> response) {
                if (response.isSuccessful()){
                    progressBar.dismiss();
                    if (response.body().getResponse().getLibrary_status().equals("no")){
                        UserCurrentLocation location = new UserCurrentLocation(ManageLibraryActivity.this);
                        Log.d("locationnn",""+location.latitude);
                        Log.d("locationnn",""+location.longitude);
                        Constants.latitude = String.valueOf(location.latitude);
                        Constants.longitude = String.valueOf(location.longitude);
                        Constants.SelectedLibraryProfileImage = "";
                        Intent intent = new Intent(ManageLibraryActivity.this, CreateLibraryActivity.class);
                        Constants.USER_APARTMENT_NAME = response.body().getResponse().getApartment_name();
                        Constants.USER_APARTMENT_ID = response.body().getResponse().getApartment_id();
                        Constants.USER_FLAT_ID = response.body().getResponse().getFlat_id();
                        Constants.USER_FLAT_NAME = response.body().getResponse().getFlat_no();
                        Constants.isOwnLibrary = true;
                        startActivity(intent);
                    }else {
                        Intent intent = new Intent(ManageLibraryActivity.this, SubAdminDashboard.class);
                        Constants.isOwnLibrary = true;
                        Constants.libraryType = response.body().getResponse().getLibrary_type();
                        startActivity(intent);
                    }
                }else {
                    progressBar.dismiss();
                    Toast.makeText(ManageLibraryActivity.this, "Something went wrong !", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LibraryStatusModel> call, Throwable t) {
                progressBar.dismiss();
                Toast.makeText(ManageLibraryActivity.this, "Check your internet !", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void onApartmentLibrary(View view) {
        Dexter.withContext(this).withPermissions(Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                        if (dataBase.getUserId().equals("")){
                            intentHome = new Intent(ManageLibraryActivity.this, LoginWithPhoneNumber.class);
                            startActivity(intentHome);
                        }if (dataBase.getApartmentName().equals("") || dataBase.getApartmentName().equals("null")){
                            Toast.makeText(ManageLibraryActivity.this, "You are not belongs to any community !", Toast.LENGTH_SHORT).show();
                        }else {
                            //checkUserSubAdminStatus();
                            isApartmentLibraryCreated();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                }).check();

    }

    private void isApartmentLibraryCreated() {
        final ProgressDialog progressBar = new ProgressDialog(ManageLibraryActivity.this);
        progressBar.setMessage("Please wait...");
        progressBar.setCancelable(false);
        progressBar.show();
        Holders holders = AllApiClass.getInstance().getApi();
        Call<LibraryStatusModel> resCall = holders.checkApartmentLibraryStatus(dataBase.getUserId());
        resCall.enqueue(new Callback<LibraryStatusModel>() {
            @Override
            public void onResponse(Call<LibraryStatusModel> call, Response<LibraryStatusModel> response) {
                if (response.isSuccessful()){
                    progressBar.dismiss();
                    if (response.body().getResponse().getLibrary_status().equals("no")){
                        UserCurrentLocation location = new UserCurrentLocation(ManageLibraryActivity.this);
                        Log.d("locationnn",""+location.latitude);
                        Log.d("locationnn",""+location.longitude);
                        Constants.latitude = String.valueOf(location.latitude);
                        Constants.longitude = String.valueOf(location.longitude);
                        Constants.SelectedLibraryProfileImage = "";
                        Intent intent = new Intent(ManageLibraryActivity.this, CommunityLibraryRulesActivity.class);
                        Constants.USER_APARTMENT_NAME = response.body().getResponse().getApartment_name();
                        Constants.USER_APARTMENT_ID = response.body().getResponse().getApartment_id();
                        Constants.USER_FLAT_ID = response.body().getResponse().getFlat_id();
                        Constants.USER_FLAT_NAME = response.body().getResponse().getFlat_no();
                        intent.putExtra("count",response.body().getResponse().getMy_accepted_count());
                        Constants.isOwnLibrary = false;
                        startActivity(intent);
                    }else {
                        Intent intent = new Intent(ManageLibraryActivity.this, SubAdminDashboard.class);
                        Constants.isOwnLibrary = false;
                        Constants.libraryType = response.body().getResponse().getLibrary_type();
                        startActivity(intent);
                    }
                }else {
                    progressBar.dismiss();
                    Toast.makeText(ManageLibraryActivity.this, "Something went wrong !", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LibraryStatusModel> call, Throwable t) {
                progressBar.dismiss();
                Toast.makeText(ManageLibraryActivity.this, "Check your internet !", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void checkUserSubAdminStatus() {
        final ProgressDialog progressBar = new ProgressDialog(ManageLibraryActivity.this);
        progressBar.setMessage("Please wait...");
        progressBar.setCancelable(false);
        progressBar.show();
        Holders holders = AllApiClass.getInstance().getApi();
        Call<SendRequestStatusModel> resCall = holders.checkSendRequestStatus(dataBase.getUserId());
        resCall.enqueue(new Callback<SendRequestStatusModel>() {
            @Override
            public void onResponse(Call<SendRequestStatusModel> call, Response<SendRequestStatusModel> response) {
                if (response.isSuccessful()){
                    if (response.body().getResponse().getCode().equals("1")){
                        progressBar.dismiss();
                        /*intentHome = new Intent(HomeActivity.this, SubAdminDashboard.class);
                        intentHome.putExtra("apart_id", response.body().getResponse().getApartment_id());
                        intentHome.putExtra("apart_name", response.body().getResponse().getApartment_name());
                        Constants.USER_APARTMENT_NAME = response.body().getResponse().getApartment_name();
                        Constants.USER_APARTMENT_ID = response.body().getResponse().getApartment_id();
                        startActivity(intentHome);*/
                        UserCurrentLocation location = new UserCurrentLocation(ManageLibraryActivity.this);
                        Log.d("locationnn",""+location.latitude);
                        Log.d("locationnn",""+location.longitude);
                        Constants.latitude = String.valueOf(location.latitude);
                        Constants.longitude = String.valueOf(location.longitude);
                        if (response.body().getResponse().getApartment_name().equals("") || response.body().getResponse().getApartment_name().equals("null")){
                            Toast.makeText(ManageLibraryActivity.this, "You are not belog to any community !", Toast.LENGTH_SHORT).show();
                        }else {
                            if (response.body().getResponse().getRequest_status().equals("1")){
                                intentHome = new Intent(ManageLibraryActivity.this, SubAdminDashboard.class);
                                //intentHome.putExtra("apart_id", response.body().getResponse().getApartment_id());
                                //intentHome.putExtra("apart_name", response.body().getResponse().getApartment_name());
                                Constants.USER_APARTMENT_NAME = response.body().getResponse().getApartment_name();
                                Constants.USER_APARTMENT_ID = response.body().getResponse().getApartment_id();
                                startActivity(intentHome);
                                Constants.isOwnLibrary = false;
                            }else {
                                intentHome = new Intent(ManageLibraryActivity.this, SingUpApartmentAdminActivity.class);
                                intentHome.putExtra("request_status", response.body().getResponse().getRequest_status());
                                Constants.isOwnLibrary = false;
                                startActivity(intentHome);
                            }
                        }
                    }else {
                        progressBar.dismiss();
                        Toast.makeText(ManageLibraryActivity.this, ""+response.body().getResponse().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }else {
                    progressBar.dismiss();
                    Toast.makeText(ManageLibraryActivity.this, "Something went wrong !", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<SendRequestStatusModel> call, Throwable t) {
                progressBar.dismiss();
                Toast.makeText(ManageLibraryActivity.this, "Check your internet !", Toast.LENGTH_SHORT).show();
            }
        });
    }
}