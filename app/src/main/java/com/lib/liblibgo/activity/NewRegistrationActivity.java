package com.lib.liblibgo.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.lib.liblibgo.R;
import com.lib.liblibgo.adapter.ApartAdapter;
import com.lib.liblibgo.api.AllApiClass;
import com.lib.liblibgo.api.Holders;
import com.lib.liblibgo.common.UserCurrentLocation;
import com.lib.liblibgo.listner.OnItemClickListener;
import com.lib.liblibgo.model.ApartmentNewModel;
import com.lib.liblibgo.model.SubmitDataResModel;
import com.scottyab.showhidepasswordedittext.ShowHidePasswordEditText;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewRegistrationActivity extends AppCompatActivity{

    private Button btnSubmit;
    private EditText etName,etEmail,etPincode,etApartment,etFlat,etArea,etCity,etState,etLandmark;
    private ShowHidePasswordEditText etPassword;
    private RecyclerView rvApartment;
    private List<ApartmentNewModel.ApartNewRes.AprtList> aprtList = new ArrayList<>();
    private ApartAdapter adapter;
    List<Address> addresses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_registration2);

        btnSubmit = (Button)findViewById(R.id.btnSubmit);

        etName = (EditText)findViewById(R.id.etName);
        etEmail = (EditText)findViewById(R.id.etEmail);
        etPincode = (EditText)findViewById(R.id.etPincode);
        etApartment = (EditText)findViewById(R.id.etApartment);
        etFlat = (EditText)findViewById(R.id.etFlat);
        etArea = (EditText)findViewById(R.id.etArea);
        etCity = (EditText)findViewById(R.id.etCity);
        etState = (EditText)findViewById(R.id.etState);
        etLandmark = (EditText)findViewById(R.id.etLandmark);
        etPassword = (ShowHidePasswordEditText)findViewById(R.id.et_password);
        rvApartment = (RecyclerView)findViewById(R.id.rvApartment);

        etApartment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String apartmet = charSequence.toString();
                if (!apartmet.equals("")){
                    if (!etPincode.getText().toString().trim().equals("") || etPincode.getText().toString().trim().length() > 5){
                        showApartmentSuggesions(apartmet,etPincode.getText().toString().trim());
                        rvApartment.setVisibility(View.VISIBLE);
                    }else {
                        Toast.makeText(NewRegistrationActivity.this, "Enter your pincode first.", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    rvApartment.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        showAddressDetails();


        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = etName.getText().toString().trim();
                String email = etEmail.getText().toString().trim();
                String pinCode = etPincode.getText().toString().trim();
                String apartmentName = etApartment.getText().toString().trim();
                String flat = etFlat.getText().toString().trim();
                String area = etArea.getText().toString().trim();
                String city = etCity.getText().toString().trim();
                String state = etState.getText().toString().trim();
                String landmark = etLandmark.getText().toString().trim();
                String password = etPassword.getText().toString().trim();

                if (name.equals("")){
                    Toast.makeText(NewRegistrationActivity.this, "Enter name", Toast.LENGTH_SHORT).show();
                }else if (email.equals("")){
                    Toast.makeText(NewRegistrationActivity.this, "Enter email", Toast.LENGTH_SHORT).show();
                }else if (pinCode.equals("")){
                    Toast.makeText(NewRegistrationActivity.this, "Enter pincode", Toast.LENGTH_SHORT).show();
                }else if (apartmentName.equals("")){
                    Toast.makeText(NewRegistrationActivity.this, "Enter apartment", Toast.LENGTH_SHORT).show();
                }else if (flat.equals("")){
                    Toast.makeText(NewRegistrationActivity.this, "Enter flat", Toast.LENGTH_SHORT).show();
                }else if (area.equals("")){
                    Toast.makeText(NewRegistrationActivity.this, "Enter city", Toast.LENGTH_SHORT).show();
                }else if (city.equals("")){
                    Toast.makeText(NewRegistrationActivity.this, "Enter city", Toast.LENGTH_SHORT).show();
                }else if (state.equals("")){
                    Toast.makeText(NewRegistrationActivity.this, "Enter state", Toast.LENGTH_SHORT).show();
                }else if (password.equals("")){
                    Toast.makeText(NewRegistrationActivity.this, "Enter password", Toast.LENGTH_SHORT).show();
                }else {
                    showAlert(name,email,pinCode,apartmentName,flat,area,city,state,landmark,password);
                }
            }
        });

    }

    private void showAlert(String name, String email, String pinCode, String apartmentName, String flat, String area, String city, String state, String landmark, String password) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View layoutView = inflater.inflate(R.layout.save_data_alert_popup, null);
        final TextView tv_yes = layoutView.findViewById(R.id.tv_yes);
        final TextView tv_no = layoutView.findViewById(R.id.tv_no);

        dialogBuilder.setView(layoutView);
        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation1;
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.setCancelable(false);
        alertDialog.show();
        alertDialog.getWindow().setLayout(700, ViewGroup.LayoutParams.WRAP_CONTENT);

        tv_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });

        tv_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
                registarNow(name,email,pinCode,apartmentName,flat,area,city,state,landmark,password);
            }
        });
    }

    private void registarNow(String name, String email, String pinCode, String apartmentName, String flat, String area, String city, String state, String landmark, String password) {
        final ProgressDialog loginDialog = new ProgressDialog(this);
        loginDialog.setMessage("Please wait..");
        loginDialog.setCancelable(false);
        loginDialog.show();
        Holders holders = AllApiClass.getInstance().getApi();
        Call<SubmitDataResModel> call = holders.registerUser(name,email,apartmentName,flat,area,city,state,pinCode,landmark,password);
        call.enqueue(new Callback<SubmitDataResModel>() {
            @Override
            public void onResponse(Call<SubmitDataResModel> call, Response<SubmitDataResModel> response) {
                if (response.isSuccessful()){
                    if (response.body().getResponse().getCode() == 1){
                        Log.d("myRes",response.toString());
                        loginDialog.dismiss();
                        onBackPressed();
                    }else {
                        loginDialog.dismiss();
                        Toast.makeText(NewRegistrationActivity.this, ""+response.body().getResponse().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(NewRegistrationActivity.this, "Something went wrong !", Toast.LENGTH_SHORT).show();
                    loginDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<SubmitDataResModel> call, Throwable t) {
                Toast.makeText(NewRegistrationActivity.this, "Server not responding yet.Try again !", Toast.LENGTH_SHORT).show();
                loginDialog.dismiss();
            }
        });
    }

    private void showAddressDetails() {
        UserCurrentLocation location = new UserCurrentLocation(NewRegistrationActivity.this);
        Log.d("locationnn",""+location.latitude);
        Log.d("locationnn",""+location.longitude);
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (!addresses.isEmpty() || addresses.size() > 0){
            String city = addresses.get(0).getSubAdminArea();
            String state = addresses.get(0).getAdminArea();
            String country = addresses.get(0).getCountryName();
            String postalCode = addresses.get(0).getPostalCode();
            String apartmentValue = addresses.get(0).getSubLocality();
            String knownName = addresses.get(0).getLocality();// Only if available else return NULL
            //adr = knownName+","+city+","+state+","+country+","+postalCode;
            etPincode.setText(postalCode);
            etCity.setText(city);
            etState.setText(state);
            etArea.setText(knownName+", "+city);
            etApartment.setText(apartmentValue);
        }else {
            etArea.setText("");
            etCity.setText("");
            etState.setText("");
            etPincode.setText("");
            etApartment.setText("");
        }
    }

    private void showApartmentSuggesions(String apartmet, String pincode) {
        Holders holders = AllApiClass.getInstance().getApi();
        Call<ApartmentNewModel> call = holders.getApartList(pincode,apartmet);
        call.enqueue(new Callback<ApartmentNewModel>() {
            @Override
            public void onResponse(Call<ApartmentNewModel> call, Response<ApartmentNewModel> response) {
                if (response.isSuccessful()){
                    if (response.body().getResponse().getCode().equals("1")){
                        aprtList.clear();
                        aprtList = new ArrayList<>();
                        aprtList = response.body().getResponse().getApartment_list();
                        if (aprtList.size() > 0){
                            adapter = new ApartAdapter(aprtList, NewRegistrationActivity.this);
                            LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
                            llm.setOrientation(LinearLayoutManager.VERTICAL);
                            rvApartment.setLayoutManager(llm);
                            rvApartment.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                            adapter.setOnItemClickListener(new OnItemClickListener() {
                                @Override
                                public void onItemClick(int position) {
                                    etApartment.setText(aprtList.get(position).getApartment_name());
                                    rvApartment.setVisibility(View.GONE);
                                }
                            });
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ApartmentNewModel> call, Throwable t) {

            }
        });
    }

    public void onBack(View view) {
        finish();
    }

}