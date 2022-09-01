package com.lib.liblibgo.dashboard.book_upload;

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
import com.lib.liblibgo.activity.SearchActivity;
import com.lib.liblibgo.activity.homedashboard.fragmentshome.CommonFragment;
import com.lib.liblibgo.dashboard.book_details.BookCatActivity;
import com.lib.liblibgo.dashboard.book_details.fragments.CommunityBooksFrag;
import com.lib.liblibgo.dashboard.book_upload.fragments.BulkBooksFragment;
import com.lib.liblibgo.dashboard.book_upload.fragments.IndividualBookFragment;
import com.lib.liblibgo.dashboard.book_upload.fragments.StackBookFragment;

public class UploadBookActivity extends AppCompatActivity implements TabLayout.OnTabSelectedListener{

    private TextView titleTool;
    private ImageView backTool;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_book);

        titleTool = findViewById(R.id.titleTool);
        backTool = findViewById(R.id.backTool);

        setTopView(getString(R.string.book_upload));

        tabLayout = (TabLayout)findViewById(R.id.tab_layout);

        tabLayout.addTab(tabLayout.newTab().setText("Individual"));
        tabLayout.addTab(tabLayout.newTab().setText("Stack"));
        tabLayout.addTab(tabLayout.newTab().setText("List"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        tabLayout.setTabTextColors(
                getResources().getColor(R.color.white),
                getResources().getColor(R.color.black)
        );

        viewPager = (ViewPager)findViewById(R.id.viewPager);
        Pager adapter = new Pager(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        /*TabLayout.Tab tab = tabLayout.getTabAt(0);
        tab.select();*/
        //viewPager.setCurrentItem(0);
        tabLayout.setOnTabSelectedListener(this);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(0).setText("Individual");
        tabLayout.getTabAt(1).setText("Stack");
        tabLayout.getTabAt(2).setText("List");

        bundle = new Bundle();
        bundle.putString("name", getIntent().getStringExtra("name"));
        bundle.putString("author", getIntent().getStringExtra("author"));
        bundle.putString("isbn", getIntent().getStringExtra("isbn"));
        bundle.putString("description", getIntent().getStringExtra("description"));
        bundle.putString("imgUrl", getIntent().getStringExtra("imgUrl"));
        bundle.putString("rental_price", getIntent().getStringExtra("rental_price"));
        bundle.putString("rental_price", getIntent().getStringExtra("rental_price"));
        bundle.putString("rental_duration", getIntent().getStringExtra("rental_duration"));
        bundle.putString("book_price", getIntent().getStringExtra("book_price"));
        bundle.putString("category_id", getIntent().getStringExtra("category_id"));
        bundle.putString("category_name", getIntent().getStringExtra("category_name"));
        bundle.putString("book_id", getIntent().getStringExtra("book_id"));
        bundle.putString("mrp", getIntent().getStringExtra("mrp"));
        bundle.putString("quantity", getIntent().getStringExtra("quantity"));
        bundle.putString("sale_type", getIntent().getStringExtra("sale_type"));
        bundle.putString("book_condition", getIntent().getStringExtra("book_condition"));
        bundle.putString("security_money", getIntent().getStringExtra("security_money"));
        bundle.putString("giveaway_status", getIntent().getStringExtra("giveaway_status"));



    }

    private void setTopView(String string) {
        titleTool.setText(string);
        backTool.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
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
                    IndividualBookFragment tab1 = new IndividualBookFragment();
                    tab1.setArguments(bundle);
                    return tab1;
                case 1:
                    StackBookFragment tab2 = new StackBookFragment();
                    return tab2;
                case 2:
                    BulkBooksFragment tab3 = new BulkBooksFragment();
                    return tab3;
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
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }
}