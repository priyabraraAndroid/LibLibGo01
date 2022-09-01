package com.lib.liblibgo.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lib.liblibgo.R;
import com.lib.liblibgo.api.AllApiClass;
import com.lib.liblibgo.api.Holders;
import com.lib.liblibgo.dashboard.book_details.OrderDetailsActivity;
import com.lib.liblibgo.model.LibraryOrderModel;
import com.lib.liblibgo.model.SubmitDataResModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LibraryOrderAdapter extends RecyclerView.Adapter<LibraryOrderAdapter.MyViewHolder> {
    List<LibraryOrderModel.ResData.MyOrderList> mList;
    Context mCtx;

    public LibraryOrderAdapter(List<LibraryOrderModel.ResData.MyOrderList> mList, Context mCtx) {
        this.mList = mList;
        this.mCtx = mCtx;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(mCtx).inflate(R.layout.library_order_row, parent, false);
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
        TextView tvOrderNumber, tvCustomerName, tvOrderAmount,tvCustomerAdr,tvOrderDate,tvOrderStatus,
                viewDetails,tvCustomerPhone,tvOrderFor,confirmOrder;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvOrderNumber = itemView.findViewById(R.id.tvOrderNumber);
            tvCustomerName = itemView.findViewById(R.id.tvCustomerName);
            tvOrderAmount = itemView.findViewById(R.id.tvOrderAmount);
            tvCustomerAdr = itemView.findViewById(R.id.tvCustomerAdr);
            tvOrderDate = itemView.findViewById(R.id.tvOrderDate);
            tvOrderStatus = itemView.findViewById(R.id.tvOrderStatus);
            viewDetails = itemView.findViewById(R.id.viewDetails);
            tvCustomerPhone = itemView.findViewById(R.id.tvCustomerPhone);
            tvOrderFor = itemView.findViewById(R.id.tvOrderFor);
            confirmOrder = itemView.findViewById(R.id.confirmOrder);
        }

        public void bind() {
            tvOrderNumber.setText("Order ID - "+mList.get(getAdapterPosition()).getOrder_number());
            tvCustomerName.setText(mList.get(getAdapterPosition()).getCustomer_name());
            tvOrderAmount.setText("₹ "+mList.get(getAdapterPosition()).getAmount());
            tvCustomerAdr.setText(mList.get(getAdapterPosition()).getCustomer_address());
            tvCustomerPhone.setText(mList.get(getAdapterPosition()).getCustomer_mobile());
            tvOrderFor.setText(mList.get(getAdapterPosition()).getOrder_for());

            if (mList.get(getAdapterPosition()).getOrder_status().equals("0")){
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

            tvOrderDate.setText(convertTime(mList.get(getAdapterPosition()).getOrder_date()));

            viewDetails.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mCtx, OrderDetailsActivity.class);
                    intent.putExtra("order_details_id",mList.get(getAdapterPosition()).getOrder_detail_id());
                    intent.putExtra("order_id","");
                    intent.putExtra("order_number",mList.get(getAdapterPosition()).getOrder_number());
                    intent.putExtra("user_name",mList.get(getAdapterPosition()).getCustomer_name());
                    intent.putExtra("user_adr",mList.get(getAdapterPosition()).getCustomer_address());
                    intent.putExtra("amount",mList.get(getAdapterPosition()).getAmount());
                    intent.putExtra("date",mList.get(getAdapterPosition()).getOrder_date());
                    intent.putExtra("status",mList.get(getAdapterPosition()).getOrder_status());
                    mCtx.startActivity(intent);
                }
            });

            confirmOrder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final ProgressDialog loginDialog = new ProgressDialog(mCtx);
                    loginDialog.setMessage("Please wait..");
                    loginDialog.setCancelable(false);
                    loginDialog.show();

                    Holders holders = AllApiClass.getInstance().getApi();
                    Call<SubmitDataResModel> call = holders.orderConfirmationByLibrary(mList.get(getAdapterPosition()).getOrder_id());
                    call.enqueue(new Callback<SubmitDataResModel>() {
                        @Override
                        public void onResponse(Call<SubmitDataResModel> call, Response<SubmitDataResModel> response) {
                            if (response.isSuccessful()){
                                if (response.body().getResponse().getCode() == 1){
                                    loginDialog.dismiss();
                                    tvOrderStatus.setText("Order Confirmed");
                                }else {
                                    loginDialog.dismiss();
                                    Toast.makeText(mCtx, ""+response.body().getResponse().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }else {
                                loginDialog.dismiss();
                                Toast.makeText(mCtx, "Something went wrong !", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<SubmitDataResModel> call, Throwable t) {
                            loginDialog.dismiss();
                            Toast.makeText(mCtx, ""+t, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });

        }
    }

    private String convertTime(String time) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat format1 = new SimpleDateFormat("dd MMM yyyy hh:mm aa");
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
