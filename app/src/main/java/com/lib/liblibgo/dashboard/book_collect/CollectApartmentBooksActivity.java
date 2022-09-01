package com.lib.liblibgo.dashboard.book_collect;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.JsonObject;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.lib.liblibgo.R;
import com.lib.liblibgo.SearchAparmentAdapter;
import com.lib.liblibgo.activity.LoginWithPhoneNumber;
import com.lib.liblibgo.activity.SearchActivity;
import com.lib.liblibgo.adapter.GoogleBookAdapter;
import com.lib.liblibgo.adapter.GoogleBookAdapterTwo;
import com.lib.liblibgo.adapter.SearchAdapter;
import com.lib.liblibgo.api.AllApiClass;
import com.lib.liblibgo.api.Holders;
import com.lib.liblibgo.common.Constants;
import com.lib.liblibgo.common.UserDatabase;
import com.lib.liblibgo.dashboard.apartment_admin.UploadBookDetails;
import com.lib.liblibgo.dashboard.book_upload.UploadBookActivity;
import com.lib.liblibgo.dialogs.CategoriFilterDialog;
import com.lib.liblibgo.listner.CommunityFilterInterfaceClass;
import com.lib.liblibgo.listner.FilterInterfaceClass;
import com.lib.liblibgo.model.CategoryListData;
import com.lib.liblibgo.model.CategoryModel;
import com.lib.liblibgo.model.CommunityStatusModel;
import com.lib.liblibgo.model.SearchResModel;
import com.lib.liblibgo.model.subadmin.BookDataModel;
import com.lib.liblibgo.views.MyTextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.lib.liblibgo.dashboard.library.fragments.CommunityLibraryFragment.MyPREFERENCES;

public class CollectApartmentBooksActivity extends AppCompatActivity implements FilterInterfaceClass{
    private static final String TAG = "CollectBookActivity";
    private MyTextView titleTool;
    private ImageView backTool;
    private RecyclerView rvCollect;
    private EditText searchBook;
    private UserDatabase database;
    private ProgressBar progBar;
    List<SearchResModel.ResDataBooks.BookListData> myList = new ArrayList<>();
    SearchAparmentAdapter adapter;
    public static int flag = 0;
    private String libId;
    private String isLibrary;
    private FloatingActionButton fabAddLibrary;
    SharedPreferences sharedpreferences;
    private SharedPreferences.Editor editor;
    private String communityId = "0";
    private String libUserId = "";
    private String libType = "";
    private String libUserName = "";
    private String catIds = "";
    private String typeValue = "";
    private String giveAwayValue = "";
    private String userId = "";
    private RelativeLayout rlCategory;
    private RelativeLayout rlLocation;
    private RelativeLayout rlType;
    private TextView tvRange;
    private TextView tvType;
    private ImageView icClearFilter;
    private FilterInterfaceClass callback;
    private CommunityFilterInterfaceClass callback2;
    public static final String MyPREFERENCES = "MyPrefMyBooksComunity";
    private List<CategoryListData> myCatList = new ArrayList<>();
    private PopupWindow popupWindow;
    private RelativeLayout llGoogleResult;
    private RecyclerView rlGoogleResRv;
    private ProgressBar progGoogle;

    private List<BookDataModel> mList = new ArrayList<>();
    private GoogleBookAdapterTwo adapterTwo;
    private String author = "N/A";

    private String smallThumbnail = "";
    private String thumbnail = "";
    private String googleIsbn = "";
    private SwipeRefreshLayout pullToRefresh;
    private int startLimit = 10;
    private int endLimit = 0;
    private boolean mLoadTing = true;
    int pastVisiblesItems, visibleItemCount, totalItemCount;
    private LinearLayoutManager verticalManager;
    List<SearchResModel.ResDataBooks.BookListData> modelList = new ArrayList<>();

    boolean noDataFlag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collect_apartment_books);

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();

        //communityId = sharedpreferences.getString("community_id","0");

        rvCollect = findViewById(R.id.rvCollect);
        titleTool = findViewById(R.id.titleTool);
        backTool = findViewById(R.id.backTool);
        searchBook = findViewById(R.id.et_search);
        libUserName = getIntent().getStringExtra("library_name");
        setTopView(getString(R.string.info_book_collect));
        database = new UserDatabase(CollectApartmentBooksActivity.this);
        progBar = (ProgressBar)findViewById(R.id.progBar);

        libId = getIntent().getStringExtra("lib_id");
        isLibrary = getIntent().getStringExtra("isLibrary");
        communityId = getIntent().getStringExtra("community_id");
        libUserId = getIntent().getStringExtra("library_user_id");


        libType = Constants.libraryType;
        //Toast.makeText(this, ""+libId, Toast.LENGTH_SHORT).show();

        fabAddLibrary = (FloatingActionButton)findViewById(R.id.fabAddLibrary);
        pullToRefresh = (SwipeRefreshLayout)findViewById(R.id.pullToRefresh);

        if (Constants.libraryType.equals("physical")){
            if (libUserId.equals(database.getUserId())){
                fabAddLibrary.setVisibility(View.VISIBLE);
            }else {
                fabAddLibrary.setVisibility(View.GONE);
            }
        }else {
            fabAddLibrary.setVisibility(View.VISIBLE);
        }

        fabAddLibrary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!database.getUserId().equals("")){
                    Dexter.withContext(CollectApartmentBooksActivity.this).withPermissions(Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA)
                            .withListener(new MultiplePermissionsListener() {
                                @Override
                                public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                                    editor.putString("community_id",communityId);
                                    editor.commit();
                                    Intent intent = new Intent(CollectApartmentBooksActivity.this, UploadBookActivity.class);
                                    Constants.isOwnLibrary = false;
                                    Constants.libraryType = libType;
                                    intent.putExtra("name","");
                                    intent.putExtra("author","");
                                    intent.putExtra("isbn","");
                                    intent.putExtra("publish_date","");
                                    intent.putExtra("description","");
                                    intent.putExtra("imgUrl","");
                                    intent.putExtra("rental_price","");
                                    intent.putExtra("rental_duration","365");
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
                                    if (Constants.libraryType.equals("physical")) {
                                        intent.putExtra("giveaway_status", "yes");
                                    }else {
                                        intent.putExtra("giveaway_status", "no");
                                    }
                                    startActivity(intent);
                                }

                                @Override
                                public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                                    permissionToken.continuePermissionRequest();
                                }
                            }).check();
                }else {
                    Intent intent = new Intent(CollectApartmentBooksActivity.this, LoginWithPhoneNumber.class);
                    startActivity(intent);
                }
            }
        });

        searchBook.setHint("Search book by name / author / isbn");
        searchBook.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String newText = charSequence.toString();
                if (!newText.equals("")){
                    searchBookList(newText);
                }else {
                    searchBookList("");
                    llGoogleResult.setVisibility(View.GONE);
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        sharedpreferences = getSharedPreferences(CollectApartmentBooksActivity.MyPREFERENCES, Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();
        callback=this;

        rlCategory = (RelativeLayout)findViewById(R.id.rlCategory);
        rlLocation = (RelativeLayout)findViewById(R.id.rlLocation);
        rlType = (RelativeLayout)findViewById(R.id.rlType);
        tvRange = (TextView)findViewById(R.id.tvRange);
        tvType = (TextView)findViewById(R.id.tvType);
        icClearFilter = (ImageView)findViewById(R.id.icClearFilter);

        llGoogleResult = (RelativeLayout)findViewById(R.id.llGoogleResult);
        rlGoogleResRv = (RecyclerView)findViewById(R.id.rlGoogleResRv);
        progGoogle = (ProgressBar)findViewById(R.id.progGoogle);

        //verticalManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        //rvCollect.setLayoutManager(verticalManager);
        //endLimit = endLimit + 1;
        getCollectBook(libId,isLibrary,catIds,giveAwayValue,typeValue,userId);

        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                noDataFlag = false;
                getCollectBook(libId,isLibrary,catIds,giveAwayValue,typeValue,userId);
            }
        });

        rlCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Constants.isOwnBooks = "1";
                CategoriFilterDialog dialog = new CategoriFilterDialog();
                dialog.newInstance(callback,callback2);
                dialog.show(getSupportFragmentManager(),
                        "cat_dialog_fragment");
                llGoogleResult.setVisibility(View.GONE);
            }
        });

        getCategoryList();

        rlLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showLocationRangeMenu(view);
            }
        });

        rlType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTypePopup(view);
            }
        });

        icClearFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getCategoryList();
                //getNearByData();
                //endLimit = 10;
                catIds = "";
                typeValue = "";
                giveAwayValue = "";
                editor.clear();
                editor.commit();
                Constants.selectedCatList.clear();
                Constants.selectedCategories = "";
                Constants.selectedNearByCity = "";
                Constants.selectedNearByArea = "";
                Constants.selectedNearByApartment = "";
                Constants.selectedNearByDistance = "";

                Constants.selectedFilterValue = "";
                Constants.giveAway = "";
                Constants.userId = "";
                tvRange.setText("Books4U");
                tvType.setText("Buy Or Rent");
                getCollectBook(libId,isLibrary,catIds,giveAwayValue,typeValue,userId);
                llGoogleResult.setVisibility(View.GONE);
            }
        });

        /*rvCollect.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) //check for scroll down
                {
                    visibleItemCount = verticalManager.getChildCount();
                    totalItemCount = verticalManager.getItemCount();
                    pastVisiblesItems = verticalManager.findFirstVisibleItemPosition();
                    if (mLoadTing) {
                        if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                            mLoadTing = false;
                            endLimit = endLimit + 1;
                            startLimit = startLimit + 10;
                            Log.e(TAG, "Last Item Wow !");
                            getCollectBook(libId,isLibrary,catIds,giveAwayValue,typeValue,userId);
                            //verticalManager.scrollToPositionWithOffset(startLimit, 0);
                            //verticalManager.setStackFromEnd(true);
                            //rvCollect.smoothScrollToPosition(startLimit);
                        }
                    }
                }
            }
        });*/

    }

    private void showTypePopup(View view) {
        LayoutInflater layoutInflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View popupView = layoutInflater.inflate(R.layout.layout_my_book_filter_type, null);

        String selectedItem = sharedpreferences.getString("select_type", "");

        popupWindow = new PopupWindow(
                popupView,
                300,
                ViewGroup.LayoutParams.WRAP_CONTENT);

        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setOutsideTouchable(true);

        final TextView tvForRent = popupView.findViewById(R.id.tvForRent);
        final TextView tvForSale = popupView.findViewById(R.id.tvForSale);
        final TextView tvForBoth = popupView.findViewById(R.id.tvForBoth);
        final TextView tvForGiveaway = popupView.findViewById(R.id.tvForGiveaway);
        final TextView tvAllTpe = popupView.findViewById(R.id.tvAllTpe);

        if (selectedItem.equals("1")){
            tvAllTpe.setBackgroundColor(Color.parseColor("#345EA8"));
            tvAllTpe.setTextColor(Color.parseColor("#ffffff"));
        }else if (selectedItem.equals("2")){
            tvForRent.setBackgroundColor(Color.parseColor("#345EA8"));
            tvForRent.setTextColor(Color.parseColor("#ffffff"));
        }else if (selectedItem.equals("3")){
            tvForSale.setBackgroundColor(Color.parseColor("#345EA8"));
            tvForSale.setTextColor(Color.parseColor("#ffffff"));
        }else if (selectedItem.equals("4")){
            tvForBoth.setBackgroundColor(Color.parseColor("#345EA8"));
            tvForBoth.setTextColor(Color.parseColor("#ffffff"));
        }else if (selectedItem.equals("5")){
            tvForGiveaway.setBackgroundColor(Color.parseColor("#345EA8"));
            tvForGiveaway.setTextColor(Color.parseColor("#ffffff"));
        }else {
            tvAllTpe.setBackgroundColor(Color.parseColor("#345EA8"));
            tvAllTpe.setTextColor(Color.parseColor("#ffffff"));
        }

        tvAllTpe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //location = new UserCurrentLocation(mContext);
                popupWindow.dismiss();
                editor.putString("select_type","1");
                editor.commit();
                tvType.setText("Buy Or Rent");
                Constants.selectedFilterValue = "";
                Constants.giveAway = "";
                catIds = Constants.selectedCategories;
                typeValue = Constants.selectedFilterValue;
                giveAwayValue = Constants.giveAway;
                userId = Constants.userId;
                getCollectBook(libId,isLibrary,catIds,giveAwayValue,typeValue,userId);
                llGoogleResult.setVisibility(View.GONE);
            }
        });

        tvForRent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //location = new UserCurrentLocation(mContext);
                popupWindow.dismiss();
                editor.putString("select_type","2");
                editor.commit();
                tvType.setText("For Rent");
                Constants.selectedFilterValue = "For Rent";
                Constants.giveAway = "";
                catIds = Constants.selectedCategories;
                typeValue = Constants.selectedFilterValue;
                giveAwayValue = Constants.giveAway;
                userId = Constants.userId;
                getCollectBook(libId,isLibrary,catIds,giveAwayValue,typeValue,userId);
                llGoogleResult.setVisibility(View.GONE);
            }
        });

        tvForSale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //location = new UserCurrentLocation(mContext);
                popupWindow.dismiss();
                editor.putString("select_type","3");
                editor.commit();
                tvType.setText("For Buy");
                Constants.selectedFilterValue = "For Sale";
                Constants.giveAway = "";
                catIds = Constants.selectedCategories;
                typeValue = Constants.selectedFilterValue;
                giveAwayValue = Constants.giveAway;
                userId = Constants.userId;
                getCollectBook(libId,isLibrary,catIds,giveAwayValue,typeValue,userId);
                llGoogleResult.setVisibility(View.GONE);
            }
        });

        tvForBoth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //location = new UserCurrentLocation(mContext);
                popupWindow.dismiss();
                editor.putString("select_type","4");
                editor.commit();
                tvType.setText("Buy & Rent");
                Constants.selectedFilterValue = "Both";
                Constants.giveAway = "";
                catIds = Constants.selectedCategories;
                typeValue = Constants.selectedFilterValue;
                giveAwayValue = Constants.giveAway;
                userId = Constants.userId;
                getCollectBook(libId,isLibrary,catIds,giveAwayValue,typeValue,userId);
                llGoogleResult.setVisibility(View.GONE);
            }
        });

        tvForGiveaway.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //location = new UserCurrentLocation(mContext);
                popupWindow.dismiss();
                editor.putString("select_type","5");
                editor.commit();
                tvType.setText("Giveaway");
                Constants.selectedFilterValue = "";
                Constants.giveAway = "yes";
                catIds = Constants.selectedCategories;
                typeValue = Constants.selectedFilterValue;
                giveAwayValue = Constants.giveAway;
                userId = Constants.userId;
                getCollectBook(libId,isLibrary,catIds,giveAwayValue,typeValue,userId);
                llGoogleResult.setVisibility(View.GONE);
            }
        });

        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                //TODO do sth here on dismiss
                popupWindow.dismiss();
            }
        });

        popupWindow.showAsDropDown(view);
    }

    private void showLocationRangeMenu(View view) {
        LayoutInflater layoutInflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View popupView = layoutInflater.inflate(R.layout.layout_my_book_filter, null);

        String selectedItem = sharedpreferences.getString("select", "");

        popupWindow = new PopupWindow(
                popupView,
                300,
                ViewGroup.LayoutParams.WRAP_CONTENT);

        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setOutsideTouchable(true);

        final TextView tvAllBooks = popupView.findViewById(R.id.tvAllBooks);
        final TextView tvmyBooks = popupView.findViewById(R.id.tvmyBooks);
        //tvmyBooks.setVisibility(View.GONE);

        if (selectedItem.equals("1")){
            tvAllBooks.setBackgroundColor(Color.parseColor("#345EA8"));
            tvAllBooks.setTextColor(Color.parseColor("#ffffff"));
        }else if (selectedItem.equals("2")){
            tvmyBooks.setBackgroundColor(Color.parseColor("#345EA8"));
            tvmyBooks.setTextColor(Color.parseColor("#ffffff"));
        }else {
            tvAllBooks.setBackgroundColor(Color.parseColor("#345EA8"));
            tvAllBooks.setTextColor(Color.parseColor("#ffffff"));
        }

        tvAllBooks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
                editor.putString("select","1");
                editor.commit();
                tvRange.setText("Books4U");
                catIds = Constants.selectedCategories;
                typeValue = Constants.selectedFilterValue;
                giveAwayValue = Constants.giveAway;
                Constants.userId = "";
                userId = Constants.userId;
                getCollectBook(libId,isLibrary,catIds,giveAwayValue,typeValue,userId);
                llGoogleResult.setVisibility(View.GONE);
            }
        });

        tvmyBooks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
                editor.putString("select","2");
                editor.commit();
                tvRange.setText("My Books");
                catIds = Constants.selectedCategories;
                typeValue = Constants.selectedFilterValue;
                giveAwayValue = Constants.giveAway;
                Constants.userId = database.getUserId();
                userId = Constants.userId;
                getCollectBook(libId,isLibrary,catIds,giveAwayValue,typeValue,userId);
                llGoogleResult.setVisibility(View.GONE);
            }
        });

        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                //TODO do sth here on dismiss
                popupWindow.dismiss();
            }
        });

        popupWindow.showAsDropDown(view);
    }

    private void getCategoryList() {
        Holders holders = AllApiClass.getInstance().getApi();
        Call<CategoryModel> resCall = holders.categoryList();
        resCall.enqueue(new Callback<CategoryModel>() {
            @Override
            public void onResponse(Call<CategoryModel> call, Response<CategoryModel> response) {
                if (response.isSuccessful()){
                    if (response.body().getResponse().getCode().equals("1")){
                        myCatList = response.body().getResponse().getApartment_list();
                        Constants.catList = myCatList;
                    }
                }
            }
            @Override
            public void onFailure(Call<CategoryModel> call, Throwable t) {

            }
        });
    }

    private boolean searchBookList(String txt){
        if (myList.size() > 0) {
            final List<SearchResModel.ResDataBooks.BookListData> filteredModelList = filter(myList, txt);
            adapter.setFilter(filteredModelList);
        }
        return true;
    }

    private void getBooksFromGoogleApi(String txt) {
        Holders holders = AllApiClass.getInstance().getApiBooks();
        Call<JsonObject> call = holders.getBooksByName(txt);
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

                        }

                        if (mList.size() > 0){
                            adapterTwo = new GoogleBookAdapterTwo(mList, CollectApartmentBooksActivity.this);
                            LinearLayoutManager manager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
                            rlGoogleResRv.setLayoutManager(manager);
                            rlGoogleResRv.setAdapter(adapterTwo);
                            adapterTwo.notifyDataSetChanged();
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

    private List<SearchResModel.ResDataBooks.BookListData> filter(List<SearchResModel.ResDataBooks.BookListData> models, String search_txt) {
        search_txt = search_txt.toLowerCase();
        final List<SearchResModel.ResDataBooks.BookListData> filteredModelList = new ArrayList<>();

        for (SearchResModel.ResDataBooks.BookListData model : models) {
            final String textName = model.getBook_name().toLowerCase();
            final String textNo = model.getAuthor_name().toLowerCase();
            final String textISB = model.getAuthor_name().toLowerCase();
            if (textName.contains(search_txt) || textNo.contains(search_txt) || textISB.contains(search_txt)) {
                filteredModelList.add(model);
            }
        }

        if (filteredModelList.size() > 0){
            llGoogleResult.setVisibility(View.GONE);
        }else {
            getBooksFromGoogleApi(search_txt);
        }
        return filteredModelList;
    }

    private void checkRequest() {
        final ProgressDialog progressBar = new ProgressDialog(this);
        progressBar.setMessage("Please wait...");
        progressBar.setCancelable(false);
        progressBar.show();
        Holders holders = AllApiClass.getInstance().getApi();
        Call<CommunityStatusModel> resCall = holders.checkCommunityRequestStatus(libId,database.getUserId());
        resCall.enqueue(new Callback<CommunityStatusModel>() {
            @Override
            public void onResponse(Call<CommunityStatusModel> call, Response<CommunityStatusModel> response) {
                if (response.isSuccessful()){
                    if (response.body().getResponse().getCode() == 1){
                        progressBar.dismiss();
                        if (response.body().getResponse().getStatus().equals("1")){
                            editor.putString("community_id",response.body().getResponse().getCommunity_id());
                            editor.commit();
                            Intent intent = new Intent(CollectApartmentBooksActivity.this, UploadBookDetails.class);
                            Constants.isOwnLibrary = false;
                            intent.putExtra("name","");
                            intent.putExtra("author","");
                            intent.putExtra("isbn","");
                            intent.putExtra("publish_date","");
                            intent.putExtra("description","");
                            intent.putExtra("imgUrl","");
                            intent.putExtra("rental_price","");
                            intent.putExtra("rental_duration","365");
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
                            intent.putExtra("giveaway_status","yes");
                            startActivity(intent);
                        }else if (response.body().getResponse().getStatus().equals("2")){
                            Toast.makeText(CollectApartmentBooksActivity.this, "Your request is rejected by the community.", Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(CollectApartmentBooksActivity.this, "Your request is pending yet.", Toast.LENGTH_SHORT).show();
                        }
                        //Toast.makeText(getContext(), "", Toast.LENGTH_SHORT).show();
                    }else {
                        progressBar.dismiss();
                        /*Intent intent = new Intent(CollectApartmentBooksActivity.this, ManageLibraryActivity.class);
                        startActivity(intent);*/
                        Toast.makeText(CollectApartmentBooksActivity.this, "You are not a part of this community.", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    progressBar.dismiss();
                    Toast.makeText(CollectApartmentBooksActivity.this, "Something went wrong !", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CommunityStatusModel> call, Throwable t) {
                progressBar.dismiss();
                Toast.makeText(CollectApartmentBooksActivity.this, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setTopView(String string) {
        titleTool.setText(string+" of "+libUserName);
        backTool.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                finish();
            }
        });
    }

    private void getCollectBook(String id, String isLibrary, String catIds, String giveAwayValue, String typeValue,String userId) {
        //Toast.makeText(this, ""+isLibrary, Toast.LENGTH_SHORT).show();
        progBar.setVisibility(View.VISIBLE);
        Holders holders = AllApiClass.getInstance().getApi();
        Call<SearchResModel> responseCall = holders.getBooksByLibrary(id, catIds, giveAwayValue, typeValue,userId,database.getUserId(), "");
        /*if (isLibrary.equals("false")){
            responseCall = holders.getBooksByCategory(id);
        }else {
            responseCall = holders.getBooksByLibrary(id,"","","");
        }*/
        Log.d("mySelectedValues","values : "
                +"\n"+"Categories :"+catIds
                +"\n"+"type : "+typeValue
                +"\n"+"giveaway : "+giveAwayValue
                +"\n"+"libId : "+libId
                +"\n"+"libName: "+libUserName
        );
        responseCall.enqueue(new Callback<SearchResModel>() {
            @Override
            public void onResponse(Call<SearchResModel> call, Response<SearchResModel> response) {
                if (response.isSuccessful()){
                    pullToRefresh.setRefreshing(false);
                    if (response.body().getResponse().getCode() == 1){
                        mLoadTing = true;
                        progBar.setVisibility(View.GONE);
                        rvCollect.setVisibility(View.VISIBLE);
                        myList = response.body().getResponse().getBook_list();
                        //modelList = response.body().getResponse().getBook_list();
                        //myList.addAll(modelList);
                        Log.e("myList",""+myList.size());
                        Log.e("myList",""+modelList.size());
                        /*if (+modelList.size() < 10){
                            noDataFlag = true;
                        }else {
                            noDataFlag = false;
                        }*/
                        if (myList.size() > 0){
                            adapter = new SearchAparmentAdapter(myList, CollectApartmentBooksActivity.this,communityId);
                            verticalManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
                            rvCollect.setLayoutManager(verticalManager);
                            rvCollect.setAdapter(adapter);
                            //adapter.notifyItemInserted(myList.size());
                        }else {
                            adapter.notifyDataSetChanged();
                        }
                    }else {
                        mLoadTing = false;
                        progBar.setVisibility(View.GONE);
                        if (noDataFlag == false){
                            progBar.setVisibility(View.GONE);
                            rvCollect.setVisibility(View.GONE);
                        }
                        Toast.makeText(CollectApartmentBooksActivity.this, ""+response.body().getResponse().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }else {
                    mLoadTing = true;
                    pullToRefresh.setRefreshing(false);
                    progBar.setVisibility(View.GONE);
                    rvCollect.setVisibility(View.VISIBLE);
                    Toast.makeText(CollectApartmentBooksActivity.this, "Something went wrong !", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<SearchResModel> call, Throwable t) {
                mLoadTing = true;
                pullToRefresh.setRefreshing(false);
                progBar.setVisibility(View.GONE);
                rvCollect.setVisibility(View.VISIBLE);
                Toast.makeText(CollectApartmentBooksActivity.this, ""+t, Toast.LENGTH_SHORT).show();
            }
        });
    }

   /* @Override
    protected void onResume() {
        if (flag == 1){
            getCollectBook(libId,isLibrary);
            flag = 0;
        }
        super.onResume();
    }*/
   @Override
   protected void onResume() {
       super.onResume();
       if (flag == 1){
           getCollectBook(libId,isLibrary, catIds, giveAwayValue, typeValue,userId);
           flag = 0;
       }
   }

    public void callbackMethod(String catId,String city,String area,String apartment,String distance,String type,String giveaway) {
        catIds = catId;
        //typeValue = type;
        //giveAwayValue = giveaway;
        //endLimit = endLimit + 10;
        getCollectBook(libId,isLibrary,catIds,giveAwayValue,typeValue,userId);
    }

    @Override
    public void onDestroy() {
        Constants.selectedCatList.clear();
        Constants.selectedCategories = "";
        Constants.selectedFilterValue = "";
        Constants.giveAway = "";
        Constants.userId = "";
        editor.clear();
        editor.commit();

        super.onDestroy();
    }
}