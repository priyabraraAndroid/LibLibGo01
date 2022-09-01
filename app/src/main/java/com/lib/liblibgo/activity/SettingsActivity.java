package com.lib.liblibgo.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.switchmaterial.SwitchMaterial;
import com.lib.liblibgo.R;
import com.lib.liblibgo.api.AllApiClass;
import com.lib.liblibgo.api.Holders;
import com.lib.liblibgo.common.UserDatabase;
import com.lib.liblibgo.dashboard.profile.OrderDetailsActivityNew;
import com.lib.liblibgo.model.SettingModel;
import com.lib.liblibgo.model.SubmitDataResModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SettingsActivity extends AppCompatActivity {
    private TextView titleTool;
    private ImageView backTool;
    private SwitchMaterial switch_one;
    private SwitchMaterial switch_two;
    private SwitchMaterial switch_three;
    private String type = "";
    private UserDatabase database;
    private SwitchMaterial switch_five;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        titleTool = findViewById(R.id.titleTool);
        backTool = findViewById(R.id.backTool);
        setTopView(getString(R.string.settings));

        database = new UserDatabase(this);

        switch_one = (SwitchMaterial)findViewById(R.id.switch_one);
        switch_two = (SwitchMaterial)findViewById(R.id.switch_two);
        switch_three = (SwitchMaterial)findViewById(R.id.switch_three);
        switch_five = (SwitchMaterial)findViewById(R.id.switch_five);

        switch_one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                type = "add_book_notification";
                setSettingNotification(type);
            }
        });

        switch_two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                type = "add_book_community_notification";
                setSettingNotification(type);
            }
        });

        switch_three.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                type = "notify_notification";
                setSettingNotification(type);
            }
        });

        switch_five.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                type = "demanded_book_nofification";
                setSettingNotification(type);
            }
        });

        getSettings();

    }

    private void getSettings() {
        final ProgressDialog progressBar = new ProgressDialog(SettingsActivity.this);
        progressBar.setMessage("Please wait...");
        progressBar.setCancelable(false);
        progressBar.show();
        Holders holders = AllApiClass.getInstance().getApi();
        Call<SettingModel> resCall = holders.getSeetingsNotification(database.getUserId());
        resCall.enqueue(new Callback<SettingModel>() {
            @Override
            public void onResponse(Call<SettingModel> call, Response<SettingModel> response) {
                if (response.isSuccessful()){
                    if (response.body().getResponse().getCode().equals("1")){
                        progressBar.dismiss();
                        if (response.body().getResponse().getAdd_book_notification().equals("1")){
                            switch_one.setChecked(true);
                        }else {
                            switch_one.setChecked(false);
                        }

                        if (response.body().getResponse().getAdd_book_community_notification().equals("1")){
                            switch_two.setChecked(true);
                        }else {
                            switch_two.setChecked(false);
                        }

                        if (response.body().getResponse().getNotify_notification().equals("1")){
                            switch_three.setChecked(true);
                        }else {
                            switch_three.setChecked(false);
                        }

                        if (response.body().getResponse().getDemanded_book_nofification().equals("1")){
                            switch_five.setChecked(true);
                        }else {
                            switch_five.setChecked(false);
                        }

                    }else {
                        progressBar.dismiss();
                        Toast.makeText(SettingsActivity.this, ""+response.body().getResponse().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }else {
                    progressBar.dismiss();
                    Toast.makeText(SettingsActivity.this, "Something went wrong !", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<SettingModel> call, Throwable t) {
                progressBar.dismiss();
                Toast.makeText(SettingsActivity.this, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setSettingNotification(String type) {
        final ProgressDialog progressBar = new ProgressDialog(SettingsActivity.this);
        progressBar.setMessage("Please wait...");
        progressBar.setCancelable(false);
        progressBar.show();
        Holders holders = AllApiClass.getInstance().getApi();
        Call<SubmitDataResModel> resCall = holders.setSettings(database.getUserId(),type);
        resCall.enqueue(new Callback<SubmitDataResModel>() {
            @Override
            public void onResponse(Call<SubmitDataResModel> call, Response<SubmitDataResModel> response) {
                if (response.isSuccessful()){
                    if (response.body().getResponse().getCode() == 1){
                        progressBar.dismiss();
                        getSettings();
                    }else {
                        progressBar.dismiss();
                        Toast.makeText(SettingsActivity.this, ""+response.body().getResponse().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }else {
                    progressBar.dismiss();
                    Toast.makeText(SettingsActivity.this, "Something went wrong !", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<SubmitDataResModel> call, Throwable t) {
                progressBar.dismiss();
                Toast.makeText(SettingsActivity.this, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
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