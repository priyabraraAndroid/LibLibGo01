package com.lib.liblibgo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lib.liblibgo.R;
import com.lib.liblibgo.model.CartModel;
import com.lib.liblibgo.model.MyOrderDetailsModel;

import java.util.ArrayList;
import java.util.List;

public class LibraryWiseOrderAdapter extends RecyclerView.Adapter<LibraryWiseOrderAdapter.CartListHolder> {
    List<MyOrderDetailsModel.ResData.LibraryList> mList;
    Context mCtx;
    List<MyOrderDetailsModel.ResData.LibraryList.MyOrderItemList> mSubList = new ArrayList<>();
    ItemAdapter subAdapter;

    public LibraryWiseOrderAdapter(List<MyOrderDetailsModel.ResData.LibraryList> mList, Context mCtx) {
        this.mList = mList;
        this.mCtx = mCtx;
    }

    @NonNull
    @Override
    public CartListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(mCtx).inflate(R.layout.order_libray_row, parent, false);
        return new CartListHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull CartListHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class CartListHolder extends RecyclerView.ViewHolder {
        TextView tvLibraryName,tvOrderStatus;
        RecyclerView rvBooks;

        public CartListHolder(@NonNull View itemView) {
            super(itemView);
            tvLibraryName = itemView.findViewById(R.id.tvLibraryName);
            tvOrderStatus = itemView.findViewById(R.id.tvOrderStatus);
            rvBooks = itemView.findViewById(R.id.rvBooks);
        }

        public void bind(int pos) {
            tvLibraryName.setText(mList.get(getAdapterPosition()).getLibrary_name());

            if(mList.get(getAdapterPosition()).getOrder_status().equals("0")){
                tvOrderStatus.setText("Processing");
            }else if (mList.get(getAdapterPosition()).getOrder_status().equals("1")){
                tvOrderStatus.setText("Order Confirmed");
            }else if (mList.get(getAdapterPosition()).getOrder_status().equals("2")){
                tvOrderStatus.setText("Picked Up");
            }else if (mList.get(getAdapterPosition()).getOrder_status().equals("3")){
                tvOrderStatus.setText("Out Of Delivery");
            }else if (mList.get(getAdapterPosition()).getOrder_status().equals("4")){
                tvOrderStatus.setText("Delivered");
            }

            mSubList = mList.get(getAdapterPosition()).getOrder_details();

            if (mSubList.size() > 0){
                subAdapter = new ItemAdapter(mSubList,mCtx);
                LinearLayoutManager verticalManager = new LinearLayoutManager(mCtx, LinearLayoutManager.VERTICAL, false);
                rvBooks.setLayoutManager(verticalManager);
                rvBooks.setAdapter(subAdapter);
            }

        }
    }
}
