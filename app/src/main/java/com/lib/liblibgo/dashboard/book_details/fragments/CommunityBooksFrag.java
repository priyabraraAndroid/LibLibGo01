package com.lib.liblibgo.dashboard.book_details.fragments;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.lib.liblibgo.R;
import com.lib.liblibgo.activity.LoginWithPhoneNumber;
import com.lib.liblibgo.adapter.CategoryAdapter;
import com.lib.liblibgo.adapter.NearMeBookAdapter;
import com.lib.liblibgo.adapter.NearMeBookAdapterTwo;
import com.lib.liblibgo.adapter.OpenBookAdapter;
import com.lib.liblibgo.api.AllApiClass;
import com.lib.liblibgo.api.Holders;
import com.lib.liblibgo.common.Constants;
import com.lib.liblibgo.common.UserCurrentLocation;
import com.lib.liblibgo.common.UserDatabase;
import com.lib.liblibgo.dashboard.apartment_admin.UploadBookDetails;
import com.lib.liblibgo.dashboard.book_upload.UploadBookActivity;
import com.lib.liblibgo.dashboard.library.CommunityLibraryRulesActivity;
import com.lib.liblibgo.dialogs.CategoriCommunityFilterDialog;
import com.lib.liblibgo.dialogs.CategoriFilterDialog;
import com.lib.liblibgo.dialogs.SortingDialog;
import com.lib.liblibgo.listner.CommunityFilterInterfaceClass;
import com.lib.liblibgo.listner.FilterInterfaceClass;
import com.lib.liblibgo.listner.SortListener;
import com.lib.liblibgo.model.BookNewModel;
import com.lib.liblibgo.model.CategoryListData;
import com.lib.liblibgo.model.CategoryModel;
import com.lib.liblibgo.model.NearMeBookModel;
import com.lib.liblibgo.model.NearMeFilterModel;
import com.lib.liblibgo.model.subadmin.LibraryStatusModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CommunityBooksFrag extends Fragment implements CommunityFilterInterfaceClass,SortListener {

    private List<CategoryListData> catList = new ArrayList<>();
    private CategoryAdapter adapter;
    private RecyclerView rvBook,rvBookTwo;
    private ProgressBar progBarr;

    private List<BookNewModel.ResModelData.NewBookList> bookList = new ArrayList<>();
    private List<BookNewModel.ResModelData.NewBookOpenList> openBookList = new ArrayList<>();

    private List<Object> myMargedList = new ArrayList<>();

    private NearMeBookAdapterTwo bookAdapter;
    private OpenBookAdapter adapterOpen;

    // private RelativeLayout rlLocation;
    private PopupWindow popupWindow;
    // private TextView tvRange;
    String apartment = "";
    String city = "";
    String area = "";
    private UserCurrentLocation location;
    private UserDatabase database;
    private RelativeLayout rlCategory;
    private RelativeLayout rlNearMe;
    private RelativeLayout rlType;
    private ImageView ivFilterCategory;
    private ImageView ivFilterNearMe;
    private ImageView ivType;
    private ImageView ivSearch;
    private FloatingActionButton fabAddBooks,fabSort;
    private String bookType = "";
    private String giveAway = "";
    private String userId = "";
    private String catIds = "";
    private String cityValue = "";
    private String areaValue = "";
    private String apartmentValue = "";
    private String distanceValue = "";
    private String typeValue = "";
    private String giveAwayValue = "";

    private List<CategoryListData> myCatList = new ArrayList<>();
    private List<NearMeFilterModel> myNearbyList = new ArrayList<>();

    private RelativeLayout rlLocation;
    private TextView tvRange;

    private SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefMyBooksCommunity";
    private SharedPreferences.Editor editor;
    private Context mContext;
    private FilterInterfaceClass callback2;
    private CommunityFilterInterfaceClass callback;
    private SortListener sortingcallback;
    private TextView tvType;
    private ImageView icClearFilter;
    private SwipeRefreshLayout pullToRefresh;
    private View popupView;
    private TextView tvForRent;
    private TextView tvForSale;
    private TextView tvForBoth;
    private TextView tvForGiveaway;
    private TextView tvAllTpe;
    private CardView cardSort;
    private String sortValue="";
    private int startLimit = 0;
    private int endLimit = 12;
    private GridLayoutManager manager;
    private GridLayoutManager managerTwo;

    int pastVisiblesItems, visibleItemCount, totalItemCount;
    private boolean mLoadTing = true;
    private NestedScrollView nestedScrollView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_community_books, container, false);

        sharedpreferences = mContext.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();

        callback=this;
        sortingcallback=this;

        rvBook = (RecyclerView)view.findViewById(R.id.rvBook);
        rvBookTwo = (RecyclerView)view.findViewById(R.id.rvBookTwo);
        progBarr = (ProgressBar)view.findViewById(R.id.progBarr);

        rlCategory = (RelativeLayout) view.findViewById(R.id.rlCategory);
        rlLocation = (RelativeLayout)view.findViewById(R.id.rlLocation);
        rlType = (RelativeLayout)view.findViewById(R.id.rlType);
        tvRange = (TextView)view.findViewById(R.id.tvRange);
        tvType = (TextView)view.findViewById(R.id.tvType);
        icClearFilter = (ImageView)view.findViewById(R.id.icClearFilter);
        nestedScrollView = (NestedScrollView)view.findViewById(R.id.nestedScrollView);

        fabAddBooks = (FloatingActionButton)view.findViewById(R.id.fabAddBooks);
        fabSort = (FloatingActionButton)view.findViewById(R.id.fabSort);
        pullToRefresh = (SwipeRefreshLayout)view.findViewById(R.id.pullToRefresh);

        database = new UserDatabase(mContext);

        manager = new GridLayoutManager(mContext,3);
        managerTwo = new GridLayoutManager(mContext,3);
        rvBook.setLayoutManager(manager);
        rvBookTwo.setLayoutManager(managerTwo);

        getCategoryList();
        getNearByData();

        if (Constants.selectedFilterValueCommunity.equals("For Rent")){
            editor.putString("select_type","2");
            editor.commit();
            tvType.setText("For Rent");
            Constants.selectedFilterValueCommunity = "For Rent";
            Constants.giveAwayCommunity = "";
            Constants.sortingValueCommunity = "";
            typeValue = Constants.selectedFilterValueCommunity;
            giveAwayValue = Constants.giveAwayCommunity;
            sortValue = Constants.sortingValueCommunity;
            getBooksByCategory(catIds,cityValue,areaValue,apartmentValue,distanceValue,typeValue,giveAwayValue,sortValue);
        }else if (Constants.selectedFilterValueCommunity.equals("For Sale")){
            editor.putString("select_type","3");
            editor.commit();
            tvType.setText("For Buy");
            Constants.selectedFilterValueCommunity = "For Sale";
            Constants.giveAwayCommunity = "";
            Constants.sortingValueCommunity = "";
            typeValue = Constants.selectedFilterValueCommunity;
            giveAwayValue = Constants.giveAwayCommunity;
            sortValue = Constants.sortingValueCommunity;
            getBooksByCategory(catIds,cityValue,areaValue,apartmentValue,distanceValue,typeValue,giveAwayValue,sortValue);
        }else if (Constants.giveAwayCommunity.equals("yes")){
            editor.putString("select_type","5");
            editor.commit();
            tvType.setText("Giveaway");
            Constants.selectedFilterValueCommunity = "";
            Constants.giveAwayCommunity = "yes";
            Constants.sortingValueCommunity = "";
            typeValue = Constants.selectedFilterValueCommunity;
            giveAwayValue = Constants.giveAwayCommunity;
            sortValue = Constants.sortingValueCommunity;
            getBooksByCategory(catIds,cityValue,areaValue,apartmentValue,distanceValue,typeValue,giveAwayValue,sortValue);
        }else if (Constants.sortingValueCommunity.equals("popular")){
            Constants.selectedFilterValueCommunity = "";
            Constants.giveAwayCommunity = "";
            Constants.sortingValueCommunity = "popular";
            typeValue = Constants.selectedFilterValueCommunity;
            giveAwayValue = Constants.giveAwayCommunity;
            sortValue = Constants.sortingValueCommunity;
            getBooksByCategory(catIds,cityValue,areaValue,apartmentValue,distanceValue,typeValue,giveAwayValue,sortValue);
        }else {
            editor.putString("select_type","1");
            editor.commit();
            tvType.setText("Buy Or Rent");
            Constants.selectedFilterValueCommunity = "";
            Constants.giveAwayCommunity = "";
            Constants.sortingValueCommunity = "";
            typeValue = Constants.selectedFilterValueCommunity;
            giveAwayValue = Constants.giveAwayCommunity;
            catIds = Constants.selectedCategoriesCommunity;
            sortValue = Constants.sortingValueCommunity;
            getBooksByCategory(catIds,cityValue,areaValue,apartmentValue,distanceValue,typeValue,giveAwayValue,sortValue);
        }


        nestedScrollView.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
            if(v.getChildAt(v.getChildCount() - 1) != null) {
                if ((scrollY >= (v.getChildAt(v.getChildCount() - 1).getMeasuredHeight() - v.getMeasuredHeight())) &&
                        scrollY > oldScrollY) {
                    if (mLoadTing) {
                        mLoadTing = false;
                        endLimit = endLimit + 12;
                        startLimit = startLimit + 12;
                        Log.e("MyDatas", "Last Item Wow !");
                        getBooksByCategory(catIds,cityValue,areaValue,apartmentValue,distanceValue,typeValue,giveAwayValue,sortValue);
                    }
                }
            }
        });

        rlCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Constants.isOwnBooks = "0";
                CategoriCommunityFilterDialog dialog = new CategoriCommunityFilterDialog();
                dialog.newInstance(callback2,callback);
                dialog.show(getActivity().getSupportFragmentManager(),
                        "cat_dialog_fragment");
            }
        });

        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                bookList.clear();
                openBookList.clear();
                bookList = new ArrayList<>();
                openBookList = new ArrayList<>();
                startLimit = 0;
                endLimit = 12;
                getBooksByCategory(catIds, cityValue, areaValue, apartmentValue, distanceValue, typeValue, giveAwayValue, sortValue);
            }
        });

        fabAddBooks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dexter.withContext(mContext).withPermissions(Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA)
                        .withListener(new MultiplePermissionsListener() {
                            @Override
                            public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                                if (database.getUserId().equals("")){
                                    Intent intentHome = new Intent(mContext, LoginWithPhoneNumber.class);
                                    startActivity(intentHome);
                                }else {
                                    //isApartmentLibraryCreated();
                                    editor.putString("community_id","0");
                                    editor.commit();
                                    Intent intent = new Intent(mContext, UploadBookActivity.class);
                                    Constants.isOwnLibrary = false;
                                    Constants.libraryType = "virtual";
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
                            }
                            @Override
                            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                                permissionToken.continuePermissionRequest();
                            }
                        }).check();
            }
        });



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
                getNearByData();
                //endLimit = 10;
                catIds = "";
                cityValue = "";
                areaValue = "";
                apartmentValue = "";
                distanceValue = "";
                typeValue = "";
                giveAwayValue = "";
                editor.clear();
                editor.commit();
                Constants.selectedCatListCommunity.clear();
                Constants.selectedCategoriesCommunity = "";
                Constants.selectedNearByCityCommunity = "";
                Constants.selectedNearByAreaCommunity = "";
                Constants.selectedNearByApartmentCommunity = "";
                Constants.selectedNearByDistanceCommunity = "";

                Constants.selectedFilterValueCommunity = "";
                Constants.giveAwayCommunity = "";
                Constants.sortingValueCommunity = "";
                tvRange.setText("Books4U");
                tvType.setText("Buy Or Rent");
                bookList.clear();
                openBookList.clear();
                bookList = new ArrayList<>();
                openBookList = new ArrayList<>();
                startLimit = 0;
                endLimit = 12;
                getBooksByCategory(catIds,cityValue,areaValue,apartmentValue,distanceValue,typeValue,giveAwayValue,sortValue);
            }
        });

        fabSort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SortingDialog dialog = new SortingDialog();
                dialog.newInstance(sortingcallback);
                dialog.show(getActivity().getSupportFragmentManager(),
                        "sort_dialog_fragment");
            }
        });

        return view;
    }



    private void showTypePopup(View view) {
        LayoutInflater layoutInflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        popupView = layoutInflater.inflate(R.layout.layout_my_book_filter_type, null);

        String selectedItem = sharedpreferences.getString("select_type", "");

        popupWindow = new PopupWindow(
                popupView,
                300,
                ViewGroup.LayoutParams.WRAP_CONTENT);

        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setOutsideTouchable(true);

        tvForRent = popupView.findViewById(R.id.tvForRent);
        tvForSale = popupView.findViewById(R.id.tvForSale);
        tvForBoth = popupView.findViewById(R.id.tvForBoth);
        tvForGiveaway = popupView.findViewById(R.id.tvForGiveaway);
        tvAllTpe = popupView.findViewById(R.id.tvAllTpe);

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
                Constants.selectedFilterValueCommunity = "";
                Constants.giveAwayCommunity = "";
                catIds = Constants.selectedCategoriesCommunity;
                cityValue = Constants.selectedNearByCityCommunity;
                areaValue = Constants.selectedNearByAreaCommunity;
                apartmentValue = Constants.selectedNearByApartmentCommunity;
                distanceValue = Constants.selectedNearByDistanceCommunity;
                typeValue = Constants.selectedFilterValueCommunity;
                giveAwayValue = Constants.giveAwayCommunity;
                sortValue = Constants.sortingValueCommunity;
                bookList.clear();
                openBookList.clear();
                bookList = new ArrayList<>();
                openBookList = new ArrayList<>();
                startLimit = 0;
                endLimit = 12;
                getBooksByCategory(catIds,cityValue,areaValue,apartmentValue,distanceValue,typeValue,giveAwayValue,sortValue);
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
                Constants.selectedFilterValueCommunity = "For Rent";
                Constants.giveAwayCommunity = "";
                catIds = Constants.selectedCategoriesCommunity;
                cityValue = Constants.selectedNearByCityCommunity;
                areaValue = Constants.selectedNearByAreaCommunity;
                apartmentValue = Constants.selectedNearByApartmentCommunity;
                distanceValue = Constants.selectedNearByDistanceCommunity;
                typeValue = Constants.selectedFilterValueCommunity;
                giveAwayValue = Constants.giveAwayCommunity;
                bookList.clear();
                openBookList.clear();
                bookList = new ArrayList<>();
                openBookList = new ArrayList<>();
                startLimit = 0;
                endLimit = 12;
                getBooksByCategory(catIds,cityValue,areaValue,apartmentValue,distanceValue,typeValue,giveAwayValue,sortValue);
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
                Constants.selectedFilterValueCommunity = "For Sale";
                Constants.giveAwayCommunity = "";
                catIds = Constants.selectedCategoriesCommunity;
                cityValue = Constants.selectedNearByCityCommunity;
                areaValue = Constants.selectedNearByAreaCommunity;
                apartmentValue = Constants.selectedNearByApartmentCommunity;
                distanceValue = Constants.selectedNearByDistanceCommunity;
                typeValue = Constants.selectedFilterValueCommunity;
                giveAwayValue = Constants.giveAwayCommunity;
                bookList.clear();
                openBookList.clear();
                bookList = new ArrayList<>();
                openBookList = new ArrayList<>();
                startLimit = 0;
                endLimit = 12;
                getBooksByCategory(catIds,cityValue,areaValue,apartmentValue,distanceValue,typeValue,giveAwayValue,sortValue);
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
                Constants.selectedFilterValueCommunity = "Both";
                Constants.giveAwayCommunity = "";
                catIds = Constants.selectedCategoriesCommunity;
                cityValue = Constants.selectedNearByCityCommunity;
                areaValue = Constants.selectedNearByAreaCommunity;
                apartmentValue = Constants.selectedNearByApartmentCommunity;
                distanceValue = Constants.selectedNearByDistanceCommunity;
                typeValue = Constants.selectedFilterValueCommunity;
                giveAwayValue = Constants.giveAwayCommunity;
                bookList.clear();
                openBookList.clear();
                bookList = new ArrayList<>();
                openBookList = new ArrayList<>();
                startLimit = 0;
                endLimit = 12;
                getBooksByCategory(catIds,cityValue,areaValue,apartmentValue,distanceValue,typeValue,giveAwayValue,sortValue);
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
                Constants.selectedFilterValueCommunity = "";
                Constants.giveAwayCommunity = "yes";
                catIds = Constants.selectedCategoriesCommunity;
                cityValue = Constants.selectedNearByCityCommunity;
                areaValue = Constants.selectedNearByAreaCommunity;
                apartmentValue = Constants.selectedNearByApartmentCommunity;
                distanceValue = Constants.selectedNearByDistanceCommunity;
                typeValue = Constants.selectedFilterValueCommunity;
                giveAwayValue = Constants.giveAwayCommunity;
                bookList.clear();
                openBookList.clear();
                bookList = new ArrayList<>();
                openBookList = new ArrayList<>();
                startLimit = 0;
                endLimit = 12;
                getBooksByCategory(catIds,cityValue,areaValue,apartmentValue,distanceValue,typeValue,giveAwayValue,sortValue);
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
    private void getBooksByCategory(String catIds,String cityValue,String areaValue,String apartmentValue,String distanceValue,String typeValue,String giveAwayValue,String sortValue) {
        Log.d("mySelectedValues","values : \nCategories :"+catIds
                +"\n"+"city : "+cityValue
                +"\n"+"pin : "+areaValue
                +"\n"+"userId : "+apartmentValue
                +"\n"+"distance : "+distanceValue
                +"\n"+"type : "+typeValue
                +"\n"+"giveaway : "+giveAwayValue
                +"\n"+"sortBy : "+sortValue
        );

        //progBarr.setVisibility(View.VISIBLE);
        final ProgressDialog progressBar = new ProgressDialog(mContext);
        progressBar.setMessage("Please wait...");
        progressBar.setCancelable(false);
        progressBar.show();
        location = new UserCurrentLocation(mContext);
        Holders holders = AllApiClass.getInstance().getApi();
        Call<JsonObject> resCall = holders.getBooksByAllFiltersNew(catIds,String.valueOf(location.latitude),String.valueOf(location.longitude),
                distanceValue,cityValue,areaValue,typeValue,giveAwayValue,apartmentValue,"0",database.getUserId(),sortValue, String.valueOf(startLimit), String.valueOf(endLimit));
        resCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                mLoadTing = true;
                if (response.isSuccessful()){
                    pullToRefresh.setRefreshing(false);
                    JsonObject body = response.body();
                    String s = new Gson().toJson(body);
                    try {
                        JSONObject mRoot = new JSONObject(s);
                        JSONObject resData = mRoot.getJSONObject("response");
                        if (resData.getString("code").equals("1")){
                            progressBar.dismiss();
                            JSONArray book_list = resData.getJSONArray("book_list");
                            JSONArray open_books = resData.getJSONArray("open_books");

                            Gson gson = new Gson();
                            Type type = new TypeToken<List<BookNewModel.ResModelData.NewBookList>>() {
                            }.getType();
                            List<BookNewModel.ResModelData.NewBookList> modelList = gson.fromJson(String.valueOf(book_list), type);
                            bookList.addAll(modelList);

                            Gson gsonOpen = new Gson();
                            Type typeOpen = new TypeToken<List<BookNewModel.ResModelData.NewBookOpenList>>() {
                            }.getType();
                            List<BookNewModel.ResModelData.NewBookOpenList> modelListOpen = gsonOpen.fromJson(String.valueOf(open_books), typeOpen);
                            openBookList.addAll(modelListOpen);

                            if (bookList.size() > 0){
                                bookAdapter = new NearMeBookAdapterTwo(bookList, mContext);
                                rvBook.setAdapter(bookAdapter);
                                rvBook.setVisibility(View.VISIBLE);
                                //bookAdapter.notifyDataSetChanged();
                            }else {
                                rvBook.setVisibility(View.GONE);
                            }

                            if (openBookList.size() > 0){
                                adapterOpen = new OpenBookAdapter(openBookList, mContext);
                                rvBookTwo.setAdapter(adapterOpen);
                                rvBookTwo.setVisibility(View.VISIBLE);
                                //adapterOpen.notifyDataSetChanged();
                            }else {
                                rvBookTwo.setVisibility(View.GONE);
                            }

                        }else {
                            Toast.makeText(mContext, ""+mRoot.getString("message"), Toast.LENGTH_SHORT).show();
                            progressBar.dismiss();
                            rvBook.setVisibility(View.GONE);
                            rvBookTwo.setVisibility(View.GONE);
                        }
                    }catch (JSONException e) {
                        e.printStackTrace();
                    }

                    /*if (response.body().getResponse().getCode().equals("1")){
                        bookList.clear();
                        openBookList.clear();
                        bookList = new ArrayList<>();
                        openBookList = new ArrayList<>();
                        progBarr.setVisibility(View.GONE);
                        bookList = response.body().getResponse().getBook_list();
                        openBookList = response.body().getResponse().getOpen_books();

                        //myMargedList.addAll(bookList);
                        //myMargedList.addAll(openBookList);

                        if (bookList.size() > 0){
                            bookAdapter = new NearMeBookAdapterTwo(bookList, mContext);
                            GridLayoutManager manager = new GridLayoutManager(mContext,3);
                            rvBook.setLayoutManager(manager);
                            rvBook.setAdapter(bookAdapter);
                            rvBook.setVisibility(View.VISIBLE);
                            //bookAdapter.notifyDataSetChanged();
                        }else {
                            rvBook.setVisibility(View.GONE);
                        }

                        if (openBookList.size() > 0){
                            adapterOpen = new OpenBookAdapter(openBookList, mContext);
                            GridLayoutManager manager = new GridLayoutManager(mContext,3);
                            rvBookTwo.setLayoutManager(manager);
                            rvBookTwo.setAdapter(adapterOpen);
                            rvBookTwo.setVisibility(View.VISIBLE);
                           //adapterOpen.notifyDataSetChanged();
                        }else {
                            rvBookTwo.setVisibility(View.GONE);
                        }

                    }else {
                        Toast.makeText(mContext, ""+response.body().getResponse().getMessage(), Toast.LENGTH_SHORT).show();
                        progBarr.setVisibility(View.GONE);
                        rvBook.setVisibility(View.GONE);
                        rvBookTwo.setVisibility(View.GONE);
                    }*/

                }else {
                    pullToRefresh.setRefreshing(false);
                    Toast.makeText(mContext, "Something went wrong !", Toast.LENGTH_SHORT).show();
                    progressBar.dismiss();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                mLoadTing = true;
                pullToRefresh.setRefreshing(false);
                Toast.makeText(mContext, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
                progBarr.setVisibility(View.GONE);
            }
        });
    }

    private void showLocationRangeMenu(View view) {
        LayoutInflater layoutInflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View popupView = layoutInflater.inflate(R.layout.layout_my_book_filter_new, null);

        String selectedItem = sharedpreferences.getString("select", "");

        popupWindow = new PopupWindow(
                popupView,
                300,
                ViewGroup.LayoutParams.WRAP_CONTENT);

        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setOutsideTouchable(true);

        final TextView tvAllBooks = popupView.findViewById(R.id.tvAllBooks);
        final TextView tvmyBooks = popupView.findViewById(R.id.tvmyBooks);

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
                Constants.selectedNearByCityCommunity = "";
                Constants.selectedNearByAreaCommunity = "";
                Constants.selectedNearByApartmentCommunity = "";
                Constants.selectedNearByDistanceCommunity = "";
                catIds = Constants.selectedCategoriesCommunity;
                cityValue = Constants.selectedNearByCityCommunity;
                areaValue = Constants.selectedNearByAreaCommunity;
                apartmentValue = Constants.selectedNearByApartmentCommunity;
                distanceValue = Constants.selectedNearByDistanceCommunity;
                typeValue = Constants.selectedFilterValueCommunity;
                giveAwayValue = Constants.giveAwayCommunity;
                rlCategory.setVisibility(View.VISIBLE);
                rlType.setVisibility(View.VISIBLE);
                icClearFilter.setVisibility(View.VISIBLE);
                bookList.clear();
                openBookList.clear();
                bookList = new ArrayList<>();
                openBookList = new ArrayList<>();
                startLimit = 0;
                endLimit = 12;
                getBooksByCategory(catIds,cityValue,areaValue,apartmentValue,distanceValue,typeValue,giveAwayValue,sortValue);
            }
        });

        tvmyBooks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
                if (database.getUserId().equals("")){
                    Intent intentHome = new Intent(mContext, LoginWithPhoneNumber.class);
                    startActivity(intentHome);
                }else {
                    editor.putString("select","2");
                    editor.commit();
                    tvRange.setText("My Books");
                    /*rlCategory.setVisibility(View.INVISIBLE);
                    rlType.setVisibility(View.INVISIBLE);
                    icClearFilter.setVisibility(View.INVISIBLE);
                    getMyBookList();*/
                    Constants.selectedNearByCityCommunity = "";
                    Constants.selectedNearByAreaCommunity = "";
                    Constants.selectedNearByApartmentCommunity = database.getUserId();
                    Constants.selectedNearByDistanceCommunity = "";
                    catIds = Constants.selectedCategoriesCommunity;
                    cityValue = Constants.selectedNearByCityCommunity;
                    areaValue = Constants.selectedNearByAreaCommunity;
                    apartmentValue = Constants.selectedNearByApartmentCommunity;
                    distanceValue = Constants.selectedNearByDistanceCommunity;
                    typeValue = Constants.selectedFilterValueCommunity;
                    giveAwayValue = Constants.giveAwayCommunity;
                    rlCategory.setVisibility(View.VISIBLE);
                    rlType.setVisibility(View.VISIBLE);
                    icClearFilter.setVisibility(View.VISIBLE);
                    bookList.clear();
                    openBookList.clear();
                    bookList = new ArrayList<>();
                    openBookList = new ArrayList<>();
                    startLimit = 0;
                    endLimit = 12;
                    getBooksByCategory(catIds,cityValue,areaValue,apartmentValue,distanceValue,typeValue,giveAwayValue,sortValue);
                }
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

    /*private void getMyBookList() {
        progBarr.setVisibility(View.VISIBLE);
        location = new UserCurrentLocation(mContext);
        Holders holders = AllApiClass.getInstance().getApi();
        Call<NearMeBookModel> resCall = holders.getMyOwnBooks(database.getUserId(),String.valueOf(location.latitude),String.valueOf(location.longitude),"0");
        resCall.enqueue(new Callback<NearMeBookModel>() {
            @Override
            public void onResponse(Call<NearMeBookModel> call, Response<NearMeBookModel> response) {
                if (response.isSuccessful()){
                    if (response.body().getResponse().getCode().equals("1")){
                        bookList.clear();
                        bookList = new ArrayList<>();
                        rvBook.setVisibility(View.VISIBLE);
                        progBarr.setVisibility(View.GONE);
                        bookList = response.body().getResponse().getBook_list();
                        if (bookList.size() > 0){
                            bookAdapter = new NearMeBookAdapter(bookList, mContext);
                            GridLayoutManager manager = new GridLayoutManager(mContext,2);
                            // verticalManager.setReverseLayout(true);
                            // verticalManager.setStackFromEnd(true);
                            rvBook.setLayoutManager(manager);
                            rvBook.setAdapter(bookAdapter);
                            bookAdapter.notifyDataSetChanged();
                        }else {
                            rvBook.setVisibility(View.GONE);
                        }
                    }else {
                        Toast.makeText(mContext, ""+response.body().getResponse().getMessage(), Toast.LENGTH_SHORT).show();
                        progBarr.setVisibility(View.GONE);
                        rvBook.setVisibility(View.GONE);
                    }
                }else {
                    Toast.makeText(mContext, "Something went wrong !", Toast.LENGTH_SHORT).show();
                    progBarr.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<NearMeBookModel> call, Throwable t) {
                Toast.makeText(mContext, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
                progBarr.setVisibility(View.GONE);
            }
        });
    }*/

    private void getNearByData() {
        Constants.myNearbyListCommunity.add(new NearMeFilterModel("Within Community"));
        Constants.myNearbyListCommunity.add(new NearMeFilterModel("Within Area"));
        Constants.myNearbyListCommunity.add(new NearMeFilterModel("Within City"));
        Constants.myNearbyListCommunity.add(new NearMeFilterModel("Within 2km"));
        Constants.myNearbyListCommunity.add(new NearMeFilterModel("Within 5km"));
        Constants.myNearbyListCommunity.add(new NearMeFilterModel("Within 10km"));
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
                        Constants.catListCommunity = myCatList;

                        if (Constants.showCatPopup.equals("true")){
                            Constants.isOwnBooks = "0";
                            CategoriCommunityFilterDialog dialog = new CategoriCommunityFilterDialog();
                            dialog.newInstance(callback2,callback);
                            dialog.show(getActivity().getSupportFragmentManager(),
                                    "cat_dialog_fragment");
                        }
                    }
                }
            }
            @Override
            public void onFailure(Call<CategoryModel> call, Throwable t) {

            }
        });
    }

    @Override
    public void onAttach(@NonNull Context context) {
        mContext = context;
        super.onAttach(context);
    }
    private void isApartmentLibraryCreated() {
        final ProgressDialog progressBar = new ProgressDialog(getContext());
        progressBar.setMessage("Please wait...");
        progressBar.setCancelable(false);
        progressBar.show();
        Holders holders = AllApiClass.getInstance().getApi();
        Call<LibraryStatusModel> resCall = holders.checkApartmentLibraryStatus(database.getUserId());
        resCall.enqueue(new Callback<LibraryStatusModel>() {
            @Override
            public void onResponse(Call<LibraryStatusModel> call, Response<LibraryStatusModel> response) {
                if (response.isSuccessful()){
                    progressBar.dismiss();
                    if (response.body().getResponse().getLibrary_status().equals("no")){
                        UserCurrentLocation location = new UserCurrentLocation(getContext());
                        Log.d("locationnn",""+location.latitude);
                        Log.d("locationnn",""+location.longitude);
                        Constants.latitude = String.valueOf(location.latitude);
                        Constants.longitude = String.valueOf(location.longitude);
                        Constants.SelectedLibraryProfileImage = "";
                        Intent intent = new Intent(getContext(), CommunityLibraryRulesActivity.class);
                        Constants.USER_APARTMENT_NAME = response.body().getResponse().getApartment_name();
                        Constants.USER_APARTMENT_ID = response.body().getResponse().getApartment_id();
                        Constants.USER_FLAT_ID = response.body().getResponse().getFlat_id();
                        Constants.USER_FLAT_NAME = response.body().getResponse().getFlat_no();
                        Constants.isOwnLibrary = false;
                        intent.putExtra("count",response.body().getResponse().getMy_accepted_count());
                        //Constants.libraryType = response.body().getResponse().getLibrary_type();
                        startActivity(intent);
                    }else {
                        editor.putString("community_id","0");
                        editor.commit();
                        Intent intent = new Intent(mContext, UploadBookDetails.class);
                        Constants.isOwnLibrary = false;
                        Constants.libraryType = response.body().getResponse().getLibrary_type();
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
                        if (Constants.libraryType.equals("physical")){
                            intent.putExtra("giveaway_status","no");
                        }else {
                            intent.putExtra("giveaway_status","yes");
                        }
                        startActivity(intent);
                    }
                }else {
                    progressBar.dismiss();
                    Toast.makeText(getContext(), "Something went wrong !", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LibraryStatusModel> call, Throwable t) {
                progressBar.dismiss();
                Toast.makeText(getContext(), "Check your internet !", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void callbackMethod(String catId1,String city1,String area1,String apartment1,String distance1,String type1,String giveaway1) {
        catIds = catId1;
        //cityValue = city;
        //areaValue = area;
        //apartmentValue = apartment;
        //distanceValue = distance;
        //typeValue = type;
        //giveAwayValue = giveaway;
        Log.d("mySelectedValues","\nCategories :"+catIds
                +"\n"+"city : "+cityValue
                +"\n"+"pin : "+areaValue
                +"\n"+"userId : "+apartmentValue
                +"\n"+"distance : "+distanceValue
                +"\n"+"type : "+typeValue
                +"\n"+"giveaway : "+giveAwayValue
        );
        bookList.clear();
        openBookList.clear();
        bookList = new ArrayList<>();
        openBookList = new ArrayList<>();
        startLimit = 0;
        endLimit = 12;
        getBooksByCategory(catIds,cityValue,areaValue,apartmentValue,distanceValue,typeValue,giveAwayValue,sortValue);
    }

    @Override
    public void onDestroy() {
        Constants.selectedCatListCommunity.clear();
        Constants.selectedCategoriesCommunity = "";
        Constants.selectedNearByCityCommunity = "";
        Constants.selectedNearByAreaCommunity = "";
        Constants.selectedNearByApartmentCommunity = "";
        Constants.selectedNearByDistanceCommunity = "";

        Constants.selectedFilterValueCommunity = "";
        Constants.giveAwayCommunity = "";
        editor.clear();
        editor.commit();
        super.onDestroy();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
    }

    @Override
    public void onShoring(String sortingValue) {
        sortValue = sortingValue;
        Constants.sortingValueCommunity = sortValue;
        bookList.clear();
        openBookList.clear();
        bookList = new ArrayList<>();
        openBookList = new ArrayList<>();
        startLimit = 0;
        endLimit = 12;
        getBooksByCategory(catIds,cityValue,areaValue,apartmentValue,distanceValue,typeValue,giveAwayValue,sortValue);
        //Toast.makeText(mContext, ""+Constants.sortingValueCommunity, Toast.LENGTH_SHORT).show();
    }
}