package com.lib.liblibgo.dashboard.library.fragments;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
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
import com.lib.liblibgo.adapter.LibraryAdapter;
import com.lib.liblibgo.api.AllApiClass;
import com.lib.liblibgo.api.Holders;
import com.lib.liblibgo.common.Constants;
import com.lib.liblibgo.common.UserCurrentLocation;
import com.lib.liblibgo.common.UserDatabase;
import com.lib.liblibgo.dashboard.apartment_admin.SubAdminDashboard;
import com.lib.liblibgo.dashboard.library.CreateLibraryActivity;
import com.lib.liblibgo.dashboard.library.LibraryActivity;
import com.lib.liblibgo.model.NearMeLibraryModel;
import com.lib.liblibgo.model.NearMeOwnLibraryModel;
import com.lib.liblibgo.model.subadmin.LibraryStatusModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AllLibraryFragment extends Fragment {
    private RecyclerView rvAllLibrary;
    private RecyclerView rvNearMeLibrary;
    private ProgressBar progBar,progNearMe;

    private List<NearMeOwnLibraryModel.ResModelData.NewrmeLibraryList> libList = new ArrayList<>();
    private List<NearMeOwnLibraryModel.ResModelData.NewrmeLibraryList> list;
    //private List<NearMeLibraryModel.ResModelData.NewrmeLibraryList> nearmeList = new ArrayList<>();
    private LibraryAdapter adapter;
    //private NearByLibraryAdapter nearMeAdapter;
    private RelativeLayout rlLocation;
    private TextView tvRange;
    private PopupWindow popupWindow;
    String apartment = "";
    String city = "";
    String area = "";
    private UserCurrentLocation location;
    private UserDatabase database;
    private View ivSearch;
    private FloatingActionButton fabAddLibrary;
    private SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefsss";
    private SharedPreferences.Editor editor;
    private SwipeRefreshLayout pullToRefresh;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_all_library, container, false);

        sharedpreferences = getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();
        pullToRefresh = (SwipeRefreshLayout)view.findViewById(R.id.pullToRefresh);
        rvAllLibrary = (RecyclerView)view.findViewById(R.id.rvAllLibrary);
        rvNearMeLibrary = (RecyclerView)view.findViewById(R.id.rvNearMeLibrary);
        progBar = (ProgressBar)view.findViewById(R.id.progBar);
        progNearMe = (ProgressBar)view.findViewById(R.id.progNearMe);

        rlLocation = (RelativeLayout)view.findViewById(R.id.rlLocation);
        tvRange = (TextView)view.findViewById(R.id.tvRange);

        fabAddLibrary = (FloatingActionButton)view.findViewById(R.id.fabAddLibrary);

        location = new UserCurrentLocation(getContext());
        database = new UserDatabase(getContext());

       // getAllLibrary();
        //getNearByLibrary("5");

        rlLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dexter.withContext(getContext()).withPermissions(Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION)
                        .withListener(new MultiplePermissionsListener() {
                            @Override
                            public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                                if (multiplePermissionsReport.areAllPermissionsGranted()){
                                    showLocationRangeMenu(view);
                                }
                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                                permissionToken.continuePermissionRequest();
                            }
                        }).check();
            }
        });


        fabAddLibrary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dexter.withContext(getContext()).withPermissions(Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION)
                        .withListener(new MultiplePermissionsListener() {
                            @Override
                            public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                                if (database.getUserId().equals("")){
                                    Intent intent = new Intent(getContext(), LoginWithPhoneNumber.class);
                                    startActivity(intent);
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
        });

        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getAllLibrary();
            }
        });

        return view;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser){
            getAllLibrary();
        }
    }

    private void isLibraryCreated() {
        final ProgressDialog progressBar = new ProgressDialog(getContext());
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
                        Constants.isOwnLibrary = true;
                        //Constants.libraryType = response.body().getResponse().getLibrary_type();
                        Constants.libraryType = "";
                        startActivity(intent);
                    }else {
                        /*Intent intent = new Intent(getContext(), SubAdminDashboard.class);
                        Constants.isOwnLibrary = true;
                        //Constants.libraryType = response.body().getResponse().getLibrary_type();
                        Constants.libraryType = "";
                        startActivity(intent);*/
                        //showPopup();
                        if (response.body().getResponse().getIs_active().equals("0")){
                            Toast.makeText(getContext(), "Your library is created.But not approved yet.", Toast.LENGTH_LONG).show();
                        }else {
                            Toast.makeText(getContext(), "You have already created your individual library.One user can create only one individual library.", Toast.LENGTH_LONG).show();
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

    private void showLocationRangeMenu(View view) {
        LayoutInflater layoutInflater = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View popupView = layoutInflater.inflate(R.layout.popup_filter_library_layout, null);

        String selectedItem = sharedpreferences.getString("selected", "");


        popupWindow = new PopupWindow(
                popupView,
                300,
                ViewGroup.LayoutParams.WRAP_CONTENT);

        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setOutsideTouchable(true);

        final TextView tvTwoKm = popupView.findViewById(R.id.tvTwoKm);
        final TextView tvThreeKm = popupView.findViewById(R.id.tvThreeKm);
        final TextView tvFiveKm = popupView.findViewById(R.id.tvFiveKm);

        final TextView tvCity = popupView.findViewById(R.id.tvCity);
        final TextView tvApartment = popupView.findViewById(R.id.tvApartment);
        final TextView tvArea = popupView.findViewById(R.id.tvArea);
        final TextView tvAll = popupView.findViewById(R.id.tvAll);
        final TextView tvMyLibrary = popupView.findViewById(R.id.tvMyLibrary);

        //Toast.makeText(getContext(), ""+selectedItem, Toast.LENGTH_SHORT).show();
        if (selectedItem.equals("1")){
            tvApartment.setBackgroundColor(Color.parseColor("#345EA8"));
            tvApartment.setTextColor(Color.parseColor("#ffffff"));
        }else if (selectedItem.equals("2")){
            tvArea.setBackgroundColor(Color.parseColor("#345EA8"));
            tvArea.setTextColor(Color.parseColor("#ffffff"));
        }else if (selectedItem.equals("3")){
            tvCity.setBackgroundColor(Color.parseColor("#345EA8"));
            tvCity.setTextColor(Color.parseColor("#ffffff"));
        }else if (selectedItem.equals("4")){
            tvTwoKm.setBackgroundColor(Color.parseColor("#345EA8"));
            tvTwoKm.setTextColor(Color.parseColor("#ffffff"));
        }else if (selectedItem.equals("5")){
            tvThreeKm.setBackgroundColor(Color.parseColor("#345EA8"));
            tvThreeKm.setTextColor(Color.parseColor("#ffffff"));
        }else if (selectedItem.equals("6")){
            tvFiveKm.setBackgroundColor(Color.parseColor("#345EA8"));
            tvFiveKm.setTextColor(Color.parseColor("#ffffff"));
        }else if (selectedItem.equals("8")){
            tvMyLibrary.setBackgroundColor(Color.parseColor("#345EA8"));
            tvMyLibrary.setTextColor(Color.parseColor("#ffffff"));
        }else {
            tvAll.setBackgroundColor(Color.parseColor("#345EA8"));
            tvAll.setTextColor(Color.parseColor("#ffffff"));
        }

        if (database.getUserId().equals("")){
            //tvApartment.setVisibility(View.GONE);
            tvMyLibrary.setVisibility(View.GONE);
        }else {
            //tvApartment.setVisibility(View.VISIBLE);
            tvMyLibrary.setVisibility(View.GONE);
        }

        tvTwoKm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                editor.putString("selected","4");
                editor.commit();

                /*tvTwoKm.setBackgroundColor(Color.parseColor("#345EA8"));
                tvTwoKm.setTextColor(Color.parseColor("#ffffff"));

                tvThreeKm.setBackgroundColor(Color.parseColor("#00000000"));
                tvThreeKm.setTextColor(Color.parseColor("#000000"));

                tvFiveKm.setBackgroundColor(Color.parseColor("#00000000"));
                tvFiveKm.setTextColor(Color.parseColor("#000000"));

                tvCity.setBackgroundColor(Color.parseColor("#00000000"));
                tvCity.setTextColor(Color.parseColor("#000000"));

                tvCity.setBackgroundColor(Color.parseColor("#00000000"));
                tvCity.setTextColor(Color.parseColor("#000000"));*/

                getNearByLibrary("2");
                tvRange.setText("2km");
                pullToRefresh.setRefreshing(false);
            }
        });

        tvThreeKm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                editor.putString("selected","5");
                editor.commit();
                getNearByLibrary("5");
                tvRange.setText("5km");
            }
        });

        tvFiveKm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                editor.putString("selected","6");
                editor.commit();
                getNearByLibrary("10");
                tvRange.setText("10km");
            }
        });

        tvCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                editor.putString("selected","3");
                editor.commit();
                city = getUserCity(location.latitude,location.longitude);
                area = "";
                apartment = "";
                getNearByLibraryByFilter(city,apartment,area);
                tvRange.setText("Within City");
            }
        });

        tvApartment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                if (database.getUserId().equals("")){
                    Intent intent = new Intent(getContext(), LoginWithPhoneNumber.class);
                    startActivity(intent);
                }else {
                    editor.putString("selected","1");
                    editor.commit();
                    city = "";
                    area = "";
                    apartment = database.getUserId();
                    getNearByLibraryByFilter(city,apartment,area);
                    tvRange.setText("Within Community");
                }
            }
        });

        tvArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                editor.putString("selected","2");
                editor.commit();
                city = "";
                area = getUserPinCode(location.latitude,location.longitude);
                apartment = "";
                getNearByLibraryByFilter(city,apartment,area);
                tvRange.setText("Within Area");
            }
        });

        tvAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                editor.putString("selected","7");
                editor.commit();
                getAllLibrary();
                tvRange.setText("All");
            }
        });

        tvMyLibrary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                editor.putString("selected","8");
                editor.commit();
                getMyLibrary();
                tvRange.setText("My Library");
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

    private void getMyLibrary() {
        pullToRefresh.setRefreshing(false);
        progBar.setVisibility(View.VISIBLE);
        Holders holders = AllApiClass.getInstance().getApi();
        Call<NearMeOwnLibraryModel> call = holders.getMyLibrary(database.getUserId());
        call.enqueue(new Callback<NearMeOwnLibraryModel>() {
            @Override
            public void onResponse(Call<NearMeOwnLibraryModel> call, Response<NearMeOwnLibraryModel> response) {
                if (response.isSuccessful()){
                    if (response.body().getResponse().getCode().equals("1")){
                        progBar.setVisibility(View.GONE);
                        libList = response.body().getResponse().getLibrary_list();
                        if (libList.size() > 0){
                            rvAllLibrary.setVisibility(View.VISIBLE);
                            adapter = new LibraryAdapter(libList, getContext());
                            LinearLayoutManager verticalManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                            // verticalManager.setReverseLayout(true);
                            // verticalManager.setStackFromEnd(true);
                            rvAllLibrary.setLayoutManager(verticalManager);
                            rvAllLibrary.setAdapter(adapter);
                        }else {
                            rvAllLibrary.setVisibility(View.GONE);
                            progNearMe.setVisibility(View.GONE);
                        }
                    }else {
                        progBar.setVisibility(View.GONE);
                        Toast.makeText(getContext(), ""+response.body().getResponse().getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }else {
                    progBar.setVisibility(View.GONE);
                    Toast.makeText(getContext(), "Something went wrong.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<NearMeOwnLibraryModel> call, Throwable t) {
                progBar.setVisibility(View.GONE);
                Toast.makeText(getContext(), ""+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public String getUserPinCode(double lat,double lon){
        Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (!addresses.isEmpty()){
            //city = addresses.get(0).getLocality();
            area = addresses.get(0).getPostalCode();
        }else {
            area = "";
        }
        return area;
    }

    public String getUserCity(double lat,double lon){
        Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(lat, lon, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (!addresses.isEmpty()){
            city = addresses.get(0).getSubAdminArea();
        }else {
            city = "";
        }
        return city;
    }

    private void getNearByLibraryByFilter(String city, String apartment, String area) {
        progNearMe.setVisibility(View.VISIBLE);
        Holders holders = AllApiClass.getInstance().getApi();
        Call<NearMeOwnLibraryModel> call = holders.getNearMeLibraryByFilter(city,area,apartment);
        call.enqueue(new Callback<NearMeOwnLibraryModel>() {
            @Override
            public void onResponse(Call<NearMeOwnLibraryModel> call, Response<NearMeOwnLibraryModel> response) {
                if (response.isSuccessful()){
                    if (response.body().getResponse().getCode().equals("1")){
                        progNearMe.setVisibility(View.GONE);
                        rvAllLibrary.setVisibility(View.VISIBLE);
                        libList = response.body().getResponse().getLibrary_list();
                        if (libList.size() > 0){
                            adapter = new LibraryAdapter(libList, getContext());
                            LinearLayoutManager verticalManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
                            // verticalManager.setReverseLayout(true);
                            // verticalManager.setStackFromEnd(true);
                            rvAllLibrary.setLayoutManager(verticalManager);
                            rvAllLibrary.setAdapter(adapter);
                            //adapter.notifyDataSetChanged();
                        }else {
                            rvAllLibrary.setVisibility(View.GONE);
                            progNearMe.setVisibility(View.GONE);
                        }
                    }else {
                        progNearMe.setVisibility(View.GONE);
                        Toast.makeText(getContext(), ""+response.body().getResponse().getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }else {
                    progNearMe.setVisibility(View.GONE);
                    Toast.makeText(getContext(), "Something went wrong.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<NearMeOwnLibraryModel> call, Throwable t) {
                progNearMe.setVisibility(View.GONE);
                Toast.makeText(getContext(), ""+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getNearByLibrary(String range) {
        progNearMe.setVisibility(View.VISIBLE);
        Holders holders = AllApiClass.getInstance().getApi();
        Call<NearMeOwnLibraryModel> call = holders.getNearMeLibrary(database.getUserId(),String.valueOf(location.latitude),String.valueOf(location.longitude),range);
        call.enqueue(new Callback<NearMeOwnLibraryModel>() {
            @Override
            public void onResponse(Call<NearMeOwnLibraryModel> call, Response<NearMeOwnLibraryModel> response) {
                if (response.isSuccessful()){

                    if (response.body().getResponse().getCode().equals("1")){
                        progNearMe.setVisibility(View.GONE);
                        libList = response.body().getResponse().getLibrary_list();
                        if (libList.size() > 0){
                            rvAllLibrary.setVisibility(View.VISIBLE);
                            adapter = new LibraryAdapter(libList, getContext());
                            LinearLayoutManager verticalManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
                            // verticalManager.setReverseLayout(true);
                            // verticalManager.setStackFromEnd(true);
                            rvAllLibrary.setLayoutManager(verticalManager);
                            rvAllLibrary.setAdapter(adapter);
                            //adapter.notifyDataSetChanged();
                        }else {
                            rvAllLibrary.setVisibility(View.GONE);
                            progNearMe.setVisibility(View.GONE);
                        }
                    }else {
                        progNearMe.setVisibility(View.GONE);
                        Toast.makeText(getContext(), ""+response.body().getResponse().getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }else {
                    progNearMe.setVisibility(View.GONE);
                    Toast.makeText(getContext(), "Something went wrong.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<NearMeOwnLibraryModel> call, Throwable t) {
                progNearMe.setVisibility(View.GONE);
                Toast.makeText(getContext(), ""+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getAllLibrary() {
        progBar.setVisibility(View.VISIBLE);
        Holders holders = AllApiClass.getInstance().getApi();
        Call<NearMeOwnLibraryModel> call = holders.getAllLibrary(database.getUserId());
        call.enqueue(new Callback<NearMeOwnLibraryModel>() {
            @Override
            public void onResponse(Call<NearMeOwnLibraryModel> call, Response<NearMeOwnLibraryModel> response) {
                if (response.isSuccessful()){
                    pullToRefresh.setRefreshing(false);
                    if (response.body().getResponse().getCode().equals("1")){
                        progBar.setVisibility(View.GONE);
                        libList = response.body().getResponse().getLibrary_list();

                        if (libList.size() > 0){
                            Log.d("test","list is grater than 0");
                            postResponseProcess(libList);
                        }else {
                            rvAllLibrary.setVisibility(View.GONE);
                            progBar.setVisibility(View.GONE);
                        }

                        /*if (libList.size() > 0){
                            rvAllLibrary.setVisibility(View.VISIBLE);
                            adapter = new LibraryAdapter(libList, getContext());
                            LinearLayoutManager verticalManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                            // verticalManager.setReverseLayout(true);
                            // verticalManager.setStackFromEnd(true);
                            rvAllLibrary.setLayoutManager(verticalManager);
                            rvAllLibrary.setAdapter(adapter);
                        }else {
                            rvAllLibrary.setVisibility(View.GONE);
                            progBar.setVisibility(View.GONE);
                        }*/

                    }else {
                        pullToRefresh.setRefreshing(false);
                        progBar.setVisibility(View.GONE);
                        Toast.makeText(getContext(), ""+response.body().getResponse().getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }else {
                    pullToRefresh.setRefreshing(false);
                    progBar.setVisibility(View.GONE);
                    Toast.makeText(getContext(), "Something went wrong.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<NearMeOwnLibraryModel> call, Throwable t) {
                pullToRefresh.setRefreshing(false);
                progBar.setVisibility(View.GONE);
                Toast.makeText(getContext(), ""+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void postResponseProcess(List<NearMeOwnLibraryModel.ResModelData.NewrmeLibraryList> libList) {
        list = new ArrayList<>();
        NearMeOwnLibraryModel.ResModelData.NewrmeLibraryList obj = null;
        //Loop the original list
        Log.d("test","data not loading");
        if (database.getUserId().equals("")) {
            list = libList;
            Log.d("test",""+list.size());
        } else {
            for (int i = 0; i < libList.size(); i++) {
                if (libList.get(i).getUser_id().equals(database.getUserId())) {
                    //if user_id same store the value at the begning of the list
                    obj = libList.get(i);
                    // list.add(0,libList.get(i));
                } else {
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
        adapter = new LibraryAdapter(list, getContext());
        LinearLayoutManager verticalManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        // verticalManager.setReverseLayout(true);
        // verticalManager.setStackFromEnd(true);
        rvAllLibrary.setLayoutManager(verticalManager);
        rvAllLibrary.setAdapter(adapter);
    }
}