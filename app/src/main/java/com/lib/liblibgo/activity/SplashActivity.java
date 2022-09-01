package com.lib.liblibgo.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.lib.liblibgo.R;
import com.lib.liblibgo.activity.homedashboard.HomeActivity;
import com.lib.liblibgo.common.UserDatabase;

public class SplashActivity extends AppCompatActivity {

    //private static final int SLEEP_TIMER = 2;
    private UserDatabase pref;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        pref = new UserDatabase(this);
        imageView = (ImageView)findViewById(R.id.imageView);
        Glide.with(this).asGif().load(R.drawable.splash).into(imageView);

        allowPermission();

    }

    private void allowPermission() {
        Thread thread = new Thread(){
            @Override
            public void run() {
                try {
                    sleep(2500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }finally {
                    Intent intentForgot=new Intent(SplashActivity.this, HomeActivity.class);
                    startActivity(intentForgot);
                    finish();
                    /*if (pref.getUserId().equals("") || pref.getUserId().equals("null")){
                        Intent intent = new Intent(SplashActivity.this, LoginWithPhoneNumber.class);
                        startActivity(intent);
                        finish();
                    }else {
                        Intent intentForgot=new Intent(SplashActivity.this,HomeActivity.class);
                        startActivity(intentForgot);
                        finish();
                    }*/
                }
            }
        };

        thread.start();
    }


}