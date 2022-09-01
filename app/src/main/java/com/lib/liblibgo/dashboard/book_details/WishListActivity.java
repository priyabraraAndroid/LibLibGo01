package com.lib.liblibgo.dashboard.book_details;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.lib.liblibgo.R;
import com.lib.liblibgo.adapter.WishListAdapter;
import com.lib.liblibgo.api.AllApiClass;
import com.lib.liblibgo.api.Holders;
import com.lib.liblibgo.common.UserDatabase;
import com.lib.liblibgo.model.NotifyListModel;
import com.lib.liblibgo.model.WishListModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WishListActivity extends AppCompatActivity {
    private TextView titleTool;
    private ImageView backTool;
    private RecyclerView rvWishList;
    private ProgressBar progBar;
    private UserDatabase database;
    private List<NotifyListModel.ResDataBooks.WishListData> myList = new ArrayList<>();
    private WishListAdapter adapter;
    public static int flag = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wish_list);

        database = new UserDatabase(this);

        titleTool = findViewById(R.id.titleTool);
        backTool = findViewById(R.id.backTool);
        setTopView(getString(R.string.my_wishlist));

        rvWishList = (RecyclerView)findViewById(R.id.rvWishList);
        progBar = (ProgressBar)findViewById(R.id.progBar);

        getWishList();

    }

    private void getWishList() {
        progBar.setVisibility(View.VISIBLE);
        Holders holders = AllApiClass.getInstance().getApi();
        Call<NotifyListModel> call = holders.getWishList(database.getUserId());
        call.enqueue(new Callback<NotifyListModel>() {
            @Override
            public void onResponse(Call<NotifyListModel> call, Response<NotifyListModel> response) {
                if (response.isSuccessful()){
                    if (response.body().getResponse().getCode().equals("1")){
                        progBar.setVisibility(View.GONE);
                        myList = response.body().getResponse().getWishlist_data();
                        if (myList.size() > 0){
                            adapter = new WishListAdapter(myList, WishListActivity.this);
                            LinearLayoutManager verticalManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
                            // verticalManager.setReverseLayout(true);
                            // verticalManager.setStackFromEnd(true);
                            rvWishList.setLayoutManager(verticalManager);
                            rvWishList.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                        }else {
                            //myList.clear();
                            rvWishList.setVisibility(View.GONE);
                        }
                    }else {
                        progBar.setVisibility(View.GONE);
                        Toast.makeText(WishListActivity.this, ""+response.body().getResponse().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }else {
                    progBar.setVisibility(View.GONE);
                    Toast.makeText(WishListActivity.this, "Something went wrong !", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<NotifyListModel> call, Throwable t) {
                progBar.setVisibility(View.GONE);
                Toast.makeText(WishListActivity.this, ""+t, Toast.LENGTH_SHORT).show();
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

    @Override
    protected void onResume() {
        if (flag == 1){
            getWishList();
            flag = 0;
        }
        super.onResume();
    }
}