package com.lib.liblibgo.dashboard.library.fragments;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.lib.liblibgo.activity.homedashboard.HomeActivity;
import com.lib.liblibgo.activity.LoginWithPhoneNumber;
import com.lib.liblibgo.adapter.LibraryApartmentAdapter;
import com.lib.liblibgo.api.AllApiClass;
import com.lib.liblibgo.api.Holders;
import com.lib.liblibgo.common.Constants;
import com.lib.liblibgo.common.UserCurrentLocation;
import com.lib.liblibgo.common.UserDatabase;
import com.lib.liblibgo.dashboard.apartment_admin.SubAdminDashboard;
import com.lib.liblibgo.dashboard.book_collect.CollectApartmentBooksActivity;
import com.lib.liblibgo.dashboard.library.CommunityLibraryRulesActivity;
import com.lib.liblibgo.dashboard.library.CreateLibraryActivity;
import com.lib.liblibgo.listner.CommunityLibraryClickListener;
import com.lib.liblibgo.model.CommunityStatusModel;
import com.lib.liblibgo.model.NearMeLibraryModel;
import com.lib.liblibgo.model.subadmin.LibraryStatusModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommunityLibraryFragment extends Fragment {

    private FloatingActionButton fabAddLibrary;
    private UserDatabase database;

    private RecyclerView rvAllLibrary;
    private ProgressBar progBar;
    private List<NearMeLibraryModel.ResModelData.NewrmeLibraryList> libList = new ArrayList<>();
    private LibraryApartmentAdapter adapter;

    SharedPreferences sharedpreferences;
    private SharedPreferences.Editor editor;
    public static final String MyPREFERENCES = "MyPrefsData";
    List<NearMeLibraryModel.ResModelData.NewrmeLibraryList> list;
    private SwipeRefreshLayout pullToRefresh;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_community_library, container, false);
        fabAddLibrary = (FloatingActionButton)view.findViewById(R.id.fabAddLibrary);
        database = new UserDatabase(getContext());
        pullToRefresh = (SwipeRefreshLayout)view.findViewById(R.id.pullToRefresh);
        rvAllLibrary = (RecyclerView)view.findViewById(R.id.rvAllLibrary);
        progBar = (ProgressBar)view.findViewById(R.id.progBar);

        fabAddLibrary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!database.getUserId().equals("")){
                    Dexter.withContext(getContext()).withPermissions(Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION)
                            .withListener(new MultiplePermissionsListener() {
                                @Override
                                public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                                    if (database.getUserId().equals("")){
                                        Intent intent = new Intent(getContext(), LoginWithPhoneNumber.class);
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
                    Intent intent = new Intent(getContext(), LoginWithPhoneNumber.class);
                    startActivity(intent);
                }
            }
        });

        sharedpreferences = getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();

        getAllLibrary();

        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getAllLibrary();
            }
        });

        return view;
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
                    progressBar.dismiss();
                    if (response.body().getResponse().getLibrary_status().equals("no")){
                        UserCurrentLocation location = new UserCurrentLocation(getContext());
                        Log.d("locationnn",""+location.latitude);
                        Log.d("locationnn",""+location.longitude);
                        Constants.latitude = String.valueOf(location.latitude);
                        Constants.longitude = String.valueOf(location.longitude);
                        Constants.SelectedLibraryProfileImage = "";
                        Intent intent = new Intent(getContext(), CommunityLibraryRulesActivity.class);
                        Constants.USER_APARTMENT_NAME = response.body().getResponse().getApartment_name();
                        Constants.USER_APARTMENT_ID = response.body().getResponse().getApartment_id();
                        Constants.USER_FLAT_ID = response.body().getResponse().getFlat_id();
                        Constants.USER_FLAT_NAME = response.body().getResponse().getFlat_no();
                        intent.putExtra("count",response.body().getResponse().getMy_accepted_count());
                        Constants.isOwnLibrary = false;
                        startActivity(intent);
                    }else {
                        /*Intent intent = new Intent(getContext(), SubAdminDashboard.class);
                        Constants.isOwnLibrary = false;
                        Constants.libraryType = response.body().getResponse().getLibrary_type();
                        startActivity(intent);*/
                       // showPopup();
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

    private void checkRequest(int position) {
        final ProgressDialog progressBar = new ProgressDialog(getContext());
        progressBar.setMessage("Please wait...");
        progressBar.setCancelable(false);
        progressBar.show();
        Holders holders = AllApiClass.getInstance().getApi();
        Call<CommunityStatusModel> resCall = holders.checkCommunityRequestStatus(list.get(position).getLibrary_id(),database.getUserId());
        resCall.enqueue(new Callback<CommunityStatusModel>() {
            @Override
            public void onResponse(Call<CommunityStatusModel> call, Response<CommunityStatusModel> response) {
                if (response.isSuccessful()){
                    if (response.body().getResponse().getCode() == 1){
                        progressBar.dismiss();
                        if (response.body().getResponse().getStatus().equals("1")){
                            editor.putString("community_id",response.body().getResponse().getCommunity_id());
                            editor.commit();
                            Intent intent = new Intent(getContext(), CollectApartmentBooksActivity.class);
                            intent.putExtra("lib_id",list.get(position).getLibrary_id());
                            intent.putExtra("community_id",response.body().getResponse().getCommunity_id());
                            intent.putExtra("library_user_id",list.get(position).getUser_id());
                            intent.putExtra("library_name",list.get(position).getLibrary_name());
                            //Toast.makeText(getContext(), ""+response.body().getResponse().getCommunity_id(), Toast.LENGTH_SHORT).show();
                            intent.putExtra("isLibrary","true");
                            Constants.libraryType = list.get(position).getLibrary_type();
                            startActivity(intent);
                        }else if (response.body().getResponse().getStatus().equals("2")){
                            Toast.makeText(getContext(), "Your request is rejected by the community.", Toast.LENGTH_SHORT).show();
                        }else if (response.body().getResponse().getStatus().equals("0")){
                            Toast.makeText(getContext(), "Your request is pending yet.", Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(getContext(), "Join first to see books of this community.", Toast.LENGTH_SHORT).show();
                        }
                        //Toast.makeText(getContext(), "", Toast.LENGTH_SHORT).show();
                    }else {
                        progressBar.dismiss();
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

    private void getAllLibrary() {
        progBar.setVisibility(View.VISIBLE);
        Holders holders = AllApiClass.getInstance().getApi();
        Call<NearMeLibraryModel> call = holders.getAllApartmentLibrary(database.getUserId());
        call.enqueue(new Callback<NearMeLibraryModel>() {
            @Override
            public void onResponse(Call<NearMeLibraryModel> call, Response<NearMeLibraryModel> response) {
                if (response.isSuccessful()){
                    pullToRefresh.setRefreshing(false);
                    if (response.body().getResponse().getCode().equals("1")){
                        progBar.setVisibility(View.GONE);
                        libList = response.body().getResponse().getLibrary_list();
                        if (libList.size() > 0){
                            postResponseProcess(libList);
                            /*rvAllLibrary.setVisibility(View.VISIBLE);
                            adapter = new LibraryApartmentAdapter(libList, getContext());
                            LinearLayoutManager verticalManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                            //verticalManager.setReverseLayout(true);
                            //verticalManager.setStackFromEnd(true);
                            rvAllLibrary.setLayoutManager(verticalManager);
                            rvAllLibrary.setAdapter(adapter);

                            adapter.setOnItemClickListener(new CommunityLibraryClickListener() {
                                @Override
                                public void onItemClick(int position) {
                                    if (database.getUserId().equals("")){
                                        Intent intent = new Intent(getContext(), LoginWithPhoneNumber.class);
                                        startActivity(intent);
                                    }else {
                                        if (database.getUserId().equals(libList.get(position).getUser_id())){
                                            editor.putString("community_id","0");
                                            editor.commit();
                                            Intent intent = new Intent(getContext(), CollectApartmentBooksActivity.class);
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
                                        }
                                        //checkRequest(position);
                                    }
                                }
                            });*/

                        }else {
                            rvAllLibrary.setVisibility(View.GONE);
                            progBar.setVisibility(View.GONE);
                        }
                    }else {
                        progBar.setVisibility(View.GONE);
                        //Toast.makeText(getContext(), ""+response.body().getResponse().getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }else {
                    pullToRefresh.setRefreshing(false);
                    progBar.setVisibility(View.GONE);
                    Toast.makeText(getContext(), "Something went wrong.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<NearMeLibraryModel> call, Throwable t) {
                pullToRefresh.setRefreshing(false);
                progBar.setVisibility(View.GONE);
                Toast.makeText(getContext(), ""+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void postResponseProcess(List<NearMeLibraryModel.ResModelData.NewrmeLibraryList> libList) {
        //First allocate create mutable array to store the final list...
        list = new ArrayList<>();
        NearMeLibraryModel.ResModelData.NewrmeLibraryList obj = null;
        //Loop the original list
        if (database.getUserId().equals("")){
            list = libList;
        }else {
            for (int i=0;i<libList.size();i++){
                if (libList.get(i).getUser_id().equals(database.getUserId())){
                    //if user_id same store the value at the begning of the list
                    obj = libList.get(i);
                    // list.add(0,libList.get(i));
                }else {
                    //if not same
                    //obj = null;
                    list.add(libList.get(i));
                }
            }

            if (obj != null){
                list.add(0,obj);
            }
        }

        rvAllLibrary.setVisibility(View.VISIBLE);
        adapter = new LibraryApartmentAdapter(list, getContext());
        LinearLayoutManager verticalManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        //verticalManager.setReverseLayout(true);
        //verticalManager.setStackFromEnd(true);
        rvAllLibrary.setLayoutManager(verticalManager);
        rvAllLibrary.setAdapter(adapter);

        adapter.setOnItemClickListener(new CommunityLibraryClickListener() {
            @Override
            public void onItemClick(int position) {
                if (database.getUserId().equals("")){
                    Intent intent = new Intent(getContext(), LoginWithPhoneNumber.class);
                    startActivity(intent);
                }else {
                    if (database.getUserId().equals(list.get(position).getUser_id())){
                        editor.putString("community_id","0");
                        editor.commit();
                        Intent intent = new Intent(getContext(), CollectApartmentBooksActivity.class);
                        intent.putExtra("lib_id",list.get(position).getLibrary_id());
                        intent.putExtra("community_id","0");
                        intent.putExtra("library_user_id",list.get(position).getUser_id());
                        intent.putExtra("library_name",list.get(position).getLibrary_name());
                        //Toast.makeText(getContext(), ""+response.body().getResponse().getCommunity_id(), Toast.LENGTH_SHORT).show();
                        intent.putExtra("isLibrary","true");
                        Constants.libraryType = list.get(position).getLibrary_type();
                        startActivity(intent);
                    }else {
                        checkRequest(position);
                    }
                    //
                }
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

}