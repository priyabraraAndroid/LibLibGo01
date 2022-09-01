package com.lib.liblibgo.activity.homedashboard.fragmentshome;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.lib.liblibgo.R;
import com.lib.liblibgo.activity.LoginWithPhoneNumber;
import com.lib.liblibgo.common.UserDatabase;

public class MyLibraryFragment extends Fragment implements TabLayout.OnTabSelectedListener{

    private TextView titleTool;
    private Context mContext;
    private UserDatabase database;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_library, container, false);
        titleTool = view.findViewById(R.id.titleTool);
        titleTool.setText("My Library Dashboard");

        database = new UserDatabase(mContext);

        tabLayout = (TabLayout)view.findViewById(R.id.tab_layout);

        tabLayout.addTab(tabLayout.newTab().setText("Community Library"));
        tabLayout.addTab(tabLayout.newTab().setText("Individual Library"));

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        tabLayout.setTabTextColors(
                getResources().getColor(R.color.white),
                getResources().getColor(R.color.black)
        );

        viewPager = (ViewPager)view.findViewById(R.id.viewPager);
        Pager adapter = new Pager(getChildFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        TabLayout.Tab tab = tabLayout.getTabAt(0);
        tab.select();
        viewPager.setCurrentItem(0);
        tabLayout.setOnTabSelectedListener(this);
        tabLayout.setupWithViewPager(viewPager);
        //viewPager.setCurrentItem(0);
        tabLayout.getTabAt(0).setText("Community Library");
        tabLayout.getTabAt(1).setText("Individual Library");

        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        mContext = context;
        super.onAttach(context);
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
                    //MyIndLibFragment tab1 = new MyIndLibFragment();
                    MyComLibFragment tab1 = new MyComLibFragment();
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

    @Override
    public void onResume() {
        if (HomeFragment.flag == 1){
            Pager adapter = new Pager(getChildFragmentManager(), tabLayout.getTabCount());
            viewPager.setAdapter(adapter);
            TabLayout.Tab tab = tabLayout.getTabAt(0);
            tab.select();
            viewPager.setCurrentItem(0);
            tabLayout.setTabTextColors(
                    getResources().getColor(R.color.white),
                    getResources().getColor(R.color.black)
            );
            //viewPager.setCurrentItem(0);
            tabLayout.getTabAt(0).setText("Community Library");
            tabLayout.getTabAt(1).setText("Individual Library");
            HomeFragment.flag = 0;
        }
        super.onResume();
    }
}