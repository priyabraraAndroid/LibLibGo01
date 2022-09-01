package com.lib.liblibgo.activity.homedashboard.fragmentshome;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.JsonObject;
import com.lib.liblibgo.R;
import com.lib.liblibgo.activity.LibCoinWithdrawActivity;
import com.lib.liblibgo.activity.LoginWithPhoneNumber;
import com.lib.liblibgo.activity.UpdateUserProfile;
import com.lib.liblibgo.activity.UserProfileActivity;
import com.lib.liblibgo.activity.homedashboard.HomeActivity;
import com.lib.liblibgo.api.AllApiClass;
import com.lib.liblibgo.api.Holders;
import com.lib.liblibgo.common.Constants;
import com.lib.liblibgo.common.UserDatabase;

import org.json.JSONException;
import org.json.JSONObject;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.lib.liblibgo.activity.homedashboard.fragmentshome.OrderFragment.MyPREFERENCES;

public class MyAccountFragment extends Fragment implements TabLayout.OnTabSelectedListener{

    private UserDatabase database;
    private Context mContext;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private CircleImageView iv_user;
    private TextView tvName,tvLibCoins,tvRedeem;
    private RelativeLayout rlLoginUser;
    private TextView tvLogin;
    private LinearLayout llLogin;
    private SharedPreferences sharedpreferences;
    private SharedPreferences.Editor editor;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_account, container, false);

        //titleTool = view.findViewById(R.id.titleTool);
        //titleTool.setText("My Account");

        sharedpreferences = getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();

        database = new UserDatabase(mContext);

        tabLayout = (TabLayout)view.findViewById(R.id.tab_layout);
        rlLoginUser = (RelativeLayout)view.findViewById(R.id.rlLoginUser);
        tvLogin = (TextView)view.findViewById(R.id.tvLogin);
        llLogin = (LinearLayout)view.findViewById(R.id.llLogin);

        tabLayout.addTab(tabLayout.newTab().setText("Order History"));
        tabLayout.addTab(tabLayout.newTab().setText("Transaction History"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        tabLayout.setTabTextColors(
                getResources().getColor(R.color.white),
                getResources().getColor(R.color.black)
        );

        viewPager = (ViewPager)view.findViewById(R.id.viewPager);
        Pager adapter = new Pager(getChildFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        tabLayout.setOnTabSelectedListener(this);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setCurrentItem(0);
        tabLayout.getTabAt(0).setText("Order History");
        tabLayout.getTabAt(1).setText("Transaction History");

        iv_user = (CircleImageView)view.findViewById(R.id.iv_user);
        tvName = (TextView)view.findViewById(R.id.tvName);
        tvLibCoins = (TextView)view.findViewById(R.id.tvLibCoins);
        tvRedeem = (TextView)view.findViewById(R.id.tvRedeem);

        if (database.getUserId().equals("")){
            rlLoginUser.setVisibility(View.GONE);
            llLogin.setVisibility(View.VISIBLE);
            iv_user.setVisibility(View.GONE);
        }else {
            rlLoginUser.setVisibility(View.VISIBLE);
            llLogin.setVisibility(View.GONE);
            iv_user.setVisibility(View.GONE);
            getUserDetails();
        }

        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentHome = new Intent(mContext, LoginWithPhoneNumber.class);
                startActivity(intentHome);
            }
        });

        iv_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentHome = new Intent(mContext, UserProfileActivity.class);
                startActivity(intentHome);
            }
        });

        return view;
    }

    private void getUserDetails() {
        final ProgressDialog progressBar = new ProgressDialog(getContext());
        progressBar.setMessage("Please wait...");
        progressBar.setCancelable(false);
        progressBar.show();
        Holders holders = AllApiClass.getInstance().getApi();
        Call<JsonObject> call = holders.userDetails(database.getUserId());
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()){
                    try {
                        progressBar.dismiss();
                        JSONObject jsonObject = new JSONObject(response.body().toString());
                        JSONObject object = jsonObject.getJSONObject("response");
                        if (object.getString("code").equals("1")){

                            String apartmentName = object.getString("apartment_name");
                            String flatId = object.getString("flat_no");
                            // String adr = object.getString("address");

                            database.createLogin(database.getUserId(),
                                    object.getString("username"),
                                    object.getString("mobile"),object.getString("pincode"),
                                    object.getString("area_name")+","+object.getString("landmark")+","+object.getString("city")+","+object.getString("state")+","+object.getString("pincode"),
                                    apartmentName,flatId,object.getString("email"));

                            Constants.myLibCoins = object.getString("libcoins");
                            Constants.myProfileImage = object.getString("profile_image");

                            tvName.setText(object.getString("username"));
                            tvLibCoins.setText("Lib Coins - "+Constants.myLibCoins);

                            if (Constants.myProfileImage.equals("")){
                                Glide.with(mContext).load(R.drawable.no_img).placeholder(R.drawable.no_img).into(iv_user);
                            }else {
                                Glide.with(mContext).load(Constants.myProfileImage).placeholder(R.drawable.no_img).into(iv_user);
                            }

                            tvRedeem.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if (!database.getUserId().equals("")){
                                        if (Integer.parseInt(Constants.myLibCoins) >= 1000){
                                            Intent intent = new Intent(mContext, LibCoinWithdrawActivity.class);
                                            intent.putExtra("libcoins",Constants.myLibCoins);
                                            startActivity(intent);
                                        }else {
                                            Toast.makeText(mContext, "Need 1000 or above LibCoins to redeem.", Toast.LENGTH_SHORT).show();
                                        }
                                    }else {
                                        Intent intentHome = new Intent(mContext, LoginWithPhoneNumber.class);
                                        startActivity(intentHome);
                                    }
                                }
                            });


                        }else {
                            progressBar.dismiss();
                            Toast.makeText(getContext(), ""+object.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else {
                    progressBar.dismiss();
                    Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                progressBar.dismiss();
                Toast.makeText(getContext(), ""+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
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
                    OrderFragment tab1 = new OrderFragment();
                    return tab1;
                case 1:
                    TransactionFragment tab2 = new TransactionFragment();
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
            if (database.getUserId().equals("")){
                rlLoginUser.setVisibility(View.GONE);
                llLogin.setVisibility(View.VISIBLE);
            }else {
                rlLoginUser.setVisibility(View.VISIBLE);
                llLogin.setVisibility(View.GONE);
                getUserDetails();
                HomeFragment.flag = 0;
            }
        }
        super.onResume();
    }

    @Override
    public void onStop() {
        editor.clear();
        editor.commit();
        super.onStop();
    }
}