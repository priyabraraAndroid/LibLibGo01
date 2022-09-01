package com.lib.liblibgo.dashboard.apartment_admin;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.JsonObject;
import com.lib.liblibgo.R;
import com.lib.liblibgo.adapter.subadmin.CatAdapterNew;
import com.lib.liblibgo.adapter.subadmin.ShelfAdapterNew;
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
import com.lib.liblibgo.model.SellingTypeModel;
import com.lib.liblibgo.model.ShelfListData;
import com.lib.liblibgo.model.ShelfModel;
import com.lib.liblibgo.model.SubmitDataResModel;
import com.lib.liblibgo.views.MyButton;
import com.lib.liblibgo.views.MyEditText;
import com.lib.liblibgo.views.MyTextView;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

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

import static com.lib.liblibgo.dashboard.library.fragments.CommunityLibraryFragment.MyPREFERENCES;

public class UploadBookDetails extends AppCompatActivity {

    private ImageView ivToolbarBack;
    private TextView tvToolbarTitle;
    private TextView tvUpload;

    private UserDatabase database;

    private Spinner spShelfNo;
    private List<ShelfListData> apartments = new ArrayList<>();
    List<ShelfListData> temp_list = new ArrayList<>();
    private String shelfId="";

    List<CategoryListData> cat_list = new ArrayList<>();
    List<CategoryListData> temp_cat_list = new ArrayList<>();
    private String categoryId="";

    private MyEditText etName,etAuthor,etIsbn,etPublishDate,etDescription,etRentDuration,etImgUrl,etMrp;
    private String bookName = "";
    private String bookAuthor = "";
    private String bookIsbn = "";
    private String bookPublishDate = "";
    private String bookDescription = "";
    private String imgUrl = "";
    public static ImageView iv_image;
    private MyButton sendBtn;
    private String DefaultImgUrl = "https://sales.arecontvision.com/images/products/img_placeholder_50618_xl.jpg";
    private String rentalPrice = "";
    private String rentalDuration = "";
    private String bookPrice = "";
    private String catId = "";
    private String catName = "";
    private String shelfIdd = "";
    private String shelfName = "";
    private MyEditText etPrice;
    private MyEditText etRentPrice;
    private MyEditText etQuantity;
    private Spinner spCat;
    private List<SellingTypeModel> selling_type_list = new ArrayList<>();
    private List<ConditionTypeModel> condition_type_list = new ArrayList<>();
    private String sellingType = "";
    private String bookConditionType = "";
    private Spinner spSellingType;
    private Spinner spCondition;
    private String bookId = "";
    private String mrp = "";
    private String quantityy = "";
    private  int conditionValue = 1;
    TextView tvShelf;
    LinearLayout llShelfView;
    private String smallThumbnail = "";
    private String thumbnail = "";
    private MyEditText etSecurityMoney;
    private MyTextView tvIsbnDigitCount;
    private ScrollView scrollView;
    private CheckBox cbGiveaway;
    private String giveawayStatus = "no";
    private String sellingTypeValue = "";
    private String bookCondiTypeValue = "";
    private String giveawayStatusValue = "";
    private String securityMoneyValue = "";
    static final int REQUEST_IMAGE_CAPTURE = 2;
    String currentPhotoPath = "";
    private int counter = 0;
    private LinearLayout llCat,llSaleType,llConditionType;
    private String isOwnLirary = "1";
    SharedPreferences sharedpreferences;
    private SharedPreferences.Editor editor;
    private String communityId = "0";
    private TextView tvSaleType,starSaleType,tvBookQty,starQty,tvConditionType,starConditionType,tvMrp,starMrp,tvSecurity,tvSalePrice,tvRentPrice,tvRentDuration,starRentDuration;

    private String APi_key = "48047_e5da204a55741d7ccc1181ce78f2a1d4";
    private String dollerValue = "80";
    private String division = "2";
    private MyTextView tvOr;

    private String As_good_as_new = "1";
    private String Marked_texts = "1";
    private String Torn_or_pale_pages = "1";
    private String Marked_texts_Torn_cover_page = "1";
    private String Security_price_type = "1";
    private String Rent_divisor = "1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_book_details);

        database = new UserDatabase(this);
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();

        communityId = sharedpreferences.getString("community_id", "0");

        Log.d("myCommunityId","Community_id : "+communityId);
        Log.d("myCommunityId","isOwnLibrary : "+Constants.isOwnLibrary);

        ivToolbarBack = (ImageView) findViewById(R.id.backTool);
        tvToolbarTitle = (TextView) findViewById(R.id.titleTool);
        tvUpload = (TextView) findViewById(R.id.tvUpload);
        setUpToolbar(getString(R.string.upload_book));

        spShelfNo = (Spinner)findViewById(R.id.spShelfNo);
        spCat = (Spinner)findViewById(R.id.spCat);
        spSellingType = (Spinner)findViewById(R.id.spSellingType);
        spCondition = (Spinner)findViewById(R.id.spCondition);

        tvShelf = (TextView)findViewById(R.id.tvShelf);
        llShelfView = (LinearLayout) findViewById(R.id.llShelfView);

        etName = (MyEditText)findViewById(R.id.etName);
        etAuthor = (MyEditText)findViewById(R.id.etAuthor);
        etIsbn = (MyEditText)findViewById(R.id.etIsbn);
        etPublishDate = (MyEditText)findViewById(R.id.etPublishDate);
        etDescription = (MyEditText)findViewById(R.id.etDescription);
        etRentDuration = (MyEditText)findViewById(R.id.etRentDuration);
        etPrice = (MyEditText)findViewById(R.id.etPrice);
        etRentPrice = (MyEditText)findViewById(R.id.etRentPrice);
        etSecurityMoney = (MyEditText)findViewById(R.id.etSecurityMoney);
        etQuantity = (MyEditText)findViewById(R.id.etQuantity);
        etImgUrl = (MyEditText)findViewById(R.id.etImgUrl);
        etMrp = (MyEditText)findViewById(R.id.etMrp);
        iv_image = (ImageView)findViewById(R.id.iv_image);
        sendBtn = (MyButton) findViewById(R.id.sendBtn);
        cbGiveaway = (CheckBox) findViewById(R.id.cbGiveaway);
        tvOr = (MyTextView) findViewById(R.id.tvOr);

        tvIsbnDigitCount = (MyTextView)findViewById(R.id.tvIsbnDigitCount);
        scrollView = (ScrollView)findViewById(R.id.scrollView);
        llCat = (LinearLayout)findViewById(R.id.llCat);
        llSaleType = (LinearLayout)findViewById(R.id.llSaleType);
        llConditionType = (LinearLayout)findViewById(R.id.llConditionType);

        tvSaleType = (TextView)findViewById(R.id.tvSaleType);
        starSaleType = (TextView)findViewById(R.id.starSaleType);
        tvBookQty = (TextView)findViewById(R.id.tvBookQty);
        starQty = (TextView)findViewById(R.id.starQty);
        tvConditionType = (TextView)findViewById(R.id.tvConditionType);
        starConditionType = (TextView)findViewById(R.id.starConditionType);
        tvMrp = (TextView)findViewById(R.id.tvMrp);
        starMrp = (TextView)findViewById(R.id.starMrp);
        tvSecurity = (TextView)findViewById(R.id.tvSecurity);
        tvSalePrice = (TextView)findViewById(R.id.tvSalePrice);
        tvRentPrice = (TextView)findViewById(R.id.tvRentPrice);
        tvRentDuration = (TextView)findViewById(R.id.tvRentDuration);
        starRentDuration = (TextView)findViewById(R.id.starRentDuration);

        etIsbn.setEnabled(true);
        etPublishDate.setEnabled(true);

        if (getIntent().getExtras() != null){
            bookName = getIntent().getStringExtra("name");
            bookAuthor = getIntent().getStringExtra("author");
            bookIsbn = getIntent().getStringExtra("isbn");
            bookPublishDate = getIntent().getStringExtra("publish_date");
            bookDescription = getIntent().getStringExtra("description");
            imgUrl = getIntent().getStringExtra("imgUrl");
            rentalPrice = getIntent().getStringExtra("rental_price");
            rentalDuration = getIntent().getStringExtra("rental_duration");
            bookPrice = getIntent().getStringExtra("book_price");
            catId = getIntent().getStringExtra("category_id");
            catName = getIntent().getStringExtra("category_name");
            shelfIdd = getIntent().getStringExtra("shelf_id");
            shelfName = getIntent().getStringExtra("shelf_name");
            bookId = getIntent().getStringExtra("book_id");
            mrp = getIntent().getStringExtra("mrp");
            quantityy = getIntent().getStringExtra("quantity");
            sellingTypeValue = getIntent().getStringExtra("sale_type");
            bookCondiTypeValue = getIntent().getStringExtra("book_condition");
            giveawayStatusValue = getIntent().getStringExtra("giveaway_status");
            securityMoneyValue = getIntent().getStringExtra("security_money");
        }

        categoryId = catId;

        Log.d("LibraryType","type :"+Constants.libraryType);

        etName.setText(bookName);
        etAuthor.setText(bookAuthor);
        etIsbn.setText(bookIsbn);
        tvIsbnDigitCount.setText("Isbn No ( "+bookIsbn.length()+"/13 )");
        etPublishDate.setText(bookPublishDate);
        etDescription.setText(bookDescription);
        etRentPrice.setText(rentalPrice);
        etRentDuration.setText(rentalDuration);
        etPrice.setText(bookPrice);
        etMrp.setText(mrp);
        etSecurityMoney.setText(securityMoneyValue);

        //Toast.makeText(this, ""+mrp, Toast.LENGTH_SHORT).show();

        if (quantityy.equals("") || quantityy.equals("0")){
            etQuantity.setText("1");
        }else {
            etQuantity.setText(quantityy);
        }

        if (bookId.equals("")){
            sendBtn.setText("Upload");
            tvToolbarTitle.setText("Upload Book Details");
        }else {
            sendBtn.setText("Update");
            tvToolbarTitle.setText("Edit Book Details");
        }

        if (giveawayStatusValue.equals("yes")){
            cbGiveaway.setChecked(true);
            giveawayStatus = "yes";
            //giveawayStatus = "yes";
            etMrp.setText("0");
            etMrp.setEnabled(false);
            etPrice.setText("0");
            etPrice.setEnabled(false);
            etRentPrice.setText("0");
            etRentPrice.setEnabled(false);
            etSecurityMoney.setText("0");
            etSecurityMoney.setEnabled(false);

            spSellingType.setSelection(0);
            spSellingType.setEnabled(false);
            tvSaleType.setTextColor(Color.parseColor("#C4C4C4"));
            starSaleType.setVisibility(View.INVISIBLE);
            llSaleType.setBackground(getResources().getDrawable(R.drawable.bg_rectangle));

            spCondition.setEnabled(false);
            spCondition.setSelection(0);
            tvConditionType.setTextColor(Color.parseColor("#C4C4C4"));
            starConditionType.setVisibility(View.INVISIBLE);
            llConditionType.setBackground(getResources().getDrawable(R.drawable.bg_rectangle));

            tvBookQty.setTextColor(Color.parseColor("#C4C4C4"));
            starQty.setVisibility(View.INVISIBLE);
            etQuantity.setEnabled(false);
            etQuantity.setBackground(getResources().getDrawable(R.drawable.bg_rectangle));

            tvMrp.setTextColor(Color.parseColor("#C4C4C4"));
            starMrp.setVisibility(View.INVISIBLE);
            etMrp.setBackground(getResources().getDrawable(R.drawable.bg_rectangle));

            tvSecurity.setTextColor(Color.parseColor("#C4C4C4"));
            etSecurityMoney.setBackground(getResources().getDrawable(R.drawable.bg_rectangle));
            tvSalePrice.setTextColor(Color.parseColor("#C4C4C4"));
            etPrice.setBackground(getResources().getDrawable(R.drawable.bg_rectangle));
            tvRentPrice.setTextColor(Color.parseColor("#C4C4C4"));
            etRentPrice.setBackground(getResources().getDrawable(R.drawable.bg_rectangle));
            tvRentDuration.setTextColor(Color.parseColor("#C4C4C4"));
            etRentDuration.setBackground(getResources().getDrawable(R.drawable.bg_rectangle));
            starRentDuration.setVisibility(View.INVISIBLE);
            etRentDuration.setEnabled(false);
            etRentDuration.setText("365");

        }else {
            cbGiveaway.setChecked(false);
            giveawayStatus = "no";

            spSellingType.setSelection(0);
            spSellingType.setEnabled(true);
            tvSaleType.setTextColor(Color.parseColor("#3C3C3C"));
            starSaleType.setVisibility(View.VISIBLE);

            spCondition.setEnabled(true);
            spCondition.setSelection(0);
            tvConditionType.setTextColor(Color.parseColor("#3C3C3C"));
            starConditionType.setVisibility(View.VISIBLE);

            tvBookQty.setTextColor(Color.parseColor("#3C3C3C"));
            starQty.setVisibility(View.VISIBLE);
            etQuantity.setEnabled(true);

            tvMrp.setTextColor(Color.parseColor("#3C3C3C"));
            starMrp.setVisibility(View.VISIBLE);

            tvSecurity.setTextColor(Color.parseColor("#3C3C3C"));
            tvSalePrice.setTextColor(Color.parseColor("#3C3C3C"));
            tvRentPrice.setTextColor(Color.parseColor("#3C3C3C"));
            tvRentDuration.setTextColor(Color.parseColor("#3C3C3C"));
            starRentDuration.setVisibility(View.VISIBLE);
            etRentDuration.setEnabled(true);
            etRentDuration.setText("30");

            llSaleType.setBackground(getResources().getDrawable(R.drawable.bg_search));
            llConditionType.setBackground(getResources().getDrawable(R.drawable.bg_search));
            etQuantity.setBackground(getResources().getDrawable(R.drawable.bg_search));
            etMrp.setBackground(getResources().getDrawable(R.drawable.bg_search));
            etSecurityMoney.setBackground(getResources().getDrawable(R.drawable.bg_search));
            etPrice.setBackground(getResources().getDrawable(R.drawable.bg_search));
            etRentPrice.setBackground(getResources().getDrawable(R.drawable.bg_search));
            etRentDuration.setBackground(getResources().getDrawable(R.drawable.bg_search));
        }

        if (imgUrl.equals("") || imgUrl.equals("null")){
            Glide.with(this).load(DefaultImgUrl).into(iv_image);
            etImgUrl.setText(imgUrl);
        }else {
            Glide.with(this).load(imgUrl).into(iv_image);
            etImgUrl.setText(imgUrl);
            //thumbnail = imgUrl;
        }

        etImgUrl.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!charSequence.toString().equals("")){
                    etImgUrl.setBackground(getResources().getDrawable(R.drawable.bg_search));
                    iv_image.setBackground(getResources().getDrawable(R.drawable.bg_search));
                    Glide.with(UploadBookDetails.this).load(charSequence.toString()).error(R.drawable.no_img).placeholder(R.drawable.no_img).into(iv_image);
                }else{
                    //etImgUrl.setBackground(getResources().getDrawable(R.drawable.bg_error_fields));
                    Glide.with(UploadBookDetails.this).load(DefaultImgUrl).error(R.drawable.no_img).placeholder(R.drawable.no_img).into(iv_image);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        etIsbn.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //Toast.makeText(UploadBookDetails.this, "Off", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String newText = charSequence.toString();
                etIsbn.setBackground(getResources().getDrawable(R.drawable.bg_search));
                //Toast.makeText(UploadBookDetails.this, "On", Toast.LENGTH_SHORT).show();
                tvIsbnDigitCount.setText("Isbn No ( "+newText.length()+"/13 )");
                 if (newText.length() == 10 || newText.length() == 13){
                    searchBookByIsbn(newText);
                }else {
                     etName.setText("");
                     etAuthor.setText("");
                     etPublishDate.setText("");
                     etDescription.setText("");
                     etImgUrl.setText(thumbnail);
                 }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                //Toast.makeText(UploadBookDetails.this, "Off", Toast.LENGTH_SHORT).show();
            }
        });

        /*if (Constants.SelectedBookImage.equals("")){
            Glide.with(this).load(R.drawable.no_img).placeholder(R.drawable.no_img).into(iv_image);
        }else {
            Glide.with(this).load(Constants.SelectedBookImage).placeholder(R.drawable.no_img).into(iv_image);
        }*/

        Log.d("imgUrl",imgUrl);

        if (Constants.isOwnLibrary == true){
            tvShelf.setVisibility(View.GONE);
            llShelfView.setVisibility(View.GONE);
            isOwnLirary = "1";
            cbGiveaway.setChecked(false);
            giveawayStatus = "no";
            etMrp.setText("");
            etPrice.setText("");
            etRentPrice.setText("");
            etSecurityMoney.setText("");
            etMrp.setEnabled(true);
            etPrice.setEnabled(true);
            etRentPrice.setEnabled(true);
            etSecurityMoney.setEnabled(true);

            spSellingType.setSelection(0);
            spSellingType.setEnabled(true);
            tvSaleType.setTextColor(Color.parseColor("#3C3C3C"));
            starSaleType.setVisibility(View.VISIBLE);

            spCondition.setEnabled(true);
            spCondition.setSelection(0);
            tvConditionType.setTextColor(Color.parseColor("#3C3C3C"));
            starConditionType.setVisibility(View.VISIBLE);

            tvBookQty.setTextColor(Color.parseColor("#3C3C3C"));
            starQty.setVisibility(View.VISIBLE);
            etQuantity.setEnabled(true);

            tvMrp.setTextColor(Color.parseColor("#3C3C3C"));
            starMrp.setVisibility(View.VISIBLE);

            tvSecurity.setTextColor(Color.parseColor("#3C3C3C"));
            tvSalePrice.setTextColor(Color.parseColor("#3C3C3C"));
            tvRentPrice.setTextColor(Color.parseColor("#3C3C3C"));
            tvRentDuration.setTextColor(Color.parseColor("#3C3C3C"));
            starRentDuration.setVisibility(View.VISIBLE);
            etRentDuration.setEnabled(true);
            etRentDuration.setText("30");

            llSaleType.setBackground(getResources().getDrawable(R.drawable.bg_search));
            llConditionType.setBackground(getResources().getDrawable(R.drawable.bg_search));
            etQuantity.setBackground(getResources().getDrawable(R.drawable.bg_search));
            etMrp.setBackground(getResources().getDrawable(R.drawable.bg_search));
            etSecurityMoney.setBackground(getResources().getDrawable(R.drawable.bg_search));
            etPrice.setBackground(getResources().getDrawable(R.drawable.bg_search));
            etRentPrice.setBackground(getResources().getDrawable(R.drawable.bg_search));
            etRentDuration.setBackground(getResources().getDrawable(R.drawable.bg_search));

        }else {
            isOwnLirary = "0";
            if (Constants.libraryType.equals("physical")){
                cbGiveaway.setChecked(true);
                cbGiveaway.setEnabled(false);
                cbGiveaway.setClickable(false);
                giveawayStatus = "yes";
                etMrp.setText("0");
                etMrp.setEnabled(false);
                etPrice.setText("0");
                etPrice.setEnabled(false);
                etRentPrice.setText("0");
                etRentPrice.setEnabled(false);
                etSecurityMoney.setText("0");
                etSecurityMoney.setEnabled(false);
                getShelfList();
                tvShelf.setVisibility(View.VISIBLE);
                llShelfView.setVisibility(View.VISIBLE);

                spSellingType.setSelection(0);
                spSellingType.setEnabled(false);
                tvSaleType.setTextColor(Color.parseColor("#C4C4C4"));
                starSaleType.setVisibility(View.INVISIBLE);

                spCondition.setEnabled(false);
                spCondition.setSelection(0);
                tvConditionType.setTextColor(Color.parseColor("#C4C4C4"));
                starConditionType.setVisibility(View.INVISIBLE);

                tvBookQty.setTextColor(Color.parseColor("#C4C4C4"));
                starQty.setVisibility(View.INVISIBLE);
                etQuantity.setEnabled(false);

                tvMrp.setTextColor(Color.parseColor("#C4C4C4"));
                starMrp.setVisibility(View.INVISIBLE);

                tvSecurity.setTextColor(Color.parseColor("#C4C4C4"));
                tvSalePrice.setTextColor(Color.parseColor("#C4C4C4"));
                tvRentPrice.setTextColor(Color.parseColor("#C4C4C4"));
                tvRentDuration.setTextColor(Color.parseColor("#C4C4C4"));
                starRentDuration.setVisibility(View.INVISIBLE);
                etRentDuration.setEnabled(false);
                etRentDuration.setText("365");

                llSaleType.setBackground(getResources().getDrawable(R.drawable.bg_rectangle));
                llConditionType.setBackground(getResources().getDrawable(R.drawable.bg_rectangle));
                etQuantity.setBackground(getResources().getDrawable(R.drawable.bg_rectangle));
                etMrp.setBackground(getResources().getDrawable(R.drawable.bg_rectangle));
                etSecurityMoney.setBackground(getResources().getDrawable(R.drawable.bg_rectangle));
                etPrice.setBackground(getResources().getDrawable(R.drawable.bg_rectangle));
                etRentPrice.setBackground(getResources().getDrawable(R.drawable.bg_rectangle));
                etRentDuration.setBackground(getResources().getDrawable(R.drawable.bg_rectangle));

            }else {
                cbGiveaway.setChecked(false);
                cbGiveaway.setEnabled(true);
                cbGiveaway.setClickable(true);
                giveawayStatus = "no";
                //etMrp.setText("");
                //etPrice.setText("");
                //etRentPrice.setText("");
                //etSecurityMoney.setText("");
                etMrp.setEnabled(true);
                etPrice.setEnabled(true);
                etRentPrice.setEnabled(true);
                etSecurityMoney.setEnabled(true);

                spSellingType.setSelection(0);
                spSellingType.setEnabled(true);
                tvSaleType.setTextColor(Color.parseColor("#3C3C3C"));
                starSaleType.setVisibility(View.VISIBLE);

                spCondition.setEnabled(true);
                spCondition.setSelection(0);
                tvConditionType.setTextColor(Color.parseColor("#3C3C3C"));
                starConditionType.setVisibility(View.VISIBLE);

                tvBookQty.setTextColor(Color.parseColor("#3C3C3C"));
                starQty.setVisibility(View.VISIBLE);
                etQuantity.setEnabled(true);

                tvMrp.setTextColor(Color.parseColor("#3C3C3C"));
                starMrp.setVisibility(View.VISIBLE);

                tvSecurity.setTextColor(Color.parseColor("#3C3C3C"));
                tvSalePrice.setTextColor(Color.parseColor("#3C3C3C"));
                tvRentPrice.setTextColor(Color.parseColor("#3C3C3C"));
                tvRentDuration.setTextColor(Color.parseColor("#3C3C3C"));
                starRentDuration.setVisibility(View.VISIBLE);
                etRentDuration.setEnabled(true);
                etRentDuration.setText("30");

                llSaleType.setBackground(getResources().getDrawable(R.drawable.bg_search));
                llConditionType.setBackground(getResources().getDrawable(R.drawable.bg_search));
                etQuantity.setBackground(getResources().getDrawable(R.drawable.bg_search));
                etMrp.setBackground(getResources().getDrawable(R.drawable.bg_search));
                etSecurityMoney.setBackground(getResources().getDrawable(R.drawable.bg_search));
                etPrice.setBackground(getResources().getDrawable(R.drawable.bg_search));
                etRentPrice.setBackground(getResources().getDrawable(R.drawable.bg_search));
                etRentDuration.setBackground(getResources().getDrawable(R.drawable.bg_search));
            }
        }

        cbGiveaway.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked == true){
                    giveawayStatus = "yes";
                    etMrp.setText("0");
                    etMrp.setEnabled(false);
                    etPrice.setText("0");
                    etPrice.setEnabled(false);
                    etRentPrice.setText("0");
                    etRentPrice.setEnabled(false);
                    etSecurityMoney.setText("0");
                    etSecurityMoney.setEnabled(false);

                    spSellingType.setSelection(0);
                    spSellingType.setEnabled(false);
                    tvSaleType.setTextColor(Color.parseColor("#C4C4C4"));
                    starSaleType.setVisibility(View.INVISIBLE);

                    spCondition.setEnabled(false);
                    spCondition.setSelection(0);
                    tvConditionType.setTextColor(Color.parseColor("#C4C4C4"));
                    starConditionType.setVisibility(View.INVISIBLE);

                    tvBookQty.setTextColor(Color.parseColor("#C4C4C4"));
                    starQty.setVisibility(View.INVISIBLE);
                    etQuantity.setEnabled(false);

                    tvMrp.setTextColor(Color.parseColor("#C4C4C4"));
                    starMrp.setVisibility(View.INVISIBLE);

                    tvSecurity.setTextColor(Color.parseColor("#C4C4C4"));
                    tvSalePrice.setTextColor(Color.parseColor("#C4C4C4"));
                    tvRentPrice.setTextColor(Color.parseColor("#C4C4C4"));
                    tvRentDuration.setTextColor(Color.parseColor("#C4C4C4"));
                    starRentDuration.setVisibility(View.INVISIBLE);
                    etRentDuration.setEnabled(false);
                    etRentDuration.setText("365");

                    llSaleType.setBackground(getResources().getDrawable(R.drawable.bg_rectangle));
                    llConditionType.setBackground(getResources().getDrawable(R.drawable.bg_rectangle));
                    etQuantity.setBackground(getResources().getDrawable(R.drawable.bg_rectangle));
                    etMrp.setBackground(getResources().getDrawable(R.drawable.bg_rectangle));
                    etSecurityMoney.setBackground(getResources().getDrawable(R.drawable.bg_rectangle));
                    etPrice.setBackground(getResources().getDrawable(R.drawable.bg_rectangle));
                    etRentPrice.setBackground(getResources().getDrawable(R.drawable.bg_rectangle));
                    etRentDuration.setBackground(getResources().getDrawable(R.drawable.bg_rectangle));

                }else {
                    giveawayStatus = "no";
                    etMrp.setText("");
                    etPrice.setText("");
                    etRentPrice.setText("0");
                    etSecurityMoney.setText("0");
                    etMrp.setEnabled(true);
                    etPrice.setEnabled(true);
                    etRentPrice.setEnabled(false);
                    etSecurityMoney.setEnabled(false);

                    spSellingType.setSelection(0);
                    spSellingType.setEnabled(true);
                    tvSaleType.setTextColor(Color.parseColor("#3C3C3C"));
                    starSaleType.setVisibility(View.VISIBLE);

                    spCondition.setEnabled(true);
                    spCondition.setSelection(0);
                    tvConditionType.setTextColor(Color.parseColor("#3C3C3C"));
                    starConditionType.setVisibility(View.VISIBLE);

                    tvBookQty.setTextColor(Color.parseColor("#3C3C3C"));
                    starQty.setVisibility(View.VISIBLE);
                    etQuantity.setEnabled(true);

                    tvMrp.setTextColor(Color.parseColor("#3C3C3C"));
                    starMrp.setVisibility(View.VISIBLE);

                    tvSecurity.setTextColor(Color.parseColor("#C4C4C4"));
                    tvSalePrice.setTextColor(Color.parseColor("#3C3C3C"));
                    tvRentPrice.setTextColor(Color.parseColor("#C4C4C4"));
                    tvRentDuration.setTextColor(Color.parseColor("#C4C4C4"));
                    starRentDuration.setVisibility(View.INVISIBLE);
                    etRentDuration.setEnabled(false);
                    etRentDuration.setText("365");

                    llSaleType.setBackground(getResources().getDrawable(R.drawable.bg_search));
                    llConditionType.setBackground(getResources().getDrawable(R.drawable.bg_search));
                    etQuantity.setBackground(getResources().getDrawable(R.drawable.bg_search));
                    etMrp.setBackground(getResources().getDrawable(R.drawable.bg_search));
                    etSecurityMoney.setBackground(getResources().getDrawable(R.drawable.bg_rectangle));
                    etPrice.setBackground(getResources().getDrawable(R.drawable.bg_search));
                    etRentPrice.setBackground(getResources().getDrawable(R.drawable.bg_rectangle));
                    etRentDuration.setBackground(getResources().getDrawable(R.drawable.bg_rectangle));
                }
            }
        });



        etName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                etName.setBackground(getResources().getDrawable(R.drawable.bg_search));
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        /*etAuthor.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                etAuthor.setBackground(getResources().getDrawable(R.drawable.bg_search));
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });*/

        etMrp.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (sellingType.equals("For Sale")){
                    etMrp.setBackground(getResources().getDrawable(R.drawable.bg_search));
                    etPrice.setBackground(getResources().getDrawable(R.drawable.bg_search));
                    etRentPrice.setBackground(getResources().getDrawable(R.drawable.bg_rectangle));
                    etSecurityMoney.setBackground(getResources().getDrawable(R.drawable.bg_rectangle));
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        /*etRentDuration.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                etRentDuration.setBackground(getResources().getDrawable(R.drawable.bg_search));
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });*/

        /*etQuantity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                etQuantity.setBackground(getResources().getDrawable(R.drawable.bg_search));
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });*/

        getRulesValue();
        getCategoryList();
        getSellingType();



        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //String rental_price = "";
                //String duration = "";
                String name = etName.getText().toString().trim();
                String author = etAuthor.getText().toString().trim();
                String isbn = etIsbn.getText().toString().trim();
                String publishDate = etPublishDate.getText().toString().trim();
                String description = etDescription.getText().toString().trim();
                String duration = etRentDuration.getText().toString().trim();
                String bookImgUrl = etImgUrl.getText().toString().trim();
                String priceMrp = etMrp.getText().toString().trim();
                String rental_price = etRentPrice.getText().toString().trim();
                String sale_price = etPrice.getText().toString().trim();
                String securityPrice = etSecurityMoney.getText().toString().trim();
                String quantity = etQuantity.getText().toString().trim();

                List<EditText> myInputFields = new ArrayList();
                if (cbGiveaway.isChecked()){
                    myInputFields.add(etIsbn);
                    myInputFields.add(etName);
                    //myInputFields.add(etAuthor);
                    //myInputFields.add(etDescription);
                    //myInputFields.add(etMrp);
                    //myInputFields.add(etPrice);
                    //myInputFields.add(etRentPrice);
                    //myInputFields.add(etSecurityMoney);
                    myInputFields.add(etQuantity);
                    myInputFields.add(etRentDuration);
                }else {
                    myInputFields.add(etIsbn);
                    myInputFields.add(etName);
                    //myInputFields.add(etAuthor);
                    //myInputFields.add(etDescription);
                    myInputFields.add(etQuantity);
                    myInputFields.add(etMrp);
                    myInputFields.add(etPrice);
                    myInputFields.add(etRentPrice);
                    myInputFields.add(etSecurityMoney);
                    myInputFields.add(etRentDuration);
                }

                counter = 0;

                for (int i=0;i<myInputFields.size();i++){
                    if (myInputFields.get(i).getText().toString().equals("")){
                        myInputFields.get(i).setBackground(getResources().getDrawable(R.drawable.bg_error_fields));
                        //myInputFields.get(i).setError("Required");
                        if (myInputFields.get(0).getText().toString().equals("")){
                            scrollView.fullScroll(View.FOCUS_UP);
                            myInputFields.get(0).requestFocus();
                        }else if (myInputFields.get(1).getText().toString().equals("")){
                            scrollView.fullScroll(View.FOCUS_UP);
                            myInputFields.get(1).requestFocus();
                        }else if (categoryId.equals("")){
                            //Toast.makeText(UploadBookDetails.this, "Select Category", Toast.LENGTH_SHORT).show();
                            llCat.setBackground(getResources().getDrawable(R.drawable.bg_error_fields));
                            scrollView.scrollTo(0, etDescription.getTop());
                        }else if (myInputFields.get(2).getText().toString().equals("")){
                            scrollView.fullScroll(View.FOCUS_DOWN);
                            myInputFields.get(2).requestFocus();
                        }else if (myInputFields.get(3).getText().toString().equals("")){
                            scrollView.scrollTo(0, llCat.getTop());
                            myInputFields.get(3).requestFocus();
                        }else if (myInputFields.get(4).getText().toString().equals("")){
                            scrollView.fullScroll(View.FOCUS_DOWN);
                            scrollView.scrollTo(0, llCat.getTop());
                        }else if (myInputFields.get(5).getText().toString().equals("")){
                            scrollView.fullScroll(View.FOCUS_DOWN);
                            scrollView.scrollTo(0, llCat.getTop());
                        }else if (myInputFields.get(6).getText().toString().equals("")){
                            scrollView.fullScroll(View.FOCUS_DOWN);
                            scrollView.scrollTo(0, llCat.getTop());
                        }else if (myInputFields.get(7).getText().toString().equals("")){
                            scrollView.fullScroll(View.FOCUS_DOWN);
                            scrollView.scrollTo(0, llCat.getTop());
                        }/*else if (myInputFields.get(8).getText().toString().equals("")){
                            scrollView.fullScroll(View.FOCUS_UP);
                            scrollView.scrollTo(0, etPublishDate.getTop());
                        }*/
                        //scrollView.scrollTo( myInputFields.get(i).getLeft(), myInputFields.get(i).getTop());
                        counter ++;
                    }else {
                        myInputFields.get(i).setBackground(getResources().getDrawable(R.drawable.bg_search));
                    }
                }

                /*if (sellingType.equals("For Sale")){
                    rental_price = "0";
                    duration = "1";
                    *//*myInputFields.get(6).setText("0");
                    myInputFields.get(6).setBackground(getResources().getDrawable(R.drawable.bg_search));
                    myInputFields.get(8).setText("0");
                    myInputFields.get(8).setBackground(getResources().getDrawable(R.drawable.bg_search));*//*
                }else {
                    rental_price = etRentPrice.getText().toString().trim();
                    duration = etRentDuration.getText().toString().trim();
                }*/


                if (counter > 0){
                    return;
                }else {
                    if (isbn.equals("")){
                        Toast.makeText(UploadBookDetails.this, "Enter Isbn no", Toast.LENGTH_SHORT).show();
                        scrollView.fullScroll(View.FOCUS_UP);
                        etIsbn.setBackground(getResources().getDrawable(R.drawable.bg_error_fields));
                    }else if (isbn.length() == 9 || isbn.length() == 12){
                        Toast.makeText(UploadBookDetails.this, "ISBN number must be 10 or 13 digit", Toast.LENGTH_SHORT).show();
                        scrollView.fullScroll(View.FOCUS_UP);
                        etIsbn.setBackground(getResources().getDrawable(R.drawable.bg_error_fields));
                    }/*else if (isbn.length() <= 12){
                        Toast.makeText(UploadBookDetails.this, "ISBN number must be 13 digit", Toast.LENGTH_SHORT).show();
                        scrollView.fullScroll(View.FOCUS_UP);
                        etIsbn.setBackground(getResources().getDrawable(R.drawable.bg_error_fields));
                    }*/else if (categoryId.equals("")){
                        Toast.makeText(UploadBookDetails.this, "Select Category", Toast.LENGTH_SHORT).show();
                        llCat.setBackground(getResources().getDrawable(R.drawable.bg_error_fields));
                        scrollView.scrollTo(0, etDescription.getTop());
                    }else if (bookImgUrl.equals("")){
                        if (Constants.SelectedBookImage.equals("")){
                            Toast.makeText(UploadBookDetails.this, "Provide Book Image.", Toast.LENGTH_SHORT).show();
                            iv_image.setBackground(getResources().getDrawable(R.drawable.bg_error_fields));
                            etImgUrl.setBackground(getResources().getDrawable(R.drawable.bg_error_fields));
                        }else {
                            saveBookDetails(name,author,isbn,publishDate,description,duration,bookImgUrl,priceMrp,rental_price,sale_price,securityPrice,quantity,giveawayStatus);
                        }
                    }else {
                        Log.d("myGiveaway",giveawayStatus);
                        saveBookDetails(name,author,isbn,publishDate,description,duration,bookImgUrl,priceMrp,rental_price,sale_price,securityPrice,quantity,giveawayStatus);
                    }
                }

                /*if (isbn.equals("")){
                    Toast.makeText(UploadBookDetails.this, "Enter Isbn no", Toast.LENGTH_SHORT).show();
                    //etIsbn.setFocusable(true);
                    scrollView.fullScroll(View.FOCUS_UP);
                }else if (isbn.length() <= 9){
                    //etIsbn.setFocusable(true);
                    Toast.makeText(UploadBookDetails.this, "ISBN number must be 10 or 13 digit", Toast.LENGTH_SHORT).show();
                    scrollView.fullScroll(View.FOCUS_UP);
                }else if (sellingType.equals("Select Type")){
                    Toast.makeText(UploadBookDetails.this, "Select Selling Type", Toast.LENGTH_SHORT).show();
                }else if (bookConditionType.equals("Select Condition")){
                    Toast.makeText(UploadBookDetails.this, "Select Book Condition Type", Toast.LENGTH_SHORT).show();
                }else if (name.equals("")){
                    Toast.makeText(UploadBookDetails.this, "Enter Name", Toast.LENGTH_SHORT).show();
                }else if (author.equals("")){
                    Toast.makeText(UploadBookDetails.this, "Enter Author Name", Toast.LENGTH_SHORT).show();
                }else if (description.equals("")){
                    Toast.makeText(UploadBookDetails.this, "Enter Description", Toast.LENGTH_SHORT).show();
                }else if (categoryId.equals("")){
                    Toast.makeText(UploadBookDetails.this, "Select Category", Toast.LENGTH_SHORT).show();
                }*//*else if (shelfId.equals("")){
                    Toast.makeText(UploadBookDetails.this, "Select Shelf", Toast.LENGTH_SHORT).show();
                }*//*else if (priceMrp.equals("")){
                    Toast.makeText(UploadBookDetails.this, "Type MRP", Toast.LENGTH_SHORT).show();
                }else if (sale_price.equals("")){
                    Toast.makeText(UploadBookDetails.this, "Type Selling Price", Toast.LENGTH_SHORT).show();
                }else if (rental_price.equals("")){
                    Toast.makeText(UploadBookDetails.this, "Type Rental Price", Toast.LENGTH_SHORT).show();
                }else if (securityPrice.equals("")){
                    Toast.makeText(UploadBookDetails.this, "Type Security Price", Toast.LENGTH_SHORT).show();
                }else if (quantity.equals("") || quantity.equals("0")){
                    Toast.makeText(UploadBookDetails.this, "Type Quantity", Toast.LENGTH_SHORT).show();
                }else if (duration.equals("")){
                    Toast.makeText(UploadBookDetails.this, "Enter Rent Duration", Toast.LENGTH_SHORT).show();
                }else if (bookImgUrl.equals("")){
                    if (Constants.SelectedBookImage.equals("")){
                        Toast.makeText(UploadBookDetails.this, "Provide Book Image.", Toast.LENGTH_SHORT).show();
                    }else {
                        //Toast.makeText(UploadBookDetails.this, "Image Selected.", Toast.LENGTH_SHORT).show();
                        saveBookDetails(name,author,isbn,publishDate,description,duration,bookImgUrl,priceMrp,rental_price,sale_price,securityPrice,quantity,giveawayStatus);
                    }
                }else {
                    Log.d("myGiveaway",giveawayStatus);
                    saveBookDetails(name,author,isbn,publishDate,description,duration,bookImgUrl,priceMrp,rental_price,sale_price,securityPrice,quantity,giveawayStatus);
                }*/
            }
        });

        TextView tvUploadImage = (TextView)findViewById(R.id.tvUploadImage);

        tvUploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dexter.withContext(UploadBookDetails.this).withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA)
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
                        //int price =  (conditionValue / 100 ) * mrpValue;
                        int price =  (int)(mrpValue / 100) * conditionValue +1;
                        //etPrice.setText(String.valueOf(price));
                        //int priceRent =  (int)(mrpValue / 100) * 95;
                        if (sellingType.equals("For Rent")){
                            etPrice.setText("0");
                            etRentPrice.setText(String.valueOf((price/Integer.parseInt(Rent_divisor))+1));
                            etSecurityMoney.setText(mrp);
                        }else if (sellingType.equals("Both")){
                            etPrice.setText(String.valueOf(price));
                            etRentPrice.setText(String.valueOf((price/Integer.parseInt(Rent_divisor))+1));
                            etSecurityMoney.setText(mrp);
                        }else if (sellingType.equals("For Sale")){
                            etPrice.setText(String.valueOf(price));
                            etRentPrice.setText("0");
                            etSecurityMoney.setText("0");
                        }
                    }
                }else {
                    //Toast.makeText(UploadBookDetails.this, "Select Book Condition First.", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

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
                            Toast.makeText(UploadBookDetails.this, "Rules not found !", Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(UploadBookDetails.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(UploadBookDetails.this, "Something went wrong !", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(UploadBookDetails.this, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private final void focusOnView(EditText editText){
        scrollView.post(new Runnable() {
            @Override
            public void run() {
                scrollView.scrollTo(0, editText.getBottom());
                Toast.makeText(UploadBookDetails.this, ""+editText.getHint().toString()+" is blank.", Toast.LENGTH_SHORT).show();
            }
        });
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
                            etPublishDate.setText(publishDate);
                            etDescription.setText(description);
                            etImgUrl.setText(thumbnail);

                            if (thumbnail.equals("") || thumbnail.equals("null")){
                                /*smallThumbnail = "";
                                thumbnail = "";
                                Glide.with(UploadBookDetails.this).load("https://sales.arecontvision.com/images/products/img_placeholder_50618_xl.jpg").into(iv_image);
                                tvOr.setVisibility(View.GONE);
                                iv_image.setVisibility(View.GONE);*/
                            }else {
                                tvOr.setVisibility(View.VISIBLE);
                                iv_image.setVisibility(View.VISIBLE);
                                Glide.with(UploadBookDetails.this).load(thumbnail).into(iv_image);
                            }

                            getMrp(isbn);
                           /* BookDataModel model = new BookDataModel();
                            model.setName(name);
                            model.setAuthor(author);
                            model.setPublish_date(publishDate);
                            model.setDescription(description);
                            model.setImgUrl(smallThumbnail);
                            model.setThumbnailUrl(thumbnail);
                            model.setIsbn_no(isbn);
                            mList.add(model);*/

                            Log.d("myAutomr",author);
                        }


                    } catch (JSONException e) {
                        etName.setText("");
                        etAuthor.setText("");
                        etPublishDate.setText("");
                        etDescription.setText("");
                        etImgUrl.setText(thumbnail);
                        e.printStackTrace();
                        //Toast.makeText(BookListActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }else {
                    //progBar.setVisibility(View.GONE);
                    etName.setText("");
                    etAuthor.setText("");
                    etPublishDate.setText("");
                    etDescription.setText("");
                    etImgUrl.setText(thumbnail);
                    Toast.makeText(UploadBookDetails.this, "Something went wrong !", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
               // progBar.setVisibility(View.GONE);
                etName.setText("");
                etAuthor.setText("");
                etPublishDate.setText("");
                etDescription.setText("");
                etImgUrl.setText(thumbnail);
                Toast.makeText(UploadBookDetails.this, "Server not responding yet !", Toast.LENGTH_SHORT).show();
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
                                Glide.with(UploadBookDetails.this).load("https://sales.arecontvision.com/images/products/img_placeholder_50618_xl.jpg").into(iv_image);
                                tvOr.setVisibility(View.GONE);
                                iv_image.setVisibility(View.GONE);
                            }else {
                                smallThumbnail = bImage;
                                thumbnail = bImage;
                                Glide.with(UploadBookDetails.this).load(bImage).into(iv_image);
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
                        /*String mrp = "";
                        if (msrp.equals("") || msrp.equals("null") || msrp.equals("0.00")){
                            etMrp.setText("");
                            tvMrp.setText("Purchase Price");
                        }else {
                            if (msrp.contains(".")){
                                mrp = msrp.substring(0,msrp.indexOf("."));
                                int mrpVal = (Integer.parseInt(mrp) * Integer.parseInt(dollerValue)) / Integer.parseInt(division);
                                etMrp.setText(String.valueOf(mrpVal));
                                tvMrp.setText("Online Price");
                            }else {
                                mrp = msrp;
                                etMrp.setText(mrp);
                                tvMrp.setText("Online Price");
                            }
                        }*/
                       /* if (mrp.equals("") || mrp.equals("null") || mrp.equals("0")){
                            etMrp.setText("");
                        }else {
                            int mrpVal = (Integer.parseInt(mrp) * Integer.parseInt(dollerValue)) / Integer.parseInt(division);
                            etMrp.setText(String.valueOf(mrpVal));
                            *//*if (Integer.parseInt(mrp) < 50){
                                int mrpVal = (Integer.parseInt(mrp) * Integer.parseInt(dollerValue)) / Integer.parseInt(division);
                                etMrp.setText(String.valueOf(mrpVal));
                            }else {
                                etMrp.setText(mrp);
                            }*//*

                        }*/

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

    private void chooseImage() {

    }

    private void showImagePicker() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(UploadBookDetails.this);
        final View layoutView = LayoutInflater.from(UploadBookDetails.this).inflate(R.layout.layout_image_chooser, null);
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

    private void chooseBookImage() {
        Constants.openPageFrom = "fromBookUpload";
        Intent intent = new Intent(UploadBookDetails.this, ImageGallery.class);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        /*if (Constants.cropedImage != null){
            iv_image.setImageBitmap(Constants.cropedImage);
        }else {

        }*/
        if (!Constants.SelectedBookImage.equals("")){
            tvOr.setVisibility(View.VISIBLE);
            iv_image.setVisibility(View.VISIBLE);
            Glide.with(this).load(Constants.SelectedBookImage).placeholder(R.drawable.no_img).into(iv_image);
            iv_image.setBackground(getResources().getDrawable(R.drawable.bg_search));
            etImgUrl.setBackground(getResources().getDrawable(R.drawable.bg_search));
        }else {
            tvOr.setVisibility(View.VISIBLE);
            iv_image.setVisibility(View.VISIBLE);
            if (imgUrl.equals("")){
                Glide.with(this).load(R.drawable.no_img).placeholder(R.drawable.no_img).into(iv_image);
            }else {
                Glide.with(this).load(imgUrl).placeholder(R.drawable.no_img).into(iv_image);
            }

        }
        Log.d("myCommunityId","Community_id : "+communityId);
        Log.d("myCommunityId","isOwnLibrary : "+Constants.isOwnLibrary);
        Log.d("myFile","Book Image : "+Constants.SelectedBookImage);
        super.onResume();
    }

    private void getSellingType() {
       // selling_type_list.add(new SellingTypeModel("Select Type"));
        selling_type_list.add(new SellingTypeModel("For Sale"));
        selling_type_list.add(new SellingTypeModel("For Rent"));
        selling_type_list.add(new SellingTypeModel("Sale or Rent"));
        SellingTypeAdapterNew sellingTypeAdapterNew = new SellingTypeAdapterNew(getApplicationContext(),selling_type_list);
        spSellingType.setAdapter(sellingTypeAdapterNew);

        /*if (sellingType != null) {
            int spinnerPosition = sellingTypeAdapterNew.getPosition(sellingType);
            spSellingType.setSelection(spinnerPosition);
        }*/


        if (sellingTypeValue.equals("")){
            spSellingType.setSelection(0);
        }else if (sellingTypeValue.equals("For Sale")){
            spSellingType.setSelection(0);
        }else if (sellingTypeValue.equals("For Rent")){
            spSellingType.setSelection(1);
        }else if (sellingTypeValue.equals("Both")){
            spSellingType.setSelection(2);
        }else {
            spSellingType.setSelection(0);
        }

        spSellingType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                int price = 0;
                if (!etMrp.getText().toString().equals("")){
                    price = (int)(Integer.parseInt(etMrp.getText().toString()) / 100) * conditionValue +1;
                }
                if (selling_type_list.get(i).getSale_type().equals("For Sale")){
                    //etRentPrice.setText("0");
                    if (Constants.isOwnLibrary == true){
                        etRentDuration.setText("30");
                    }else {
                        if (Constants.libraryType.equals("physical")){
                            etRentDuration.setText("365");
                        }else {
                            etRentDuration.setText("30");
                        }
                    }

                    if (giveawayStatus.equals("no")){
                        //etMrp.setText("");
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

                        etSecurityMoney.setBackground(getResources().getDrawable(R.drawable.bg_rectangle));
                        etPrice.setBackground(getResources().getDrawable(R.drawable.bg_search));
                        etRentPrice.setBackground(getResources().getDrawable(R.drawable.bg_rectangle));
                        etRentDuration.setBackground(getResources().getDrawable(R.drawable.bg_rectangle));
                    }
                    sellingType = selling_type_list.get(i).getSale_type();
                    etSecurityMoney.setText("0");
                    etSecurityMoney.setEnabled(false);
                }else if (selling_type_list.get(i).getSale_type().equals("Sale or Rent")){
                    sellingType = "Both";
                    if (giveawayStatus.equals("no")){
                        //etMrp.setText("");
                        tvSecurity.setTextColor(Color.parseColor("#3C3C3C"));
                        //etSecurityMoney.setText("");
                        etSecurityMoney.setText(etMrp.getText().toString().trim());
                        etSecurityMoney.setEnabled(true);
                        tvSalePrice.setTextColor(Color.parseColor("#3C3C3C"));
                        etPrice.setEnabled(true);
                        //etPrice.setText("");
                        tvRentPrice.setTextColor(Color.parseColor("#3C3C3C"));
                        etRentPrice.setEnabled(true);
                        //etRentPrice.setText("");
                        tvRentDuration.setTextColor(Color.parseColor("#3C3C3C"));
                        etRentDuration.setEnabled(true);
                        etRentDuration.setText("30");
                        starRentDuration.setVisibility(View.VISIBLE);

                        etPrice.setText(String.valueOf(price));
                        etRentPrice.setText(String.valueOf((price/Integer.parseInt(Rent_divisor))+1));
                        etSecurityMoney.setText(etMrp.getText().toString());

                        etSecurityMoney.setBackground(getResources().getDrawable(R.drawable.bg_search));
                        etPrice.setBackground(getResources().getDrawable(R.drawable.bg_search));
                        etRentPrice.setBackground(getResources().getDrawable(R.drawable.bg_search));
                        etRentDuration.setBackground(getResources().getDrawable(R.drawable.bg_search));
                    }
                }else if (selling_type_list.get(i).getSale_type().equals("For Rent")){
                    if (giveawayStatus.equals("no")){
                        //etMrp.setText("");
                        tvSecurity.setTextColor(Color.parseColor("#3C3C3C"));
                        //etSecurityMoney.setText("");
                        etSecurityMoney.setText(etMrp.getText().toString().trim());
                        etSecurityMoney.setEnabled(true);
                        tvSalePrice.setTextColor(Color.parseColor("#C4C4C4"));
                        etPrice.setEnabled(false);
                        etPrice.setText("0");
                        tvRentPrice.setTextColor(Color.parseColor("#3C3C3C"));
                        etRentPrice.setEnabled(true);
                        //etRentPrice.setText("");
                        tvRentDuration.setTextColor(Color.parseColor("#3C3C3C"));
                        etRentDuration.setEnabled(true);
                        etRentDuration.setText("30");
                        starRentDuration.setVisibility(View.VISIBLE);

                        etRentPrice.setText(String.valueOf((price/Integer.parseInt(Rent_divisor))+1));
                        etSecurityMoney.setText(etMrp.getText().toString());

                        etSecurityMoney.setBackground(getResources().getDrawable(R.drawable.bg_search));
                        etPrice.setBackground(getResources().getDrawable(R.drawable.bg_rectangle));
                        etRentPrice.setBackground(getResources().getDrawable(R.drawable.bg_search));
                        etRentDuration.setBackground(getResources().getDrawable(R.drawable.bg_search));
                    }
                    sellingType = selling_type_list.get(i).getSale_type();

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    private void getBookConditions() {
        //condition_type_list.add(new ConditionTypeModel("Select Condition"));
        condition_type_list.add(new ConditionTypeModel("As good as new"));
        condition_type_list.add(new ConditionTypeModel("Marked texts"));
        condition_type_list.add(new ConditionTypeModel("Torn or pale pages"));
        condition_type_list.add(new ConditionTypeModel("Marked texts & Torn cover page"));

        spCondition.setAdapter(new ConditionAdapterNew(getApplicationContext(),condition_type_list));

        if (bookCondiTypeValue.equals("")){
            spCondition.setSelection(0);
        }else if (bookCondiTypeValue.equals("As good as new")){
            spCondition.setSelection(0);
        }else if (bookCondiTypeValue.equals("Marked texts")){
            spCondition.setSelection(1);
        }else if (bookCondiTypeValue.equals("Torn or pale pages")){
            spCondition.setSelection(2);
        }else if (bookCondiTypeValue.equals("Marked texts & Torn cover page")){
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
                    /*etMrp.setText("");
                    etPrice.setText("");
                    etRentPrice.setText("");
                    etSecurityMoney.setText("");*/
                    /*etMrp.setEnabled(true);
                    etPrice.setEnabled(true);
                    etRentPrice.setEnabled(true);
                    etSecurityMoney.setEnabled(true);*/
                    //conditionValue = 60;
                    conditionValue = 100 - Integer.parseInt(As_good_as_new);
                    if (!etMrp.getText().toString().trim().equals("")){
                        int mrpValue = Integer.parseInt(etMrp.getText().toString().trim());
                        //int price =  (conditionValue / 100 ) * mrpValue;
                        int price =  (int)(mrpValue / 100) * conditionValue +1;
                        //int priceRent =  (int)(mrpValue / 100) * 95;
                        if (sellingType.equals("For Rent")){
                            etPrice.setText("0");
                            etRentPrice.setText(String.valueOf((price/Integer.parseInt(Rent_divisor))+1));
                            etSecurityMoney.setText(etMrp.getText().toString().trim());
                        }else if (sellingType.equals("Both")){
                            etPrice.setText(String.valueOf(price));
                            etRentPrice.setText(String.valueOf((price/Integer.parseInt(Rent_divisor))+1));
                            etSecurityMoney.setText(etMrp.getText().toString().trim());
                        }else if (sellingType.equals("For Sale")){
                            etPrice.setText(String.valueOf(price));
                            etRentPrice.setText("0");
                            etSecurityMoney.setText("0");
                        }
                    }
                }else if (bookConditionType.equals("Marked texts")){
                    /*etMrp.setText("");
                    etPrice.setText("");
                    etRentPrice.setText("");
                    etSecurityMoney.setText("");*/
                    /*etMrp.setEnabled(true);
                    etPrice.setEnabled(true);
                    etRentPrice.setEnabled(true);
                    etSecurityMoney.setEnabled(true);*/
                    //conditionValue = 40;
                    conditionValue = 100 - Integer.parseInt(Marked_texts);
                    if (!etMrp.getText().toString().trim().equals("")){
                        int mrpValue = Integer.parseInt(etMrp.getText().toString().trim());
                        //int price =  (conditionValue / 100 ) * mrpValue;
                        int price =  (int)(mrpValue / 100) * conditionValue +1;
                        //etPrice.setText(String.valueOf(price));
                        //int priceRent =  (int)(mrpValue / 100) * 95;
                        if (sellingType.equals("For Rent")){
                            etPrice.setText("0");
                            etRentPrice.setText(String.valueOf((price/Integer.parseInt(Rent_divisor))+1));
                            etSecurityMoney.setText(etMrp.getText().toString().trim());
                        }else if (sellingType.equals("Both")){
                            etPrice.setText(String.valueOf(price));
                            etRentPrice.setText(String.valueOf((price/Integer.parseInt(Rent_divisor))+1));
                            etSecurityMoney.setText(etMrp.getText().toString().trim());
                        }else if (sellingType.equals("For Sale")){
                            etPrice.setText(String.valueOf(price));
                            etRentPrice.setText("0");
                            etSecurityMoney.setText("0");
                        }
                    }
                }else if (bookConditionType.equals("Torn or pale pages")){
                   /* etMrp.setText("");
                    etPrice.setText("");
                    etRentPrice.setText("");
                    etSecurityMoney.setText("");*/
                    /*etMrp.setEnabled(true);
                    etPrice.setEnabled(true);
                    etRentPrice.setEnabled(true);
                    etSecurityMoney.setEnabled(true);*/
                    //conditionValue = 30;
                    conditionValue = 100 - Integer.parseInt(Torn_or_pale_pages);
                    if (!etMrp.getText().toString().trim().equals("")){
                        int mrpValue = Integer.parseInt(etMrp.getText().toString().trim());
                        //int price =  (conditionValue / 100 ) * mrpValue;
                        int price =  (int)(mrpValue / 100) * conditionValue +1;
                        //etPrice.setText(String.valueOf(price));
                        //int priceRent =  (int)(mrpValue / 100) * 95;
                        if (sellingType.equals("For Rent")){
                            etPrice.setText("0");
                            etRentPrice.setText(String.valueOf((price/Integer.parseInt(Rent_divisor))+1));
                            etSecurityMoney.setText(etMrp.getText().toString().trim());
                        }else if (sellingType.equals("Both")){
                            etPrice.setText(String.valueOf(price));
                            etRentPrice.setText(String.valueOf((price/Integer.parseInt(Rent_divisor))+1));
                            etSecurityMoney.setText(etMrp.getText().toString().trim());
                        }else if (sellingType.equals("For Sale")){
                            etPrice.setText(String.valueOf(price));
                            etRentPrice.setText("0");
                            etSecurityMoney.setText("0");
                        }
                    }
                }else if (bookConditionType.equals("Marked texts & Torn cover page")){
                    /*etMrp.setText("");
                    etPrice.setText("");
                    etRentPrice.setText("");
                    etSecurityMoney.setText("");*/
                    /*etMrp.setEnabled(true);
                    etPrice.setEnabled(true);
                    etRentPrice.setEnabled(true);
                    etSecurityMoney.setEnabled(true);*/
                    //conditionValue = 20;
                    conditionValue = 100 - Integer.parseInt(Marked_texts_Torn_cover_page);
                    if (!etMrp.getText().toString().trim().equals("")){
                        int mrpValue = Integer.parseInt(etMrp.getText().toString().trim());
                        //int price =  (conditionValue / 100 ) * mrpValue;
                        int price =  (int)(mrpValue / 100) * conditionValue +1;
                        //etPrice.setText(String.valueOf(price));
                        //int priceRent =  (int)(mrpValue / 100) * 95;
                        if (sellingType.equals("For Rent")){
                            etPrice.setText("0");
                            etRentPrice.setText(String.valueOf((price/Integer.parseInt(Rent_divisor))+1));
                            etSecurityMoney.setText(etMrp.getText().toString().trim());
                        }else if (sellingType.equals("Both")){
                            etPrice.setText(String.valueOf(price));
                            etRentPrice.setText(String.valueOf((price/Integer.parseInt(Rent_divisor))+1));
                            etSecurityMoney.setText(etMrp.getText().toString().trim());
                        }else if (sellingType.equals("For Sale")){
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

    public void onQRcodeScanner(View view) {
        Dexter.withContext(this).withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                        Intent intent = new Intent(UploadBookDetails.this, QRCodeScannerActivity.class);
                        startActivityForResult(intent,1);
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                }).check();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
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
                    Toast.makeText(this, "Failed to save image.Please try again. ", Toast.LENGTH_SHORT).show();
                }else {
                    Constants.SelectedBookImage = currentPhotoPath;
                    Log.d("mmmmm : ","imgUri :"+Constants.SelectedBookImage);
                    Intent intent = new Intent(this, ImageCroperActivity.class);
                    startActivity(intent);
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        Constants.SelectedBookImage = "";
        /*Constants.SelectedBookImage = "";
        editor.clear();
        editor.commit();*/
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        //editor.clear();
        //editor.commit();
        super.onStop();
    }

    public class SellingTypeAdapterNew extends BaseAdapter {
        private Context mCtx;
        private List<SellingTypeModel> mList;
        private LayoutInflater inflter;

        public SellingTypeAdapterNew(Context mCtx, List<SellingTypeModel> mList) {
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
            tvApartmentName.setText(mList.get(i).getSale_type());

            return view;
        }
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

                                    if (!temp_cat_list.get(i).getCategory_name().equals("Select Category")){
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
                Toast.makeText(UploadBookDetails.this, "Try again", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveBookDetails(String name, String author, String isbn, String publishDate, String description, String duration, String bookImgUrl,String priceMrp,String rentalPrice,String salePrice,String securityPrice,String quantity,String giveaway) {
        final ProgressDialog loginDialog = new ProgressDialog(this);
        loginDialog.setMessage("Please wait..");
        loginDialog.setCancelable(false);
        loginDialog.show();
        Holders holders = AllApiClass.getInstance().getApi();
        Call<BookEntryModel> call = holders.uploadBooks(name,database.getUserId(),shelfId,author,publishDate,description,bookImgUrl,isbn,duration,bookId,categoryId,priceMrp,rentalPrice,salePrice,securityPrice,quantity,sellingType,bookConditionType,giveaway,isOwnLirary,"",communityId);
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
                            Toast.makeText(UploadBookDetails.this, ""+response.body().getResponse().getMessage(), Toast.LENGTH_SHORT).show();
                            onBackPressed();
                        }
                    }else {
                        loginDialog.dismiss();
                        Toast.makeText(UploadBookDetails.this, ""+response.body().getResponse().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }else {
                    loginDialog.dismiss();
                    Toast.makeText(UploadBookDetails.this, "Something went wrong.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<BookEntryModel> call, Throwable t) {
                loginDialog.dismiss();
                Toast.makeText(UploadBookDetails.this, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void uploadBookImage(String book_id) {
        //Toast.makeText(this, "Image is uploaded successfully.", Toast.LENGTH_SHORT).show();
        final ProgressDialog progressBar = new ProgressDialog(this);
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
                        Toast.makeText(UploadBookDetails.this, "Book Uploaded Successfully.", Toast.LENGTH_SHORT).show();
                        onBackPressed();
                    }else {
                        progressBar.dismiss();
                        Toast.makeText(UploadBookDetails.this, ""+response.body().getResponse().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }else {
                    progressBar.dismiss();
                    Toast.makeText(UploadBookDetails.this, "Something went wrong !", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<SubmitDataResModel> call, Throwable t) {
                progressBar.dismiss();
                Toast.makeText(UploadBookDetails.this, "Check your internet !", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getShelfList() {
        Holders holders = AllApiClass.getInstance().getApi();
        Call<ShelfModel> resCall = holders.shelfList();
        //Log.d("myId",Constants.USER_APARTMENT_ID);
        resCall.enqueue(new Callback<ShelfModel>() {
            @Override
            public void onResponse(Call<ShelfModel> call, Response<ShelfModel> response) {
                if (response.isSuccessful()) {
                    if (response.body().getResponse().getCode() == 1) {
                        if (response.body().getResponse().getCell_list() != null) {
                            apartments = response.body().getResponse().getCell_list();
                            /*ShelfListData data = new ShelfListData();
                            data.set$shelf_id("");
                            data.setShelf_no("Select Shelf");
                            temp_list.add(data);
                            for (int i=0;i<apartments.size();i++){
                                temp_list.add(apartments.get(i));
                            }*/
                            spShelfNo.setAdapter(new ShelfAdapterNew(getApplicationContext(),apartments));

                            spShelfNo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                    if (i != 0 || !apartments.get(i).getShelf_no().equals("Select Shelf")){
                                        shelfId = apartments.get(i).get$shelf_id();
                                    }else {
                                        shelfId = "";
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
            public void onFailure(Call<ShelfModel> call, Throwable t) {
                Toast.makeText(UploadBookDetails.this, "Try again", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setUpToolbar(String param) {
        if (Constants.isOwnLibrary == true){
            tvToolbarTitle.setText("Upload Books In Own Library");
            tvToolbarTitle.setSelected(true);
        }else {
            tvToolbarTitle.setText("Upload Books In Community");
            tvToolbarTitle.setSelected(true);
        }

        ivToolbarBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        tvUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentBookManagement = new Intent(UploadBookDetails.this, BookListActivity.class);
                startActivity(intentBookManagement);
            }
        });
    }

    @Override
    public void onBackPressed() {
        /*editor.clear();
        editor.commit();*/
        super.onBackPressed();
    }



}