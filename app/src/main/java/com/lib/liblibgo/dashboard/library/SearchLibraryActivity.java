package com.lib.liblibgo.dashboard.library;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.lib.liblibgo.R;
import com.lib.liblibgo.adapter.LibraryAdapter;
import com.lib.liblibgo.api.AllApiClass;
import com.lib.liblibgo.api.Holders;
import com.lib.liblibgo.common.UserDatabase;
import com.lib.liblibgo.model.NearMeBookModel;
import com.lib.liblibgo.model.NearMeLibraryModel;
import com.lib.liblibgo.model.NearMeOwnLibraryModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchLibraryActivity extends AppCompatActivity {

    private TextView titleTool;
    private ImageView backTool;
    private EditText searchBook;
    private RecyclerView rvAllLibrary;
    private ProgressBar progBar;
    private List<NearMeOwnLibraryModel.ResModelData.NewrmeLibraryList> libList = new ArrayList<>();
    private LibraryAdapter adapter;
    private UserDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_library);

        database = new UserDatabase(this);

        titleTool = findViewById(R.id.titleTool);
        backTool = findViewById(R.id.backTool);
        setTopView(getString(R.string.search_library));

        searchBook = findViewById(R.id.et_search);
        rvAllLibrary = findViewById(R.id.rl_library);
        progBar = (ProgressBar)findViewById(R.id.progBar);

        searchBook.setHint("Search Library By Name");

        searchBook.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(searchBook, InputMethodManager.SHOW_IMPLICIT);

        searchBook.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String txt = charSequence.toString();
                if (txt.equals("")){
                    rvAllLibrary.setVisibility(View.GONE);
                }else {
                    getAllLibrary(txt);
                    rvAllLibrary.setVisibility(View.VISIBLE);
                }
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
            }
        });
    }

    private void getAllLibrary(String txt) {
        progBar.setVisibility(View.GONE);
        Holders holders = AllApiClass.getInstance().getApi();
        Call<NearMeOwnLibraryModel> call = holders.searchLibrary(txt,"1");
        call.enqueue(new Callback<NearMeOwnLibraryModel>() {
            @Override
            public void onResponse(Call<NearMeOwnLibraryModel> call, Response<NearMeOwnLibraryModel> response) {
                if (response.isSuccessful()){
                    if (response.body().getResponse().getCode().equals("1")){
                        progBar.setVisibility(View.GONE);
                        libList = response.body().getResponse().getLibrary_list();
                        if (libList.size() > 0){
                            //rvAllLibrary.setVisibility(View.VISIBLE);
                            adapter = new LibraryAdapter(libList, SearchLibraryActivity.this);
                            LinearLayoutManager verticalManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
                            // verticalManager.setReverseLayout(true);
                            // verticalManager.setStackFromEnd(true);
                            rvAllLibrary.setLayoutManager(verticalManager);
                            rvAllLibrary.setAdapter(adapter);
                        }else {
                            rvAllLibrary.setVisibility(View.GONE);
                            progBar.setVisibility(View.GONE);
                        }
                    }else {
                        progBar.setVisibility(View.GONE);
                        Toast.makeText(SearchLibraryActivity.this, ""+response.body().getResponse().getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }else {
                    progBar.setVisibility(View.GONE);
                    Toast.makeText(SearchLibraryActivity.this, "Something went wrong.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<NearMeOwnLibraryModel> call, Throwable t) {
                progBar.setVisibility(View.GONE);
                Toast.makeText(SearchLibraryActivity.this, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}