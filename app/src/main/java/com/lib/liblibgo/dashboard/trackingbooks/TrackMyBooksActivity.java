package com.lib.liblibgo.dashboard.trackingbooks;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.lib.liblibgo.R;
import com.lib.liblibgo.activity.homedashboard.fragmentshome.CommonFragment;
import com.lib.liblibgo.dashboard.library.SearchLibraryActivity;
import com.lib.liblibgo.dashboard.library.fragments.AllLibraryFragment;
import com.lib.liblibgo.dashboard.library.fragments.CommunityLibraryFragment;
import com.lib.liblibgo.dashboard.trackingbooks.fragments.MyComBooksFragment;
import com.lib.liblibgo.dashboard.trackingbooks.fragments.MyIndBooksFragment;

public class TrackMyBooksActivity extends AppCompatActivity implements TabLayout.OnTabSelectedListener{
    private TextView titleTool;
    private ImageView backTool;
    private ImageView ivSearch;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_my_books);

        titleTool = findViewById(R.id.titleTool);
        backTool = findViewById(R.id.backTool);
        ivSearch = findViewById(R.id.ivSearch);
        setTopView(getString(R.string.my_books_track));

        tabLayout = (TabLayout)findViewById(R.id.tab_layout);

        tabLayout.addTab(tabLayout.newTab().setText("My Community Books"));
        tabLayout.addTab(tabLayout.newTab().setText("My Individual Books"));

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        tabLayout.setTabTextColors(
                getResources().getColor(R.color.white),
                getResources().getColor(R.color.black)
        );

        viewPager = (ViewPager)findViewById(R.id.viewPager);
        Pager adapter = new Pager(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        TabLayout.Tab tab = tabLayout.getTabAt(0);
        tab.select();
        viewPager.setCurrentItem(0);
        tabLayout.setOnTabSelectedListener(this);
        tabLayout.setupWithViewPager(viewPager);
        //viewPager.setCurrentItem(0);
        tabLayout.getTabAt(0).setText("My Community Books");
        tabLayout.getTabAt(1).setText("My Individual Books");


    }

    private void setTopView(String string) {
        titleTool.setText(string);
        ivSearch.setVisibility(View.INVISIBLE);
        backTool.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                finish();
            }
        });

        ivSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TrackMyBooksActivity.this, SearchLibraryActivity.class);
                startActivity(intent);
            }
        });
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
                    //MyIndBooksFragment tab1 = new MyIndBooksFragment();
                    MyComBooksFragment tab1 = new MyComBooksFragment();
                    return tab1;
                case 1:
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
}