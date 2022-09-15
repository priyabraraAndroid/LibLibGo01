package com.lib.liblibgo.dashboard.book_upload.fragments;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
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
import com.lib.liblibgo.dashboard.apartment_admin.ImageGallery;
import com.lib.liblibgo.dashboard.apartment_admin.MyBooksActivity;
import com.lib.liblibgo.dashboard.apartment_admin.QRCodeScannerActivity;
import com.lib.liblibgo.dashboard.apartment_admin.UploadBookDetails;
import com.lib.liblibgo.dashboard.book_collect.CollectApartmentBooksActivity;
import com.lib.liblibgo.dashboard.profile.ImageCroperActivity;
import com.lib.liblibgo.listner.MoveToCommunityClickListener;
import com.lib.liblibgo.model.BookEntryModel;
import com.lib.liblibgo.model.CategoryListData;
import com.lib.liblibgo.model.CategoryModel;
import com.lib.liblibgo.model.CheckCommunityModel;
import com.lib.liblibgo.model.CommunityListModel;
import com.lib.liblibgo.model.ConditionTypeModel;
import com.lib.liblibgo.model.SubmitDataResModel;
import com.lib.liblibgo.views.MyEditText;
import com.lib.liblibgo.views.MyTextView;

import org.json.JSONArray;
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

public class IndividualBookFragment extends Fragment{

    private CheckBox cbCommunity;
    private Context mContext;
    private UserDatabase database;
    private CheckBox cbOpen;
    private LinearLayout llCommunities;
    private RelativeLayout rlExpandView;
    private ImageView ivExpand;
    private TextView tvExpand;
    private LinearLayout llCommunityList;
    private CheckBox cbSelect;
    private TextView tvLibraryName;
    private RecyclerView rvCommunity;
    private List<CommunityListModel.ResDataBooks.CommunityList> myList = new ArrayList<>();
    private RelativeLayout rlOwnCommunity;
    private CommunityAdapterNew adapter;
    private Button saveDetailsBtn;
    private RadioGroup rgSaleType;
    private String saleType = "";
    private CheckBox cbGiveaway;
    private String giveawayStatus = "no";
    private Spinner spCat;
    private Spinner spCondition;
    private MyEditText etName;
    private MyEditText etAuthor;
    private MyEditText etIsbn;
    private MyEditText etDescription;
    private MyEditText etRentDuration;
    private MyEditText etPrice;
    private MyEditText etRentPrice;
    private MyEditText etSecurityMoney;
    private MyEditText etMrp;
    private ImageView iv_image;
    private MyTextView tvOr;
    private MyTextView tvIsbnDigitCount;
    private MyTextView tvUploadImage;
    private NestedScrollView scrollView;
    private LinearLayout llCat;
    private LinearLayout llConditionType;
    private TextView tvSaleType;
    private TextView starSaleType;
    private TextView tvConditionType;
    private TextView starConditionType;
    private TextView tvMrp;
    private TextView starMrp;
    private TextView tvSecurity;
    private TextView tvSalePrice;
    private TextView tvRentPrice;
    private TextView tvRentDuration;
    private TextView starRentDuration;
    private RadioButton rbSale;
    private RadioButton rbRent;
    private RadioButton rbBoth;
    private String thumbnail = "";
    private String smallThumbnail = "";
    private String APi_key = "48047_e5da204a55741d7ccc1181ce78f2a1d4";
    private String dollerValue = "80";
    private String division = "2";
    private String As_good_as_new = "1";
    private String Marked_texts = "1";
    private String Torn_or_pale_pages = "1";
    private String Marked_texts_Torn_cover_page = "1";
    private String Security_price_type = "1";
    private String Rent_divisor = "1";
    private List<ConditionTypeModel> condition_type_list = new ArrayList<>();
    private String bookConditionType = "";
    private  int conditionValue = 1;
    List<CategoryListData> cat_list = new ArrayList<>();
    List<CategoryListData> temp_cat_list = new ArrayList<>();
    private String categoryId="";
    private String catName = "";
    private ImageView ivScanner;
    private LinearLayout llIsbn;
    int price = 0;
    static final int REQUEST_IMAGE_CAPTURE = 2;
    String currentPhotoPath = "";
    private int counter = 0;
    private String bookId = "";
    private String isOpenLib = "1";
    private String bookName = "";
    private String authorName = "";
    private String isbnNum = "";
    private String description = "";
    private String imgUrl = "";
    private String rental_price = "";
    private String rental_duration = "";
    private String bookSalePrice = "";
    private String bookCatId = "";
    private String bookUpdateId = "";
    private String bookMrp = "";
    private String bookQuantity = "";
    private String bookSaleType = "";
    private String bookCoditionType = "";
    private String bookSecurityMoney = "";
    private String bookGiveAway = "";
    private String bookCatName = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_individual_book, container, false);

        database = new UserDatabase(mContext);
        Constants.selectedCommunityIds = "";

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
        ivScanner = (ImageView)view.findViewById(R.id.ivScanner);

        saveDetailsBtn = (Button)view.findViewById(R.id.sendBtn);
        rgSaleType = (RadioGroup) view.findViewById(R.id.rgSaleType);
        rbSale = (RadioButton) view.findViewById(R.id.rbSale);
        rbRent = (RadioButton) view.findViewById(R.id.rbRent);
        rbBoth = (RadioButton) view.findViewById(R.id.rbBoth);

        //Fields
        spCat = (Spinner)view.findViewById(R.id.spCat);
        spCondition = (Spinner)view.findViewById(R.id.spCondition);
        etName = (MyEditText)view.findViewById(R.id.etName);
        etAuthor = (MyEditText)view.findViewById(R.id.etAuthor);
        etIsbn = (MyEditText)view.findViewById(R.id.etIsbn);
        etDescription = (MyEditText)view.findViewById(R.id.etDescription);
        etRentDuration = (MyEditText)view.findViewById(R.id.etRentDuration);
        etPrice = (MyEditText)view.findViewById(R.id.etPrice);
        etRentPrice = (MyEditText)view.findViewById(R.id.etRentPrice);
        etSecurityMoney = (MyEditText)view.findViewById(R.id.etSecurityMoney);
        etMrp = (MyEditText)view.findViewById(R.id.etMrp);
        iv_image = (ImageView)view.findViewById(R.id.iv_image);
        tvOr = (MyTextView)view.findViewById(R.id.tvOr);
        tvUploadImage = (MyTextView)view.findViewById(R.id.tvUploadImage);

        tvIsbnDigitCount = (MyTextView)view.findViewById(R.id.tvIsbnDigitCount);
        scrollView = (NestedScrollView)view.findViewById(R.id.scrollView);
        llCat = (LinearLayout)view.findViewById(R.id.llCat);
        llConditionType = (LinearLayout)view.findViewById(R.id.llConditionType);
        llIsbn = (LinearLayout)view.findViewById(R.id.llIsbn);

        tvSaleType = (TextView)view.findViewById(R.id.tvSaleType);
        starSaleType = (TextView)view.findViewById(R.id.starSaleType);
        tvConditionType = (TextView)view.findViewById(R.id.tvConditionType);
        starConditionType = (TextView)view.findViewById(R.id.starConditionType);
        tvMrp = (TextView)view.findViewById(R.id.tvMrp);
        starMrp = (TextView)view.findViewById(R.id.starMrp);
        tvSecurity = (TextView)view.findViewById(R.id.tvSecurity);
        tvSalePrice = (TextView)view.findViewById(R.id.tvSalePrice);
        tvRentPrice = (TextView)view.findViewById(R.id.tvRentPrice);
        tvRentDuration = (TextView)view.findViewById(R.id.tvRentDuration);
        starRentDuration = (TextView)view.findViewById(R.id.starRentDuration);

        bookName = getArguments().getString("name");
        authorName = getArguments().getString("author");
        isbnNum = getArguments().getString("isbn");
        description = getArguments().getString("description");
        imgUrl = getArguments().getString("imgUrl");
        rental_price = getArguments().getString("rental_price");
        rental_duration = getArguments().getString("rental_duration");
        bookSalePrice = getArguments().getString("book_price");
        bookCatId = getArguments().getString("category_id");
        bookCatName = getArguments().getString("category_id");
        bookUpdateId = getArguments().getString("book_id");
        bookMrp = getArguments().getString("mrp");
        bookQuantity = getArguments().getString("quantity");
        bookSaleType = getArguments().getString("sale_type");
        bookCoditionType = getArguments().getString("book_condition");
        bookSecurityMoney = getArguments().getString("security_money");
        bookGiveAway = getArguments().getString("giveaway_status");


        catName = bookCatName;
        bookId = bookUpdateId;

        etName.setText(bookName);
        etAuthor.setText(authorName);
        etDescription.setText(description);
        etIsbn.setText(isbnNum);

        if (imgUrl.equals("") || imgUrl.equals("null")){
            Glide.with(this).load("").into(iv_image);
        }else {
            Glide.with(this).load(imgUrl).into(iv_image);
        }

        etMrp.setText(bookMrp);
        etPrice.setText(bookSalePrice);

        cbOpen.setChecked(true);
        rbSale.setChecked(true);
        cbOpen.setButtonTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary)));
        rbSale.setButtonTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary)));
        saleType = "For Sale";
        tvSecurity.setTextColor(Color.parseColor("#C4C4C4"));
        etSecurityMoney.setText("0");
        etSecurityMoney.setEnabled(false);
        tvSalePrice.setTextColor(Color.parseColor("#3C3C3C"));
        etPrice.setEnabled(true);
        //etPrice.setText("");
        tvRentPrice.setTextColor(Color.parseColor("#C4C4C4"));
        etRentPrice.setEnabled(false);
        etRentPrice.setText("0");
        tvRentDuration.setTextColor(Color.parseColor("#C4C4C4"));
        etRentDuration.setEnabled(false);
        etRentDuration.setText("365");
        starRentDuration.setVisibility(View.INVISIBLE);

        etPrice.setText(String.valueOf(price));

        etSecurityMoney.setBackground(getResources().getDrawable(R.drawable.bg_rectangle_new));
        etPrice.setBackground(getResources().getDrawable(R.drawable.et_background));
        etRentPrice.setBackground(getResources().getDrawable(R.drawable.bg_rectangle_new));
        etRentDuration.setBackground(getResources().getDrawable(R.drawable.bg_rectangle_new));

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

        checkCommunity();

        rlExpandView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                llCommunityList.setVisibility((llCommunityList.getVisibility() == View.VISIBLE)
                        ? View.GONE : View.VISIBLE);
                if (llCommunityList.getVisibility() == View.VISIBLE){
                    ivExpand.setImageResource(R.drawable.ic_baseline_remove_24);
                    tvExpand.setText("Collapse");
                }else {
                    ivExpand.setImageResource(R.drawable.ic_add);
                    tvExpand.setText("Expand");
                }
            }
        });


        rgSaleType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                int selectedId = radioGroup.getCheckedRadioButtonId();
                RadioButton radioButton = (RadioButton) view.findViewById(selectedId);
                if (!etMrp.getText().toString().equals("")){
                    price = (int)(Integer.parseInt(etMrp.getText().toString()) / 100) * conditionValue +1;
                }

                if (radioButton.getText().equals("Sell")){
                    saleType = "For Sale";
                    rbSale.setButtonTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary)));
                    rbRent.setButtonTintList(ColorStateList.valueOf(getResources().getColor(R.color.black_fade)));
                    rbBoth.setButtonTintList(ColorStateList.valueOf(getResources().getColor(R.color.black_fade)));

                    tvSecurity.setTextColor(Color.parseColor("#C4C4C4"));
                    etSecurityMoney.setText("0");
                    etSecurityMoney.setEnabled(false);
                    tvSalePrice.setTextColor(Color.parseColor("#3C3C3C"));
                    etPrice.setEnabled(true);
                    tvRentPrice.setTextColor(Color.parseColor("#C4C4C4"));
                    etRentPrice.setEnabled(false);
                    etRentPrice.setText("0");
                    tvRentDuration.setTextColor(Color.parseColor("#C4C4C4"));
                    etRentDuration.setEnabled(false);
                    etRentDuration.setText("365");
                    starRentDuration.setVisibility(View.INVISIBLE);

                    etPrice.setText(String.valueOf(price));

                    etSecurityMoney.setBackground(getResources().getDrawable(R.drawable.bg_rectangle_new));
                    etPrice.setBackground(getResources().getDrawable(R.drawable.et_background));
                    etRentPrice.setBackground(getResources().getDrawable(R.drawable.bg_rectangle_new));
                    etRentDuration.setBackground(getResources().getDrawable(R.drawable.bg_rectangle_new));

                }else if (radioButton.getText().equals("Rent")){
                    saleType = "For Rent";
                    rbRent.setButtonTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary)));
                    rbSale.setButtonTintList(ColorStateList.valueOf(getResources().getColor(R.color.black_fade)));
                    rbBoth.setButtonTintList(ColorStateList.valueOf(getResources().getColor(R.color.black_fade)));

                    tvSecurity.setTextColor(Color.parseColor("#3C3C3C"));
                    etSecurityMoney.setText(etMrp.getText().toString().trim());
                    etSecurityMoney.setEnabled(true);
                    tvSalePrice.setTextColor(Color.parseColor("#C4C4C4"));
                    etPrice.setEnabled(false);
                    etPrice.setText("0");
                    tvRentPrice.setTextColor(Color.parseColor("#3C3C3C"));
                    etRentPrice.setEnabled(true);
                    tvRentDuration.setTextColor(Color.parseColor("#3C3C3C"));
                    etRentDuration.setEnabled(true);
                    etRentDuration.setText("30");
                    starRentDuration.setVisibility(View.VISIBLE);

                    etRentPrice.setText(String.valueOf((price/Integer.parseInt(Rent_divisor))+1));
                    etSecurityMoney.setText(etMrp.getText().toString());

                    etSecurityMoney.setBackground(getResources().getDrawable(R.drawable.et_background));
                    etPrice.setBackground(getResources().getDrawable(R.drawable.bg_rectangle_new));
                    etRentPrice.setBackground(getResources().getDrawable(R.drawable.et_background));
                    etRentDuration.setBackground(getResources().getDrawable(R.drawable.et_background));

                }else{
                    saleType = "Both";
                    rbBoth.setButtonTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary)));
                    rbRent.setButtonTintList(ColorStateList.valueOf(getResources().getColor(R.color.black_fade)));
                    rbSale.setButtonTintList(ColorStateList.valueOf(getResources().getColor(R.color.black_fade)));

                    tvSecurity.setTextColor(Color.parseColor("#3C3C3C"));
                    etSecurityMoney.setText(etMrp.getText().toString().trim());
                    etSecurityMoney.setEnabled(true);
                    tvSalePrice.setTextColor(Color.parseColor("#3C3C3C"));
                    etPrice.setEnabled(true);
                    tvRentPrice.setTextColor(Color.parseColor("#3C3C3C"));
                    etRentPrice.setEnabled(true);
                    tvRentDuration.setTextColor(Color.parseColor("#3C3C3C"));
                    etRentDuration.setEnabled(true);
                    etRentDuration.setText("30");
                    starRentDuration.setVisibility(View.VISIBLE);

                    etPrice.setText(String.valueOf(price));
                    etRentPrice.setText(String.valueOf((price/Integer.parseInt(Rent_divisor))+1));
                    etSecurityMoney.setText(etMrp.getText().toString());

                    etSecurityMoney.setBackground(getResources().getDrawable(R.drawable.et_background));
                    etPrice.setBackground(getResources().getDrawable(R.drawable.et_background));
                    etRentPrice.setBackground(getResources().getDrawable(R.drawable.et_background));
                    etRentDuration.setBackground(getResources().getDrawable(R.drawable.et_background));
                }
            }
        });


        if (bookGiveAway.equals("yes")){
            showGiveawayView();
            cbGiveaway.setChecked(true);
            cbGiveaway.setButtonTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary)));
        }

        cbGiveaway.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cbGiveaway.isChecked()){
                    showGiveawayView();
                    cbGiveaway.setButtonTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary)));
                }else {
                    showWithoutGiveawayView();
                    cbGiveaway.setButtonTintList(ColorStateList.valueOf(getResources().getColor(R.color.black_fade)));
                }
            }
        });

        etIsbn.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String newText = charSequence.toString();
                llIsbn.setBackground(getResources().getDrawable(R.drawable.et_background));
                etIsbn.setBackground(getResources().getDrawable(R.drawable.et_background));
                tvIsbnDigitCount.setText("ISBN Number ( "+newText.length()+"/13 )");
                if (newText.length() == 10 || newText.length() == 13){
                    searchBookByIsbn(newText);
                }else {
                    etName.setText("");
                    etAuthor.setText("");
                    etDescription.setText("");
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        etName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                etName.setBackground(getResources().getDrawable(R.drawable.et_background));
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        etMrp.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (conditionValue != 1){
                    String mrp = charSequence.toString();
                    if (!mrp.equals("")){
                        int mrpValue = Integer.parseInt(mrp);
                        int price =  (int)(mrpValue / 100) * conditionValue +1;
                        if (rbRent.isChecked()){
                            etPrice.setText("0");
                            etRentPrice.setText(String.valueOf((price/Integer.parseInt(Rent_divisor))+1));
                            etSecurityMoney.setText(mrp);
                        }else if (rbBoth.isChecked()){
                            etPrice.setText(String.valueOf(price));
                            etRentPrice.setText(String.valueOf((price/Integer.parseInt(Rent_divisor))+1));
                            etSecurityMoney.setText(mrp);
                        }else if (rbSale.isChecked()){
                            etPrice.setText(String.valueOf(price));
                            etRentPrice.setText("0");
                            etSecurityMoney.setText("0");
                        }
                    }
                }
                /*if (rbSale.equals("For Sale")){
                    if (saleType.equals("For Sale")){
                        etMrp.setBackground(getResources().getDrawable(R.drawable.et_background));
                        etPrice.setBackground(getResources().getDrawable(R.drawable.et_background));
                        etRentPrice.setBackground(getResources().getDrawable(R.drawable.bg_rectangle_new));
                        etSecurityMoney.setBackground(getResources().getDrawable(R.drawable.bg_rectangle_new));
                    }
                }*/
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        getRulesValue();
        getCategoryList();

        ivScanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dexter.withContext(mContext).withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA)
                        .withListener(new MultiplePermissionsListener() {
                            @Override
                            public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                                Intent intent = new Intent(mContext, QRCodeScannerActivity.class);
                                startActivityForResult(intent,1);
                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                                permissionToken.continuePermissionRequest();
                            }
                        }).check();
            }
        });

        tvUploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dexter.withContext(mContext).withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA)
                        .withListener(new MultiplePermissionsListener() {
                            @Override
                            public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                                showImagePicker();
                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                                permissionToken.continuePermissionRequest();
                            }
                        }).check();
            }
        });

        saveDetailsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = etName.getText().toString().trim();
                String author = etAuthor.getText().toString().trim();
                String isbn = etIsbn.getText().toString().trim();
                String description = etDescription.getText().toString().trim();
                String duration = etRentDuration.getText().toString().trim();
                String priceMrp = etMrp.getText().toString().trim();
                String rental_price = etRentPrice.getText().toString().trim();
                String sale_price = etPrice.getText().toString().trim();
                String securityPrice = etSecurityMoney.getText().toString().trim();

                if (rbSale.isChecked()){
                    saleType = "For Sale";
                }else if (rbRent.isChecked()){
                    saleType = "For Rent";
                }else {
                    saleType = "Both";
                }

                List<EditText> myInputFields = new ArrayList();
                if (cbGiveaway.isChecked()){
                    myInputFields.add(etIsbn);
                    myInputFields.add(etName);
                    myInputFields.add(etRentDuration);
                }else {
                    myInputFields.add(etIsbn);
                    myInputFields.add(etName);
                    myInputFields.add(etMrp);
                    myInputFields.add(etPrice);
                    myInputFields.add(etRentPrice);
                    myInputFields.add(etSecurityMoney);
                    myInputFields.add(etRentDuration);
                }

                counter = 0;

                for (int i=0;i<myInputFields.size();i++) {
                    if (myInputFields.get(i).getText().toString().equals("")) {
                        myInputFields.get(i).setBackground(getResources().getDrawable(R.drawable.bg_error_fields_new));
                        if (myInputFields.get(0).getText().toString().equals("")) {
                            llIsbn.setBackground(getResources().getDrawable(R.drawable.bg_error_fields_new));
                            scrollView.fullScroll(View.FOCUS_UP);
                            myInputFields.get(0).requestFocus();
                        } else if (myInputFields.get(1).getText().toString().equals("")) {
                            scrollView.fullScroll(View.FOCUS_UP);
                            myInputFields.get(1).requestFocus();
                        } else if (categoryId.equals("")) {
                            llCat.setBackground(getResources().getDrawable(R.drawable.bg_error_fields_new));
                            scrollView.scrollTo(0, etDescription.getTop());
                        } else if (myInputFields.get(2).getText().toString().equals("")) {
                            scrollView.fullScroll(View.FOCUS_DOWN);
                            myInputFields.get(2).requestFocus();
                        } else if (myInputFields.get(3).getText().toString().equals("")) {
                            scrollView.scrollTo(0, llCat.getTop());
                            myInputFields.get(3).requestFocus();
                        } else if (myInputFields.get(4).getText().toString().equals("")) {
                            scrollView.fullScroll(View.FOCUS_DOWN);
                            scrollView.scrollTo(0, llCat.getTop());
                        } else if (myInputFields.get(5).getText().toString().equals("")) {
                            scrollView.fullScroll(View.FOCUS_DOWN);
                            scrollView.scrollTo(0, llCat.getTop());
                        } else if (myInputFields.get(6).getText().toString().equals("")) {
                            scrollView.fullScroll(View.FOCUS_DOWN);
                            scrollView.scrollTo(0, llCat.getTop());
                        } else if (myInputFields.get(7).getText().toString().equals("")) {
                            scrollView.fullScroll(View.FOCUS_DOWN);
                            scrollView.scrollTo(0, llCat.getTop());
                        }
                        counter++;
                    } else {
                        myInputFields.get(i).setBackground(getResources().getDrawable(R.drawable.et_background));
                    }
                }

                    if (counter > 0){
                        return;
                    }else {
                        if (saleType.equals("")){
                            Toast.makeText(mContext, "Select sale type.", Toast.LENGTH_SHORT).show();
                            scrollView.fullScroll(View.FOCUS_UP);
                            llIsbn.setBackground(getResources().getDrawable(R.drawable.bg_error_fields_new));
                        }else if (isbn.equals("")){
                            Toast.makeText(mContext, "Enter Isbn no", Toast.LENGTH_SHORT).show();
                            scrollView.fullScroll(View.FOCUS_UP);
                            etIsbn.setBackground(getResources().getDrawable(R.drawable.bg_error_fields_new));
                        }/*else if (isbn.length() == 9 || isbn.length() == 12){
                            Toast.makeText(mContext, "ISBN number must be 10 or 13 digit.", Toast.LENGTH_SHORT).show();
                            scrollView.fullScroll(View.FOCUS_UP);
                            etIsbn.setBackground(getResources().getDrawable(R.drawable.bg_error_fields_new));
                        }*/else if (categoryId.equals("")){
                            Toast.makeText(mContext, "Select Category", Toast.LENGTH_SHORT).show();
                            llCat.setBackground(getResources().getDrawable(R.drawable.bg_error_fields_new));
                            scrollView.scrollTo(0, etDescription.getTop());
                        }else if (thumbnail.equals("")){
                            if (Constants.SelectedBookImage.equals("")){
                                Toast.makeText(mContext, "Provide Book Image.", Toast.LENGTH_SHORT).show();
                                iv_image.setBackground(getResources().getDrawable(R.drawable.bg_error_fields));
                            }else {
                                Log.d("allValues","Name :"+name+"\n"+"author :"+author+"\n"+
                                        "isbn :"+isbn+"\n"+"description :"+description+"\n"+
                                        "duration :"+duration+"\n"+"thumbnail :"+thumbnail+"\n"+
                                        "priceMrp :"+priceMrp+"\n"+"rental_price :"+rental_price+"\n"+
                                        "sale_price :"+sale_price+"\n"+"securityPrice :"+securityPrice+"\n"+
                                        "giveawayStatus :"+giveawayStatus+"\n"+"categoryId :"+categoryId+"\n"+
                                        "saleType :"+saleType+"\n"+"bookConditionType :"+bookConditionType+"\n"+
                                        "isOpenLib :"+isOpenLib+"\n"+"communityIds :"+Constants.selectedCommunityIds+"\n");
                                saveBookDetails(name,author,isbn,description,duration,thumbnail,priceMrp,rental_price,sale_price,securityPrice,giveawayStatus);
                            }
                        }else {
                            Log.d("allValues","aaaaa : "+giveawayStatus);
                            Log.d("allValues","Name :"+name+"\n"+"author :"+author+"\n"+
                                    "isbn :"+isbn+"\n"+"description :"+description+"\n"+
                                    "duration :"+duration+"\n"+"thumbnail :"+thumbnail+"\n"+
                                    "priceMrp :"+priceMrp+"\n"+"rental_price :"+rental_price+"\n"+
                                    "sale_price :"+sale_price+"\n"+"securityPrice :"+securityPrice+"\n"+
                                    "giveawayStatus :"+giveawayStatus+"\n"+"categoryId :"+categoryId+"\n"+
                                    "saleType :"+saleType+"\n"+"bookConditionType :"+bookConditionType+"\n"+
                                    "isOpenLib :"+isOpenLib+"\n"+"communityIds :"+Constants.selectedCommunityIds+"\n");
                            saveBookDetails(name,author,isbn,description,duration,thumbnail,priceMrp,rental_price,sale_price,securityPrice,giveawayStatus);
                        }
                    }

            }
        });

        return view;
    }

    private void saveBookDetails(String name, String author, String isbn, String description, String duration, String bookImgUrl,String priceMrp,String rentalPrice,String salePrice,String securityPrice,String giveaway) {
        final ProgressDialog loginDialog = new ProgressDialog(mContext);
        loginDialog.setMessage("Please wait..");
        loginDialog.setCancelable(false);
        loginDialog.show();
        Holders holders = AllApiClass.getInstance().getApi();
        Call<BookEntryModel> call = holders.uploadBooks(name,database.getUserId(),"",author,"",description,bookImgUrl,isbn,duration,bookId,categoryId,priceMrp,rentalPrice,salePrice,securityPrice,"1",saleType,bookConditionType,giveaway,"",isOpenLib,Constants.selectedCommunityIds);
        call.enqueue(new Callback<BookEntryModel>() {
            @Override
            public void onResponse(Call<BookEntryModel> call, Response<BookEntryModel> response) {
                if (response.isSuccessful()){
                    if (response.body().getResponse().getCode() == 1){
                        loginDialog.dismiss();
                        if (!Constants.SelectedBookImage.equals("")){
                            uploadBookImage(response.body().getResponse().getBook_id());
                        }else {
                            MyBooksActivity.flag = 1;
                            CollectApartmentBooksActivity.flag = 1;
                            /*editor.clear();
                            editor.commit();*/
                            Toast.makeText(mContext, ""+response.body().getResponse().getMessage(), Toast.LENGTH_SHORT).show();
                            getActivity().onBackPressed();
                        }
                    }else {
                        loginDialog.dismiss();
                        Toast.makeText(mContext, ""+response.body().getResponse().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }else {
                    loginDialog.dismiss();
                    Toast.makeText(mContext, "Something went wrong.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<BookEntryModel> call, Throwable t) {
                loginDialog.dismiss();
                Toast.makeText(mContext, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void uploadBookImage(String book_id) {
        //Toast.makeText(this, "Image is uploaded successfully.", Toast.LENGTH_SHORT).show();
        final ProgressDialog progressBar = new ProgressDialog(mContext);
        progressBar.setMessage("Please wait...");
        progressBar.setCancelable(false);
        progressBar.show();
        Holders holders = AllApiClass.getInstance().getApi();

        MultipartBody.Part part = null;
        try {
            File file = new File(Constants.SelectedBookImage);
            RequestBody fileReqBody = RequestBody.create(MediaType.parse("image/*"), file);
            part = MultipartBody.Part.createFormData("image", file.getName(), fileReqBody);
        }catch (Exception e){
            e.printStackTrace();
        }
        Log.d("myFilePath",""+part);
        RequestBody book_idReq = RequestBody.create(MediaType.parse("multipart/form-data"), book_id);

        Call<SubmitDataResModel> resCall = holders.uploadBookImage(book_idReq,part);
        resCall.enqueue(new Callback<SubmitDataResModel>() {
            @Override
            public void onResponse(Call<SubmitDataResModel> call, Response<SubmitDataResModel> response) {
                if (response.isSuccessful()){
                    if (response.body().getResponse().getCode() == 1){
                        progressBar.dismiss();
                        Constants.SelectedBookImage = "";
                        MyBooksActivity.flag = 1;
                        CollectApartmentBooksActivity.flag = 1;
                        Toast.makeText(mContext, "Book Uploaded Successfully.", Toast.LENGTH_SHORT).show();
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

    private void showImagePicker() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(mContext);
        final View layoutView = LayoutInflater.from(mContext).inflate(R.layout.layout_image_chooser, null);
        dialogBuilder.setView(layoutView);
        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation1;
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();
        alertDialog.setCancelable(true);
        alertDialog.getWindow().setLayout(850, ViewGroup.LayoutParams.WRAP_CONTENT);

        TextView tvGallery = layoutView.findViewById(R.id.tvGallery);
        TextView tvCamera = layoutView.findViewById(R.id.tvCamera);

        tvGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseBookImage();
                alertDialog.dismiss();
            }
        });

        tvCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Constants.openPageFrom = "fromBookUpload";
                chooseBookImageFromCamera();
                alertDialog.dismiss();
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

    private void chooseBookImage() {
        Constants.openPageFrom = "fromBookUpload";
        Intent intent = new Intent(mContext, ImageGallery.class);
        startActivity(intent);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1){
            if (resultCode == RESULT_OK) {
                // Get String data from Intent
                String returnString = data.getStringExtra("code");
                etIsbn.setText(returnString);
                searchBookByIsbn(returnString);
            }
        }else {
            if (resultCode == RESULT_OK) {
                if (currentPhotoPath.equals("")){
                    Toast.makeText(mContext, "Failed to save image.Please try again. ", Toast.LENGTH_SHORT).show();
                }else {
                    Constants.SelectedBookImage = currentPhotoPath;
                    Log.d("mmmmm : ","imgUri :"+Constants.SelectedBookImage);
                    Intent intent = new Intent(mContext, ImageCroperActivity.class);
                    startActivity(intent);
                }
            }
        }
    }

    @Override
    public void onDestroy() {
        Constants.SelectedBookImage = "";
        Constants.selectedCommunityIds = "";
        Constants.selectedCommunityList.clear();
        super.onDestroy();
    }

    @Override
    public void onResume() {
        if (!Constants.SelectedBookImage.equals("")){
            tvOr.setVisibility(View.VISIBLE);
            iv_image.setVisibility(View.VISIBLE);
            Glide.with(this).load(Constants.SelectedBookImage).placeholder(R.drawable.no_img).into(iv_image);
            iv_image.setBackground(getResources().getDrawable(R.drawable.bg_rectangle));
            //etImgUrl.setBackground(getResources().getDrawable(R.drawable.bg_search));
        }else {
            tvOr.setVisibility(View.VISIBLE);
            iv_image.setVisibility(View.VISIBLE);
            if (thumbnail.equals("")){
                if (imgUrl.equals("")){
                    Glide.with(this).load(R.drawable.no_img).placeholder(R.drawable.no_img).into(iv_image);
                }else {
                    Glide.with(this).load(imgUrl).placeholder(R.drawable.no_img).into(iv_image);
                }
            }else {
                Glide.with(this).load(thumbnail).placeholder(R.drawable.no_img).into(iv_image);
            }

        }
        Log.d("myCommunityId","Community_id : "+Constants.selectedCommunityIds);
        Log.d("myCommunityId","isOwnLibrary : "+Constants.isOwnLibrary);
        Log.d("myFile","Book Image : "+Constants.SelectedBookImage);
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

                                    if (!temp_cat_list.get(i).getCategory_name().equals("Select Category")){
                                        categoryId = temp_cat_list.get(i).getCategory_id();
                                        llCat.setBackground(getResources().getDrawable(R.drawable.et_background));
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

    private void getRulesValue() {
        //spCondition.setSelection(0);
        Holders holders = AllApiClass.getInstance().getApi();
        Call<JsonObject> call = holders.getRuleOfBook();
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()){
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().toString());
                        JSONObject res = jsonObject.getJSONObject("response");
                        if (res.getString("code").equals("1")){
                            As_good_as_new = res.getString("As_good_as_new");
                            Marked_texts = res.getString("Marked_texts");
                            Torn_or_pale_pages = res.getString("Torn_or_pale_pages");
                            Marked_texts_Torn_cover_page = res.getString("Marked_texts_Torn_cover_page");
                            Security_price_type = res.getString("Security_price_type");
                            Rent_divisor = res.getString("Rent_divisor");
                            getBookConditions();
                        }else {
                            Toast.makeText(mContext, "Rules not found !", Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(mContext, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(mContext, "Something went wrong !", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(mContext, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getBookConditions() {
        //condition_type_list.add(new ConditionTypeModel("Select Condition"));
        condition_type_list.add(new ConditionTypeModel("As good as new"));
        condition_type_list.add(new ConditionTypeModel("Marked texts"));
        condition_type_list.add(new ConditionTypeModel("Torn or pale pages"));
        condition_type_list.add(new ConditionTypeModel("Marked texts & Torn cover page"));

        spCondition.setAdapter(new ConditionAdapterNew(mContext,condition_type_list));

        if (bookCoditionType.equals("")){
            spCondition.setSelection(0);
        }else if (bookCoditionType.equals("As good as new")){
            spCondition.setSelection(0);
        }else if (bookCoditionType.equals("Marked texts")){
            spCondition.setSelection(1);
        }else if (bookCoditionType.equals("Torn or pale pages")){
            spCondition.setSelection(2);
        }else if (bookCoditionType.equals("Marked texts & Torn cover page")){
            spCondition.setSelection(3);
        }else {
            spCondition.setSelection(0);
        }

        spCondition.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                bookConditionType = condition_type_list.get(i).getCondition_type();
                if (bookConditionType.equals("Select Condition")){
                    conditionValue = 1;
                    etMrp.setEnabled(false);
                    etPrice.setEnabled(false);
                    etRentPrice.setEnabled(false);
                    etSecurityMoney.setEnabled(false);
                    etMrp.setText("");
                    etPrice.setText("");
                    etRentPrice.setText("");
                    etSecurityMoney.setText("");
                }else if (bookConditionType.equals("As good as new")){
                    conditionValue = 100 - Integer.parseInt(As_good_as_new);
                    if (!etMrp.getText().toString().trim().equals("")){
                        int mrpValue = Integer.parseInt(etMrp.getText().toString().trim());
                        //int price =  (conditionValue / 100 ) * mrpValue;
                        int price =  (int)(mrpValue / 100) * conditionValue +1;
                        //int priceRent =  (int)(mrpValue / 100) * 95;
                        if (rbRent.isChecked()){
                            etPrice.setText("0");
                            etRentPrice.setText(String.valueOf((price/Integer.parseInt(Rent_divisor))+1));
                            etSecurityMoney.setText(etMrp.getText().toString().trim());
                        }else if (rbBoth.isChecked()){
                            etPrice.setText(String.valueOf(price));
                            etRentPrice.setText(String.valueOf((price/Integer.parseInt(Rent_divisor))+1));
                            etSecurityMoney.setText(etMrp.getText().toString().trim());
                        }else if (rbSale.isChecked()){
                            etPrice.setText(String.valueOf(price));
                            etRentPrice.setText("0");
                            etSecurityMoney.setText("0");
                        }
                    }
                }else if (bookConditionType.equals("Marked texts")){
                    conditionValue = 100 - Integer.parseInt(Marked_texts);
                    if (!etMrp.getText().toString().trim().equals("")){
                        int mrpValue = Integer.parseInt(etMrp.getText().toString().trim());
                        //int price =  (conditionValue / 100 ) * mrpValue;
                        int price =  (int)(mrpValue / 100) * conditionValue +1;
                        //etPrice.setText(String.valueOf(price));
                        //int priceRent =  (int)(mrpValue / 100) * 95;
                        if (rbRent.isChecked()){
                            etPrice.setText("0");
                            etRentPrice.setText(String.valueOf((price/Integer.parseInt(Rent_divisor))+1));
                            etSecurityMoney.setText(etMrp.getText().toString().trim());
                        }else if (rbBoth.isChecked()){
                            etPrice.setText(String.valueOf(price));
                            etRentPrice.setText(String.valueOf((price/Integer.parseInt(Rent_divisor))+1));
                            etSecurityMoney.setText(etMrp.getText().toString().trim());
                        }else if (rbSale.isChecked()){
                            etPrice.setText(String.valueOf(price));
                            etRentPrice.setText("0");
                            etSecurityMoney.setText("0");
                        }
                    }
                }else if (bookConditionType.equals("Torn or pale pages")){
                    conditionValue = 100 - Integer.parseInt(Torn_or_pale_pages);
                    if (!etMrp.getText().toString().trim().equals("")){
                        int mrpValue = Integer.parseInt(etMrp.getText().toString().trim());
                        int price =  (int)(mrpValue / 100) * conditionValue +1;
                        if (rbRent.isChecked()){
                            etPrice.setText("0");
                            etRentPrice.setText(String.valueOf((price/Integer.parseInt(Rent_divisor))+1));
                            etSecurityMoney.setText(etMrp.getText().toString().trim());
                        }else if (rbBoth.isChecked()){
                            etPrice.setText(String.valueOf(price));
                            etRentPrice.setText(String.valueOf((price/Integer.parseInt(Rent_divisor))+1));
                            etSecurityMoney.setText(etMrp.getText().toString().trim());
                        }else if (rbSale.isChecked()){
                            etPrice.setText(String.valueOf(price));
                            etRentPrice.setText("0");
                            etSecurityMoney.setText("0");
                        }
                    }
                }else if (bookConditionType.equals("Marked texts & Torn cover page")){
                    conditionValue = 100 - Integer.parseInt(Marked_texts_Torn_cover_page);
                    if (!etMrp.getText().toString().trim().equals("")){
                        int mrpValue = Integer.parseInt(etMrp.getText().toString().trim());
                        int price =  (int)(mrpValue / 100) * conditionValue +1;
                        if (rbRent.isChecked()){
                            etPrice.setText("0");
                            etRentPrice.setText(String.valueOf((price/Integer.parseInt(Rent_divisor))+1));
                            etSecurityMoney.setText(etMrp.getText().toString().trim());
                        }else if (rbBoth.isChecked()){
                            etPrice.setText(String.valueOf(price));
                            etRentPrice.setText(String.valueOf((price/Integer.parseInt(Rent_divisor))+1));
                            etSecurityMoney.setText(etMrp.getText().toString().trim());
                        }else if (rbSale.isChecked()){
                            etPrice.setText(String.valueOf(price));
                            etRentPrice.setText("0");
                            etSecurityMoney.setText("0");
                        }
                    }
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

    private void searchBookByIsbn(String isbn) {
        //progBar.setVisibility(View.VISIBLE);
        Holders holders = AllApiClass.getInstance().getApiBooks();
        Call<JsonObject> call = holders.getBooks("isbn:"+isbn);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()){
                    //progBar.setVisibility(View.GONE);
                    Log.d("myBookResponse",response.body().toString());
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().toString());
                        String kind = jsonObject.optString("kind");
                        //Toast.makeText(BookListActivity.this, ""+kind, Toast.LENGTH_SHORT).show();
                        JSONArray jsonArray = jsonObject.getJSONArray("items");
                        for (int i = 0; i<jsonArray.length();i++){
                            JSONObject object = jsonArray.getJSONObject(i);
                            JSONObject obj = object.getJSONObject("volumeInfo");
                            String name = obj.optString("title");
                            String author = obj.getJSONArray("authors").get(0).toString();
                            String publishDate = obj.optString("publishedDate");
                            String description = obj.optString("description");
                            JSONObject imgObj = obj.optJSONObject("imageLinks");

                            if (imgObj == null){
                                smallThumbnail = "";
                                thumbnail = "";
                            }else {
                                smallThumbnail = imgObj.optString("smallThumbnail");
                                thumbnail = imgObj.optString("thumbnail");
                            }

                            etName.setText(name);
                            etAuthor.setText(author);
                            etDescription.setText(description);

                            if (thumbnail.equals("") || thumbnail.equals("null")){
                                smallThumbnail = "";
                                thumbnail = "";
                                tvOr.setVisibility(View.GONE);
                                iv_image.setVisibility(View.GONE);
                            }else {
                                tvOr.setVisibility(View.VISIBLE);
                                iv_image.setVisibility(View.VISIBLE);
                                Glide.with(mContext).load(thumbnail).into(iv_image);
                            }

                            getMrp(isbn);

                        }


                    } catch (JSONException e) {
                        etName.setText("");
                        etAuthor.setText("");
                        etDescription.setText("");
                        e.printStackTrace();
                    }

                }else {
                    //progBar.setVisibility(View.GONE);
                    etName.setText("");
                    etAuthor.setText("");
                    etDescription.setText("");
                    Toast.makeText(mContext, "Something went wrong !", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                // progBar.setVisibility(View.GONE);
                etName.setText("");
                etAuthor.setText("");
                etDescription.setText("");
                Toast.makeText(mContext, "Server not responding yet !", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getMrp(String isbn) {
        Holders holders = AllApiClass.getInstance().getApiBooksMrp();
        Call<JsonObject> call = holders.getBookMrp(APi_key,isbn);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()){
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().toString());
                        Log.d("mrpResponse",response.body().toString());
                        JSONObject bookJson = jsonObject.getJSONObject("book");
                        String msrp = bookJson.getString("msrp");
                        String bImage = bookJson.getString("image");
                        //String mrp = "";
                        if (thumbnail.equals("") || thumbnail.equals("null")){
                            if (bImage.equals("") || bImage.equals("null")){
                                smallThumbnail = "";
                                thumbnail = "";
                                Glide.with(mContext).load("https://sales.arecontvision.com/images/products/img_placeholder_50618_xl.jpg").into(iv_image);
                                tvOr.setVisibility(View.GONE);
                                iv_image.setVisibility(View.GONE);
                            }else {
                                smallThumbnail = bImage;
                                thumbnail = bImage;
                                Glide.with(mContext).load(bImage).into(iv_image);
                                //Glide.with(UploadBookDetails.this).load("https://sales.arecontvision.com/images/products/img_placeholder_50618_xl.jpg").into(iv_image);
                                tvOr.setVisibility(View.VISIBLE);
                                iv_image.setVisibility(View.VISIBLE);
                            }
                        }

                        if (msrp.equals("") || msrp.equals("null") || msrp.equals("0.00")){
                            etMrp.setText("");
                            tvMrp.setText("Purchase Price");
                            //tvMrp.setText("Online Price");
                            Log.d("mrpNew","0");
                        }else {
                            Log.d("mrpNew",msrp);
                            tvMrp.setText("Online Price");
                            if (msrp.contains(".")){
                                if (Double.parseDouble(msrp) < 45.00){
                                    double mrpVal = (Double.parseDouble(msrp) * Double.parseDouble(dollerValue)) / Double.parseDouble(division);
                                    int finalValue = (int) Math.round(mrpVal);
                                    etMrp.setText(String.valueOf(finalValue));
                                }else {
                                    int finalValue = (int) Math.round(Double.parseDouble(msrp));
                                    etMrp.setText(String.valueOf(finalValue));
                                }
                            }else {
                                if (Integer.parseInt(msrp) < 45){
                                    int mrpVal = (Integer.parseInt(msrp) * Integer.parseInt(dollerValue)) / Integer.parseInt(division);
                                    etMrp.setText(String.valueOf(mrpVal));
                                }else {
                                    etMrp.setText(msrp);
                                }
                            }

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.d("msgError",""+e.getMessage());
                        etMrp.setText("");
                        tvMrp.setText("Purchase Price");
                    }
                }else {
                    Log.d("msgError","Something went wrong");
                    etMrp.setText("");
                    tvMrp.setText("Purchase Price");
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.d("msgError","Network issue.");
                etMrp.setText("");
                tvMrp.setText("Purchase Price");
            }
        });
    }

    private void showWithoutGiveawayView() {
        giveawayStatus = "no";
        etMrp.setText("");
        etPrice.setText("");
        etRentPrice.setText("");
        etSecurityMoney.setText("");
        etMrp.setEnabled(true);
        etPrice.setEnabled(true);
        etRentPrice.setEnabled(true);
        etSecurityMoney.setEnabled(true);

        rbSale.setChecked(true);
        rbRent.setChecked(false);
        rbBoth.setChecked(false);

        rbSale.setButtonTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary)));
        rbRent.setButtonTintList(ColorStateList.valueOf(getResources().getColor(R.color.black_fade)));
        rbBoth.setButtonTintList(ColorStateList.valueOf(getResources().getColor(R.color.black_fade)));

        rbSale.setTextColor(Color.parseColor("#3C3C3C"));
        rbRent.setTextColor(Color.parseColor("#3C3C3C"));
        rbBoth.setTextColor(Color.parseColor("#3C3C3C"));

        rbSale.setEnabled(true);
        rbRent.setEnabled(true);
        rbBoth.setEnabled(true);

        tvSaleType.setTextColor(Color.parseColor("#3C3C3C"));
        starSaleType.setVisibility(View.VISIBLE);

        spCondition.setEnabled(true);
        spCondition.setSelection(0);
        tvConditionType.setTextColor(Color.parseColor("#3C3C3C"));
        starConditionType.setVisibility(View.VISIBLE);

        tvMrp.setTextColor(Color.parseColor("#3C3C3C"));
        starMrp.setVisibility(View.VISIBLE);

        tvSecurity.setTextColor(Color.parseColor("#C4C4C4"));
        tvSalePrice.setTextColor(Color.parseColor("#C4C4C4"));
        tvRentPrice.setTextColor(Color.parseColor("#C4C4C4"));
        tvRentDuration.setTextColor(Color.parseColor("#C4C4C4"));
        starRentDuration.setVisibility(View.VISIBLE);
        etRentDuration.setEnabled(true);
        etRentDuration.setText("30");

        llConditionType.setBackground(getResources().getDrawable(R.drawable.et_background));
        etMrp.setBackground(getResources().getDrawable(R.drawable.et_background));
        etSecurityMoney.setBackground(getResources().getDrawable(R.drawable.et_background));
        etPrice.setBackground(getResources().getDrawable(R.drawable.et_background));
        etRentPrice.setBackground(getResources().getDrawable(R.drawable.et_background));
        etRentDuration.setBackground(getResources().getDrawable(R.drawable.et_background));
    }

    private void showGiveawayView() {
        giveawayStatus = "yes";
        etMrp.setText("0");
        etMrp.setEnabled(false);
        etPrice.setText("0");
        etPrice.setEnabled(false);
        etRentPrice.setText("0");
        etRentPrice.setEnabled(false);
        etSecurityMoney.setText("0");
        etSecurityMoney.setEnabled(false);

        //spSellingType.setSelection(0);
        //spSellingType.setEnabled(false);
        rbSale.setChecked(true);
        rbRent.setChecked(false);
        rbBoth.setChecked(false);

        rbSale.setEnabled(true);
        rbRent.setEnabled(false);
        rbBoth.setEnabled(false);

        rbSale.setButtonTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary)));
        rbRent.setButtonTintList(ColorStateList.valueOf(getResources().getColor(R.color.rect_color)));
        rbBoth.setButtonTintList(ColorStateList.valueOf(getResources().getColor(R.color.rect_color)));

        rbSale.setTextColor(Color.parseColor("#0e0f0f"));
        rbRent.setTextColor(Color.parseColor("#C4C4C4"));
        rbBoth.setTextColor(Color.parseColor("#C4C4C4"));

        tvSaleType.setTextColor(Color.parseColor("#C4C4C4"));
        starSaleType.setVisibility(View.INVISIBLE);

        spCondition.setEnabled(false);
        spCondition.setSelection(0);
        tvConditionType.setTextColor(Color.parseColor("#C4C4C4"));
        starConditionType.setVisibility(View.INVISIBLE);

        //tvBookQty.setTextColor(Color.parseColor("#C4C4C4"));
        //starQty.setVisibility(View.INVISIBLE);
        //etQuantity.setEnabled(false);

        tvMrp.setTextColor(Color.parseColor("#C4C4C4"));
        starMrp.setVisibility(View.INVISIBLE);

        tvSecurity.setTextColor(Color.parseColor("#C4C4C4"));
        tvSalePrice.setTextColor(Color.parseColor("#C4C4C4"));
        tvRentPrice.setTextColor(Color.parseColor("#C4C4C4"));
        tvRentDuration.setTextColor(Color.parseColor("#C4C4C4"));
        starRentDuration.setVisibility(View.INVISIBLE);
        etRentDuration.setEnabled(false);
        etRentDuration.setText("365");

        //llSaleType.setBackground(getResources().getDrawable(R.drawable.bg_rectangle));
        llConditionType.setBackground(getResources().getDrawable(R.drawable.bg_rectangle_new));
        //etQuantity.setBackground(getResources().getDrawable(R.drawable.bg_rectangle));
        etMrp.setBackground(getResources().getDrawable(R.drawable.bg_rectangle_new));
        etSecurityMoney.setBackground(getResources().getDrawable(R.drawable.bg_rectangle_new));
        etPrice.setBackground(getResources().getDrawable(R.drawable.bg_rectangle_new));
        etRentPrice.setBackground(getResources().getDrawable(R.drawable.bg_rectangle_new));
        etRentDuration.setBackground(getResources().getDrawable(R.drawable.bg_rectangle_new));

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
                                    Constants.selectedCommunityIds =  android.text.TextUtils.join(",",Constants.selectedCommunityList);
                                }else {
                                    llCommunities.setVisibility(View.GONE);
                                    Constants.selectedCommunityIds = "";
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
                        Constants.selectedCommunityList = new ArrayList<>();
                        myList = response.body().getResponse().getCommunity_list();
                        if (response.body().getResponse().getCommunity_library_status().equals("1")){
                            rlOwnCommunity.setVisibility(View.VISIBLE);
                            tvLibraryName.setText(response.body().getResponse().getOwn_library_name());
                            Constants.selectedCommunityList.add("0");
                            cbSelect.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if (cbSelect.isChecked()){
                                        Constants.selectedCommunityList.add("0");
                                    }else {
                                        Constants.selectedCommunityList.remove("0");
                                    }

                                    Constants.selectedCommunityIds =  android.text.TextUtils.join(",",Constants.selectedCommunityList);
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

    @Override
    public void onAttach(@NonNull Context context) {
        mContext = context;
        super.onAttach(context);
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
        public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View mView = LayoutInflater.from(mCtx).inflate(R.layout.community_select_row, parent, false);
            return new MyHolder(mView);
        }

        @Override
        public void onBindViewHolder(@NonNull MyHolder holder, int position) {
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

                Constants.selectedCommunityList.add(mList.get(getAdapterPosition()).getCommunity_id());
                Constants.selectedCommunityIds =  android.text.TextUtils.join(",",Constants.selectedCommunityList);

                cbSelectHolder.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //isSelected = mList.get(getAdapterPosition()).getIsSelected() ? true : false;
                        if (cbSelectHolder.isChecked()){
                            Constants.selectedCommunityList.add(mList.get(getAdapterPosition()).getCommunity_id());
                        }else {
                            Constants.selectedCommunityList.remove(mList.get(getAdapterPosition()).getCommunity_id());
                        }

                        Constants.selectedCommunityIds =  android.text.TextUtils.join(",",Constants.selectedCommunityList);
                    }
                });

            }
        }

        public void setOnItemClickListener(MoveToCommunityClickListener listener){
            this.listener = listener;
        }

    }
}