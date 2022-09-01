package com.lib.liblibgo.activity.homedashboard.fragmentshome;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.lib.liblibgo.activity.LoginWithPhoneNumber;
import com.lib.liblibgo.api.AllApiClass;
import com.lib.liblibgo.api.Holders;
import com.lib.liblibgo.common.Constants;
import com.lib.liblibgo.common.UserCurrentLocation;
import com.lib.liblibgo.common.UserDatabase;
import com.lib.liblibgo.dashboard.apartment_admin.BookHistoryActivity;
import com.lib.liblibgo.dashboard.apartment_admin.ManageLibraryActivity;
import com.lib.liblibgo.dashboard.apartment_admin.MyBooksActivity;
import com.lib.liblibgo.dashboard.apartment_admin.SubAdminDashboard;
import com.lib.liblibgo.dashboard.apartment_admin.UploadBookDetails;
import com.lib.liblibgo.dashboard.book_collect.CollectApartmentBooksActivity;
import com.lib.liblibgo.dashboard.library.CreateLibraryActivity;
import com.lib.liblibgo.dashboard.library.EditLibraryActivity;
import com.lib.liblibgo.dashboard.library.fragments.CommunityLibraryFragment;
import com.lib.liblibgo.model.subadmin.LibraryStatusModel;
import com.lib.liblibgo.model.subadmin.OwnLibraryProfModel;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyIndLibFragment extends Fragment {
    private TextView tvLogin;
    private LinearLayout llLogin;
    private UserDatabase database;
    private TextView tvOwnLibraryChecking;
    private LinearLayout llOwnLibraryChecking;
    private RelativeLayout rlOwnLibraryDashboard;
    private CircleImageView imageView;
    private TextView libraryNameTv;
    private TextView libraryAdrTv;
    private TextView tv_withdrawal;
    private TextView tv_lib_coins;
    private CardView cardProfile;
    private ProgressBar progBar;
    private RelativeLayout rl_prof;
    private String isOwnLirary = "1";
    private LinearLayout layoutBookManagement;
    private LinearLayout layoutMyBooks;
    private LinearLayout layoutNotification;
    private Context mContext;
    private ImageView ivEdit;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_ind_lib, container, false);

        database = new UserDatabase(mContext);

        tvLogin = (TextView)view.findViewById(R.id.tvLogin);
        llLogin = (LinearLayout)view.findViewById(R.id.llLogin);

        tvOwnLibraryChecking = (TextView)view.findViewById(R.id.tvOwnLibraryChecking);
        llOwnLibraryChecking = (LinearLayout)view.findViewById(R.id.llOwnLibraryChecking);

        rlOwnLibraryDashboard = (RelativeLayout)view.findViewById(R.id.rlOwnLibraryDashboard);

        imageView = view.findViewById(R.id.iv_user);
        libraryNameTv = view.findViewById(R.id.tv_library_name);
        libraryAdrTv = view.findViewById(R.id.library_adr);
        tv_withdrawal = view.findViewById(R.id.tv_withdrawal);
        tv_lib_coins = view.findViewById(R.id.tv_lib_coins);
        cardProfile = view.findViewById(R.id.cardProfile);
        progBar = view.findViewById(R.id.progBar);
        rl_prof = view.findViewById(R.id.rl_prof);
        ivEdit = view.findViewById(R.id.ivEdit);

        layoutBookManagement = (LinearLayout)view.findViewById(R.id.layoutBookManagement);
        layoutMyBooks = (LinearLayout)view.findViewById(R.id.layoutMyBooks);
        layoutNotification = (LinearLayout)view.findViewById(R.id.layoutNotification);

        if (database.getUserId().equals("")){
            llLogin.setVisibility(View.VISIBLE);
            rlOwnLibraryDashboard.setVisibility(View.GONE);
            llOwnLibraryChecking.setVisibility(View.GONE);
        }else {
            llLogin.setVisibility(View.GONE);
            Dexter.withContext(mContext).withPermissions(Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION)
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

        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentHome = new Intent(mContext, LoginWithPhoneNumber.class);
                startActivity(intentHome);
            }
        });

        return view;
    }

    private void isLibraryCreated() {
        final ProgressDialog progressBar = new ProgressDialog(mContext);
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
                        llLogin.setVisibility(View.GONE);
                        llOwnLibraryChecking.setVisibility(View.VISIBLE);
                        rlOwnLibraryDashboard.setVisibility(View.GONE);
                        Constants.isOwnLibrary = true;
                        tvOwnLibraryChecking.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                UserCurrentLocation location = new UserCurrentLocation(mContext);
                                //Constants.libraryType = response.body().getResponse().getLibrary_type();
                                Constants.libraryType = "";
                                Log.d("locationnn",""+location.latitude);
                                Log.d("locationnn",""+location.longitude);
                                Constants.latitude = String.valueOf(location.latitude);
                                Constants.longitude = String.valueOf(location.longitude);
                                Constants.SelectedLibraryProfileImage = "";
                                Intent intent = new Intent(mContext, CreateLibraryActivity.class);
                                Constants.USER_APARTMENT_NAME = response.body().getResponse().getApartment_name();
                                Constants.USER_APARTMENT_ID = response.body().getResponse().getApartment_id();
                                Constants.USER_FLAT_ID = response.body().getResponse().getFlat_id();
                                Constants.USER_FLAT_NAME = response.body().getResponse().getFlat_no();
                                Constants.isOwnLibrary = true;
                                startActivity(intent);
                            }
                        });

                    }else {
                        //rlOwnLibraryDashboard.setVisibility(View.GONE);
                        llOwnLibraryChecking.setVisibility(View.GONE);
                        Constants.isOwnLibrary = true;
                        //Constants.libraryType = response.body().getResponse().getLibrary_type();
                        showOwnLibraryProfile(response.body().getResponse().getLibrary_type());
                        /*Intent intent = new Intent(mContext, SubAdminDashboard.class);
                        Constants.isOwnLibrary = true;
                        startActivity(intent);*/
                    }
                }else {
                    progressBar.dismiss();
                    Toast.makeText(mContext, "Something went wrong !", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LibraryStatusModel> call, Throwable t) {
                progressBar.dismiss();
                Toast.makeText(mContext, "Check your internet !", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showOwnLibraryProfile(String libStatus) {
        rlOwnLibraryDashboard.setVisibility(View.VISIBLE);
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

                        Constants.libraryType = "";

                        Constants.USER_APARTMENT_ID = response.body().getResponse().getApartment_id();
                        Constants.SelectedLibraryProfileImage = response.body().getResponse().getLibrary_image();

                        tv_lib_coins.setText(response.body().getResponse().getLibcoins());

                        tv_withdrawal.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (Integer.parseInt(response.body().getResponse().getLibcoins()) >= 2000){
                                    Toast.makeText(mContext, "Amount withdrawal successfully !", Toast.LENGTH_SHORT).show();
                                }else {
                                    Toast.makeText(mContext, "You need 2000 LibCoins to withdrawal.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                        ivEdit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Dexter.withContext(mContext).withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                        .withListener(new MultiplePermissionsListener() {
                                            @Override
                                            public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                                                Intent intent = new Intent(mContext, EditLibraryActivity.class);
                                                Constants.isOwnLibrary = true;
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
                                Glide.with(mContext).load(R.drawable.no_img).into(imageView);
                            }else {
                                Glide.with(mContext).load(Constants.SelectedLibraryProfileImage).into(imageView);
                            }
                        }else {
                            Glide.with(mContext).load(response.body().getResponse().getLibrary_image()).into(imageView);
                        }

                        layoutBookManagement.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                SharedPreferences sharedpreferences = getActivity().getSharedPreferences(CommunityLibraryFragment.MyPREFERENCES, Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedpreferences.edit();
                                editor.putString("community_id","0");
                                editor.commit();
                                Constants.libraryType = "";
                                Constants.isOwnLibrary = true;
                                Intent intentUploadBooks = new Intent(mContext, UploadBookDetails.class);
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
                            }
                        });

                        layoutMyBooks.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Constants.isOwnLibrary = true;
                                Intent intent = new Intent(mContext, MyBooksActivity.class);
                                startActivity(intent);
                            }
                        });

                        layoutNotification.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Constants.isOwnLibrary = true;
                                Intent intentNotifcation = new Intent(mContext, BookHistoryActivity.class);
                                startActivity(intentNotifcation);
                            }
                        });

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

    @Override
    public void onResume() {
        database = new UserDatabase(mContext);
        if (EditLibraryActivity.editLibFlag == 1){
            showOwnLibraryProfile("");
            //EditLibraryActivity.editLibFlag = 0;
        }
        if (HomeFragment.flag == 1){
            if (database.getUserId().equals("")){
                llLogin.setVisibility(View.VISIBLE);
                rlOwnLibraryDashboard.setVisibility(View.GONE);
                llOwnLibraryChecking.setVisibility(View.GONE);
            }else {
                llLogin.setVisibility(View.GONE);
                Dexter.withContext(mContext).withPermissions(Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION)
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
                HomeFragment.flag = 0;
            }
        }
        super.onResume();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        mContext = context;
        super.onAttach(context);
    }
}