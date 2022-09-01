package com.lib.liblibgo.dashboard.profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.lib.liblibgo.R;
import com.lib.liblibgo.activity.homedashboard.HomeActivity;
import com.lib.liblibgo.adapter.ApartmentAdapter;
import com.lib.liblibgo.api.AllApiClass;
import com.lib.liblibgo.api.Holders;
import com.lib.liblibgo.common.Helper;
import com.lib.liblibgo.common.UserDatabase;
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

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private MyButton UpBtn,alreadyBtn;
    private TextView myTextView;
    private Spinner spApartment;
    private Intent profIntent;
    private MyEditText regName,regEmail,regPhone,regFlat,regPassword;
    private String name,email,phone,flat,password,apartID, apartName;
    private List<ApartmentModel> prApList;
    private UserDatabase database;
    private Intent profileIntent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        database = new UserDatabase(ProfileActivity.this);
        initView();
        getAllApartment();
    }

    private void initView() {
        UpBtn = findViewById(R.id.RegBtn);
        alreadyBtn = findViewById(R.id.alreadyBtn);
        myTextView = findViewById(R.id.myTextView);
        spApartment = findViewById(R.id.regApartment);
        regName = findViewById(R.id.regName);
        regEmail = findViewById(R.id.regEmail);
        regPhone = findViewById(R.id.regPhone);
        regFlat = findViewById(R.id.regFlat);
        regPassword = findViewById(R.id.regPassword);

        UpBtn.setText("Update");
        myTextView.setText("Profile Update");
        alreadyBtn.setVisibility(View.GONE);
        regPassword.setVisibility(View.GONE);


        regName.setText(database.getNAME());
        regEmail.setText(database.getEMAIL());
        regEmail.setEnabled(false);
        regPhone.setText(database.getPHONE());
        regFlat.setText(database.getFLAT());

        UpBtn.setOnClickListener(this);

        spApartment.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                apartID = prApList.get(position).getApartmentId();
                apartName = prApList.get(position).getApartmentName();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void getAllApartment() {
        prApList = new ArrayList<>();
        Holders holders = AllApiClass.getInstance().getApi1();
        Call<ResModel> resCall = holders.apartmentLis();
        resCall.enqueue(new Callback<ResModel>() {
            @Override
            public void onResponse(Call<ResModel> call, Response<ResModel> response) {
                if (response.isSuccessful()) {
                    if (response.body().getResponse().getCode() == 1) {
                        if (response.body().getResponse().getApartmentModel() != null) {
                            prApList = response.body().getResponse().getApartmentModel();
                            ApartmentAdapter adapter = new ApartmentAdapter(ProfileActivity.this,
                                    prApList);
                            spApartment.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                            for (int i=0;i<prApList.size();i++){
                                if (prApList.get(i).getApartmentId().equals(database.getApartId()))
                                    spApartment.setSelection(i);
                            }
                        }
                    }
                }

            }

            @Override
            public void onFailure(Call<ResModel> call, Throwable t) {
                Toast.makeText(ProfileActivity.this, "Try again", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.RegBtn:
                if (!regError()){
                    profileUpdate();
                }
                break;
        }
    }

    private void profileUpdate() {
        Holders holders = AllApiClass.getInstance().getApi();
        Call<ResModel> call = holders.updateProfile(database.getUserId(),name,email,phone,apartID,flat);
        call.enqueue(new Callback<ResModel>() {
            @Override
            public void onResponse(Call<ResModel> call, Response<ResModel> response) {
                if (response.isSuccessful()){
                    Helper.showToast(ProfileActivity.this,response.body().getResponse().getMessage());

                    if (response.body().getResponse().getCode()==1){
                        Helper.showToast(ProfileActivity.this,response.body().getResponse().getMessage());
                        /*database.createLogin(name,email
                                ,phone,flat,apartID);*/
                        Helper.showToast(ProfileActivity.this,response.body().getResponse().getMessage());
                        profileIntent = new Intent(ProfileActivity.this, HomeActivity.class);
                        profileIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(profileIntent);
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
        return error;
    }
}
