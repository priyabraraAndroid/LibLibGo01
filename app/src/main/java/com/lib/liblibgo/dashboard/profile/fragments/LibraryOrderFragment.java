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
import com.lib.liblibgo.adapter.LibraryOrderAdapter;
import com.lib.liblibgo.api.AllApiClass;
import com.lib.liblibgo.api.Holders;
import com.lib.liblibgo.common.Constants;
import com.lib.liblibgo.common.UserDatabase;
import com.lib.liblibgo.model.LibraryOrderModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LibraryOrderFragment extends Fragment {
    private UserDatabase database;
    private ProgressBar progBar;
    private RecyclerView rvMyOrder;
    private List<LibraryOrderModel.ResData.MyOrderList> myList = new ArrayList<>();
    private LibraryOrderAdapter adapter;
    private String isOwnLirary = "1";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_library_order, container, false);

        database = new UserDatabase(getContext());
        progBar = (ProgressBar)view.findViewById(R.id.progBar);
        rvMyOrder = view.findViewById(R.id.rvMyOrder);

        if (Constants.isOwnLibrary == true){
            isOwnLirary = "1";
        }else {
            isOwnLirary = "0";
        }

        getOrderList();

        return view;
    }

    private void getOrderList() {
        progBar.setVisibility(View.VISIBLE);
        Holders holders = AllApiClass.getInstance().getApi();
        Call<LibraryOrderModel> responseCall = holders.getLibraryOrderList(database.getUserId(),isOwnLirary);
        responseCall.enqueue(new Callback<LibraryOrderModel>() {
            @Override
            public void onResponse(Call<LibraryOrderModel> call, Response<LibraryOrderModel> response) {
                if (response.isSuccessful()){
                    if (response.body().getResponse().getCode().equals("1")){
                        progBar.setVisibility(View.GONE);
                        myList = response.body().getResponse().getOrder_details();
                        if (myList.size() > 0){
                            adapter = new LibraryOrderAdapter(myList, getContext());
                            LinearLayoutManager verticalManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                            // verticalManager.setReverseLayout(true);
                            // verticalManager.setStackFromEnd(true);
                            rvMyOrder.setLayoutManager(verticalManager);
                            rvMyOrder.setAdapter(adapter);
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
            public void onFailure(Call<LibraryOrderModel> call, Throwable t) {
                progBar.setVisibility(View.GONE);
                Toast.makeText(getContext(), "Something went wrong !", Toast.LENGTH_SHORT).show();
            }
        });
    }
}