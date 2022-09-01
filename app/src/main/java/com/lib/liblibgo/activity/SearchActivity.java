package com.lib.liblibgo.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.JsonObject;
import com.lib.liblibgo.R;
import com.lib.liblibgo.adapter.GoogleBookAdapter;
import com.lib.liblibgo.adapter.NearMeBookAdapter;
import com.lib.liblibgo.adapter.NearMeBookSearchAdapter;
import com.lib.liblibgo.adapter.subadmin.BookListAdapter;
import com.lib.liblibgo.api.AllApiClass;
import com.lib.liblibgo.api.Holders;
import com.lib.liblibgo.common.Constants;
import com.lib.liblibgo.common.UserDatabase;
import com.lib.liblibgo.dashboard.apartment_admin.BookListActivity;
import com.lib.liblibgo.dashboard.apartment_admin.UploadBookDetails;
import com.lib.liblibgo.model.NearMeBookModel;
import com.lib.liblibgo.model.SubmitDataResModel;
import com.lib.liblibgo.model.subadmin.BookDataModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchActivity extends AppCompatActivity {

    private TextView titleTool;
    private ImageView backTool;
    private EditText searchBook;
    private Switch switchTxt;
    private RecyclerView list;
    private ProgressBar progBar;
    //private List<SearchResModel.ResDataBooks.BookListData> myList = new ArrayList<>();
    //private SearchAdapter adapter;
    private List<NearMeBookModel.ResModelData.NewrmeBookList> bookList = new ArrayList<>();
    private NearMeBookAdapter bookAdapter;
    private String isOwnLibrary = "0";
    private String userId = "";
    private UserDatabase database;

    private List<NearMeBookModel.ResModelData.NewrmeBookList> fakeSearchBookList = new ArrayList<>();
    //private ImageView ivBook;
    //private TextView tvBookName;
    //private TextView tvBookAuthor;
   // private TextView tvNotify;
    private String smallThumbnail = "";
    private String thumbnail = "";
    private RelativeLayout llGoogleResult;
    private String googleIsbn = "";
    private RecyclerView rlGoogleResRv;
    private ProgressBar progGoogle;

    private List<BookDataModel> mList = new ArrayList<>();
    private GoogleBookAdapter adapter;
    private String author = "N/A";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        titleTool = findViewById(R.id.titleTool);
        backTool = findViewById(R.id.backTool);
        switchTxt = findViewById(R.id.switchTxt);
        setTopView(getString(R.string.search_books));

        searchBook = findViewById(R.id.et_search);
        list = findViewById(R.id.rl_books);
        progBar = (ProgressBar)findViewById(R.id.progBar);

        //ivBook = (ImageView)findViewById(R.id.ivBook);
        //tvBookName = (TextView)findViewById(R.id.tvBookName);
        //tvBookAuthor = (TextView)findViewById(R.id.tvBookAuthor);
        //tvNotify = (TextView)findViewById(R.id.tvNotify);
        llGoogleResult = (RelativeLayout)findViewById(R.id.llGoogleResult);
        rlGoogleResRv = (RecyclerView)findViewById(R.id.rlGoogleResRv);
        progGoogle = (ProgressBar)findViewById(R.id.progGoogle);

        database = new UserDatabase(this);

        searchBook.setHint("Search Book By Name / Author / ISBN");
        Log.d("searchFrom",Constants.searchFrom);

        searchBook.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(searchBook, InputMethodManager.SHOW_IMPLICIT);

        if (Constants.searchFrom.equals("individual")){
            titleTool.setText("Search Books");
            isOwnLibrary = "1";
            userId = "";
            fakeSearchBookList = Constants.fakeSearchBookList;
            switchTxt.setChecked(false);
        }else {
            titleTool.setText("Search Books");
            userId = database.getUserId();
            isOwnLibrary = "0";
            switchTxt.setChecked(true);
            fakeSearchBookList.clear();
            fakeSearchBookList = new ArrayList<>();
            getFirstTimeBooks();
        }

        switchTxt.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                Constants.fakeSearchBookList.clear();
                if (isChecked){
                    titleTool.setText("Search Books");
                    userId = database.getUserId();
                    isOwnLibrary = "0";
                    if (!searchBook.getText().toString().trim().equals("")){
                        getBooksByCategory(searchBook.getText().toString().trim());
                    }
                    fakeSearchBookList.clear();
                    fakeSearchBookList = new ArrayList<>();
                    getFirstTimeBooks();
                    //list.setVisibility(View.GONE);
                }else {
                    titleTool.setText("Search Books");
                    isOwnLibrary = "1";
                    userId = "";
                    if (!searchBook.getText().toString().trim().equals("")){
                        getBooksByCategory(searchBook.getText().toString().trim());
                    }
                    //getFirstTimeBooks();
                    fakeSearchBookList = Constants.fakeSearchBookList;
                }
            }
        });

        searchBook.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String txt = charSequence.toString();
                if (txt.equals("")){
                    list.setVisibility(View.GONE);
                }else {
                    getBooksByCategory(txt);
                    list.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

    private void getFirstTimeBooks() {
        //Constants.fakeSearchBookList
        if (fakeSearchBookList.size() > 0){
            bookAdapter = new NearMeBookAdapter(fakeSearchBookList, SearchActivity.this);
            GridLayoutManager manager = new GridLayoutManager(SearchActivity.this,3);
            list.setLayoutManager(manager);
            list.setAdapter(bookAdapter);
            bookAdapter.notifyDataSetChanged();
        }
    }

    private void getBooksByCategory(String catIds) {
        progBar.setVisibility(View.VISIBLE);
        list.setVisibility(View.VISIBLE);
        //location = new UserCurrentLocation(this);
        Holders holders = AllApiClass.getInstance().getApi();
        Call<NearMeBookModel> resCall = holders.getBookSearchResult(catIds,isOwnLibrary,userId);
        resCall.enqueue(new Callback<NearMeBookModel>() {
            @Override
            public void onResponse(Call<NearMeBookModel> call, Response<NearMeBookModel> response) {
                if (response.isSuccessful()){
                    if (response.body().getResponse().getCode().equals("1")){
                        //list.setVisibility(View.VISIBLE);
                        bookList.clear();
                        bookList = new ArrayList<>();
                        progBar.setVisibility(View.GONE);
                        llGoogleResult.setVisibility(View.GONE);
                        progGoogle.setVisibility(View.GONE);
                        list.setVisibility(View.VISIBLE);
                        bookList = response.body().getResponse().getBook_list();
                        if (bookList.size() > 0){
                            bookAdapter = new NearMeBookAdapter(bookList, SearchActivity.this);
                            GridLayoutManager manager = new GridLayoutManager(SearchActivity.this,3);
                            //verticalManager.setReverseLayout(true);
                            //verticalManager.setStackFromEnd(true);
                            list.setLayoutManager(manager);
                            list.setAdapter(bookAdapter);
                            list.setVisibility(View.VISIBLE);
                            bookAdapter.notifyDataSetChanged();
                            llGoogleResult.setVisibility(View.GONE);
                            progGoogle.setVisibility(View.GONE);
                        }else {
                            list.setVisibility(View.GONE);
                        }
                    }else {
                        getBookFromGoogleApi(catIds);
                        progBar.setVisibility(View.GONE);
                        list.setVisibility(View.GONE);
                    }
                }else {
                    llGoogleResult.setVisibility(View.GONE);
                    progGoogle.setVisibility(View.GONE);
                    Toast.makeText(SearchActivity.this, "Something went wrong !", Toast.LENGTH_SHORT).show();
                    progBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<NearMeBookModel> call, Throwable t) {
                llGoogleResult.setVisibility(View.GONE);
                progGoogle.setVisibility(View.GONE);
                Toast.makeText(SearchActivity.this, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
                progBar.setVisibility(View.GONE);
            }
        });
    }

    private void getBookFromGoogleApi(String isbn) {
            Holders holders = AllApiClass.getInstance().getApiBooks();
            Call<JsonObject> call = holders.getBooksByName(isbn);
            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    if (response.isSuccessful()){
                        mList.clear();
                        mList = new ArrayList<>();
                        //progBar.setVisibility(View.GONE);
                        Log.d("myBookResponse",response.body().toString());
                        try {
                            llGoogleResult.setVisibility(View.VISIBLE);
                            progGoogle.setVisibility(View.GONE);
                            JSONObject jsonObject = new JSONObject(response.body().toString());
                            String kind = jsonObject.optString("kind");
                            JSONArray jsonArray = jsonObject.getJSONArray("items");
                            for (int i = 0; i<jsonArray.length();i++){
                                JSONObject object = jsonArray.getJSONObject(i);
                                JSONObject obj = object.getJSONObject("volumeInfo");
                                String name = obj.optString("title");

                                try {
                                    if (obj.optJSONArray("authors") != null){
                                        author = obj.optJSONArray("authors").get(0).toString();
                                    }else {
                                        author = "N/A";
                                    }
                                }catch (Exception e){
                                    e.printStackTrace();
                                    author = "N/A";
                                }

                                JSONObject imgObj = obj.optJSONObject("imageLinks");

                                JSONArray isbnArray = obj.getJSONArray("industryIdentifiers");
                                if (isbnArray.length() > 0){
                                    for (int j=0;j<isbnArray.length();j++){
                                        JSONObject isbnObj = isbnArray.getJSONObject(j);
                                        if (isbnObj.getString("type").equals("ISBN_13")){
                                            googleIsbn = isbnObj.getString("identifier");
                                        }else {
                                            googleIsbn = isbnObj.getString("identifier");
                                        }
                                    }
                                }

                                Log.d("mySearchedIsbn",googleIsbn);

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
                                model.setPublish_date("");
                                model.setDescription("");
                                model.setImgUrl(smallThumbnail);
                                model.setThumbnailUrl(thumbnail);
                                model.setIsbn_no(googleIsbn);
                                mList.add(model);

                                //tvBookName.setText(name);
                                //tvBookAuthor.setText(author);



                                /*if (thumbnail.equals("") || thumbnail.equals("null")){
                                    smallThumbnail = "";
                                    thumbnail = "";
                                    Glide.with(SearchActivity.this).load("https://sales.arecontvision.com/images/products/img_placeholder_50618_xl.jpg").into(ivBook);
                                }else {
                                    Glide.with(SearchActivity.this).load(thumbnail).placeholder(R.drawable.no_img).into(ivBook);
                                }*/

                                /**/

                            }

                            if (mList.size() > 0){
                                adapter = new GoogleBookAdapter(mList, SearchActivity.this);
                                GridLayoutManager manager = new GridLayoutManager(SearchActivity.this,3);
                                rlGoogleResRv.setLayoutManager(manager);
                                rlGoogleResRv.setAdapter(adapter);
                                adapter.notifyDataSetChanged();
                            }


                        } catch (JSONException e) {
                            llGoogleResult.setVisibility(View.VISIBLE);
                            progGoogle.setVisibility(View.GONE);
                            e.printStackTrace();
                            //Toast.makeText(BookListActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }else {
                        llGoogleResult.setVisibility(View.GONE);
                        progGoogle.setVisibility(View.GONE);
                        //progBar.setVisibility(View.GONE);
                        //Toast.makeText(SearchActivity.this, "Something went wrong !", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    llGoogleResult.setVisibility(View.GONE);
                    progGoogle.setVisibility(View.GONE);
                    // progBar.setVisibility(View.GONE);
                    //Toast.makeText(SearchActivity.this, "Server not responding yet !", Toast.LENGTH_SHORT).show();
                }
            });
    }

    /*private boolean getSearchBookList(String txt) {
        if (bookList.size() > 0) {
            final List<NearMeBookModel.ResModelData.NewrmeBookList> filteredModelList = filter(bookList, txt);
            bookAdapter.setFilter(filteredModelList);
        }
        return true;
    }*/

    /*private List<NearMeBookModel.ResModelData.NewrmeBookList> filter(List<NearMeBookModel.ResModelData.NewrmeBookList> models, String search_txt) {

        search_txt = search_txt.toLowerCase();
        final List<NearMeBookModel.ResModelData.NewrmeBookList> filteredModelList = new ArrayList<>();

        for (NearMeBookModel.ResModelData.NewrmeBookList model : models) {
            final String textName = model.getBook_name().toLowerCase();
            final String textNo = model.getAuthor_name().toLowerCase();
            final String textISB = model.getAuthor_name().toLowerCase();
            if (textName.contains(search_txt) || textNo.contains(search_txt) || textISB.contains(search_txt)) {
                filteredModelList.add(model);
            }
        }
        return filteredModelList;
    }*/

    /*private void getSearchList(String isbn) {
        progBar.setVisibility(View.VISIBLE);
        Holders holders = AllApiClass.getInstance().getApi();
        Call<SearchResModel> responseCall = holders.searchResult(isbn);
        responseCall.enqueue(new Callback<SearchResModel>() {
            @Override
            public void onResponse(Call<SearchResModel> call, Response<SearchResModel> response) {
                if (response.isSuccessful()){
                    if (response.body().getResponse().getCode() == 1){
                        myList.clear();
                        myList = new ArrayList<>();
                        progBar.setVisibility(View.GONE);
                        myList = response.body().getResponse().getBook_list();
                        if (myList.size() > 0){
                            list.setVisibility(View.VISIBLE);
                            adapter = new SearchAdapter(myList, SearchActivity.this);
                            LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
                            llm.setOrientation(LinearLayoutManager.VERTICAL);
                            list.setLayoutManager(llm);
                            list.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                        }

                    }else {
                        myList.clear();
                        list.setVisibility(View.GONE);
                        progBar.setVisibility(View.GONE);
                    }
                }else {
                    progBar.setVisibility(View.GONE);
                    Toast.makeText(SearchActivity.this, "aaa"+response.errorBody().toString(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<SearchResModel> call, Throwable t) {
                progBar.setVisibility(View.GONE);
                Toast.makeText(SearchActivity.this, "Check your internet.", Toast.LENGTH_SHORT).show();
            }
        });
    }*/

    private void setTopView(String string) {
        backTool.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                finish();
            }
        });
    }
}