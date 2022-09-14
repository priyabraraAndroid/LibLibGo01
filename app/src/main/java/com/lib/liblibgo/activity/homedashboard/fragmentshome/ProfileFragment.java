package com.lib.liblibgo.activity.homedashboard.fragmentshome;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.gson.JsonObject;
import com.lib.liblibgo.R;
import com.lib.liblibgo.activity.ContactUsActivity;
import com.lib.liblibgo.activity.LibCoinWithdrawActivity;
import com.lib.liblibgo.activity.LoginWithPhoneNumber;
import com.lib.liblibgo.activity.PrivacyPolicyActivity;
import com.lib.liblibgo.activity.SettingsActivity;
import com.lib.liblibgo.activity.UserProfileActivity;
import com.lib.liblibgo.activity.homedashboard.HomeActivity;
import com.lib.liblibgo.api.AllApiClass;
import com.lib.liblibgo.api.Holders;
import com.lib.liblibgo.common.Constants;
import com.lib.liblibgo.common.UserDatabase;
import com.lib.liblibgo.dashboard.book_details.WishListActivity;
import com.lib.liblibgo.model.SubmitDataResModel;
import com.lib.liblibgo.views.MyTextView;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONException;
import org.json.JSONObject;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileFragment extends Fragment implements PaymentResultListener {

    private ImageView ivMenu;
    private LinearLayout editProfileLl;
    private CircleImageView ivUser;
    private TextView tvName;
    private TextView tvPhone;
    private TextView tvAdr;
    private TextView tvLibCoins;
    private TextView tvRedeem;
    private Button btnLogout;
    private Button btnDeleteAccount;
    private LinearLayout llLogin;
    private RelativeLayout rlLoginUser;
    private Context mContext;
    private UserDatabase database;
    private SharedPreferences sharedpreferences;
    private SharedPreferences.Editor editor;
    private TextView tvLogin;
    private AlertDialog.Builder dialogBuilder;
    private String libCoins = "0";
    private AlertDialog alertDialog;
    private String enteredLibCoin = "";
    private String upiStatus = "0";
    private String transactionId = "";
    private DrawerLayout drawer;
    private MyTextView menuEditProfile,menuPrivacyPolicy,menuAboutUs,menuTermCondition,menuContactUs,menuSettings,wishList,menuLogout;
    private LinearLayout llSetting;
    private View viewSetting;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        ivMenu = (ImageView)view.findViewById(R.id.ivMenu);
        editProfileLl = (LinearLayout)view.findViewById(R.id.editProfileLl);
        ivUser = (CircleImageView)view.findViewById(R.id.ivUser);
        tvName = (TextView)view.findViewById(R.id.tvName);
        tvPhone = (TextView)view.findViewById(R.id.tvPhone);
        tvAdr = (TextView)view.findViewById(R.id.tvAdr);
        tvLibCoins = (TextView)view.findViewById(R.id.tvLibCoins);
        tvRedeem = (TextView)view.findViewById(R.id.tvRedeem);
        tvLogin = (TextView)view.findViewById(R.id.tvLogin);
        btnLogout = (Button)view.findViewById(R.id.btnLogout);
        btnDeleteAccount = (Button)view.findViewById(R.id.btnDeleteAccount);
        menuLogout = view.findViewById(R.id.log_out);

        rlLoginUser = (RelativeLayout)view.findViewById(R.id.rlLoginUser);
        llLogin = (LinearLayout)view.findViewById(R.id.llLogin);

        database = new UserDatabase(mContext);
        sharedpreferences = mContext.getSharedPreferences(HomeActivity.MyPREFERENCES, Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();

        drawer = (DrawerLayout)view.findViewById(R.id.drawer);

        menuEditProfile = view.findViewById(R.id.edit_profile);
        menuPrivacyPolicy = view.findViewById(R.id.privacyPolicy);
        menuAboutUs = view.findViewById(R.id.aboutUs);
        menuTermCondition = view.findViewById(R.id.termCondition);
        menuContactUs = view.findViewById(R.id.contactUs);
        menuSettings = view.findViewById(R.id.settings);
        //iv_menu = view.findViewById(R.id.ivMenu);
        llSetting = view.findViewById(R.id.llSetting);
        viewSetting = view.findViewById(R.id.viewSetting);
        wishList = view.findViewById(R.id.wishList);


        if (database.getUserId().equals("")){
            llLogin.setVisibility(View.VISIBLE);
            rlLoginUser.setVisibility(View.GONE);
            menuEditProfile.setVisibility(View.GONE);
            llSetting.setVisibility(View.GONE);
            viewSetting.setVisibility(View.GONE);
            wishList.setVisibility(View.GONE);
            menuLogout.setText("Login");
        }else {
            rlLoginUser.setVisibility(View.VISIBLE);
            llLogin.setVisibility(View.GONE);
            menuEditProfile.setVisibility(View.VISIBLE);
            llSetting.setVisibility(View.VISIBLE);
            viewSetting.setVisibility(View.VISIBLE);
            wishList.setVisibility(View.VISIBLE);
            menuLogout.setText("Logout");
            getUserDetails();
        }

        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentHome = new Intent(mContext, LoginWithPhoneNumber.class);
                startActivity(intentHome);
            }
        });

        menuLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (database.getUserId().equals("")){
                    Intent intentHome = new Intent(getContext(), LoginWithPhoneNumber.class);
                    startActivity(intentHome);
                }else {
                    logoutUser();
                }
            }
        });

        editProfileLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentHome = new Intent(mContext, UserProfileActivity.class);
                startActivity(intentHome);
            }
        });

        btnDeleteAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(mContext);
                LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View layoutView = inflater.inflate(R.layout.delete_account_popup, null);
                final TextView tv_yes = layoutView.findViewById(R.id.tv_yes);
                final TextView tv_no = layoutView.findViewById(R.id.tv_no);

                dialogBuilder.setView(layoutView);
                AlertDialog alertDialog = dialogBuilder.create();
                alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation1;
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                alertDialog.setCancelable(false);
                alertDialog.show();
                alertDialog.getWindow().setLayout(700, ViewGroup.LayoutParams.WRAP_CONTENT);

                tv_no.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.dismiss();
                    }
                });

                tv_yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.dismiss();
                        deleteMyAccount();
                    }
                });

            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logoutUser();
            }
        });

        menuEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentHome = new Intent(getContext(), UserProfileActivity.class);
                startActivity(intentHome);
            }
        });

        menuPrivacyPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentHome = new Intent(getContext(), PrivacyPolicyActivity.class);
                intentHome.putExtra("value","0");
                startActivity(intentHome);
            }
        });

        menuAboutUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentHome = new Intent(getContext(),PrivacyPolicyActivity.class);
                intentHome.putExtra("value","1");
                startActivity(intentHome);
            }
        });

        menuContactUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentHome = new Intent(getContext(), ContactUsActivity.class);
                startActivity(intentHome);
            }
        });

        menuTermCondition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentHome = new Intent(getContext(),PrivacyPolicyActivity.class);
                intentHome.putExtra("value","3");
                startActivity(intentHome);
            }
        });

        wishList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (database.getUserId().equals("")){
                    Intent intentHome = new Intent(getContext(),LoginWithPhoneNumber.class);
                    startActivity(intentHome);
                }else {
                    Intent intentHome = new Intent(getContext(), WishListActivity.class);
                    startActivity(intentHome);
                }
            }
        });

        menuSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentHome = new Intent(getContext(), SettingsActivity.class);
                startActivity(intentHome);
            }
        });

        ivMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.openDrawer(GravityCompat.START);
            }
        });

        return view;
    }

    private void logoutUser() {
        final ProgressDialog loginDialog = new ProgressDialog(mContext);
        loginDialog.setMessage("Please wait..");
        loginDialog.setCancelable(false);
        loginDialog.show();
        Holders holders = AllApiClass.getInstance().getApi();
        Call<SubmitDataResModel> call = holders.userLogout(database.getUserId());
        call.enqueue(new Callback<SubmitDataResModel>() {
            @Override
            public void onResponse(Call<SubmitDataResModel> call, Response<SubmitDataResModel> response) {
                if (response.isSuccessful()){
                    if (response.body().getResponse().getCode() == 1){
                        loginDialog.dismiss();
                        database.clearSession();
                        editor.clear();
                        editor.commit();
                        LoginManager.getInstance().logOut();
                        GoogleSignInOptions gso = new GoogleSignInOptions.
                                Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).
                                build();
                        GoogleSignInClient googleSignInClient= GoogleSignIn.getClient(mContext,gso);
                        googleSignInClient.signOut();
                        Intent intentHome = new Intent(mContext, HomeActivity.class);
                        intentHome.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intentHome);
                        getActivity().finish();
                    }else {
                        loginDialog.dismiss();
                        Toast.makeText(mContext, response.body().getResponse().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }else {
                    loginDialog.dismiss();
                    Toast.makeText(mContext, "Something went wrong !", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<SubmitDataResModel> call, Throwable t) {
                loginDialog.dismiss();
                Toast.makeText(mContext, "Check Your Internet Connection.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void deleteMyAccount() {
        final ProgressDialog progressBar = new ProgressDialog(mContext);
        progressBar.setMessage("Please wait...");
        progressBar.setCancelable(false);
        progressBar.show();
        Holders holders = AllApiClass.getInstance().getApi();
        Call<SubmitDataResModel> resCall = holders.deleteAccount(database.getUserId());
        resCall.enqueue(new Callback<SubmitDataResModel>() {
            @Override
            public void onResponse(Call<SubmitDataResModel> call, Response<SubmitDataResModel> response) {
                if (response.isSuccessful()){
                    if (response.body().getResponse().getCode() == 1){
                        Toast.makeText(mContext, "Account deleted successfully.", Toast.LENGTH_SHORT).show();
                        progressBar.dismiss();
                        database.clearSession();
                        editor.clear();
                        editor.commit();
                        LoginManager.getInstance().logOut();
                        GoogleSignInOptions gso = new GoogleSignInOptions.
                                Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).
                                build();
                        GoogleSignInClient googleSignInClient= GoogleSignIn.getClient(mContext,gso);
                        googleSignInClient.signOut();
                        Intent intentHome = new Intent(mContext, HomeActivity.class);
                        intentHome.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intentHome);
                        getActivity().finishAffinity();
                    }else {
                        progressBar.dismiss();
                        Toast.makeText(mContext, ""+response.body().getResponse().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }else {
                    progressBar.dismiss();
                    Toast.makeText(mContext, "Something went wrong !", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<SubmitDataResModel> call, Throwable t) {
                progressBar.dismiss();
                Toast.makeText(mContext, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
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

                            libCoins = Constants.myLibCoins;
                            upiStatus = object.getString("upi_status");

                            tvName.setText(object.getString("username"));
                            tvPhone.setText(object.getString("mobile"));
                            tvLibCoins.setText(Constants.myLibCoins);
                            tvAdr.setText(apartmentName+", "+
                                    object.getString("area_name")+", "
                                    +object.getString("landmark")+", "
                                    +object.getString("city")+", "
                                    +object.getString("state")+", "
                                    +object.getString("pincode"));

                            if (Constants.myProfileImage.equals("")){
                                Glide.with(mContext).load(R.drawable.no_img).placeholder(R.drawable.no_img).into(ivUser);
                            }else {
                                Glide.with(mContext).load(Constants.myProfileImage).placeholder(R.drawable.no_img).into(ivUser);
                            }

                            if (database.getUserId().equals("")){
                                llLogin.setVisibility(View.VISIBLE);
                                rlLoginUser.setVisibility(View.GONE);
                                menuEditProfile.setVisibility(View.GONE);
                                llSetting.setVisibility(View.GONE);
                                viewSetting.setVisibility(View.GONE);
                                wishList.setVisibility(View.GONE);
                                menuLogout.setText("Login");
                            }else {
                                rlLoginUser.setVisibility(View.VISIBLE);
                                llLogin.setVisibility(View.GONE);
                                menuEditProfile.setVisibility(View.VISIBLE);
                                llSetting.setVisibility(View.VISIBLE);
                                viewSetting.setVisibility(View.VISIBLE);
                                wishList.setVisibility(View.VISIBLE);
                                menuLogout.setText("Logout");
                            }

                            tvRedeem.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if (Constants.myLibCoins.equals("0")){
                                        Toast.makeText(mContext, "You don't have libcoin.", Toast.LENGTH_SHORT).show();
                                        tvRedeem.setBackgroundResource(R.drawable.btn_bg_grey);
                                        //tvRedeem.setEnabled(false);
                                    }else {
                                        tvRedeem.setBackgroundResource(R.drawable.btn_bg);
                                        showRedeemPopup(R.layout.redeem_lib_coin_popup);
                                    }
                                }
                            });

                        }else {
                            progressBar.dismiss();
                            Toast.makeText(mContext, ""+object.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else {
                    progressBar.dismiss();
                    Toast.makeText(mContext, "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                progressBar.dismiss();
                Toast.makeText(mContext, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onAttach(@NonNull Context context) {
        mContext = context;
        super.onAttach(context);
    }

    private void showRedeemPopup(int layout) {
        dialogBuilder = new AlertDialog.Builder(mContext);
        LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View layoutView = inflater.inflate(layout, null);
        final Button btn_done = layoutView.findViewById(R.id.btn_done);
        final TextView tv_txt = layoutView.findViewById(R.id.tv_txt);
        final EditText et_lib_coin = layoutView.findViewById(R.id.et_lib_coin);

        tv_txt.setText("You have "+libCoins+" Lib coins."+"\n"+"Enter how many lib coins you want to redeem.");
        et_lib_coin.setText(libCoins);

        dialogBuilder.setView(layoutView);
        alertDialog = dialogBuilder.create();
        alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();
        alertDialog.setCancelable(true);
        alertDialog.getWindow().setLayout(700, ViewGroup.LayoutParams.WRAP_CONTENT);

        btn_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enteredLibCoin = et_lib_coin.getText().toString().trim();
                if (!enteredLibCoin.equals("") || enteredLibCoin.equals("0")){
                    if (Integer.parseInt(libCoins) >= Integer.parseInt(enteredLibCoin)){
                        if (upiStatus.equals("0")){
                            gotoPaymentMethod();
                        }else {
                            transactionId = "";
                            createOrder(transactionId);
                        }
                    }else {
                        Toast.makeText(mContext, "You have only "+libCoins + " Lib coins.", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(mContext, "You don't have lib coins to redeem.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void gotoPaymentMethod() {
        Checkout checkout = new Checkout();
        //checkout.setImage(R.drawable.logo);
        final Context activity = mContext;

        try {
            JSONObject options = new JSONObject();

            options.put("name", "LibLibGo");
            options.put("description", "is a one stop solution for your library management.");
            //options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png");
            //options.put("order_id", "order_9A33XWu170gUtm");
            options.put("currency", "INR");

            double amount = 1;
            amount = amount * 100;

            options.put("amount", amount);

            JSONObject preFill  = new JSONObject();
            //JSONObject methodObj  = new JSONObject();
            //methodObj.put("netbanking", 0);
            //methodObj.put("card", 0);
            // methodObj.put("upi", 1);
            //methodObj.put("wallet", 0);
            preFill.put("method", "upi");
            preFill.put("email","libpayment@gmail.com");
            preFill.put("contact",database.getPHONE());

            options.put("prefill",preFill );
            //Log.d("deductedLibCoins",deductedLibCoins);
            checkout.open((Activity) activity, options);
        } catch(Exception e) {
            Toast.makeText(mContext, "Error in payment : "+e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.e("ErrorMessage", "Error in starting Razorpay Checkout", e);
        }
    }

    @Override
    public void onPaymentSuccess(String s) {
        transactionId = s;
        createOrder(transactionId);
    }

    private void createOrder(String transactionId) {
        final ProgressDialog progressBar = new ProgressDialog(mContext);
        progressBar.setMessage("Please wait...");
        progressBar.setCancelable(false);
        progressBar.show();
        Holders holders = AllApiClass.getInstance().getApi();
        Call<SubmitDataResModel> resCall = holders.requestForRedeem(database.getUserId(),enteredLibCoin,transactionId);
        resCall.enqueue(new Callback<SubmitDataResModel>() {
            @Override
            public void onResponse(Call<SubmitDataResModel> call, Response<SubmitDataResModel> response) {
                if (response.isSuccessful()){
                    progressBar.dismiss();
                    alertDialog.dismiss();
                    getUserDetails();
                    Toast.makeText(mContext, "Redeem request has been sent.", Toast.LENGTH_SHORT).show();
                }else {
                    progressBar.dismiss();
                    Toast.makeText(mContext, "Something went wrong !", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<SubmitDataResModel> call, Throwable t) {
                progressBar.dismiss();
                Toast.makeText(mContext, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onPaymentError(int i, String s) {
        try {
            Toast.makeText(mContext, "Payment Failed. Please Try Again.", Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    @Override
    public void onResume() {
        if (HomeFragment.flag == 1){
            rlLoginUser.setVisibility(View.VISIBLE);
            llLogin.setVisibility(View.GONE);
            getUserDetails();
            HomeFragment.flag = 0;
        }
        super.onResume();
    }

}