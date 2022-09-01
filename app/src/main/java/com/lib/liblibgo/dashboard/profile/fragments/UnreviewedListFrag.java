package com.lib.liblibgo.dashboard.profile.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.lib.liblibgo.R;
import com.lib.liblibgo.adapter.UnReviewedBookAdapter;
import com.lib.liblibgo.api.AllApiClass;
import com.lib.liblibgo.api.Holders;
import com.lib.liblibgo.common.UserDatabase;
import com.lib.liblibgo.model.CollectedBookModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UnreviewedListFrag extends Fragment {

    private UserDatabase database;
    private ProgressBar progBar;
    private RecyclerView rvCollect;
    List<CollectedBookModel.ResData.ListData> myList = new ArrayList<>();
    UnReviewedBookAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_unreviewed_list, container, false);
        //Toast.makeText(getContext(), "No data found", Toast.LENGTH_SHORT).show();
        database = new UserDatabase(getContext());
        progBar = (ProgressBar)view.findViewById(R.id.progBar);
        rvCollect = (RecyclerView)view.findViewById(R.id.rvCollect);

        getUnreviewedBookList();

        return view;
    }

    private void getUnreviewedBookList() {
        progBar.setVisibility(View.VISIBLE);
        Holders holders = AllApiClass.getInstance().getApi();
        Call<CollectedBookModel> call = holders.getUnReviewedBooks(database.getUserId());
        call.enqueue(new Callback<CollectedBookModel>() {
            @Override
            public void onResponse(Call<CollectedBookModel> call, Response<CollectedBookModel> response) {
                if (response.isSuccessful()){
                    if (response.body().getResponse().getCode().equals("1")){
                        progBar.setVisibility(View.GONE);
                        myList = response.body().getResponse().getBook_issue_list();
                        if (myList.size() > 0){
                            adapter = new UnReviewedBookAdapter(myList, getContext());
                            LinearLayoutManager verticalManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                            // verticalManager.setReverseLayout(true);
                            // verticalManager.setStackFromEnd(true);
                            rvCollect.setLayoutManager(verticalManager);
                            rvCollect.setAdapter(adapter);
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
            public void onFailure(Call<CollectedBookModel> call, Throwable t) {
                progBar.setVisibility(View.GONE);
                Toast.makeText(getContext(), ""+t, Toast.LENGTH_SHORT).show();
            }
        });
    }
}