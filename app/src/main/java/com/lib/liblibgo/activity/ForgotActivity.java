package com.lib.liblibgo.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.lib.liblibgo.R;
import com.lib.liblibgo.api.AllApiClass;
import com.lib.liblibgo.api.Holders;
import com.lib.liblibgo.model.SubmitDataResModel;
import com.lib.liblibgo.views.MyButton;
import com.lib.liblibgo.views.MyEditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "ForgotActivity";
    private Intent intent;
    private MyButton sendCode,backLoginBtn;
    private MyEditText forgetEmail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot);
        initView();
    }

    private void initView() {
        sendCode = findViewById(R.id.sendCode);
        backLoginBtn = findViewById(R.id.backLoginBtn);
        forgetEmail = findViewById(R.id.forgetEmail);

        sendCode.setOnClickListener(this);
        backLoginBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.backLoginBtn:
                intent = new Intent(ForgotActivity.this,LoginWithEmail.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                break;
            case R.id.sendCode:
                if (forgetEmail.getText().toString().trim().isEmpty()){
                    forgetEmail.setError("please enter email");
                    forgetEmail.requestFocus();
                    return;
                }else {
                    resetPassword();
                }
                break;
        }
    }

    private void resetPassword() {
        final ProgressDialog progressBar = new ProgressDialog(ForgotActivity.this);
        progressBar.setMessage("Please wait...");
        progressBar.setCancelable(false);
        progressBar.show();
        Holders holders = AllApiClass.getInstance().getApi();
        Call<SubmitDataResModel> msgCall = holders.sendVerificationCode(forgetEmail.getText().toString());
        msgCall.enqueue(new Callback<SubmitDataResModel>() {
            @Override
            public void onResponse(Call<SubmitDataResModel> call, Response<SubmitDataResModel> response) {
                if (response.isSuccessful()){
                    if (response.body().getResponse().getCode()==1){
                        progressBar.dismiss();
                        Toast.makeText(ForgotActivity.this,response.body().getResponse().getMessage()
                                ,Toast.LENGTH_LONG).show();
                        Intent intentForgot=new Intent(ForgotActivity.this,PasswordActivity.class);
                        intentForgot.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intentForgot);
                    }else {
                        progressBar.dismiss();
                        Toast.makeText(ForgotActivity.this,response.body().getResponse().getMessage()
                                ,Toast.LENGTH_LONG).show();
                    }
                }else {
                    progressBar.dismiss();
                    Toast.makeText(ForgotActivity.this,"Something went wrong !"
                            ,Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<SubmitDataResModel> call, Throwable t) {
                progressBar.dismiss();
                Toast.makeText(ForgotActivity.this,"Try again",Toast.LENGTH_LONG).show();
                Log.d("FORGET", "onFailure: "+t.getMessage());
            }
        });
    }
}