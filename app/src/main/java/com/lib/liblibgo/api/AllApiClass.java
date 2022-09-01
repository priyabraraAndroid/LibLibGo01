package com.lib.liblibgo.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lib.liblibgo.BuildConfig;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AllApiClass {
    //public static final String BASE_URL = "https://liblibgo.com/User/";
    public static final String BASE_URL = "http://staging.liblibgo.com/User/";
    private static final String ADMIN_URL = "https://liblibgo.com/Admin/";
    private static final String BOOK_URL = "https://www.googleapis.com/books/";
    private static final String BOOK_MRP_URL = "https://api2.isbndb.com/";

    private static com.lib.liblibgo.api.AllApiClass mInstance;
    private static Retrofit retrofit,adminRetrofit,bookRetrofit,bookMrpRetrofit;

    private AllApiClass() {

        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder okhttp = new OkHttpClient.Builder();
        okhttp.addInterceptor(loggingInterceptor);
        final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(20, TimeUnit.SECONDS)
                .readTimeout(100, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
//                .callTimeout(180,TimeUnit.SECONDS)
                .addInterceptor(getHttpLoggingInterceptor())
                .build();

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        adminRetrofit = new Retrofit.Builder()
                .baseUrl(ADMIN_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        bookRetrofit = new Retrofit.Builder()
                .baseUrl(BOOK_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        bookMrpRetrofit = new Retrofit.Builder()
                .baseUrl(BOOK_MRP_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }

    public static synchronized com.lib.liblibgo.api.AllApiClass getInstance() {
        if (mInstance == null) {
            mInstance = new com.lib.liblibgo.api.AllApiClass();
        }
        return mInstance;
    }

    public Holders getApi() {
        return retrofit.create(Holders.class);
    }
    public Holders getApi1() {
        return adminRetrofit.create(Holders.class);
    }

    public Holders getApiBooks() {
        return bookRetrofit.create(Holders.class);
    }

    public Holders getApiBooksMrp() {
        return bookMrpRetrofit.create(Holders.class);
    }

private static HttpLoggingInterceptor getHttpLoggingInterceptor(){
    HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
    if (BuildConfig.DEBUG)
    httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
    else
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.NONE);
    return httpLoggingInterceptor;
}

}
