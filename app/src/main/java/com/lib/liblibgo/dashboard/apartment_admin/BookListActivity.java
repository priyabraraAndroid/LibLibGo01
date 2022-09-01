package com.lib.liblibgo.dashboard.apartment_admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.JsonObject;
import com.lib.liblibgo.R;
import com.lib.liblibgo.adapter.subadmin.BookListAdapter;
import com.lib.liblibgo.api.AllApiClass;
import com.lib.liblibgo.api.Holders;
import com.lib.liblibgo.common.Constants;
import com.lib.liblibgo.common.FileUtil;
import com.lib.liblibgo.common.UserDatabase;
import com.lib.liblibgo.dashboard.book_collect.CollectApartmentBooksActivity;
import com.lib.liblibgo.model.SubmitDataResModel;
import com.lib.liblibgo.model.subadmin.BookDataModel;
import com.lib.liblibgo.views.MyEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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


public class BookListActivity extends AppCompatActivity {
    private RecyclerView rvBookOperation;
    //private ImageView ivToolbarBack;
    private TextView tvUpload;
    private ProgressBar progBar;
    private FloatingActionButton add_book_entry;
    UserDatabase preferences;
    private MyEditText et_search;
    //private String API_KEY= "AIzaSyB7nQgwK-XGbFkYIakgx1S2Qi881WV9KB8";
    private List<BookDataModel> mList = new ArrayList<>();
    private BookListAdapter adapter;

    String smallThumbnail = "";
    String thumbnail = "";
    private LinearLayout ll_file_view;
    DownloadManager manager;
    private Button uploadCsvFileBtn;
    private int EXCEL_IMPORTED = 1;
    private String isOwnLirary = "1";
    SharedPreferences sharedpreferences;
    private SharedPreferences.Editor editor;
    private String communityId = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_list);

        //ivToolbarBack = (ImageView) findViewById(R.id.backTool);
        tvUpload = (TextView) findViewById(R.id.tvUpload);
        setUpToolbar(getString(R.string.upload_bulk_books));

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();

        communityId = sharedpreferences.getString("community_id", "0");
        Log.d("myCommunityId","Community_id : "+communityId);
        rvBookOperation = (RecyclerView) findViewById(R.id.rv_book);
        progBar = (ProgressBar)findViewById(R.id.progBar);

        ll_file_view = (LinearLayout)findViewById(R.id.ll_file_view);
        preferences = new UserDatabase(BookListActivity.this);

        if (Constants.isOwnLibrary == true){
            isOwnLirary = "1";
        }else {
            isOwnLirary = "0";
        }

        et_search = (MyEditText)findViewById(R.id.et_search);
        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String newText = charSequence.toString();
                if (newText.equals("")){
                    ll_file_view.setVisibility(View.VISIBLE);
                    rvBookOperation.setVisibility(View.INVISIBLE);
                }else {
                    ll_file_view.setVisibility(View.INVISIBLE);
                    rvBookOperation.setVisibility(View.VISIBLE);
                    searchBookByIsbn(newText);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        et_search.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                ll_file_view.setVisibility(View.INVISIBLE);
                rvBookOperation.setVisibility(View.VISIBLE);
            }
        });

        //searchBookByIsbn("0199535566");

        uploadCsvFileBtn = (Button)findViewById(R.id.uploadCsvFileBtn);

        uploadCsvFileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                String[] mimeTypes = {"application/vnd.ms-excel" , "application/*.xlsx"};
//                Intent searchExcel = new Intent();
//                searchExcel.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true);
//                searchExcel.setAction(Intent.ACTION_GET_CONTENT);
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//                    searchExcel.setType(mimeTypes.length == 1 ? mimeTypes[0] : "*/*");
//                    if (mimeTypes.length > 0) {
//                        searchExcel.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
//                    }
//                } else {
//                    String mimeTypesStr = "";
//                    for (String mimeType : mimeTypes) {
//                        mimeTypesStr += mimeType + "|";
//                    }
//                    searchExcel.setType(mimeTypesStr.substring(0,mimeTypesStr.length() - 1));
//                }
//
//                startActivityForResult(Intent.createChooser(searchExcel,"Selecione Arquivo Excel"), EXCEL_IMPORTED);
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("application/*");
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult(intent, EXCEL_IMPORTED);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == EXCEL_IMPORTED) {
                Uri uri = data.getData();
                Log.d("MyPath", "uri path: "+uri.getPath());
                String path = FileUtil.getFileAbsolutePath(this, uri);
                Log.d("MyPath", "file path: "+path);
                readExcelData(path);
            }
        } else {
            Log.i("debinf cliinfo", "resultCol NOT OK");
        }
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
            RequestBody fileReqBody = RequestBody.create(MediaType.parse("application/xlsx"), file);
            part = MultipartBody.Part.createFormData("uploadFile", file.getName(), fileReqBody);
        }catch (Exception e){
            e.printStackTrace();
        }
        Log.d("myFilePath",""+part);
        String isOpen = "";
        RequestBody userIdReq = RequestBody.create(MediaType.parse("multipart/form-data"), preferences.getUserId());
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
                        CollectApartmentBooksActivity.flag = 1;
                        Intent intent = new Intent(BookListActivity.this,MyBooksActivity.class);
                        startActivity(intent);
                        finish();
                    }else {
                        progressBar.dismiss();
                        Toast.makeText(BookListActivity.this, ""+response.body().getResponse().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }else {
                    progressBar.dismiss();
                    Toast.makeText(BookListActivity.this, "All the fields are required.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<SubmitDataResModel> call, Throwable t) {
                progressBar.dismiss();
                Toast.makeText(BookListActivity.this, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void searchBookByIsbn(String isbn) {
        progBar.setVisibility(View.VISIBLE);
        Holders holders = AllApiClass.getInstance().getApiBooks();
        Call<JsonObject> call = holders.getBooks("isbn:"+isbn);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()){
                    mList.clear();
                    mList = new ArrayList<>();
                    progBar.setVisibility(View.GONE);
                    Log.d("myBookResponse",response.body().toString());
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().toString());
                        String kind = jsonObject.optString("kind");
                        //Toast.makeText(BookListActivity.this, ""+kind, Toast.LENGTH_SHORT).show();
                        JSONArray jsonArray = jsonObject.getJSONArray("items");
                        for (int i = 0; i<jsonArray.length();i++){
                            JSONObject object = jsonArray.getJSONObject(i);
                            JSONObject obj = object.getJSONObject("volumeInfo");
                            String name = obj.optString("title");
                            String author = obj.getJSONArray("authors").get(0).toString();
                            String publishDate = obj.optString("publishedDate");
                            String description = obj.optString("description");
                            JSONObject imgObj = obj.optJSONObject("imageLinks");

                            if (imgObj == null){
                                smallThumbnail = "";
                                thumbnail = "";
                            }else {
                                smallThumbnail = imgObj.optString("smallThumbnail");
                                thumbnail = imgObj.optString("thumbnail");
                            }

                            BookDataModel model = new BookDataModel();
                            model.setName(name);
                            model.setAuthor(author);
                            model.setPublish_date(publishDate);
                            model.setDescription(description);
                            model.setImgUrl(smallThumbnail);
                            model.setThumbnailUrl(thumbnail);
                            model.setIsbn_no(isbn);
                            mList.add(model);

                            Log.d("myAutomr",author);
                        }

                        if (mList.size() > 0){
                            adapter = new BookListAdapter(mList, BookListActivity.this);
                            LinearLayoutManager verticalManager = new LinearLayoutManager(BookListActivity.this, LinearLayoutManager.VERTICAL, false);
                            rvBookOperation.setLayoutManager(verticalManager);
                            rvBookOperation.setAdapter(adapter);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        //Toast.makeText(BookListActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }else {
                    progBar.setVisibility(View.GONE);
                    Toast.makeText(BookListActivity.this, "Something went wrong !", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                progBar.setVisibility(View.GONE);
                Toast.makeText(BookListActivity.this, "Server not responding yet !", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setUpToolbar(String param) {
        //tvToolbarTitle.setText(param);
        tvUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //onBackPressed();
                finish();
            }
        });
    }

    public void onAddBooksInStack(View view) {
        Intent intent = new Intent(BookListActivity.this,StackBookUpload.class);
        intent.putExtra("name","");
        intent.putExtra("description","");
        intent.putExtra("imgUrl","");
        intent.putExtra("book_price","");
        intent.putExtra("category_id","");
        intent.putExtra("category_name","");
        intent.putExtra("book_id","");
        intent.putExtra("book_condition","");
        intent.putExtra("giveaway_status","");
        startActivity(intent);
    }

    public void onCsvDownload(View view) {
        manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse("https://liblibgo.com/uploads/csv/SampleBooks.xlsx");
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "SampleBooks.xlsx");
        request.allowScanningByMediaScanner();
        long reference = manager.enqueue(request);
        Toast.makeText(this, "Downloading...", Toast.LENGTH_SHORT).show();
    }
}