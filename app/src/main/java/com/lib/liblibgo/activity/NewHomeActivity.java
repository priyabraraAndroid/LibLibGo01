package com.lib.liblibgo.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.lib.liblibgo.R;
import com.lib.liblibgo.api.AllApiClass;
import com.lib.liblibgo.api.Holders;
import com.lib.liblibgo.common.Constants;
import com.lib.liblibgo.common.UserDatabase;
import com.lib.liblibgo.dashboard.apartment_admin.SingUpApartmentAdminActivity;
import com.lib.liblibgo.dashboard.apartment_admin.SubAdminDashboard;
import com.lib.liblibgo.dashboard.book_return.ReturnBookActivity;
import com.lib.liblibgo.dashboard.profile.DashboardActivity;
import com.lib.liblibgo.dashboard.return_history.ReturnHistoryActivity;
import com.lib.liblibgo.model.SendRequestStatusModel;
import com.lib.liblibgo.model.SubmitDataResModel;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewHomeActivity extends AppCompatActivity {

    ImageView mMenu;
    private DrawerLayout mDrawer;
    private RelativeLayout rl_no_user;
    private RelativeLayout rl_user_info;
    private CircleImageView iv_user;
    private TextView tv_user_name;
    private TextView tv_user_email;
    private TextView tv_user_adr;
    private RelativeLayout rl_home;
    private RelativeLayout rl_manage_library;
    private RelativeLayout rl_return_books;
    private RelativeLayout rl_return_history;
    private RelativeLayout rl_dashboard;
    private RelativeLayout rl_contact_us;
    private RelativeLayout rl_about_us;
    private RelativeLayout rl_privacy_policy;
    private RelativeLayout rl_term_condition;
    private RelativeLayout rl_logout;
    private UserDatabase mUserDataBase;
    private View view_last;
    private Intent intentHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_home);

        mUserDataBase = new UserDatabase(this);

        mMenu = findViewById(R.id.ic_menu);
        mDrawer = (DrawerLayout)findViewById(R.id.drawer);

        mMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDrawer.openDrawer(Gravity.LEFT);
            }
        });

        rl_no_user = (RelativeLayout)findViewById(R.id.rl_no_user);
        rl_user_info = (RelativeLayout)findViewById(R.id.rl_user_info);
        rl_home = (RelativeLayout)findViewById(R.id.rl_home);
        rl_manage_library = (RelativeLayout)findViewById(R.id.rl_manage_library);
        rl_return_books = (RelativeLayout)findViewById(R.id.rl_return_books);
        rl_return_history = (RelativeLayout)findViewById(R.id.rl_return_history);
        rl_dashboard = (RelativeLayout)findViewById(R.id.rl_dashboard);
        rl_contact_us = (RelativeLayout)findViewById(R.id.rl_contact_us);
        rl_about_us = (RelativeLayout)findViewById(R.id.rl_about_us);
        rl_privacy_policy = (RelativeLayout)findViewById(R.id.rl_privacy_policy);
        rl_term_condition = (RelativeLayout)findViewById(R.id.rl_term_condition);
        rl_logout = (RelativeLayout)findViewById(R.id.rl_logout);
        view_last = (View)findViewById(R.id.view_last);

        iv_user = (CircleImageView)findViewById(R.id.iv_user);
        tv_user_name = (TextView)findViewById(R.id.tv_user_name);
        tv_user_email = (TextView)findViewById(R.id.tv_user_email);
        tv_user_adr = (TextView)findViewById(R.id.tv_user_adr);

        if (mUserDataBase.getUserId().equals("")){
            rl_no_user.setVisibility(View.VISIBLE);
            rl_user_info.setVisibility(View.GONE);
            rl_logout.setVisibility(View.GONE);
            view_last.setVisibility(View.GONE);
        }else {
            rl_no_user.setVisibility(View.GONE);
            rl_user_info.setVisibility(View.VISIBLE);
            rl_logout.setVisibility(View.VISIBLE);
            view_last.setVisibility(View.VISIBLE);

            tv_user_name.setText(mUserDataBase.getNAME());

            if (mUserDataBase.getEMAIL().equals("")){
                tv_user_email.setText(mUserDataBase.getPHONE());
            }else {
                tv_user_email.setText(mUserDataBase.getEMAIL());
            }
        }

        rl_no_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NewHomeActivity.this,LoginWithPhoneNumber.class);
                startActivity(intent);
            }
        });

        rl_user_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NewHomeActivity.this,UpdateUserProfile.class);
                startActivity(intent);
            }
        });

        rl_about_us.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NewHomeActivity.this,PrivacyPolicyActivity.class);
                intent.putExtra("value","1");
                startActivity(intent);
            }
        });

        rl_privacy_policy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NewHomeActivity.this,PrivacyPolicyActivity.class);
                intent.putExtra("value","2");
                startActivity(intent);
            }
        });

        rl_term_condition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NewHomeActivity.this,PrivacyPolicyActivity.class);
                intent.putExtra("value","3");
                startActivity(intent);
            }
        });

        rl_contact_us.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NewHomeActivity.this,ContactUsActivity.class);
                startActivity(intent);
            }
        });

        rl_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userLogOut();
            }
        });

        rl_manage_library.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mUserDataBase.getUserId().equals("")){
                    Intent intent = new Intent(NewHomeActivity.this,LoginWithPhoneNumber.class);
                    startActivity(intent);
                }else {
                    manageLibrary();
                }
            }
        });

        rl_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDrawer.closeDrawer(Gravity.LEFT);
            }
        });

        rl_dashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mUserDataBase.getUserId().equals("")){
                    intentHome = new Intent(NewHomeActivity.this,LoginWithPhoneNumber.class);
                    startActivity(intentHome);
                }else {
                    intentHome = new Intent(NewHomeActivity.this, DashboardActivity.class);
                    startActivity(intentHome);
                }
            }
        });

        rl_return_books.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mUserDataBase.getUserId().equals("")){
                    intentHome = new Intent(NewHomeActivity.this,LoginWithPhoneNumber.class);
                    startActivity(intentHome);
                }else {
                    intentHome = new Intent(NewHomeActivity.this, ReturnBookActivity.class);
                    startActivity(intentHome);
                }
            }
        });

        rl_return_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mUserDataBase.getUserId().equals("")){
                    intentHome = new Intent(NewHomeActivity.this,LoginWithPhoneNumber.class);
                    startActivity(intentHome);
                }else {
                    intentHome = new Intent(NewHomeActivity.this, ReturnHistoryActivity.class);
                    startActivity(intentHome);
                }
            }
        });
    }

    private void manageLibrary() {
        final ProgressDialog progressBar = new ProgressDialog(NewHomeActivity.this);
        progressBar.setMessage("Please wait...");
        progressBar.setCancelable(false);
        progressBar.show();
        Holders holders = AllApiClass.getInstance().getApi();
        Call<SendRequestStatusModel> resCall = holders.checkSendRequestStatus(mUserDataBase.getUserId());
        resCall.enqueue(new Callback<SendRequestStatusModel>() {
            @Override
            public void onResponse(Call<SendRequestStatusModel> call, Response<SendRequestStatusModel> response) {
                if (response.isSuccessful()){
                    if (response.body().getResponse().getCode().equals("1")){
                        progressBar.dismiss();
                        if (response.body().getResponse().getRequest_status().equals("1")){
                            intentHome = new Intent(NewHomeActivity.this, SubAdminDashboard.class);
                            intentHome.putExtra("apart_id", response.body().getResponse().getApartment_id());
                            intentHome.putExtra("apart_name", response.body().getResponse().getApartment_name());
                            Constants.USER_APARTMENT_NAME = response.body().getResponse().getApartment_name();
                            Constants.USER_APARTMENT_ID = response.body().getResponse().getApartment_id();
                            startActivity(intentHome);
                        }else {
                            intentHome = new Intent(NewHomeActivity.this, SingUpApartmentAdminActivity.class);
                            intentHome.putExtra("request_status", response.body().getResponse().getRequest_status());
                            startActivity(intentHome);
                        }
                    }else {
                        progressBar.dismiss();
                        Toast.makeText(NewHomeActivity.this, ""+response.body().getResponse().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }else {
                    progressBar.dismiss();
                    Toast.makeText(NewHomeActivity.this, "Something went wrong !", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<SendRequestStatusModel> call, Throwable t) {
                progressBar.dismiss();
                Toast.makeText(NewHomeActivity.this, "Check your internet !", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void userLogOut() {
        final ProgressDialog loginDialog = new ProgressDialog(NewHomeActivity.this);
        loginDialog.setMessage("Please wait..");
        loginDialog.setCancelable(false);
        loginDialog.show();
        Holders holders = AllApiClass.getInstance().getApi();
        Call<SubmitDataResModel> call = holders.userLogout(mUserDataBase.getUserId());
        call.enqueue(new Callback<SubmitDataResModel>() {
            @Override
            public void onResponse(Call<SubmitDataResModel> call, Response<SubmitDataResModel> response) {
                if (response.isSuccessful()){
                    if (response.body().getResponse().getCode() == 1){
                        loginDialog.dismiss();
                        mUserDataBase.clearSession();
                        LoginManager.getInstance().logOut();
                        GoogleSignInOptions gso = new GoogleSignInOptions.
                                Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).
                                build();
                        GoogleSignInClient googleSignInClient= GoogleSignIn.getClient(NewHomeActivity.this,gso);
                        googleSignInClient.signOut();
                        Intent intent = new Intent(NewHomeActivity.this, LoginWithPhoneNumber.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }else {
                        loginDialog.dismiss();
                        Toast.makeText(NewHomeActivity.this, response.body().getResponse().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }else {
                    loginDialog.dismiss();
                    Toast.makeText(NewHomeActivity.this, "Something went wrong !", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<SubmitDataResModel> call, Throwable t) {
                loginDialog.dismiss();
                Toast.makeText(NewHomeActivity.this, "Check Your Internet Connection.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}