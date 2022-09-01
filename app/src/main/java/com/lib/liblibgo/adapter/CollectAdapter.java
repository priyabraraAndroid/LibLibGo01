package com.lib.liblibgo.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.lib.liblibgo.R;
import com.lib.liblibgo.api.AllApiClass;
import com.lib.liblibgo.api.Holders;
import com.lib.liblibgo.common.Helper;
import com.lib.liblibgo.common.UserDatabase;
import com.lib.liblibgo.dashboard.book_details.BookDetails;
import com.lib.liblibgo.model.BookListModel;
import com.lib.liblibgo.model.SubmitDataResModel;
import com.lib.liblibgo.views.MyButton;
import com.lib.liblibgo.views.MyTextView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CollectAdapter extends RecyclerView.Adapter<CollectAdapter.CollectHolder> {
    private static final String TAG = "CollectAdapter";
    private List<BookListModel.ResData.BookListData> bookModels;
    private Context ctx;

    public CollectAdapter(List<BookListModel.ResData.BookListData> bookModels, Context context) {
        this.bookModels = bookModels;
        this.ctx = context;
    }

    @NonNull
    @Override
    public CollectHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(ctx).inflate(R.layout.row_book_collect,parent,false);
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
        MyTextView tvBookName, tvAuthorName,tvBookNumber,tvPubDate,tvIsbnNo,tvApartmentName;
        private MyButton collectBook;
        ImageView iv_flag;
        CardView card;
        public CollectHolder(@NonNull View itemView) {
            super(itemView);
            tvBookName = itemView.findViewById(R.id.tvBookName);
            tvAuthorName = itemView.findViewById(R.id.tvAuthorName);
            collectBook = itemView.findViewById(R.id.collectBook);
            tvBookNumber = itemView.findViewById(R.id.tvBookNumber);
            tvPubDate = itemView.findViewById(R.id.tvPubDate);
            tvIsbnNo = itemView.findViewById(R.id.tvIsbnNo);
            tvApartmentName = itemView.findViewById(R.id.tvApartmentName);
            iv_flag = itemView.findViewById(R.id.iv_flag);
            card = itemView.findViewById(R.id.card);
        }

        public void bookBind(){
            UserDatabase database = new UserDatabase(ctx);
            tvBookName.setText( "Book Name : "+bookModels.get(getAdapterPosition()).getBook_name());
            tvAuthorName.setText("Author : "+bookModels.get(getAdapterPosition()).getAuthor_name());
            tvBookNumber.setText("Book Number : "+bookModels.get(getAdapterPosition()).getBook_number());
            tvPubDate.setText("Publish On : "+bookModels.get(getAdapterPosition()).getPublish_date());
            tvIsbnNo.setText("Isbn No : "+bookModels.get(getAdapterPosition()).getIsbn_no());
            tvApartmentName.setText("Apartment Name : "+bookModels.get(getAdapterPosition()).getApartment_name());

            if (bookModels.get(getAdapterPosition()).getFlag_status().equals("1")){
                iv_flag.setColorFilter(ContextCompat.getColor(ctx, R.color.green), android.graphics.PorterDuff.Mode.SRC_IN);
            }else if (bookModels.get(getAdapterPosition()).getFlag_status().equals("0")){
                iv_flag.setColorFilter(ContextCompat.getColor(ctx, R.color.red), android.graphics.PorterDuff.Mode.SRC_IN);
            }else if (bookModels.get(getAdapterPosition()).getFlag_status().equals("2")){
                iv_flag.setColorFilter(ContextCompat.getColor(ctx, R.color.yellow), android.graphics.PorterDuff.Mode.SRC_IN);
            }

            card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(ctx, BookDetails.class);
                    intent.putExtra("book_id",bookModels.get(getAdapterPosition()).getBook_id());
                    ctx.startActivity(intent);
                }
            });

            if (bookModels.get(getAdapterPosition()).getIssue_status().equals("1")){
                collectBook.setEnabled(false);
                collectBook.setText("Issued");
                collectBook.setBackgroundColor(Color.parseColor("#cccccc"));
            }else {
                collectBook.setEnabled(true);
                collectBook.setText("Issue");
                collectBook.setBackgroundColor(Color.parseColor("#3A9B3E"));

                collectBook.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final ProgressDialog loginDialog = new ProgressDialog(ctx);
                        loginDialog.setMessage("Please wait..");
                        loginDialog.setCancelable(false);
                        loginDialog.show();
                        //Toast.makeText(ctx, ""+database.getUserId()+"\n"+bookModels.get(getAdapterPosition()).getBook_id(), Toast.LENGTH_SHORT).show();
                        Holders holders = AllApiClass.getInstance().getApi();
                        Call<SubmitDataResModel> call = holders.collectBook(database.getUserId(),bookModels.get(getAdapterPosition()).getBook_id());
                        call.enqueue(new Callback<SubmitDataResModel>() {
                            @Override
                            public void onResponse(Call<SubmitDataResModel> call, Response<SubmitDataResModel> response) {
                                if (response.isSuccessful()){
                                    if (response.body().getResponse().getCode().equals("1")){
                                        loginDialog.dismiss();
                                        Helper.showToast(ctx,response.body().getResponse().getMessage());
                                        collectBook.setEnabled(false);
                                        collectBook.setText("Issued");
                                        collectBook.setBackgroundColor(Color.parseColor("#cccccc"));
                                    }else{
                                        loginDialog.dismiss();
                                        Helper.showToast(ctx,response.body().getResponse().getMessage());
                                    }
                                }else {
                                    loginDialog.dismiss();
                                    Helper.showToast(ctx,"Something went wrong !");
                                }
                            }

                            @Override
                            public void onFailure(Call<SubmitDataResModel> call, Throwable t) {
                                Helper.showToast(ctx,""+t);
                                loginDialog.dismiss();
                            }
                        });
                    }
                });
            }
        }
    }

    public void setFilter(List<BookListModel.ResData.BookListData> arrayList) {
        bookModels = new ArrayList<>();
        bookModels.addAll(arrayList);
        notifyDataSetChanged();
    }
}
