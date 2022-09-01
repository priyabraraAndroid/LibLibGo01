package com.lib.liblibgo.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lib.liblibgo.R;
import com.lib.liblibgo.dashboard.book_details.OrderDetailsActivity;
import com.lib.liblibgo.dashboard.profile.OrderDetailsActivityNew;
import com.lib.liblibgo.model.MyOederModel;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

public class MyOrderAdapter extends RecyclerView.Adapter<MyOrderAdapter.MyViewHolder> {
    List<MyOederModel.ResData.MyOrderList> mList;
    Context mCtx;

    public MyOrderAdapter(List<MyOederModel.ResData.MyOrderList> mList, Context mCtx) {
        this.mList = mList;
        this.mCtx = mCtx;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(mCtx).inflate(R.layout.my_order_row, parent, false);
        return new MyViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.bind();
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvOrderNumber, tvCustomerName, tvOrderAmount,tvCustomerAdr,tvOrderDate,tvOrderStatus,viewDetails;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvOrderNumber = itemView.findViewById(R.id.tvOrderNumber);
            tvCustomerName = itemView.findViewById(R.id.tvCustomerName);
            tvOrderAmount = itemView.findViewById(R.id.tvOrderAmount);
            tvCustomerAdr = itemView.findViewById(R.id.tvCustomerAdr);
            tvOrderDate = itemView.findViewById(R.id.tvOrderDate);
            tvOrderStatus = itemView.findViewById(R.id.tvOrderStatus);
            viewDetails = itemView.findViewById(R.id.viewDetails);
        }

        public void bind() {
            tvOrderNumber.setText("Order ID - "+mList.get(getAdapterPosition()).getOrder_number());
            tvCustomerName.setText(mList.get(getAdapterPosition()).getBook_name());
           // tvOrderAmount.setText("₹ "+mList.get(getAdapterPosition()).getOrder_amount());
           // tvCustomerAdr.setText(mList.get(getAdapterPosition()).getCustomer_address());
            tvOrderDate.setText(convertTime(mList.get(getAdapterPosition()).getOrder_date()));

            /*if (mList.get(getAdapterPosition()).getOrder_status().equals("0")){
                tvOrderStatus.setText("Processing");
            }else if (mList.get(getAdapterPosition()).getOrder_status().equals("1")){
                tvOrderStatus.setText("Order Confirmed");
            }else if (mList.get(getAdapterPosition()).getOrder_status().equals("2")){
                tvOrderStatus.setText("Picked Up");
            }else if (mList.get(getAdapterPosition()).getOrder_status().equals("3")){
                tvOrderStatus.setText("Out Of Delivery");
            }else if (mList.get(getAdapterPosition()).getOrder_status().equals("4")){
                tvOrderStatus.setText("Delivered");
            }*/
            //tvOrderStatus.setText(mList.get(getAdapterPosition()).getOrder_date());

            viewDetails.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mCtx, OrderDetailsActivityNew.class);
                    intent.putExtra("order_id",mList.get(getAdapterPosition()).getOrder_id());
                    intent.putExtra("order_number",mList.get(getAdapterPosition()).getOrder_number());
                    mCtx.startActivity(intent);
                }
            });
        }
    }

    private String convertTime(String time) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        SimpleDateFormat format1 = new SimpleDateFormat("dd MMM yyyy");
        java.util.Date date = null;

        try {
            date = format.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String convertedDate = format1.format(date);

        return convertedDate;
    }
}
