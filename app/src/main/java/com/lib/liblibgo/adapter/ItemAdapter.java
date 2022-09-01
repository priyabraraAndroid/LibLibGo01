package com.lib.liblibgo.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.lib.liblibgo.R;
import com.lib.liblibgo.api.AllApiClass;
import com.lib.liblibgo.api.Holders;
import com.lib.liblibgo.model.MyOrderDetailsModel;
import com.lib.liblibgo.model.SubmitDataResModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.MyViewHolder> {
    List<MyOrderDetailsModel.ResData.LibraryList.MyOrderItemList> mList;
    Context mCtx;

    public ItemAdapter(List<MyOrderDetailsModel.ResData.LibraryList.MyOrderItemList> mList, Context mCtx) {
        this.mList = mList;
        this.mCtx = mCtx;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(mCtx).inflate(R.layout.item_order_ist, parent, false);
        return new MyViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.bind();
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_book, tv_author, tv_price,tvReturnBooks;
        ImageView vi_book;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_book = itemView.findViewById(R.id.tv_book);
            tv_author = itemView.findViewById(R.id.tv_author);
            tv_price = itemView.findViewById(R.id.tv_price);
            vi_book = itemView.findViewById(R.id.vi_book);
            tvReturnBooks = itemView.findViewById(R.id.tvReturnBooks);

        }

        public void bind() {
            tv_book.setText(mList.get(getAdapterPosition()).getBook_name());
            tv_author.setText("By " + mList.get(getAdapterPosition()).getAuthor_name());
            if (mList.get(getAdapterPosition()).getBook_image().equals("")) {
                Glide.with(mCtx).load("https://sales.arecontvision.com/images/products/img_placeholder_50618_xl.jpg").into(vi_book);
            } else {
                Glide.with(mCtx).load(mList.get(getAdapterPosition()).getBook_image()).into(vi_book);
            }
            tv_price.setText("\u20B9 " + mList.get(getAdapterPosition()).getPrice());

            if (mList.get(getAdapterPosition()).getReturn_status().equals("0")){
                tvReturnBooks.setText("Return");
                tvReturnBooks.setEnabled(true);
                tvReturnBooks.setBackground(mCtx.getResources().getDrawable(R.drawable.btn_bg));
            }else {
                tvReturnBooks.setText("Returned");
                tvReturnBooks.setEnabled(false);
                tvReturnBooks.setBackground(mCtx.getResources().getDrawable(R.drawable.btn_bg_grey));
            }

            if (mList.get(getAdapterPosition()).getOrder_for().equals("rent") ){
                if (mList.get(getAdapterPosition()).getReturn_status().equals("1")){
                    tvReturnBooks.setVisibility(View.GONE);
                }else {
                    tvReturnBooks.setVisibility(View.GONE);
                }
            }else {
                tvReturnBooks.setVisibility(View.GONE);
            }

            tvReturnBooks.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final ProgressDialog progressBar = new ProgressDialog(mCtx);
                    progressBar.setMessage("Please wait...");
                    progressBar.setCancelable(false);
                    progressBar.show();
                    Holders holders = AllApiClass.getInstance().getApi();
                    Call<SubmitDataResModel> resCall = holders.returnBook(mList.get(getAdapterPosition()).getOrder_details_id());
                    resCall.enqueue(new Callback<SubmitDataResModel>() {
                        @Override
                        public void onResponse(Call<SubmitDataResModel> call, Response<SubmitDataResModel> response) {
                            if (response.isSuccessful()){
                                if (response.body().getResponse().getCode() == 1){
                                    progressBar.dismiss();
                                    tvReturnBooks.setText("Returned");
                                    tvReturnBooks.setEnabled(false);
                                    tvReturnBooks.setBackground(mCtx.getResources().getDrawable(R.drawable.btn_bg_grey));
                                    Toast.makeText(mCtx, ""+response.body().getResponse().getMessage(), Toast.LENGTH_SHORT).show();
                                }else {
                                    progressBar.dismiss();
                                    Toast.makeText(mCtx, ""+response.body().getResponse().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }else {
                                progressBar.dismiss();
                                Toast.makeText(mCtx, "Something went wrong !", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<SubmitDataResModel> call, Throwable t) {
                            progressBar.dismiss();
                            Toast.makeText(mCtx, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        }
    }
}
