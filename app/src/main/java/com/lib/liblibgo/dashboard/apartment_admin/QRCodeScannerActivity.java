package com.lib.liblibgo.dashboard.apartment_admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.zxing.Result;
import com.gpfreetech.awesomescanner.ui.GpCodeScanner;
import com.gpfreetech.awesomescanner.ui.ScannerView;
import com.gpfreetech.awesomescanner.util.DecodeCallback;
import com.lib.liblibgo.R;

public class QRCodeScannerActivity extends AppCompatActivity {
    private GpCodeScanner mCodeScanner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_q_r_code_scanner);

        ScannerView scannerView = findViewById(R.id.scanner_view);

        mCodeScanner = new GpCodeScanner(this, scannerView);
        //mCodeScanner = new GpCodeScanner(this, scannerView,GpCodeScanner.CAMERA_BACK);

        mCodeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull final Result result) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //Toast.makeText(getApplicationContext(), result.getText(), Toast.LENGTH_SHORT).show();
                        //txtScanText.setText("" + result.getText());
                        Intent intent = new Intent();
                        intent.putExtra("code", result.getText());
                        Log.d("scannedIsbn","isbn : "+result.getText());
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                });
            }
        });

        scannerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCodeScanner.startPreview();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(mCodeScanner!=null) {
            mCodeScanner.startPreview();
        }
    }

    @Override
    protected void onPause() {
        if(mCodeScanner!=null) {
            mCodeScanner.releaseResources();
        }
        super.onPause();
    }

    public void onBack(View view) {
        finish();
    }
}