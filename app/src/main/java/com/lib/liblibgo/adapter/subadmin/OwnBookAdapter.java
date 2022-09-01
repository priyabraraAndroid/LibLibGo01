package com.lib.liblibgo.adapter.subadmin;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
import com.lib.liblibgo.api.AllApiClass;
import com.lib.liblibgo.api.Holders;
import com.lib.liblibgo.common.Helper;
import com.lib.liblibgo.dashboard.apartment_admin.UploadBookDetails;
import com.lib.liblibgo.model.SubmitDataResModel;
import com.lib.liblibgo.model.subadmin.MyOwnBookModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OwnBookAdapter extends RecyclerView.Adapter<OwnBookAdapter.OwnBookHolder>{
    private static final String TAG = "OwnBookAdapter";
    private List<MyOwnBookModel.ResData.BookList> bookModels;
    private Context ctx;

    public OwnBookAdapter(List<MyOwnBookModel.ResData.BookList> bookModels, Context context) {
        this.bookModels = bookModels;
        this.ctx = context;
    }

    @NonNull
    @Override
    public OwnBookHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(ctx).inflate(R.layout.own_book_list_row,parent,false);
        return new OwnBookHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OwnBookHolder holder, int position) {
        holder.bookBind();
    }

    public class OwnBookHolder extends RecyclerView.ViewHolder {
        TextView tv_book, tv_author, tv_isbn, tv_quantity, tv_aprove;
        CardView card;
        ImageView iv_edit,iv_delete,vi_book;

        public OwnBookHolder(@NonNull View itemView) {
            super(itemView);
            tv_book = itemView.findViewById(R.id.tv_book);
            tv_author = itemView.findViewById(R.id.tv_author);
            tv_isbn = itemView.findViewById(R.id.tv_isbn);
            tv_quantity = itemView.findViewById(R.id.tv_quantity);
            tv_aprove = itemView.findViewById(R.id.tv_aprove);
            card = itemView.findViewById(R.id.card);
            iv_edit = itemView.findViewById(R.id.iv_edit);
            iv_delete = itemView.findViewById(R.id.iv_delete);
            vi_book = itemView.findViewById(R.id.vi_book);
        }

        public void bookBind(){
            tv_book.setText(bookModels.get(getAdapterPosition()).getBook_name());
            tv_author.setText("By "+bookModels.get(getAdapterPosition()).getAuthor_name());
            tv_isbn.setText("Isbn no : "+bookModels.get(getAdapterPosition()).getIsbn_no());
            tv_quantity.setText("Quantity : "+bookModels.get(getAdapterPosition()).getQuantity());
            if (bookModels.get(getAdapterPosition()).getApproval_status().equals("1")){
                tv_aprove.setText("Approved");
                tv_aprove.setTextColor(Color.parseColor("#07950D"));
            }else {
                tv_aprove.setText("Under Review");
                tv_aprove.setTextColor(Color.parseColor("#D81406"));
            }

            if (bookModels.get(getAdapterPosition()).getImage_url().equals("") || bookModels.get(getAdapterPosition()).getImage_url().equals("null")){
                Glide.with(ctx).load("https://sales.arecontvision.com/images/products/img_placeholder_50618_xl.jpg").into(vi_book);
            }else {
                Glide.with(ctx).load(bookModels.get(getAdapterPosition()).getImage_url()).into(vi_book);
            }

            iv_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    deleteBook(R.layout.delete_popup,bookModels.get(getAdapterPosition()).getBook_id(),getAdapterPosition());
                }
            });

            iv_edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(ctx, UploadBookDetails.class);
                    intent.putExtra("name",bookModels.get(getAdapterPosition()).getBook_name());
                    intent.putExtra("author",bookModels.get(getAdapterPosition()).getAuthor_name());
                    intent.putExtra("isbn",bookModels.get(getAdapterPosition()).getIsbn_no());
                    intent.putExtra("publish_date",bookModels.get(getAdapterPosition()).getPublish_date());
                    intent.putExtra("description",bookModels.get(getAdapterPosition()).getDescription());
                    intent.putExtra("imgUrl",bookModels.get(getAdapterPosition()).getImage_url());

                    intent.putExtra("rental_price",bookModels.get(getAdapterPosition()).getRental_price());
                    intent.putExtra("rental_duration",bookModels.get(getAdapterPosition()).getRent_duration());
                    intent.putExtra("book_price",bookModels.get(getAdapterPosition()).getSale_price());
                    intent.putExtra("category_id",bookModels.get(getAdapterPosition()).getCategory_id());
                    intent.putExtra("category_name",bookModels.get(getAdapterPosition()).getCategory_name());
                    intent.putExtra("shelf_id",bookModels.get(getAdapterPosition()).getShelf_id());
                    intent.putExtra("shelf_name",bookModels.get(getAdapterPosition()).getShelf_no());

                    intent.putExtra("book_id",bookModels.get(getAdapterPosition()).getBook_id());
                    intent.putExtra("mrp",bookModels.get(getAdapterPosition()).getMrp());
                    intent.putExtra("quantity",bookModels.get(getAdapterPosition()).getQuantity());

                    intent.putExtra("sale_type",bookModels.get(getAdapterPosition()).getSelling_type());
                    intent.putExtra("book_condition",bookModels.get(getAdapterPosition()).getBook_condition_type());
                    intent.putExtra("security_money",bookModels.get(getAdapterPosition()).getSecurity_money());
                    intent.putExtra("giveaway_status",bookModels.get(getAdapterPosition()).getGiveaway());
                    ctx.startActivity(intent);
                }
            });
        }
    }

    private void deleteBook(int layout,String bookId,int nPos) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(ctx);
        LayoutInflater inflater = (LayoutInflater)ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View layoutView = inflater.inflate(layout, null);
        final TextView tv_yes = layoutView.findViewById(R.id.tv_yes);
        final TextView tv_no = layoutView.findViewById(R.id.tv_no);

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
                final ProgressDialog loginDialog = new ProgressDialog(ctx);
                loginDialog.setMessage("Please wait..");
                loginDialog.setCancelable(false);
                loginDialog.show();
                Holders holders = AllApiClass.getInstance().getApi();
                Call<SubmitDataResModel> returnb = holders.deleteBooks(bookId);
                returnb.enqueue(new Callback<SubmitDataResModel>() {
                    @Override
                    public void onResponse(Call<SubmitDataResModel> call, Response<SubmitDataResModel> response) {
                        if (response.isSuccessful()){
                            if (response.body().getResponse().getCode() == 1){
                                loginDialog.dismiss();
                                alertDialog.dismiss();
                                bookModels.remove(nPos);
                                notifyItemRemoved(nPos);
                                notifyItemRangeChanged(nPos, bookModels.size());
                                Helper.showToast(ctx,response.body().getResponse().getMessage());
                            }else {
                                loginDialog.dismiss();
                                Toast.makeText(ctx, ""+response.body().getResponse().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }else {
                            loginDialog.dismiss();
                            Toast.makeText(ctx, "Something went wrong !", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<SubmitDataResModel> call, Throwable t) {
                        loginDialog.dismiss();
                        Toast.makeText(ctx, ""+t, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return bookModels.size();
    }

    public void setFilter(List<MyOwnBookModel.ResData.BookList> arrayList) {
        bookModels = new ArrayList<>();
        bookModels.addAll(arrayList);
        notifyDataSetChanged();
    }

}
