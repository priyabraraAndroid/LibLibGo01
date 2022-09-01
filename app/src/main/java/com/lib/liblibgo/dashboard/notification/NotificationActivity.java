package com.lib.liblibgo.dashboard.notification;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lib.liblibgo.R;
import com.lib.liblibgo.adapter.NotificationAdapter;
import com.lib.liblibgo.api.AllApiClass;
import com.lib.liblibgo.api.Holders;
import com.lib.liblibgo.common.UserDatabase;
import com.lib.liblibgo.dashboard.profile.OrderDetailsActivityNew;
import com.lib.liblibgo.model.NotificationModel;
import com.lib.liblibgo.model.SubmitDataResModel;
import com.lib.liblibgo.views.MyTextView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.lib.liblibgo.notification.MyFirebaseMessagingService.badge;

public class NotificationActivity extends AppCompatActivity {

    private static final String TAG = "NotificationActivity";
    private MyTextView titleTool;
    private ImageView backTool;
    private RecyclerView rvNotification;
    private UserDatabase NoteDatabase;
    private EditText searchNotification;
    private ProgressBar progBar;
    UserDatabase database;
    List<NotificationModel.ResData.NotificationList> myList = new ArrayList<>();
    NotificationAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        rvNotification = findViewById(R.id.rvCollect);
        titleTool = findViewById(R.id.titleTool);
        backTool = findViewById(R.id.backTool);
        searchNotification = findViewById(R.id.et_search);
        setTopView(getString(R.string.info_notification));

        progBar = (ProgressBar)findViewById(R.id.progBar);

        database = new UserDatabase(NotificationActivity.this);

        getNotification();

        searchNotification.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String newText = charSequence.toString();
                searchNoti(newText);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

    private boolean searchNoti(String txt){
        //adapter = new NotificationAdapter(myList, NotificationActivity.this);
        if (myList.size() > 0){
            final List<NotificationModel.ResData.NotificationList> filteredModelList = filter(myList, txt);
            adapter.setFilter(filteredModelList);
        }
        return true;
    }

    private List<NotificationModel.ResData.NotificationList> filter(List<NotificationModel.ResData.NotificationList> models, String search_txt) {

        search_txt = search_txt.toLowerCase();
        final List<NotificationModel.ResData.NotificationList> filteredModelList = new ArrayList<>();

        for (NotificationModel.ResData.NotificationList model : models) {
            final String textName = model.getTitle().toLowerCase();
            if (textName.contains(search_txt)) {
                filteredModelList.add(model);
            }
        }
        return filteredModelList;
    }

    private void getNotification() {
        progBar.setVisibility(View.VISIBLE);
        Holders holders = AllApiClass.getInstance().getApi();
        Call<NotificationModel> responseCall = holders.getNotiList(database.getUserId());
        responseCall.enqueue(new Callback<NotificationModel>() {
            @Override
            public void onResponse(Call<NotificationModel> call, Response<NotificationModel> response) {
                if (response.isSuccessful()){
                    if (response.body().getResponse().getCode().equals("1")){
                        progBar.setVisibility(View.GONE);
                        myList = response.body().getResponse().getNotifications();
                        if (myList.size() > 0){
                            adapter = new NotificationAdapter(myList, NotificationActivity.this);
                            LinearLayoutManager verticalManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
                            // verticalManager.setReverseLayout(true);
                            // verticalManager.setStackFromEnd(true);
                            rvNotification.setLayoutManager(verticalManager);
                            rvNotification.setAdapter(adapter);
                        }
                    }else {
                        progBar.setVisibility(View.GONE);
                        //Toast.makeText(NotificationActivity.this, ""+response.body().getResponse().getMessage(), Toast.LENGTH_SHORT).show();
                        Toast.makeText(NotificationActivity.this, "Notification not available.", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    progBar.setVisibility(View.GONE);
                    Toast.makeText(NotificationActivity.this, "Something went wrong !", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<NotificationModel> call, Throwable t) {
                progBar.setVisibility(View.GONE);
                Toast.makeText(NotificationActivity.this, ""+t, Toast.LENGTH_SHORT).show();
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
    public void onBackPressed() {
        redAllNotification();
        super.onBackPressed();
    }

    private void redAllNotification() {
        Holders holders = AllApiClass.getInstance().getApi();
        Call<SubmitDataResModel> resCall = holders.readAllNotifications(database.getUserId());
        resCall.enqueue(new Callback<SubmitDataResModel>() {
            @Override
            public void onResponse(Call<SubmitDataResModel> call, Response<SubmitDataResModel> response) {
                if (response.isSuccessful()){
                    if (response.body().getResponse().getCode() == 1){
                        //badge = "0";
                        Log.d("msgNotification","success : true");
                    }else {
                        Log.d("msgNotification","success : false");
                    }
                }else {
                    Log.d("msgNotification","failed");
                }
            }

            @Override
            public void onFailure(Call<SubmitDataResModel> call, Throwable t) {
                Log.d("msgNotification","network issue");
            }
        });
    }
}