package com.lib.liblibgo.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lib.liblibgo.R;
import com.lib.liblibgo.api.AllApiClass;
import com.lib.liblibgo.api.Holders;
import com.lib.liblibgo.common.UserDatabase;
import com.lib.liblibgo.model.SubmitDataResModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LibCoinWithdrawActivity extends AppCompatActivity {

    private String libCoins = "";
    private TextView tvCoins;
    private Button btnRedeem;
    int count = 1;
    private UserDatabase database;
    private RelativeLayout rlBankInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lib_coin_withdraw);

        database = new UserDatabase(this);

        libCoins = getIntent().getStringExtra("libcoins");
        tvCoins = (TextView)findViewById(R.id.tvCoins);
        btnRedeem = (Button)findViewById(R.id.btnRedeem);
        rlBankInfo = (RelativeLayout)findViewById(R.id.rlBankInfo);

        tvCoins.setText("Total LibCoins : "+libCoins);

        btnRedeem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(LibCoinWithdrawActivity.this, "Redeem Successfully.", Toast.LENGTH_SHORT).show();
                showRedeemPopup(R.layout.redeem_popup);
            }
        });

        if (Integer.parseInt(libCoins) >= 10000){
            rlBankInfo.setVisibility(View.VISIBLE);
        }else {
            rlBankInfo.setVisibility(View.INVISIBLE);
        }
    }

    private void showRedeemPopup(int layout) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View layoutView = inflater.inflate(layout, null);
        final Button btn_done = layoutView.findViewById(R.id.btn_done);
        final TextView tvQty = layoutView.findViewById(R.id.tvQty);
        final ImageView ivPlus = layoutView.findViewById(R.id.ivPlus);
        final ImageView ivMinus = layoutView.findViewById(R.id.ivMinus);
        final ImageView ivClose = layoutView.findViewById(R.id.ivClose);
        final ProgressBar progBar = layoutView.findViewById(R.id.progBar);

        dialogBuilder.setView(layoutView);
        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();
        alertDialog.setCancelable(false);
        alertDialog.getWindow().setLayout(800, ViewGroup.LayoutParams.WRAP_CONTENT);

        tvQty.setText(String.valueOf(count));

        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });

        ivPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int coinDiv = Integer.parseInt(libCoins) / 1000;
                if (coinDiv != count){
                    ++count;
                    tvQty.setText(String.valueOf(count));
                }

            }
        });

        ivMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (count != 1){
                    count--;
                    tvQty.setText(String.valueOf(count));
                }else {
                    count = 1;
                }
            }
        });

        btn_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btn_done.setVisibility(View.INVISIBLE);
                progBar.setVisibility(View.VISIBLE);
                Holders holders = AllApiClass.getInstance().getApi();
                Log.d("myQty",String.valueOf(count));
                Call<SubmitDataResModel> call = holders.sendAmazonCardRequest(database.getUserId(),String.valueOf(count));
                call.enqueue(new Callback<SubmitDataResModel>() {
                    @Override
                    public void onResponse(Call<SubmitDataResModel> call, Response<SubmitDataResModel> response) {
                        if (response.isSuccessful()){
                            if (response.body().getResponse().getCode() == 1){
                                alertDialog.dismiss();
                                UserProfileActivity.flag = 1;
                                onBackPressed();
                            }else {
                                btn_done.setVisibility(View.VISIBLE);
                                progBar.setVisibility(View.INVISIBLE);
                                Toast.makeText(LibCoinWithdrawActivity.this, ""+response.body().getResponse().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }else {
                            btn_done.setVisibility(View.VISIBLE);
                            progBar.setVisibility(View.INVISIBLE);
                            Toast.makeText(LibCoinWithdrawActivity.this, "Something went wrong !", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<SubmitDataResModel> call, Throwable t) {
                        btn_done.setVisibility(View.VISIBLE);
                        progBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(LibCoinWithdrawActivity.this, ""+t, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }

    public void onBack(View view) {
        onBackPressed();
    }
}