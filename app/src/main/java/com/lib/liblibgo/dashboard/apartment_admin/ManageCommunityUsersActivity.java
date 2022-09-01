package com.lib.liblibgo.dashboard.apartment_admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.lib.liblibgo.R;
import com.lib.liblibgo.adapter.CommunityRequestAdapter;
import com.lib.liblibgo.adapter.LibraryAdapter;
import com.lib.liblibgo.api.AllApiClass;
import com.lib.liblibgo.api.Holders;
import com.lib.liblibgo.common.UserDatabase;
import com.lib.liblibgo.model.CommunityRequestModel;
import com.lib.liblibgo.model.SubmitDataResModel;
import com.lib.liblibgo.views.MyTextView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ManageCommunityUsersActivity extends AppCompatActivity {
    private MyTextView titleTool;
    private ImageView backTool;
    private RecyclerView rvRequest;
    private ProgressBar progBar;
    private UserDatabase database;
    private List<CommunityRequestModel.ResDataBooks.ReqList> myList = new ArrayList<>();
    private CommunityRequestAdapter adapter;
    private TextView tvRequest;
    private SwipeRefreshLayout pullToRefresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_community_users);

        titleTool = findViewById(R.id.titleTool);
        backTool = findViewById(R.id.backTool);
        setTopView(getString(R.string.mange_community_user));

        database = new UserDatabase(this);

        pullToRefresh = (SwipeRefreshLayout)findViewById(R.id.pullToRefresh);

        rvRequest = (RecyclerView)findViewById(R.id.rvRequest);
        progBar = (ProgressBar)findViewById(R.id.progBar);
        tvRequest = (TextView)findViewById(R.id.tvRequest);

        getCommunityRequestList();

        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getCommunityRequestList();
            }
        });

    }

    private void getCommunityRequestList() {
        progBar.setVisibility(View.VISIBLE);
        Holders holders = AllApiClass.getInstance().getApi();
        Call<CommunityRequestModel> resCall = holders.getCommunityRequest(database.getUserId());
        resCall.enqueue(new Callback<CommunityRequestModel>() {
            @Override
            public void onResponse(Call<CommunityRequestModel> call, Response<CommunityRequestModel> response) {
                if (response.isSuccessful()){
                    pullToRefresh.setRefreshing(false);
                    if (response.body().getResponse().getCode().equals("1")){
                        progBar.setVisibility(View.GONE);
                        myList = response.body().getResponse().getRequest_list();
                        tvRequest.setText("Number of pending requests ("+response.body().getResponse().getPending_request() +")");
                        if (myList.size() > 0){
                            adapter = new CommunityRequestAdapter(myList, ManageCommunityUsersActivity.this,tvRequest,response.body().getResponse().getPending_request());
                            LinearLayoutManager verticalManager = new LinearLayoutManager(ManageCommunityUsersActivity.this, LinearLayoutManager.VERTICAL, false);
                            // verticalManager.setReverseLayout(true);
                            // verticalManager.setStackFromEnd(true);
                            rvRequest.setLayoutManager(verticalManager);
                            rvRequest.setAdapter(adapter);
                        }
                    }else {
                        progBar.setVisibility(View.GONE);
                        Toast.makeText(ManageCommunityUsersActivity.this, ""+response.body().getResponse().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }else {
                    pullToRefresh.setRefreshing(false);
                    progBar.setVisibility(View.GONE);
                    Toast.makeText(ManageCommunityUsersActivity.this, "Something went wrong !", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CommunityRequestModel> call, Throwable t) {
                pullToRefresh.setRefreshing(false);
                progBar.setVisibility(View.GONE);
                Toast.makeText(ManageCommunityUsersActivity.this, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
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