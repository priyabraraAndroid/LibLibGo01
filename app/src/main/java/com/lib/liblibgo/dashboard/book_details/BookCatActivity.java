package com.lib.liblibgo.dashboard.book_details;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
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
import com.google.android.material.tabs.TabLayout;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.lib.liblibgo.R;
import com.lib.liblibgo.activity.LoginWithPhoneNumber;
import com.lib.liblibgo.activity.SearchActivity;
import com.lib.liblibgo.activity.homedashboard.fragmentshome.CommonFragment;
import com.lib.liblibgo.activity.homedashboard.fragmentshome.HomeFragment;
import com.lib.liblibgo.activity.homedashboard.fragmentshome.MyLibraryFragment;
import com.lib.liblibgo.adapter.CategoryAdapter;
import com.lib.liblibgo.adapter.NearMeBookAdapter;
import com.lib.liblibgo.api.AllApiClass;
import com.lib.liblibgo.api.Holders;
import com.lib.liblibgo.common.Constants;
import com.lib.liblibgo.common.UserCurrentLocation;
import com.lib.liblibgo.common.UserDatabase;
import com.lib.liblibgo.dashboard.apartment_admin.UploadBookDetails;
import com.lib.liblibgo.dashboard.book_details.fragments.CommunityBooksFrag;
import com.lib.liblibgo.dashboard.book_details.fragments.OwnBooksFrag;
import com.lib.liblibgo.dashboard.library.CreateLibraryActivity;
import com.lib.liblibgo.dashboard.library.LibraryActivity;
import com.lib.liblibgo.dashboard.library.fragments.CommunityLibraryFragment;
import com.lib.liblibgo.dashboard.profile.fragments.IssuedListFrag;
import com.lib.liblibgo.dashboard.profile.fragments.MyOrderFragment;
import com.lib.liblibgo.dashboard.trackingbooks.TrackMyBooksActivity;
import com.lib.liblibgo.dashboard.trackingbooks.fragments.MyComBooksFragment;
import com.lib.liblibgo.dashboard.trackingbooks.fragments.MyIndBooksFragment;
import com.lib.liblibgo.dialogs.CategoriFilterDialog;
import com.lib.liblibgo.model.CategoryListData;
import com.lib.liblibgo.model.CategoryModel;
import com.lib.liblibgo.model.NearMeBookModel;
import com.lib.liblibgo.model.NearMeFilterModel;
import com.lib.liblibgo.model.subadmin.LibraryStatusModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookCatActivity extends AppCompatActivity implements TabLayout.OnTabSelectedListener{
    private TextView titleTool;
    private ImageView backTool;
    private ImageView ivSearch;

    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_cat);

        titleTool = findViewById(R.id.titleTool);
        backTool = findViewById(R.id.backTool);
        ivSearch = findViewById(R.id.ivSearch);
        setTopView(getString(R.string.categories));

        tabLayout = (TabLayout)findViewById(R.id.tab_layout);

        tabLayout.addTab(tabLayout.newTab().setText("Individual Books"));
        tabLayout.addTab(tabLayout.newTab().setText("Community Books"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        tabLayout.setTabTextColors(
                getResources().getColor(R.color.white),
                getResources().getColor(R.color.black)
        );

        viewPager = (ViewPager)findViewById(R.id.viewPager);
        Pager adapter = new Pager(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        TabLayout.Tab tab = tabLayout.getTabAt(1);
        tab.select();
        viewPager.setCurrentItem(1);
        tabLayout.setOnTabSelectedListener(this);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(0).setText("Individual Books");
        tabLayout.getTabAt(1).setText("Community Books");

        inflateFragment();

    }

    private void inflateFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = new CommunityBooksFrag();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.content, fragment);
        fragmentTransaction.commit();
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

        ivSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BookCatActivity.this, SearchActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        Constants.sortingValueCommunity = "";
        super.onBackPressed();
    }

    public class Pager extends FragmentStatePagerAdapter {

        int tabCount;

        public Pager(FragmentManager fm, int tabCount) {
            super(fm);
            this.tabCount = tabCount;
        }

        @Override
        public Fragment getItem(int position) {
            //Returning the current tabs
            switch (position) {
                case 0:
                    CommonFragment tab1 = new CommonFragment();
                    return tab1;
                case 1:
                    CommunityBooksFrag tab2 = new CommunityBooksFrag();
                    return tab2;
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return tabCount;
        }
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        viewPager.setCurrentItem(tab.getPosition());
        /*if (tab.getPosition() == 0){
            ivSearch.setVisibility(View.INVISIBLE);
            //Constants.searchFrom = "individual";
        }else {
            //Constants.searchFrom = "community";
            ivSearch.setVisibility(View.VISIBLE);
        }*/
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    @Override
    public void onResume() {
        if (HomeFragment.flag == 1){
            Pager adapter = new Pager(getSupportFragmentManager(), tabLayout.getTabCount());
            viewPager.setAdapter(adapter);
            TabLayout.Tab tab = tabLayout.getTabAt(1);
            tab.select();
            viewPager.setCurrentItem(1);
            tabLayout.setOnTabSelectedListener(this);
            tabLayout.setupWithViewPager(viewPager);
            tabLayout.getTabAt(0).setText("Individual Books");
            tabLayout.getTabAt(1).setText("Community Books");
            HomeFragment.flag = 0;
            inflateFragment();
        }
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        Constants.selectedCategories = "";
        super.onDestroy();
    }
}