package com.lib.liblibgo.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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

import com.bumptech.glide.Glide;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.gson.JsonObject;
import com.lib.liblibgo.R;
import com.lib.liblibgo.activity.homedashboard.HomeActivity;
import com.lib.liblibgo.adapter.ApartAdapter;
import com.lib.liblibgo.api.AllApiClass;
import com.lib.liblibgo.api.Holders;
import com.lib.liblibgo.common.Constants;
import com.lib.liblibgo.common.UserDatabase;
import com.lib.liblibgo.dashboard.apartment_admin.ImageGallery;
import com.lib.liblibgo.dashboard.book_details.CheckoutActivity;
import com.lib.liblibgo.listner.OnItemClickListener;
import com.lib.liblibgo.model.ApartmentNewModel;
import com.lib.liblibgo.model.SaveDataModel;
import com.lib.liblibgo.model.SubmitDataResModel;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserProfileActivity extends AppCompatActivity implements PaymentResultListener {

    private CircleImageView iv_user;
    private TextView tvLibCoin,tvSecureLibCoin;

    private Button btnSubmit;
    private EditText etName,etEmail,etPincode,etApartment,etFlat,etArea,etCity,etState,etLandmark;
    private RecyclerView rvApartment;
    private List<ApartmentNewModel.ApartNewRes.AprtList> aprtList = new ArrayList<>();
    private ApartAdapter adapter;
    private EditText etPhone;
    private UserDatabase database;
    private String apartId = "";
    private String flatId = "";
    private String libCoins = "";
    public static int flag = 0;
    private boolean isRequiredFieldsEmpty = false;
    private int counter = 0;
    private String transactionId = "";
    private String enteredLibCoin = "";
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog alertDialog;
    private String upiStatus = "0";
    private Button btnRedeem;
    private Button btnDeleteAccount;
    private SharedPreferences sharedpreferences;
    private SharedPreferences.Editor editor;
    private Button btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        database = new UserDatabase(this);
        sharedpreferences = getSharedPreferences(HomeActivity.MyPREFERENCES, Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();

        iv_user = (CircleImageView)findViewById(R.id.iv_user);
        tvLibCoin = (TextView)findViewById(R.id.tvLibCoin);
        tvSecureLibCoin = (TextView)findViewById(R.id.tvSecureLibCoin);
        Log.d("myImg",Constants.SelectedProfileImage);

        btnSubmit = (Button)findViewById(R.id.btnSubmit);
        btnRedeem = (Button)findViewById(R.id.btnRedeem);
        btnDeleteAccount = (Button)findViewById(R.id.btnDeleteAccount);
        btnLogout = (Button)findViewById(R.id.btnLogout);

        etName = (EditText)findViewById(R.id.etName);
        etEmail = (EditText)findViewById(R.id.etEmail);
        etPhone = (EditText)findViewById(R.id.etPhone);
        etPincode = (EditText)findViewById(R.id.etPincode);
        etApartment = (EditText)findViewById(R.id.etApartment);
        etFlat = (EditText)findViewById(R.id.etFlat);
        etArea = (EditText)findViewById(R.id.etArea);
        etCity = (EditText)findViewById(R.id.etCity);
        etState = (EditText)findViewById(R.id.etState);
        etLandmark = (EditText)findViewById(R.id.etLandmark);

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
                        //Toast.makeText(UserProfileActivity.this, "Enter your pincode first.", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    rvApartment.setVisibility(View.GONE);
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
                String phone = etPhone.getText().toString().trim();
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
                        //myInputFields.get(i).setError("Required");
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
                    //Toast.makeText(UserProfileActivity.this, "Done", Toast.LENGTH_SHORT).show();
                    saveDetails(name,email,phone,pinCode,apartmentName,flat,area,city,state,landmark);
                }

                /*if (name.equals("")){
                    Toast.makeText(UserProfileActivity.this, "Enter your name.", Toast.LENGTH_SHORT).show();
                }else if (pinCode.equals("")){
                    Toast.makeText(UserProfileActivity.this, "Enter your pincode.", Toast.LENGTH_SHORT).show();
                }else if (area.equals("")){
                    Toast.makeText(UserProfileActivity.this, "Enter your area.", Toast.LENGTH_SHORT).show();
                }else if (city.equals("")){
                    Toast.makeText(UserProfileActivity.this, "Enter your city.", Toast.LENGTH_SHORT).show();
                }else if (state.equals("")){
                    Toast.makeText(UserProfileActivity.this, "Enter your state.", Toast.LENGTH_SHORT).show();
                }else {
                    //showAlert(name,email,phone,pinCode,apartmentName,flat,area,city,state,landmark);
                    saveDetails(name,email,phone,pinCode,apartmentName,flat,area,city,state,landmark);
                }*/
            }
        });

        getUserDetails();

        btnDeleteAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(UserProfileActivity.this);
                LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View layoutView = inflater.inflate(R.layout.delete_account_popup, null);
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
                        deleteMyAccount();
                    }
                });

            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final ProgressDialog loginDialog = new ProgressDialog(UserProfileActivity.this);
                loginDialog.setMessage("Please wait..");
                loginDialog.setCancelable(false);
                loginDialog.show();
                Holders holders = AllApiClass.getInstance().getApi();
                Call<SubmitDataResModel> call = holders.userLogout(database.getUserId());
                call.enqueue(new Callback<SubmitDataResModel>() {
                    @Override
                    public void onResponse(Call<SubmitDataResModel> call, Response<SubmitDataResModel> response) {
                        if (response.isSuccessful()){
                            if (response.body().getResponse().getCode() == 1){
                                loginDialog.dismiss();
                                database.clearSession();
                                editor.clear();
                                editor.commit();
                                LoginManager.getInstance().logOut();
                                GoogleSignInOptions gso = new GoogleSignInOptions.
                                        Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).
                                        build();
                                GoogleSignInClient googleSignInClient= GoogleSignIn.getClient(UserProfileActivity.this,gso);
                                googleSignInClient.signOut();
                                Intent intentHome = new Intent(UserProfileActivity.this, HomeActivity.class);
                                intentHome.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intentHome);
                                finish();
                            }else {
                                loginDialog.dismiss();
                                Toast.makeText(UserProfileActivity.this, response.body().getResponse().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }else {
                            loginDialog.dismiss();
                            Toast.makeText(UserProfileActivity.this, "Something went wrong !", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<SubmitDataResModel> call, Throwable t) {
                        loginDialog.dismiss();
                        Toast.makeText(UserProfileActivity.this, "Check Your Internet Connection.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }

    private void deleteMyAccount() {
        final ProgressDialog progressBar = new ProgressDialog(UserProfileActivity.this);
        progressBar.setMessage("Please wait...");
        progressBar.setCancelable(false);
        progressBar.show();
        Holders holders = AllApiClass.getInstance().getApi();
        Call<SubmitDataResModel> resCall = holders.deleteAccount(database.getUserId());
        resCall.enqueue(new Callback<SubmitDataResModel>() {
            @Override
            public void onResponse(Call<SubmitDataResModel> call, Response<SubmitDataResModel> response) {
                if (response.isSuccessful()){
                    if (response.body().getResponse().getCode() == 1){
                        Toast.makeText(UserProfileActivity.this, "Account deleted successfully.", Toast.LENGTH_SHORT).show();
                        progressBar.dismiss();
                        database.clearSession();
                        editor.clear();
                        editor.commit();
                        LoginManager.getInstance().logOut();
                        GoogleSignInOptions gso = new GoogleSignInOptions.
                                Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).
                                build();
                        GoogleSignInClient googleSignInClient= GoogleSignIn.getClient(UserProfileActivity.this,gso);
                        googleSignInClient.signOut();
                        Intent intentHome = new Intent(UserProfileActivity.this, HomeActivity.class);
                        intentHome.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intentHome);
                        finishAffinity();
                    }else {
                        progressBar.dismiss();
                        Toast.makeText(UserProfileActivity.this, ""+response.body().getResponse().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }else {
                    progressBar.dismiss();
                    Toast.makeText(UserProfileActivity.this, "Something went wrong !", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<SubmitDataResModel> call, Throwable t) {
                progressBar.dismiss();
                Toast.makeText(UserProfileActivity.this, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
}

    private void showAlert(String name, String email, String phone, String pinCode, String apartmentName, String flat, String area, String city, String state, String landmark) {
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
        alertDialog.getWindow().setLayout(650, ViewGroup.LayoutParams.WRAP_CONTENT);

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
                saveDetails(name,email,phone,pinCode,apartmentName,flat,area,city,state,landmark);
            }
        });
    }

    private void saveDetails(String name, String email, String phone, String pinCode, String apartmentName, String flat, String area, String city, String state, String landmark) {
        final ProgressDialog loginDialog = new ProgressDialog(this);
        loginDialog.setMessage("Please wait..");
        loginDialog.setCancelable(false);
        loginDialog.show();
        Holders holders = AllApiClass.getInstance().getApi();
        Call<SaveDataModel> call = holders.updateUserProfileDetails(database.getUserId(),name,email,phone,area,city,state,pinCode,landmark,flat,apartmentName);
        call.enqueue(new Callback<SaveDataModel>() {
            @Override
            public void onResponse(Call<SaveDataModel> call, Response<SaveDataModel> response) {
                if (response.isSuccessful()){
                    if (response.body().getResponse().getCode().equals("1")){
                        Log.d("myRes",response.toString());
                        loginDialog.dismiss();
                        database.createLogin(response.body().getResponse().getUser_id(),
                                response.body().getResponse().getUser_name(),
                                response.body().getResponse().getMobile(),response.body().getResponse().getPincode(),
                                response.body().getResponse().getAddress(),response.body().getResponse().getApartment_name());
                        Intent intent = new Intent(UserProfileActivity.this, HomeActivity.class);
                        startActivity(intent);
                        finish();
                    }else {
                        loginDialog.dismiss();
                        Toast.makeText(UserProfileActivity.this, ""+response.body().getResponse().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(UserProfileActivity.this, "Something went wrong !", Toast.LENGTH_SHORT).show();
                    loginDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<SaveDataModel> call, Throwable t) {
                Toast.makeText(UserProfileActivity.this, "Server not responding yet.Try again !", Toast.LENGTH_SHORT).show();
                loginDialog.dismiss();
            }
        });
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
                            adapter = new ApartAdapter(aprtList, UserProfileActivity.this);
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

    private void getUserDetails() {
        final ProgressDialog progressBar = new ProgressDialog(UserProfileActivity.this);
        progressBar.setMessage("Please wait...");
        progressBar.setCancelable(false);
        progressBar.show();
        Holders holders = AllApiClass.getInstance().getApi();
        Call<JsonObject> call = holders.userDetails(database.getUserId());
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()){
                    try {
                        progressBar.dismiss();
                        JSONObject jsonObject = new JSONObject(response.body().toString());
                        JSONObject object = jsonObject.getJSONObject("response");
                        if (object.getString("code").equals("1")){
                            etName.setText(object.getString("username"));
                            //mUserNameEt.setText(object.getString("username"));
                            etEmail.setText(object.getString("email"));
                            etPhone.setText(object.getString("mobile"));
                            libCoins = object.getString("libcoins");
                            tvLibCoin.setText("Lib Coins : "+libCoins);
                            tvSecureLibCoin.setText("Secure Lib Coins : "+object.getString("secure_libcoins"));

                            apartId = object.getString("apartment_id");
                            String apartmentName = object.getString("apartment_name");
                            flatId = object.getString("flat_no");
                            String adr = object.getString("address");
                            upiStatus = object.getString("upi_status");

                            etApartment.setText(apartmentName);
                            etFlat.setText(flatId);
                            //et_adr.setText(adr);
                            etArea.setText(object.getString("area_name"));
                            etPincode.setText(object.getString("pincode"));
                            etCity.setText(object.getString("city"));
                            etState.setText(object.getString("state"));
                            etLandmark.setText(object.getString("landmark"));

                            Constants.SelectedProfileImage =  object.getString("profile_image");

                            if (Constants.SelectedProfileImage.equals("")){
                                Glide.with(UserProfileActivity.this).load(R.drawable.no_img).placeholder(R.drawable.no_img).into(iv_user);
                            }else {
                                Glide.with(UserProfileActivity.this).load(Constants.SelectedProfileImage).placeholder(R.drawable.no_img).into(iv_user);
                            }

                            if (object.getString("login_type").equals("mobile")){
                               etPhone.setEnabled(false);
                            }else if (object.getString("login_type").equals("email")){
                                etEmail.setEnabled(false);
                            }else if (object.getString("login_type").equals("google")){
                                etEmail.setEnabled(false);
                            }else {
                                etEmail.setEnabled(true);
                                etPhone.setEnabled(true);
                            }

                            database.createLogin(database.getUserId(),
                                    object.getString("username"),
                                    object.getString("mobile"),object.getString("pincode"),
                                    object.getString("area_name")+","+object.getString("landmark")+","+object.getString("city")+","+object.getString("state")+","+object.getString("pincode"),
                                    apartmentName,flatId,object.getString("email"));

                            btnRedeem.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if (libCoins.equals("0")){
                                        Toast.makeText(UserProfileActivity.this, "You don't have libcoin.", Toast.LENGTH_SHORT).show();
                                    }else {
                                        showRedeemPopup(R.layout.redeem_lib_coin_popup);
                                    }

                                }
                            });

                        }else {
                            progressBar.dismiss();
                            Toast.makeText(UserProfileActivity.this, ""+object.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else {
                    progressBar.dismiss();
                    Toast.makeText(UserProfileActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                progressBar.dismiss();
                Toast.makeText(UserProfileActivity.this, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void onBack(View view) {
        onBackPressed();
    }

    public void uploadImage(View view) {
        Dexter.withContext(this).withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                        chooseImage();
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                }).check();
    }

    private void chooseImage() {
        Constants.openPageFrom = "fromUserProfile";
        Intent intent = new Intent(UserProfileActivity.this, ImageGallery.class);
        startActivityForResult(intent,1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1){
            if (Constants.SelectedProfileImage.equals("")){
                Glide.with(this).load(R.drawable.no_img).placeholder(R.drawable.no_img).into(iv_user);
            }else {
                Glide.with(this).load(Constants.SelectedProfileImage).placeholder(R.drawable.no_img).into(iv_user);
                uploadProfileImage();
            }
        }
    }

    private void uploadProfileImage() {
        final ProgressDialog progressBar = new ProgressDialog(this);
        progressBar.setMessage("Please wait...");
        progressBar.setCancelable(false);
        progressBar.show();
        Holders holders = AllApiClass.getInstance().getApi();

        MultipartBody.Part part = null;
        try {
            File file = new File(Constants.SelectedProfileImage);
            RequestBody fileReqBody = RequestBody.create(MediaType.parse("image/*"), file);
            part = MultipartBody.Part.createFormData("image", file.getName(), fileReqBody);
        }catch (Exception e){
            e.printStackTrace();
        }
        Log.d("myFilePath",""+part);
        RequestBody userIdReq = RequestBody.create(MediaType.parse("multipart/form-data"), database.getUserId());

        Call<SubmitDataResModel> resCall = holders.uploadProfileImage(userIdReq,part);
        resCall.enqueue(new Callback<SubmitDataResModel>() {
            @Override
            public void onResponse(Call<SubmitDataResModel> call, Response<SubmitDataResModel> response) {
                if (response.isSuccessful()){
                    if (response.body().getResponse().getCode() == 1){
                        progressBar.dismiss();
                        getUserDetails();
                    }else {
                        progressBar.dismiss();
                        Toast.makeText(UserProfileActivity.this, ""+response.body().getResponse().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }else {
                    progressBar.dismiss();
                    Toast.makeText(UserProfileActivity.this, "Something went wrong !", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<SubmitDataResModel> call, Throwable t) {
                progressBar.dismiss();
                Toast.makeText(UserProfileActivity.this, "Check your internet !", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void showRedeemPopup(int layout) {
        dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View layoutView = inflater.inflate(layout, null);
        final Button btn_done = layoutView.findViewById(R.id.btn_done);
        final TextView tv_txt = layoutView.findViewById(R.id.tv_txt);
        final EditText et_lib_coin = layoutView.findViewById(R.id.et_lib_coin);

        tv_txt.setText("You have "+libCoins+" Lib coins."+"\n"+"Enter how many lib coins you want to redeem.");
        et_lib_coin.setText(libCoins);

        dialogBuilder.setView(layoutView);
        alertDialog = dialogBuilder.create();
        alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();
        alertDialog.setCancelable(true);
        alertDialog.getWindow().setLayout(700, ViewGroup.LayoutParams.WRAP_CONTENT);

        btn_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enteredLibCoin = et_lib_coin.getText().toString().trim();
                if (!enteredLibCoin.equals("") || enteredLibCoin.equals("0")){
                    if (Integer.parseInt(libCoins) >= Integer.parseInt(enteredLibCoin)){
                        if (upiStatus.equals("0")){
                            gotoPaymentMethod();
                        }else {
                            transactionId = "";
                            createOrder(transactionId);
                        }
                    }else {
                        Toast.makeText(UserProfileActivity.this, "You have only "+libCoins + " Lib coins.", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(UserProfileActivity.this, "You don't have lib coins to redeem.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void gotoPaymentMethod() {
        Checkout checkout = new Checkout();
        //checkout.setImage(R.drawable.logo);
        final Activity activity = this;

        try {
            JSONObject options = new JSONObject();

            options.put("name", "LibLibGo");
            options.put("description", "is a one stop solution for your library management.");
            //options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png");
            //options.put("order_id", "order_9A33XWu170gUtm");
            options.put("currency", "INR");

            double amount = 1;
            amount = amount * 100;

            options.put("amount", amount);

            JSONObject preFill  = new JSONObject();
            //JSONObject methodObj  = new JSONObject();
            //methodObj.put("netbanking", 0);
            //methodObj.put("card", 0);
           // methodObj.put("upi", 1);
            //methodObj.put("wallet", 0);
            preFill.put("method", "upi");
            preFill.put("email","libpayment@gmail.com");
            preFill.put("contact",database.getPHONE());

            options.put("prefill",preFill );
            //Log.d("deductedLibCoins",deductedLibCoins);
            checkout.open(activity, options);
        } catch(Exception e) {
            Toast.makeText(UserProfileActivity.this, "Error in payment : "+e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.e("ErrorMessage", "Error in starting Razorpay Checkout", e);
        }
    }

    @Override
    protected void onResume() {
        if (flag == 1){
            getUserDetails();
            flag = 0;
        }
        super.onResume();
    }

    @Override
    public void onPaymentSuccess(String s) {
        transactionId = s;
        createOrder(transactionId);
    }

    private void createOrder(String transactionId) {
        final ProgressDialog progressBar = new ProgressDialog(UserProfileActivity.this);
        progressBar.setMessage("Please wait...");
        progressBar.setCancelable(false);
        progressBar.show();
        Holders holders = AllApiClass.getInstance().getApi();
        Call<SubmitDataResModel> resCall = holders.requestForRedeem(database.getUserId(),enteredLibCoin,transactionId);
        resCall.enqueue(new Callback<SubmitDataResModel>() {
            @Override
            public void onResponse(Call<SubmitDataResModel> call, Response<SubmitDataResModel> response) {
                if (response.isSuccessful()){
                    progressBar.dismiss();
                    alertDialog.dismiss();
                    getUserDetails();
                    //tvLibCoin.setText("Lib Coins : "+ (Integer.parseInt(libCoins) - Integer.parseInt(enteredLibCoin)));
                    Toast.makeText(UserProfileActivity.this, "Redeem request has been sent.", Toast.LENGTH_SHORT).show();
                    /*if (response.body().getResponse().getCode() == 1){

                    }else {
                        alertDialog.dismiss();
                        progressBar.dismiss();
                        getUserDetails();
                        Toast.makeText(UserProfileActivity.this, ""+response.body().getResponse().getMessage(), Toast.LENGTH_SHORT).show();
                    }*/
                }else {
                    progressBar.dismiss();
                    Toast.makeText(UserProfileActivity.this, "Something went wrong !", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<SubmitDataResModel> call, Throwable t) {
                progressBar.dismiss();
                Toast.makeText(UserProfileActivity.this, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onPaymentError(int i, String s) {
        try {
            Toast.makeText(this, "Payment Failed. Please Try Again.", Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}