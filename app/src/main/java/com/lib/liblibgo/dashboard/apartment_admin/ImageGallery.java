package com.lib.liblibgo.dashboard.apartment_admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lib.liblibgo.R;
import com.lib.liblibgo.adapter.ImageGalleryAdapter;
import com.lib.liblibgo.model.ImageModel;

import java.util.ArrayList;
import java.util.Collections;

public class ImageGallery extends AppCompatActivity {

    private ImageView backTool;
    private TextView titleTool;
    private RecyclerView mRvImage;

    ArrayList<ImageModel> imageList = new ArrayList<>();
    Uri collection;
    private ImageGalleryAdapter adapter;
    private String mAppend = "file:/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_books);

        titleTool = findViewById(R.id.titleTool);
        backTool = findViewById(R.id.backTool);
        setUpToolbar(getString(R.string.select_image));

        mRvImage = (RecyclerView)findViewById(R.id.rvImage);

        getAllImagesFromGallery();

    }

    private void getAllImagesFromGallery() {
        imageList = new ArrayList<>();
        imageList.clear();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            collection = MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL);
        } else {
            collection = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        }
        String[] projection = {MediaStore.MediaColumns.DATA};
        Cursor cursor = getContentResolver().query(collection, projection, null,null, null);
        while (cursor.moveToNext()) {
            String absolutePathOfImage = cursor.getString(cursor.getColumnIndex(MediaStore.MediaColumns.DATA));
            ImageModel ImageModel = new ImageModel();
            ImageModel.setImage(absolutePathOfImage);
            imageList.add(ImageModel);
        }
        cursor.close();
        Collections.reverse(imageList);
        adapter = new ImageGalleryAdapter(imageList,mAppend,this);
        GridLayoutManager manager = new GridLayoutManager(this,3);
        mRvImage.setLayoutManager(manager);
        mRvImage.setAdapter(adapter);
    }

    private void setUpToolbar(String param) {
        titleTool.setText(param);
        backTool.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                finish();
            }
        });
    }

}