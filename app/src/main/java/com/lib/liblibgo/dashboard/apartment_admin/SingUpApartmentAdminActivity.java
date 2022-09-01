package com.lib.liblibgo.dashboard.apartment_admin;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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

public class SingUpApartmentAdminActivity extends AppCompatActivity {

    private TextView mTvStatus;
    private Button mButtonSendRequest;
    UserDatabase userDatabase;
    private String requestStatus;
    private TextView mTvMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sing_up_apartment_admin);

        userDatabase = new UserDatabase(this);

        mTvStatus = (TextView)findViewById(R.id.tv_status);
        mTvMsg = (TextView)findViewById(R.id.tv_msg);
        mButtonSendRequest = (Button)findViewById(R.id.sendRequestBtn);

        requestStatus = getIntent().getStringExtra("request_status");

        if (requestStatus.equals("0")){
            mButtonSendRequest.setBackground(getResources().getDrawable(R.drawable.btn_bg_grey));
            mTvStatus.setVisibility(View.VISIBLE);
            mTvMsg.setVisibility(View.INVISIBLE);
            mTvStatus.setText("Already send request.");
            mTvStatus.setTextColor(getResources().getColor(R.color.yellow));
            mButtonSendRequest.setText("Request Sent");
            mButtonSendRequest.setEnabled(false);
        }else if (requestStatus.equals("2")){
            mButtonSendRequest.setBackground(getResources().getDrawable(R.drawable.btn_bg));
            mTvStatus.setVisibility(View.VISIBLE);
            mTvStatus.setText("Your request is rejected");
            mTvStatus.setTextColor(getResources().getColor(R.color.red));
            mButtonSendRequest.setText("Send Request");
            mTvMsg.setVisibility(View.INVISIBLE);
            mButtonSendRequest.setEnabled(true);
        }else {
            mButtonSendRequest.setBackground(getResources().getDrawable(R.drawable.btn_bg));
            mTvStatus.setVisibility(View.INVISIBLE);
            mButtonSendRequest.setText("Send Request");
            mButtonSendRequest.setEnabled(true);
            mTvMsg.setVisibility(View.VISIBLE);
            mTvStatus.setTextColor(getResources().getColor(R.color.green));
        }

    }

    public void onRequestSend(View view) {
        final ProgressDialog progressBar = new ProgressDialog(SingUpApartmentAdminActivity.this);
        progressBar.setMessage("Please wait...");
        progressBar.setCancelable(false);
        progressBar.show();
        Holders holders = AllApiClass.getInstance().getApi();
        Call<SubmitDataResModel> resCall = holders.sendSubAdminRequest(userDatabase.getUserId());
        resCall.enqueue(new Callback<SubmitDataResModel>() {
            @Override
            public void onResponse(Call<SubmitDataResModel> call, Response<SubmitDataResModel> response) {
                if (response.isSuccessful()){
                    if (response.body().getResponse().getCode() == 1){
                        progressBar.dismiss();
                        mButtonSendRequest.setBackground(getResources().getDrawable(R.drawable.btn_bg_grey));
                        mTvStatus.setVisibility(View.VISIBLE);
                        mTvStatus.setText("Request has been sent successfully.");
                        mTvStatus.setTextColor(getResources().getColor(R.color.yellow));
                        mButtonSendRequest.setText("Request Sent");
                        mTvMsg.setVisibility(View.INVISIBLE);
                    }else {
                        progressBar.dismiss();
                        Toast.makeText(SingUpApartmentAdminActivity.this, ""+response.body().getResponse().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }else {
                    progressBar.dismiss();
                    Toast.makeText(SingUpApartmentAdminActivity.this, "Something went wrong !", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<SubmitDataResModel> call, Throwable t) {
                progressBar.dismiss();
                Toast.makeText(SingUpApartmentAdminActivity.this, "Check your internet !", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void onBack(View view) {
        onBackPressed();
    }
}