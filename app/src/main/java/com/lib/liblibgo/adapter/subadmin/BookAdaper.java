package com.lib.liblibgo.adapter.subadmin;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.lib.liblibgo.R;
import com.lib.liblibgo.model.subadmin.CustBookHisModel;

import java.util.ArrayList;
import java.util.List;

public class BookAdaper extends RecyclerView.Adapter<BookAdaper.ApartmentHolder> {
    List<CustBookHisModel.ResData.BookList> mListItems;
    Context mCtx;

    public BookAdaper(List<CustBookHisModel.ResData.BookList> mListItems, Context mCtx) {
        this.mListItems = mListItems;
        this.mCtx = mCtx;
    }

    @NonNull
    @Override
    public ApartmentHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(mCtx).inflate(R.layout.book_his_row_tow, parent, false);
        return new ApartmentHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ApartmentHolder holder, final int position) {
        holder.bindData();

    }


    @Override
    public int getItemCount() {
        return mListItems.size();
    }

    public class ApartmentHolder extends RecyclerView.ViewHolder {
        TextView tv_book_no;
        TextView tv_book_name;
        TextView tv_auth_name;
        TextView tv_issue_date;
        TextView tv_return_date;

        public ApartmentHolder(@NonNull View itemView) {
            super(itemView);
            tv_book_no = itemView.findViewById(R.id.tv_book_no);
            tv_book_name = itemView.findViewById(R.id.tv_book_name);
            tv_auth_name = itemView.findViewById(R.id.tv_auth_name);
            tv_issue_date = itemView.findViewById(R.id.tv_issue_date);
            tv_return_date = itemView.findViewById(R.id.tv_return_date);
        }

        public void bindData() {
            tv_book_no.setText("Book No : " + mListItems.get(getAdapterPosition()).getBook_number());
            tv_book_name.setText("Book Name : " + mListItems.get(getAdapterPosition()).getBook_name());
            tv_auth_name.setText("Author Name : " + mListItems.get(getAdapterPosition()).getAuthor_name());
            tv_return_date.setText("Return Date : " + mListItems.get(getAdapterPosition()).getBook_return_date());
            tv_issue_date.setText("Issue Date : " + mListItems.get(getAdapterPosition()).getBook_issue_date());
        }
    }

    public void setFilter(List<CustBookHisModel.ResData.BookList> arrayList) {
        mListItems = new ArrayList<>();
        mListItems.addAll(arrayList);
        notifyDataSetChanged();
    }
}
