package com.lib.liblibgo.activity.homedashboard.fragmentshome;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.lib.liblibgo.R;
import com.lib.liblibgo.activity.LoginWithPhoneNumber;
import com.lib.liblibgo.adapter.CartAdapter;
import com.lib.liblibgo.api.AllApiClass;
import com.lib.liblibgo.api.Holders;
import com.lib.liblibgo.common.UserDatabase;
import com.lib.liblibgo.dashboard.book_details.CartActivity;
import com.lib.liblibgo.dashboard.book_details.CheckoutActivity;
import com.lib.liblibgo.model.CartModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartFragment extends Fragment {

    private TextView titleTool;
    private UserDatabase database;
    private RecyclerView rvCartList;
    private ProgressBar progBar;
    private List<CartModel.ResDataBooks.CartListData> myCartList = new ArrayList<>();
    private CartAdapter adapter;
    private Button btnContinue;
    private Context mContext;
    private TextView tvLogin;
    private LinearLayout llLogin;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cart, container, false);

        titleTool = view.findViewById(R.id.titleTool);
        titleTool.setText("My Cart");

        database = new UserDatabase(mContext);

        rvCartList = (RecyclerView)view.findViewById(R.id.rvCartList);
        progBar = (ProgressBar)view.findViewById(R.id.progBar);

        btnContinue = (Button)view.findViewById(R.id.btnContinue);
        tvLogin = (TextView)view.findViewById(R.id.tvLogin);
        llLogin = (LinearLayout)view.findViewById(R.id.llLogin);

        if (database.getUserId().equals("")){
            llLogin.setVisibility(View.VISIBLE);
            rvCartList.setVisibility(View.GONE);
        }else {
            llLogin.setVisibility(View.GONE);
            rvCartList.setVisibility(View.VISIBLE);
            getCartLists();
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

    private void getCartLists() {
        progBar.setVisibility(View.VISIBLE);
        Holders holders = AllApiClass.getInstance().getApi();
        Call<CartModel> call = holders.getCartList(database.getUserId());

        call.enqueue(new Callback<CartModel>() {
            @Override
            public void onResponse(Call<CartModel> call, Response<CartModel> response) {
                if (response.isSuccessful()){
                    if (response.body().getResponse().getCode().equals("1")){
                        progBar.setVisibility(View.GONE);
                        btnContinue.setVisibility(View.VISIBLE);
                        myCartList = response.body().getResponse().getCart_list();
                        if (myCartList.size() > 0){
                            adapter = new CartAdapter(myCartList, mContext);
                            LinearLayoutManager verticalManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
                            // verticalManager.setReverseLayout(true);
                            // verticalManager.setStackFromEnd(true);
                            rvCartList.setLayoutManager(verticalManager);
                            rvCartList.setAdapter(adapter);
                            adapter.notifyDataSetChanged();

                            btnContinue.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if (myCartList.size() > 0 ){
                                        Intent intent = new Intent(mContext, CheckoutActivity.class);
                                        startActivity(intent);
                                    }else {
                                        Toast.makeText(mContext, "Add item in cart.", Toast.LENGTH_SHORT).show();
                                    }

                                }
                            });

                        }
                    }else {
                        progBar.setVisibility(View.GONE);
                        btnContinue.setVisibility(View.GONE);
                        //Toast.makeText(mContext, ""+response.body().getResponse().getMessage(), Toast.LENGTH_SHORT).show();
                        Toast.makeText(mContext, "Cart is empty.", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    progBar.setVisibility(View.GONE);
                    btnContinue.setVisibility(View.GONE);
                    Toast.makeText(mContext, "Something went wrong !", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CartModel> call, Throwable t) {
                progBar.setVisibility(View.GONE);
                btnContinue.setVisibility(View.GONE);
                Toast.makeText(mContext, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onAttach(@NonNull Context context) {
        mContext = context;
        super.onAttach(context);
    }

    @Override
    public void onResume() {
        if (HomeFragment.flag == 1){
            if (database.getUserId().equals("")){
                llLogin.setVisibility(View.VISIBLE);
                rvCartList.setVisibility(View.GONE);
            }else {
                llLogin.setVisibility(View.GONE);
                getCartLists();
                rvCartList.setVisibility(View.VISIBLE);
                HomeFragment.flag = 0;
            }
        }
        super.onResume();
    }
}