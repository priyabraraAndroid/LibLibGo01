package com.lib.liblibgo.dashboard.profile.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.lib.liblibgo.R;
import com.lib.liblibgo.adapter.ReturnAdapter;
import com.lib.liblibgo.api.AllApiClass;
import com.lib.liblibgo.api.Holders;
import com.lib.liblibgo.common.UserDatabase;
import com.lib.liblibgo.model.CollectedBookModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class IssuedListFrag extends Fragment {

    private UserDatabase database;
    private ProgressBar progBar;
    private RecyclerView rvCollect;
    List<CollectedBookModel.ResData.ListData> myList = new ArrayList<>();
    ReturnAdapter adapter;
    private Context mContext;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_issued_list, container, false);
        //Toast.makeText(mContext, "No data found", Toast.LENGTH_SHORT).show();

        database = new UserDatabase(mContext);
        progBar = (ProgressBar)view.findViewById(R.id.progBar);
        rvCollect = view.findViewById(R.id.rvCollect);

        getCollectBook();

        return view;
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
                            adapter = new ReturnAdapter(myList, mContext);
                            LinearLayoutManager verticalManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
                            // verticalManager.setReverseLayout(true);
                            // verticalManager.setStackFromEnd(true);
                            rvCollect.setLayoutManager(verticalManager);
                            rvCollect.setAdapter(adapter);
                        }
                    }else {
                        progBar.setVisibility(View.GONE);
                        Toast.makeText(mContext, ""+response.body().getResponse().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }else {
                    progBar.setVisibility(View.GONE);
                    Toast.makeText(mContext, "Something went wrong !", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CollectedBookModel> call, Throwable t) {
                progBar.setVisibility(View.GONE);
                Toast.makeText(mContext, "Something went wrong !", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onAttach(@NonNull Context context) {
        mContext = context;
        super.onAttach(context);
    }
}