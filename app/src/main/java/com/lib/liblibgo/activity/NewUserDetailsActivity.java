package com.lib.liblibgo.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androdocs.httprequest.HttpRequest;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.lib.liblibgo.R;
import com.lib.liblibgo.activity.homedashboard.HomeActivity;
import com.lib.liblibgo.activity.homedashboard.fragmentshome.HomeFragment;
import com.lib.liblibgo.adapter.ApartAdapter;
import com.lib.liblibgo.api.AllApiClass;
import com.lib.liblibgo.api.Holders;
import com.lib.liblibgo.common.Constants;
import com.lib.liblibgo.common.UserCurrentLocation;
import com.lib.liblibgo.common.UserDatabase;
import com.lib.liblibgo.listner.OnItemClickListener;
import com.lib.liblibgo.model.ApartmentNewModel;
import com.lib.liblibgo.model.SaveDataModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewUserDetailsActivity extends AppCompatActivity {

    private Button btnSubmit;
    private EditText etName,etEmail,etPincode,etApartment,etFlat,etArea,etCity,etState,etLandmark;
    private RecyclerView rvApartment;
    private List<ApartmentNewModel.ApartNewRes.AprtList> aprtList = new ArrayList<>();
    private ApartAdapter adapter;
    List<Address> addresses;
    private TextView tvEmail;
    private String phoneNumber;
    private UserDatabase preferences;
    private int counter = 0;
    private LinearLayout llAllData;
    private String pinCode = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user_details);

        preferences = new UserDatabase(NewUserDetailsActivity.this);
        phoneNumber = getIntent().getStringExtra("phone");

        btnSubmit = (Button)findViewById(R.id.btnSubmit);

        etName = (EditText)findViewById(R.id.etName);
        etEmail = (EditText)findViewById(R.id.etEmail);
        tvEmail = (TextView)findViewById(R.id.tvEmail);
        etPincode = (EditText)findViewById(R.id.etPincode);
        etApartment = (EditText)findViewById(R.id.etApartment);
        etFlat = (EditText)findViewById(R.id.etFlat);
        etArea = (EditText)findViewById(R.id.etArea);
        etCity = (EditText)findViewById(R.id.etCity);
        etState = (EditText)findViewById(R.id.etState);
        etLandmark = (EditText)findViewById(R.id.etLandmark);
        llAllData = (LinearLayout)findViewById(R.id.llAllData);

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
                        Toast.makeText(NewUserDetailsActivity.this, "Enter your pincode first.", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    rvApartment.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        Dexter.withContext(this).withPermissions(Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                        showAddressDetails();
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                }).check();

        if (Constants.SOCIAL_USER_TYPE.equals("")){
            etEmail.setVisibility(View.GONE);
            tvEmail.setVisibility(View.GONE);
        }else {
            etEmail.setVisibility(View.VISIBLE);
            tvEmail.setVisibility(View.VISIBLE);
        }

        etEmail.setText(Constants.SOCIAL_USER_EMAIL);
        etName.setText(Constants.SOCIAL_USER_NAME);

        etPincode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                pinCode = charSequence.toString();
               etPincode.setBackground(getResources().getDrawable(R.drawable.bg_search));
               etName.setBackground(getResources().getDrawable(R.drawable.bg_search));
               etCity.setBackground(getResources().getDrawable(R.drawable.bg_search));
               etArea.setBackground(getResources().getDrawable(R.drawable.bg_search));
               etState.setBackground(getResources().getDrawable(R.drawable.bg_search));
                if (pinCode.length() >= 6){
                    //showAddress(pinCode);
                    new AddressTask().execute();
                    llAllData.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

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

                List<EditText> myInputFields = new ArrayList();
                myInputFields.add(etName);
                myInputFields.add(etPincode);
                myInputFields.add(etArea);
                myInputFields.add(etCity);
                myInputFields.add(etState);

                counter = 0;

                for (int i=0;i<myInputFields.size();i++){
                    if (myInputFields.get(i).getText().toString().equals("")){
                        myInputFields.get(i).setBackground(getResources().getDrawable(R.drawable.bg_error_fields));
                       // myInputFields.get(i).setError("Required");
                        //isRequiredFieldsEmpty = false;
                        counter ++;
                    }else {
                        myInputFields.get(i).setBackground(getResources().getDrawable(R.drawable.bg_search));
                        //Toast.makeText(UserProfileActivity.this, "Done", Toast.LENGTH_SHORT).show();
                    }
                    //break;
                }

                if (counter > 0){
                    return;
                }else {
                    if (pinCode.length() <= 5){
                        Toast.makeText(NewUserDetailsActivity.this, "Enter correct pincode.", Toast.LENGTH_SHORT).show();
                    }else {
                        if (Constants.SOCIAL_USER_TYPE.equals("")){
                            saveDetailsByPhoneNum(phoneNumber,name,pinCode,apartmentName,flat,area,city,state,landmark);
                        }else {
                            saveDetailsBySocial(Constants.SOCIAL_ID,name,email,pinCode,apartmentName,flat,area,city,state,landmark);
                        }
                    }

                }

                /*if (name.equals("")){
                    Toast.makeText(NewUserDetailsActivity.this, "Enter name", Toast.LENGTH_SHORT).show();
                }else if (pinCode.equals("")){
                    Toast.makeText(NewUserDetailsActivity.this, "Enter pincode", Toast.LENGTH_SHORT).show();
                }*//*else if (apartmentName.equals("")){
                    Toast.makeText(NewUserDetailsActivity.this, "Enter apartment", Toast.LENGTH_SHORT).show();
                }else if (flat.equals("")){
                    Toast.makeText(NewUserDetailsActivity.this, "Enter flat", Toast.LENGTH_SHORT).show();
                }*//*else if (area.equals("")){
                    Toast.makeText(NewUserDetailsActivity.this, "Enter city", Toast.LENGTH_SHORT).show();
                }else if (city.equals("")){
                    Toast.makeText(NewUserDetailsActivity.this, "Enter city", Toast.LENGTH_SHORT).show();
                }else if (state.equals("")){
                    Toast.makeText(NewUserDetailsActivity.this, "Enter state", Toast.LENGTH_SHORT).show();
                }else {
                    //showAlert(Constants.SOCIAL_ID,name,email,pinCode,apartmentName,flat,area,city,state,landmark);
                    if (Constants.SOCIAL_USER_TYPE.equals("")){
                        saveDetailsByPhoneNum(phoneNumber,name,pinCode,apartmentName,flat,area,city,state,landmark);
                    }else {
                        saveDetailsBySocial(Constants.SOCIAL_ID,name,email,pinCode,apartmentName,flat,area,city,state,landmark);
                    }
                }*/
            }
        });

    }

    class AddressTask extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... args) {
            String response = HttpRequest.excuteGet("http://www.postalpincode.in/api/pincode/" + pinCode);
            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            try {
                JSONObject jsonObj = new JSONObject(result);
                Log.d("myRes", jsonObj.toString());
                String status = jsonObj.getString("Status");
                JSONArray PostOffice = jsonObj.getJSONArray("PostOffice");
                JSONObject jsonObject = PostOffice.getJSONObject(0);
                String name = jsonObject.getString("Name");
                String circle = jsonObject.getString("Circle");
                String district = jsonObject.getString("District");
                String state = jsonObject.getString("State");
                etArea.setText(circle);
                etCity.setText(district);
                etState.setText(state);
            } catch (Exception e) {
                etArea.setText("");
                etCity.setText("");
                etState.setText("");
                Toast.makeText(NewUserDetailsActivity.this, "Error:" + e.toString(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void showAlert(String socialId, String name, String email, String pinCode, String apartmentName, String flat, String area, String city, String state, String landmark) {
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
                if (Constants.SOCIAL_USER_TYPE.equals("")){
                    saveDetailsByPhoneNum(phoneNumber,name,pinCode,apartmentName,flat,area,city,state,landmark);
                }else {
                    saveDetailsBySocial(Constants.SOCIAL_ID,name,email,pinCode,apartmentName,flat,area,city,state,landmark);
                }
            }
        });

    }

    private void saveDetailsBySocial(String socialId, String name, String email, String pinCode, String apartmentName, String flat, String area, String city, String state, String landmark) {
        final ProgressDialog loginDialog = new ProgressDialog(this);
        loginDialog.setMessage("Please wait..");
        loginDialog.setCancelable(false);
        loginDialog.show();
        Holders holders = AllApiClass.getInstance().getApi();
        Call<SaveDataModel> call = holders.saveUserDetailsBySocialLogin(socialId,name,email,apartmentName,flat,area,city,state,pinCode,landmark);
        call.enqueue(new Callback<SaveDataModel>() {
            @Override
            public void onResponse(Call<SaveDataModel> call, Response<SaveDataModel> response) {
                if (response.isSuccessful()){
                    if (response.body().getResponse().getCode().equals("1")){
                        //Log.d("myRes",response.toString());
                        loginDialog.dismiss();
                        preferences.createLogin(response.body().getResponse().getUser_id(),
                                response.body().getResponse().getUser_name(),
                                response.body().getResponse().getMobile(),response.body().getResponse().getPincode(),
                                response.body().getResponse().getAddress(),response.body().getResponse().getApartment_name());
                       /* Intent intent = new Intent(NewUserDetailsActivity.this,HomeActivity.class);
                        startActivity(intent);*/
                        HomeActivity.flag = 1;
                        HomeFragment.flag = 1;
                        finish();
                    }else {
                        loginDialog.dismiss();
                        Toast.makeText(NewUserDetailsActivity.this, ""+response.body().getResponse().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(NewUserDetailsActivity.this, "Something went wrong !", Toast.LENGTH_SHORT).show();
                    loginDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<SaveDataModel> call, Throwable t) {
                Toast.makeText(NewUserDetailsActivity.this, "Server not responding yet.Try again !", Toast.LENGTH_SHORT).show();
                loginDialog.dismiss();
            }
        });
    }

    private void saveDetailsByPhoneNum(String phoneNumber, String name, String pinCode, String apartmentName, String flat, String area, String city, String state, String landmark) {
        final ProgressDialog loginDialog = new ProgressDialog(this);
        loginDialog.setMessage("Please wait..");
        loginDialog.setCancelable(false);
        loginDialog.show();
        Holders holders = AllApiClass.getInstance().getApi();
        Call<SaveDataModel> call = holders.saveUserDetalsByPhone(name,phoneNumber,apartmentName,flat,area,city,state,pinCode,landmark);
        call.enqueue(new Callback<SaveDataModel>() {
            @Override
            public void onResponse(Call<SaveDataModel> call, Response<SaveDataModel> response) {
                if (response.isSuccessful()){
                    if (response.body().getResponse().getCode().equals("1")){
                        Log.d("myRes",response.toString());
                        loginDialog.dismiss();
                        preferences.createLogin(response.body().getResponse().getUser_id(),
                                response.body().getResponse().getUser_name(),
                                response.body().getResponse().getMobile(),response.body().getResponse().getPincode(),
                                response.body().getResponse().getAddress(),response.body().getResponse().getApartment_name());
                        /*Intent intent = new Intent(NewUserDetailsActivity.this,HomeActivity.class);
                        startActivity(intent);*/
                        HomeActivity.flag = 1;
                        HomeFragment.flag = 1;
                        finish();
                    }else {
                        loginDialog.dismiss();
                        Toast.makeText(NewUserDetailsActivity.this, ""+response.body().getResponse().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(NewUserDetailsActivity.this, "Something went wrong !", Toast.LENGTH_SHORT).show();
                    loginDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<SaveDataModel> call, Throwable t) {
                Toast.makeText(NewUserDetailsActivity.this, "Server not responding yet.Try again !", Toast.LENGTH_SHORT).show();
                loginDialog.dismiss();
            }
        });
    }

    private void showAddressDetails() {
        UserCurrentLocation location = new UserCurrentLocation(NewUserDetailsActivity.this);
        Log.d("locationnn",""+location.latitude);
        Log.d("locationnn",""+location.longitude);
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            addresses = geocoder.getFromLocation(location.latitude, location.longitude, 2);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (!addresses.isEmpty() || addresses.size() > 0){
            String city = addresses.get(0).getSubAdminArea();
            String state = addresses.get(0).getAdminArea();
            String country = addresses.get(0).getCountryName();
            String postalCode = addresses.get(0).getPostalCode();
            String apartmentValue = addresses.get(0).getThoroughfare();
            String knownName = addresses.get(0).getLocality();// Only if available else return NULL
            //adr = knownName+","+city+","+state+","+country+","+postalCode;
            etPincode.setText(postalCode);
            etCity.setText(city);
            etState.setText(state);
            etApartment.setText(apartmentValue);
            etArea.setText(knownName);

            if (postalCode.equals("")){
                llAllData.setVisibility(View.GONE);
            }else {
                llAllData.setVisibility(View.VISIBLE);
            }

        }else if (addresses.size() > 1){
            String city = addresses.get(1).getSubAdminArea();
            String state = addresses.get(1).getAdminArea();
            String country = addresses.get(1).getCountryName();
            String postalCode = addresses.get(0).getPostalCode();
            String apartmentValue = addresses.get(1).getThoroughfare();
            String knownName = addresses.get(1).getLocality();// Only if available else return NULL
            //adr = knownName+","+city+","+state+","+country+","+postalCode;
            etPincode.setText(postalCode);
            etCity.setText(city);
            etState.setText(state);
            etApartment.setText(apartmentValue);
            etArea.setText(knownName);

            if (postalCode.equals("")){
                llAllData.setVisibility(View.GONE);
            }else {
                llAllData.setVisibility(View.VISIBLE);
            }

        }else {
            etArea.setText("");
            etCity.setText("");
            etState.setText("");
            etPincode.setText("");
            llAllData.setVisibility(View.GONE);
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
                            adapter = new ApartAdapter(aprtList, NewUserDetailsActivity.this);
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