package com.lib.liblibgo.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.lib.liblibgo.R;
import com.lib.liblibgo.activity.homedashboard.HomeActivity;
import com.lib.liblibgo.api.AllApiClass;
import com.lib.liblibgo.api.Holders;
import com.lib.liblibgo.common.Constants;
import com.lib.liblibgo.common.Helper;
import com.lib.liblibgo.common.UserDatabase;
import com.lib.liblibgo.common.ValidityClass;
import com.lib.liblibgo.model.ResData;
import com.lib.liblibgo.model.ResModel;
import com.lib.liblibgo.views.MyButton;
import com.lib.liblibgo.views.MyCheckBox;
import com.lib.liblibgo.views.MyEditText;
import com.lib.liblibgo.views.MyTextView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "LoginActivity";
    MyButton loginBtn;
    private MyTextView tvForgot, tvRegister;
    private MyEditText loginPhone, loginPass;
    private MyCheckBox rememberCheck;
    private Intent intent;
    private String userId,userPass;
    private UserDatabase preferences;
    private String refreshedToken = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        preferences = new UserDatabase(LoginActivity.this);
        initView();

        /*if (preferences.isLogin()){
            Log.d("SAVED", "onCreate: "+preferences.isLogin());
            Intent intentForgot=new Intent(LoginActivity.this,HomeActivity.class);
            intentForgot.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intentForgot);
        }*/

        refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d("myToken",refreshedToken);

    }

    private void initView() {
        tvForgot = findViewById(R.id.tvForgot);
        tvRegister = findViewById(R.id.tvRegister);
        loginPhone = findViewById(R.id.loginPhone);
        loginPass = findViewById(R.id.loginPass);
        loginBtn = findViewById(R.id.loginBtn);
        rememberCheck = findViewById(R.id.rememberCheck);

        loginBtn.setOnClickListener(this);
        tvForgot.setOnClickListener(this);
        tvRegister.setOnClickListener(this);
        rememberCheck.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.loginBtn:
                Log.d(TAG, "onClick: Forget");
                if (!loginError()){
                   serverLogin();
                }
                break;
            case R.id.tvForgot:
                Log.d(TAG, "onClick: Forget");
                intent = new Intent(LoginActivity.this, ForgotActivity.class);
                startActivity(intent);
                break;
            case R.id.tvRegister:
                Log.d(TAG, "onClick: Register");
                intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                break;
        }
    }

    private void serverLogin() {
        Holders holders = AllApiClass.getInstance().getApi();
        Call<ResModel> resCall = holders.serverLogin(userId,userPass,refreshedToken);
        resCall.enqueue(new Callback<ResModel>() {
            @Override
            public void onResponse(Call<ResModel> call, Response<ResModel> response) {
                if (response.isSuccessful()){
                    Helper.showToast(LoginActivity.this,response.body().getResponse().getMessage());
                    if (response.body().getResponse().getCode()==1){
                        if (rememberCheck.isChecked()){
                            Constants.SAVED = true;
                            Constants.LOG_ID = loginPhone.getText().toString();
                            Constants.LOG_PASS = loginPass.getText().toString();
                        }
                        ResData data = response.body().getResponse();
                        preferences.createLogin(userId,userPass,data.getUserId(),data.getName(),data.getEmail()
                                ,data.getMobile(),data.getFlatNo(),data.getApartmentId(),response.body().getResponse().getPincode(),
                                response.body().getResponse().getAddress(),response.body().getResponse().getApartmentName());
                        Helper.showToast(LoginActivity.this,response.body().getResponse().getMessage());
                        intent = new Intent(LoginActivity.this, HomeActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }else {
                        Toast.makeText(LoginActivity.this, ""+response.body().getResponse().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(LoginActivity.this, "Something went wrong !", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResModel> call, Throwable t) {
                Toast.makeText(LoginActivity.this, ""+t, Toast.LENGTH_SHORT).show();
            }
        });

    }

    private boolean loginError() {
        boolean error = false;
        userId = loginPhone.getText().toString();
        userPass = loginPass.getText().toString();
        if (userId.isEmpty()) {
            error = true;
            loginPhone.setError("Enter Email");
            loginPhone.requestFocus();
        }
        if (userPass.isEmpty()){
            error = true;
            loginPass.setError("Enter password");
            loginPass.requestFocus();
        }
        if (!ValidityClass.isValidEmail(userId)){
            error = true;
            loginPhone.setError("Invalid Email");
            loginPhone.requestFocus();
        }
        return error;
    }
}