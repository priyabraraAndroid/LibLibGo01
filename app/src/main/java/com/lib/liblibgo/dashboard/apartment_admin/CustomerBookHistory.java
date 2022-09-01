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
import com.lib.liblibgo.adapter.subadmin.BookAdaper;
import com.lib.liblibgo.api.AllApiClass;
import com.lib.liblibgo.api.Holders;
import com.lib.liblibgo.common.UserDatabase;
import com.lib.liblibgo.model.subadmin.CustBookHisModel;
import com.lib.liblibgo.views.MyEditText;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CustomerBookHistory extends AppCompatActivity {

    private String custId;
    private ImageView ivToolbarBack;
    private TextView tvToolbarTitle;
    private RecyclerView rvBookHistory;
    private ProgressBar progBar;
    private List<CustBookHisModel.ResData.BookList> myList = new ArrayList<>();
    BookAdaper adapter;
    private MyEditText et_search;
    UserDatabase preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_book_history);
        ivToolbarBack = (ImageView) findViewById(R.id.backTool);
        tvToolbarTitle = (TextView) findViewById(R.id.titleTool);
        setUpToolbar(getString(R.string.info_customer_book_history));

        custId = getIntent().getStringExtra("customer_id");

        rvBookHistory = (RecyclerView) findViewById(R.id.rv_book);
        progBar = (ProgressBar)findViewById(R.id.progBar);

        preferences = new UserDatabase(CustomerBookHistory.this);

        showBookHistoryList();

        et_search = (MyEditText)findViewById(R.id.et_search);
        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String newText = charSequence.toString();
                searchBook(newText);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

    public boolean searchBook(String txt){
        if (myList.size() > 0) {
            final List<CustBookHisModel.ResData.BookList> filteredModelList = filter(myList, txt);
            adapter.setFilter(filteredModelList);
        }
        return true;
    }

    private List<CustBookHisModel.ResData.BookList> filter(List<CustBookHisModel.ResData.BookList> models, String search_txt) {
        search_txt = search_txt.toLowerCase();
        final List<CustBookHisModel.ResData.BookList> filteredModelList = new ArrayList<>();

        for (CustBookHisModel.ResData.BookList model : models) {
            final String textName = model.getBook_name().toLowerCase();
            final String textNumber = model.getAuthor_name().toLowerCase();
            if (textName.contains(search_txt) || textNumber.contains(search_txt)) {
                filteredModelList.add(model);
            }
        }
        return filteredModelList;
    }

    private void showBookHistoryList() {
        progBar.setVisibility(View.VISIBLE);
        Holders holders = AllApiClass.getInstance().getApi();
        Call<CustBookHisModel> modelCall = holders.showCustomerBookHistory(custId);
        modelCall.enqueue(new Callback<CustBookHisModel>() {
            @Override
            public void onResponse(Call<CustBookHisModel> call, Response<CustBookHisModel> response) {
                if (response.isSuccessful()){
                    if (response.body().getResponse().getCode().equals("1")){
                        progBar.setVisibility(View.GONE);
                        myList = response.body().getResponse().getBook_history();
                        if (myList.size() > 0){
                            adapter = new BookAdaper(myList, CustomerBookHistory.this);
                            LinearLayoutManager verticalManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
                            //verticalManager.setReverseLayout(true);
                            //verticalManager.setStackFromEnd(true);
                            rvBookHistory.setLayoutManager(verticalManager);
                            rvBookHistory.setAdapter(adapter);
                        }
                    }else {
                        progBar.setVisibility(View.GONE);
                        Toast.makeText(CustomerBookHistory.this, ""+response.body().getResponse().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }else {
                    progBar.setVisibility(View.GONE);
                    Toast.makeText(CustomerBookHistory.this, "Something went wrong !", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CustBookHisModel> call, Throwable t) {
                progBar.setVisibility(View.GONE);
                Toast.makeText(CustomerBookHistory.this, "Server not responding !", Toast.LENGTH_SHORT).show();
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