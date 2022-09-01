package com.lib.liblibgo.adapter.subadmin;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.lib.liblibgo.R;
import com.lib.liblibgo.model.subadmin.BookHisModel;

import java.util.ArrayList;
import java.util.List;

public class BookHisAdapter extends RecyclerView.Adapter<BookHisAdapter.ApartmentHolder> {
    List<BookHisModel.ResData.CustomerList> mListItems;
    Context mCtx;

    public BookHisAdapter(List<BookHisModel.ResData.CustomerList> mListItems, Context mCtx) {
        this.mListItems = mListItems;
        this.mCtx = mCtx;
    }

    @NonNull
    @Override
    public ApartmentHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(mCtx).inflate(R.layout.book_his_row, parent, false);
        return new ApartmentHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ApartmentHolder holder, final int position) {
        holder.bindData();
        holder.tv_view_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //showPopup(R.layout.cust_details);
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(mCtx);
                final View layoutView = LayoutInflater.from(mCtx).inflate(R.layout.cust_details, null);
                dialogBuilder.setView(layoutView);
                final AlertDialog alertDialog = dialogBuilder.create();
                alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation1;
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                alertDialog.show();
                alertDialog.setCancelable(false);
                alertDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                ImageView close = layoutView.findViewById(R.id.close);
                TextView tv_book_name = layoutView.findViewById(R.id.tv_book_name);
                TextView tv_cust_name = layoutView.findViewById(R.id.tv_cust_name);
                TextView tv_cust_phone = layoutView.findViewById(R.id.tv_cust_phone);
                TextView tv_cust_email = layoutView.findViewById(R.id.tv_cust_email);
                TextView tv_status = layoutView.findViewById(R.id.tv_status);
                TextView tv_issue_date = layoutView.findViewById(R.id.tv_issue_date);
                TextView tv_return_date = layoutView.findViewById(R.id.tv_return_date);
                TextView tv_apartment = layoutView.findViewById(R.id.tv_apartment);
                TextView tv_isbn = layoutView.findViewById(R.id.tv_isbn);

                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.dismiss();
                    }
                });

                tv_book_name.setText(mListItems.get(position).getBook_name());
                tv_cust_name.setText("Customer Name : "+mListItems.get(position).getCustomer_name());
                tv_cust_phone.setText("Customer Phone no : "+mListItems.get(position).getCustomer_mobile());
                tv_cust_email.setText("Customer Email : "+mListItems.get(position).getCustomer_email());
                tv_issue_date.setText("Issue Date : "+mListItems.get(position).getIssue_date());
                tv_return_date.setText("Return Date : "+mListItems.get(position).getReturn_date());
                tv_apartment.setText("Apartment Name : "+mListItems.get(position).getApartment());
                tv_isbn.setText("ISBN No : "+mListItems.get(position).getIsbn_no());

                if (mListItems.get(position).getReturn_status().equals("1")){
                    tv_status.setText("Return : Yes");
                    tv_return_date.setVisibility(View.VISIBLE);
                }else {
                    tv_status.setText("Return : No");
                    tv_return_date.setVisibility(View.GONE);
                }

            }
        });
    }


    @Override
    public int getItemCount() {
        return mListItems.size();
    }

    public class ApartmentHolder extends RecyclerView.ViewHolder {
        TextView tv_book_no;
        TextView tv_book_name;
        TextView tv_auth_name;
        TextView tv_return_status;
        TextView tv_view_details;

        public ApartmentHolder(@NonNull View itemView) {
            super(itemView);
            tv_book_no = itemView.findViewById(R.id.tv_book_no);
            tv_book_name = itemView.findViewById(R.id.tv_book_name);
            tv_auth_name = itemView.findViewById(R.id.tv_auth_name);
            tv_return_status = itemView.findViewById(R.id.tv_return_status);
            tv_view_details = itemView.findViewById(R.id.tv_view_details);
        }

        public void bindData() {
            tv_book_no.setText("Book No : "+mListItems.get(getAdapterPosition()).getBook_number());
            tv_book_name.setText("Book Name : "+mListItems.get(getAdapterPosition()).getBook_name());
            tv_auth_name.setText("Author Name : "+mListItems.get(getAdapterPosition()).getAuthor_name());

            if (mListItems.get(getAdapterPosition()).getReturn_status().equals("1")){
                tv_return_status.setText("Returned");
                tv_return_status.setTextColor(Color.parseColor("#06C30F"));
            }else {
                tv_return_status.setText("Not Return");
                tv_return_status.setTextColor(Color.parseColor("#E40909"));
            }
        }
    }

    public void setFilter(List<BookHisModel.ResData.CustomerList> arrayList) {
        mListItems = new ArrayList<>();
        mListItems.addAll(arrayList);
        notifyDataSetChanged();
    }
}
