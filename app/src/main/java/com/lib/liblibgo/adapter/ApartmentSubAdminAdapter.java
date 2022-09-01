package com.lib.liblibgo.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lib.liblibgo.R;
import com.lib.liblibgo.common.Constants;
import com.lib.liblibgo.dashboard.apartment_admin.AddApartmentActivity;
import com.lib.liblibgo.model.ApartmentModel;

import java.util.ArrayList;
import java.util.List;

public class ApartmentSubAdminAdapter extends RecyclerView.Adapter<ApartmentSubAdminAdapter.ApartmentHolder> {
    List<ApartmentModel> mListItems;
    Context mCtx;

    public ApartmentSubAdminAdapter(List<ApartmentModel> mListItems, Context mCtx) {
        this.mListItems = mListItems;
        this.mCtx = mCtx;
    }

    @NonNull
    @Override
    public ApartmentHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(mCtx).inflate(R.layout.row_apartment_item, parent, false);
        return new ApartmentHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ApartmentHolder holder, final int position) {
        holder.bindData();
        holder.tvEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Constants constants;
                Intent intent = new Intent(mCtx, AddApartmentActivity.class);
                intent.putExtra("ApName",holder.tvName.getText().toString());
                intent.putExtra("ApId",mListItems.get(position).getApartmentId());
                intent.putExtra("btnTxt","UPDATE");
                ((Activity)mCtx).startActivityForResult(intent,Constants.REQUEST_CREATE);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mListItems.size();
    }

    public class ApartmentHolder extends RecyclerView.ViewHolder {
        TextView tvName;
        ImageView tvEdit;
        public ApartmentHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvEdit = itemView.findViewById(R.id.tvEdit);
        }

        public void bindData() {
            tvName.setText(mListItems.get(getAdapterPosition()).getApartmentName());
        }
    }
    public void setFilter(List<ApartmentModel> arrayList) {
        mListItems = new ArrayList<>();
        mListItems.addAll(arrayList);
        notifyDataSetChanged();
    }

}