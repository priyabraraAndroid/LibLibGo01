package com.lib.liblibgo.dashboard.book_return;

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
import com.lib.liblibgo.adapter.ReturnAdapter;
import com.lib.liblibgo.api.AllApiClass;
import com.lib.liblibgo.api.Holders;
import com.lib.liblibgo.common.UserDatabase;
import com.lib.liblibgo.model.BookModel;
import com.lib.liblibgo.model.CollectedBookModel;
import com.lib.liblibgo.views.MyTextView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReturnBookActivity extends AppCompatActivity {
    private static final String TAG = "ReturnBookActivity";
    private MyTextView titleTool;
    private ImageView backTool;
    private RecyclerView rvCollect;
    private List<BookModel> bList;
    private UserDatabase returnDatabase;
    private EditText searchBook;
    private List<BookModel> mList;
    private UserDatabase database;
    private ProgressBar progBar;
    List<CollectedBookModel.ResData.ListData> myList = new ArrayList<>();
    ReturnAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collect_book);
        rvCollect = findViewById(R.id.rvCollect);
        titleTool = findViewById(R.id.titleTool);
        backTool = findViewById(R.id.backTool);
        searchBook = findViewById(R.id.et_search);
        setTopView(getString(R.string.info_book_return));

        database = new UserDatabase(ReturnBookActivity.this);
        progBar = (ProgressBar)findViewById(R.id.progBar);

        getCollectBook();

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

    private boolean searchBookList(String txt){
        if (myList.size() > 0) {
            final List<CollectedBookModel.ResData.ListData> filteredModelList = filter(myList, txt);
            adapter.setFilter(filteredModelList);
        }
        return true;
    }

    private List<CollectedBookModel.ResData.ListData> filter(List<CollectedBookModel.ResData.ListData> models, String search_txt) {

        search_txt = search_txt.toLowerCase();
        final List<CollectedBookModel.ResData.ListData> filteredModelList = new ArrayList<>();

        for (CollectedBookModel.ResData.ListData model : models) {
            final String textName = model.getBook_name().toLowerCase();
            final String textNo = model.getAuthor_name().toLowerCase();
            if (textName.contains(search_txt) || textNo.contains(search_txt)) {
                filteredModelList.add(model);
            }
        }
        return filteredModelList;
    }

    private void getCollectBook() {
        progBar.setVisibility(View.VISIBLE);
        Holders holders = AllApiClass.getInstance().getApi();
        Call<CollectedBookModel> responseCall = holders.myCollectedBooks(database.getUserId());
        responseCall.enqueue(new Callback<CollectedBookModel>() {
            @Override
            public void onResponse(Call<CollectedBookModel> call, Response<CollectedBookModel> response) {
                if (response.isSuccessful()){
                    if (response.body().getResponse().getCode().equals("1")){
                        progBar.setVisibility(View.GONE);
                        myList = response.body().getResponse().getBook_issue_list();
                        if (myList.size() > 0){
                            adapter = new ReturnAdapter(myList, ReturnBookActivity.this);
                            LinearLayoutManager verticalManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
                            // verticalManager.setReverseLayout(true);
                            // verticalManager.setStackFromEnd(true);
                            rvCollect.setLayoutManager(verticalManager);
                            rvCollect.setAdapter(adapter);
                        }
                    }else {
                        progBar.setVisibility(View.GONE);
                        Toast.makeText(ReturnBookActivity.this, ""+response.body().getResponse().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }else {
                    progBar.setVisibility(View.GONE);
                    Toast.makeText(ReturnBookActivity.this, "Something went wrong !", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CollectedBookModel> call, Throwable t) {
                progBar.setVisibility(View.GONE);
                Toast.makeText(ReturnBookActivity.this, "Something went wrong !", Toast.LENGTH_SHORT).show();
            }
        });
    }

}