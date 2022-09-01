package com.lib.liblibgo.dashboard.library;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.lib.liblibgo.R;
import com.lib.liblibgo.api.AllApiClass;
import com.lib.liblibgo.api.Holders;
import com.lib.liblibgo.common.Constants;
import com.lib.liblibgo.common.UserDatabase;
import com.lib.liblibgo.dashboard.apartment_admin.ImageGallery;
import com.lib.liblibgo.dashboard.apartment_admin.SubAdminDashboard;
import com.lib.liblibgo.model.SubmitDataResModel;

import java.io.File;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditLibraryActivity extends AppCompatActivity {

    private String libName = "";
    private String libAddress = "";
    private String libImg = "";
    private EditText mEtAddress;
    private EditText et_Username;
    private EditText et_library_name;
    private EditText et_phone;
    private UserDatabase database;
    private TextView titleTool;
    private ImageView backTool;
    private CircleImageView iv_user;
    private String isOwnLirary = "";
    public static int editLibFlag = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_library);

        database = new UserDatabase(this);

        titleTool = findViewById(R.id.titleTool);
        backTool = findViewById(R.id.backTool);
        setTopView();

        Constants.SelectedLibraryEditImage = "";
        Constants.SelectedLibraryProfileImage = "";

        mEtAddress = (EditText)findViewById(R.id.et_adr);
        et_Username = (EditText)findViewById(R.id.et_fname);
        et_library_name = (EditText)findViewById(R.id.et_library_name);
        et_phone = (EditText)findViewById(R.id.et_phone);
        iv_user = (CircleImageView)findViewById(R.id.iv_user);

        if (getIntent().getExtras() != null){
            libName = getIntent().getStringExtra("name");
            libAddress = getIntent().getStringExtra("adr");
            libImg = getIntent().getStringExtra("img");
        }

        setData();

    }

    private void setData() {
        et_Username.setText(database.getNAME());
        et_phone.setText(database.getPHONE());
        mEtAddress.setText(libAddress);
        et_library_name.setText(libName);

        if (libImg.equals("")){
            Glide.with(EditLibraryActivity.this).load(R.drawable.no_img).into(iv_user);
        }else {
            Glide.with(EditLibraryActivity.this).load(libImg).into(iv_user);
        }

    }

    private void setTopView() {
        if (Constants.isOwnLibrary == true){
            titleTool.setText("Edit Own Library");
        }else {
            titleTool.setText("Edit Community Library");
        }

        backTool.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                finish();
            }
        });
    }

    public void onLibraryImageUpload(View view) {
        Constants.openPageFrom = "fromEditLibrary";
        Intent intent = new Intent(EditLibraryActivity.this, ImageGallery.class);
        startActivity(intent);
    }

    public void onSubmit(View view) {
        if (Constants.isOwnLibrary == true){
            isOwnLirary = "1";
        }else {
            isOwnLirary = "0";
        }
        String libraryName = et_library_name.getText().toString().trim();
        String libraryAddress = mEtAddress.getText().toString().trim();

        if (libraryName.equals("")){
            Toast.makeText(this, "Enter library name.", Toast.LENGTH_SHORT).show();
            et_library_name.setBackground(getResources().getDrawable(R.drawable.bg_search));
        }else if (libraryAddress.equals("")){
            Toast.makeText(this, "Enter address.", Toast.LENGTH_SHORT).show();
            mEtAddress.setBackground(getResources().getDrawable(R.drawable.bg_search));
        }else {
            saveLibraryData(libraryName,libraryAddress);
        }
    }

    private void saveLibraryData(String libraryName, String libraryAddress) {
        final ProgressDialog progressBar = new ProgressDialog(this);
        progressBar.setMessage("Please wait...");
        progressBar.setCancelable(false);
        progressBar.show();
        Holders holders = AllApiClass.getInstance().getApi();

        MultipartBody.Part part = null;
        if (Constants.SelectedLibraryEditImage.equals("")){
            part = null;
        }else {
            try {
                File file = new File(Constants.SelectedLibraryEditImage);
                RequestBody fileReqBody = RequestBody.create(MediaType.parse("image/*"), file);
                part = MultipartBody.Part.createFormData("library_image", file.getName(), fileReqBody);
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        Log.d("myFilePath",""+part);
        RequestBody userIdReq = RequestBody.create(MediaType.parse("multipart/form-data"), database.getUserId());
        RequestBody libNameReq = RequestBody.create(MediaType.parse("multipart/form-data"), libraryName);
        RequestBody userAdrReq = RequestBody.create(MediaType.parse("multipart/form-data"), libraryAddress);
        RequestBody isOwnLiraryReq = RequestBody.create(MediaType.parse("multipart/form-data"), isOwnLirary);

        Call<SubmitDataResModel> resCall = holders.editLibrary(userIdReq,isOwnLiraryReq,part,userAdrReq,libNameReq);
        resCall.enqueue(new Callback<SubmitDataResModel>() {
            @Override
            public void onResponse(Call<SubmitDataResModel> call, Response<SubmitDataResModel> response) {
                if (response.isSuccessful()){
                    if (response.body().getResponse().getCode() == 1){
                        progressBar.dismiss();
                        editLibFlag = 1;
                        Constants.SelectedLibraryEditImage = "";
                        finish();
                    }else {
                        progressBar.dismiss();
                        Toast.makeText(EditLibraryActivity.this, ""+response.body().getResponse().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }else {
                    progressBar.dismiss();
                    Toast.makeText(EditLibraryActivity.this, "Something went wrong !", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<SubmitDataResModel> call, Throwable t) {
                progressBar.dismiss();
                Toast.makeText(EditLibraryActivity.this, "Check your internet !", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onResume() {
        if (!Constants.SelectedLibraryEditImage.equals("")){
            Glide.with(this).load(Constants.SelectedLibraryEditImage).placeholder(R.drawable.no_img).into(iv_user);
        }else {
            if (libImg.equals("")){
                Glide.with(EditLibraryActivity.this).load(R.drawable.no_img).into(iv_user);
            }else {
                Glide.with(EditLibraryActivity.this).load(libImg).into(iv_user);
            }
        }
        super.onResume();
    }
}