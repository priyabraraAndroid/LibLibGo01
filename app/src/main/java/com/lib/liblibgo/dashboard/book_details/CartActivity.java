package com.lib.liblibgo.dashboard.book_details;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.lib.liblibgo.R;
import com.lib.liblibgo.activity.homedashboard.HomeActivity;
import com.lib.liblibgo.adapter.CartAdapter;
import com.lib.liblibgo.api.AllApiClass;
import com.lib.liblibgo.api.Holders;
import com.lib.liblibgo.common.UserDatabase;
import com.lib.liblibgo.model.CartModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartActivity extends AppCompatActivity {
    private TextView titleTool;
    private ImageView backTool;
    private UserDatabase database;
    private RecyclerView rvCartList;
    private ProgressBar progBar;
    private List<CartModel.ResDataBooks.CartListData> myCartList = new ArrayList<>();
    private CartAdapter adapter;
    private Button btnContinue;
    private SharedPreferences sharedpreferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        database = new UserDatabase(this);
        sharedpreferences = getSharedPreferences(HomeActivity.MyPREFERENCES, Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();

        titleTool = findViewById(R.id.titleTool);
        backTool = findViewById(R.id.backTool);
        setTopView(getString(R.string.my_cart));

        rvCartList = (RecyclerView)findViewById(R.id.rvCartList);
        progBar = (ProgressBar)findViewById(R.id.progBar);

        btnContinue = (Button)findViewById(R.id.btnContinue);

        getCartLists();

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
                       /* editor.putInt("cart_count",myCartList.size());
                        editor.commit();*/
                        if (myCartList.size() > 0){
                            adapter = new CartAdapter(myCartList, CartActivity.this);
                            LinearLayoutManager verticalManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
                            // verticalManager.setReverseLayout(true);
                            // verticalManager.setStackFromEnd(true);
                            rvCartList.setLayoutManager(verticalManager);
                            rvCartList.setAdapter(adapter);
                            adapter.notifyDataSetChanged();

                            btnContinue.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if (myCartList.size() > 0 ){
                                        Intent intent = new Intent(CartActivity.this,CheckoutActivity.class);
                                        startActivity(intent);
                                    }else {
                                        Toast.makeText(CartActivity.this, "Add item in cart.", Toast.LENGTH_SHORT).show();
                                    }

                                }
                            });

                        }
                    }else {
                        editor.putInt("cart_count",myCartList.size());
                        editor.commit();
                        progBar.setVisibility(View.GONE);
                        btnContinue.setVisibility(View.GONE);
                        Toast.makeText(CartActivity.this, "Cart is empty.", Toast.LENGTH_SHORT).show();
                        //Toast.makeText(CartActivity.this, ""+response.body().getResponse().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }else {
                    progBar.setVisibility(View.GONE);
                    btnContinue.setVisibility(View.GONE);
                    Toast.makeText(CartActivity.this, "Something went wrong !", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CartModel> call, Throwable t) {
                progBar.setVisibility(View.GONE);
                btnContinue.setVisibility(View.GONE);
                Toast.makeText(CartActivity.this, ""+t, Toast.LENGTH_SHORT).show();
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
    protected void onResume() {
        super.onResume();
    }
}