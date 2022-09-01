package com.lib.liblibgo.dashboard.library;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.lib.liblibgo.R;
import com.lib.liblibgo.activity.LoginWithPhoneNumber;
import com.lib.liblibgo.activity.homedashboard.HomeActivity;
import com.lib.liblibgo.adapter.LibraryApartmentAdapter;
import com.lib.liblibgo.api.AllApiClass;
import com.lib.liblibgo.api.Holders;
import com.lib.liblibgo.common.Constants;
import com.lib.liblibgo.common.UserCurrentLocation;
import com.lib.liblibgo.common.UserDatabase;
import com.lib.liblibgo.dashboard.book_collect.CollectApartmentBooksActivity;
import com.lib.liblibgo.listner.CommunityLibraryClickListener;
import com.lib.liblibgo.model.CommunityStatusModel;
import com.lib.liblibgo.model.NearMeLibraryModel;
import com.lib.liblibgo.model.subadmin.LibraryStatusModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RequestedLibraryActivity extends AppCompatActivity {
    private FloatingActionButton fabAddLibrary;
    private UserDatabase database;

    private RecyclerView rvAllLibrary;
    private ProgressBar progBar;
    private List<NearMeLibraryModel.ResModelData.NewrmeLibraryList> libList = new ArrayList<>();
    private LibraryApartmentAdapter adapter;

    SharedPreferences sharedpreferences;
    private SharedPreferences.Editor editor;
    public static final String MyPREFERENCES = "MyPrefsData";
    private TextView titleTool;
    private ImageView backTool;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requested_library);
        titleTool = findViewById(R.id.titleTool);
        backTool = findViewById(R.id.backTool);
        setTopView(getString(R.string.contact_us));
        rvAllLibrary = (RecyclerView)findViewById(R.id.rvAllLibrary);
        progBar = (ProgressBar)findViewById(R.id.progBar);

        fabAddLibrary = (FloatingActionButton)findViewById(R.id.fabAddLibrary);
        database = new UserDatabase(this);

        fabAddLibrary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!database.getUserId().equals("")){
                    Dexter.withContext(RequestedLibraryActivity.this).withPermissions(Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION)
                            .withListener(new MultiplePermissionsListener() {
                                @Override
                                public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                                    if (database.getUserId().equals("")){
                                        Intent intent = new Intent(RequestedLibraryActivity.this, LoginWithPhoneNumber.class);
                                        startActivity(intent);
                                    }/*if (database.getApartmentName().equals("") || database.getApartmentName().equals("null")){
                                        Toast.makeText(getContext(), "You are not belongs to any community !", Toast.LENGTH_SHORT).show();
                                    }*/else {
                                        //checkUserSubAdminStatus();
                                        isApartmentLibraryCreated();
                                    }
                                }

                                @Override
                                public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                                    permissionToken.continuePermissionRequest();
                                }
                            }).check();
                }else {
                    Intent intent = new Intent(RequestedLibraryActivity.this, LoginWithPhoneNumber.class);
                    startActivity(intent);
                }
            }
        });
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();

        getAllLibrary();

    }

    private void getAllLibrary() {
        progBar.setVisibility(View.VISIBLE);
        Holders holders = AllApiClass.getInstance().getApi();
        Call<NearMeLibraryModel> call = holders.getJoinedLibrary(database.getUserId());
        call.enqueue(new Callback<NearMeLibraryModel>() {
            @Override
            public void onResponse(Call<NearMeLibraryModel> call, Response<NearMeLibraryModel> response) {
                if (response.isSuccessful()){
                    if (response.body().getResponse().getCode().equals("1")){
                        progBar.setVisibility(View.GONE);
                        libList = response.body().getResponse().getLibrary_list();
                        if (libList.size() > 0){
                            //postResponseProcess(libList);

                            rvAllLibrary.setVisibility(View.VISIBLE);
                            adapter = new LibraryApartmentAdapter(libList, RequestedLibraryActivity.this);
                            LinearLayoutManager verticalManager = new LinearLayoutManager(RequestedLibraryActivity.this, LinearLayoutManager.VERTICAL, false);
                            //verticalManager.setReverseLayout(true);
                            //verticalManager.setStackFromEnd(true);
                            rvAllLibrary.setLayoutManager(verticalManager);
                            rvAllLibrary.setAdapter(adapter);

                            adapter.setOnItemClickListener(new CommunityLibraryClickListener() {
                                @Override
                                public void onItemClick(int position) {
                                    if (database.getUserId().equals("")){
                                        Intent intent = new Intent(RequestedLibraryActivity.this, LoginWithPhoneNumber.class);
                                        startActivity(intent);
                                    }else {
                                        /*if (database.getUserId().equals(libList.get(position).getUser_id())){
                                            editor.putString("community_id","0");
                                            editor.commit();
                                            Intent intent = new Intent(RequestedLibraryActivity.this, CollectApartmentBooksActivity.class);
                                            intent.putExtra("lib_id",libList.get(position).getLibrary_id());
                                            intent.putExtra("community_id","0");
                                            intent.putExtra("library_user_id",libList.get(position).getUser_id());
                                            intent.putExtra("library_name",libList.get(position).getLibrary_name());
                                            //Toast.makeText(getContext(), ""+response.body().getResponse().getCommunity_id(), Toast.LENGTH_SHORT).show();
                                            intent.putExtra("isLibrary","true");
                                            Constants.libraryType = libList.get(position).getLibrary_type();
                                            startActivity(intent);
                                        }else {
                                            checkRequest(position);
                                        }*/
                                        checkRequest(position);
                                    }
                                }
                            });

                        }else {
                            rvAllLibrary.setVisibility(View.GONE);
                            progBar.setVisibility(View.GONE);
                        }
                    }else {
                        progBar.setVisibility(View.GONE);
                        //Toast.makeText(getContext(), ""+response.body().getResponse().getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }else {
                    progBar.setVisibility(View.GONE);
                    Toast.makeText(RequestedLibraryActivity.this, "Something went wrong.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<NearMeLibraryModel> call, Throwable t) {
                progBar.setVisibility(View.GONE);
                Toast.makeText(RequestedLibraryActivity.this, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void checkRequest(int position) {
        final ProgressDialog progressBar = new ProgressDialog(RequestedLibraryActivity.this);
        progressBar.setMessage("Please wait...");
        progressBar.setCancelable(false);
        progressBar.show();
        Holders holders = AllApiClass.getInstance().getApi();
        Call<CommunityStatusModel> resCall = holders.checkCommunityRequestStatus(libList.get(position).getLibrary_id(),database.getUserId());
        resCall.enqueue(new Callback<CommunityStatusModel>() {
            @Override
            public void onResponse(Call<CommunityStatusModel> call, Response<CommunityStatusModel> response) {
                if (response.isSuccessful()){
                    if (response.body().getResponse().getCode() == 1){
                        progressBar.dismiss();
                        if (response.body().getResponse().getStatus().equals("1")){
                            editor.putString("community_id",response.body().getResponse().getCommunity_id());
                            editor.commit();
                            Intent intent = new Intent(RequestedLibraryActivity.this, CollectApartmentBooksActivity.class);
                            intent.putExtra("lib_id",libList.get(position).getLibrary_id());
                            intent.putExtra("community_id",response.body().getResponse().getCommunity_id());
                            intent.putExtra("library_user_id",libList.get(position).getUser_id());
                            intent.putExtra("library_name",libList.get(position).getLibrary_name());
                            //Toast.makeText(getContext(), ""+response.body().getResponse().getCommunity_id(), Toast.LENGTH_SHORT).show();
                            intent.putExtra("isLibrary","true");
                            Constants.libraryType = libList.get(position).getLibrary_type();
                            startActivity(intent);
                        }else if (response.body().getResponse().getStatus().equals("2")){
                            Toast.makeText(RequestedLibraryActivity.this, "Your request is rejected by the community.", Toast.LENGTH_SHORT).show();
                        }else if (response.body().getResponse().getStatus().equals("0")){
                            Toast.makeText(RequestedLibraryActivity.this, "Your request is pending yet.", Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(RequestedLibraryActivity.this, "Join first to see books of this community.", Toast.LENGTH_SHORT).show();
                        }
                        //Toast.makeText(getContext(), "", Toast.LENGTH_SHORT).show();
                    }else {
                        progressBar.dismiss();
                        Toast.makeText(RequestedLibraryActivity.this, "Join first to see books of this community.", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    progressBar.dismiss();
                    Toast.makeText(RequestedLibraryActivity.this, "Something went wrong !", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CommunityStatusModel> call, Throwable t) {
                progressBar.dismiss();
                Toast.makeText(RequestedLibraryActivity.this, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void isApartmentLibraryCreated() {
        final ProgressDialog progressBar = new ProgressDialog(RequestedLibraryActivity.this);
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
                        UserCurrentLocation location = new UserCurrentLocation(RequestedLibraryActivity.this);
                        Log.d("locationnn",""+location.latitude);
                        Log.d("locationnn",""+location.longitude);
                        Constants.latitude = String.valueOf(location.latitude);
                        Constants.longitude = String.valueOf(location.longitude);
                        Constants.SelectedLibraryProfileImage = "";
                        Intent intent = new Intent(RequestedLibraryActivity.this, CommunityLibraryRulesActivity.class);
                        Constants.USER_APARTMENT_NAME = response.body().getResponse().getApartment_name();
                        Constants.USER_APARTMENT_ID = response.body().getResponse().getApartment_id();
                        Constants.USER_FLAT_ID = response.body().getResponse().getFlat_id();
                        Constants.USER_FLAT_NAME = response.body().getResponse().getFlat_no();
                        Constants.isOwnLibrary = false;
                        intent.putExtra("count",response.body().getResponse().getMy_accepted_count());
                        startActivity(intent);
                    }else {
                        /*Intent intent = new Intent(getContext(), SubAdminDashboard.class);
                        Constants.isOwnLibrary = false;
                        Constants.libraryType = response.body().getResponse().getLibrary_type();
                        startActivity(intent);*/
                        // showPopup();
                        if (response.body().getResponse().getIs_active().equals("0")){
                            Toast.makeText(RequestedLibraryActivity.this, "Your library is created.But not approved yet.", Toast.LENGTH_LONG).show();
                        }else {
                            Toast.makeText(RequestedLibraryActivity.this, "You have already created your community library.One user can create only one community library.", Toast.LENGTH_LONG).show();
                        }

                    }
                }else {
                    progressBar.dismiss();
                    Toast.makeText(RequestedLibraryActivity.this, "Something went wrong !", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LibraryStatusModel> call, Throwable t) {
                progressBar.dismiss();
                Toast.makeText(RequestedLibraryActivity.this, "Check your internet !", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onResume() {
        if (HomeActivity.flag == 1){
            getAllLibrary();
            HomeActivity.flag = 0;
        }
        super.onResume();
    }

    private void setTopView(String string) {
        titleTool.setText("My Joined Communities");
        backTool.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        overridePendingTransition(R.anim.out_to_top,R.anim.in_from_bottom);
        super.onBackPressed();
    }
}