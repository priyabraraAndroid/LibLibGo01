package com.lib.liblibgo.adapter.subadmin;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.lib.liblibgo.R;
import com.lib.liblibgo.dashboard.apartment_admin.UploadBookDetails;
import com.lib.liblibgo.model.subadmin.BookDataModel;

import java.util.ArrayList;
import java.util.List;

public class BookListAdapter extends RecyclerView.Adapter<BookListAdapter.CollectHolder> {

    private List<BookDataModel> bookModels = new ArrayList<>();
    private Context ctx;
    private AlertDialog alertDialog;

    public BookListAdapter(List<BookDataModel> bookModels, Context context) {
        this.bookModels = bookModels;
        this.ctx = context;
    }

    @NonNull
    @Override
    public CollectHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(ctx).inflate(R.layout.book_google_search_row,parent,false);
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
        TextView tvBookName, tvAuthorName,tv_isbn,tv_publish_date;
        ImageView vi_book;
        CardView itemClick;
        public CollectHolder(@NonNull View itemView) {
            super(itemView);
            tvBookName = itemView.findViewById(R.id.tv_book);
            tvAuthorName = itemView.findViewById(R.id.tv_author);
            tv_isbn = itemView.findViewById(R.id.tv_isbn);
            tv_publish_date = itemView.findViewById(R.id.tv_publish_date);
            vi_book = itemView.findViewById(R.id.vi_book);
            itemClick = itemView.findViewById(R.id.card);
        }
        public void bookBind(){
            tvBookName.setText(bookModels.get(getAdapterPosition()).getName());
            tvAuthorName.setText("By "+bookModels.get(getAdapterPosition()).getAuthor());
            tv_isbn.setText("Isbn no : "+bookModels.get(getAdapterPosition()).getIsbn_no());
            tv_publish_date.setText("Publish date : "+bookModels.get(getAdapterPosition()).getPublish_date());

            if (bookModels.get(getAdapterPosition()).getThumbnailUrl().equals("") || bookModels.get(getAdapterPosition()).getThumbnailUrl().equals("null")){
                Glide.with(ctx).load("https://sales.arecontvision.com/images/products/img_placeholder_50618_xl.jpg").into(vi_book);
            }else {
                Glide.with(ctx).load(bookModels.get(getAdapterPosition()).getThumbnailUrl()).into(vi_book);
            }

            itemClick.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        Intent intent = new Intent(ctx, UploadBookDetails.class);
                        intent.putExtra("name",bookModels.get(getAdapterPosition()).getName());
                        intent.putExtra("author",bookModels.get(getAdapterPosition()).getAuthor());
                        intent.putExtra("isbn",bookModels.get(getAdapterPosition()).getIsbn_no());
                        intent.putExtra("publish_date",bookModels.get(getAdapterPosition()).getPublish_date());
                        intent.putExtra("description",bookModels.get(getAdapterPosition()).getDescription());
                        intent.putExtra("imgUrl",bookModels.get(getAdapterPosition()).getThumbnailUrl());
                        intent.putExtra("rental_price","");
                        intent.putExtra("rental_duration","");
                        intent.putExtra("book_price","");
                        intent.putExtra("category_id","");
                        intent.putExtra("category_name","");
                        intent.putExtra("shelf_id","");
                        intent.putExtra("shelf_name","");
                        intent.putExtra("book_id","");
                        intent.putExtra("mrp","");
                        intent.putExtra("quantity","");
                        intent.putExtra("sale_type","");
                        intent.putExtra("book_condition","");
                        intent.putExtra("security_money","");
                        intent.putExtra("giveaway_status","no");
                        ctx.startActivity(intent);
                    }catch (IndexOutOfBoundsException e){
                        e.printStackTrace();
                    }
                }
            });

        }
    }
}
