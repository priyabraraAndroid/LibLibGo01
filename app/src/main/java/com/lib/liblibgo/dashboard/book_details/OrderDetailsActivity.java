package com.lib.liblibgo.dashboard.book_details;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.lib.liblibgo.R;
import com.lib.liblibgo.adapter.ItemAdapter;
import com.lib.liblibgo.adapter.LibraryWiseOrderAdapter;
import com.lib.liblibgo.api.AllApiClass;
import com.lib.liblibgo.api.Holders;
import com.lib.liblibgo.model.MyOrderDetailsModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderDetailsActivity extends AppCompatActivity {
    private TextView titleTool;
    private ImageView backTool;
    private String orderId="";
    private RecyclerView rvItems;
    private TextView tvOrderNumber;
    private TextView tvCustomerName;
    private TextView tvOrderAmount;
    private TextView tvCustomerAdr;
    private TextView tvOrderDate;
    private TextView tvOrderStatus;
    private ProgressBar progBar;
    //private List<MyOrderDetailsModel.ResData.MyOrderItemList> myList = new ArrayList<>();
    private List<MyOrderDetailsModel.ResData.LibraryList> myList = new ArrayList<>();
    private LibraryWiseOrderAdapter adapter;
    private String orderDetailsId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);

        titleTool = findViewById(R.id.titleTool);
        backTool = findViewById(R.id.backTool);
        setTopView(getString(R.string.order_details));

        orderId = getIntent().getStringExtra("order_id");
        orderDetailsId = getIntent().getStringExtra("order_details_id");

        rvItems = (RecyclerView)findViewById(R.id.rvItems);
        progBar = (ProgressBar)findViewById(R.id.progBar);

        tvOrderNumber = (TextView)findViewById(R.id.tvOrderNumber);
        tvCustomerName = (TextView)findViewById(R.id.tvCustomerName);
        tvOrderAmount = (TextView)findViewById(R.id.tvOrderAmount);
        tvCustomerAdr = (TextView)findViewById(R.id.tvCustomerAdr);
        tvOrderDate = (TextView)findViewById(R.id.tvOrderDate);
        tvOrderStatus = (TextView)findViewById(R.id.tvOrderStatus);

        tvOrderNumber.setText("Order ID : "+getIntent().getStringExtra("order_number"));
        tvCustomerName.setText(getIntent().getStringExtra("user_name"));
        tvOrderAmount.setText("₹ "+getIntent().getStringExtra("amount"));
        tvCustomerAdr.setText(getIntent().getStringExtra("user_adr"));
        tvOrderDate.setText(convertTime(getIntent().getStringExtra("date")));

        /*if (getIntent().getStringExtra("status").equals("0")){
            tvOrderStatus.setText("Processing");
        }else if (getIntent().getStringExtra("status").equals("1")){
            tvOrderStatus.setText("Order Confirmed");
        }else if (getIntent().getStringExtra("status").equals("2")){
            tvOrderStatus.setText("Picked Up");
        }else if (getIntent().getStringExtra("status").equals("3")){
            tvOrderStatus.setText("Out Of Delivery");
        }else if (getIntent().getStringExtra("status").equals("4")){
            tvOrderStatus.setText("Delivered");
        }*/

        getItemList();

    }

    private void getItemList() {
        progBar.setVisibility(View.VISIBLE);
        Holders holders = AllApiClass.getInstance().getApi();
        Call<MyOrderDetailsModel> responseCall = holders.getItemList(orderId);
        responseCall.enqueue(new Callback<MyOrderDetailsModel>() {
            @Override
            public void onResponse(Call<MyOrderDetailsModel> call, Response<MyOrderDetailsModel> response) {
                if (response.isSuccessful()){
                    if (response.body().getResponse().getCode().equals("1")){
                        progBar.setVisibility(View.GONE);
                        myList = response.body().getResponse().getLibrary_list();
                        if (myList.size() > 0){
                            adapter = new LibraryWiseOrderAdapter(myList, OrderDetailsActivity.this);
                            LinearLayoutManager verticalManager = new LinearLayoutManager(OrderDetailsActivity.this, LinearLayoutManager.VERTICAL, false);
                            // verticalManager.setReverseLayout(true);
                            // verticalManager.setStackFromEnd(true);
                            rvItems.setLayoutManager(verticalManager);
                            rvItems.setAdapter(adapter);
                        }
                    }else {
                        progBar.setVisibility(View.GONE);
                        Toast.makeText(OrderDetailsActivity.this, "Error : "+response.body().getResponse().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }else {
                    progBar.setVisibility(View.GONE);
                    Toast.makeText(OrderDetailsActivity.this, "Something went wrong !!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<MyOrderDetailsModel> call, Throwable t) {
                progBar.setVisibility(View.GONE);
                Toast.makeText(OrderDetailsActivity.this, "Something went wrong !!!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String convertTime(String time) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat format1 = new SimpleDateFormat("dd MMM yyyy hh:mm aa");
        java.util.Date date = null;

        try {
            date = format.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String convertedDate = format1.format(date);

        return convertedDate;
    }

    private void setTopView(String string) {
        titleTool.setText(string);
        backTool.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                finish();
            }
        });
    }

}