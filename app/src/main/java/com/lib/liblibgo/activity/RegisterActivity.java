package com.lib.liblibgo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.lib.liblibgo.R;
import com.lib.liblibgo.adapter.ApartmentAdapter;
import com.lib.liblibgo.api.AllApiClass;
import com.lib.liblibgo.api.Holders;
import com.lib.liblibgo.common.Helper;
import com.lib.liblibgo.common.ValidityClass;
import com.lib.liblibgo.model.ApartmentModel;
import com.lib.liblibgo.model.ResModel;
import com.lib.liblibgo.views.MyButton;
import com.lib.liblibgo.views.MyEditText;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    private static final String TAG = "RegisterActivity";
    private MyButton RegBtn,alreadyBtn;
    private MyEditText regName,regEmail,regPhone,regFlat,regPassword;
    private String name,email,phone,flat,password,apartID, apartName;
    Spinner spApartment;
    private Intent regIntent;
    private List<ApartmentModel> apartments;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initView();
        getAllApartment();

//        ArrayList<String> mList = Helper.getApartment();
//        ApartmentAdapter adapter = new ApartmentAdapter(RegisterActivity.this,mList);
//        spApartment.setAdapter(adapter);
        spApartment.setOnItemSelectedListener(this);
        alreadyBtn.setOnClickListener(this);
        RegBtn.setOnClickListener(this);
    }

    private void initView() {
        alreadyBtn =  findViewById(R.id.alreadyBtn);
        RegBtn = (MyButton) findViewById(R.id.RegBtn);
        spApartment = findViewById(R.id.regApartment);
        regName = findViewById(R.id.regName);
        regEmail = findViewById(R.id.regEmail);
        regPhone = findViewById(R.id.regPhone);
        regFlat = findViewById(R.id.regFlat);
        regPassword = findViewById(R.id.regPassword);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.alreadyBtn:
                regIntent = new Intent(RegisterActivity.this,LoginActivity.class);
                regIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(regIntent);
                break;
            case R.id.RegBtn:
                if (!regError()){
                    userRegister();
                }
        }
    }


    private void getAllApartment() {
        apartments = new ArrayList<>();
        Holders holders = AllApiClass.getInstance().getApi1();
        Call<ResModel> resCall = holders.apartmentLis();
        resCall.enqueue(new Callback<ResModel>() {
            @Override
            public void onResponse(Call<ResModel> call, Response<ResModel> response) {
                if (response.isSuccessful()) {
                    if (response.body().getResponse().getCode() == 1) {
                        if (response.body().getResponse().getApartmentModel() != null) {
                            apartments = new ArrayList<>();
                            apartments = response.body().getResponse().getApartmentModel();
                            ApartmentAdapter adapter = new ApartmentAdapter(RegisterActivity.this,
                                    apartments);
                            spApartment.setAdapter(adapter);
                            adapter.notifyDataSetChanged();

                        }
                    }
                }

            }

            @Override
            public void onFailure(Call<ResModel> call, Throwable t) {
                Toast.makeText(RegisterActivity.this, "Try again", Toast.LENGTH_SHORT).show();
            }
        });

    }
    private void userRegister() {
        Holders holders = AllApiClass.getInstance().getApi();
        Call<ResModel> call = holders.register(name,email,phone,apartID,flat,password);
        call.enqueue(new Callback<ResModel>() {
            @Override
            public void onResponse(Call<ResModel> call, Response<ResModel> response) {
                if (response.isSuccessful()){
                    Helper.showToast(RegisterActivity.this,response.body().getResponse().getMessage());

                    if (response.body().getResponse().getCode()==1){
                        Helper.showToast(RegisterActivity.this,response.body().getResponse().getMessage());
                        regIntent = new Intent(RegisterActivity.this, LoginActivity.class);
                        regIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(regIntent);
                    }
                }
            }

            @Override
            public void onFailure(Call<ResModel> call, Throwable t) {

            }
        });
    }

    private boolean regError() {
        name = regName.getText().toString();
        email = regEmail.getText().toString();
        phone = regPhone.getText().toString();
        flat = regFlat.getText().toString();
        password = regPassword.getText().toString();
        boolean error = false;
        if (name.isEmpty()){
            error = true;
            regName.setError("Enter name");
            regName.requestFocus();
        }
        if (email.isEmpty()){
            error = true;
            regEmail.setError("Enter email");
            regEmail.requestFocus();
        }
        if (phone.isEmpty()){
            error = true;
            regPhone.setError("Enter phone");
            regPhone.requestFocus();
        }
        if (flat.isEmpty()){
            error = true;
            regFlat.setError("Enter flat");
            regFlat.requestFocus();
        }
        if (password.isEmpty()){
            error = true;
            regPassword.setError("Enter password");
            regPassword.requestFocus();
        }
        if (!ValidityClass.isValidMobile(phone)){
            error = true;
            regPhone.setError("Invalid number");
            regPhone.requestFocus();
        }
        if (!ValidityClass.isValidEmail(email)){
            error = true;
            regEmail.setError("Invalid email");
            regEmail.requestFocus();
        }
        if (apartName.isEmpty()){
            error = true;
            spApartment.requestFocus();
            Helper.spinnerError(spApartment,"Select apartment");
        }

        return error;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        apartID = apartments.get(position).getApartmentId();
        apartName = apartments.get(position).getApartmentName();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}