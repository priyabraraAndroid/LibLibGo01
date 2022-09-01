package com.lib.liblibgo.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.lib.liblibgo.R;
import com.lib.liblibgo.activity.homedashboard.HomeActivity;
import com.lib.liblibgo.api.AllApiClass;
import com.lib.liblibgo.api.Holders;
import com.lib.liblibgo.common.Helper;
import com.lib.liblibgo.common.UserDatabase;
import com.lib.liblibgo.common.ValidityClass;
import com.lib.liblibgo.model.ResData;
import com.lib.liblibgo.model.ResModel;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.scottyab.showhidepasswordedittext.ShowHidePasswordEditText;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginWithEmail extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    Button loginBtn;
    private EditText loginEmail;
    private ShowHidePasswordEditText loginPass;
   // private MyCheckBox rememberCheck;
    private Intent intent;
    private String userId,userPass;
    private UserDatabase preferences;
    private String refreshedToken = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_with_email);

        preferences = new UserDatabase(LoginWithEmail.this);

        refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d("myToken",refreshedToken);

        initView();
    }

    private void initView() {
        loginEmail = findViewById(R.id.et_email);
        loginPass = findViewById(R.id.et_password);
    }

    public void onUserLogin(View view) {
        if (!loginError()){
            serverLogin();
        }
    }

    private void serverLogin() {
        final ProgressDialog loginDialog = new ProgressDialog(this);
        loginDialog.setMessage("Please wait..");
        loginDialog.setCancelable(false);
        loginDialog.show();
        Holders holders = AllApiClass.getInstance().getApi();
        Call<ResModel> resCall = holders.serverLogin(userId,userPass,refreshedToken);
        resCall.enqueue(new Callback<ResModel>() {
            @Override
            public void onResponse(Call<ResModel> call, Response<ResModel> response) {
                if (response.isSuccessful()){
                    Helper.showToast(LoginWithEmail.this,response.body().getResponse().getMessage());
                    if (response.body().getResponse().getCode()==1){
                        /*if (rememberCheck.isChecked()){
                            Constants.SAVED = true;
                            Constants.LOG_ID = loginPhone.getText().toString();
                            Constants.LOG_PASS = loginPass.getText().toString();
                        }*/
                        loginDialog.dismiss();
                        ResData data = response.body().getResponse();
                        preferences.createLogin(userId,userPass,data.getUserId(),data.getName(),data.getEmail()
                                ,data.getMobile(),data.getFlatNo(),data.getApartmentId(),response.body().getResponse().getPincode(),
                                response.body().getResponse().getAddress(),response.body().getResponse().getApartmentName());
                        Helper.showToast(LoginWithEmail.this,response.body().getResponse().getMessage());
                        intent = new Intent(LoginWithEmail.this, HomeActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }else {
                        loginDialog.dismiss();
                        Toast.makeText(LoginWithEmail.this, ""+response.body().getResponse().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }else {
                    loginDialog.dismiss();
                    Toast.makeText(LoginWithEmail.this, "Something went wrong !", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResModel> call, Throwable t) {
                loginDialog.dismiss();
                Toast.makeText(LoginWithEmail.this, ""+t, Toast.LENGTH_SHORT).show();
            }
        });

    }

    private boolean loginError() {
        boolean error = false;
        userId = loginEmail.getText().toString();
        userPass = loginPass.getText().toString();
        if (userId.isEmpty()) {
            error = true;
            loginEmail.setError("Enter Email");
            loginEmail.requestFocus();
        }
        if (userPass.isEmpty()){
            error = true;
            loginPass.setError("Enter password");
            loginPass.requestFocus();
        }
        if (!ValidityClass.isValidEmail(userId)){
            error = true;
            loginEmail.setError("Invalid Email");
            loginEmail.requestFocus();
        }
        return error;
    }

    public void onForgotPassword(View view) {
        Intent intent = new Intent(LoginWithEmail.this,ForgotActivity.class);
        startActivity(intent);
    }

    public void onRegisterUser(View view) {
        allowPermission();
    }

    private void allowPermission() {
        Dexter.withContext(this).withPermissions(Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                        Intent intent = new Intent(LoginWithEmail.this,NewRegistrationActivity.class);
                        startActivity(intent);
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                }).check();
    }
}