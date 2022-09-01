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

//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Response;

public class PasswordActivity extends AppCompatActivity implements View.OnClickListener{

    private static boolean SEND_CODE = false;
    private MyEditText verifCode,newPassword,reNewPassword;
    private MyButton resetPassBtn,backLoginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);

        resetPassBtn = findViewById(R.id.resetPassBtn);
        backLoginBtn = findViewById(R.id.backLoginBtn1);


        verifCode = findViewById(R.id.verifCode);
        newPassword = findViewById(R.id.newPassword);
        reNewPassword = findViewById(R.id.reNewPassword);

        resetPassBtn.setOnClickListener(this);
        backLoginBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.resetPassBtn:
                if (verifCode.getText().toString().trim().isEmpty()){
                    verifCode.setError("please enter email");
                    verifCode.requestFocus();
                    return;
                }else if (newPassword.getText().toString().isEmpty()) {
                    newPassword.setError("enter new password");
                    newPassword.requestFocus();
                    return;
                }else if (reNewPassword.getText().toString().isEmpty()){
                    reNewPassword.setError("enter new password");
                    reNewPassword.requestFocus();
                }else if (!newPassword.getText().toString().equals(reNewPassword.getText().toString())){
                    reNewPassword.setError("password not match");
                    reNewPassword.requestFocus();
                }else {
                    resetPassword();
                }
                break;
            case R.id.backLoginBtn1:
                Intent intentForgot=new Intent(com.lib.liblibgo.activity.PasswordActivity.this,LoginWithEmail.class);
                startActivity(intentForgot);
                break;
        }
    }

    private void resetPassword() {
        final ProgressDialog progressBar = new ProgressDialog(PasswordActivity.this);
        progressBar.setMessage("Please wait...");
        progressBar.setCancelable(false);
        progressBar.show();
        Holders holders = AllApiClass.getInstance().getApi();
        Call<SubmitDataResModel> msgCall = holders.changePassword(verifCode.getText().toString(),
                newPassword.getText().toString());
        msgCall.enqueue(new Callback<SubmitDataResModel>() {
            @Override
            public void onResponse(Call<SubmitDataResModel> call, Response<SubmitDataResModel> response) {
                if (response.isSuccessful()){
                    if (response.body().getResponse().getCode()==1){
                        progressBar.dismiss();
                        Toast.makeText(PasswordActivity.this,response.body().getResponse().getMessage()
                                ,Toast.LENGTH_LONG).show();
                        Intent intentForgot=new Intent(PasswordActivity.this,LoginWithEmail.class);
                        intentForgot.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intentForgot);
                    }else {
                        progressBar.dismiss();
                        Toast.makeText(PasswordActivity.this,response.body().getResponse().getMessage()
                                ,Toast.LENGTH_LONG).show();
                    }
                }else {
                    progressBar.dismiss();
                    Toast.makeText(PasswordActivity.this,"Something went wrong !"
                            ,Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<SubmitDataResModel> call, Throwable t) {
                progressBar.dismiss();
                Toast.makeText(PasswordActivity.this,"Try again",Toast.LENGTH_LONG).show();
                Log.d("FORGET", "onFailure: "+t.getMessage());
            }
        });
    }

}