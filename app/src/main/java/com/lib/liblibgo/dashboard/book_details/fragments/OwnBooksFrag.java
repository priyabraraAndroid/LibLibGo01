package com.lib.liblibgo.dashboard.book_details.fragments;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;

import androidx.annotation.NonNull;
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
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.lib.liblibgo.R;
import com.lib.liblibgo.activity.LoginWithPhoneNumber;
import com.lib.liblibgo.adapter.CategoryAdapter;
import com.lib.liblibgo.adapter.NearMeBookAdapter;
import com.lib.liblibgo.api.AllApiClass;
import com.lib.liblibgo.api.Holders;
import com.lib.liblibgo.common.Constants;
import com.lib.liblibgo.common.UserCurrentLocation;
import com.lib.liblibgo.common.UserDatabase;
import com.lib.liblibgo.dashboard.apartment_admin.UploadBookDetails;
import com.lib.liblibgo.dashboard.library.CreateLibraryActivity;
import com.lib.liblibgo.dialogs.CategoriFilterDialog;
import com.lib.liblibgo.listner.CommunityFilterInterfaceClass;
import com.lib.liblibgo.listner.FilterInterfaceClass;
import com.lib.liblibgo.model.CategoryListData;
import com.lib.liblibgo.model.CategoryModel;
import com.lib.liblibgo.model.NearMeBookModel;
import com.lib.liblibgo.model.NearMeFilterModel;
import com.lib.liblibgo.model.subadmin.LibraryStatusModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OwnBooksFrag extends Fragment implements FilterInterfaceClass {

    //private RecyclerView rvCategory;
    //private ProgressBar progBar;

    private List<CategoryListData> catList = new ArrayList<>();
    private CategoryAdapter adapter;
    private RecyclerView rvBook;
    private ProgressBar progBarr;

    private List<NearMeBookModel.ResModelData.NewrmeBookList> bookList = new ArrayList<>();
    private List<NearMeBookModel.ResModelData.NewrmeBookList> reloededList = new ArrayList<>();
    private NearMeBookAdapter bookAdapter;

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
    private FloatingActionButton fabAddBooks;
    private String bookType = "";
    private String giveAway = "";
    //private LinearLayout llFilter;
    //private ImageView ivFilter;
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
    private TextView tvRange,tvType;

    private SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefMyBooks";
    private SharedPreferences.Editor editor;
    private Context mContext;
    private FilterInterfaceClass callback;
    private CommunityFilterInterfaceClass callback2;
    private int startLimit = 0;
    private int endLimit = 0;
    int pastVisiblesItems, visibleItemCount, totalItemCount;
    private boolean mLoadTing = true;
    private ImageView icClearFilter;
    private SwipeRefreshLayout pullToRefresh;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_own_books, container, false);

        sharedpreferences = mContext.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();
        callback=this;

        rvBook = (RecyclerView)view.findViewById(R.id.rvBook);
        progBarr = (ProgressBar)view.findViewById(R.id.progBarr);

        //ivFilter = (ImageView)view.findViewById(R.id.ivFilter);
        rlCategory = (RelativeLayout) view.findViewById(R.id.rlCategory);
        rlLocation = (RelativeLayout)view.findViewById(R.id.rlLocation);
        rlType = (RelativeLayout)view.findViewById(R.id.rlType);
        tvRange = (TextView)view.findViewById(R.id.tvRange);
        tvType = (TextView)view.findViewById(R.id.tvType);
        icClearFilter = (ImageView)view.findViewById(R.id.icClearFilter);

        fabAddBooks = (FloatingActionButton)view.findViewById(R.id.fabAddBooks);
        pullToRefresh = (SwipeRefreshLayout)view.findViewById(R.id.pullToRefresh);

        database = new UserDatabase(mContext);
        location = new UserCurrentLocation(mContext);

        GridLayoutManager manager = new GridLayoutManager(mContext,2);
        rvBook.setLayoutManager(manager);

        //endLimit = endLimit + 10;
        getBooksByCategory(catIds,cityValue,areaValue,apartmentValue,distanceValue,typeValue,giveAwayValue);

        rlCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Constants.isOwnBooks = "1";
                CategoriFilterDialog dialog = new CategoriFilterDialog();
                dialog.newInstance(callback,callback2);
                dialog.show(getActivity().getSupportFragmentManager(),
                        "cat_dialog_fragment");
            }
        });

        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getBooksByCategory(catIds,cityValue,areaValue,apartmentValue,distanceValue,typeValue,giveAwayValue);
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
                                    isLibraryCreated();
                                }
                            }
                            @Override
                            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                                permissionToken.continuePermissionRequest();
                            }
                        }).check();
            }
        });

        getCategoryList();
        getNearByData();

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
                endLimit = 10;
                catIds = "";
                cityValue = "";
                areaValue = "";
                apartmentValue = "";
                distanceValue = "";
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
                tvRange.setText("Books4U");
                tvType.setText("Buy Or Rent");
                getBooksByCategory(catIds,cityValue,areaValue,apartmentValue,distanceValue,typeValue,giveAwayValue);
            }
        });

        /*rvBook.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) //check for scroll down
                {
                    visibleItemCount = manager.getChildCount();
                    totalItemCount = manager.getItemCount();
                    pastVisiblesItems = manager.findFirstVisibleItemPosition();
                    if (mLoadTing) {
                        if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                            mLoadTing = false;
                            endLimit = endLimit + 10;
                            Log.e("TAG", "Last Item Wow !");
                            getBooksByCategory(catIds,cityValue,areaValue,apartmentValue,distanceValue,typeValue,giveAwayValue);
                           // rvBook.smoothScrollToPosition(bookList.size()-1);
                        }
                    }
                }
            }
        });*/

        return view;
    }

    private void showTypePopup(View view) {
        LayoutInflater layoutInflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
                cityValue = Constants.selectedNearByCity;
                areaValue = Constants.selectedNearByArea;
                apartmentValue = Constants.selectedNearByApartment;
                distanceValue = Constants.selectedNearByDistance;
                typeValue = Constants.selectedFilterValue;
                giveAwayValue = Constants.giveAway;
                getBooksByCategory(catIds,cityValue,areaValue,apartmentValue,distanceValue,typeValue,giveAwayValue);
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
                cityValue = Constants.selectedNearByCity;
                areaValue = Constants.selectedNearByArea;
                apartmentValue = Constants.selectedNearByApartment;
                distanceValue = Constants.selectedNearByDistance;
                typeValue = Constants.selectedFilterValue;
                giveAwayValue = Constants.giveAway;
                getBooksByCategory(catIds,cityValue,areaValue,apartmentValue,distanceValue,typeValue,giveAwayValue);
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
                cityValue = Constants.selectedNearByCity;
                areaValue = Constants.selectedNearByArea;
                apartmentValue = Constants.selectedNearByApartment;
                distanceValue = Constants.selectedNearByDistance;
                typeValue = Constants.selectedFilterValue;
                giveAwayValue = Constants.giveAway;
                getBooksByCategory(catIds,cityValue,areaValue,apartmentValue,distanceValue,typeValue,giveAwayValue);
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
                cityValue = Constants.selectedNearByCity;
                areaValue = Constants.selectedNearByArea;
                apartmentValue = Constants.selectedNearByApartment;
                distanceValue = Constants.selectedNearByDistance;
                typeValue = Constants.selectedFilterValue;
                giveAwayValue = Constants.giveAway;
                getBooksByCategory(catIds,cityValue,areaValue,apartmentValue,distanceValue,typeValue,giveAwayValue);
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
                cityValue = Constants.selectedNearByCity;
                areaValue = Constants.selectedNearByArea;
                apartmentValue = Constants.selectedNearByApartment;
                distanceValue = Constants.selectedNearByDistance;
                typeValue = Constants.selectedFilterValue;
                giveAwayValue = Constants.giveAway;
                getBooksByCategory(catIds,cityValue,areaValue,apartmentValue,distanceValue,typeValue,giveAwayValue);
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
        LayoutInflater layoutInflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View popupView = layoutInflater.inflate(R.layout.layout_my_book_filter_all, null);

        String selectedItem = sharedpreferences.getString("select", "");

        popupWindow = new PopupWindow(
                popupView,
                300,
                ViewGroup.LayoutParams.WRAP_CONTENT);

        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setOutsideTouchable(true);

        final TextView tvAllBooks = popupView.findViewById(R.id.tvAllBooks);
        final TextView tvmyBooks = popupView.findViewById(R.id.tvmyBooks);

        final TextView tvApartment = popupView.findViewById(R.id.tvApartment);
        final TextView tvArea = popupView.findViewById(R.id.tvArea);
        final TextView tvCity = popupView.findViewById(R.id.tvCity);
        final TextView tv2km = popupView.findViewById(R.id.tv2km);
        final TextView tv5km = popupView.findViewById(R.id.tv5km);
        final TextView tv10km = popupView.findViewById(R.id.tv10km);

        if (selectedItem.equals("1")){
            tvAllBooks.setBackgroundColor(Color.parseColor("#345EA8"));
            tvAllBooks.setTextColor(Color.parseColor("#ffffff"));
        }else if (selectedItem.equals("2")){
            tvmyBooks.setBackgroundColor(Color.parseColor("#345EA8"));
            tvmyBooks.setTextColor(Color.parseColor("#ffffff"));
        }else if (selectedItem.equals("3")){
            tvApartment.setBackgroundColor(Color.parseColor("#345EA8"));
            tvApartment.setTextColor(Color.parseColor("#ffffff"));
        }else if (selectedItem.equals("4")){
            tvArea.setBackgroundColor(Color.parseColor("#345EA8"));
            tvArea.setTextColor(Color.parseColor("#ffffff"));
        }else if (selectedItem.equals("5")){
            tvCity.setBackgroundColor(Color.parseColor("#345EA8"));
            tvCity.setTextColor(Color.parseColor("#ffffff"));
        }else if (selectedItem.equals("6")){
            tv2km.setBackgroundColor(Color.parseColor("#345EA8"));
            tv2km.setTextColor(Color.parseColor("#ffffff"));
        }else if (selectedItem.equals("7")){
            tv5km.setBackgroundColor(Color.parseColor("#345EA8"));
            tv5km.setTextColor(Color.parseColor("#ffffff"));
        }else if (selectedItem.equals("8")){
            tv10km.setBackgroundColor(Color.parseColor("#345EA8"));
            tv10km.setTextColor(Color.parseColor("#ffffff"));
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
                endLimit = endLimit + 10;
                Constants.selectedNearByCity = "";
                Constants.selectedNearByArea = "";
                Constants.selectedNearByApartment = "";
                Constants.selectedNearByDistance = "";
                catIds = Constants.selectedCategories;
                cityValue = Constants.selectedNearByCity;
                areaValue = Constants.selectedNearByArea;
                apartmentValue = Constants.selectedNearByApartment;
                distanceValue = Constants.selectedNearByDistance;
                typeValue = Constants.selectedFilterValue;
                giveAwayValue = Constants.giveAway;
                getBooksByCategory(catIds,cityValue,areaValue,apartmentValue,distanceValue,typeValue,giveAwayValue);
                //llFilter.setVisibility(View.VISIBLE);
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
                    //llFilter.setVisibility(View.INVISIBLE);
                    getMyBookList();
                }
            }
        });

        tvApartment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
                if (database.getUserId().equals("") || database.getApartmentName().equals("")){
                    Toast.makeText(getContext(), "You are not any community user.", Toast.LENGTH_SHORT).show();
                    Constants.selectedNearByCity = "";
                    Constants.selectedNearByArea = "";
                    Constants.selectedNearByApartment = "";
                    Constants.selectedNearByDistance = "";
                }else {
                    editor.putString("select","3");
                    editor.commit();
                    tvRange.setText("Within Community");
                    Constants.selectedNearByCity = "";
                    Constants.selectedNearByArea = "";
                    Constants.selectedNearByApartment = database.getUserId();
                    Constants.selectedNearByDistance = "";
                    /*catIds = Constants.selectedCategories;
                    cityValue = Constants.selectedNearByCity;
                    areaValue = Constants.selectedNearByArea;
                    apartmentValue = Constants.selectedNearByApartment;
                    distanceValue = Constants.selectedNearByDistance;
                    typeValue = "";
                    giveAwayValue = "";*/
                    catIds = Constants.selectedCategories;
                    cityValue = Constants.selectedNearByCity;
                    areaValue = Constants.selectedNearByArea;
                    apartmentValue = Constants.selectedNearByApartment;
                    distanceValue = Constants.selectedNearByDistance;
                    typeValue = Constants.selectedFilterValue;
                    giveAwayValue = Constants.giveAway;
                    getBooksByCategory(catIds,cityValue,areaValue,apartmentValue,distanceValue,typeValue,giveAwayValue);
                }
            }
        });

        tvArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //location = new UserCurrentLocation(mContext);
                popupWindow.dismiss();
                editor.putString("select","4");
                editor.commit();
                tvRange.setText("Area");
                Constants.selectedNearByCity = "";
                Constants.selectedNearByArea = getUserPinCode(location.latitude,location.longitude);
                Constants.selectedNearByApartment = "";
                Constants.selectedNearByDistance = "";
                catIds = Constants.selectedCategories;
                cityValue = Constants.selectedNearByCity;
                areaValue = Constants.selectedNearByArea;
                apartmentValue = Constants.selectedNearByApartment;
                distanceValue = Constants.selectedNearByDistance;
                typeValue = Constants.selectedFilterValue;
                giveAwayValue = Constants.giveAway;
                getBooksByCategory(catIds,cityValue,areaValue,apartmentValue,distanceValue,typeValue,giveAwayValue);
            }
        });

        tvCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //location = new UserCurrentLocation(mContext);
                popupWindow.dismiss();
                editor.putString("select","5");
                editor.commit();
                tvRange.setText("City");
                Constants.selectedNearByCity = getUserCity(location.latitude,location.longitude);
                Constants.selectedNearByArea = "";
                Constants.selectedNearByApartment = "";
                Constants.selectedNearByDistance = "";
                catIds = Constants.selectedCategories;
                cityValue = Constants.selectedNearByCity;
                areaValue = Constants.selectedNearByArea;
                apartmentValue = Constants.selectedNearByApartment;
                distanceValue = Constants.selectedNearByDistance;
                typeValue = Constants.selectedFilterValue;
                giveAwayValue = Constants.giveAway;
                getBooksByCategory(catIds,cityValue,areaValue,apartmentValue,distanceValue,typeValue,giveAwayValue);
            }
        });

        tv2km.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //location = new UserCurrentLocation(mContext);
                popupWindow.dismiss();
                editor.putString("select","6");
                editor.commit();
                tvRange.setText("<= 2km");
                Constants.selectedNearByCity = "";
                Constants.selectedNearByArea = "";
                Constants.selectedNearByApartment = "";
                Constants.selectedNearByDistance = "2";
                catIds = Constants.selectedCategories;
                cityValue = Constants.selectedNearByCity;
                areaValue = Constants.selectedNearByArea;
                apartmentValue = Constants.selectedNearByApartment;
                distanceValue = Constants.selectedNearByDistance;
                typeValue = Constants.selectedFilterValue;
                giveAwayValue = Constants.giveAway;
                getBooksByCategory(catIds,cityValue,areaValue,apartmentValue,distanceValue,typeValue,giveAwayValue);
            }
        });

        tv5km.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //location = new UserCurrentLocation(mContext);
                popupWindow.dismiss();
                editor.putString("select","7");
                editor.commit();
                tvRange.setText("<= 5km");
                Constants.selectedNearByCity = "";
                Constants.selectedNearByArea = "";
                Constants.selectedNearByApartment = "";
                Constants.selectedNearByDistance = "5";
                catIds = Constants.selectedCategories;
                cityValue = Constants.selectedNearByCity;
                areaValue = Constants.selectedNearByArea;
                apartmentValue = Constants.selectedNearByApartment;
                distanceValue = Constants.selectedNearByDistance;
                typeValue = Constants.selectedFilterValue;
                giveAwayValue = Constants.giveAway;
                getBooksByCategory(catIds,cityValue,areaValue,apartmentValue,distanceValue,typeValue,giveAwayValue);
            }
        });

        tv10km.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //location = new UserCurrentLocation(mContext);
                popupWindow.dismiss();
                editor.putString("select","8");
                editor.commit();
                tvRange.setText("<= 10km");
                Constants.selectedNearByCity = "";
                Constants.selectedNearByArea = "";
                Constants.selectedNearByApartment = "";
                Constants.selectedNearByDistance = "10";
                catIds = Constants.selectedCategories;
                cityValue = Constants.selectedNearByCity;
                areaValue = Constants.selectedNearByArea;
                apartmentValue = Constants.selectedNearByApartment;
                distanceValue = Constants.selectedNearByDistance;
                typeValue = Constants.selectedFilterValue;
                giveAwayValue = Constants.giveAway;
                getBooksByCategory(catIds,cityValue,areaValue,apartmentValue,distanceValue,typeValue,giveAwayValue);
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

    public String getUserCity(double lat,double lon){
        //location = new UserCurrentLocation(this);
        String city="";
        Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
        List<Address> addresses = new ArrayList<>();
        try {
            addresses = geocoder.getFromLocation(lat, lon, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (!addresses.isEmpty()){
            city = addresses.get(0).getSubAdminArea();
        }else {
            city = "";
        }
        return city;
    }

    public String getUserPinCode(double lat,double lon){
        //location = new UserCurrentLocation(this);
        String area = "";
        Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
        List<Address> addresses = new ArrayList<>();
        try {
            addresses = geocoder.getFromLocation(lat, lon, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (!addresses.isEmpty()){
            //city = addresses.get(0).getLocality();
            area = addresses.get(0).getPostalCode();
        }else {
            area = "";
        }
        return area;
    }

    private void getMyBookList() {
        pullToRefresh.setRefreshing(false);
        progBarr.setVisibility(View.VISIBLE);
        location = new UserCurrentLocation(mContext);
        Holders holders = AllApiClass.getInstance().getApi();
        Call<NearMeBookModel> resCall = holders.getMyOwnBooks(database.getUserId(),String.valueOf(location.latitude),String.valueOf(location.longitude),"1");
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
                        //reloededList = bookList;
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
    }

    private void getNearByData() {
        Constants.myNearbyList.add(new NearMeFilterModel("Within Community"));
        Constants.myNearbyList.add(new NearMeFilterModel("Within Area"));
        Constants.myNearbyList.add(new NearMeFilterModel("Within City"));
        Constants.myNearbyList.add(new NearMeFilterModel("Within 2km"));
        Constants.myNearbyList.add(new NearMeFilterModel("Within 5km"));
        Constants.myNearbyList.add(new NearMeFilterModel("Within 10km"));
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

    private void isLibraryCreated() {
        final ProgressDialog progressBar = new ProgressDialog(mContext);
        progressBar.setMessage("Please wait...");
        progressBar.setCancelable(false);
        progressBar.show();
        Holders holders = AllApiClass.getInstance().getApi();
        Call<LibraryStatusModel> resCall = holders.checkLibraryStatus(database.getUserId());
        resCall.enqueue(new Callback<LibraryStatusModel>() {
            @Override
            public void onResponse(Call<LibraryStatusModel> call, Response<LibraryStatusModel> response) {
                if (response.isSuccessful()){
                    progressBar.dismiss();
                    if (response.body().getResponse().getLibrary_status().equals("no")){
                        UserCurrentLocation location = new UserCurrentLocation(mContext);
                        Log.d("locationnn",""+location.latitude);
                        Log.d("locationnn",""+location.longitude);
                        Constants.latitude = String.valueOf(location.latitude);
                        Constants.longitude = String.valueOf(location.longitude);
                        Constants.SelectedLibraryProfileImage = "";
                        Intent intent = new Intent(mContext, CreateLibraryActivity.class);
                        Constants.USER_APARTMENT_NAME = response.body().getResponse().getApartment_name();
                        Constants.USER_APARTMENT_ID = response.body().getResponse().getApartment_id();
                        Constants.USER_FLAT_ID = response.body().getResponse().getFlat_id();
                        Constants.USER_FLAT_NAME = response.body().getResponse().getFlat_no();
                        // Constants.libraryType = response.body().getResponse().getLibrary_type();
                        Constants.libraryType = "";
                        Constants.isOwnLibrary = true;
                        startActivity(intent);
                    }else {
                        Intent intent = new Intent(mContext, UploadBookDetails.class);
                        Constants.isOwnLibrary = true;
                        //Constants.libraryType = response.body().getResponse().getLibrary_type();
                        Constants.libraryType = "";
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
                }else {
                    progressBar.dismiss();
                    Toast.makeText(mContext, "Something went wrong !", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LibraryStatusModel> call, Throwable t) {
                progressBar.dismiss();
                Toast.makeText(mContext, "Check your internet !", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getBooksByCategory(String catIds,String cityValue,String areaValue,String apartmentValue,String distanceValue,String typeValue,String giveAwayValue) {
        Log.d("mySelectedValues","values : \nCategories :"+catIds
                +"\n"+"city : "+cityValue
                +"\n"+"pin : "+areaValue
                +"\n"+"userId : "+apartmentValue
                +"\n"+"distance : "+distanceValue
                +"\n"+"type : "+typeValue
                +"\n"+"giveaway : "+giveAwayValue
        );
        progBarr.setVisibility(View.VISIBLE);
        location = new UserCurrentLocation(mContext);
        Holders holders = AllApiClass.getInstance().getApi();
        /*Call<NearMeBookModel> resCall = holders.getBooksByAllFilters(catIds,String.valueOf(location.latitude),String.valueOf(location.longitude),
                distanceValue,cityValue,areaValue,typeValue,giveAwayValue,apartmentValue,"1",database.getUserId(),"","","");
        resCall.enqueue(new Callback<NearMeBookModel>() {
            @Override
            public void onResponse(Call<NearMeBookModel> call, Response<NearMeBookModel> response) {
                if (response.isSuccessful()){
                    pullToRefresh.setRefreshing(false);
                    if (response.body().getResponse().getCode().equals("1")){
                        rvBook.setVisibility(View.VISIBLE);
                        progBarr.setVisibility(View.GONE);
                        bookList = response.body().getResponse().getBook_list();
                        mLoadTing = true;
                        *//*if (bookList.size() > 0 && bookAdapter == null) {
                            bookAdapter = new NearMeBookAdapter(bookList, mContext);
                            rvBook.setAdapter(bookAdapter);
                            bookAdapter.notifyDataSetChanged();
                        } else {
                            bookAdapter.notifyDataSetChanged();
                            //rvBook.setVisibility(View.GONE);
                        }*//*
                        if (bookList.size() > 0){
                            bookAdapter = new NearMeBookAdapter(bookList, mContext);
                            rvBook.setAdapter(bookAdapter);
                            //bookAdapter.notifyDataSetChanged();
                        }else {
                            rvBook.setVisibility(View.GONE);
                        }
                    }else {
                        Toast.makeText(mContext, ""+response.body().getResponse().getMessage(), Toast.LENGTH_SHORT).show();
                        progBarr.setVisibility(View.GONE);
                        rvBook.setVisibility(View.GONE);
                        mLoadTing = false;
                    }
                }else {
                    pullToRefresh.setRefreshing(false);
                    mLoadTing = true;
                    Toast.makeText(mContext, "Something went wrong !", Toast.LENGTH_SHORT).show();
                    progBarr.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<NearMeBookModel> call, Throwable t) {
                pullToRefresh.setRefreshing(false);
                mLoadTing = true;
                Toast.makeText(mContext, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
                progBarr.setVisibility(View.GONE);
            }
        });*/
    }

    @Override
    public void onAttach(@NonNull Context context) {
        mContext = context;
        super.onAttach(context);
    }

    public void callbackMethod(String catId1,String city1,String area1,String apartment1,String distance1,String type1,String giveaway1) {
        catIds = catId1;
        //cityValue = city;
        //areaValue = area;
        //apartmentValue = apartment;
        //distanceValue = distance;
        //typeValue = type;
        //giveAwayValue = giveaway;
        //endLimit = endLimit + 10;
        getBooksByCategory(catIds,cityValue,areaValue,apartmentValue,distanceValue,typeValue,giveAwayValue);
    }

    @Override
    public void onDestroy() {
        Constants.selectedCatList.clear();
        Constants.selectedCategories = "";
        Constants.selectedNearByCity = "";
        Constants.selectedNearByArea = "";
        Constants.selectedNearByApartment = "";
        Constants.selectedNearByDistance = "";

        Constants.selectedFilterValue = "";
        Constants.giveAway = "";
        editor.clear();
        editor.commit();

        super.onDestroy();
    }

    @Override
    public void onStop() {
        super.onStop();
    }
}