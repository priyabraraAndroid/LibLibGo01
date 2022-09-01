package com.lib.liblibgo.dashboard.book_details;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.lib.liblibgo.R;
import com.lib.liblibgo.activity.LoginWithPhoneNumber;
import com.lib.liblibgo.activity.homedashboard.HomeActivity;
import com.lib.liblibgo.adapter.ReviewAdapter;
import com.lib.liblibgo.api.AllApiClass;
import com.lib.liblibgo.api.Holders;
import com.lib.liblibgo.common.Constants;
import com.lib.liblibgo.common.Helper;
import com.lib.liblibgo.common.UserDatabase;
import com.lib.liblibgo.dashboard.book_collect.CollectBookActivity;
import com.lib.liblibgo.model.AddToWishListModel;
import com.lib.liblibgo.model.BookDetailsModel;
import com.lib.liblibgo.model.SubmitDataResModel;

import java.util.ArrayList;
import java.util.List;

import me.zhanghai.android.materialratingbar.MaterialRatingBar;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookDetails extends AppCompatActivity {
    private String bookId = "";
    private TextView mBookNameTv;
    private TextView mAuthorNameTv;
    private TextView mDescriptionTv;
    private TextView mRatingTv;
    private MaterialRatingBar mRatingView;
    private Button mBookNowBtn;
    private RecyclerView mReviewRv;
    private ProgressBar mProgressBar;
    private LinearLayout mNoDataLl;
    private List<BookDetailsModel.ResData.ResDataRes.ReviewListData> myList = new ArrayList<>();
    private ReviewAdapter adapter;
    UserDatabase database;
    private RelativeLayout mNoDataBody;
    private String bookImage="";
    private ImageView mBookImageIv;
    private LinearLayout ll_buttons;
    private Button mBuyNowBtn;
    private TextView tvSelePrice;
    private TextView tvRentPrice;
    private TextView tvMrp;
    private LinearLayout llSalePrice;
    private LinearLayout llRentPrice;
    private LinearLayout bookOutOfStock;
    private ImageView ivWishList,ivCart;
    private TextView tvNotify;
    private TextView tvCartCount;
    private SharedPreferences sharedpreferences;
    private SharedPreferences.Editor editor;
    private int cartCount;
    private String bookOwnerId = "";
    private TextView tvOutOfStock;
    private RelativeLayout rlMrp;
    private TextView tv_community;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_details);

        sharedpreferences = getSharedPreferences(HomeActivity.MyPREFERENCES, Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();
        cartCount = sharedpreferences.getInt("cart_count",0);

        bookId = getIntent().getStringExtra("book_id");
        bookImage = getIntent().getStringExtra("book_img");
        database = new UserDatabase(this);

        mBookNameTv = (TextView)findViewById(R.id.book_name);
        mAuthorNameTv = (TextView)findViewById(R.id.book_author);
        mDescriptionTv = (TextView)findViewById(R.id.tv_details);
        mRatingTv = (TextView)findViewById(R.id.tv_rate);
        mRatingView = (MaterialRatingBar)findViewById(R.id.rating);
        mBookNowBtn = (Button)findViewById(R.id.btn_book_now);
        mBuyNowBtn = (Button)findViewById(R.id.btn_buy);
        mReviewRv = (RecyclerView)findViewById(R.id.rv_review);
        mProgressBar = (ProgressBar)findViewById(R.id.progressBar);
        mNoDataLl = (LinearLayout)findViewById(R.id.ll_no_data);
        mNoDataBody = (RelativeLayout)findViewById(R.id.no_data);
        rlMrp = (RelativeLayout)findViewById(R.id.rlMrp);
        mBookImageIv = (ImageView)findViewById(R.id.iv_image);
        ll_buttons = (LinearLayout)findViewById(R.id.ll_buttons);
        tvOutOfStock = (TextView) findViewById(R.id.tvOutOfStock);
        tv_community = (TextView) findViewById(R.id.tv_community);
        mRatingView.setEnabled(false);

        if (bookImage.equals("")){
            Glide.with(getApplicationContext()).load("https://sales.arecontvision.com/images/products/img_placeholder_50618_xl.jpg").into(mBookImageIv);
        }else {
            Glide.with(getApplicationContext()).load(bookImage).into(mBookImageIv);
        }

        showBookDetails(bookId);

        tvMrp = (TextView)findViewById(R.id.tvMrp);
        tvSelePrice = (TextView)findViewById(R.id.tvSelePrice);
        tvRentPrice = (TextView)findViewById(R.id.tvRentPrice);
        tvNotify = (TextView)findViewById(R.id.tvNotify);


        llSalePrice = (LinearLayout)findViewById(R.id.llSalePrice);
        llRentPrice = (LinearLayout)findViewById(R.id.llRentPrice);
        bookOutOfStock = (LinearLayout)findViewById(R.id.bookOutOfStock);

        ivWishList = (ImageView)findViewById(R.id.ivWishList);
        ivCart = (ImageView)findViewById(R.id.ivCart);
        tvCartCount = (TextView)findViewById(R.id.tvCartCount);

        ivWishList.setVisibility(View.INVISIBLE);

        mBookNowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (database.getUserId().equals("")){
                    Intent intent = new Intent(BookDetails.this, LoginWithPhoneNumber.class);
                    startActivity(intent);
                }else if (bookOwnerId.equals(database.getUserId())){
                    Toast.makeText(BookDetails.this, "This is your own book.", Toast.LENGTH_SHORT).show();
                }else {
                    addToCart("rent");
                }

            }
        });

        mBuyNowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (database.getUserId().equals("")){
                    Intent intent = new Intent(BookDetails.this, LoginWithPhoneNumber.class);
                    startActivity(intent);
                }else if (bookOwnerId.equals(database.getUserId())){
                    Toast.makeText(BookDetails.this, "This is your own book.", Toast.LENGTH_SHORT).show();
                }else {
                    addToCart("purchase");
                }
            }
        });

        ivWishList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (database.getUserId().equals("")){
                    Intent intent = new Intent(BookDetails.this, LoginWithPhoneNumber.class);
                    startActivity(intent);
                }else if (bookOwnerId.equals(database.getUserId())){
                    Toast.makeText(BookDetails.this, "This is your own book.", Toast.LENGTH_SHORT).show();
                }else {
                    addToWishListBook();
                }
            }
        });

        ivCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (database.getUserId().equals("")){
                    Intent intent = new Intent(BookDetails.this, LoginWithPhoneNumber.class);
                    startActivity(intent);
                }else {
                    Intent intent = new Intent(BookDetails.this, CartActivity.class);
                    startActivity(intent);
                }
            }
        });

        tvNotify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                notifyMe();
            }
        });

    }

    private void notifyMe() {
        final ProgressDialog loginDialog = new ProgressDialog(this);
        loginDialog.setMessage("Please wait..");
        loginDialog.setCancelable(false);
        loginDialog.show();

        Holders holders = AllApiClass.getInstance().getApi();
        Call<SubmitDataResModel> call = holders.notifyBooksToMe(database.getUserId(),bookId);
        call.enqueue(new Callback<SubmitDataResModel>() {
            @Override
            public void onResponse(Call<SubmitDataResModel> call, Response<SubmitDataResModel> response) {
                if (response.isSuccessful()){
                    if (response.body().getResponse().getCode() == 1){
                        loginDialog.dismiss();
                        Toast.makeText(BookDetails.this, "You will be notified when  book is available.", Toast.LENGTH_SHORT).show();
                        finish();
                    }else {
                        loginDialog.dismiss();
                        Toast.makeText(BookDetails.this, ""+response.body().getResponse().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }else {
                    loginDialog.dismiss();
                    Toast.makeText(BookDetails.this, "Something went wrong !", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<SubmitDataResModel> call, Throwable t) {
                loginDialog.dismiss();
                Toast.makeText(BookDetails.this, ""+t, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addToCart(String cartFor) {
        final ProgressDialog loginDialog = new ProgressDialog(this);
        loginDialog.setMessage("Please wait..");
        loginDialog.setCancelable(false);
        loginDialog.show();

        Holders holders = AllApiClass.getInstance().getApi();
        Call<SubmitDataResModel> call = holders.addToCartList(bookId,database.getUserId(),"1",cartFor);
        call.enqueue(new Callback<SubmitDataResModel>() {
            @Override
            public void onResponse(Call<SubmitDataResModel> call, Response<SubmitDataResModel> response) {
                if (response.isSuccessful()){
                    if (response.body().getResponse().getCode() == 1){
                        loginDialog.dismiss();
                        editor.putInt("cart_count",cartCount+1);
                        editor.commit();
                        cartCount = sharedpreferences.getInt("cart_count",0);
                        tvCartCount.setVisibility(View.VISIBLE);
                        if (cartCount < 10){
                            tvCartCount.setText(String.valueOf(cartCount));
                        }else {
                            tvCartCount.setText("9+");
                        }
                        ivCart.setColorFilter(getResources().getColor(R.color.colorPrimary));
                        Toast.makeText(BookDetails.this, "Book added in cart successfully.", Toast.LENGTH_SHORT).show();

                        /*Intent intent = new Intent(BookDetails.this,CartActivity.class);
                        startActivity(intent);
                        finish();*/

                    }else {
                        loginDialog.dismiss();
                        Toast.makeText(BookDetails.this, ""+response.body().getResponse().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }else {
                    loginDialog.dismiss();
                    Toast.makeText(BookDetails.this, "Something went wrong !", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<SubmitDataResModel> call, Throwable t) {
                loginDialog.dismiss();
                Toast.makeText(BookDetails.this, ""+t, Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void addToWishListBook() {
        final ProgressDialog loginDialog = new ProgressDialog(this);
        loginDialog.setMessage("Please wait..");
        loginDialog.setCancelable(false);
        loginDialog.show();

        Holders holders = AllApiClass.getInstance().getApi();
        Call<AddToWishListModel> call = holders.addToWishList(database.getUserId(),bookId);
        call.enqueue(new Callback<AddToWishListModel>() {
            @Override
            public void onResponse(Call<AddToWishListModel> call, Response<AddToWishListModel> response) {
                if (response.isSuccessful()){
                    if (response.body().getResponse().getCode() == 1){
                        loginDialog.dismiss();
                        WishListActivity.flag = 1;
                        if (response.body().getResponse().getWishlist_status().equals("1")){
                            ivWishList.setImageResource(R.drawable.ic_fill_fav);
                        }else {
                            ivWishList.setImageResource(R.drawable.ic_fav);
                        }
                    }else {
                        loginDialog.dismiss();
                        Toast.makeText(BookDetails.this, ""+response.body().getResponse().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }else {
                    loginDialog.dismiss();
                    Toast.makeText(BookDetails.this, "Something went wrong !", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AddToWishListModel> call, Throwable t) {
                loginDialog.dismiss();
                Toast.makeText(BookDetails.this, ""+t, Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void collectBook() {
        final ProgressDialog loginDialog = new ProgressDialog(this);
        loginDialog.setMessage("Please wait..");
        loginDialog.setCancelable(false);
        loginDialog.show();
        //Toast.makeText(ctx, ""+database.getUserId()+"\n"+bookModels.get(getAdapterPosition()).getBook_id(), Toast.LENGTH_SHORT).show();
        Holders holders = AllApiClass.getInstance().getApi();
        Call<SubmitDataResModel> call = holders.collectBook(database.getUserId(),bookId);
        call.enqueue(new Callback<SubmitDataResModel>() {
            @Override
            public void onResponse(Call<SubmitDataResModel> call, Response<SubmitDataResModel> response) {
                if (response.isSuccessful()){
                    if (response.body().getResponse().getCode() == 1){
                        loginDialog.dismiss();
                        Helper.showToast(BookDetails.this,response.body().getResponse().getMessage());
                        mBookNowBtn.setEnabled(false);
                        mBookNowBtn.setText("Issued");
                        mBookNowBtn.setBackgroundColor(Color.parseColor("#cccccc"));
                        CollectBookActivity.flag = 1;
                    }else{
                        loginDialog.dismiss();
                        Helper.showToast(BookDetails.this,response.body().getResponse().getMessage());
                    }
                }else {
                    loginDialog.dismiss();
                    Helper.showToast(BookDetails.this,"Something went wrong !");
                }
            }

            @Override
            public void onFailure(Call<SubmitDataResModel> call, Throwable t) {
                Helper.showToast(BookDetails.this,""+t);
                loginDialog.dismiss();
            }
        });
    }

    private void showBookDetails(String bookId) {
        mProgressBar.setVisibility(View.VISIBLE);
        mNoDataLl.setVisibility(View.GONE);
        Holders holders = AllApiClass.getInstance().getApi();
        Call<BookDetailsModel> responseCall = holders.getBookDetails(database.getUserId(),bookId);
        responseCall.enqueue(new Callback<BookDetailsModel>() {
            @Override
            public void onResponse(Call<BookDetailsModel> call, Response<BookDetailsModel> response) {
                if (response.isSuccessful()){
                    mProgressBar.setVisibility(View.GONE);
                    if (response.body().getResponse().getCode().equals("1")){

                        mNoDataLl.setVisibility(View.GONE);
                        mNoDataBody.setVisibility(View.GONE);
                        ll_buttons.setVisibility(View.VISIBLE);
                        mBookNameTv.setText(response.body().getResponse().getBook_list().getBook_name());
                        if (Constants.isStackUpload.equals("1")){
                            mAuthorNameTv.setVisibility(View.GONE);
                            rlMrp.setVisibility(View.GONE);
                        }else {
                            mAuthorNameTv.setVisibility(View.VISIBLE);
                            rlMrp.setVisibility(View.VISIBLE);
                            mAuthorNameTv.setText("By "+response.body().getResponse().getBook_list().getAuthor_name());
                        }

                        mDescriptionTv.setText(response.body().getResponse().getBook_list().getDescription());

                        if (response.body().getResponse().getBook_list().getLibraries_name().equals("")){
                            tv_community.setText("Open");
                        }else {
                            tv_community.setText(response.body().getResponse().getBook_list().getLibraries_name());
                        }

                        bookOwnerId = response.body().getResponse().getBook_list().getOwner_id();

                        if (response.body().getResponse().getBook_list().getAverage_ratings().equals("nan")){
                            mRatingView.setRating(Float.parseFloat("1.0"));
                            mRatingTv.setText("1.0");
                        }else {
                            mRatingView.setRating(Float.parseFloat(response.body().getResponse().getBook_list().getAverage_ratings()));
                            mRatingTv.setText(response.body().getResponse().getBook_list().getAverage_ratings());
                        }

                        if (response.body().getResponse().getBook_list().getMrp().equals("0")){
                            tvMrp.setText("MRP : Free");
                        }else {
                            tvMrp.setText("MRP : \u20B9"+response.body().getResponse().getBook_list().getMrp());
                        }


                        if (response.body().getResponse().getBook_list().getSale_price().equals("") || response.body().getResponse().getBook_list().getSale_price().equals("null")){
                            tvSelePrice.setText("");
                        }else {
                            if (response.body().getResponse().getBook_list().getSale_price().equals("0")){
                                tvSelePrice.setText("Free");
                            }else {
                                tvSelePrice.setText("\u20B9 "+response.body().getResponse().getBook_list().getSale_price());
                            }

                        }

                        if (response.body().getResponse().getBook_list().getRental_price().equals("") || response.body().getResponse().getBook_list().getRental_price().equals("null")){
                            tvRentPrice.setText("");
                        }else {
                            if (response.body().getResponse().getBook_list().getRental_price().equals("0")){
                                tvRentPrice.setText("Free");
                            }else {
                                tvRentPrice.setText("\u20B9 "+response.body().getResponse().getBook_list().getRental_price());
                            }
                        }

                        if (response.body().getResponse().getBook_list().getGiveaway().equals("yes")){
                            mBuyNowBtn.setBackgroundTintList(getResources().getColorStateList(R.color.colorPrimary));
                            mBuyNowBtn.setEnabled(true);
                            mBuyNowBtn.setVisibility(View.VISIBLE);
                            mBookNowBtn.setVisibility(View.GONE);
                            mBookNowBtn.setBackgroundTintList(getResources().getColorStateList(R.color.rect_color));
                            mBookNowBtn.setEnabled(false);
                        }else {
                            if (response.body().getResponse().getBook_list().getSelling_type().equals("Both")){
                                llRentPrice.setVisibility(View.VISIBLE);
                                llSalePrice.setVisibility(View.VISIBLE);
                                mBuyNowBtn.setBackgroundTintList(getResources().getColorStateList(R.color.colorPrimary));
                                mBuyNowBtn.setEnabled(true);
                                mBookNowBtn.setBackgroundTintList(getResources().getColorStateList(R.color.colorPrimary));
                                mBookNowBtn.setEnabled(true);
                                mBuyNowBtn.setVisibility(View.VISIBLE);
                                mBookNowBtn.setVisibility(View.VISIBLE);
                            }else if (response.body().getResponse().getBook_list().getSelling_type().equals("For Rent")){
                                llRentPrice.setVisibility(View.VISIBLE);
                                llSalePrice.setVisibility(View.GONE);
                                mBuyNowBtn.setBackgroundTintList(getResources().getColorStateList(R.color.rect_color));
                                mBuyNowBtn.setEnabled(false);
                                mBuyNowBtn.setVisibility(View.GONE);
                                mBookNowBtn.setVisibility(View.VISIBLE);
                            }else if (response.body().getResponse().getBook_list().getSelling_type().equals("For Sale")){
                                llRentPrice.setVisibility(View.GONE);
                                llSalePrice.setVisibility(View.VISIBLE);
                                mBookNowBtn.setBackgroundTintList(getResources().getColorStateList(R.color.rect_color));
                                mBookNowBtn.setEnabled(false);
                                mBuyNowBtn.setVisibility(View.VISIBLE);
                                mBookNowBtn.setVisibility(View.GONE);
                            }
                        }


                        if (response.body().getResponse().getBook_list().getQuantity().equals("0")){
                            bookOutOfStock.setVisibility(View.VISIBLE);
                            tvOutOfStock.setVisibility(View.VISIBLE);
                            ll_buttons.setVisibility(View.GONE);
                        }else {
                            bookOutOfStock.setVisibility(View.GONE);
                            tvOutOfStock.setVisibility(View.GONE);
                            ll_buttons.setVisibility(View.VISIBLE);
                        }

                        if (!database.getUserId().equals("")){
                            if (response.body().getResponse().getBook_list().getWishlist_status().equals("1")){
                                ivWishList.setImageResource(R.drawable.ic_fill_fav);
                            }else {
                                ivWishList.setImageResource(R.drawable.ic_fav);
                            }
                        }

                        myList = response.body().getResponse().getBook_list().getReviews();

                        if (myList.size() > 0){
                            adapter = new ReviewAdapter(myList, BookDetails.this);
                            LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
                            llm.setOrientation(LinearLayoutManager.VERTICAL);
                            mReviewRv.setLayoutManager(llm);
                            mReviewRv.setAdapter(adapter);
                        }

                    }else {
                        mNoDataLl.setVisibility(View.VISIBLE);
                        mNoDataBody.setVisibility(View.VISIBLE);
                        ll_buttons.setVisibility(View.GONE);
                        Toast.makeText(BookDetails.this, ""+response.body().getResponse().getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }else {
                    mProgressBar.setVisibility(View.GONE);
                    mNoDataBody.setVisibility(View.VISIBLE);
                    mNoDataLl.setVisibility(View.GONE);
                    ll_buttons.setVisibility(View.GONE);
                    Toast.makeText(BookDetails.this, "Something went wrong !", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<BookDetailsModel> call, Throwable t) {
                mProgressBar.setVisibility(View.GONE);
                mNoDataLl.setVisibility(View.GONE);
                mNoDataBody.setVisibility(View.VISIBLE);
                ll_buttons.setVisibility(View.GONE);
                Toast.makeText(BookDetails.this, "Check Your Internet Connection !", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void onBack(View view) {
        onBackPressed();
    }

    @Override
    protected void onResume() {
        /*editor.putInt("cart_count",cartCount);
        editor.commit();*/
        cartCount = sharedpreferences.getInt("cart_count",0);
        if (cartCount > 0){
            tvCartCount.setVisibility(View.VISIBLE);
            ivCart.setColorFilter(getResources().getColor(R.color.colorPrimary));
            if (cartCount < 10){
                tvCartCount.setText(String.valueOf(cartCount));
            }else {
                tvCartCount.setText("9+");
            }
        }else {
            editor.putInt("cart_count",0);
            editor.commit();
            tvCartCount.setVisibility(View.INVISIBLE);
            ivCart.setColorFilter(null);
        }
        Log.d("myCart", String.valueOf(cartCount));
        super.onResume();
    }
}