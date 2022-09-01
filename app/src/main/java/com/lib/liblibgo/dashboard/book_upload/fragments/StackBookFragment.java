package com.lib.liblibgo.dashboard.book_upload.fragments;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.JsonObject;
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
import com.lib.liblibgo.dashboard.apartment_admin.StackBookUpload;
import com.lib.liblibgo.dashboard.book_collect.CollectApartmentBooksActivity;
import com.lib.liblibgo.dashboard.profile.ImageCroperActivity;
import com.lib.liblibgo.listner.MoveToCommunityClickListener;
import com.lib.liblibgo.model.CategoryListData;
import com.lib.liblibgo.model.CategoryModel;
import com.lib.liblibgo.model.CheckCommunityModel;
import com.lib.liblibgo.model.CommunityListModel;
import com.lib.liblibgo.model.ConditionTypeModel;
import com.lib.liblibgo.model.SubmitDataResModel;
import com.lib.liblibgo.views.MyButton;
import com.lib.liblibgo.views.MyEditText;
import com.lib.liblibgo.views.MyTextView;

import org.json.JSONException;
import org.json.JSONObject;

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

import static android.app.Activity.RESULT_OK;

public class StackBookFragment extends Fragment {

    private Context mContext;
    private UserDatabase database;
    private CheckBox cbCommunity;
    private CheckBox cbOpen;
    private CheckBox cbGiveaway;
    private CheckBox cbSelect;
    private TextView tvLibraryName;
    private RecyclerView rvCommunity;
    private RelativeLayout rlOwnCommunity;
    private LinearLayout llCommunities;
    private LinearLayout llCommunityList;
    private RelativeLayout rlExpandView;
    private ImageView ivExpand;
    private TextView tvExpand;
    private String isOpenLib = "1";
    private List<CommunityListModel.ResDataBooks.CommunityList> myList = new ArrayList<>();
    private CommunityAdapterNew adapter;
    private MyEditText etName;
    private MyEditText etDescription;
    private MyEditText etPrice;
    private MyEditText etQty;
    private Spinner spCat;
    private ImageView iv_image;
    private MyTextView tvUploadImage;
    private MyTextView tvSalePrice;
    private TextView starPrice;
    private MyButton sendBtn;
    private LinearLayout llCat;
    private LinearLayout llConditionType;
    private Spinner spCondition;
    List<CategoryListData> cat_list = new ArrayList<>();
    List<CategoryListData> temp_cat_list = new ArrayList<>();
    private String categoryId = "";
    private String catName = "";
    private List<ConditionTypeModel> condition_type_list = new ArrayList<>();
    private String bookConditionType = "";
    private  int conditionValue = 1;
    private String giveawayStatus = "no";
    static final int REQUEST_IMAGE_CAPTURE = 2;
    String currentPhotoPath = "";
    private String bookId = "";
    private String isOwnLirary = "0";
    private String bookImgUrl = "";
    private int flag = 0;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_stack_book, container, false);

        database = new UserDatabase(mContext);
        Constants.selectedStackCommunityIds = "";

        cbCommunity = (CheckBox)view.findViewById(R.id.cbCommunity);
        cbOpen = (CheckBox)view.findViewById(R.id.cbOpen);
        cbGiveaway = (CheckBox)view.findViewById(R.id.cbGiveaway);

        cbSelect = (CheckBox)view.findViewById(R.id.cbSelect);
        tvLibraryName = (TextView)view.findViewById(R.id.tvLibraryName);
        rvCommunity = (RecyclerView)view.findViewById(R.id.rvCommunity);
        rlOwnCommunity = (RelativeLayout)view.findViewById(R.id.rlOwnCommunity);

        llCommunities = (LinearLayout)view.findViewById(R.id.llCommunities);
        llCommunityList = (LinearLayout)view.findViewById(R.id.llCommunityList);
        rlExpandView = (RelativeLayout)view.findViewById(R.id.rlExpandView);
        ivExpand = (ImageView)view.findViewById(R.id.ivExpand);
        tvExpand = (TextView)view.findViewById(R.id.tvExpand);

        etName = (MyEditText)view.findViewById(R.id.etName);
        etDescription = (MyEditText)view.findViewById(R.id.etDescription);
        etPrice = (MyEditText)view.findViewById(R.id.etPrice);
        etQty = (MyEditText)view.findViewById(R.id.etQty);
        spCat = (Spinner)view.findViewById(R.id.spCat);
        iv_image = (ImageView)view.findViewById(R.id.iv_image);
        tvUploadImage = (MyTextView)view.findViewById(R.id.tvUploadImage);
        tvSalePrice = (MyTextView)view.findViewById(R.id.tvSalePrice);
        starPrice = (TextView)view.findViewById(R.id.starPrice);
        sendBtn = (MyButton)view.findViewById(R.id.sendBtn);
        llCat = (LinearLayout)view.findViewById(R.id.llCat);
        llConditionType = (LinearLayout)view.findViewById(R.id.llConditionType);
        spCondition = (Spinner)view.findViewById(R.id.spCondition);
        cbOpen.setChecked(true);
        cbOpen.setButtonTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary)));

        cbOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cbOpen.isChecked()){
                    isOpenLib = "1";
                    cbOpen.setButtonTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary)));
                }else {
                    isOpenLib = "0";
                    cbOpen.setButtonTintList(ColorStateList.valueOf(getResources().getColor(R.color.black_fade)));
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

                //Log.e("myDatas","All Data : "+isOwnLirary+"\n"+communityId+"\n"+database.getUserId()+"\n"+giveawayStatus);

                if (name.equals("")){
                    etName.setBackground(getResources().getDrawable(R.drawable.bg_error_fields_new));
                    etName.requestFocus();
                }else if (categoryId.equals("")){
                    llCat.setBackground(getResources().getDrawable(R.drawable.bg_error_fields_new));
                }else if (bookConditionType.equals("")){
                    llConditionType.setBackground(getResources().getDrawable(R.drawable.bg_error_fields_new));
                }else if (price.equals("")){
                    etPrice.setBackground(getResources().getDrawable(R.drawable.bg_error_fields_new));
                    etName.requestFocus();
                }else if (stackQty.equals("")){
                    etQty.setBackground(getResources().getDrawable(R.drawable.bg_error_fields_new));
                    etName.requestFocus();
                }else if (bookImgUrl.equals("")){
                    //Constants.SelectedBookImage = bookImgUrl;
                    Toast.makeText(mContext, "Provide Book Image.", Toast.LENGTH_SHORT).show();
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
                Dexter.withContext(mContext).withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA)
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

        return view;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser){
            flag = 1;
            bookImgUrl = "";
            currentPhotoPath = "";
            Constants.selectedStackCommunityIds = "";
            Constants.SelectedBookImage = "";
            checkCommunity();
            getCategoryList();
            getConditionType();
        }
    }

    private void saveData(String name, String price, String stackQty, String description) {
        String sellingType = "For Sale";
        final ProgressDialog progressBar = new ProgressDialog(mContext);
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

        Log.d("myFilePath",""+part);
        RequestBody userReq = RequestBody.create(MediaType.parse("multipart/form-data"), database.getUserId());
        RequestBody bookIdReq = RequestBody.create(MediaType.parse("multipart/form-data"), bookId);
        RequestBody isOwnLibReq = RequestBody.create(MediaType.parse("multipart/form-data"), isOwnLirary);
        RequestBody communityIdReq = RequestBody.create(MediaType.parse("multipart/form-data"), Constants.selectedStackCommunityIds);
        RequestBody bookNameReq = RequestBody.create(MediaType.parse("multipart/form-data"), name);
        RequestBody bookDescReq = RequestBody.create(MediaType.parse("multipart/form-data"), description);
        RequestBody condTypeReq = RequestBody.create(MediaType.parse("multipart/form-data"), bookConditionType);
        RequestBody catReq = RequestBody.create(MediaType.parse("multipart/form-data"), categoryId);
        RequestBody qtyReq = RequestBody.create(MediaType.parse("multipart/form-data"), stackQty);
        RequestBody giveawayReq = RequestBody.create(MediaType.parse("multipart/form-data"), giveawayStatus);
        RequestBody isOpenLibraryReq = RequestBody.create(MediaType.parse("multipart/form-data"), isOpenLib);
        RequestBody sellingTypeReq = RequestBody.create(MediaType.parse("multipart/form-data"), sellingType);
        RequestBody priceReq = RequestBody.create(MediaType.parse("multipart/form-data"), price);

        Call<SubmitDataResModel> resCall = holders.addStackBooks(userReq,bookIdReq,isOwnLibReq,communityIdReq,bookNameReq,bookDescReq,condTypeReq,catReq,qtyReq,giveawayReq,isOpenLibraryReq,
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
                        Toast.makeText(mContext, "Stack Uploaded Successfully.", Toast.LENGTH_SHORT).show();
                        getActivity().onBackPressed();
                    }else {
                        progressBar.dismiss();
                        Toast.makeText(mContext, ""+response.body().getResponse().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }else {
                    progressBar.dismiss();
                    Toast.makeText(mContext, "Something went wrong !", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<SubmitDataResModel> call, Throwable t) {
                progressBar.dismiss();
                Toast.makeText(mContext, "Check your internet !", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void chooseBookImageFromCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        takePictureIntent.putExtra("android.intent.extra.quickCapture", true);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(mContext,
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
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
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
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2){
            if (resultCode == RESULT_OK) {
                if (currentPhotoPath.equals("")){
                    Toast.makeText(mContext, "Failed to save image.Please try again. ", Toast.LENGTH_SHORT).show();
                }else {
                    Constants.SelectedBookImage = currentPhotoPath;
                    Intent intent = new Intent(mContext, ImageCroperActivity.class);
                    startActivity(intent);
                    Log.d("mmmmm : ","imgUri :"+Constants.SelectedBookImage);
                }
            }
        }
    }

    @Override
    public void onDestroy() {
        Constants.SelectedBookImage = "";
        flag = 0;
        super.onDestroy();
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
                            CatAdapterNew adapter = new CatAdapterNew(mContext,temp_cat_list);
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
                Toast.makeText(mContext, "Try again", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void getConditionType() {
        condition_type_list.add(new ConditionTypeModel("Select Condition"));
        condition_type_list.add(new ConditionTypeModel("As good as new"));
        condition_type_list.add(new ConditionTypeModel("Marked texts"));
        condition_type_list.add(new ConditionTypeModel("Torn or pale pages"));
        condition_type_list.add(new ConditionTypeModel("Marked texts & Torn cover page"));

        spCondition.setAdapter(new ConditionAdapterNew(mContext,condition_type_list));

        /*if (bookConditionTypeValue.equals("")){
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
        }*/

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

    private void checkCommunity() {
        Holders holders = AllApiClass.getInstance().getApi();
        Call<CheckCommunityModel> call = holders.checkUserCommunityStatus(database.getUserId());
        call.enqueue(new Callback<CheckCommunityModel>() {
            @Override
            public void onResponse(Call<CheckCommunityModel> call, Response<CheckCommunityModel> response) {
                if (response.isSuccessful()){
                    if (response.body().getResponse().getCode().equals("1")){
                        if (response.body().getResponse().getCommunity_status().equals("true")){
                            cbCommunity.setEnabled(true);
                            cbCommunity.setTextColor(Color.parseColor("#2B2A2A"));
                            cbCommunity.setButtonTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary)));
                            showCommunityList();
                        }else {
                            cbCommunity.setChecked(false);
                            cbCommunity.setEnabled(false);
                            cbCommunity.setTextColor(Color.parseColor("#C4C4C4"));
                            cbCommunity.setButtonTintList(ColorStateList.valueOf(getResources().getColor(R.color.rect_color)));
                        }

                        cbCommunity.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (cbCommunity.isChecked()){
                                    llCommunities.setVisibility(View.VISIBLE);
                                    Constants.selectedStackCommunityIds =  android.text.TextUtils.join(",",Constants.selectedStackCommunityList);
                                }else {
                                    llCommunities.setVisibility(View.GONE);
                                    Constants.selectedStackCommunityIds = "";
                                }
                            }
                        });
                    }
                }
            }

            @Override
            public void onFailure(Call<CheckCommunityModel> call, Throwable t) {

            }
        });
    }

    private void showCommunityList() {
        Holders holders = AllApiClass.getInstance().getApi();
        Call<CommunityListModel> call = holders.getMyAllCommunities(database.getUserId());
        call.enqueue(new Callback<CommunityListModel>() {
            @Override
            public void onResponse(Call<CommunityListModel> call, Response<CommunityListModel> response) {
                if (response.isSuccessful()){
                    if (response.body().getResponse().getCode().equals("1")){
                        Constants.selectedStackCommunityList = new ArrayList<>();
                        myList = response.body().getResponse().getCommunity_list();
                        if (response.body().getResponse().getCommunity_library_status().equals("1")){
                            rlOwnCommunity.setVisibility(View.VISIBLE);
                            tvLibraryName.setText(response.body().getResponse().getOwn_library_name());
                            Constants.selectedStackCommunityList.add("0");
                            cbSelect.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if (cbSelect.isChecked()){
                                        Constants.selectedStackCommunityList.add("0");
                                    }else {
                                        Constants.selectedStackCommunityList.remove("0");
                                    }

                                    Constants.selectedStackCommunityIds =  android.text.TextUtils.join(",",Constants.selectedStackCommunityList);
                                }
                            });

                        }else {
                            rlOwnCommunity.setVisibility(View.GONE);
                        }

                        if (myList.size() > 0){
                            adapter = new CommunityAdapterNew(myList, mContext);
                            LinearLayoutManager verticalManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
                            rvCommunity.setLayoutManager(verticalManager);
                            rvCommunity.setAdapter(adapter);
                        }
                    }
                }else {
                    Toast.makeText(mContext, "Something went wrong.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CommunityListModel> call, Throwable t) {
                Toast.makeText(mContext, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public class CommunityAdapterNew extends RecyclerView.Adapter<CommunityAdapterNew.MyHolder> {
        List<CommunityListModel.ResDataBooks.CommunityList> mList;
        Context mCtx;
        MoveToCommunityClickListener listener;
        private boolean isSelected = true;

        public CommunityAdapterNew(List<CommunityListModel.ResDataBooks.CommunityList> mList, Context mCtx) {
            this.mList = mList;
            this.mCtx = mCtx;
        }

        @NonNull
        @Override
        public CommunityAdapterNew.MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View mView = LayoutInflater.from(mCtx).inflate(R.layout.community_select_row, parent, false);
            return new CommunityAdapterNew.MyHolder(mView);
        }

        @Override
        public void onBindViewHolder(@NonNull CommunityAdapterNew.MyHolder holder, int position) {
            holder.bind();
        }

        @Override
        public int getItemCount() {
            return mList.size();
        }

        public class MyHolder extends RecyclerView.ViewHolder {
            TextView tvLibraryName;
            CheckBox cbSelectHolder;

            public MyHolder(@NonNull View itemView) {
                super(itemView);
                tvLibraryName = itemView.findViewById(R.id.tvLibraryName);
                cbSelectHolder = itemView.findViewById(R.id.cbSelectHolder);

            }

            public void bind() {
                tvLibraryName.setText(mList.get(getAdapterPosition()).getCommunity_library_name());

                Constants.selectedStackCommunityList.add(mList.get(getAdapterPosition()).getCommunity_id());
                Constants.selectedStackCommunityIds =  android.text.TextUtils.join(",",Constants.selectedStackCommunityList);

                cbSelectHolder.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //isSelected = mList.get(getAdapterPosition()).getIsSelected() ? true : false;
                        if (cbSelectHolder.isChecked()){
                            Constants.selectedStackCommunityList.add(mList.get(getAdapterPosition()).getCommunity_id());
                        }else {
                            Constants.selectedStackCommunityList.remove(mList.get(getAdapterPosition()).getCommunity_id());
                        }

                        Constants.selectedStackCommunityIds =  android.text.TextUtils.join(",",Constants.selectedStackCommunityList);
                    }
                });

            }
        }

        public void setOnItemClickListener(MoveToCommunityClickListener listener){
            this.listener = listener;
        }

    }

    @Override
    public void onAttach(@NonNull Context context) {
        mContext = context;
        super.onAttach(context);
    }

    @Override
    public void onResume() {
        if (flag == 1){
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
            Log.d("myCommunityId","Community_id : "+Constants.selectedStackCommunityIds);
        }
        super.onResume();
    }
}