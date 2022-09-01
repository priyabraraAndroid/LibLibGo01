package com.lib.liblibgo.dashboard.profile.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.lib.liblibgo.R;
import com.lib.liblibgo.adapter.subadmin.BookHisAdapter;
import com.lib.liblibgo.api.AllApiClass;
import com.lib.liblibgo.api.Holders;
import com.lib.liblibgo.common.UserDatabase;
import com.lib.liblibgo.model.subadmin.BookHisModel;
import com.lib.liblibgo.views.MyEditText;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookHistoryFrag extends Fragment {
    private static final String TAG = "BookHistoryActivity";
    private ImageView ivToolbarBack;
    private TextView tvToolbarTitle;
    private RecyclerView rvHistory;
    private ProgressBar progBar;
    private List<BookHisModel.ResData.CustomerList> bList = new ArrayList<>();
    private BookHisAdapter adapter;
    private MyEditText et_search;
    UserDatabase preferences;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_book_history, container, false);
        //Toast.makeText(getContext(), "No data found.", Toast.LENGTH_SHORT).show();
        preferences = new UserDatabase(getContext());
        rvHistory = (RecyclerView)view.findViewById(R.id.rv_history);
        progBar = (ProgressBar)view.findViewById(R.id.progBar);

        et_search = (MyEditText)view.findViewById(R.id.et_search);
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

        showBookHistoryList();

        return view;
    }

    public boolean searchBook(String txt){
        if (bList.size() > 0) {
            final List<BookHisModel.ResData.CustomerList> filteredModelList = filter(bList, txt);
            adapter.setFilter(filteredModelList);
        }
        return true;
    }

    private List<BookHisModel.ResData.CustomerList> filter(List<BookHisModel.ResData.CustomerList> models, String search_txt) {
        search_txt = search_txt.toLowerCase();
        final List<BookHisModel.ResData.CustomerList> filteredModelList = new ArrayList<>();

        for (BookHisModel.ResData.CustomerList model : models) {
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
        Call<BookHisModel> modelCall = holders.getBookHistory(preferences.getUserId());
        modelCall.enqueue(new Callback<BookHisModel>() {
            @Override
            public void onResponse(Call<BookHisModel> call, Response<BookHisModel> response) {
                if (response.isSuccessful()){
                    if (response.body().getResponse().getCode().equals("1")){
                        progBar.setVisibility(View.GONE);
                        bList = response.body().getResponse().getCustomer_list();
                        if (bList.size() > 0){
                            adapter = new BookHisAdapter(bList, getContext());
                            LinearLayoutManager verticalManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                            //verticalManager.setReverseLayout(true);
                            //verticalManager.setStackFromEnd(true);
                            rvHistory.setLayoutManager(verticalManager);
                            rvHistory.setAdapter(adapter);
                        }
                    }else {
                        progBar.setVisibility(View.GONE);
                        Toast.makeText(getContext(), ""+response.body().getResponse().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }else {
                    progBar.setVisibility(View.GONE);
                    Toast.makeText(getContext(), "Something went wrong !", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<BookHisModel> call, Throwable t) {
                progBar.setVisibility(View.GONE);
                Toast.makeText(getContext(), ""+t, Toast.LENGTH_SHORT).show();
            }
        });
    }
}