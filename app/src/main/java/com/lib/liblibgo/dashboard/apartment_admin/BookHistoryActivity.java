package com.lib.liblibgo.dashboard.apartment_admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.google.android.material.tabs.TabLayout;
import com.lib.liblibgo.R;
import com.lib.liblibgo.common.Constants;
import com.lib.liblibgo.dashboard.profile.fragments.BookHistoryFrag;
import com.lib.liblibgo.dashboard.profile.fragments.CutomerBookHistoryFrag;
import com.lib.liblibgo.dashboard.profile.fragments.IssuedListFrag;
import com.lib.liblibgo.dashboard.profile.fragments.LibraryOrderFragment;
import com.lib.liblibgo.views.MyTextView;

public class BookHistoryActivity extends AppCompatActivity implements TabLayout.OnTabSelectedListener{
    private MyTextView titleTool;
    private ImageView backTool;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private NestedScrollView srclView;
    private FrameLayout content1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_history);

        titleTool = findViewById(R.id.titleTool);
        backTool = findViewById(R.id.backTool);
        setTopView(getString(R.string.info_book_history));

        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        srclView = (NestedScrollView) findViewById(R.id.srclView);
        content1 = (FrameLayout) findViewById(R.id.content1);

        if (Constants.isOwnLibrary == true){
            /*tabLayout.addTab(tabLayout.newTab().setText("My Orders"));
            viewPager = (ViewPager) findViewById(R.id.viewPager);
            PagerNew adapter = new PagerNew(getSupportFragmentManager(), tabLayout.getTabCount());
            viewPager.setAdapter(adapter);
            tabLayout.setOnTabSelectedListener(this);
            tabLayout.setupWithViewPager(viewPager);
            viewPager.setCurrentItem(0);
            tabLayout.getTabAt(0).setText("My Orders");*/
            srclView.setVisibility(View.GONE);
            content1.setVisibility(View.VISIBLE);

            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            LibraryOrderFragment fragment = new LibraryOrderFragment();
            fragmentTransaction.replace(R.id.content1, fragment);
            fragmentTransaction.commit();

        }else {

            srclView.setVisibility(View.VISIBLE);
            content1.setVisibility(View.GONE);

            tabLayout.addTab(tabLayout.newTab().setText("Book History"));
            tabLayout.addTab(tabLayout.newTab().setText("Customer Book History"));

            tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

            tabLayout.setTabTextColors(
                    getResources().getColor(R.color.rect_color),
                    getResources().getColor(R.color.colorPrimaryDark)
            );

            viewPager = (ViewPager) findViewById(R.id.viewPager);
            Pager adapter = new Pager(getSupportFragmentManager(), tabLayout.getTabCount());
            viewPager.setAdapter(adapter);
            tabLayout.setOnTabSelectedListener(this);
            tabLayout.setupWithViewPager(viewPager);
            viewPager.setCurrentItem(0);
            tabLayout.getTabAt(0).setText("Book History");
            tabLayout.getTabAt(1).setText("Customer Book History");

        }


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
    }

    public class Pager extends FragmentStatePagerAdapter {

        //integer to count number of tabs
        int tabCount;

        //Constructor to the class
        public Pager(FragmentManager fm, int tabCount) {
            super(fm);
            //Initializing tab count
            this.tabCount = tabCount;
        }

        //Overriding method getItem
        @Override
        public Fragment getItem(int position) {
            //Returning the current tabs
            switch (position) {
                case 0:
                    BookHistoryFrag tab1 = new BookHistoryFrag();
                    return tab1;
                case 1:
                    CutomerBookHistoryFrag tab2 = new CutomerBookHistoryFrag();
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

    public class PagerNew extends FragmentStatePagerAdapter {

        //integer to count number of tabs
        int tabCount;

        //Constructor to the class
        public PagerNew(FragmentManager fm, int tabCount) {
            super(fm);
            //Initializing tab count
            this.tabCount = tabCount;
        }

        //Overriding method getItem
        @Override
        public Fragment getItem(int position) {
            //Returning the current tabs
            switch (position) {
                case 0:
                    LibraryOrderFragment tab1 = new LibraryOrderFragment();
                    return tab1;
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