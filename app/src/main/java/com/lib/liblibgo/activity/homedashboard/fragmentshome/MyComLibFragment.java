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
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.lib.liblibgo.adapter.LibraryApartmentAdapter;
import com.lib.liblibgo.adapter.LibraryApartmentAdapterTwo;
import com.lib.liblibgo.api.AllApiClass;
import com.lib.liblibgo.api.Holders;
import com.lib.liblibgo.common.Constants;
import com.lib.liblibgo.common.UserCurrentLocation;
import com.lib.liblibgo.common.UserDatabase;
import com.lib.liblibgo.dashboard.apartment_admin.BookHistoryActivity;
import com.lib.liblibgo.dashboard.apartment_admin.ManageCommunityUsersActivity;
import com.lib.liblibgo.dashboard.apartment_admin.ManageLibraryActivity;
import com.lib.liblibgo.dashboard.apartment_admin.MyBooksActivity;
import com.lib.liblibgo.dashboard.apartment_admin.SubAdminDashboard;
import com.lib.liblibgo.dashboard.apartment_admin.UploadBookDetails;
import com.lib.liblibgo.dashboard.book_collect.CollectApartmentBooksActivity;
import com.lib.liblibgo.dashboard.library.CommunityLibraryRulesActivity;
import com.lib.liblibgo.dashboard.library.CreateLibraryActivity;
import com.lib.liblibgo.dashboard.library.EditLibraryActivity;
import com.lib.liblibgo.dashboard.library.LibraryActivity;
import com.lib.liblibgo.dashboard.library.RequestedLibraryActivity;
import com.lib.liblibgo.dashboard.library.fragments.CommunityLibraryFragment;
import com.lib.liblibgo.listner.CommunityLibraryClickListener;
import com.lib.liblibgo.model.CommunityStatusModel;
import com.lib.liblibgo.model.NearMeLibraryModel;
import com.lib.liblibgo.model.subadmin.LibraryStatusModel;
import com.lib.liblibgo.model.subadmin.OwnLibraryProfModel;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyComLibFragment extends Fragment {

    private TextView tvLogin;
    private LinearLayout llLogin;
    private UserDatabase database;
   // private TextView tvOwnLibraryChecking;
    private LinearLayout llOwnLibraryChecking;
    private RelativeLayout rlOwnLibraryDashboard;
    private CircleImageView imageView;
    private TextView libraryNameTv;
    private TextView libraryAdrTv;
    private TextView tv_withdrawal;
    private TextView tv_lib_coins;
    private TextView tv_manage;
    private CardView cardProfile;
    private ProgressBar progBar;
    private RelativeLayout rl_prof;
    private String isOwnLirary = "0";
    private LinearLayout layoutBookManagement;
    private LinearLayout layoutMyBooks;
    private LinearLayout layoutNotification;
    private LinearLayout layoutCustomerManagement;
    private Button btnPhysical;
    private Button btnVirtual;
    private ImageView ivEdit;
    private RelativeLayout rlMyJoindLib;
    private RecyclerView rvAllLibrary;
    private ProgressBar progBarNew;
    private Context mContext;
    private List<NearMeLibraryModel.ResModelData.NewrmeLibraryList> libList = new ArrayList<>();
    private LibraryApartmentAdapterTwo adapter;
    SharedPreferences sharedpreferences;
    private SharedPreferences.Editor editor;
    public static final String MyPREFERENCES = "MyPrefsData";
    private ImageView vi_book;
    private ImageView ivEditLib;
    private TextView tv_own_library_name;
    private TextView tv_owner;
    private TextView tv_num_users;
    private TextView tvCount;
    private TextView tv_manage_members;
    private SwipeRefreshLayout pullToRefresh;
    private TextView titleTool;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_com_lib, container, false);

        titleTool = view.findViewById(R.id.titleTool);
        titleTool.setText("My Communities");

        database = new UserDatabase(getContext());
        sharedpreferences = getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();

        tvLogin = (TextView)view.findViewById(R.id.tvLogin);
        llLogin = (LinearLayout)view.findViewById(R.id.llLogin);

      //  tvOwnLibraryChecking = (TextView)view.findViewById(R.id.tvOwnLibraryChecking);
        llOwnLibraryChecking = (LinearLayout)view.findViewById(R.id.llOwnLibraryChecking);
        rlOwnLibraryDashboard = (RelativeLayout)view.findViewById(R.id.rlOwnLibraryDashboard);
        rlMyJoindLib = (RelativeLayout)view.findViewById(R.id.rlMyJoindLib);

        imageView = view.findViewById(R.id.iv_user);
        libraryNameTv = view.findViewById(R.id.tv_library_name);
        libraryAdrTv = view.findViewById(R.id.library_adr);
        tv_withdrawal = view.findViewById(R.id.tv_withdrawal);
        tv_lib_coins = view.findViewById(R.id.tv_lib_coins);
        tv_manage = view.findViewById(R.id.tv_manage);
        cardProfile = view.findViewById(R.id.cardProfile);
        progBar = view.findViewById(R.id.progBar);
        rl_prof = view.findViewById(R.id.rl_prof);
        ivEdit = view.findViewById(R.id.ivEdit);


        vi_book = view.findViewById(R.id.vi_book);
        ivEditLib = view.findViewById(R.id.ivEditLib);
        tv_own_library_name = view.findViewById(R.id.tv_own_library_name);
        tv_owner = view.findViewById(R.id.tv_owner);
        tv_num_users = view.findViewById(R.id.tv_num_users);
        tvCount = view.findViewById(R.id.tvCount);
        tv_manage_members = view.findViewById(R.id.tv_manage_members);

        btnPhysical = view.findViewById(R.id.btnLibList);
        btnVirtual = view.findViewById(R.id.btnVirtual);

        layoutBookManagement = (LinearLayout)view.findViewById(R.id.layoutBookManagement);
        layoutMyBooks = (LinearLayout)view.findViewById(R.id.layoutMyBooks);
        layoutNotification = (LinearLayout)view.findViewById(R.id.layoutNotification);
        layoutCustomerManagement = (LinearLayout)view.findViewById(R.id.layoutCustomerManagement);

        pullToRefresh = (SwipeRefreshLayout)view.findViewById(R.id.pullToRefresh);

        /*if (database.getUserId().equals("")){
            llLogin.setVisibility(View.VISIBLE);
            llOwnLibraryChecking.setVisibility(View.GONE);
            rlOwnLibraryDashboard.setVisibility(View.GONE);
        }else {
            llLogin.setVisibility(View.GONE);
            Dexter.withContext(getContext()).withPermissions(Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION)
                    .withListener(new MultiplePermissionsListener() {
                        @Override
                        public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                            isApartmentLibraryCreated();
                        }

                        @Override
                        public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                            permissionToken.continuePermissionRequest();
                        }
                    }).check();
        }*/

        rvAllLibrary = (RecyclerView)view.findViewById(R.id.rvAllLibrary);
        progBarNew = (ProgressBar)view.findViewById(R.id.progBarnew);

        if (database.getUserId().equals("")){
            llLogin.setVisibility(View.VISIBLE);
            llOwnLibraryChecking.setVisibility(View.GONE);
            rlMyJoindLib.setVisibility(View.GONE);
            rlOwnLibraryDashboard.setVisibility(View.GONE);
        }else {
            llLogin.setVisibility(View.GONE);
            isApartmentLibraryCreated();
        }

        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (database.getUserId().equals("")){
                    Intent intentHome = new Intent(getContext(), LoginWithPhoneNumber.class);
                    startActivity(intentHome);
                }/*else {
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.detach(MyComLibFragment.this).attach(MyComLibFragment.this).commit();
                }*/
            }
        });

        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (database.getUserId().equals("")){
                    llLogin.setVisibility(View.VISIBLE);
                    llOwnLibraryChecking.setVisibility(View.GONE);
                    rlMyJoindLib.setVisibility(View.GONE);
                    rlOwnLibraryDashboard.setVisibility(View.GONE);
                    pullToRefresh.setRefreshing(false);
                }else {
                    llLogin.setVisibility(View.GONE);
                    isApartmentLibraryCreated();
                }
            }
        });

        return view;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser){
            /*if (database.getUserId().equals("")){
                llLogin.setVisibility(View.VISIBLE);
                llOwnLibraryChecking.setVisibility(View.GONE);
                rlMyJoindLib.setVisibility(View.GONE);
                rlOwnLibraryDashboard.setVisibility(View.GONE);
            }else {
                llLogin.setVisibility(View.GONE);
                Dexter.withContext(getContext()).withPermissions(Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION)
                    .withListener(new MultiplePermissionsListener() {
                        @Override
                        public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                            isApartmentLibraryCreated();
                        }

                        @Override
                        public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                            permissionToken.continuePermissionRequest();
                        }
                    }).check();
            }*/
        }
    }

    private void isApartmentLibraryCreated() {
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
                    pullToRefresh.setRefreshing(false);
                    progressBar.dismiss();
                    if (response.body().getResponse().getLibrary_status().equals("no")){
                        llLogin.setVisibility(View.GONE);
                        //llOwnLibraryChecking.setVisibility(View.VISIBLE);
                        rlOwnLibraryDashboard.setVisibility(View.GONE);
                        if (response.body().getResponse().getMy_accepted_count().equals("0")){
                            llOwnLibraryChecking.setVisibility(View.VISIBLE);
                            rlMyJoindLib.setVisibility(View.GONE);
                        }else {
                            llOwnLibraryChecking.setVisibility(View.GONE);
                            rlMyJoindLib.setVisibility(View.VISIBLE);
                            showJoindList();
                        }
                        Constants.isOwnLibrary = false;
                        btnVirtual.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Dexter.withContext(getContext()).withPermissions(Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION)
                                        .withListener(new MultiplePermissionsListener() {
                                            @Override
                                            public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                                                UserCurrentLocation location = new UserCurrentLocation(getContext());
                                                Log.d("locationnn",""+location.latitude);
                                                Log.d("locationnn",""+location.longitude);
                                                Constants.latitude = String.valueOf(location.latitude);
                                                Constants.longitude = String.valueOf(location.longitude);
                                                Constants.SelectedLibraryProfileImage = "";
                                                Intent intent = new Intent(getContext(), CreateLibraryActivity.class);
                                                Constants.isOwnLibrary = false;
                                                Constants.libraryType = "virtual";
                                                Constants.USER_APARTMENT_NAME = response.body().getResponse().getApartment_name();
                                                Constants.USER_APARTMENT_ID = response.body().getResponse().getApartment_id();
                                                Constants.USER_FLAT_ID = response.body().getResponse().getFlat_id();
                                                Constants.USER_FLAT_NAME = response.body().getResponse().getFlat_no();
                                                startActivity(intent);
                                            }

                                            @Override
                                            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                                                permissionToken.continuePermissionRequest();
                                            }
                                        }).check();

                            }
                        });

                        btnPhysical.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                               /* UserCurrentLocation location = new UserCurrentLocation(getContext());
                                Log.d("locationnn",""+location.latitude);
                                Log.d("locationnn",""+location.longitude);
                                Constants.latitude = String.valueOf(location.latitude);
                                Constants.longitude = String.valueOf(location.longitude);
                                Constants.SelectedLibraryProfileImage = "";
                                Intent intent = new Intent(getContext(), CreateLibraryActivity.class);
                                Constants.isOwnLibrary = false;
                                Constants.libraryType = "physical";
                                Constants.USER_APARTMENT_NAME = response.body().getResponse().getApartment_name();
                                Constants.USER_APARTMENT_ID = response.body().getResponse().getApartment_id();
                                Constants.USER_FLAT_ID = response.body().getResponse().getFlat_id();
                                Constants.USER_FLAT_NAME = response.body().getResponse().getFlat_no();
                                startActivity(intent);*/
                                Dexter.withContext(getContext()).withPermissions(Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION)
                                        .withListener(new MultiplePermissionsListener() {
                                            @Override
                                            public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                                                /*if (response.body().getResponse().getMy_accepted_count().equals("0")){
                                                    Intent intentHome = new Intent(getContext(), LibraryActivity.class);
                                                    startActivity(intentHome);
                                                }else {
                                                    Intent intentHome = new Intent(getContext(), RequestedLibraryActivity.class);
                                                    getActivity().overridePendingTransition(R.anim.in_from_bottom,R.anim.out_to_top);
                                                    startActivity(intentHome);
                                                }*/
                                                Intent intentHome = new Intent(getContext(), LibraryActivity.class);
                                                startActivity(intentHome);

                                            }

                                            @Override
                                            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                                                permissionToken.continuePermissionRequest();
                                            }
                                        }).check();
                            }
                        });

                    }else {
                        llOwnLibraryChecking.setVisibility(View.GONE);
                        rlMyJoindLib.setVisibility(View.VISIBLE);
                        showJoindList();
                        Constants.isOwnLibrary = false;
                        Constants.libraryType = response.body().getResponse().getLibrary_type();
                        showOwnLibraryProfile();
                    }
                }else {
                    pullToRefresh.setRefreshing(false);
                    progressBar.dismiss();
                    Toast.makeText(getContext(), "Something went wrong !", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LibraryStatusModel> call, Throwable t) {
                pullToRefresh.setRefreshing(false);
                progressBar.dismiss();
                Toast.makeText(getContext(), "Check your internet !", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showJoindList() {
        progBarNew.setVisibility(View.VISIBLE);
        Holders holders = AllApiClass.getInstance().getApi();
        Call<NearMeLibraryModel> call = holders.getJoinedLibrary(database.getUserId());
        call.enqueue(new Callback<NearMeLibraryModel>() {
            @Override
            public void onResponse(Call<NearMeLibraryModel> call, Response<NearMeLibraryModel> response) {
                if (response.isSuccessful()){
                    if (response.body().getResponse().getCode().equals("1")){
                        progBarNew.setVisibility(View.GONE);
                        libList = response.body().getResponse().getLibrary_list();
                        if (libList.size() > 0){
                            //postResponseProcess(libList);
                            rvAllLibrary.setVisibility(View.VISIBLE);
                            adapter = new LibraryApartmentAdapterTwo(libList, mContext);
                            LinearLayoutManager verticalManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
                            //verticalManager.setReverseLayout(true);
                            //verticalManager.setStackFromEnd(true);
                            rvAllLibrary.setLayoutManager(verticalManager);
                            rvAllLibrary.setAdapter(adapter);

                            adapter.setOnItemClickListener(new CommunityLibraryClickListener() {
                                @Override
                                public void onItemClick(int position) {
                                    if (database.getUserId().equals("")){
                                        Intent intent = new Intent(mContext, LoginWithPhoneNumber.class);
                                        startActivity(intent);
                                    }else {
                                        checkRequest(position);
                                    }
                                }
                            });

                        }else {
                            rvAllLibrary.setVisibility(View.GONE);
                            progBarNew.setVisibility(View.GONE);
                        }
                    }else {
                        progBarNew.setVisibility(View.GONE);
                        //Toast.makeText(getContext(), ""+response.body().getResponse().getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }else {
                    progBarNew.setVisibility(View.GONE);
                    Toast.makeText(mContext, "Something went wrong.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<NearMeLibraryModel> call, Throwable t) {
                progBarNew.setVisibility(View.GONE);
                Toast.makeText(mContext, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void checkRequest(int position) {
        final ProgressDialog progressBar = new ProgressDialog(mContext);
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
                            Intent intent = new Intent(mContext, CollectApartmentBooksActivity.class);
                            intent.putExtra("lib_id",libList.get(position).getLibrary_id());
                            intent.putExtra("community_id",response.body().getResponse().getCommunity_id());
                            intent.putExtra("library_user_id",libList.get(position).getUser_id());
                            intent.putExtra("library_name",libList.get(position).getLibrary_name());
                            //Toast.makeText(getContext(), ""+response.body().getResponse().getCommunity_id(), Toast.LENGTH_SHORT).show();
                            intent.putExtra("isLibrary","true");
                            Constants.libraryType = libList.get(position).getLibrary_type();
                            startActivity(intent);
                        }else if (response.body().getResponse().getStatus().equals("2")){
                            Toast.makeText(mContext, "Your request is rejected by the community.", Toast.LENGTH_SHORT).show();
                        }else if (response.body().getResponse().getStatus().equals("0")){
                            Toast.makeText(mContext, "Your request is pending yet.", Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(mContext, "Join first to see books of this community.", Toast.LENGTH_SHORT).show();
                        }
                        //Toast.makeText(getContext(), "", Toast.LENGTH_SHORT).show();
                    }else {
                        progressBar.dismiss();
                        Toast.makeText(mContext, "Join first to see books of this community.", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    progressBar.dismiss();
                    Toast.makeText(mContext, "Something went wrong !", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CommunityStatusModel> call, Throwable t) {
                progressBar.dismiss();
                Toast.makeText(mContext, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onAttach(@NonNull Context context) {
        mContext = context;
        super.onAttach(context);
    }

    private void showOwnLibraryProfile() {
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
                        tv_own_library_name.setText(response.body().getResponse().getLibrary_name());
                        libraryAdrTv.setText(response.body().getResponse().getLibrary_address());
                        tv_owner.setText("Owner, "+response.body().getResponse().getName());
                        tvCount.setText(response.body().getResponse().getMy_pending_user());
                        tv_num_users.setText(response.body().getResponse().getMy_accepted_user() + " people joined.");

                        Constants.USER_APARTMENT_ID = response.body().getResponse().getApartment_id();
                        Constants.SelectedLibraryProfileImage = response.body().getResponse().getLibrary_image();

                        tv_lib_coins.setText(response.body().getResponse().getLibcoins());

                        tv_withdrawal.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (Integer.parseInt(response.body().getResponse().getLibcoins()) >= 2000){
                                    Toast.makeText(getContext(), "Amount withdrawal successfully !", Toast.LENGTH_SHORT).show();
                                }else {
                                    Toast.makeText(getContext(), "You need 2000 LibCoins to withdrawal.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                        tv_manage_members.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                SharedPreferences sharedpreferences = mContext.getSharedPreferences(CommunityLibraryFragment.MyPREFERENCES, Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedpreferences.edit();
                                editor.putString("community_id","0");
                                editor.commit();
                                Intent intent = new Intent(mContext, ManageCommunityUsersActivity.class);
                                Constants.isOwnLibrary = false;
                                Constants.libraryType = Constants.libraryType;
                                mContext.startActivity(intent);
                            }
                        });

                        rlOwnLibraryDashboard.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                SharedPreferences sharedpreferences = getActivity().getSharedPreferences(CommunityLibraryFragment.MyPREFERENCES, Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedpreferences.edit();
                                editor.putString("community_id","0");
                                editor.commit();
                                Intent intent = new Intent(mContext, CollectApartmentBooksActivity.class);
                                intent.putExtra("lib_id",response.body().getResponse().getLibrary_id());
                                intent.putExtra("community_id","0");
                                intent.putExtra("library_user_id",database.getUserId());
                                intent.putExtra("library_name",response.body().getResponse().getLibrary_name());
                                intent.putExtra("isLibrary","true");
                                Constants.libraryType = "virtual";
                                Constants.isOwnLibrary = false;
                                startActivity(intent);
                            }
                        });

                        ivEditLib.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Dexter.withContext(getContext()).withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                        .withListener(new MultiplePermissionsListener() {
                                            @Override
                                            public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                                                Intent intent = new Intent(getContext(), EditLibraryActivity.class);
                                                Constants.isOwnLibrary = false;
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
                                Glide.with(getContext()).load(R.drawable.no_img).into(imageView);
                                Glide.with(getContext()).load(R.drawable.no_img).into(vi_book);
                            }else {
                                Glide.with(getContext()).load(Constants.SelectedLibraryProfileImage).into(imageView);
                                Glide.with(getContext()).load(Constants.SelectedLibraryProfileImage).into(vi_book);
                            }
                        }else {
                            Glide.with(getContext()).load(response.body().getResponse().getLibrary_image()).into(imageView);
                            Glide.with(getContext()).load(response.body().getResponse().getLibrary_image()).into(vi_book);
                        }

                        layoutBookManagement.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                SharedPreferences sharedpreferences = getActivity().getSharedPreferences(CommunityLibraryFragment.MyPREFERENCES, Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedpreferences.edit();
                                editor.putString("community_id","0");
                                editor.commit();
                                Intent intent = new Intent(getContext(), UploadBookDetails.class);
                                Constants.isOwnLibrary = false;
                                intent.putExtra("name","");
                                intent.putExtra("author","");
                                intent.putExtra("isbn","");
                                intent.putExtra("publish_date","");
                                intent.putExtra("description","");
                                intent.putExtra("imgUrl","");
                                intent.putExtra("rental_price","");
                                intent.putExtra("rental_duration","365");
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
                                if (Constants.libraryType.equals("physical")){
                                    intent.putExtra("giveaway_status","yes");
                                }else {
                                    intent.putExtra("giveaway_status","no");
                                }
                                startActivity(intent);
                            }
                        });

                        layoutMyBooks.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                SharedPreferences sharedpreferences = getActivity().getSharedPreferences(CommunityLibraryFragment.MyPREFERENCES, Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedpreferences.edit();
                                editor.putString("community_id","0");
                                Constants.isOwnLibrary = false;
                                Intent intent = new Intent(getContext(), MyBooksActivity.class);
                                startActivity(intent);
                            }
                        });

                        layoutNotification.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Constants.isOwnLibrary = false;
                                Intent intentNotifcation = new Intent(getContext(), BookHistoryActivity.class);
                                startActivity(intentNotifcation);
                            }
                        });

                        layoutCustomerManagement.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                SharedPreferences sharedpreferences = getActivity().getSharedPreferences(CommunityLibraryFragment.MyPREFERENCES, Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedpreferences.edit();
                                editor.putString("community_id","0");
                                Constants.isOwnLibrary = false;
                                Intent intentCommunityUser = new Intent(getContext(), ManageCommunityUsersActivity.class);
                                startActivity(intentCommunityUser);
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
        database = new UserDatabase(getContext());
        if (EditLibraryActivity.editLibFlag == 1){
            showOwnLibraryProfile();
            EditLibraryActivity.editLibFlag = 0;
        }
        if (HomeFragment.flag == 1){
            if (database.getUserId().equals("")){
                llLogin.setVisibility(View.VISIBLE);
                rlOwnLibraryDashboard.setVisibility(View.GONE);
                llOwnLibraryChecking.setVisibility(View.GONE);
                rlMyJoindLib.setVisibility(View.GONE);
            }else {
                llLogin.setVisibility(View.GONE);
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.detach(MyComLibFragment.this).attach(MyComLibFragment.this).commit();
                Dexter.withContext(getContext()).withPermissions(Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION)
                        .withListener(new MultiplePermissionsListener() {
                            @Override
                            public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                                isApartmentLibraryCreated();
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
}