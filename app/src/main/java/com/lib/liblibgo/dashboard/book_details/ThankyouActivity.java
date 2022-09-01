package com.lib.liblibgo.dashboard.book_details;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.lib.liblibgo.R;
import com.lib.liblibgo.activity.homedashboard.HomeActivity;

import cdflynn.android.library.checkview.CheckView;

public class ThankyouActivity extends AppCompatActivity {

    private CheckView mCheckView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thankyou);
        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
        mCheckView = (CheckView)findViewById(R.id.check);
        mCheckView.check();

        Thread thread = new Thread(){
            @Override
            public void run() {
                try {
                    sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }finally {
                    Intent intent = new Intent(ThankyouActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finishAffinity();
                }
            }
        };
        thread.start();
    }
}