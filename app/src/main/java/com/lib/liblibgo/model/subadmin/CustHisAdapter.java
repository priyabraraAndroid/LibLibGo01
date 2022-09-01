package com.lib.liblibgo.model.subadmin;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;


import com.lib.liblibgo.R;
import com.lib.liblibgo.dashboard.apartment_admin.CustomerBookHistory;
import com.lib.liblibgo.dashboard.apartment_admin.SendNotification;

import java.util.ArrayList;
import java.util.List;

public class CustHisAdapter extends RecyclerView.Adapter<CustHisAdapter.CustManagementHolder> {
    List<CustListModel.ResData.ListData> mList;
    Context mCtx;

    public CustHisAdapter(List<CustListModel.ResData.ListData> mList, Context mCtx) {
        this.mList = mList;
        this.mCtx = mCtx;
    }

    @NonNull
    @Override
    public CustManagementHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(mCtx).inflate(R.layout.customer_hist_row, parent, false);
        return new CustManagementHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull CustManagementHolder holder, final int position) {
        holder.bind();
        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mCtx, CustomerBookHistory.class);
                intent.putExtra("customer_id",mList.get(position).getCustomer_id());
                mCtx.startActivity(intent);
            }
        });

        holder.tv_msg_customer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mCtx, SendNotification.class);
                intent.putExtra("customer_id",mList.get(position).getCustomer_id());
                intent.putExtra("customer_name",mList.get(position).getUser_name());
                mCtx.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class CustManagementHolder extends RecyclerView.ViewHolder {
        TextView tvCustName, tvPhoneNumber, tvEmail,tvStatus,tvFlatNo,tv_apartment_name,tv_msg_customer;
        CardView card;

        public CustManagementHolder(@NonNull View itemView) {
            super(itemView);
            tvCustName = itemView.findViewById(R.id.tv_name);
            tvPhoneNumber = itemView.findViewById(R.id.tv_phone);
            tvEmail = itemView.findViewById(R.id.tv_email);
            tvStatus = itemView.findViewById(R.id.tv_status);
            tvFlatNo = itemView.findViewById(R.id.tv_flat_no);
            tv_apartment_name = itemView.findViewById(R.id.tv_apartment_name);
            tv_msg_customer = itemView.findViewById(R.id.tv_msg_customer);
            card = itemView.findViewById(R.id.card);
        }

        public void bind() {
            tvCustName.setText("Name : "+mList.get(getAdapterPosition()).getUser_name());
            tvPhoneNumber.setText("Phone No : "+mList.get(getAdapterPosition()).getMobile_number());
            tvEmail.setText("Email-Id : "+mList.get(getAdapterPosition()).getEmail());
            tvStatus.setText("Return Status : "+mList.get(getAdapterPosition()).getBook_return_status());
            tvFlatNo.setText("Flat No : "+mList.get(getAdapterPosition()).getFlat_no());
            tv_apartment_name.setText("Apartment Name : "+mList.get(getAdapterPosition()).getApartment_name());
        }
    }

    public void setFilter(List<CustListModel.ResData.ListData> arrayList) {
        mList = new ArrayList<>();
        mList.addAll(arrayList);
        notifyDataSetChanged();
    }
}
