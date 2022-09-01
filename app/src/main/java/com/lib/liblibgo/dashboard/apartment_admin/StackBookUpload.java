package com.lib.liblibgo.dashboard.apartment_admin;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.lib.liblibgo.R;
import com.lib.liblibgo.adapter.subadmin.CatAdapterNew;
import com.lib.liblibgo.api.AllApiClass;
import com.lib.liblibgo.api.Holders;
import com.lib.liblibgo.common.Constants;
import com.lib.liblibgo.common.UserDatabase;
import com.lib.liblibgo.dashboard.book_collect.CollectApartmentBooksActivity;
import com.lib.liblibgo.dashboard.profile.ImageCroperActivity;
import com.lib.liblibgo.model.BookEntryModel;
import com.lib.liblibgo.model.CategoryListData;
import com.lib.liblibgo.model.CategoryModel;
import com.lib.liblibgo.model.ConditionTypeModel;
import com.lib.liblibgo.model.SubmitDataResModel;
import com.lib.liblibgo.views.MyButton;
import com.lib.liblibgo.views.MyEditText;
import com.lib.liblibgo.views.MyTextView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.lib.liblibgo.dashboard.library.fragments.CommunityLibraryFragment.MyPREFERENCES;

public class StackBookUpload extends AppCompatActivity {
    private ImageView ivToolbarBack;
    private TextView titleTool;
    private CheckBox cbGiveaway;
    private MyEditText etName;
    private MyEditText etDescription;
    private Spinner spCat;
    private MyEditText etPrice;
    private MyEditText etQty;
    private ImageView iv_image;
    private MyTextView tvUploadImage,tvSalePrice;
    List<CategoryListData> cat_list = new ArrayList<>();
    List<CategoryListData> temp_cat_list = new ArrayList<>();
    private String categoryId = "";
    private MyButton sendBtn;
    static final int REQUEST_IMAGE_CAPTURE = 2;
    String currentPhotoPath = "";
    private String communityId = "0";
    SharedPreferences sharedpreferences;
    private SharedPreferences.Editor editor;
    private UserDatabase database;
    private String isOwnLirary = "1";
    private String giveawayStatus = "no";
    private TextView starPrice;
    private LinearLayout llCat;
    private List<ConditionTypeModel> condition_type_list = new ArrayList<>();
    private Spinner spCondition;
    private String bookConditionType = "";
    private LinearLayout llConditionType;
    private String bookId = "";
    private String bookName = "";
    private String bookDescription = "";
    private String bookImgUrl = "";
    private String bookSalePrice = "";
    private String catId = "";
    private String catName = "";
    private String bookConditionTypeValue = "";
    private String giveawayStatusValue = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stack_book_upload);

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();

        communityId = sharedpreferences.getString("community_id", "0");
        database = new UserDatabase(this);
        Log.d("myCommunityId","Community_id : "+communityId);
        ivToolbarBack = (ImageView) findViewById(R.id.backTool);
        titleTool = (TextView) findViewById(R.id.titleTool);
        setUpToolbar(getString(R.string.upload_stack_books));

        cbGiveaway = (CheckBox)findViewById(R.id.cbGiveaway);

        etName = (MyEditText)findViewById(R.id.etName);
        etDescription = (MyEditText)findViewById(R.id.etDescription);
        etPrice = (MyEditText)findViewById(R.id.etPrice);
        etQty = (MyEditText)findViewById(R.id.etQty);
        spCat = (Spinner)findViewById(R.id.spCat);
        iv_image = (ImageView)findViewById(R.id.iv_image);
        tvUploadImage = (MyTextView)findViewById(R.id.tvUploadImage);
        tvSalePrice = (MyTextView)findViewById(R.id.tvSalePrice);
        starPrice = (TextView)findViewById(R.id.starPrice);
        sendBtn = (MyButton)findViewById(R.id.sendBtn);
        llCat = (LinearLayout)findViewById(R.id.llCat);
        llConditionType = (LinearLayout)findViewById(R.id.llConditionType);
        spCondition = (Spinner)findViewById(R.id.spCondition);

        bookId = getIntent().getStringExtra("book_id");
        bookName = getIntent().getStringExtra("name");
        bookDescription = getIntent().getStringExtra("description");
        bookImgUrl = getIntent().getStringExtra("imgUrl");
        bookSalePrice = getIntent().getStringExtra("book_price");
        catId = getIntent().getStringExtra("category_id");
        catName = getIntent().getStringExtra("category_name");
        bookConditionTypeValue = getIntent().getStringExtra("book_condition");
        giveawayStatusValue = getIntent().getStringExtra("giveaway_status");

        if (Constants.isOwnLibrary == true){
            isOwnLirary = "1";
        }else {
            isOwnLirary = "0";
        }

        if (giveawayStatusValue.equals("yes")){
            cbGiveaway.setChecked(true);
            giveawayStatus = "yes";
            etPrice.setText("0");
            etPrice.setEnabled(false);
            tvSalePrice.setTextColor(Color.parseColor("#C4C4C4"));
            starPrice.setVisibility(View.INVISIBLE);
            etPrice.setBackground(getResources().getDrawable(R.drawable.bg_rectangle));
        }else {
            cbGiveaway.setChecked(false);
            giveawayStatus = "no";
            etPrice.setText("");
            etPrice.setEnabled(true);
            tvSalePrice.setTextColor(Color.parseColor("#3C3C3C"));
            starPrice.setVisibility(View.VISIBLE);
            etPrice.setBackground(getResources().getDrawable(R.drawable.bg_search));
        }

        etName.setText(bookName);
        etDescription.setText(bookDescription);
        etPrice.setText(bookSalePrice);


        getCategoryList();
        getConditionType();

        cbGiveaway.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked == true){
                    giveawayStatus = "yes";
                    etPrice.setText("0");
                    etPrice.setEnabled(false);
                    tvSalePrice.setTextColor(Color.parseColor("#C4C4C4"));
                    starPrice.setVisibility(View.INVISIBLE);
                    etPrice.setBackground(getResources().getDrawable(R.drawable.bg_rectangle));
                }else {
                    giveawayStatus = "no";
                    etPrice.setText("");
                    etPrice.setEnabled(true);
                    tvSalePrice.setTextColor(Color.parseColor("#3C3C3C"));
                    starPrice.setVisibility(View.VISIBLE);
                    etPrice.setBackground(getResources().getDrawable(R.drawable.bg_search));
                }
            }
        });

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = etName.getText().toString().trim();
                String price = etPrice.getText().toString().trim();
                String stackQty = etQty.getText().toString().trim();
                String description = etDescription.getText().toString().trim();

               if (cbGiveaway.isChecked()){
                   giveawayStatus = "yes";
               }else {
                   giveawayStatus = "no";
               }

                Log.e("myDatas","All Data : "+isOwnLirary+"\n"+communityId+"\n"+database.getUserId()+"\n"+giveawayStatus);

                if (name.equals("")){
                    etName.setBackground(getResources().getDrawable(R.drawable.bg_error_fields));
                    etName.requestFocus();
                }else if (categoryId.equals("")){
                    llCat.setBackground(getResources().getDrawable(R.drawable.bg_error_fields));
                }else if (bookConditionType.equals("")){
                    llConditionType.setBackground(getResources().getDrawable(R.drawable.bg_error_fields));
                }else if (price.equals("")){
                    etPrice.setBackground(getResources().getDrawable(R.drawable.bg_error_fields));
                    etName.requestFocus();
                }else if (stackQty.equals("")){
                    etQty.setBackground(getResources().getDrawable(R.drawable.bg_error_fields));
                    etName.requestFocus();
                }else if (bookImgUrl.equals("")){
                    //Constants.SelectedBookImage = bookImgUrl;
                    Toast.makeText(StackBookUpload.this, "Provide Book Image.", Toast.LENGTH_SHORT).show();
                    iv_image.setBackground(getResources().getDrawable(R.drawable.bg_error_fields));
                }else {
                    saveData(name,price,stackQty,description);
                    //Log.d("myDatas",isOwnLirary+"\n"+communityId+"\n"+database.getUserId());
                }
            }
        });

        tvUploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dexter.withContext(StackBookUpload.this).withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA)
                        .withListener(new MultiplePermissionsListener() {
                            @Override
                            public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                                chooseBookImageFromCamera();
                                Constants.openPageFrom = "fromBookUpload";
                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                                permissionToken.continuePermissionRequest();
                            }
                        }).check();
            }
        });

    }

    private void getConditionType() {
        condition_type_list.add(new ConditionTypeModel("Select Condition"));
        condition_type_list.add(new ConditionTypeModel("As good as new"));
        condition_type_list.add(new ConditionTypeModel("Marked texts"));
        condition_type_list.add(new ConditionTypeModel("Torn or pale pages"));
        condition_type_list.add(new ConditionTypeModel("Marked texts & Torn cover page"));

        spCondition.setAdapter(new ConditionAdapterNew(getApplicationContext(),condition_type_list));

        if (bookConditionTypeValue.equals("")){
            spCondition.setSelection(0);
        }else if (bookConditionTypeValue.equals("As good as new")){
            spCondition.setSelection(1);
        }else if (bookConditionTypeValue.equals("Marked texts")){
            spCondition.setSelection(2);
        }else if (bookConditionTypeValue.equals("Torn or pale pages")){
            spCondition.setSelection(3);
        }else if (bookConditionTypeValue.equals("Marked texts & Torn cover page")){
            spCondition.setSelection(4);
        }else {
            spCondition.setSelection(0);
        }

        spCondition.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0 || condition_type_list.get(i).getCondition_type().equals("Select Condition")){
                    bookConditionType = "";
                }else {
                    bookConditionType = condition_type_list.get(i).getCondition_type();
                    llConditionType.setBackground(getResources().getDrawable(R.drawable.bg_search));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    public class ConditionAdapterNew extends BaseAdapter {
        private Context mCtx;
        private List<ConditionTypeModel> mList;
        private LayoutInflater inflter;

        public ConditionAdapterNew(Context mCtx, List<ConditionTypeModel> mList) {
            this.mCtx = mCtx;
            this.mList = mList;
            inflter = (LayoutInflater.from(mCtx));
        }

        @Override
        public int getCount() {
            return mList.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            view = inflter.inflate(R.layout.row_spinner_item, null);
            MyTextView tvApartmentName = (MyTextView) view.findViewById(R.id.tvApartmentName);
            tvApartmentName.setText(mList.get(i).getCondition_type());
            return view;
        }
    }

    private void saveData(String name, String price, String stackQty,String description) {
        String sellingType = "For Sale";
        final ProgressDialog progressBar = new ProgressDialog(this);
        progressBar.setMessage("Please wait..");
        progressBar.setCancelable(false);
        progressBar.show();
        Holders holders = AllApiClass.getInstance().getApi();

        MultipartBody.Part part = null;

        if (Constants.SelectedBookImage.equals("")){
            part = null;
        }else {
            try {
                File file = new File(Constants.SelectedBookImage);
                RequestBody fileReqBody = RequestBody.create(MediaType.parse("image/*"), file);
                part = MultipartBody.Part.createFormData("book_image", file.getName(), fileReqBody);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        /*MultipartBody.Part part = null;
        try {
            File file = new File(Constants.SelectedBookImage);
            RequestBody fileReqBody = RequestBody.create(MediaType.parse("image/*"), file);
            part = MultipartBody.Part.createFormData("book_image", file.getName(), fileReqBody);
        }catch (Exception e){
            e.printStackTrace();
        }*/
        Log.d("myFilePath",""+part);
        String isOpenLib = "";
        RequestBody userReq = RequestBody.create(MediaType.parse("multipart/form-data"), database.getUserId());
        RequestBody bookIdReq = RequestBody.create(MediaType.parse("multipart/form-data"), bookId);
        RequestBody isOwnLibReq = RequestBody.create(MediaType.parse("multipart/form-data"), isOwnLirary);
        RequestBody communityIdReq = RequestBody.create(MediaType.parse("multipart/form-data"), communityId);
        RequestBody bookNameReq = RequestBody.create(MediaType.parse("multipart/form-data"), name);
        RequestBody bookDescReq = RequestBody.create(MediaType.parse("multipart/form-data"), description);
        RequestBody condTypeReq = RequestBody.create(MediaType.parse("multipart/form-data"), bookConditionType);
        RequestBody catReq = RequestBody.create(MediaType.parse("multipart/form-data"), categoryId);
        RequestBody qtyReq = RequestBody.create(MediaType.parse("multipart/form-data"), stackQty);
        RequestBody giveawayReq = RequestBody.create(MediaType.parse("multipart/form-data"), giveawayStatus);
        RequestBody sellingTypeReq = RequestBody.create(MediaType.parse("multipart/form-data"), sellingType);
        RequestBody priceReq = RequestBody.create(MediaType.parse("multipart/form-data"), price);
        RequestBody isOpenLibReq = RequestBody.create(MediaType.parse("multipart/form-data"), isOpenLib);

        Call<SubmitDataResModel> resCall = holders.addStackBooks(userReq,bookIdReq,isOwnLibReq,communityIdReq,bookNameReq,bookDescReq,condTypeReq,catReq,qtyReq,giveawayReq,isOpenLibReq,
                sellingTypeReq,priceReq,part);

        resCall.enqueue(new Callback<SubmitDataResModel>() {
            @Override
            public void onResponse(Call<SubmitDataResModel> call, Response<SubmitDataResModel> response) {
                if (response.isSuccessful()){
                    if (response.body().getResponse().getCode() == 1){
                        progressBar.dismiss();
                        //MyBooksActivity.flag = 1;
                        Constants.SelectedBookImage = "";
                        CollectApartmentBooksActivity.flag = 1;
                        Toast.makeText(StackBookUpload.this, "Stack Uploaded Successfully.", Toast.LENGTH_SHORT).show();
                        onBackPressed();
                    }else {
                        progressBar.dismiss();
                        Toast.makeText(StackBookUpload.this, ""+response.body().getResponse().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }else {
                    progressBar.dismiss();
                    Toast.makeText(StackBookUpload.this, "Something went wrong !", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<SubmitDataResModel> call, Throwable t) {
                progressBar.dismiss();
                Toast.makeText(StackBookUpload.this, "Check your internet !", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void chooseBookImageFromCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        takePictureIntent.putExtra("android.intent.extra.quickCapture", true);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.provider.cartoonprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        if (image.getAbsolutePath() != null){
            currentPhotoPath = image.getAbsolutePath();
        }else {
            currentPhotoPath = "";
        }

        return image;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2){
            if (resultCode == RESULT_OK) {
                if (currentPhotoPath.equals("")){
                    Toast.makeText(this, "Failed to save image.Please try again. ", Toast.LENGTH_SHORT).show();
                }else {
                    Constants.SelectedBookImage = currentPhotoPath;
                    Intent intent = new Intent(this, ImageCroperActivity.class);
                    startActivity(intent);
                    Log.d("mmmmm : ","imgUri :"+Constants.SelectedBookImage);
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        Constants.SelectedBookImage = "";
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        if (!Constants.SelectedBookImage.equals("")){
            Glide.with(this).load(Constants.SelectedBookImage).placeholder(R.drawable.no_img).into(iv_image);
            iv_image.setBackground(getResources().getDrawable(R.drawable.bg_search));
            bookImgUrl = Constants.SelectedBookImage;
        }else {
            if (bookImgUrl.equals("")){
                Glide.with(this).load(R.drawable.no_img).placeholder(R.drawable.no_img).into(iv_image);
            }else {
                Glide.with(this).load(bookImgUrl).placeholder(R.drawable.no_img).into(iv_image);
            }
        }
        Log.d("myCommunityId","Community_id : "+communityId);
        super.onResume();
    }

    private void getCategoryList() {
        Holders holders = AllApiClass.getInstance().getApi();
        Call<CategoryModel> resCall = holders.categoryList();
        resCall.enqueue(new Callback<CategoryModel>() {
            @Override
            public void onResponse(Call<CategoryModel> call, Response<CategoryModel> response) {
                if (response.isSuccessful()) {
                    if (response.body().getResponse().getCode().equals("1")) {
                        if (response.body().getResponse().getApartment_list() != null) {
                            cat_list = response.body().getResponse().getApartment_list();
                            CategoryListData data = new CategoryListData();
                            data.setCategory_id("");
                            data.setCategory_name("Select Category");
                            temp_cat_list.add(data);
                            for (int i=0;i<cat_list.size();i++){
                                temp_cat_list.add(cat_list.get(i));
                            }
                            CatAdapterNew adapter = new CatAdapterNew(getApplicationContext(),temp_cat_list);
                            spCat.setAdapter(adapter);
                            //spCat.setSelection(adapter.getPosition("Category 2"));
                            for (int i=0;i<temp_cat_list.size();i++){
                                if (temp_cat_list.get(i).getCategory_name().equals(catName)){
                                    spCat.setSelection(i);
                                    break;
                                }else {
                                    spCat.setSelection(0);
                                }
                            }
                            spCat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                                    if (i != 0 || !temp_cat_list.get(i).getCategory_name().equals("Select Category")){
                                        categoryId = temp_cat_list.get(i).getCategory_id();
                                        llCat.setBackground(getResources().getDrawable(R.drawable.bg_search));
                                    }else {
                                        categoryId = "";
                                    }
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
            public void onFailure(Call<CategoryModel> call, Throwable t) {
                Toast.makeText(StackBookUpload.this, "Try again", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setUpToolbar(String param) {
        titleTool.setText(param);
        ivToolbarBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //onBackPressed();
                finish();
            }
        });
    }
}