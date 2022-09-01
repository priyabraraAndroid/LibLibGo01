package com.lib.liblibgo.activity.homedashboard.fragmentshome;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.lib.liblibgo.R;
import com.lib.liblibgo.adapter.MyOrderAdapter;
import com.lib.liblibgo.api.AllApiClass;
import com.lib.liblibgo.api.Holders;
import com.lib.liblibgo.common.UserDatabase;
import com.lib.liblibgo.dashboard.profile.fragments.IssuedListFrag;
import com.lib.liblibgo.dashboard.profile.fragments.MyOrderFragment;
import com.lib.liblibgo.dashboard.profile.fragments.ReturnListFrag;
import com.lib.liblibgo.dashboard.profile.fragments.UnreviewedListFrag;
import com.lib.liblibgo.model.MyOederModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderFragment extends Fragment{

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private RelativeLayout rlLocation;
    private TextView tvRange;
    private UserDatabase database;
    private PopupWindow popupWindow;

    private SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefMyAccount";
    private SharedPreferences.Editor editor;
    private ProgressBar progBar;
    private RecyclerView rvMyOrder;
    private List<MyOederModel.ResData.MyOrderList> myList = new ArrayList<>();
    private MyOrderAdapter adapter;
    private String filter = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_order2, container, false);

        rlLocation = (RelativeLayout)view.findViewById(R.id.rlLocation);
        tvRange = (TextView)view.findViewById(R.id.tvRange);
        database = new UserDatabase(getContext());
        progBar = (ProgressBar)view.findViewById(R.id.progBar);
        rvMyOrder = view.findViewById(R.id.rvMyOrder);
        sharedpreferences = getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();

        rlLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showLocationRangeMenu(view);
            }
        });

        /*FragmentManager fragmentManager = getChildFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        IssuedListFrag fragment = new IssuedListFrag();
        fragmentTransaction.replace(R.id.content1, fragment);
        fragmentTransaction.commit();*/

        getMyOrderList(filter);

        return view;
    }

    private void getMyOrderList(String filter) {
        progBar.setVisibility(View.VISIBLE);
        Holders holders = AllApiClass.getInstance().getApi();
        Call<MyOederModel> responseCall = holders.getMyOrderList(database.getUserId(),filter);
        responseCall.enqueue(new Callback<MyOederModel>() {
            @Override
            public void onResponse(Call<MyOederModel> call, Response<MyOederModel> response) {
                if (response.isSuccessful()){
                    if (response.body().getResponse().getCode().equals("1")){
                        progBar.setVisibility(View.GONE);
                        myList = response.body().getResponse().getOrder_list();
                        if (myList.size() > 0){
                            rvMyOrder.setVisibility(View.VISIBLE);
                            progBar.setVisibility(View.GONE);
                            adapter = new MyOrderAdapter(myList, getContext());
                            LinearLayoutManager verticalManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                            //verticalManager.setReverseLayout(true);
                            //verticalManager.setStackFromEnd(true);
                            rvMyOrder.setLayoutManager(verticalManager);
                            rvMyOrder.setAdapter(adapter);
                        }else {
                            rvMyOrder.setVisibility(View.GONE);
                            progBar.setVisibility(View.GONE);
                            Toast.makeText(getContext(), "No Order Found.", Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        rvMyOrder.setVisibility(View.GONE);
                        progBar.setVisibility(View.GONE);
                        Toast.makeText(getContext(), ""+response.body().getResponse().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }else {
                    rvMyOrder.setVisibility(View.GONE);
                    progBar.setVisibility(View.GONE);
                    Toast.makeText(getContext(), "Something went wrong !", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<MyOederModel> call, Throwable t) {
                progBar.setVisibility(View.GONE);
                rvMyOrder.setVisibility(View.GONE);
                Toast.makeText(getContext(), "Something went wrong !", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void showLocationRangeMenu(View view) {
        LayoutInflater layoutInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View popupView = layoutInflater.inflate(R.layout.my_account_filter, null);

        String selectedItem = sharedpreferences.getString("select", "");

        popupWindow = new PopupWindow(
                popupView,
                300,
                ViewGroup.LayoutParams.WRAP_CONTENT);

        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setOutsideTouchable(true);

        final TextView tvIssuedBooks = popupView.findViewById(R.id.tvIssuedBooks);
        final TextView tvmyOrders = popupView.findViewById(R.id.tvmyOrders);
        final TextView tvReturnBooks = popupView.findViewById(R.id.tvReturnBooks);
        final TextView tvUnReviewed = popupView.findViewById(R.id.tvUnReviewed);
        final TextView tvAll = popupView.findViewById(R.id.tvAll);

        if (selectedItem.equals("1")){
            tvIssuedBooks.setBackgroundColor(Color.parseColor("#345EA8"));
            tvIssuedBooks.setTextColor(Color.parseColor("#ffffff"));
        }else if (selectedItem.equals("2")){
            tvmyOrders.setBackgroundColor(Color.parseColor("#345EA8"));
            tvmyOrders.setTextColor(Color.parseColor("#ffffff"));
        }else if (selectedItem.equals("3")){
            tvReturnBooks.setBackgroundColor(Color.parseColor("#345EA8"));
            tvReturnBooks.setTextColor(Color.parseColor("#ffffff"));
        }else if (selectedItem.equals("4")){
            tvUnReviewed.setBackgroundColor(Color.parseColor("#345EA8"));
            tvUnReviewed.setTextColor(Color.parseColor("#ffffff"));
        }else {
            tvAll.setBackgroundColor(Color.parseColor("#345EA8"));
            tvAll.setTextColor(Color.parseColor("#ffffff"));
        }

        tvIssuedBooks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
                editor.putString("select","1");
                editor.commit();
                tvRange.setText("Borrowed");
                filter = "buyer_rent";
                getMyOrderList(filter);
               /* FragmentManager fragmentManager = getChildFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                IssuedListFrag fragment = new IssuedListFrag();
                fragmentTransaction.replace(R.id.content1, fragment);
                fragmentTransaction.commit();*/
            }
        });

        tvmyOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
                editor.putString("select","2");
                editor.commit();
                tvRange.setText("Lent");
                filter = "seller_rent";
                getMyOrderList(filter);
                /*FragmentManager fragmentManager = getChildFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                MyOrderFragment fragment = new MyOrderFragment();
                fragmentTransaction.replace(R.id.content1, fragment);
                fragmentTransaction.commit();*/
            }
        });

        tvReturnBooks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
                editor.putString("select","3");
                editor.commit();
                tvRange.setText("Bought");
                filter = "buyer_purchase";
                getMyOrderList(filter);
                /*FragmentManager fragmentManager = getChildFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                ReturnListFrag fragment = new ReturnListFrag();
                fragmentTransaction.replace(R.id.content1, fragment);
                fragmentTransaction.commit();*/
            }
        });

        tvUnReviewed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
                editor.putString("select","4");
                editor.commit();
                tvRange.setText("Sold");
                filter = "seller_purchase";
                getMyOrderList(filter);
                /*FragmentManager fragmentManager = getChildFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                UnreviewedListFrag fragment = new UnreviewedListFrag();
                fragmentTransaction.replace(R.id.content1, fragment);
                fragmentTransaction.commit();*/
            }
        });

        tvAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
                editor.putString("select","5");
                editor.commit();
                tvRange.setText("All Orders");
                filter = "";
                getMyOrderList(filter);
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



    /*public class Pager extends FragmentStatePagerAdapter {

        int tabCount;

        public Pager(FragmentManager fm, int tabCount) {
            super(fm);
            this.tabCount = tabCount;
        }

        @Override
        public Fragment getItem(int position) {
            //Returning the current tabs
            switch (position) {
                case 0:
                    IssuedListFrag tab1 = new IssuedListFrag();
                    return tab1;
                case 1:
                    MyOrderFragment tab2 = new MyOrderFragment();
                    return tab2;
                case 2:
                    ReturnListFrag tab3 = new ReturnListFrag();
                    return tab3;
                case 3:
                    UnreviewedListFrag tab4 = new UnreviewedListFrag();
                    return tab4;
                default:
                    return null;
            }
        }

        //Overriden method getCount to get the number of tabs
        @Override
        public int getCount() {
            return tabCount;
        }
    }*/

}