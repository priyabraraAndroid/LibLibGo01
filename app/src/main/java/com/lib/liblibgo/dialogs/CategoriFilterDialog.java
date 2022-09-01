package com.lib.liblibgo.dialogs;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.lib.liblibgo.R;
import com.lib.liblibgo.adapter.BookCatAdapter;
import com.lib.liblibgo.adapter.NearMeFilterAdapter;
import com.lib.liblibgo.api.AllApiClass;
import com.lib.liblibgo.api.Holders;
import com.lib.liblibgo.common.Constants;
import com.lib.liblibgo.common.UserCurrentLocation;
import com.lib.liblibgo.common.UserDatabase;
import com.lib.liblibgo.dashboard.book_details.BookCatActivity;
import com.lib.liblibgo.dashboard.book_details.fragments.OwnBooksFrag;
import com.lib.liblibgo.listner.CommunityFilterInterfaceClass;
import com.lib.liblibgo.listner.FilterInterfaceClass;
import com.lib.liblibgo.listner.OnItemClickListener;
import com.lib.liblibgo.listner.OnItemClickListenerThree;
import com.lib.liblibgo.listner.OnItemClickListenerTow;
import com.lib.liblibgo.model.CategoryListData;
import com.lib.liblibgo.model.CategoryModel;
import com.lib.liblibgo.model.NearMeFilterModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoriFilterDialog extends BottomSheetDialogFragment {

    private RecyclerView rvCategory;
    private BookCatAdapter adapter;
    private ImageView ivClose;
    private UserDatabase dataBase;
    private Button btnApply;
    private RecyclerView rvLocation;
    //private List<NearMeFilterModel> myNearbyList = new ArrayList<>();
    private NearMeFilterAdapter adapternearby;
    private TextView tvForRent,tvForSale,tvForBoth,tvForGiveaway;
   // private TextView tvApartment,tvArea,tvCity,tv2km,tv5km,tv10km;
    //private UserCurrentLocation location;
    //private String selectedFilterValue = "";
    //private String giveAway = "";
    private FilterInterfaceClass callback;
    private CommunityFilterInterfaceClass callback2;
    private TextView tvNearMe;
    private LinearLayout ll1,ll2;

    public CategoriFilterDialog newInstance(FilterInterfaceClass callback,CommunityFilterInterfaceClass callback2) {
        this.callback=callback;
        this.callback2=callback2;
        return new CategoriFilterDialog();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.cat_filter_dialog, container, false);

        dataBase = new UserDatabase(getContext());
        //location = new UserCurrentLocation(getContext());

        rvCategory = (RecyclerView) view.findViewById(R.id.rvCategory);
        ivClose = (ImageView) view.findViewById(R.id.ivClose);
        btnApply = (Button) view.findViewById(R.id.btnApply);
        rvLocation = (RecyclerView)view.findViewById(R.id.rvLocation);

        tvForRent = (TextView)view.findViewById(R.id.tvForRent);
        tvForSale = (TextView)view.findViewById(R.id.tvForSale);
        tvForBoth = (TextView)view.findViewById(R.id.tvForBoth);
        tvForGiveaway = (TextView)view.findViewById(R.id.tvForGiveaway);

        tvNearMe = (TextView)view.findViewById(R.id.tvNearMe);
        ll1 = (LinearLayout)view.findViewById(R.id.ll1);
        ll2 = (LinearLayout)view.findViewById(R.id.ll2);

        /*tvApartment = (TextView)view.findViewById(R.id.tvApartment);
        tvArea = (TextView)view.findViewById(R.id.tvArea);
        tvCity = (TextView)view.findViewById(R.id.tvCity);
        tv2km = (TextView)view.findViewById(R.id.tv2km);
        tv5km = (TextView)view.findViewById(R.id.tv5km);
        tv10km = (TextView)view.findViewById(R.id.tv10km);*/

        getCategoryList();
        //nearbyFilter();
        //typeFilter();

        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        btnApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //sendFilterData(Constants.selectedCategories,Constants.selectedNearByCity,Constants.selectedNearByArea,Constants.selectedNearByApartment,Constants.selectedNearByDistance,Constants.selectedFilterValue,Constants.giveAway);
                dismiss();
            }
        });

        /*if (Constants.isOwnBooks.equals("1")){
            ll1.setVisibility(View.VISIBLE);
            ll2.setVisibility(View.VISIBLE);
            tvNearMe.setVisibility(View.VISIBLE);
        }else {
            ll1.setVisibility(View.GONE);
            ll2.setVisibility(View.GONE);
            tvNearMe.setVisibility(View.GONE);
        }*/

        return view;
    }

    private void typeFilter() {
        if (Constants.selectedFilterValue.equals("For Rent")){
            tvForRent.setBackgroundResource(R.drawable.btn_bg);
            tvForSale.setBackgroundResource(R.drawable.btn_bg_grey);
            tvForBoth.setBackgroundResource(R.drawable.btn_bg_grey);
            tvForGiveaway.setBackgroundResource(R.drawable.btn_bg_grey);
        }else if (Constants.selectedFilterValue.equals("For Sale")){
            tvForRent.setBackgroundResource(R.drawable.btn_bg_grey);
            tvForSale.setBackgroundResource(R.drawable.btn_bg);
            tvForBoth.setBackgroundResource(R.drawable.btn_bg_grey);
            tvForGiveaway.setBackgroundResource(R.drawable.btn_bg_grey);
        }else if (Constants.selectedFilterValue.equals("Both")){
            tvForRent.setBackgroundResource(R.drawable.btn_bg_grey);
            tvForSale.setBackgroundResource(R.drawable.btn_bg_grey);
            tvForBoth.setBackgroundResource(R.drawable.btn_bg);
            tvForGiveaway.setBackgroundResource(R.drawable.btn_bg_grey);
        }else if (Constants.giveAway.equals("yes")){
            tvForRent.setBackgroundResource(R.drawable.btn_bg_grey);
            tvForSale.setBackgroundResource(R.drawable.btn_bg_grey);
            tvForBoth.setBackgroundResource(R.drawable.btn_bg_grey);
            tvForGiveaway.setBackgroundResource(R.drawable.btn_bg);
        }

        tvForRent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Constants.selectedFilterValue = "For Rent";
                Constants.giveAway = "";
                tvForRent.setBackgroundResource(R.drawable.btn_bg);
                tvForSale.setBackgroundResource(R.drawable.btn_bg_grey);
                tvForBoth.setBackgroundResource(R.drawable.btn_bg_grey);
                tvForGiveaway.setBackgroundResource(R.drawable.btn_bg_grey);
            }
        });

        tvForSale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Constants.selectedFilterValue = "For Sale";
                Constants.giveAway = "";
                tvForRent.setBackgroundResource(R.drawable.btn_bg_grey);
                tvForSale.setBackgroundResource(R.drawable.btn_bg);
                tvForBoth.setBackgroundResource(R.drawable.btn_bg_grey);
                tvForGiveaway.setBackgroundResource(R.drawable.btn_bg_grey);
            }
        });

        tvForBoth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Constants.selectedFilterValue = "Both";
                Constants.giveAway = "";
                tvForRent.setBackgroundResource(R.drawable.btn_bg_grey);
                tvForSale.setBackgroundResource(R.drawable.btn_bg_grey);
                tvForBoth.setBackgroundResource(R.drawable.btn_bg);
                tvForGiveaway.setBackgroundResource(R.drawable.btn_bg_grey);
            }
        });

        tvForGiveaway.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Constants.selectedFilterValue = "";
                Constants.giveAway = "yes";
                tvForRent.setBackgroundResource(R.drawable.btn_bg_grey);
                tvForSale.setBackgroundResource(R.drawable.btn_bg_grey);
                tvForBoth.setBackgroundResource(R.drawable.btn_bg_grey);
                tvForGiveaway.setBackgroundResource(R.drawable.btn_bg);
            }
        });
    }

   /* private void nearbyFilter() {
        if (!Constants.selectedNearByApartment.equals("")){
            tvApartment.setBackgroundResource(R.drawable.btn_bg);
            tvArea.setBackgroundResource(R.drawable.btn_bg_grey);
            tvCity.setBackgroundResource(R.drawable.btn_bg_grey);
            tv2km.setBackgroundResource(R.drawable.btn_bg_grey);
            tv5km.setBackgroundResource(R.drawable.btn_bg_grey);
            tv10km.setBackgroundResource(R.drawable.btn_bg_grey);
        }else if (!Constants.selectedNearByArea.equals("")){
            tvApartment.setBackgroundResource(R.drawable.btn_bg_grey);
            tvArea.setBackgroundResource(R.drawable.btn_bg);
            tvCity.setBackgroundResource(R.drawable.btn_bg_grey);
            tv2km.setBackgroundResource(R.drawable.btn_bg_grey);
            tv5km.setBackgroundResource(R.drawable.btn_bg_grey);
            tv10km.setBackgroundResource(R.drawable.btn_bg_grey);
        }else if (!Constants.selectedNearByCity.equals("")){
            tvApartment.setBackgroundResource(R.drawable.btn_bg_grey);
            tvArea.setBackgroundResource(R.drawable.btn_bg_grey);
            tvCity.setBackgroundResource(R.drawable.btn_bg);
            tv2km.setBackgroundResource(R.drawable.btn_bg_grey);
            tv5km.setBackgroundResource(R.drawable.btn_bg_grey);
            tv10km.setBackgroundResource(R.drawable.btn_bg_grey);
        }else if (Constants.selectedNearByDistance.equals("2")){
            tvApartment.setBackgroundResource(R.drawable.btn_bg_grey);
            tvArea.setBackgroundResource(R.drawable.btn_bg_grey);
            tvCity.setBackgroundResource(R.drawable.btn_bg_grey);
            tv2km.setBackgroundResource(R.drawable.btn_bg);
            tv5km.setBackgroundResource(R.drawable.btn_bg_grey);
            tv10km.setBackgroundResource(R.drawable.btn_bg_grey);
        }else if (Constants.selectedNearByDistance.equals("5")){
            tvApartment.setBackgroundResource(R.drawable.btn_bg_grey);
            tvArea.setBackgroundResource(R.drawable.btn_bg_grey);
            tvCity.setBackgroundResource(R.drawable.btn_bg_grey);
            tv2km.setBackgroundResource(R.drawable.btn_bg_grey);
            tv5km.setBackgroundResource(R.drawable.btn_bg);
            tv10km.setBackgroundResource(R.drawable.btn_bg_grey);
        }else if (Constants.selectedNearByDistance.equals("10")){
            tvApartment.setBackgroundResource(R.drawable.btn_bg_grey);
            tvArea.setBackgroundResource(R.drawable.btn_bg_grey);
            tvCity.setBackgroundResource(R.drawable.btn_bg_grey);
            tv2km.setBackgroundResource(R.drawable.btn_bg_grey);
            tv5km.setBackgroundResource(R.drawable.btn_bg_grey);
            tv10km.setBackgroundResource(R.drawable.btn_bg);
        }

        tvApartment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dataBase.getUserId().equals("") || dataBase.getApartmentName().equals("")){
                    Toast.makeText(getContext(), "You are not any community user.", Toast.LENGTH_SHORT).show();
                    Constants.selectedNearByCity = "";
                    Constants.selectedNearByArea = "";
                    Constants.selectedNearByApartment = "";
                    Constants.selectedNearByDistance = "";
                }else {
                    Constants.selectedNearByCity = "";
                    Constants.selectedNearByArea = "";
                    Constants.selectedNearByApartment = dataBase.getUserId();
                    Constants.selectedNearByDistance = "";
                    tvApartment.setBackgroundResource(R.drawable.btn_bg);
                    tvArea.setBackgroundResource(R.drawable.btn_bg_grey);
                    tvCity.setBackgroundResource(R.drawable.btn_bg_grey);
                    tv2km.setBackgroundResource(R.drawable.btn_bg_grey);
                    tv5km.setBackgroundResource(R.drawable.btn_bg_grey);
                    tv10km.setBackgroundResource(R.drawable.btn_bg_grey);
                }
            }
        });

        tvArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Constants.selectedNearByCity = "";
                Constants.selectedNearByArea = getUserPinCode(location.latitude,location.longitude);
                Constants.selectedNearByApartment = "";
                Constants.selectedNearByDistance = "";
                tvApartment.setBackgroundResource(R.drawable.btn_bg_grey);
                tvArea.setBackgroundResource(R.drawable.btn_bg);
                tvCity.setBackgroundResource(R.drawable.btn_bg_grey);
                tv2km.setBackgroundResource(R.drawable.btn_bg_grey);
                tv5km.setBackgroundResource(R.drawable.btn_bg_grey);
                tv10km.setBackgroundResource(R.drawable.btn_bg_grey);
            }
        });

        tvCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Constants.selectedNearByCity = getUserCity(location.latitude,location.longitude);
                Constants.selectedNearByArea = "";
                Constants.selectedNearByApartment = "";
                Constants.selectedNearByDistance = "";
                tvApartment.setBackgroundResource(R.drawable.btn_bg_grey);
                tvArea.setBackgroundResource(R.drawable.btn_bg_grey);
                tvCity.setBackgroundResource(R.drawable.btn_bg);
                tv2km.setBackgroundResource(R.drawable.btn_bg_grey);
                tv5km.setBackgroundResource(R.drawable.btn_bg_grey);
                tv10km.setBackgroundResource(R.drawable.btn_bg_grey);
            }
        });

        tv2km.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Constants.selectedNearByCity = "";
                Constants.selectedNearByArea = "";
                Constants.selectedNearByApartment = "";
                Constants.selectedNearByDistance = "2";
                tvApartment.setBackgroundResource(R.drawable.btn_bg_grey);
                tvArea.setBackgroundResource(R.drawable.btn_bg_grey);
                tvCity.setBackgroundResource(R.drawable.btn_bg_grey);
                tv2km.setBackgroundResource(R.drawable.btn_bg);
                tv5km.setBackgroundResource(R.drawable.btn_bg_grey);
                tv10km.setBackgroundResource(R.drawable.btn_bg_grey);
            }
        });

        tv5km.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Constants.selectedNearByCity = "";
                Constants.selectedNearByArea = "";
                Constants.selectedNearByApartment = "";
                Constants.selectedNearByDistance = "5";
                tvApartment.setBackgroundResource(R.drawable.btn_bg_grey);
                tvArea.setBackgroundResource(R.drawable.btn_bg_grey);
                tvCity.setBackgroundResource(R.drawable.btn_bg_grey);
                tv2km.setBackgroundResource(R.drawable.btn_bg_grey);
                tv5km.setBackgroundResource(R.drawable.btn_bg);
                tv10km.setBackgroundResource(R.drawable.btn_bg_grey);
            }
        });

        tv10km.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Constants.selectedNearByCity = "";
                Constants.selectedNearByArea = "";
                Constants.selectedNearByApartment = "";
                Constants.selectedNearByDistance = "10";
                tvApartment.setBackgroundResource(R.drawable.btn_bg_grey);
                tvArea.setBackgroundResource(R.drawable.btn_bg_grey);
                tvCity.setBackgroundResource(R.drawable.btn_bg_grey);
                tv2km.setBackgroundResource(R.drawable.btn_bg_grey);
                tv5km.setBackgroundResource(R.drawable.btn_bg_grey);
                tv10km.setBackgroundResource(R.drawable.btn_bg);
            }
        });
    }*/

    @Override
    public int getTheme() {
        return R.style.BottomSheetDialogTheme;
    }


    private void getNearByData() {
        adapternearby = new NearMeFilterAdapter(Constants.myNearbyList, getContext());
        GridLayoutManager manager = new GridLayoutManager(getContext(),3);
        rvLocation.setLayoutManager(manager);
        rvLocation.setAdapter(adapternearby);

        adapternearby.setOnItemClickListener(new OnItemClickListenerThree() {
            @Override
            public void onItemClick(int position,String city,String area,String apartment,String distance) {
                Constants.selectedNearByCity = city;
                Constants.selectedNearByArea = area;
                Constants.selectedNearByDistance = distance;
                if (Constants.myNearbyList.get(position).getFilter().equals("Within Community")){
                    if (dataBase.getUserId().equals("") || dataBase.getApartmentName().equals("") || dataBase.getApartmentName().equals("null")){
                        Toast.makeText(getContext(), "You are not in any community.", Toast.LENGTH_SHORT).show();
                    }else {
                        Constants.selectedNearByApartment = apartment;
                    }
                }else {
                    Constants.selectedNearByApartment = apartment;
                }
            }
        });
    }

    private void getCategoryList() {
        if (Constants.catList.size() > 0){
            adapter = new BookCatAdapter(Constants.catList, getContext());
            GridLayoutManager manager = new GridLayoutManager(getContext(), 3);
            rvCategory.setLayoutManager(manager);
            rvCategory.setAdapter(adapter);

            adapter.setOnItemClickListener(new OnItemClickListenerTow() {
                @Override
                public void onItemClick(int position,boolean isSelected) {
                    if (isSelected){
                        Constants.selectedCatList.add(Constants.catList.get(position).getCategory_id());
                    }else {
                        Constants.selectedCatList.remove(Constants.catList.get(position).getCategory_id());
                    }

                    Constants.selectedCategories =  android.text.TextUtils.join(",",Constants.selectedCatList);
                    //sendFilterData(Constants.selectedCategories,Constants.selectedNearByCity,Constants.selectedNearByArea,Constants.selectedNearByApartment,Constants.selectedNearByDistance,Constants.selectedFilterValue,Constants.giveAway);
                    sendFilterData(Constants.selectedCategories);
                }
            });

        }
    }

    /*private void sendFilterData(String selectedCategories,String city,String area,String apartment,String distance,String type,String giveAway) {
        //OwnBooksFrag  frag = new OwnBooksFrag();
        //setCategoryData(selectedCategories,city,area,apartment,distance,type,giveAway);
        if (Constants.isOwnBooks.equals("1")){
            callback.callbackMethod(selectedCategories,city,area,apartment,distance,type,giveAway);
        }*//*else {
            callback2.callbackMethod(selectedCategories,city,area,apartment,distance,type,giveAway);
        }*//*

    }*/

    private void sendFilterData(String selectedCategories) {
        String city = "";
        String area = "";
        String apartment = "";
        String distance = "";
        String type = "";
        String giveAway = "";
        if (Constants.isOwnBooks.equals("1")){
            callback.callbackMethod(selectedCategories,city,area,apartment,distance,type,giveAway);
        }/*else {
            callback2.callbackMethod(selectedCategories,city,area,apartment,distance,type,giveAway);
        }*/

    }

    public String getUserCity(double lat,double lon){
        //location = new UserCurrentLocation(this);
        String city="";
        Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
        List<Address> addresses = new ArrayList<>();
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

    public String getUserPinCode(double lat,double lon){
        //location = new UserCurrentLocation(this);
        String area = "";
        Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
        List<Address> addresses = new ArrayList<>();
        try {
            addresses = geocoder.getFromLocation(lat, lon, 1);
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

}
