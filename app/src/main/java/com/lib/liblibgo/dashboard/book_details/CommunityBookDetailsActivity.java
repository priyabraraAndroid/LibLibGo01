package com.lib.liblibgo.dashboard.book_details;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
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
import com.lib.liblibgo.adapter.ReviewAdapter;
import com.lib.liblibgo.api.AllApiClass;
import com.lib.liblibgo.api.Holders;
import com.lib.liblibgo.common.Helper;
import com.lib.liblibgo.common.UserDatabase;
import com.lib.liblibgo.dashboard.book_collect.CollectApartmentBooksActivity;
import com.lib.liblibgo.dashboard.book_collect.CollectBookActivity;
import com.lib.liblibgo.model.BookDetailsModel;
import com.lib.liblibgo.model.SubmitDataResModel;

import java.util.ArrayList;
import java.util.List;

import me.zhanghai.android.materialratingbar.MaterialRatingBar;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommunityBookDetailsActivity extends AppCompatActivity {

    private String bookId = "";
    private TextView mBookNameTv;
    private TextView mAuthorNameTv;
    private TextView mDescriptionTv;
    private TextView mRatingTv;
    private MaterialRatingBar mRatingView;
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
    private Button mIssueNowBtn;
    private LinearLayout bookOutOfStock;
    private ImageView ivWishList;
    private TextView tvNotify;
    private String addedBy = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community_book_details);

        bookId = getIntent().getStringExtra("book_id");
        bookImage = getIntent().getStringExtra("book_img");
        database = new UserDatabase(this);

        mBookNameTv = (TextView)findViewById(R.id.book_name);
        mAuthorNameTv = (TextView)findViewById(R.id.book_author);
        mDescriptionTv = (TextView)findViewById(R.id.tv_details);
        mRatingTv = (TextView)findViewById(R.id.tv_rate);
        mRatingView = (MaterialRatingBar)findViewById(R.id.rating);
        mIssueNowBtn = (Button)findViewById(R.id.btn_book_now);
        mReviewRv = (RecyclerView)findViewById(R.id.rv_review);
        mProgressBar = (ProgressBar)findViewById(R.id.progressBar);
        mNoDataLl = (LinearLayout)findViewById(R.id.ll_no_data);
        mNoDataBody = (RelativeLayout)findViewById(R.id.no_data);
        mBookImageIv = (ImageView)findViewById(R.id.iv_image);
        ll_buttons = (LinearLayout)findViewById(R.id.ll_buttons);
        mRatingView.setEnabled(false);

        addedBy = getIntent().getStringExtra("added_by");

        if (bookImage.equals("")){
            Glide.with(getApplicationContext()).load("https://sales.arecontvision.com/images/products/img_placeholder_50618_xl.jpg").into(mBookImageIv);
        }else {
            Glide.with(getApplicationContext()).load(bookImage).into(mBookImageIv);
        }

        showBookDetails(bookId);

        tvNotify = (TextView)findViewById(R.id.tvNotify);
        bookOutOfStock = (LinearLayout)findViewById(R.id.bookOutOfStock);

        ivWishList = (ImageView)findViewById(R.id.ivWishList);


        mIssueNowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (database.getUserId().equals("")){
                    Intent intent = new Intent(CommunityBookDetailsActivity.this, LoginWithPhoneNumber.class);
                    startActivity(intent);
                }else {
                    if (addedBy.equals(database.getUserId())){
                        Toast.makeText(CommunityBookDetailsActivity.this, "This your book.", Toast.LENGTH_SHORT).show();
                    }else {
                        collectBook();
                    }
                }
            }
        });

        /*ivWishList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (database.getUserId().equals("")){
                    Intent intent = new Intent(CommunityBookDetailsActivity.this, LoginWithPhoneNumber.class);
                    startActivity(intent);
                }else {
                    addToWishListBook();
                }
            }
        });*/

        tvNotify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                notifyMe();
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
                        mAuthorNameTv.setText("By "+response.body().getResponse().getBook_list().getAuthor_name());
                        mDescriptionTv.setText(response.body().getResponse().getBook_list().getDescription());

                        if (response.body().getResponse().getBook_list().getAverage_ratings().equals("nan")){
                            mRatingView.setRating(Float.parseFloat("1.0"));
                            mRatingTv.setText("1.0");
                        }else {
                            mRatingView.setRating(Float.parseFloat(response.body().getResponse().getBook_list().getAverage_ratings()));
                            mRatingTv.setText(response.body().getResponse().getBook_list().getAverage_ratings());
                        }

                        /*if (response.body().getResponse().getBook_list().getMrp().equals("0")){
                            tvMrp.setText("MRP : Free");
                        }else {
                            tvMrp.setText("MRP : \u20B9"+response.body().getResponse().getBook_list().getMrp());
                        }*/


                        /*if (response.body().getResponse().getBook_list().getSale_price().equals("") || response.body().getResponse().getBook_list().getSale_price().equals("null")){
                            tvSelePrice.setText("");
                        }else {
                            if (response.body().getResponse().getBook_list().getSale_price().equals("0")){
                                tvSelePrice.setText("Free");
                            }else {
                                tvSelePrice.setText("\u20B9 "+response.body().getResponse().getBook_list().getSale_price());
                            }

                        }*/

                        /*if (response.body().getResponse().getBook_list().getRental_price().equals("") || response.body().getResponse().getBook_list().getRental_price().equals("null")){
                            tvRentPrice.setText("");
                        }else {
                            if (response.body().getResponse().getBook_list().getRental_price().equals("0")){
                                tvRentPrice.setText("Free");
                            }else {
                                tvRentPrice.setText("\u20B9 "+response.body().getResponse().getBook_list().getRental_price());
                            }
                        }*/

                        /*if (response.body().getResponse().getBook_list().getSelling_type().equals("Both")){
                            llRentPrice.setVisibility(View.VISIBLE);
                            llSalePrice.setVisibility(View.VISIBLE);
                            mBuyNowBtn.setBackgroundTintList(getResources().getColorStateList(R.color.colorPrimary));
                            mBuyNowBtn.setEnabled(true);
                            mBookNowBtn.setBackgroundTintList(getResources().getColorStateList(R.color.colorPrimary));
                            mBookNowBtn.setEnabled(true);
                        }else if (response.body().getResponse().getBook_list().getSelling_type().equals("For Rent")){
                            llRentPrice.setVisibility(View.VISIBLE);
                            llSalePrice.setVisibility(View.GONE);
                            mBuyNowBtn.setBackgroundTintList(getResources().getColorStateList(R.color.rect_color));
                            mBuyNowBtn.setEnabled(false);
                        }else if (response.body().getResponse().getBook_list().getSelling_type().equals("For Sale")){
                            llRentPrice.setVisibility(View.GONE);
                            llSalePrice.setVisibility(View.VISIBLE);
                            mBookNowBtn.setBackgroundTintList(getResources().getColorStateList(R.color.rect_color));
                            mBookNowBtn.setEnabled(false);
                        }*/

                        if (response.body().getResponse().getBook_list().getQuantity().equals("0")){
                            bookOutOfStock.setVisibility(View.VISIBLE);
                            ll_buttons.setVisibility(View.GONE);
                        }else {
                            bookOutOfStock.setVisibility(View.GONE);
                            ll_buttons.setVisibility(View.VISIBLE);
                        }

                        if (!database.getUserId().equals("")){
                            if (response.body().getResponse().getBook_list().getWishlist_status().equals("1")){
                                ivWishList.setImageResource(R.drawable.ic_fill_fav);
                            }else {
                                ivWishList.setImageResource(R.drawable.ic_fav);
                            }
                        }

                        if (response.body().getResponse().getBook_list().getIssue_status().equals("1")){
                            mIssueNowBtn.setEnabled(false);
                            mIssueNowBtn.setText("Issued");
                            mIssueNowBtn.setBackground(getResources().getDrawable(R.drawable.btn_bg_grey));
                        }else {
                            mIssueNowBtn.setEnabled(true);
                            mIssueNowBtn.setText("Issue Now");
                            mIssueNowBtn.setBackground(getResources().getDrawable(R.drawable.btn_bg));
                        }

                        myList = response.body().getResponse().getBook_list().getReviews();

                        if (myList.size() > 0){
                            adapter = new ReviewAdapter(myList, CommunityBookDetailsActivity.this);
                            LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
                            llm.setOrientation(LinearLayoutManager.VERTICAL);
                            mReviewRv.setLayoutManager(llm);
                            mReviewRv.setAdapter(adapter);
                        }

                    }else {
                        mNoDataLl.setVisibility(View.VISIBLE);
                        mNoDataBody.setVisibility(View.VISIBLE);
                        ll_buttons.setVisibility(View.GONE);
                        Toast.makeText(CommunityBookDetailsActivity.this, ""+response.body().getResponse().getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }else {
                    mProgressBar.setVisibility(View.GONE);
                    mNoDataBody.setVisibility(View.VISIBLE);
                    mNoDataLl.setVisibility(View.GONE);
                    ll_buttons.setVisibility(View.GONE);
                    Toast.makeText(CommunityBookDetailsActivity.this, "Something went wrong !", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<BookDetailsModel> call, Throwable t) {
                mProgressBar.setVisibility(View.GONE);
                mNoDataLl.setVisibility(View.GONE);
                mNoDataBody.setVisibility(View.VISIBLE);
                ll_buttons.setVisibility(View.GONE);
                Toast.makeText(CommunityBookDetailsActivity.this, "Check Your Internet Connection !", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void onBack(View view) {
        onBackPressed();
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
                        Helper.showToast(CommunityBookDetailsActivity.this,response.body().getResponse().getMessage());
                        mIssueNowBtn.setEnabled(false);
                        mIssueNowBtn.setText("Issued");
                        mIssueNowBtn.setBackground(getResources().getDrawable(R.drawable.btn_bg_grey));
                        CollectApartmentBooksActivity.flag = 1;
                    }else{
                        loginDialog.dismiss();
                        Helper.showToast(CommunityBookDetailsActivity.this,response.body().getResponse().getMessage());
                    }
                }else {
                    loginDialog.dismiss();
                    Helper.showToast(CommunityBookDetailsActivity.this,"Something went wrong !");
                }
            }

            @Override
            public void onFailure(Call<SubmitDataResModel> call, Throwable t) {
                Helper.showToast(CommunityBookDetailsActivity.this,""+t);
                loginDialog.dismiss();
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
                        Toast.makeText(CommunityBookDetailsActivity.this, "You will be notified when  book is available.", Toast.LENGTH_SHORT).show();
                        finish();
                    }else {
                        loginDialog.dismiss();
                        Toast.makeText(CommunityBookDetailsActivity.this, ""+response.body().getResponse().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }else {
                    loginDialog.dismiss();
                    Toast.makeText(CommunityBookDetailsActivity.this, "Something went wrong !", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<SubmitDataResModel> call, Throwable t) {
                loginDialog.dismiss();
                Toast.makeText(CommunityBookDetailsActivity.this, ""+t, Toast.LENGTH_SHORT).show();
            }
        });
    }

}