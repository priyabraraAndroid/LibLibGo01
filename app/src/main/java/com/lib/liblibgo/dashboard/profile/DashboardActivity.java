package com.lib.liblibgo.dashboard.profile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.google.android.material.tabs.TabLayout;
import com.lib.liblibgo.R;
import com.lib.liblibgo.dashboard.profile.fragments.IssuedListFrag;
import com.lib.liblibgo.dashboard.profile.fragments.MyOrderFragment;
import com.lib.liblibgo.dashboard.profile.fragments.ReturnListFrag;
import com.lib.liblibgo.dashboard.profile.fragments.UnreviewedListFrag;
import com.lib.liblibgo.views.MyTextView;

public class DashboardActivity extends AppCompatActivity implements TabLayout.OnTabSelectedListener{
    private MyTextView titleTool;
    private ImageView backTool;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        titleTool = findViewById(R.id.titleTool);
        backTool = findViewById(R.id.backTool);
        setTopView(getString(R.string.info_dashboard));

        tabLayout = (TabLayout) findViewById(R.id.tab_layout);

        tabLayout.addTab(tabLayout.newTab().setText("My Order"));
        tabLayout.addTab(tabLayout.newTab().setText("Issued Books"));
        tabLayout.addTab(tabLayout.newTab().setText("Returned Books"));
        tabLayout.addTab(tabLayout.newTab().setText("Unreviewed Books"));
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
        tabLayout.getTabAt(0).setText("My Order");
        tabLayout.getTabAt(1).setText("Issued Books");
        tabLayout.getTabAt(2).setText("Returned Books");
        tabLayout.getTabAt(3).setText("Unreviewed Books");


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
                    MyOrderFragment tab1 = new MyOrderFragment();
                    return tab1;
                case 1:
                    IssuedListFrag tab2 = new IssuedListFrag();
                    return tab2;
                case 2:
                    ReturnListFrag tab3 = new ReturnListFrag();
                    return tab3;
                case 3:
                    UnreviewedListFrag tab4 = new UnreviewedListFrag();
                    return tab4;
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