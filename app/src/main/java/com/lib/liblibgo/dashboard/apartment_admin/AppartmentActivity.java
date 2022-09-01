package com.lib.liblibgo.dashboard.apartment_admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lib.liblibgo.R;
import com.lib.liblibgo.adapter.ApartmentSubAdminAdapter;
import com.lib.liblibgo.api.AllApiClass;
import com.lib.liblibgo.api.Holders;
import com.lib.liblibgo.common.Constants;
import com.lib.liblibgo.model.ApartmentModel;
import com.lib.liblibgo.model.ResModel;
import com.lib.liblibgo.views.MyEditText;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AppartmentActivity extends AppCompatActivity implements View.OnClickListener{

    private ImageView backTool;
    private TextView titleTool;
    private ImageView ivCreate;
    int mCount = 0;
    private List<ApartmentModel> mListName = new ArrayList<>();
    private RecyclerView rvApartment;
    private MyEditText et_search;
    ApartmentSubAdminAdapter apartmentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appartment);
        titleTool = findViewById(R.id.titleTool);
        backTool = findViewById(R.id.backTool);
        setUpToolbar(getString(R.string.info_apartment_management));
        ivCreate = (ImageView) findViewById(R.id.ivCreate);
        rvApartment = (RecyclerView) findViewById(R.id.rvApartment);
        rvApartment.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        ivCreate.setOnClickListener(this);
        AllApartment();

        et_search = (MyEditText)findViewById(R.id.et_search);
        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String newText = charSequence.toString();
                searchApartment(newText);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

    private boolean searchApartment(String txt){
        if (mListName.size() > 0) {
            final List<ApartmentModel> filteredModelList = filter(mListName, txt);
            apartmentAdapter.setFilter(filteredModelList);
        }
        return true;
    }

    private List<ApartmentModel> filter(List<ApartmentModel> models, String search_txt) {

        search_txt = search_txt.toLowerCase();
        final List<ApartmentModel> filteredModelList = new ArrayList<>();

        for (ApartmentModel model : models) {
            final String textName = model.getApartmentName().toLowerCase();
            if (textName.contains(search_txt)) {
                filteredModelList.add(model);
            }
        }
        return filteredModelList;
    }

    private void AllApartment() {
        Holders holders = AllApiClass.getInstance().getApi();
        Call<ResModel> modelCall = holders.apartmentLis();
        modelCall.enqueue(new Callback<ResModel>() {
            @Override
            public void onResponse(Call<ResModel> call, Response<ResModel> response) {
                if (response.isSuccessful()){
                    if (response.body().getResponse().getCode()==1){
                        if (response.body().getResponse().getApartmentModel()!=null){
                            mListName = response.body().getResponse().getApartmentModel();
                            apartmentAdapter = new ApartmentSubAdminAdapter(mListName,AppartmentActivity.this);
                            rvApartment.setAdapter(apartmentAdapter);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ResModel> call, Throwable t) {

            }
        });
    }

    private void setUpToolbar(String param) {
        titleTool.setText(param);
        backTool.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                finish();
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivCreate:
                Intent intentCreate = new Intent(AppartmentActivity.this, AddApartmentActivity.class);
                startActivityForResult(intentCreate, Constants.REQUEST_CREATE);
                break;
        }
    }
}