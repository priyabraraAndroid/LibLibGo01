package com.lib.liblibgo.dashboard.apartment_admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.lib.liblibgo.R;
import com.lib.liblibgo.adapter.subadmin.OwnBookAdapter;
import com.lib.liblibgo.api.AllApiClass;
import com.lib.liblibgo.api.Holders;
import com.lib.liblibgo.common.Constants;
import com.lib.liblibgo.common.FileUtil;
import com.lib.liblibgo.common.UserDatabase;
import com.lib.liblibgo.dashboard.book_collect.CollectApartmentBooksActivity;
import com.lib.liblibgo.model.SubmitDataResModel;
import com.lib.liblibgo.model.subadmin.MyOwnBookModel;
import com.lib.liblibgo.views.MyTextView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.lib.liblibgo.dashboard.library.fragments.CommunityLibraryFragment.MyPREFERENCES;

public class MyBooksActivity extends AppCompatActivity {
    private MyTextView titleTool;
    private ImageView backTool;
    private ProgressBar progressBar;
    private RecyclerView rvBooks;
    private UserDatabase dataBase;
    private List<MyOwnBookModel.ResData.BookList> mBookList = new ArrayList<>();
    private OwnBookAdapter adapter;
    private int EXCEL_IMPORTED = 1;
    public static int flag = 0;
    private String isOwnLirary = "1";
    SharedPreferences sharedpreferences;
    private SharedPreferences.Editor editor;
    private String communityId = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_books);

        dataBase = new UserDatabase(this);

        titleTool = findViewById(R.id.titleTool);
        backTool = findViewById(R.id.backTool);
        setTopView(getString(R.string.my_books));

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();

        communityId = sharedpreferences.getString("community_id", "0");

        progressBar = (ProgressBar)findViewById(R.id.progBar);
        rvBooks = (RecyclerView)findViewById(R.id.rvBooks);

        if (Constants.isOwnLibrary == true){
            isOwnLirary = "1";
        }else {
            isOwnLirary = "0";
        }

        showAllBooks();
    }

    private void showAllBooks() {
        Log.d("myId",dataBase.getUserId());
        progressBar.setVisibility(View.VISIBLE);
        Holders holders = AllApiClass.getInstance().getApi();
        Call<MyOwnBookModel> resCall = holders.getAllOwnBooks(dataBase.getUserId(),isOwnLirary);
        resCall.enqueue(new Callback<MyOwnBookModel>() {
            @Override
            public void onResponse(Call<MyOwnBookModel> call, Response<MyOwnBookModel> response) {
                if (response.isSuccessful()){
                    if (response.body().getResponse().getCode().equals("1")){
                        progressBar.setVisibility(View.GONE);
                        mBookList = response.body().getResponse().getBook_list();
                        if (mBookList.size() > 0){
                            adapter = new OwnBookAdapter(mBookList, MyBooksActivity.this);
                            LinearLayoutManager verticalManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
                            // verticalManager.setReverseLayout(true);
                            // verticalManager.setStackFromEnd(true);
                            rvBooks.setLayoutManager(verticalManager);
                            rvBooks.setAdapter(adapter);
                        }
                    }else {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(MyBooksActivity.this, ""+response.body().getResponse().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }else {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(MyBooksActivity.this, "Something went wrong !", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<MyOwnBookModel> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(MyBooksActivity.this, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
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

    public void onAddBooks(View view) {
        SharedPreferences sharedpreferences = getSharedPreferences(CollectApartmentBooksActivity.MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("community_id","0");
        editor.commit();
        Intent intent = new Intent(MyBooksActivity.this, UploadBookDetails.class);
        intent.putExtra("name","");
        intent.putExtra("author","");
        intent.putExtra("isbn","");
        intent.putExtra("publish_date","");
        intent.putExtra("description","");
        intent.putExtra("imgUrl","");
        intent.putExtra("rental_price","");
        intent.putExtra("rental_duration","");
        intent.putExtra("book_price","");
        intent.putExtra("category_id","");
        intent.putExtra("category_name","");
        intent.putExtra("shelf_id","");
        intent.putExtra("shelf_name","");
        intent.putExtra("book_id","");
        intent.putExtra("mrp","");
        intent.putExtra("quantity","");
        intent.putExtra("sale_type","");
        intent.putExtra("book_condition","");
        intent.putExtra("security_money","");
        intent.putExtra("giveaway_status","no");
        startActivity(intent);
    }

    public void uploadFile(View view) {
        /*Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("application/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, EXCEL_IMPORTED);*/
        Intent intent = new Intent(MyBooksActivity.this,BookListActivity.class);
        startActivity(intent);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
       /* if (resultCode == RESULT_OK) {
            if (requestCode == EXCEL_IMPORTED) {
                Uri uri = data.getData();
                Log.d("MyPath", "uri path: "+uri.getPath());
                String path = FileUtil.getFileAbsolutePath(this, uri);
                Log.d("MyPath", "file path: "+path);
                readExcelData(path);
            }
        } else {
            Log.i("debinf cliinfo", "resultCol NOT OK");
        }*/
    }

    private void readExcelData(String path) {
        Log.d("myExelFile",path);
        final ProgressDialog progressBar = new ProgressDialog(this);
        progressBar.setMessage("Please wait...");
        progressBar.setCancelable(false);
        progressBar.show();
        Holders holders = AllApiClass.getInstance().getApi();

        MultipartBody.Part part = null;
        try {
            File file = new File(path);
            RequestBody fileReqBody = RequestBody.create(MediaType.parse("application/*"), file);
            part = MultipartBody.Part.createFormData("uploadFile", file.getName(), fileReqBody);
        }catch (Exception e){
            e.printStackTrace();
        }
        Log.d("myFilePath",""+part);
        String isOpen = "";
        RequestBody userIdReq = RequestBody.create(MediaType.parse("multipart/form-data"), dataBase.getUserId());
        RequestBody isOwnLiraryReq = RequestBody.create(MediaType.parse("multipart/form-data"), isOwnLirary);
        RequestBody communityIdReq = RequestBody.create(MediaType.parse("multipart/form-data"), communityId);
        RequestBody isOpenIdReq = RequestBody.create(MediaType.parse("multipart/form-data"), isOpen);
        // database.getUserId(),et_library_name.getText().toString().trim(),mEtAddress.getText().toString().trim(), Constants.latitude,Constants.longitude,isShelfPickup

        Call<SubmitDataResModel> resCall = holders.uploadBookByCsvFile(userIdReq,part,isOwnLiraryReq,communityIdReq,isOpenIdReq);
        resCall.enqueue(new Callback<SubmitDataResModel>() {
            @Override
            public void onResponse(Call<SubmitDataResModel> call, Response<SubmitDataResModel> response) {
                if (response.isSuccessful()){
                    if (response.body().getResponse().getCode() == 1){
                        progressBar.dismiss();
                        Intent intent = new Intent(MyBooksActivity.this,MyBooksActivity.class);
                        startActivity(intent);
                        finish();
                    }else {
                        progressBar.dismiss();
                        Toast.makeText(MyBooksActivity.this, ""+response.body().getResponse().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }else {
                    progressBar.dismiss();
                    Toast.makeText(MyBooksActivity.this, "Something went wrong !", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<SubmitDataResModel> call, Throwable t) {
                progressBar.dismiss();
                Toast.makeText(MyBooksActivity.this, "Check your internet !", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onResume() {
        if (flag == 1){
            showAllBooks();
            flag = 0;
        }
        super.onResume();
    }
}