package com.lib.liblibgo.dashboard.library;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
import com.lib.liblibgo.activity.homedashboard.fragmentshome.CommonFragment;
import com.lib.liblibgo.activity.homedashboard.fragmentshome.HomeFragment;
import com.lib.liblibgo.adapter.LibraryAdapter;
import com.lib.liblibgo.api.AllApiClass;
import com.lib.liblibgo.api.Holders;
import com.lib.liblibgo.common.UserCurrentLocation;
import com.lib.liblibgo.common.UserDatabase;
import com.lib.liblibgo.dashboard.book_details.BookCatActivity;
import com.lib.liblibgo.dashboard.library.fragments.AllLibraryFragment;
import com.lib.liblibgo.dashboard.library.fragments.CommunityLibraryFragment;
import com.lib.liblibgo.model.NearMeLibraryModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LibraryActivity extends AppCompatActivity implements TabLayout.OnTabSelectedListener{
    private TextView titleTool;
    private ImageView backTool;
    //private ImageView ivSearch;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    /*private RecyclerView rvAllLibrary;
    private RecyclerView rvNearMeLibrary;
    private ProgressBar progBar,progNearMe;

    private List<NearMeLibraryModel.ResModelData.NewrmeLibraryList> libList = new ArrayList<>();
    //private List<NearMeLibraryModel.ResModelData.NewrmeLibraryList> nearmeList = new ArrayList<>();
    private LibraryAdapter adapter;
    //private NearByLibraryAdapter nearMeAdapter;
    private RelativeLayout rlLocation;
    private TextView tvRange;
    private PopupWindow popupWindow;
    String apartment = "";
    String city = "";
    String area = "";
    private UserCurrentLocation location;
    private UserDatabase database;
    private View ivSearch;
    private FloatingActionButton fabAddLibrary;*/
    public static final String MyPREFERENCES = "MyPrefsss";
    private SharedPreferences.Editor editor;
    private SharedPreferences sharedpreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library);

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();

        titleTool = findViewById(R.id.titleTool);
        backTool = findViewById(R.id.backTool);
        //ivSearch = findViewById(R.id.ivSearch);
        setTopView(getString(R.string.libraries));

        tabLayout = (TabLayout)findViewById(R.id.tab_layout);

        tabLayout.addTab(tabLayout.newTab().setText("Community Library"));
        tabLayout.addTab(tabLayout.newTab().setText("Individual Library"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        tabLayout.setTabTextColors(
                getResources().getColor(R.color.white),
                getResources().getColor(R.color.black)
        );

        viewPager = (ViewPager)findViewById(R.id.viewPager);
        Pager adapter = new Pager(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        tabLayout.setOnTabSelectedListener(this);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setCurrentItem(0);
        tabLayout.getTabAt(0).setText("Community Library");
        tabLayout.getTabAt(1).setText("Individual Library");

        inflateFragment();

    }

    private void inflateFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = new CommunityLibraryFragment();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.content, fragment);
        //fragmentTransaction.addToBackStack(null);
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

        /*ivSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LibraryActivity.this,SearchLibraryActivity.class);
                startActivity(intent);
            }
        });*/

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
                    CommunityLibraryFragment tab1 = new CommunityLibraryFragment();
                    return tab1;
                case 1:
                    //AllLibraryFragment tab2 = new AllLibraryFragment();
                    CommonFragment tab2 = new CommonFragment();
                    return tab2;
                default:
                    return null;
            }
        }

        //Overriden method getCount to get the number of tabs
        @Override
        public int getCount() {
            return tabCount;
        }
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    @Override
    protected void onDestroy() {
        editor.clear();
        editor.commit();
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        if (HomeFragment.flag == 1){
            inflateFragment();
            HomeFragment.flag = 0;
        }
        super.onResume();
    }
}