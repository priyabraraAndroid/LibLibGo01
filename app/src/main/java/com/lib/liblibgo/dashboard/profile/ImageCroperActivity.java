package com.lib.liblibgo.dashboard.profile;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import com.lib.liblibgo.R;
import com.lib.liblibgo.common.Constants;
import com.lib.liblibgo.common.FileHelper;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;

public class ImageCroperActivity extends AppCompatActivity {

    private CropImageView cropImageView;
    private Uri fileUri;
    private ProgressDialog progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_croper);

        progressBar = new ProgressDialog(this);
        progressBar.setMessage("Image cropping please wait...");
        progressBar.setCancelable(false);

        cropImageView = (CropImageView)findViewById(R.id.cropImageView);
        if (Constants.openPageFrom.equals("fromLibraryProfile")){
            fileUri = Uri.fromFile(new File(Constants.SelectedLibraryProfileImage));
        }else if (Constants.openPageFrom.equals("fromEditLibrary")){
            fileUri = Uri.fromFile(new File(Constants.SelectedLibraryEditImage));
        }else if (Constants.openPageFrom.equals("fromBookUpload")){
            fileUri = Uri.fromFile(new File(Constants.SelectedBookImage));
        }

        cropImageView.setImageUriAsync(fileUri);
        cropImageView.setAspectRatio(1, 1);
        cropImageView.setFixedAspectRatio(false);

        Log.e("myFilePathCroping",""+fileUri);
    }

    public void onBack(View view) {
        Constants.SelectedLibraryProfileImage = "";
        Constants.SelectedLibraryEditImage="";
        Constants.SelectedBookImage = "";
        //Constants.cropedImage = null;
        finish();
    }

    @Override
    public void onBackPressed() {
        Constants.SelectedLibraryProfileImage = "";
        Constants.SelectedLibraryEditImage="";
        Constants.SelectedBookImage = "";
        super.onBackPressed();
    }

    public void onDone(View view) {
        progressBar.show();
        if (fileUri != null) {
            Constants.cropedImage = cropImageView.getCroppedImage();
            finish();
            Handler handler = new Handler();  //Optional. Define as a variable in your activity.
            Runnable r = new Runnable()
            {
                @Override
                public void run()
                {
                    // your code here
                    handler.post(new Runnable()  //If you want to update the UI, queue the code on the UI thread
                    {
                        public void run()
                        {
                            File mfileSignature = new File(getCacheDir(), FileHelper.getUniqueFileName());
                            try {
                                boolean newFile = mfileSignature.createNewFile();
                                if (newFile) {
                                    //Convert bitmap to byte array
                                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                                    Constants.cropedImage.compress(Bitmap.CompressFormat.JPEG, 25, bos);
                                    byte[] bitmapdata = bos.toByteArray();
                                    //write the bytes in file
                                    FileOutputStream fos = null;
                                    try {
                                        fos = new FileOutputStream(mfileSignature);
                                        fos.write(bitmapdata);
                                        fos.flush();
                                        fos.close();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            if (Constants.openPageFrom.equals("fromLibraryProfile")){
                                Constants.SelectedLibraryProfileImage = mfileSignature.getAbsolutePath();
                            }else if (Constants.openPageFrom.equals("fromEditLibrary")){
                                Constants.SelectedLibraryEditImage = mfileSignature.getAbsolutePath();
                            }else if (Constants.openPageFrom.equals("fromBookUpload")){
                                Constants.SelectedBookImage = mfileSignature.getAbsolutePath();
                            }
                            Constants.cropedImage = null;
                        }
                    });
                }
            };

            Thread t = new Thread(r);
            t.start();
        }
    }

    @Override
    protected void onDestroy() {
        progressBar.dismiss();
        super.onDestroy();
    }
}