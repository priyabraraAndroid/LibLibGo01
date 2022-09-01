package com.lib.liblibgo.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.lib.liblibgo.R;
import com.lib.liblibgo.model.ReturnHisModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class ReturnHistoryAdapter extends RecyclerView.Adapter<ReturnHistoryAdapter.CollectHolder> {

    private List<ReturnHisModel.ResData.DataList> bookModels;
    private Context ctx;
    private AlertDialog alertDialog;

    public ReturnHistoryAdapter(List<ReturnHisModel.ResData.DataList> bookModels, Context context) {
        this.bookModels = bookModels;
        this.ctx = context;
    }

    @NonNull
    @Override
    public CollectHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(ctx).inflate(R.layout.row_book_history_new,parent,false);
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
        TextView tvBookName, tvAuthorName, tvIssueDate, tvReturnDate,tvShelfNo;
       // Button btn_review;
        ImageView vi_book;
        public CollectHolder(@NonNull View itemView) {
            super(itemView);
            tvBookName = itemView.findViewById(R.id.tv_book);
            tvAuthorName = itemView.findViewById(R.id.tv_author);
            tvShelfNo = itemView.findViewById(R.id.tv_cell);
            tvIssueDate = itemView.findViewById(R.id.tv_issued_date);
            tvReturnDate = itemView.findViewById(R.id.tv_return_date);
            vi_book = itemView.findViewById(R.id.vi_book);
            //tvBookNumber = itemView.findViewById(R.id.tvBookNumber);
            //btn_review = itemView.findViewById(R.id.btn_review);
        }
        public void bookBind(){
            tvBookName.setText(bookModels.get(getAdapterPosition()).getBook_name());
            tvAuthorName.setText("By "+bookModels.get(getAdapterPosition()).getAuthor_name());
            tvShelfNo.setText("Shelf No : "+bookModels.get(getAdapterPosition()).getShelf_no());
            String issue_date = "<font color='#07950D'>" + "Issued Date : " + "</font>" + convertTime(bookModels.get(getAdapterPosition()).getIssue_date());
            tvIssueDate.setText(Html.fromHtml(issue_date));
            String return_date = "<font color='#D81406'>" + "Return Date : " + "</font>" + convertTime(bookModels.get(getAdapterPosition()).getReturn_date());
            tvReturnDate.setText(Html.fromHtml(return_date));

            if (bookModels.get(getAdapterPosition()).getImage_url().equals("")){
                Glide.with(ctx).load("https://sales.arecontvision.com/images/products/img_placeholder_50618_xl.jpg").into(vi_book);
            }else {
                Glide.with(ctx).load(bookModels.get(getAdapterPosition()).getImage_url()).into(vi_book);
            }

           /* btn_review.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Toast.makeText(ctx, "Clicked "+getAdapterPosition(), Toast.LENGTH_SHORT).show();
                    showRatingPopup(R.layout.rateing_reviews,bookModels.get(getAdapterPosition()).getBook_id(),database.getUserId());
                }
            });*/
        }

    }
    private String convertTime(String time) {
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
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

    /*private void showRatingPopup(int layout, final String book_id,String userId) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(ctx);
        LayoutInflater inflater = (LayoutInflater)ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View layoutView = inflater.inflate(layout, null);
        final Button btn_submit = layoutView.findViewById(R.id.btn_submit);
        final ImageView iv_close = layoutView.findViewById(R.id.iv_close);
        final MaterialRatingBar rating = layoutView.findViewById(R.id.rating);
        final TextInputEditText reviewEt = layoutView.findViewById(R.id.et_rating);

        dialogBuilder.setView(layoutView);
        alertDialog = dialogBuilder.create();
        alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation1;
        alertDialog.setCancelable(false);
        alertDialog.show();

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mReview = reviewEt.getText().toString().trim();
                String mRating = String.valueOf(rating.getRating());
                //Toast.makeText(getActivity(), ""+mRating+"\n"+mReview, Toast.LENGTH_SHORT).show();
                sendReview(book_id,mReview,mRating,userId);
            }
        });

        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });

    }

    private void sendReview(String book_id, String mReview, String mRating,String userId) {
        final ProgressDialog loginDialog = new ProgressDialog(ctx);
        loginDialog.setMessage("Please wait..");
        loginDialog.setCancelable(false);
        loginDialog.show();
        Holders holders = AllApiClass.getInstance().getApi();
        Call<SubmitDataResModel> call = holders.sendBookReview(userId,book_id,mRating,mReview);
        call.enqueue(new Callback<SubmitDataResModel>() {
            @Override
            public void onResponse(Call<SubmitDataResModel> call, Response<SubmitDataResModel> response) {
                if (response.isSuccessful()){
                    if (response.body().getResponse().getCode() == 1){
                        loginDialog.dismiss();
                        alertDialog.dismiss();
                    }else {
                        loginDialog.dismiss();
                        Toast.makeText(ctx, response.body().getResponse().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }else {
                    loginDialog.dismiss();
                    Toast.makeText(ctx, "Something went wrong !", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<SubmitDataResModel> call, Throwable t) {
                loginDialog.dismiss();
                Toast.makeText(ctx, "Check Your Internet Connection.", Toast.LENGTH_SHORT).show();
            }
        });

    }*/

    public void setFilter(List<ReturnHisModel.ResData.DataList> arrayList) {
        bookModels = new ArrayList<>();
        bookModels.addAll(arrayList);
        notifyDataSetChanged();
    }
}
