package com.lib.liblibgo.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.lib.liblibgo.R;
import com.lib.liblibgo.activity.homedashboard.HomeActivity;
import com.lib.liblibgo.activity.homedashboard.fragmentshome.HomeFragment;
import com.lib.liblibgo.api.AllApiClass;
import com.lib.liblibgo.api.Holders;
import com.lib.liblibgo.common.Constants;
import com.lib.liblibgo.common.Helper;
import com.lib.liblibgo.common.UserDatabase;
import com.lib.liblibgo.model.LoginWithPhoneModel;
import com.lib.liblibgo.model.VerifyOTPModel;
import com.mukesh.OtpView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VerifyOtp extends AppCompatActivity {

    private String phoneNo;
    private String otpCode;
    private TextView mTvNumber;
    private OtpView mOtpView;
    private UserDatabase preferences;
    private String refreshedToken = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_otp);
        preferences = new UserDatabase(VerifyOtp.this);

        phoneNo = getIntent().getStringExtra("phone");
        otpCode = getIntent().getStringExtra("otp");

        mTvNumber = (TextView)findViewById(R.id.tv3);
        mTvNumber.setText("+91 "+phoneNo);

        mOtpView = (OtpView)findViewById(R.id.otp_view);
        //mOtpView.setText(otpCode);

        refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d("myToken",refreshedToken);

    }

    public void onVerify(View view) {
        String otp = mOtpView.getText().toString();
        if (otp.equals("")){
            Toast.makeText(this, "Enter OTP.", Toast.LENGTH_SHORT).show();
        }else if (otp.length() < 3){
            Toast.makeText(this, "Enter 4 digit otp.", Toast.LENGTH_SHORT).show();
        }else {
            gotoVerify(otp);
        }
    }

    private void gotoVerify(String otp) {
        final ProgressDialog loginDialog = new ProgressDialog(this);
        loginDialog.setMessage("Please wait..");
        loginDialog.setCancelable(false);
        loginDialog.show();
        Holders holders = AllApiClass.getInstance().getApi();
        Call<VerifyOTPModel> call = holders.verifyOtp(phoneNo,otp,refreshedToken);
        call.enqueue(new Callback<VerifyOTPModel>() {
            @Override
            public void onResponse(Call<VerifyOTPModel> call, Response<VerifyOTPModel> response) {
                if (response.isSuccessful()){
                    if (response.body().getResponse().getCode().equals("1")){
                        loginDialog.dismiss();

                        if (response.body().getResponse().getApartment_status().equals("0")){
                            Intent intent = new Intent(VerifyOtp.this,NewUserDetailsActivity.class);
                            intent.putExtra("phone",response.body().getResponse().getMobile());
                            startActivity(intent);
                            finish();
                        }else {
                            Helper.showToast(VerifyOtp.this,response.body().getResponse().getMessage());
                            /*Intent intent = new Intent(VerifyOtp.this, HomeActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);*/
                            Constants.SOCIAL_USER_TYPE = "";
                            Constants.SOCIAL_USER_NAME = "";
                            Constants.SOCIAL_USER_EMAIL = "";
                            Constants.SOCIAL_ID = "";
                            preferences.createLogin(response.body().getResponse().getUser_id(),
                                    response.body().getResponse().getUser_name(),
                                    response.body().getResponse().getMobile(),response.body().getResponse().getPincode(),
                                    response.body().getResponse().getAddress(),response.body().getResponse().getApartment_name());
                            HomeActivity.flag = 1;
                            HomeFragment.flag = 1;
                            finish();
                        }

                    }else {
                        loginDialog.dismiss();
                        Toast.makeText(VerifyOtp.this, ""+response.body().getResponse().getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }else {
                    Toast.makeText(VerifyOtp.this, "Something went wrong !", Toast.LENGTH_SHORT).show();
                    loginDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<VerifyOTPModel> call, Throwable t) {
                Toast.makeText(VerifyOtp.this, "Server not responding yet.Try again !", Toast.LENGTH_SHORT).show();
                loginDialog.dismiss();
            }
        });
    }

    public void onResendOtp(View view) {
        final ProgressDialog loginDialog = new ProgressDialog(this);
        loginDialog.setMessage("Please wait..");
        loginDialog.setCancelable(false);
        loginDialog.show();
        Holders holders = AllApiClass.getInstance().getApi();
        Call<LoginWithPhoneModel> call = holders.loginWithPhone(phoneNo);
        call.enqueue(new Callback<LoginWithPhoneModel>() {
            @Override
            public void onResponse(Call<LoginWithPhoneModel> call, Response<LoginWithPhoneModel> response) {
                if (response.isSuccessful()){
                    if (response.body().getResponse().getCode() == 1){
                        loginDialog.dismiss();
                        Toast.makeText(VerifyOtp.this, "Otp Sent.", Toast.LENGTH_SHORT).show();
                    }else {
                        loginDialog.dismiss();
                        Toast.makeText(VerifyOtp.this, ""+response.body().getResponse().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(VerifyOtp.this, "Something went wrong !", Toast.LENGTH_SHORT).show();
                    loginDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<LoginWithPhoneModel> call, Throwable t) {
                Toast.makeText(VerifyOtp.this, "Server not responding yet.Try again !", Toast.LENGTH_SHORT).show();
                loginDialog.dismiss();
            }
        });
    }

    public void onBackToLogin(View view) {
        Intent intent = new Intent(VerifyOtp.this,LoginWithPhoneNumber.class);
        startActivity(intent);
        finish();
    }
}