package com.lib.liblibgo.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.iid.FirebaseInstanceId;
import com.lib.liblibgo.R;
import com.lib.liblibgo.api.AllApiClass;
import com.lib.liblibgo.api.Holders;
import com.lib.liblibgo.common.Constants;
import com.lib.liblibgo.common.UserDatabase;
import com.lib.liblibgo.model.LoginWithPhoneModel;
import com.lib.liblibgo.model.SocialLoginModel;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginWithPhoneNumber extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener{

    private EditText mPhoneNumberEt;
    private UserDatabase preferences;
    private TextView mFacebookLogin,mGoogleLogin,mEmailLogin;
    GoogleApiClient googleApiClient;
    private static final int SIGN_IN=1;

    private String gUserName;
    private String gUserEmail;
    private String gUserImage;
    private String gGmailId;

    private String fUserName;
    private String fUserEmail;
    private String fFbId;

    CallbackManager callbackManager;
    private URL profile_pic;
    private String refreshedToken="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_with_phone_number);
        preferences = new UserDatabase(LoginWithPhoneNumber.this);

        /*if (preferences.isLogin()){
            Log.d("SAVED", "onCreate: "+preferences.isLogin());
            Intent intentForgot=new Intent(LoginWithPhoneNumber.this,HomeActivity.class);
            intentForgot.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intentForgot);
        }*/

        callbackManager=CallbackManager.Factory.create();
        mPhoneNumberEt = (EditText)findViewById(R.id.et_phone);

        mFacebookLogin = (TextView)findViewById(R.id.tv_facebook);
        mGoogleLogin = (TextView)findViewById(R.id.tv_google);
        mEmailLogin = (TextView)findViewById(R.id.tv_email);

        GoogleSignInOptions gso =  new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        googleApiClient=new GoogleApiClient.Builder(this)
                .enableAutoManage(this,this)
                .addApi(Auth.GOOGLE_SIGN_IN_API,gso)
                .build();

        mGoogleLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dexter.withContext(LoginWithPhoneNumber.this).withPermissions(Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION)
                        .withListener(new MultiplePermissionsListener() {
                            @Override
                            public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                                googleLogin();
                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                                permissionToken.continuePermissionRequest();
                            }
                        }).check();
            }
        });

        mFacebookLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dexter.withContext(LoginWithPhoneNumber.this).withPermissions(Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION)
                        .withListener(new MultiplePermissionsListener() {
                            @Override
                            public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                                facebookLogin();
                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                                permissionToken.continuePermissionRequest();
                            }
                        }).check();

            }
        });

        refreshedToken = FirebaseInstanceId.getInstance().getToken();
        //Log.d("myToken",refreshedToken);

        mEmailLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dexter.withContext(LoginWithPhoneNumber.this).withPermissions(Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION)
                        .withListener(new MultiplePermissionsListener() {
                            @Override
                            public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                                Intent intent = new Intent(LoginWithPhoneNumber.this,LoginWithEmail.class);
                                startActivity(intent);
                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                                permissionToken.continuePermissionRequest();
                            }
                        }).check();

            }
        });

        mPhoneNumberEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 9){
                    hideKeyboard(LoginWithPhoneNumber.this);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus();
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void facebookLogin() {
        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("email","public_profile"));
        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {@Override
                public void onSuccess(LoginResult loginResult) {

                    System.out.println("onSuccess");

                    String accessToken = loginResult.getAccessToken()
                            .getToken();
                    Log.i("accessToken", accessToken);

                    GraphRequest request = GraphRequest.newMeRequest(
                            loginResult.getAccessToken(),
                            new GraphRequest.GraphJSONObjectCallback() {@Override
                            public void onCompleted(JSONObject object,
                                                    GraphResponse response) {

                                Log.i("LoginActivity",
                                        response.toString());
                                try {
                                    fFbId = object.getString("id");
                                    try {
                                        profile_pic = new URL(
                                                "http://graph.facebook.com/" + fFbId + "/picture?type=large");
                                        Log.i("profile_pic",
                                                profile_pic + "");
                                       /* Log.i("Facebook_id",
                                                fFbId + "");*/

                                    } catch (MalformedURLException e) {
                                        e.printStackTrace();
                                    }
                                    fUserName = object.getString("name");
                                    fUserEmail = object.getString("email");
                                    fFbId = object.getString("id");
                                    Log.d("facebookLoginDetails",fUserName);
                                    Log.d("facebookLoginDetails",fUserEmail);
                                    Log.d("facebookLoginDetails",fFbId);
                                    //fbLogin(fFbId,fUserName,fUserEmail);
                                    //Toast.makeText(LoginWithPhoneNumber.this, ""+fUserName, Toast.LENGTH_SHORT).show();
                                    socialLogin("facebook",fFbId,fUserName,fUserEmail);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Toast.makeText(LoginWithPhoneNumber.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                            });

                    Bundle parameters = new Bundle();
                    parameters.putString("fields",
                            "id,name,email,gender, birthday");
                    request.setParameters(parameters);
                    request.executeAsync();
                }

                    @Override
                    public void onCancel() {
                        System.out.println("onCancel");
                        Toast.makeText(LoginWithPhoneNumber.this, "Cancel", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        System.out.println("onError");
                        fUserEmail = "";
                        //Log.v("LoginActivity", exception.getCause().toString());
                        Toast.makeText(LoginWithPhoneNumber.this, "Email id not found \nin your facebook account.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void socialLogin(String type, String socialId,String name,String email) {
        final ProgressDialog loginDialog = new ProgressDialog(this);
        loginDialog.setMessage("Please wait..");
        loginDialog.setCancelable(false);
        loginDialog.show();
        Holders holders = AllApiClass.getInstance().getApi();
        Call<SocialLoginModel> call = holders.socialLogin(type,socialId,refreshedToken);
        call.enqueue(new Callback<SocialLoginModel>() {
            @Override
            public void onResponse(Call<SocialLoginModel> call, Response<SocialLoginModel> response) {
                if (response.isSuccessful()){
                    if (response.body().getResponse().getCode() == 1){
                        loginDialog.dismiss();
                        preferences.createLogin(response.body().getResponse().getUser_id(),
                                response.body().getResponse().getUser_name(),
                                response.body().getResponse().getMobile(),
                                response.body().getResponse().getPincode(),
                                response.body().getResponse().getAddress(),
                                response.body().getResponse().getApartment_name()
                                );
                        if (response.body().getResponse().getApartment_status().equals("0")){
                            Intent intent = new Intent(LoginWithPhoneNumber.this,NewUserDetailsActivity.class);
                            intent.putExtra("phone","");
                            Constants.SOCIAL_USER_TYPE = type;
                            Constants.SOCIAL_USER_NAME = name;
                            Constants.SOCIAL_USER_EMAIL = email;
                            Constants.SOCIAL_ID = socialId;
                            startActivity(intent);
                            finish();
                        }else {
                            //Helper.showToast(LoginWithPhoneNumber.this,response.body().getResponse().getMessage());
                            /*Intent intent = new Intent(LoginWithPhoneNumber.this, HomeActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);*/
                            finish();
                        }
                    }else {
                        loginDialog.dismiss();
                        Toast.makeText(LoginWithPhoneNumber.this, ""+response.body().getResponse().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(LoginWithPhoneNumber.this, "Something went wrong !", Toast.LENGTH_SHORT).show();
                    loginDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<SocialLoginModel> call, Throwable t) {
                Toast.makeText(LoginWithPhoneNumber.this, "Server not responding yet.Try again !", Toast.LENGTH_SHORT).show();
                loginDialog.dismiss();
            }
        });
    }

    private void googleLogin() {
        Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(intent,SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        callbackManager.onActivityResult(requestCode,resultCode,data);
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==SIGN_IN){
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    private void handleSignInResult(GoogleSignInResult result){
        if(result.isSuccess()){
            GoogleSignInAccount account=result.getSignInAccount();
            gUserName=account.getDisplayName();
            gUserEmail =account.getEmail();
            gGmailId =account.getId();
            Log.d("googleLoginDetails",gUserName);
            Log.d("googleLoginDetails",gUserEmail);
            Log.d("googleLoginDetails",gGmailId);
            //googleLoginApi(gGmailId,gUserName,gUserEmail);
            try{
                gUserImage =account.getPhotoUrl().toString();
            }catch (NullPointerException e){
                gUserImage = "";
            }
            socialLogin("google",gGmailId,gUserName,gUserEmail);
            Log.d("googleLoginDetails",gUserImage);
        }else{
            Toast.makeText(this, "Google login cancel.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this, "Google Login Failed.", Toast.LENGTH_SHORT).show();
    }

    public void onUserLogin(View view) {
        Dexter.withContext(LoginWithPhoneNumber.this).withPermissions(Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                        String phoneNo = mPhoneNumberEt.getText().toString().trim();
                        if (phoneNo.equals("")){
                            Toast.makeText(LoginWithPhoneNumber.this, "Enter Phone Number.", Toast.LENGTH_SHORT).show();
                        }else if (phoneNo.length() < 10){
                            Toast.makeText(LoginWithPhoneNumber.this, "Enter Correct Number.", Toast.LENGTH_SHORT).show();
                        }else {
                            //Toast.makeText(this, ""+phoneNo, Toast.LENGTH_SHORT).show();
                            gotoVerifyNumber(phoneNo);
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                }).check();

    }

    private void gotoVerifyNumber(String phoneNo) {
        final ProgressDialog loginDialog = new ProgressDialog(this);
        loginDialog.setMessage("Please wait..");
        loginDialog.setCancelable(false);
        loginDialog.show();
        Holders holders = AllApiClass.getInstance().getApi();
        Call<LoginWithPhoneModel> call = holders.loginWithPhone(phoneNo);
        call.enqueue(new Callback<LoginWithPhoneModel>() {
            @Override
            public void onResponse(Call<LoginWithPhoneModel> call, Response<LoginWithPhoneModel> response) {
                if (response.isSuccessful()){
                    if (response.body().getResponse().getCode() == 1){
                        loginDialog.dismiss();
                        Intent intent = new Intent(LoginWithPhoneNumber.this,VerifyOtp.class);
                        intent.putExtra("phone",phoneNo);
                        intent.putExtra("otp",response.body().getResponse().getOtp());
                        startActivity(intent);
                        finish();
                    }else {
                        loginDialog.dismiss();
                        Toast.makeText(LoginWithPhoneNumber.this, ""+response.body().getResponse().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(LoginWithPhoneNumber.this, "Something went wrong !", Toast.LENGTH_SHORT).show();
                    loginDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<LoginWithPhoneModel> call, Throwable t) {
                Toast.makeText(LoginWithPhoneNumber.this, "Server not responding yet.Try again !", Toast.LENGTH_SHORT).show();
                loginDialog.dismiss();
            }
        });

    }
}