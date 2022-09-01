package com.lib.liblibgo.dashboard.library;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.location.Address;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.lib.liblibgo.R;
import com.lib.liblibgo.api.AllApiClass;
import com.lib.liblibgo.api.Holders;
import com.lib.liblibgo.common.Constants;
import com.lib.liblibgo.common.UserCurrentLocation;
import com.lib.liblibgo.common.UserDatabase;
import com.lib.liblibgo.dashboard.apartment_admin.ImageGallery;
import com.lib.liblibgo.dashboard.apartment_admin.SubAdminDashboard;
import com.lib.liblibgo.model.SubmitDataResModel;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.io.File;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateLibraryActivity extends AppCompatActivity {
    private TextView titleTool;
    private ImageView backTool;
    private EditText mEtAddress;
    private EditText et_Username;
    private EditText et_library_name;
    private EditText et_email;
    private TextView tv_apartment;
    private TextView tv_flats;
    private UserDatabase database;
    List<Address> addresses;
    private String adr = "";
    private ImageView iv_adr_close;
    private CircleImageView iv_user;
    private CheckBox cbComunity;
    private String isShelfPickup = "0";
    //private String allowContact = "0";
    private RelativeLayout rlPhoneView;
    private EditText et_phone;
    private CheckBox cbAll;
    private String isOwnLirary="";
    private String phoneNum = "";
    private TextView tvNoteMsg;
    private String selfPickupFor = "";
    private RelativeLayout rlApartment;
    private RelativeLayout rlFlat;
    private TextView tvTxt;
    private LinearLayout llSelf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_library);

        database = new UserDatabase(this);

        titleTool = findViewById(R.id.titleTool);
        backTool = findViewById(R.id.backTool);
        setTopView(getString(R.string.create_library));

        mEtAddress = (EditText)findViewById(R.id.et_adr);
        et_Username = (EditText)findViewById(R.id.et_fname);
        et_library_name = (EditText)findViewById(R.id.et_library_name);
        et_email = (EditText)findViewById(R.id.et_email);
        et_phone = (EditText)findViewById(R.id.et_phone);
        tv_apartment = (TextView)findViewById(R.id.tv_apartment);
        tv_flats = (TextView)findViewById(R.id.tv_flats);
        iv_adr_close = (ImageView)findViewById(R.id.iv_adr_close);
        iv_user = (CircleImageView)findViewById(R.id.iv_user);
        rlPhoneView = (RelativeLayout) findViewById(R.id.rlPhoneView);

        rlApartment = (RelativeLayout) findViewById(R.id.rlApartment);
        rlFlat = (RelativeLayout) findViewById(R.id.rlFlat);

        cbComunity = (CheckBox)findViewById(R.id.cbComunity);
        cbAll = (CheckBox)findViewById(R.id.cbAll);
        tvNoteMsg = (TextView)findViewById(R.id.tvNoteMsg);

        tvTxt = (TextView)findViewById(R.id.tvTxt);
        llSelf = (LinearLayout)findViewById(R.id.llSelf);

        //Toast.makeText(this, ""+Constants.libraryType, Toast.LENGTH_SHORT).show();
        //mEtAddress.setText("Mahindra Alias Babugram,Raghunathpur,Purulia,West Bengal, 723133");
        et_Username.setText(database.getNAME());
        tv_apartment.setText(database.getApartmentName());
        tv_flats.setText(database.getFLAT());
        et_email.setText(database.getEMAIL());
        if (database.getPHONE().equals("null")){
            et_phone.setText("");
        }else {
            et_phone.setText(database.getPHONE());
        }

        if (Constants.isOwnLibrary == true){
            isOwnLirary = "1";
            rlApartment.setVisibility(View.GONE);
            rlFlat.setVisibility(View.GONE);
            tvTxt.setVisibility(View.VISIBLE);
            llSelf.setVisibility(View.VISIBLE);
        }else {
            isOwnLirary = "0";
            rlApartment.setVisibility(View.GONE);
            rlFlat.setVisibility(View.GONE);
            tvTxt.setVisibility(View.GONE);
            llSelf.setVisibility(View.GONE);
        }

        Dexter.withContext(CreateLibraryActivity.this).withPermissions(Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                        showAddress();
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                }).check();

        cbComunity.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
               //Toast.makeText(CreateLibraryActivity.this, ""+b, Toast.LENGTH_SHORT).show();
                if (b == true){
                    isShelfPickup = "1";
                    cbAll.setChecked(false);
                    rlPhoneView.setVisibility(View.VISIBLE);
                    tvNoteMsg.setVisibility(View.VISIBLE);
                    tvNoteMsg.setText("Note : Only Community's customers can call.");
                    selfPickupFor = "apartment";
                }else {
                    isShelfPickup = "0";
                    rlPhoneView.setVisibility(View.GONE);
                    phoneNum = "";
                    tvNoteMsg.setVisibility(View.GONE);
                    selfPickupFor = "";
                }
            }
        });

        cbAll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                // Toast.makeText(CreateLibraryActivity.this, ""+b, Toast.LENGTH_SHORT).show();
                if (b == true){
                    isShelfPickup = "1";
                    cbComunity.setChecked(false);
                    rlPhoneView.setVisibility(View.VISIBLE);
                    tvNoteMsg.setVisibility(View.VISIBLE);
                    tvNoteMsg.setText("Note : All customers can call.");
                    selfPickupFor = "all";
                }else {
                    isShelfPickup = "0";
                    rlPhoneView.setVisibility(View.GONE);
                    phoneNum = "";
                    tvNoteMsg.setVisibility(View.GONE);
                    selfPickupFor = "";
                }
            }
        });

        /*cbAllowCall.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                // Toast.makeText(CreateLibraryActivity.this, ""+b, Toast.LENGTH_SHORT).show();
                if (b == true){
                    allowContact = "1";
                    rlPhoneView.setVisibility(View.VISIBLE);
                }else {
                    allowContact = "0";
                    rlPhoneView.setVisibility(View.GONE);
                    phoneNum = "";
                }
            }
        });*/

        if (Constants.SelectedLibraryProfileImage.equals("")){
            Glide.with(this).load(R.drawable.no_img).placeholder(R.drawable.no_img).into(iv_user);
        }else {
            Glide.with(this).load(Constants.SelectedLibraryProfileImage).placeholder(R.drawable.no_img).into(iv_user);
        }


        iv_adr_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mEtAddress.setText("");
                iv_adr_close.setVisibility(View.INVISIBLE);
                //mEtAddress.setFocusable(true);
            }
        });


        mEtAddress.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().equals("")){
                    iv_adr_close.setVisibility(View.INVISIBLE);
                }else {
                    iv_adr_close.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void showAddress() {
        UserCurrentLocation location = new UserCurrentLocation(CreateLibraryActivity.this);
        Log.d("locationnn",""+location.latitude);
        Log.d("locationnn",""+location.longitude);
        Constants.latitude = String.valueOf(location.latitude);
        Constants.longitude = String.valueOf(location.longitude);
        String adr = database.getAddress();
        String splitted[] = adr.split(",");
        StringBuffer sb = new StringBuffer();
        String retrieveData = "";
        for(int i =0; i<splitted.length; i++){
            retrieveData = splitted[i];
            if((retrieveData.trim()).length()>0){
                if(i!=0){
                    sb.append(",");
                }
                sb.append(retrieveData);
            }
        }
        adr = sb.toString();
        mEtAddress.setText(adr);
    }

    private void setTopView(String string) {
        if (Constants.isOwnLibrary == true){
            titleTool.setText("Create Your Own Library");
        }else {
            titleTool.setText("Create Community Library");
        }

        backTool.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                finish();
            }
        });
    }

    public void onSubmit(View view) {
        phoneNum = et_phone.getText().toString().trim();
        if (et_library_name.getText().toString().trim().equals("")){
            Toast.makeText(this, "Library name can not be blank.", Toast.LENGTH_SHORT).show();
        }else if (mEtAddress.getText().toString().trim().equals("")){
            Toast.makeText(this, "Address can not be blank.", Toast.LENGTH_SHORT).show();
        }else if (cbComunity.isChecked() == true && phoneNum.equals("")){
            Toast.makeText(this, "Enter phone number.", Toast.LENGTH_SHORT).show();
        }else if (cbAll.isChecked() == true && phoneNum.equals("")){
            Toast.makeText(this, "Enter phone number.", Toast.LENGTH_SHORT).show();
        }/*else if (Constants.SelectedLibraryProfileImage.equals("")){
            Toast.makeText(this, "Upload your Library Logo.", Toast.LENGTH_SHORT).show();
        }*/else {
            if (Constants.isOwnLibrary == true){
                createLibrary();
            }else {
                createApartmentLibrary();
            }
            //Toast.makeText(this, "Library Created For - " +selfPickupFor , Toast.LENGTH_SHORT).show();
        }
    }

    private void createApartmentLibrary() {
        final ProgressDialog progressBar = new ProgressDialog(this);
        progressBar.setMessage("Please wait...");
        progressBar.setCancelable(false);
        progressBar.show();
        Holders holders = AllApiClass.getInstance().getApi();

        MultipartBody.Part part = null;
        if (Constants.SelectedLibraryProfileImage.equals("")){
            part = null;
        }else {
            try {
                File file = new File(Constants.SelectedLibraryProfileImage);
                RequestBody fileReqBody = RequestBody.create(MediaType.parse("image/*"), file);
                part = MultipartBody.Part.createFormData("image", file.getName(), fileReqBody);
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        Log.d("myFilePath",""+part);
        RequestBody userIdReq = RequestBody.create(MediaType.parse("multipart/form-data"), database.getUserId());
        RequestBody userEmailReq = RequestBody.create(MediaType.parse("multipart/form-data"), et_library_name.getText().toString().trim());
        RequestBody userAdrReq = RequestBody.create(MediaType.parse("multipart/form-data"), mEtAddress.getText().toString().trim());
        RequestBody latReq = RequestBody.create(MediaType.parse("multipart/form-data"), Constants.latitude);
        RequestBody lonReq = RequestBody.create(MediaType.parse("multipart/form-data"), Constants.longitude);
        RequestBody isShelfPickupReq = RequestBody.create(MediaType.parse("multipart/form-data"), isShelfPickup);
        RequestBody isOwnLiraryReq = RequestBody.create(MediaType.parse("multipart/form-data"), isOwnLirary);
        RequestBody allowContactReq = RequestBody.create(MediaType.parse("multipart/form-data"), selfPickupFor);
        RequestBody phoneNumReq = RequestBody.create(MediaType.parse("multipart/form-data"), phoneNum);
        RequestBody library_typeReq = RequestBody.create(MediaType.parse("multipart/form-data"), Constants.libraryType);
        // database.getUserId(),et_library_name.getText().toString().trim(),mEtAddress.getText().toString().trim(), Constants.latitude,Constants.longitude,isShelfPickup

        Call<SubmitDataResModel> resCall = holders.createApartmentLibrary(userIdReq,userEmailReq,userAdrReq,latReq,lonReq,isShelfPickupReq,isOwnLiraryReq,allowContactReq,phoneNumReq,library_typeReq,part);
        resCall.enqueue(new Callback<SubmitDataResModel>() {
            @Override
            public void onResponse(Call<SubmitDataResModel> call, Response<SubmitDataResModel> response) {
                if (response.isSuccessful()){
                    if (response.body().getResponse().getCode() == 1){
                        progressBar.dismiss();
                        Intent intent = new Intent(CreateLibraryActivity.this, SubAdminDashboard.class);
                        startActivity(intent);
                        finishAffinity();
                    }else {
                        progressBar.dismiss();
                        Toast.makeText(CreateLibraryActivity.this, ""+response.body().getResponse().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }else {
                    progressBar.dismiss();
                    Toast.makeText(CreateLibraryActivity.this, "Something went wrong !", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<SubmitDataResModel> call, Throwable t) {
                progressBar.dismiss();
                Toast.makeText(CreateLibraryActivity.this, "Check your internet !", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void createLibrary() {
        final ProgressDialog progressBar = new ProgressDialog(this);
        progressBar.setMessage("Please wait...");
        progressBar.setCancelable(false);
        progressBar.show();
        Holders holders = AllApiClass.getInstance().getApi();

        MultipartBody.Part part = null;
        if (Constants.SelectedLibraryProfileImage.equals("")){
            part = null;
        }else {
            try {
                File file = new File(Constants.SelectedLibraryProfileImage);
                RequestBody fileReqBody = RequestBody.create(MediaType.parse("image/*"), file);
                part = MultipartBody.Part.createFormData("image", file.getName(), fileReqBody);
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        Log.d("myFilePath",""+part);
        RequestBody userIdReq = RequestBody.create(MediaType.parse("multipart/form-data"), database.getUserId());
        RequestBody userEmailReq = RequestBody.create(MediaType.parse("multipart/form-data"), et_library_name.getText().toString().trim());
        RequestBody userAdrReq = RequestBody.create(MediaType.parse("multipart/form-data"), mEtAddress.getText().toString().trim());
        RequestBody latReq = RequestBody.create(MediaType.parse("multipart/form-data"), Constants.latitude);
        RequestBody lonReq = RequestBody.create(MediaType.parse("multipart/form-data"), Constants.longitude);
        RequestBody isShelfPickupReq = RequestBody.create(MediaType.parse("multipart/form-data"), isShelfPickup);
        RequestBody isOwnLiraryReq = RequestBody.create(MediaType.parse("multipart/form-data"), isOwnLirary);
        RequestBody allowContactReq = RequestBody.create(MediaType.parse("multipart/form-data"), selfPickupFor);
        RequestBody phoneNumReq = RequestBody.create(MediaType.parse("multipart/form-data"), phoneNum);
        RequestBody library_typeReq = RequestBody.create(MediaType.parse("multipart/form-data"), Constants.libraryType);
       // database.getUserId(),et_library_name.getText().toString().trim(),mEtAddress.getText().toString().trim(), Constants.latitude,Constants.longitude,isShelfPickup

        Call<SubmitDataResModel> resCall = holders.createLibrary(userIdReq,userEmailReq,userAdrReq,latReq,lonReq,isShelfPickupReq,isOwnLiraryReq,allowContactReq,phoneNumReq,library_typeReq,part);
        resCall.enqueue(new Callback<SubmitDataResModel>() {
            @Override
            public void onResponse(Call<SubmitDataResModel> call, Response<SubmitDataResModel> response) {
                if (response.isSuccessful()){
                    if (response.body().getResponse().getCode() == 1){
                        progressBar.dismiss();
                        Intent intent = new Intent(CreateLibraryActivity.this, SubAdminDashboard.class);
                        startActivity(intent);
                        finish();
                    }else {
                        progressBar.dismiss();
                        Toast.makeText(CreateLibraryActivity.this, ""+response.body().getResponse().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }else {
                    progressBar.dismiss();
                    Toast.makeText(CreateLibraryActivity.this, "Something went wrong !", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<SubmitDataResModel> call, Throwable t) {
                progressBar.dismiss();
                Toast.makeText(CreateLibraryActivity.this, "Check your internet !", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void onLibraryImageUpload(View view) {
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
        Constants.openPageFrom = "fromLibraryProfile";
        Intent intent = new Intent(CreateLibraryActivity.this, ImageGallery.class);
        startActivity(intent);
        //finish();
    }



    @Override
    protected void onResume() {
        if (Constants.SelectedLibraryProfileImage.equals("")){
            Glide.with(this).load(R.drawable.no_img).placeholder(R.drawable.no_img).into(iv_user);
        }else {
            Glide.with(this).load(Constants.SelectedLibraryProfileImage).placeholder(R.drawable.no_img).into(iv_user);
        }
        super.onResume();
    }



}