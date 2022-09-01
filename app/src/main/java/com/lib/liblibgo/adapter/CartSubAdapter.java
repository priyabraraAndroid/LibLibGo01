package com.lib.liblibgo.adapter;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.lib.liblibgo.R;
import com.lib.liblibgo.activity.homedashboard.HomeActivity;
import com.lib.liblibgo.api.AllApiClass;
import com.lib.liblibgo.api.Holders;
import com.lib.liblibgo.dashboard.book_details.BookDetails;
import com.lib.liblibgo.listner.CartAddDaysListener;
import com.lib.liblibgo.listner.OnItemClickListener;
import com.lib.liblibgo.model.CartModel;
import com.lib.liblibgo.model.SubmitDataResModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartSubAdapter extends RecyclerView.Adapter<CartSubAdapter.CartListHolder> {
    List<CartModel.ResDataBooks.CartListData.CartBookList> mSubList;
    Context mCtx;
    private int durationDays = 5;
    private SharedPreferences sharedpreferences;
    private SharedPreferences.Editor editor;
    private int cartCount;

    public CartSubAdapter(List<CartModel.ResDataBooks.CartListData.CartBookList> mSubList, Context mCtx) {
        this.mSubList = mSubList;
        this.mCtx = mCtx;
    }

    @NonNull
    @Override
    public CartListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(mCtx).inflate(R.layout.sub_cartlist_row, parent, false);
        return new CartListHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull CartListHolder holder, int position) {
        holder.bind();
    }

    @Override
    public int getItemCount() {
        return mSubList.size();
    }

    public class CartListHolder extends RecyclerView.ViewHolder {
        TextView tv_book,tv_author,tv_price,tv_cart_for,tv_qty;
        ImageView vi_book,tv_decrese_qty,iv_increse_qty;
        ImageView ivDeleteBook;
        LinearLayout layoutCount;

        public CartListHolder(@NonNull View itemView) {
            super(itemView);
            tv_book = itemView.findViewById(R.id.tv_book);
            tv_qty = itemView.findViewById(R.id.tv_qty);
            tv_author = itemView.findViewById(R.id.tv_author);
            tv_price = itemView.findViewById(R.id.tv_price);
            tv_cart_for = itemView.findViewById(R.id.tv_cart_for);
            vi_book = itemView.findViewById(R.id.vi_book);
            tv_decrese_qty = itemView.findViewById(R.id.tv_decrese_qty);
            iv_increse_qty = itemView.findViewById(R.id.iv_increse_qty);
            ivDeleteBook = itemView.findViewById(R.id.ivDeleteBook);
            layoutCount = itemView.findViewById(R.id.layoutCount);
        }

        public void bind() {

            sharedpreferences = mCtx.getSharedPreferences(HomeActivity.MyPREFERENCES, Context.MODE_PRIVATE);
            editor = sharedpreferences.edit();

            tv_book.setText(mSubList.get(getAdapterPosition()).getBook_name());
            tv_author.setText("By " + mSubList.get(getAdapterPosition()).getAuthor_name());

            if (mSubList.get(getAdapterPosition()).getBook_image().equals("") || mSubList.get(getAdapterPosition()).getBook_image().equals("null")) {
                Glide.with(mCtx).load("https://sales.arecontvision.com/images/products/img_placeholder_50618_xl.jpg").into(vi_book);
            } else {
                Glide.with(mCtx).load(mSubList.get(getAdapterPosition()).getBook_image()).into(vi_book);
            }

            if (mSubList.get(getAdapterPosition()).getBook_price().equals("0")){
                tv_price.setText("Free");
            }else {
                tv_price.setText("\u20B9 "+mSubList.get(getAdapterPosition()).getBook_price());
            }

            //tv_cart_for.setText(mSubList.get(getAdapterPosition()).getCart_for());
            if (mSubList.get(getAdapterPosition()).getCart_for().equals("rent")){
                tv_cart_for.setText("Rent Days ");
                layoutCount.setVisibility(View.VISIBLE);
                tv_qty.setText(mSubList.get(getAdapterPosition()).getCart_rent_duration());
            }else {
                tv_cart_for.setText("Purchase");
                layoutCount.setVisibility(View.GONE);
                tv_qty.setText("1");
            }

            if (mSubList.size() > 1){
                ivDeleteBook.setVisibility(View.VISIBLE);
            }else {
                ivDeleteBook.setVisibility(View.INVISIBLE);
            }

            ivDeleteBook.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    deleteCart(R.layout.delete_popup,mSubList.get(getAdapterPosition()).getCart_id(),getAdapterPosition());
                }
            });

            tv_decrese_qty.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //if ()
                    durationDays = Integer.parseInt(String.valueOf(tv_qty.getText()));
                    if (durationDays > 5){
                        durationDays--;
                        tv_qty.setText(Integer.toString(durationDays));
                        final ProgressDialog loginDialog = new ProgressDialog(mCtx);
                        loginDialog.setMessage("Please wait..");
                        loginDialog.setCancelable(false);
                        loginDialog.show();
                        Holders holders = AllApiClass.getInstance().getApi();
                        Call<SubmitDataResModel> call = holders.updateCart(mSubList.get(getAdapterPosition()).cart_id,String.valueOf(durationDays));
                        call.enqueue(new Callback<SubmitDataResModel>() {
                            @Override
                            public void onResponse(Call<SubmitDataResModel> call, Response<SubmitDataResModel> response) {
                                if (response.isSuccessful()){
                                    if (response.body().getResponse().getCode() == 1){
                                        loginDialog.dismiss();
                                        tv_price.setText("\u20B9 "+ (durationDays * Integer.parseInt(mSubList.get(getAdapterPosition()).getDefault_price())));
                                        //Toast.makeText(mCtx, "Notify you,when book is available.", Toast.LENGTH_SHORT).show();
                                    }else {
                                        loginDialog.dismiss();
                                        Toast.makeText(mCtx, ""+response.body().getResponse().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }else {
                                    loginDialog.dismiss();
                                    Toast.makeText(mCtx, "Something went wrong !", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<SubmitDataResModel> call, Throwable t) {
                                loginDialog.dismiss();
                                Toast.makeText(mCtx, ""+t, Toast.LENGTH_SHORT).show();
                            }
                        });
                        iv_increse_qty.setEnabled(true);
                    }else {
                        durationDays = 5;
                        Toast.makeText(mCtx, "Minimum duration is 5 days.", Toast.LENGTH_SHORT).show();
                        tv_decrese_qty.setEnabled(false);
                    }
                }
            });

            iv_increse_qty.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    durationDays = Integer.parseInt(String.valueOf(tv_qty.getText()));
                    if (durationDays < Integer.parseInt(mSubList.get(getAdapterPosition()).getBook_rent_duration())) {
                        durationDays++;
                        tv_qty.setText(Integer.toString(durationDays));
                        final ProgressDialog loginDialog = new ProgressDialog(mCtx);
                        loginDialog.setMessage("Please wait..");
                        loginDialog.setCancelable(false);
                        loginDialog.show();
                        Holders holders = AllApiClass.getInstance().getApi();
                        Call<SubmitDataResModel> call = holders.updateCart(mSubList.get(getAdapterPosition()).cart_id,String.valueOf(durationDays));
                        call.enqueue(new Callback<SubmitDataResModel>() {
                            @Override
                            public void onResponse(Call<SubmitDataResModel> call, Response<SubmitDataResModel> response) {
                                if (response.isSuccessful()){
                                    if (response.body().getResponse().getCode() == 1){
                                        loginDialog.dismiss();
                                        tv_price.setText("\u20B9 "+ (durationDays * Integer.parseInt(mSubList.get(getAdapterPosition()).getDefault_price())));
                                        //Toast.makeText(mCtx, "Notify you,when book is available.", Toast.LENGTH_SHORT).show();
                                    }else {
                                        loginDialog.dismiss();
                                        Toast.makeText(mCtx, ""+response.body().getResponse().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }else {
                                    loginDialog.dismiss();
                                    Toast.makeText(mCtx, "Something went wrong !", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<SubmitDataResModel> call, Throwable t) {
                                loginDialog.dismiss();
                                Toast.makeText(mCtx, ""+t, Toast.LENGTH_SHORT).show();
                            }
                        });
                        tv_decrese_qty.setEnabled(true);
                    }else {
                        Toast.makeText(mCtx, "Duration limited by lender.", Toast.LENGTH_SHORT).show();
                        iv_increse_qty.setEnabled(false);
                    }
                }
            });
        }
    }


    private void deleteCart(int layout, String cart_id, int adapterPosition) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(mCtx);
        LayoutInflater inflater = (LayoutInflater)mCtx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View layoutView = inflater.inflate(layout, null);
        final TextView tv_yes = layoutView.findViewById(R.id.tv_yes);
        final TextView tv_no = layoutView.findViewById(R.id.tv_no);
        final TextView tv_txt = layoutView.findViewById(R.id.tv_txt);

        tv_txt.setText("Do you want to delete this cart item ?");

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
                final ProgressDialog loginDialog = new ProgressDialog(mCtx);
                loginDialog.setMessage("Please wait..");
                loginDialog.setCancelable(false);
                loginDialog.show();

                Holders holders = AllApiClass.getInstance().getApi();
                Call<SubmitDataResModel> call = holders.removeCart("cart",cart_id);
                call.enqueue(new Callback<SubmitDataResModel>() {
                    @Override
                    public void onResponse(Call<SubmitDataResModel> call, Response<SubmitDataResModel> response) {
                        if (response.isSuccessful()){
                            if (response.body().getResponse().getCode() == 1){
                                loginDialog.dismiss();
                                alertDialog.dismiss();
                                mSubList.remove(adapterPosition);
                                notifyItemRemoved(adapterPosition);
                                notifyItemRangeChanged(adapterPosition, mSubList.size());
                                cartCount = sharedpreferences.getInt("cart_count",0);
                                cartCount = cartCount - 1;
                                editor.putInt("cart_count",cartCount);
                                editor.commit();
                            }else {
                                loginDialog.dismiss();
                                Toast.makeText(mCtx, ""+response.body().getResponse().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }else {
                            loginDialog.dismiss();
                            Toast.makeText(mCtx, "Something went wrong !", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<SubmitDataResModel> call, Throwable t) {
                        loginDialog.dismiss();
                        Toast.makeText(mCtx, ""+t, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}