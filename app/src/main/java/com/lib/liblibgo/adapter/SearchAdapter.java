package com.lib.liblibgo.adapter;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.lib.liblibgo.R;
import com.lib.liblibgo.api.AllApiClass;
import com.lib.liblibgo.api.Holders;
import com.lib.liblibgo.common.UserDatabase;
import com.lib.liblibgo.dashboard.book_details.BookDetails;
import com.lib.liblibgo.model.SearchResModel;
import com.lib.liblibgo.model.SubmitDataResModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchHolder>{

    private static final String TAG = "SearchAdapter";
    private List<SearchResModel.ResDataBooks.BookListData> bookModels;
    private Context ctx;

    public SearchAdapter(List<SearchResModel.ResDataBooks.BookListData> bookModels, Context context) {
        this.bookModels = bookModels;
        this.ctx = context;
    }

    @NonNull
    @Override
    public SearchHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(ctx).inflate(R.layout.book_list_search,parent,false);
        return new SearchHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchHolder holder, int position) {
        holder.bookBind();
    }

    @Override
    public int getItemCount() {
        return bookModels.size();
    }

    public class SearchHolder extends RecyclerView.ViewHolder {
        TextView tv_book,tv_author,tv_cell,tv_issued_user,tv_retun_date;
        CardView card;
        ImageView iv_flag,vi_book;
        public SearchHolder(@NonNull View itemView) {
            super(itemView);

            tv_book = itemView.findViewById(R.id.tv_book);
            tv_author = itemView.findViewById(R.id.tv_author);
            card = itemView.findViewById(R.id.card);
            iv_flag = itemView.findViewById(R.id.iv_flag);
            vi_book = itemView.findViewById(R.id.vi_book);
            tv_cell = itemView.findViewById(R.id.tv_cell);
            tv_issued_user = itemView.findViewById(R.id.tv_issued_user);
            tv_retun_date = itemView.findViewById(R.id.tv_retun_date);
        }

        public void bookBind(){
            UserDatabase database = new UserDatabase(ctx);
            tv_book.setText(bookModels.get(getAdapterPosition()).getBook_name());
            tv_author.setText("By "+bookModels.get(getAdapterPosition()).getAuthor_name());
            tv_cell.setText("Library name : "+bookModels.get(getAdapterPosition()).getLibrary_name());
            /*if (bookModels.get(getAdapterPosition()).getCell_name().equals("") || bookModels.get(getAdapterPosition()).getCell_name().equals("null")){
                tv_cell.setVisibility(View.GONE);
            }else {
                tv_cell.setText("Shelf No - "+bookModels.get(getAdapterPosition()).getCell_name());
            }*/


            if (bookModels.get(getAdapterPosition()).getIssued_user().equals("")){
                tv_issued_user.setText("Issued By - No one");
            }else {
                tv_issued_user.setText("Issued By - "+bookModels.get(getAdapterPosition()).getIssued_user());
            }

            if (bookModels.get(getAdapterPosition()).getReturn_date().equals("")){
                tv_retun_date.setText("Book is available for you.");
            }else {
                tv_retun_date.setText("Return Date - "+convertTime(bookModels.get(getAdapterPosition()).getReturn_date()));
            }


            if (bookModels.get(getAdapterPosition()).getFlag_status().equals("1")){
                iv_flag.setColorFilter(ContextCompat.getColor(ctx, R.color.green), android.graphics.PorterDuff.Mode.SRC_IN);
            }else if (bookModels.get(getAdapterPosition()).getFlag_status().equals("0")){
                iv_flag.setColorFilter(ContextCompat.getColor(ctx, R.color.red), android.graphics.PorterDuff.Mode.SRC_IN);
            }else if (bookModels.get(getAdapterPosition()).getFlag_status().equals("2")){
                iv_flag.setColorFilter(ContextCompat.getColor(ctx, R.color.yellow), android.graphics.PorterDuff.Mode.SRC_IN);
            }

            if (bookModels.get(getAdapterPosition()).getImage_url().equals("")){
                Glide.with(ctx).load("https://sales.arecontvision.com/images/products/img_placeholder_50618_xl.jpg").into(vi_book);
            }else {
                Glide.with(ctx).load(bookModels.get(getAdapterPosition()).getImage_url()).into(vi_book);
            }

            iv_flag.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (bookModels.get(getAdapterPosition()).getFlag_status().equals("1")){
                        //Toast.makeText(ctx, "", Toast.LENGTH_LONG).show();
                        //Helper.showCustomToast(ctx,"Book is available for you.",R.color.green);
                        showGreenPopup(R.layout.book_info_green_pop,bookModels.get(getAdapterPosition()).getBook_id(),bookModels.get(getAdapterPosition()).getImage_url());
                    }else if (bookModels.get(getAdapterPosition()).getFlag_status().equals("0")){
                        //Helper.showCustomToast(ctx,"Book is not available yet!",R.color.red);
                        showRedPopup(R.layout.book_info_red_pop,bookModels.get(getAdapterPosition()).getBook_id(),bookModels.get(getAdapterPosition()).getReturn_date(),bookModels.get(getAdapterPosition()).getIssued_user(),database.getUserId());
                    }else if (bookModels.get(getAdapterPosition()).getFlag_status().equals("2")){
                        //Helper.showCustomToast(ctx,"Book is over dated.",R.color.yellow);
                        showYellowPopup(R.layout.book_info_yellow_pop,bookModels.get(getAdapterPosition()).getBook_id(),bookModels.get(getAdapterPosition()).getIssued_user(),database.getUserId());
                    }
                }
            });

            card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(ctx, BookDetails.class);
                    intent.putExtra("book_id",bookModels.get(getAdapterPosition()).getBook_id());
                    intent.putExtra("book_img",bookModels.get(getAdapterPosition()).getImage_url());
                    ctx.startActivity(intent);
                }
            });
        }
    }

    private void showYellowPopup(int layout, String book_id, String issued_user, String userId) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(ctx);
        LayoutInflater inflater = (LayoutInflater)ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View layoutView = inflater.inflate(layout, null);
        final Button btn_done = layoutView.findViewById(R.id.btn_done);
        final TextView tv_txt = layoutView.findViewById(R.id.tv_txt);

        dialogBuilder.setView(layoutView);
        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();
        alertDialog.setCancelable(true);
        alertDialog.getWindow().setLayout(800, ViewGroup.LayoutParams.WRAP_CONTENT);

        //tv_txt.setText("This book is already issued by "+issued_user+" & he well return this book by "+convertTime(return_date)+".");
        tv_txt.setText("This book is already issued by "+issued_user+" but he is not return this book on time.Notify admin to this book request.");

        btn_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //alertDialog.dismiss();
                sendBookRequestNotification(book_id,userId,alertDialog);
            }
        });
    }

    private void sendBookRequestNotification(String book_id, String userId, AlertDialog alertDialog) {
        final ProgressDialog progressBar = new ProgressDialog(ctx);
        progressBar.setMessage("Please wait...");
        progressBar.setCancelable(false);
        progressBar.show();
        Holders holders = AllApiClass.getInstance().getApi();
        Call<SubmitDataResModel> resCall = holders.sendBookRequest(userId,book_id);
        resCall.enqueue(new Callback<SubmitDataResModel>() {
            @Override
            public void onResponse(Call<SubmitDataResModel> call, Response<SubmitDataResModel> response) {
                if (response.isSuccessful()){
                    if (response.body().getResponse().getCode() == 1){
                        progressBar.dismiss();
                        alertDialog.dismiss();
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

    private void showRedPopup(int layout, String book_id, String return_date, String issued_user,String user_id) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(ctx);
        LayoutInflater inflater = (LayoutInflater)ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View layoutView = inflater.inflate(layout, null);
        final Button btn_done = layoutView.findViewById(R.id.btn_done);
        final TextView tv_txt = layoutView.findViewById(R.id.tv_txt);

        dialogBuilder.setView(layoutView);
        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();
        alertDialog.setCancelable(true);
        alertDialog.getWindow().setLayout(800, ViewGroup.LayoutParams.WRAP_CONTENT);

        tv_txt.setText("This book is already issued by "+issued_user+" & he well return this book by "+convertTime(return_date)+".");

        btn_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
    }

    private void showGreenPopup(int layout,String bookId, String imgUrl) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(ctx);
        LayoutInflater inflater = (LayoutInflater)ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View layoutView = inflater.inflate(layout, null);
        final Button btn_done = layoutView.findViewById(R.id.btn_done);

        dialogBuilder.setView(layoutView);
        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();
        alertDialog.setCancelable(true);
        alertDialog.getWindow().setLayout(800, ViewGroup.LayoutParams.WRAP_CONTENT);

        btn_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
                Intent intent = new Intent(ctx, BookDetails.class);
                intent.putExtra("book_id",bookId);
                intent.putExtra("book_img",imgUrl);
                ctx.startActivity(intent);
            }
        });
    }

    private String convertTime(String time) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat format1 = new SimpleDateFormat("dd MMM,yyyy");
        java.util.Date date = null;

        try {
            date = format.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String convertedDate = format1.format(date);

        return convertedDate;
    }

    public void setFilter(List<SearchResModel.ResDataBooks.BookListData> arrayList) {
        bookModels = new ArrayList<>();
        bookModels.addAll(arrayList);
        notifyDataSetChanged();
    }

}
