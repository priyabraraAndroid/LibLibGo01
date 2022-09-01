package com.lib.liblibgo.dashboard.apartment_admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.lib.liblibgo.R;
import com.lib.liblibgo.adapter.subadmin.CustomerAdapterTwo;
import com.lib.liblibgo.api.AllApiClass;
import com.lib.liblibgo.api.Holders;
import com.lib.liblibgo.common.UserDatabase;
import com.lib.liblibgo.model.subadmin.CustList;
import com.lib.liblibgo.model.subadmin.CustomerModel;
import com.lib.liblibgo.views.MyEditText;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ManageCutomer extends AppCompatActivity {

    private ImageView ivToolbarBack;
    private TextView tvToolbarTitle;
    private RecyclerView rvCustomer;
    private ProgressBar progBar;
    private MyEditText et_search;
    private List<CustList> myList = new ArrayList<>();
    CustomerAdapterTwo adapter;
    UserDatabase preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_cutomer);

        ivToolbarBack = (ImageView) findViewById(R.id.backTool);
        tvToolbarTitle = (TextView) findViewById(R.id.titleTool);
        setUpToolbar(getString(R.string.info_customer_management));
        rvCustomer = (RecyclerView) findViewById(R.id.rv_customer);
        progBar = (ProgressBar)findViewById(R.id.progBar);

        preferences = new UserDatabase(ManageCutomer.this);

        showCustomerList();

        et_search = (MyEditText)findViewById(R.id.et_search);
        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String newText = charSequence.toString();
                searchCustomer(newText);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    public boolean searchCustomer(String txt){
        if (myList.size() > 0) {
            final List<CustList> filteredModelList = filter(myList, txt);
            adapter.setFilter(filteredModelList);
        }
        return true;
    }

    private List<CustList> filter(List<CustList> models, String search_txt) {
        search_txt = search_txt.toLowerCase();
        final List<CustList> filteredModelList = new ArrayList<>();

        for (CustList model : models) {
            final String textName = model.getName().toLowerCase();
            if (textName.contains(search_txt)) {
                filteredModelList.add(model);
            }
        }
        return filteredModelList;
    }

    private void showCustomerList() {
        progBar.setVisibility(View.VISIBLE);
        Holders holders = AllApiClass.getInstance().getApi();
        Call<CustomerModel> modelCall = holders.getCustomer(preferences.getUserId());
        modelCall.enqueue(new Callback<CustomerModel>() {
            @Override
            public void onResponse(Call<CustomerModel> call, Response<CustomerModel> response) {
                if (response.isSuccessful()){
                    if (response.body().getResponse().getCode().equals("1")){
                        progBar.setVisibility(View.GONE);
                        myList = response.body().getResponse().getCustomer_list();
                        if (myList.size() > 0){
                            adapter = new CustomerAdapterTwo(myList, ManageCutomer.this);
                            LinearLayoutManager verticalManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
                            //verticalManager.setReverseLayout(true);
                            //verticalManager.setStackFromEnd(true);
                            rvCustomer.setLayoutManager(verticalManager);
                            rvCustomer.setAdapter(adapter);
                        }
                    }else {
                        progBar.setVisibility(View.GONE);
                        Toast.makeText(ManageCutomer.this, ""+response.body().getResponse().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }else {
                    progBar.setVisibility(View.GONE);
                    Toast.makeText(ManageCutomer.this, "Something went wrong !", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CustomerModel> call, Throwable t) {
                progBar.setVisibility(View.GONE);
                Toast.makeText(ManageCutomer.this, "Server not responding !", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void setUpToolbar(String param) {
        tvToolbarTitle.setText(param);
        ivToolbarBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                finish();
            }
        });
    }
}