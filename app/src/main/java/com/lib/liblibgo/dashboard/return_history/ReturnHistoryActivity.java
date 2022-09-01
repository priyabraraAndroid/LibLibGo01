package com.lib.liblibgo.dashboard.return_history;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lib.liblibgo.R;
import com.lib.liblibgo.adapter.ReturnHistoryAdapter;
import com.lib.liblibgo.api.AllApiClass;
import com.lib.liblibgo.api.Holders;
import com.lib.liblibgo.common.UserDatabase;
import com.lib.liblibgo.model.BookModel;
import com.lib.liblibgo.model.ReturnHisModel;
import com.lib.liblibgo.views.MyTextView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReturnHistoryActivity extends AppCompatActivity {

    private MyTextView titleTool;
    private ImageView backTool;
    private RecyclerView rvCollect;
    private List<BookModel> bList;
    private EditText searchBook;
    private UserDatabase userData;
    private static String TAG = "ReturnHistoryActivity";
    private ProgressBar progBar;
    List<ReturnHisModel.ResData.DataList> myList = new ArrayList<>();
    ReturnHistoryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collect_book);
        rvCollect = findViewById(R.id.rvCollect);
        titleTool = findViewById(R.id.titleTool);
        backTool = findViewById(R.id.backTool);
        searchBook = findViewById(R.id.et_search);
        setTopView(getString(R.string.info_return_history));
        userData = new UserDatabase(ReturnHistoryActivity.this);
        progBar = (ProgressBar)findViewById(R.id.progBar);
        getReturnHistory();

        searchBook.setHint("Search by book name or author name");

        searchBook.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String newText = charSequence.toString();
                searchBookList(newText);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


    }

    private boolean searchBookList(String txt){
        if (myList.size() > 0) {
            final List<ReturnHisModel.ResData.DataList> filteredModelList = filter(myList, txt);
            adapter.setFilter(filteredModelList);
        }
        return true;
    }

    private List<ReturnHisModel.ResData.DataList> filter(List<ReturnHisModel.ResData.DataList> models, String search_txt) {

        search_txt = search_txt.toLowerCase();
        final List<ReturnHisModel.ResData.DataList> filteredModelList = new ArrayList<>();

        for (ReturnHisModel.ResData.DataList model : models) {
            final String textName = model.getBook_name().toLowerCase();
            final String textNo = model.getAuthor_name().toLowerCase();
            if (textName.contains(search_txt) || textNo.contains(search_txt)) {
                filteredModelList.add(model);
            }
        }
        return filteredModelList;
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

    private void getReturnHistory() {
        progBar.setVisibility(View.VISIBLE);
        Holders holders = AllApiClass.getInstance().getApi();
        Call<ReturnHisModel> call = holders.myBookReturnHistory(userData.getUserId());
        call.enqueue(new Callback<ReturnHisModel>() {
            @Override
            public void onResponse(Call<ReturnHisModel> call, Response<ReturnHisModel> response) {
                if (response.isSuccessful()){
                    if (response.body().getResponse().getCode().equals("1")){
                        progBar.setVisibility(View.GONE);
                        myList = response.body().getResponse().getReturn_history();
                        if (myList.size() > 0){
                            adapter = new ReturnHistoryAdapter(myList, ReturnHistoryActivity.this);
                            LinearLayoutManager verticalManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
                            // verticalManager.setReverseLayout(true);
                            // verticalManager.setStackFromEnd(true);
                            rvCollect.setLayoutManager(verticalManager);
                            rvCollect.setAdapter(adapter);
                        }
                    }else {
                        progBar.setVisibility(View.GONE);
                        Toast.makeText(ReturnHistoryActivity.this, ""+response.body().getResponse().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }else {
                    progBar.setVisibility(View.GONE);
                    Toast.makeText(ReturnHistoryActivity.this, "Something went wrong !", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ReturnHisModel> call, Throwable t) {
                progBar.setVisibility(View.GONE);
                Toast.makeText(ReturnHistoryActivity.this, ""+t, Toast.LENGTH_SHORT).show();
            }
        });

    }
}