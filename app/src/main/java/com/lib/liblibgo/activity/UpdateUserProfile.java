package com.lib.liblibgo.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.JsonObject;
import com.lib.liblibgo.R;
import com.lib.liblibgo.api.AllApiClass;
import com.lib.liblibgo.api.Holders;
import com.lib.liblibgo.common.Constants;
import com.lib.liblibgo.common.UserDatabase;
import com.lib.liblibgo.dashboard.apartment_admin.ImageGallery;
import com.lib.liblibgo.model.ApartmentListData;
import com.lib.liblibgo.model.ResModel;
import com.lib.liblibgo.model.SubmitDataResModel;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

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

public class UpdateUserProfile extends AppCompatActivity {

    private EditText mNameEt;
    private EditText mUserNameEt;
    private EditText mEmailEt;
    private EditText mPhoneEt;
    private EditText et_adr;
    private RelativeLayout mEmailLayout;
    private RelativeLayout mPhoneLayout;
    private TextView mApartmentTv;
    private TextView mFlatTv;

    private String flatId = "";
    private UserDatabase database;
    private Spinner mApartmentSp;

    private List<ApartmentListData> apartments = new ArrayList<>();
    List<ApartmentListData> temp_list = new ArrayList<>();
    private String apartId="";

    private TextView tv_flats;
    private TextView tvLibCoin;
    private ImageView ivFlats;
    public static CircleImageView iv_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_user_profile);

        database = new UserDatabase(this);

        mNameEt = (EditText)findViewById(R.id.et_fname);
        mUserNameEt = (EditText)findViewById(R.id.et_lname);
        mEmailEt = (EditText)findViewById(R.id.et_email);
        mPhoneEt = (EditText)findViewById(R.id.et_phone);
        et_adr = (EditText)findViewById(R.id.et_adr);

        mEmailEt.setEnabled(false);
        mPhoneEt.setEnabled(false);

        mEmailLayout = (RelativeLayout)findViewById(R.id.rlEmail);
        mPhoneLayout = (RelativeLayout)findViewById(R.id.rlPhone);

        mApartmentTv = (TextView)findViewById(R.id.tv_apartment);
        mFlatTv = (TextView)findViewById(R.id.tv_flats);
        tvLibCoin = (TextView)findViewById(R.id.tvLibCoin);

        mApartmentSp = (Spinner)findViewById(R.id.sp_apartment);

        tv_flats = (TextView)findViewById(R.id.tv_flats);
        ivFlats = (ImageView)findViewById(R.id.ivFlats);
        iv_user = (CircleImageView)findViewById(R.id.iv_user);

        Log.d("myImg",Constants.SelectedProfileImage);

        getUserDetails();

    }

    private void getUserDetails() {
        final ProgressDialog progressBar = new ProgressDialog(UpdateUserProfile.this);
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
                            mNameEt.setText(object.getString("username"));
                            //mUserNameEt.setText(object.getString("username"));
                            mEmailEt.setText(object.getString("email"));
                            mPhoneEt.setText(object.getString("mobile"));
                            tvLibCoin.setText("Lib Coins : "+object.getString("libcoins"));

                            apartId = object.getString("apartment_id");
                            String apartmentName = object.getString("apartment_name");
                            flatId = object.getString("flat_no");
                            String adr = object.getString("address");

                            mApartmentTv.setText(apartmentName);
                            mFlatTv.setText(flatId);
                            et_adr.setText(adr);

                            Constants.SelectedProfileImage =  object.getString("profile_image");


                            if (Constants.SelectedProfileImage.equals("")){
                                Glide.with(UpdateUserProfile.this).load(R.drawable.no_img).placeholder(R.drawable.no_img).into(iv_user);
                            }else {
                                Glide.with(UpdateUserProfile.this).load(Constants.SelectedProfileImage).placeholder(R.drawable.no_img).into(iv_user);
                            }

                            if (object.getString("login_type").equals("mobile")){
                                mPhoneLayout.setVisibility(View.VISIBLE);
                                mEmailLayout.setVisibility(View.GONE);
                            }else {
                                mPhoneLayout.setVisibility(View.GONE);
                                mEmailLayout.setVisibility(View.VISIBLE);
                            }

                        }else {
                            progressBar.dismiss();
                            Toast.makeText(UpdateUserProfile.this, ""+object.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else {
                    progressBar.dismiss();
                    Toast.makeText(UpdateUserProfile.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                    progressBar.dismiss();
                Toast.makeText(UpdateUserProfile.this, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void onSubmit(View view) {
        String name = mNameEt.getText().toString().trim();
        String adr = et_adr.getText().toString().trim();
        String email = mEmailEt.getText().toString().trim();
        String mobile = mPhoneEt.getText().toString().trim();

        if (name.equals("")){
            Toast.makeText(this, "Enter Name", Toast.LENGTH_SHORT).show();
        }else if (adr.equals("")){
            Toast.makeText(this, "Enter your address", Toast.LENGTH_SHORT).show();
        }else {
            saveUserDetails(name,email,mobile,adr);
        }

    }

    private void saveUserDetails(String name, String email, String mobile,String adr) {
        final ProgressDialog progressBar = new ProgressDialog(UpdateUserProfile.this);
        progressBar.setMessage("Please wait...");
        progressBar.setCancelable(false);
        progressBar.show();
        Holders holders = AllApiClass.getInstance().getApi();
        Call<ResModel> call = holders.updateProfileNew(database.getUserId(),name,email,mobile,adr);
        call.enqueue(new Callback<ResModel>() {
            @Override
            public void onResponse(Call<ResModel> call, Response<ResModel> response) {
                if (response.isSuccessful()){
                    if (response.body().getResponse().getCode() == 1){
                        progressBar.dismiss();
                        getUserDetails();
                    }else {
                        progressBar.dismiss();
                        Toast.makeText(UpdateUserProfile.this, ""+response.body().getResponse().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }else {
                    progressBar.dismiss();
                    Toast.makeText(UpdateUserProfile.this, "Something went wrong !", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResModel> call, Throwable t) {
                progressBar.dismiss();
                Toast.makeText(UpdateUserProfile.this, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void onContactUs(View view) {

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
        Intent intent = new Intent(UpdateUserProfile.this, ImageGallery.class);
        startActivityForResult(intent,1);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1){
            if (Constants.SelectedProfileImage.equals("")){
                Glide.with(this).load(R.drawable.no_img).placeholder(R.drawable.no_img).into(iv_user);
            }else {
                Glide.with(this).load(Constants.SelectedProfileImage).placeholder(R.drawable.no_img).into(iv_user);
            }
            uploadProfileImage();
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
                        Toast.makeText(UpdateUserProfile.this, ""+response.body().getResponse().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }else {
                    progressBar.dismiss();
                    Toast.makeText(UpdateUserProfile.this, "Something went wrong !", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<SubmitDataResModel> call, Throwable t) {
                progressBar.dismiss();
                Toast.makeText(UpdateUserProfile.this, "Check your internet !", Toast.LENGTH_SHORT).show();
            }
        });
    }
}