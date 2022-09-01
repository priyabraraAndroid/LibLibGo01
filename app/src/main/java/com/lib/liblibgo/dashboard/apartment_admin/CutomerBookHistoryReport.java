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
import com.lib.liblibgo.api.AllApiClass;
import com.lib.liblibgo.api.Holders;
import com.lib.liblibgo.common.UserDatabase;
import com.lib.liblibgo.model.subadmin.CustHisAdapter;
import com.lib.liblibgo.model.subadmin.CustListModel;
import com.lib.liblibgo.views.MyEditText;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CutomerBookHistoryReport extends AppCompatActivity {
    private static final String TAG = "CustomerHistoryActivity";
    private ImageView ivToolbarBack;
    private TextView tvToolbarTitle;
    //private List<CustListModel> mList;
    private RecyclerView rvCustomerHistory;
    private ProgressBar progBar;
    private MyEditText et_search;
    UserDatabase preferences;
    private List<CustListModel.ResData.ListData> myList = new ArrayList<>();
    CustHisAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cutomer_book_history_report);

        ivToolbarBack = (ImageView) findViewById(R.id.backTool);
        tvToolbarTitle = (TextView) findViewById(R.id.titleTool);
        setUpToolbar(getString(R.string.info_customer_history));
        rvCustomerHistory = (RecyclerView) findViewById(R.id.rv_customer);
        progBar = (ProgressBar)findViewById(R.id.progBar);

        preferences = new UserDatabase(CutomerBookHistoryReport.this);

        showCustHistory();

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
            final List<CustListModel.ResData.ListData> filteredModelList = filter(myList, txt);
            adapter.setFilter(filteredModelList);
        }
        return true;
    }

    private List<CustListModel.ResData.ListData> filter(List<CustListModel.ResData.ListData> models, String search_txt) {
        search_txt = search_txt.toLowerCase();
        final List<CustListModel.ResData.ListData> filteredModelList = new ArrayList<>();

        for (CustListModel.ResData.ListData model : models) {
            final String textName = model.getUser_name().toLowerCase();
            if (textName.contains(search_txt)) {
                filteredModelList.add(model);
            }
        }
        return filteredModelList;
    }

    private void showCustHistory() {
        progBar.setVisibility(View.VISIBLE);
        Holders holders = AllApiClass.getInstance().getApi();
        Call<CustListModel> modelCall = holders.showCustomerHistory(preferences.getUserId());
        modelCall.enqueue(new Callback<CustListModel>() {
            @Override
            public void onResponse(Call<CustListModel> call, Response<CustListModel> response) {
                if (response.isSuccessful()){
                    if (response.body().getResponse().getCode().equals("1")){
                        progBar.setVisibility(View.GONE);
                        myList = response.body().getResponse().getCustomer_list();
                        if (myList.size() > 0){
                            adapter = new CustHisAdapter(myList, CutomerBookHistoryReport.this);
                            LinearLayoutManager verticalManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
                            //verticalManager.setReverseLayout(true);
                            //verticalManager.setStackFromEnd(true);
                            rvCustomerHistory.setLayoutManager(verticalManager);
                            rvCustomerHistory.setAdapter(adapter);
                        }
                    }else {
                        progBar.setVisibility(View.GONE);
                        Toast.makeText(CutomerBookHistoryReport.this, ""+response.body().getResponse().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }else {
                    progBar.setVisibility(View.GONE);
                    Toast.makeText(CutomerBookHistoryReport.this, "Something went wrong !", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CustListModel> call, Throwable t) {
                progBar.setVisibility(View.GONE);
                Toast.makeText(CutomerBookHistoryReport.this, "Server not responding !", Toast.LENGTH_SHORT).show();
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