package com.lib.liblibgo.dashboard.trackingbooks.fragments;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.lib.liblibgo.R;
import com.lib.liblibgo.activity.LoginWithPhoneNumber;
import com.lib.liblibgo.adapter.MyOwnBookAdapter;
import com.lib.liblibgo.adapter.ReturnHistoryAdapter;
import com.lib.liblibgo.api.AllApiClass;
import com.lib.liblibgo.api.Holders;
import com.lib.liblibgo.common.Constants;
import com.lib.liblibgo.common.UserCurrentLocation;
import com.lib.liblibgo.common.UserDatabase;
import com.lib.liblibgo.dashboard.apartment_admin.UploadBookDetails;
import com.lib.liblibgo.dashboard.book_collect.CollectApartmentBooksActivity;
import com.lib.liblibgo.dashboard.library.CreateLibraryActivity;
import com.lib.liblibgo.dashboard.library.fragments.CommunityLibraryFragment;
import com.lib.liblibgo.dashboard.return_history.ReturnHistoryActivity;
import com.lib.liblibgo.dialogs.MyCommunitiesDialog;
import com.lib.liblibgo.listner.CommunityLibraryClickListenerTwo;
import com.lib.liblibgo.listner.CustomInterfaceClass;
import com.lib.liblibgo.listner.MoveToCommunityClickListener;
import com.lib.liblibgo.model.GiftCardModel;
import com.lib.liblibgo.model.MyOwnBooksModel;
import com.lib.liblibgo.model.SearchResModel;
import com.lib.liblibgo.model.subadmin.LibraryStatusModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyIndBooksFragment extends Fragment implements CustomInterfaceClass {

    private RecyclerView rvAllBooks;
    private ProgressBar progBar;
    private UserDatabase database;
    private String isOwnLibrary = "1";
    private List<MyOwnBooksModel.ResModelData.MyBookList> mBookList = new ArrayList<>();
    private MyOwnBookAdapter adapter;
    private CustomInterfaceClass callback;
    private EditText searchBook;
    private FloatingActionButton fabAddBooks;
    private Context mContext;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_ind_books, container, false);

        rvAllBooks = (RecyclerView)view.findViewById(R.id.rvAllBooks);
        progBar = (ProgressBar)view.findViewById(R.id.progBar);
        searchBook = view.findViewById(R.id.et_search);
        fabAddBooks = (FloatingActionButton)view.findViewById(R.id.fabAddBooks);

        database = new UserDatabase(mContext);
        callback=this;
        getBookList();

        searchBook.setHint("Search book");
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

        return view;
    }

    private boolean searchBookList(String txt){
        if (mBookList.size() > 0) {
            final List<MyOwnBooksModel.ResModelData.MyBookList> filteredModelList = filter(mBookList, txt);
            adapter.setFilter(filteredModelList);
        }
        return true;
    }

    private List<MyOwnBooksModel.ResModelData.MyBookList> filter(List<MyOwnBooksModel.ResModelData.MyBookList> models, String search_txt) {

        search_txt = search_txt.toLowerCase();
        final List<MyOwnBooksModel.ResModelData.MyBookList> filteredModelList = new ArrayList<>();

        for (MyOwnBooksModel.ResModelData.MyBookList model : models) {
            final String textName = model.getBook_name().toLowerCase();
            //final String textNo = model.getAuthor_name().toLowerCase();
           // final String textISB = model.getAuthor_name().toLowerCase();
            if (textName.contains(search_txt)) {
                filteredModelList.add(model);
            }
        }
        return filteredModelList;
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
                        SharedPreferences sharedpreferences = getActivity().getSharedPreferences(CommunityLibraryFragment.MyPREFERENCES, Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putString("community_id","0");
                        editor.commit();
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

    private void getBookList() {
        progBar.setVisibility(View.VISIBLE);
        Holders holders = AllApiClass.getInstance().getApi();
        Call<MyOwnBooksModel> call = holders.getMyAllBooks(database.getUserId(),isOwnLibrary);
        call.enqueue(new Callback<MyOwnBooksModel>() {
            @Override
            public void onResponse(Call<MyOwnBooksModel> call, Response<MyOwnBooksModel> response) {
                if (response.isSuccessful()){
                    if (response.body().getResponse().getCode() == 1){
                        progBar.setVisibility(View.GONE);
                        mBookList = response.body().getResponse().getBook_list();
                        if (mBookList.size() > 0){
                            adapter = new MyOwnBookAdapter(mBookList, mContext);
                            LinearLayoutManager verticalManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
                            //verticalManager.setReverseLayout(true);
                            //verticalManager.setStackFromEnd(true);
                            rvAllBooks.setLayoutManager(verticalManager);
                            rvAllBooks.setAdapter(adapter);

                            adapter.setOnItemClickListener(new CommunityLibraryClickListenerTwo() {
                                @Override
                                public void onItemClick(int position) {
                                    Constants.myBookId = mBookList.get(position).getBook_id();
                                    MyCommunitiesDialog dialog = new MyCommunitiesDialog();
                                    dialog.newInstance(callback);
                                    dialog.show(getActivity().getSupportFragmentManager(),
                                            "community_dialog_fragment");
                                }
                            });

                        }
                    }else {
                        progBar.setVisibility(View.GONE);
                        Toast.makeText(mContext, ""+response.body().getResponse().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }else {
                    progBar.setVisibility(View.GONE);
                    Toast.makeText(mContext, "Something went wrong.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<MyOwnBooksModel> call, Throwable t) {
                progBar.setVisibility(View.GONE);
                Toast.makeText(mContext, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onAttach(@NonNull Context context) {
        mContext = context;
        super.onAttach(context);
    }

    @Override
    public void callbackMethod(String data) {
        if (!data.equals("") || data != null || !data.equals("null")){
            //Toast.makeText(mContext, ""+data, Toast.LENGTH_SHORT).show();
            getBookList();
        }
    }

}