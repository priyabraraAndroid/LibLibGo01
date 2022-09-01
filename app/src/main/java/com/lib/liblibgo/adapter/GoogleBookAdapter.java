package com.lib.liblibgo.adapter;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.lib.liblibgo.R;
import com.lib.liblibgo.adapter.subadmin.BookListAdapter;
import com.lib.liblibgo.api.AllApiClass;
import com.lib.liblibgo.api.Holders;
import com.lib.liblibgo.common.UserDatabase;
import com.lib.liblibgo.dashboard.apartment_admin.UploadBookDetails;
import com.lib.liblibgo.model.SubmitDataResModel;
import com.lib.liblibgo.model.subadmin.BookDataModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GoogleBookAdapter extends RecyclerView.Adapter<GoogleBookAdapter.CollectHolder> {

    private List<BookDataModel> bookModels = new ArrayList<>();
    private Context ctx;
    private AlertDialog alertDialog;

    public GoogleBookAdapter(List<BookDataModel> bookModels, Context context) {
        this.bookModels = bookModels;
        this.ctx = context;
    }

    @NonNull
    @Override
    public CollectHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(ctx).inflate(R.layout.google_books_row,parent,false);
        return new CollectHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CollectHolder holder, int position) {
        holder.bookBind();
    }

    @Override
    public int getItemCount() {
        return bookModels.size();
    }

    public class CollectHolder extends RecyclerView.ViewHolder {
        TextView tvBookName, tvBookAuthor,tvNotify;
        ImageView ivBook;
        public CollectHolder(@NonNull View itemView) {
            super(itemView);
            tvBookName = itemView.findViewById(R.id.tvBookName);
            tvBookAuthor = itemView.findViewById(R.id.tvBookAuthor);
            tvNotify = itemView.findViewById(R.id.tvNotify);
            ivBook = itemView.findViewById(R.id.ivBook);
        }
        public void bookBind(){
            tvBookName.setText(bookModels.get(getAdapterPosition()).getName());
            tvBookAuthor.setText("By "+bookModels.get(getAdapterPosition()).getAuthor());

            if (bookModels.get(getAdapterPosition()).getThumbnailUrl().equals("") || bookModels.get(getAdapterPosition()).getThumbnailUrl().equals("null")){
                Glide.with(ctx).load("https://sales.arecontvision.com/images/products/img_placeholder_50618_xl.jpg").into(ivBook);
            }else {
                Glide.with(ctx).load(bookModels.get(getAdapterPosition()).getThumbnailUrl()).placeholder(R.drawable.no_img).into(ivBook);
            }

            tvNotify.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    UserDatabase database = new UserDatabase(ctx);
                    final ProgressDialog progressBar = new ProgressDialog(ctx);
                    progressBar.setMessage("Please wait...");
                    progressBar.setCancelable(false);
                    progressBar.show();
                    Holders holders = AllApiClass.getInstance().getApi();
                    Call<SubmitDataResModel> resCall = holders.notifyBooksFromGoogle(database.getUserId(),bookModels.get(getAdapterPosition()).getIsbn_no(),
                            bookModels.get(getAdapterPosition()).getName(),
                            bookModels.get(getAdapterPosition()).getAuthor(),
                            bookModels.get(getAdapterPosition()).getThumbnailUrl());
                    resCall.enqueue(new Callback<SubmitDataResModel>() {
                        @Override
                        public void onResponse(Call<SubmitDataResModel> call, Response<SubmitDataResModel> response) {
                            if (response.isSuccessful()){
                                if (response.body().getResponse().getCode() == 1){
                                    progressBar.dismiss();
                                    Toast.makeText(ctx, "You will be notified when book is available.", Toast.LENGTH_SHORT).show();
                                }else {
                                    progressBar.dismiss();
                                    Toast.makeText(ctx, ""+response.body().getResponse().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }else {
                                progressBar.dismiss();
                                Toast.makeText(ctx, "Something went wrong !", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<SubmitDataResModel> call, Throwable t) {
                            progressBar.dismiss();
                            Toast.makeText(ctx, "Check your internet !", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        }
    }
}
