package com.lib.liblibgo.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.lib.liblibgo.R;
import com.lib.liblibgo.activity.homedashboard.HomeActivity;
import com.lib.liblibgo.adapter.ApartmentAdapterNew;
import com.lib.liblibgo.api.AllApiClass;
import com.lib.liblibgo.api.Holders;
import com.lib.liblibgo.common.Constants;
import com.lib.liblibgo.common.UserDatabase;
import com.lib.liblibgo.dialogs.FlatDialog;
import com.lib.liblibgo.model.ApartmentListData;
import com.lib.liblibgo.model.ApartmentModelNew;
import com.lib.liblibgo.model.FlatListData;
import com.lib.liblibgo.model.FlatModel;
import com.lib.liblibgo.model.SaveDataModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserDetails extends AppCompatActivity {

    private Spinner mApartmentSp;
    private List<ApartmentListData> apartments = new ArrayList<>();
    List<ApartmentListData> temp_list = new ArrayList<>();
    private String apartId="";

    private Spinner mFlatSp;
    private List<FlatListData> flats = new ArrayList<>();
    List<FlatListData> temp_flatlist = new ArrayList<>();
    private String flatId="";

    private EditText et_fname;
    private EditText et_lname;
    private EditText et_email;
    private String phone;
    private UserDatabase preferences;
    private TextView tv_flats;
    private ImageView ivFlats;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);
        preferences = new UserDatabase(UserDetails.this);

        mApartmentSp = (Spinner)findViewById(R.id.sp_apartment);
        mFlatSp = (Spinner)findViewById(R.id.sp_flat);

        et_fname = (EditText)findViewById(R.id.et_fname);
        et_lname = (EditText)findViewById(R.id.et_lname);
        et_email = (EditText)findViewById(R.id.et_email);

        phone = getIntent().getStringExtra("phone");

        et_fname.setText(Constants.SOCIAL_USER_NAME);
        et_email.setText(Constants.SOCIAL_USER_EMAIL);

        tv_flats = (TextView)findViewById(R.id.tv_flats);
        ivFlats = (ImageView)findViewById(R.id.ivFlats);

        getApartments();

        ivFlats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (apartId.equals("")){
                    Toast.makeText(UserDetails.this, "Select Apartment First.", Toast.LENGTH_SHORT).show();
                }else {
                    FlatDialog flatDialog =
                            FlatDialog.newInstance();
                    flatDialog.show(getSupportFragmentManager(),
                            "flat_dialog_fragment");

                    Constants.USER_DETAIL_PAGE_TYPE = "0";
                }
            }
        });

        tv_flats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (apartId.equals("")){
                    Toast.makeText(UserDetails.this, "Select Apartment First.", Toast.LENGTH_SHORT).show();
                }else {
                    FlatDialog flatDialog =
                            FlatDialog.newInstance();
                    flatDialog.show(getSupportFragmentManager(),
                            "flat_dialog_fragment");

                    Constants.USER_DETAIL_PAGE_TYPE = "0";
                }
            }
        });

    }

    private void getApartments() {
        Holders holders = AllApiClass.getInstance().getApi();
        Call<ApartmentModelNew> resCall = holders.apartmentListNew();
        resCall.enqueue(new Callback<ApartmentModelNew>() {
            @Override
            public void onResponse(Call<ApartmentModelNew> call, Response<ApartmentModelNew> response) {
                if (response.isSuccessful()) {
                    if (response.body().getResponse().getCode() == 1) {
                        if (response.body().getResponse().getApartment_list() != null) {
                            apartments = response.body().getResponse().getApartment_list();
                            ApartmentListData data = new ApartmentListData();
                            data.setApartmentId("");
                            data.setApartmentName("Select Apartment");
                            temp_list.add(data);
                            for (int i=0;i<apartments.size();i++){
                                temp_list.add(apartments.get(i));
                            }
                            mApartmentSp.setAdapter(new ApartmentAdapterNew(getApplicationContext(),temp_list));

                            mApartmentSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                    if (i != 0 || !temp_list.get(i).getApartmentName().equals("Select Apartment")){
                                        apartId = temp_list.get(i).getApartmentId();
                                        Constants.AprtmentId = apartId;
                                        //getFlatList(apartId);
                                    }else {
                                        apartId = "";
                                    }

                                    //Toast.makeText(UserDetails.this, ""+apartId, Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> adapterView) {
                                }
                            });

                        }
                    }
                }

            }

            @Override
            public void onFailure(Call<ApartmentModelNew> call, Throwable t) {
                Toast.makeText(UserDetails.this, "Try again", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getFlatList(String apartId) {
        Holders holders = AllApiClass.getInstance().getApi();
        Call<FlatModel> resCall = holders.getFlatByApartment(apartId,"20");
        resCall.enqueue(new Callback<FlatModel>() {
            @Override
            public void onResponse(Call<FlatModel> call, Response<FlatModel> response) {
                if (response.isSuccessful()) {
                    if (response.body().getResponse().getCode() == 10) {
                        if (response.body().getResponse().getFlat_list() != null) {
                            flats = response.body().getResponse().getFlat_list();

                            Constants.flatList = flats;
                            Constants.AprtmentId = apartId;

                        }
                    }
                }

            }

            @Override
            public void onFailure(Call<FlatModel> call, Throwable t) {
                Toast.makeText(UserDetails.this, "Try again", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void onSubmit(View view) {
        String fName = et_fname.getText().toString().trim();
        String lName = et_lname.getText().toString().trim();
        String email = et_email.getText().toString().trim();

        if (fName.equals("")){
            Toast.makeText(this, "Enter name name.", Toast.LENGTH_SHORT).show();
        }else if (lName.equals("")){
            Toast.makeText(this, "Enter unique id.", Toast.LENGTH_SHORT).show();
        }else if (apartId.equals("")){
            Toast.makeText(this, "Select Apartment.", Toast.LENGTH_SHORT).show();
        }else if (flatId.equals("")){
            Toast.makeText(this, "Select Door Number.", Toast.LENGTH_SHORT).show();
        }else {
            if (Constants.SOCIAL_USER_TYPE.equals("")){
                saveData(phone,fName,lName,email,apartId,flatId);
            }else {
                saveDataBySocial(Constants.SOCIAL_ID,fName,lName,email,apartId,flatId);
            }

        }
    }

    private void saveDataBySocial(String phoneNo,String fName, String lName, String email, String apartId, String flatId) {
        final ProgressDialog loginDialog = new ProgressDialog(this);
        loginDialog.setMessage("Please wait..");
        loginDialog.setCancelable(false);
        loginDialog.show();
        Holders holders = AllApiClass.getInstance().getApi();
        Call<SaveDataModel> call = holders.saveUserDetailsBySocial(phoneNo,fName,lName,email,apartId,flatId);
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
                        Intent intent = new Intent(UserDetails.this, HomeActivity.class);
                        startActivity(intent);
                        finish();
                    }else {
                        loginDialog.dismiss();
                        Toast.makeText(UserDetails.this, ""+response.body().getResponse().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(UserDetails.this, "Something went wrong !", Toast.LENGTH_SHORT).show();
                    loginDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<SaveDataModel> call, Throwable t) {
                Toast.makeText(UserDetails.this, "Server not responding yet.Try again !", Toast.LENGTH_SHORT).show();
                loginDialog.dismiss();
            }
        });

    }

    private void saveData(String phoneNo,String fName, String lName, String email, String apartId, String flatId) {
        final ProgressDialog loginDialog = new ProgressDialog(this);
        loginDialog.setMessage("Please wait..");
        loginDialog.setCancelable(false);
        loginDialog.show();
        Holders holders = AllApiClass.getInstance().getApi();
        Call<SaveDataModel> call = holders.saveUserDetails(phoneNo,fName,lName,email,apartId,flatId);
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
                        Intent intent = new Intent(UserDetails.this,HomeActivity.class);
                        startActivity(intent);
                        finish();
                    }else {
                        loginDialog.dismiss();
                        Toast.makeText(UserDetails.this, ""+response.body().getResponse().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(UserDetails.this, "Something went wrong !", Toast.LENGTH_SHORT).show();
                    loginDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<SaveDataModel> call, Throwable t) {
                Toast.makeText(UserDetails.this, "Server not responding yet.Try again !", Toast.LENGTH_SHORT).show();
                loginDialog.dismiss();
            }
        });

    }

    public void onContactUs(View view) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:"));
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"abc@gmail.com"});
        intent.putExtra(Intent.EXTRA_SUBJECT, "Contact For New Apartment");
        intent.putExtra(Intent.EXTRA_TEXT,"");
        startActivity(intent);
    }

    public void setUserData(String flatName, String flat_id) {
        //Toast.makeText(this, ""+countryName+"\n"+countryCode+"\n"+countryDialCode, Toast.LENGTH_SHORT).show();
        tv_flats.setText(flatName);
        flatId = flat_id;
    }
}