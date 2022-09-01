package com.lib.liblibgo.adapter;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputEditText;
import com.lib.liblibgo.R;
import com.lib.liblibgo.api.AllApiClass;
import com.lib.liblibgo.api.Holders;
import com.lib.liblibgo.common.UserDatabase;
import com.lib.liblibgo.model.CollectedBookModel;
import com.lib.liblibgo.model.SubmitDataResModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import me.zhanghai.android.materialratingbar.MaterialRatingBar;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UnReviewedBookAdapter extends RecyclerView.Adapter<UnReviewedBookAdapter.CollectHolder> {
    private List<CollectedBookModel.ResData.ListData> bookModels = new ArrayList<>();
    private Context ctx;
    private AlertDialog alertDialog;

    public UnReviewedBookAdapter(List<CollectedBookModel.ResData.ListData> bookModels, Context context) {
        this.bookModels = bookModels;
        this.ctx = context;
    }

    @NonNull
    @Override
    public CollectHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(ctx).inflate(R.layout.unreviewed_book_row,parent,false);
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
        TextView tvBookName, tvAuthorName, tvIssueDate,tvShelfNo,tvReturnDate;
        Button btnReview;
        ImageView vi_book;
        public CollectHolder(@NonNull View itemView) {
            super(itemView);
            tvBookName = itemView.findViewById(R.id.tv_book);
            tvAuthorName = itemView.findViewById(R.id.tv_author);
            tvShelfNo = itemView.findViewById(R.id.tv_cell);
            tvIssueDate = itemView.findViewById(R.id.tv_issued_date);
            tvReturnDate = itemView.findViewById(R.id.tv_return_date);
            btnReview = itemView.findViewById(R.id.btnReview);
            vi_book = itemView.findViewById(R.id.vi_book);

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

            btnReview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    UserDatabase database = new UserDatabase(ctx);
                    showRatingPopup(R.layout.rateing_reviews,bookModels.get(getAdapterPosition()).getBook_id(),bookModels.get(getAdapterPosition()).getIssue_id(),database.getUserId());
                }
            });
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

    private void showRatingPopup(int layout, final String book_id,final String issue_id,String userId) {
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
                sendReview(book_id,mReview,mRating,userId,issue_id);
            }
        });

        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
                //returnBook(userId,issue_id);
            }
        });

    }

    private void sendReview(String book_id, String mReview, String mRating,String userId,String issue_id) {
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

    }
}
