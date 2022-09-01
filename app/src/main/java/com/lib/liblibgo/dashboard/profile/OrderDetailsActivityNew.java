package com.lib.liblibgo.dashboard.profile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Telephony;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.JsonObject;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.lib.liblibgo.R;
import com.lib.liblibgo.activity.UserProfileActivity;
import com.lib.liblibgo.api.AllApiClass;
import com.lib.liblibgo.api.Holders;
import com.lib.liblibgo.common.Constants;
import com.lib.liblibgo.common.UserDatabase;
import com.lib.liblibgo.model.SearchResModel;
import com.lib.liblibgo.model.SubmitDataResModel;
import com.lib.liblibgo.views.MyTextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderDetailsActivityNew extends AppCompatActivity {
    private MyTextView titleTool;
    private ImageView backTool;
    private String orderId;
    private String orderNumber;
    private TextView orderNumberTv;
    private TextView orderDate;
    private ImageView ivImg;
    private TextView tvBookName;
    private TextView tvAuthorName;
    private TextView tvPaymentStatus;
    private TextView tvOrderPrice;
    private TextView tvLibcoin;
    private TextView tvUsedLibCoin;
    private TextView tvTotalPrice;
    private TextView tvInfo;
    private TextView tvUserName;
    private TextView tvOrderStatus;
    private TextView tvChangeStatus;
    private UserDatabase database;
    private RelativeLayout rlBody;
    private ProgressBar prgBar;
    private TextView tvPrice;
    private TextView tvOrderFor;
    private ImageView iv_whatsapp;
    private ImageView iv_msg;
    private SwipeRefreshLayout pullToRefresh;
    private TextView book_price_new;
    private TextView tvBookPriceNew;
    private TextView tvSecurityPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details_new);
        titleTool = findViewById(R.id.titleTool);
        backTool = findViewById(R.id.backTool);
        setTopView();

        database = new UserDatabase(this);

        orderId = getIntent().getStringExtra("order_id");
        //orderNumber = getIntent().getStringExtra("order_number");
        Log.d("order_id","Order ID : "+orderId);

        Constants.orderId = orderId;
        Log.d("order_id","Order ID : "+Constants.orderId);

        pullToRefresh = (SwipeRefreshLayout)findViewById(R.id.pullToRefresh);

        orderNumberTv = (TextView)findViewById(R.id.orderNumber);
        orderDate = (TextView)findViewById(R.id.orderDate);
        tvBookName = (TextView)findViewById(R.id.tvBookName);
        tvAuthorName = (TextView)findViewById(R.id.tvAuthorName);
        tvPrice = (TextView)findViewById(R.id.tvPrice);
        book_price_new = (TextView)findViewById(R.id.book_price_new);
        tvBookPriceNew = (TextView)findViewById(R.id.tvBookPriceNew);
        tvSecurityPrice = (TextView)findViewById(R.id.tvSecurityPrice);
        tvPaymentStatus = (TextView)findViewById(R.id.tvPaymentStatus);
        tvOrderPrice = (TextView)findViewById(R.id.tvOrderPrice);
        tvLibcoin = (TextView)findViewById(R.id.tvLibcoin);
        tvUsedLibCoin = (TextView)findViewById(R.id.tvUsedLibCoin);
        tvTotalPrice = (TextView)findViewById(R.id.tvTotalPrice);
        tvInfo = (TextView)findViewById(R.id.tvInfo);
        tvUserName = (TextView)findViewById(R.id.tvUserName);
        tvOrderStatus = (TextView)findViewById(R.id.tvOrderStatus);
        tvChangeStatus = (TextView)findViewById(R.id.tvChangeStatus);
        tvOrderFor = (TextView)findViewById(R.id.tvOrderFor);
        ivImg = (ImageView)findViewById(R.id.ivImg);
        iv_whatsapp = (ImageView)findViewById(R.id.iv_whatsapp);
        iv_msg = (ImageView)findViewById(R.id.iv_msg);

        rlBody = (RelativeLayout)findViewById(R.id.rlBody);
        prgBar = (ProgressBar)findViewById(R.id.prgBar);

        getOrderDetails();

        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getOrderDetails();
            }
        });

    }

    private void getOrderDetails() {
        prgBar.setVisibility(View.VISIBLE);
        Holders holders = AllApiClass.getInstance().getApi();
        Call<JsonObject> responseCall = holders.orderDetailsApi(database.getUserId(),orderId);
        responseCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()){
                    pullToRefresh.setRefreshing(false);
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().toString());
                        JSONObject jsonObj = jsonObject.getJSONObject("response");
                        if (jsonObj.getString("code").equals("1")){
                            rlBody.setVisibility(View.VISIBLE);
                            prgBar.setVisibility(View.GONE);
                            JSONObject object = jsonObj.getJSONObject("data");

                            //Order Information
                            orderNumberTv.setText("Order Id : #ID"+object.getString("order_no"));
                            orderDate.setText(convertTime(object.getString("order_date")));
                            if (object.getString("image_url").equals("")){
                                Glide.with(OrderDetailsActivityNew.this).load("https://sales.arecontvision.com/images/products/img_placeholder_50618_xl.jpg").into(ivImg);
                            }else {
                                Glide.with(OrderDetailsActivityNew.this).load(object.getString("image_url")).apply(new RequestOptions().override(300, 500)).into(ivImg);
                            }

                            tvBookName.setText(object.getString("book_name"));
                            tvAuthorName.setText("By, "+object.getString("author_name"));
                            tvPrice.setText("₹ "+object.getString("book_price"));
                            tvBookPriceNew.setText("₹ "+object.getString("book_price"));
                            tvSecurityPrice.setText("₹ "+object.getString("security_money"));

                            if (object.getString("order_for").equals("purchase")){
                                tvOrderFor.setText("Purchase");
                                book_price_new.setText("Book Price (Buy)");
                            }else {
                                tvOrderFor.setText("Rent for "+object.getString("rent_duration")+" days");
                                book_price_new.setText("Book Price (Rent for "+object.getString("rent_duration")+" days)");
                            }

                            if (object.getString("payment_status").equals("success")){
                                tvPaymentStatus.setText("Paid");
                                tvPaymentStatus.setTextColor(Color.parseColor("#07950D"));
                            }else {
                                tvPaymentStatus.setText("Due");
                                tvPaymentStatus.setTextColor(Color.parseColor("#D81406"));
                            }

                            //Price Information
                            tvOrderPrice.setText("₹ "+object.getString("order_amout"));
                            tvLibcoin.setText("Used Lib Coins ("+object.getString("libcoins")+")");
                            tvUsedLibCoin.setText("₹ "+object.getString("libcoins"));
                            tvTotalPrice.setText("₹ "+ String.valueOf(Integer.parseInt(object.getString("order_amout")) + Integer.parseInt(object.getString("libcoins"))));

                            //Contact Information
                            tvUserName.setText(object.getString("user_name"));
                            String phoneNum = object.getString("mobile");
                            if (object.getString("user_type").equals("seller")){
                                tvInfo.setText("Contact Seller");
                            }else {
                                tvInfo.setText("Contact Buyer");
                            }

                            iv_whatsapp.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    String url = "https://api.whatsapp.com/send?phone=91" + phoneNum;
                                    try {
                                        PackageManager pm = view.getContext().getPackageManager();
                                        pm.getPackageInfo("com.whatsapp", PackageManager.GET_ACTIVITIES);
                                        Intent i = new Intent(Intent.ACTION_VIEW);
                                        i.setData(Uri.parse(url));
                                        view.getContext().startActivity(i);
                                    } catch (PackageManager.NameNotFoundException e) {
                                        view.getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                                    }
                                }
                            });

                            iv_msg.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Dexter.withContext(OrderDetailsActivityNew.this).withPermissions(Manifest.permission.SEND_SMS)
                                            .withListener(new MultiplePermissionsListener() {
                                                @Override
                                                public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                                                    try {
                                                        String defaultSmsPackageName = Telephony.Sms.getDefaultSmsPackage(OrderDetailsActivityNew.this);
                                                        Uri _uri = Uri.parse("tel:" +  phoneNum);
                                                        Intent sendIntent = new Intent(Intent.ACTION_VIEW, _uri);
                                                        sendIntent.putExtra("address", phoneNum);
                                                        sendIntent.putExtra("sms_body", "");
                                                        sendIntent.setPackage(defaultSmsPackageName);
                                                        sendIntent.setType("vnd.android-dir/mms-sms");
                                                        startActivity(sendIntent);
                                                    }catch (Exception e){
                                                        e.printStackTrace();
                                                        Toast.makeText(OrderDetailsActivityNew.this, "SMS application not found.", Toast.LENGTH_SHORT).show();
                                                    }
                                                }

                                                @Override
                                                public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                                                    permissionToken.continuePermissionRequest();
                                                }
                                            }).check();
                                }
                            });

                            //Order Status Details
                            if (object.getString("order_status").equals("0")){
                                tvOrderStatus.setText("Order Processing");
                                if (object.getString("user_type").equals("seller")){
                                    tvChangeStatus.setVisibility(View.GONE);
                                }else {
                                    tvChangeStatus.setText("Mark as confirm");
                                    tvChangeStatus.setVisibility(View.VISIBLE);
                                }
                            }else if (object.getString("order_status").equals("1")){
                                tvOrderStatus.setText("Order Confirmed");
                                if (object.getString("user_type").equals("seller")){
                                    tvChangeStatus.setVisibility(View.VISIBLE);
                                    tvChangeStatus.setText("Mark as delivered");
                                }else {
                                    tvChangeStatus.setVisibility(View.GONE);
                                }
                            }else if (object.getString("order_status").equals("2")){
                                tvOrderStatus.setText("Order Delivered");
                                if (object.getString("user_type").equals("seller")){
                                    tvChangeStatus.setVisibility(View.GONE);
                                }else {
                                    tvChangeStatus.setVisibility(View.VISIBLE);
                                    if (object.getString("order_for").equals("purchase")){
                                        tvChangeStatus.setText("Mark as sold");
                                    }else {
                                        tvChangeStatus.setText("Mark as lent");
                                    }
                                }
                            }else if (object.getString("order_status").equals("3")){
                                tvOrderStatus.setText("Order Complete");
                                tvChangeStatus.setVisibility(View.GONE);
                            }else if (object.getString("order_status").equals("4")){
                                tvOrderStatus.setText("Order  Received");
                                if (object.getString("user_type").equals("seller")){
                                    tvChangeStatus.setVisibility(View.VISIBLE);
                                    tvChangeStatus.setText("Mark as returned");
                                }else {
                                    tvChangeStatus.setVisibility(View.GONE);
                                }
                            }else if (object.getString("order_status").equals("5")){
                                tvOrderStatus.setText("Book Returned");
                                if (object.getString("user_type").equals("seller")){
                                    tvChangeStatus.setVisibility(View.GONE);
                                }else {
                                    tvChangeStatus.setVisibility(View.VISIBLE);
                                    tvChangeStatus.setText("Confirm return");
                                }
                            }else if (object.getString("order_status").equals("6")){
                                tvOrderStatus.setText("Return Complete");
                                tvChangeStatus.setVisibility(View.GONE);
                            }

                            tvChangeStatus.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    String status = "0";
                                    if (tvChangeStatus.getText().toString().trim().equals("Mark as confirm")){
                                        status = "1";
                                        changeOrderStatus(status);
                                    }else if (tvChangeStatus.getText().toString().trim().equals("Mark as delivered")){
                                        status = "2";
                                        changeOrderStatus(status);
                                    }else if (tvChangeStatus.getText().toString().trim().equals("Mark as sold")){
                                        status = "3";
                                        changeOrderStatus(status);
                                    }else if (tvChangeStatus.getText().toString().trim().equals("Mark as lent")){
                                        status = "4";
                                        changeOrderStatus(status);
                                    }else if (tvChangeStatus.getText().toString().trim().equals("Mark as returned")){
                                        status = "5";
                                        changeOrderStatus(status);
                                    }else if (tvChangeStatus.getText().toString().trim().equals("Confirm return")){
                                        status = "6";
                                        changeOrderStatus(status);
                                    }
                                }
                            });

                        }else {
                            rlBody.setVisibility(View.INVISIBLE);
                            prgBar.setVisibility(View.GONE);
                            Toast.makeText(OrderDetailsActivityNew.this, "Order details not found.", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        rlBody.setVisibility(View.INVISIBLE);
                        prgBar.setVisibility(View.GONE);
                        Toast.makeText(OrderDetailsActivityNew.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }else {
                    pullToRefresh.setRefreshing(false);
                    Toast.makeText(OrderDetailsActivityNew.this, "Something went wrong.", Toast.LENGTH_SHORT).show();
                    rlBody.setVisibility(View.INVISIBLE);
                    prgBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                pullToRefresh.setRefreshing(false);
                rlBody.setVisibility(View.INVISIBLE);
                prgBar.setVisibility(View.GONE);
                Toast.makeText(OrderDetailsActivityNew.this, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void changeOrderStatus(String status) {
        //Toast.makeText(this, ""+status, Toast.LENGTH_SHORT).show();
        final ProgressDialog progressBar = new ProgressDialog(OrderDetailsActivityNew.this);
        progressBar.setMessage("Please wait...");
        progressBar.setCancelable(false);
        progressBar.show();
        Holders holders = AllApiClass.getInstance().getApi();
        Call<SubmitDataResModel> resCall = holders.changeOrderStatus(orderId,status);
        resCall.enqueue(new Callback<SubmitDataResModel>() {
            @Override
            public void onResponse(Call<SubmitDataResModel> call, Response<SubmitDataResModel> response) {
                if (response.isSuccessful()){
                    if (response.body().getResponse().getCode() == 1){
                        progressBar.dismiss();
                        getOrderDetails();
                        //Toast.makeText(OrderDetailsActivityNew.this, "Redeem request has been sent.", Toast.LENGTH_SHORT).show();
                    }else {
                        progressBar.dismiss();
                        Toast.makeText(OrderDetailsActivityNew.this, ""+response.body().getResponse().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }else {
                    progressBar.dismiss();
                    Toast.makeText(OrderDetailsActivityNew.this, "Something went wrong !", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<SubmitDataResModel> call, Throwable t) {
                progressBar.dismiss();
                Toast.makeText(OrderDetailsActivityNew.this, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String convertTime(String time) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        SimpleDateFormat format1 = new SimpleDateFormat("dd MMM yyyy");
        java.util.Date date = null;

        try {
            date = format.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String convertedDate = format1.format(date);

        return convertedDate;
    }

    private void setTopView() {
        titleTool.setText("Order Details");
        backTool.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}