package com.lib.liblibgo.dashboard.book_collect;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lib.liblibgo.R;
import com.lib.liblibgo.activity.LoginWithPhoneNumber;
import com.lib.liblibgo.adapter.SearchAdapter;
import com.lib.liblibgo.common.Constants;
import com.lib.liblibgo.dashboard.book_details.BookCatActivity;
import com.lib.liblibgo.dashboard.library.LibraryActivity;
import com.lib.liblibgo.api.AllApiClass;
import com.lib.liblibgo.api.Holders;
import com.lib.liblibgo.common.UserDatabase;
import com.lib.liblibgo.dialogs.CategoriFilterDialog;
import com.lib.liblibgo.listner.CommunityFilterInterfaceClass;
import com.lib.liblibgo.listner.FilterInterfaceClass;
import com.lib.liblibgo.model.BookModel;
import com.lib.liblibgo.model.CategoryListData;
import com.lib.liblibgo.model.CategoryModel;
import com.lib.liblibgo.model.SearchResModel;
import com.lib.liblibgo.views.MyTextView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CollectBookActivity extends AppCompatActivity implements FilterInterfaceClass{

    private static final String TAG = "CollectBookActivity";
    private MyTextView titleTool;
    private ImageView backTool;
    private RecyclerView rvCollect;
    private List<BookModel> bList;
    private List<BookModel> mList;
    private EditText searchBook;
    private UserDatabase database;
    private ProgressBar progBar;
    List<SearchResModel.ResDataBooks.BookListData> myList = new ArrayList<>();
    SearchAdapter adapter;
    public static int flag = 0;
    private String libId;
    //private String catId;
    private String isLibrary;
    private LinearLayout llAllBooks;
    private LinearLayout llAllLibrary;
    private String libraryName = "";
    private String catIds = "";
    private String typeValue = "";
    private String giveAwayValue = "";
    private RelativeLayout rlCategory;
    private RelativeLayout rlLocation;
    private RelativeLayout rlType;
    private TextView tvRange;
    private TextView tvType;
    private ImageView icClearFilter;
    private FilterInterfaceClass callback;
    private CommunityFilterInterfaceClass callback2;
    private SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefMyBooks";
    private SharedPreferences.Editor editor;
    private List<CategoryListData> myCatList = new ArrayList<>();
    private PopupWindow popupWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collect_book);

        rvCollect = findViewById(R.id.rvCollect);
        titleTool = findViewById(R.id.titleTool);
        backTool = findViewById(R.id.backTool);
        searchBook = findViewById(R.id.et_search);
        libraryName = getIntent().getStringExtra("library_name");
        setTopView(getString(R.string.info_book_collect));
        database = new UserDatabase(CollectBookActivity.this);
        progBar = (ProgressBar)findViewById(R.id.progBar);

        sharedpreferences = getSharedPreferences(CollectBookActivity.MyPREFERENCES, Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();
        callback=this;


        llAllBooks = (LinearLayout)findViewById(R.id.llAllBooks);
        llAllLibrary = (LinearLayout)findViewById(R.id.llAllLibrary);

        libId = getIntent().getStringExtra("lib_id");
        isLibrary = getIntent().getStringExtra("isLibrary");

        //catId = getIntent().getStringExtra("cat_id");
        //isLibrary = getIntent().getStringExtra("isLibrary");

        rlCategory = (RelativeLayout)findViewById(R.id.rlCategory);
        rlLocation = (RelativeLayout)findViewById(R.id.rlLocation);
        rlType = (RelativeLayout)findViewById(R.id.rlType);
        tvRange = (TextView)findViewById(R.id.tvRange);
        tvType = (TextView)findViewById(R.id.tvType);
        icClearFilter = (ImageView)findViewById(R.id.icClearFilter);

        getCollectBook(libId,isLibrary,catIds,giveAwayValue,typeValue);

        searchBook.setHint("Search book by name / author / isbn");
        searchBook.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String newText = charSequence.toString();
                searchBookList(newText);
            }

            @Override
            public void afterTextChanged(Editable editable) {

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
                tvRange.setText("All Books");
                tvType.setText("Buy Or Rent");
                getCollectBook(libId,isLibrary,catIds,giveAwayValue,typeValue);
            }
        });

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
                getCollectBook(libId,isLibrary,catIds,giveAwayValue,typeValue);
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
                getCollectBook(libId,isLibrary,catIds,giveAwayValue,typeValue);
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
                getCollectBook(libId,isLibrary,catIds,giveAwayValue,typeValue);
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
                getCollectBook(libId,isLibrary,catIds,giveAwayValue,typeValue);
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
                getCollectBook(libId,isLibrary,catIds,giveAwayValue,typeValue);
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
        tvmyBooks.setVisibility(View.GONE);

        if (selectedItem.equals("1")){
            tvAllBooks.setBackgroundColor(Color.parseColor("#345EA8"));
            tvAllBooks.setTextColor(Color.parseColor("#ffffff"));
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
                tvRange.setText("All Books");
                catIds = Constants.selectedCategoriesCommunity;
                typeValue = Constants.selectedFilterValueCommunity;
                giveAwayValue = Constants.giveAwayCommunity;
                getCollectBook(libId,isLibrary,catIds,giveAwayValue,typeValue);
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

    public void onGoLibraryList(View view) {
        Intent intent = new Intent(CollectBookActivity.this, LibraryActivity.class);
        startActivity(intent);
    }

    public void onGoBookCategory(View view) {
        Intent intent = new Intent(CollectBookActivity.this, BookCatActivity.class);
        startActivity(intent);
    }

    private boolean searchBookList(String txt){
        if (myList.size() > 0) {
            final List<SearchResModel.ResDataBooks.BookListData> filteredModelList = filter(myList, txt);
            adapter.setFilter(filteredModelList);
        }
        return true;
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
        return filteredModelList;
    }

    private void setTopView(String string) {
        titleTool.setText(string+" of "+libraryName);
        backTool.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                finish();
            }
        });
    }

    private void getCollectBook(String id, String isLibrary, String catIds, String giveAwayValue, String typeValue) {
        //Toast.makeText(this, ""+isLibrary, Toast.LENGTH_SHORT).show();
        progBar.setVisibility(View.VISIBLE);
        Holders holders = AllApiClass.getInstance().getApi();
        Call<SearchResModel> responseCall = holders.getBooksByLibrary(id, catIds, giveAwayValue, typeValue,"","","");
        /*if (isLibrary.equals("false")){
            responseCall = holders.getBooksByCategory(id);
        }else {
            responseCall = holders.getBooksByLibrary(id,catIds,giveAwayValue,typeValue);
        }*/
        Log.d("mySelectedValues","values : "
                +"\n"+"Categories :"+catIds
                +"\n"+"type : "+typeValue
                +"\n"+"giveaway : "+giveAwayValue
                +"\n"+"libId : "+libId
                +"\n"+"libName: "+libraryName
        );

        responseCall.enqueue(new Callback<SearchResModel>() {
            @Override
            public void onResponse(Call<SearchResModel> call, Response<SearchResModel> response) {
                if (response.isSuccessful()){
                    if (response.body().getResponse().getCode() == 1){
                        progBar.setVisibility(View.GONE);
                        rvCollect.setVisibility(View.VISIBLE);
                        myList = response.body().getResponse().getBook_list();
                        if (myList.size() > 0){
                            adapter = new SearchAdapter(myList, CollectBookActivity.this);
                            LinearLayoutManager verticalManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
                            // verticalManager.setReverseLayout(true);
                            // verticalManager.setStackFromEnd(true);
                            rvCollect.setLayoutManager(verticalManager);
                            rvCollect.setAdapter(adapter);
                        }
                    }else {
                        progBar.setVisibility(View.GONE);
                        rvCollect.setVisibility(View.GONE);
                        Toast.makeText(CollectBookActivity.this, "No Result Fount.", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    progBar.setVisibility(View.GONE);
                    rvCollect.setVisibility(View.GONE);
                    Toast.makeText(CollectBookActivity.this, "Something went wrong !", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<SearchResModel> call, Throwable t) {
                progBar.setVisibility(View.GONE);
                rvCollect.setVisibility(View.GONE);
                Toast.makeText(CollectBookActivity.this, ""+t, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (flag == 1){
            getCollectBook(libId,isLibrary, catIds, giveAwayValue, typeValue);
            flag = 0;
        }
    }

    public void callbackMethod(String catId,String city,String area,String apartment,String distance,String type,String giveaway) {
        catIds = catId;
        //typeValue = type;
        //giveAwayValue = giveaway;
        //endLimit = endLimit + 10;
        getCollectBook(libId,isLibrary,catIds,giveAwayValue,typeValue);
    }

    @Override
    public void onDestroy() {
        Constants.selectedCatList.clear();
        Constants.selectedCategories = "";
        Constants.selectedFilterValue = "";
        Constants.giveAway = "";
        editor.clear();
        editor.commit();

        super.onDestroy();
    }
}