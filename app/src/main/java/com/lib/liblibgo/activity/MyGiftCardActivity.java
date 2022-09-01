package com.lib.liblibgo.activity;

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
import com.lib.liblibgo.adapter.GiftCardAdapter;
import com.lib.liblibgo.api.AllApiClass;
import com.lib.liblibgo.api.Holders;
import com.lib.liblibgo.common.UserDatabase;
import com.lib.liblibgo.model.GiftCardModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyGiftCardActivity extends AppCompatActivity {
    private TextView titleTool;
    private ImageView backTool;
    private UserDatabase database;
    private RecyclerView rvCardList;
    private ProgressBar progBar;
    private List<GiftCardModel.ResModelData.RequestList> myList = new ArrayList<>();
    private GiftCardAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_gift_card);

        database = new UserDatabase(this);

        titleTool = findViewById(R.id.titleTool);
        backTool = findViewById(R.id.backTool);
        setTopView(getString(R.string.my_gift));

        rvCardList = (RecyclerView)findViewById(R.id.rvCardList);
        progBar = (ProgressBar)findViewById(R.id.progBar);

        getCuponCards();

    }

    private void getCuponCards() {
        progBar.setVisibility(View.VISIBLE);
        Holders holders = AllApiClass.getInstance().getApi();
        Call<GiftCardModel> call = holders.getGiftCards(database.getUserId());
        call.enqueue(new Callback<GiftCardModel>() {
            @Override
            public void onResponse(Call<GiftCardModel> call, Response<GiftCardModel> response) {
                if (response.isSuccessful()){
                    if (response.body().getResponse().getCode().equals("1")){
                        progBar.setVisibility(View.GONE);
                        myList = response.body().getResponse().getRequest_list();
                        if (myList.size() > 0){
                            adapter = new GiftCardAdapter(myList, MyGiftCardActivity.this);
                            LinearLayoutManager verticalManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
                            // verticalManager.setReverseLayout(true);
                            // verticalManager.setStackFromEnd(true);
                            rvCardList.setLayoutManager(verticalManager);
                            rvCardList.setAdapter(adapter);
                            //adapter.notifyDataSetChanged();
                        }else {
                            //myList.clear();
                            rvCardList.setVisibility(View.GONE);
                        }
                    }else {
                        progBar.setVisibility(View.GONE);
                        Toast.makeText(MyGiftCardActivity.this, ""+response.body().getResponse().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }else {
                    progBar.setVisibility(View.GONE);
                    Toast.makeText(MyGiftCardActivity.this, "Something went wrong !", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<GiftCardModel> call, Throwable t) {
                progBar.setVisibility(View.GONE);
                Toast.makeText(MyGiftCardActivity.this, ""+t, Toast.LENGTH_SHORT).show();
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
}