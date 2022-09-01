package com.lib.liblibgo.common;

import android.content.Context;
import android.content.SharedPreferences;

public class UserDatabase {

    private static final String APP_SHARED_PREF = UserDatabase.class.getSimpleName();
    public static final String IS_SESSION_EXPIRED = "session";
    public static final String IS_REMEMBER = "remember";
    public static final String NAME = "name";
    public static final String EMAIL = "email";
    public static final String PHONE = "phone";
    public static final String PIN = "pin";
    public static final String ADDRESS = "address";
    public static final String FLAT = "flat";
    public static final String APART_ID = "apart_id";
    public static final String APART_NAME = "apart_id";
    public static final String USER_ID = "user_id";
    public static final String LOGIN_ID = "login_id";
    public static final String LOGIN_PASS = "login_pass";

    private SharedPreferences sharedPrefs;
    private SharedPreferences.Editor editorPrefs;


    public UserDatabase(Context context){
        this.sharedPrefs = context.getSharedPreferences(APP_SHARED_PREF,Context.MODE_PRIVATE);
        this.editorPrefs = sharedPrefs.edit();
    }

    public void clearSession() {
        editorPrefs.clear();
        editorPrefs.apply();
    }

    public  boolean isLogin() {
        return sharedPrefs.getBoolean(IS_SESSION_EXPIRED,false);
    }

    public void setLogin(boolean login){
        editorPrefs.putBoolean(IS_SESSION_EXPIRED,login);
        editorPrefs.apply();
    }

    public String getLoginId() {
        return sharedPrefs.getString(LOGIN_ID,"");
    }

    public String getLoginPass() {
        return sharedPrefs.getString(LOGIN_PASS,"");
    }

    public boolean IsRemember() {
        return sharedPrefs.getBoolean(IS_REMEMBER,false);
    }

    public void saveLogin(String LoginId, String LoginPass){
        editorPrefs.putString(LOGIN_ID,"");
        editorPrefs.putString(LOGIN_PASS,"");
        editorPrefs.putBoolean(IS_REMEMBER,true);
        editorPrefs.apply();
    }

    public void createLogin(String LoginId, String Pass, String id , String name, String email,
                            String phone,String flat,String apartId,String pin,String adr,String ApartmentName){
        editorPrefs.putString(LOGIN_ID,LoginId);
        editorPrefs.putString(LOGIN_PASS,Pass);
        editorPrefs.putString(USER_ID,id);
        editorPrefs.putString(NAME,name);
        editorPrefs.putString(EMAIL,email);
        editorPrefs.putString(PHONE,phone);
        editorPrefs.putString(FLAT,flat);
        editorPrefs.putString(APART_ID,apartId);
        editorPrefs.putString(PIN,pin);
        editorPrefs.putString(ADDRESS,adr);
        editorPrefs.putString(APART_NAME,ApartmentName);
        editorPrefs.putBoolean(IS_SESSION_EXPIRED,true);
        editorPrefs.apply();
    }

    /*public void createLogin(
            String name, String email, String mobile, String flatNo, String apartmentId) {
        editorPrefs.putString(NAME,name);
        editorPrefs.putString(EMAIL,email);
        editorPrefs.putString(PHONE,mobile);
        editorPrefs.putString(FLAT,flatNo);
        editorPrefs.putString(APART_ID,apartmentId);
        editorPrefs.apply();
    }*/

    public void createLogin(String userId, String name, String mobile,String pin,String adr,String ApartmentName) {
        editorPrefs.putString(USER_ID,userId);
        editorPrefs.putString(NAME,name);
        editorPrefs.putString(PHONE,mobile);
        editorPrefs.putString(PIN,pin);
        editorPrefs.putString(ADDRESS,adr);
        editorPrefs.putString(APART_NAME,ApartmentName);
        editorPrefs.apply();
    }

    public void createLogin(String userId, String name, String mobile,String pin,String adr,String ApartmentName,String flat,String email) {
        editorPrefs.putString(USER_ID,userId);
        editorPrefs.putString(NAME,name);
        editorPrefs.putString(PHONE,mobile);
        editorPrefs.putString(PIN,pin);
        editorPrefs.putString(ADDRESS,adr);
        editorPrefs.putString(APART_NAME,ApartmentName);
        editorPrefs.putString(FLAT,flat);
        editorPrefs.putString(EMAIL,email);
        editorPrefs.apply();
    }

    public  String getNAME() {
        return sharedPrefs.getString(NAME,"");
    }

    public String getPHONE() {
        return sharedPrefs.getString(PHONE,"");
    }


    public String getFLAT() {
        return sharedPrefs.getString(FLAT,"");
    }

    public String getApartId() {
        return sharedPrefs.getString(APART_ID,"");
    }

    public  String getEMAIL() {
        return sharedPrefs.getString(EMAIL,"");
    }

    public String getUserId() {
        return sharedPrefs.getString(USER_ID,"");
    }

    public String getPin() {
        return sharedPrefs.getString(PIN,"");
    }
    public String getAddress() {
        return sharedPrefs.getString(ADDRESS,"");
    }

    public String getApartmentName() {
        return sharedPrefs.getString(APART_NAME,"");
    }
}
