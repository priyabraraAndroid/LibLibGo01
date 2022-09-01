package com.lib.liblibgo.activity.homedashboard.fragmentshome;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.lib.liblibgo.R;
import com.lib.liblibgo.adapter.MyOrderAdapter;
import com.lib.liblibgo.api.AllApiClass;
import com.lib.liblibgo.api.Holders;
import com.lib.liblibgo.common.UserDatabase;
import com.lib.liblibgo.dashboard.book_details.CheckoutActivity;
import com.lib.liblibgo.model.CheckoutModel;
import com.lib.liblibgo.model.MyOederModel;
import com.lib.liblibgo.model.MyTransactionModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TransactionFragment extends Fragment {

    private UserDatabase database;
    private ProgressBar progBar;
    private RecyclerView rvTransHis;
    private List<MyTransactionModel.ResData.TransList> myList = new ArrayList<>();
    private TransactionAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_transaction, container, false);
        database = new UserDatabase(getContext());
        progBar = (ProgressBar)view.findViewById(R.id.progBar);
        rvTransHis = view.findViewById(R.id.rvTransHis);

        return view;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser){
            getTransactionList();
        }
    }

    private void getTransactionList() {
        progBar.setVisibility(View.VISIBLE);
        Holders holders = AllApiClass.getInstance().getApi();
        Call<MyTransactionModel> responseCall = holders.getTransactionHistory(database.getUserId());

        responseCall.enqueue(new Callback<MyTransactionModel>() {
            @Override
            public void onResponse(Call<MyTransactionModel> call, Response<MyTransactionModel> response) {
                if (response.isSuccessful()){
                    if (response.body().getResponse().getCode().equals("1")){
                        progBar.setVisibility(View.GONE);
                        myList = response.body().getResponse().getHistory_list();
                        if (myList.size() > 0){
                            adapter = new TransactionAdapter(myList, getContext());
                            LinearLayoutManager verticalManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                            //verticalManager.setReverseLayout(true);
                            //verticalManager.setStackFromEnd(true);
                            rvTransHis.setLayoutManager(verticalManager);
                            rvTransHis.setAdapter(adapter);
                        }
                    }else {
                        progBar.setVisibility(View.GONE);
                        Toast.makeText(getContext(), ""+response.body().getResponse().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }else {
                    progBar.setVisibility(View.GONE);
                    Toast.makeText(getContext(), "Something went wrong !", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<MyTransactionModel> call, Throwable t) {
                progBar.setVisibility(View.GONE);
                Toast.makeText(getContext(), "Server not responding.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.CartListHolder> {
        List<MyTransactionModel.ResData.TransList> mList;
        Context mCtx;
        //int count = 0;

        public TransactionAdapter(List<MyTransactionModel.ResData.TransList> mList, Context mCtx) {
            this.mList = mList;
            this.mCtx = mCtx;
        }

        @NonNull
        @Override
        public CartListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View mView = LayoutInflater.from(mCtx).inflate(R.layout.trans_row, parent, false);
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
            TextView tvTxt,tvLibCoin,tvDate;

            public CartListHolder(@NonNull View itemView) {
                super(itemView);
                tvTxt = itemView.findViewById(R.id.tvTxt);
                tvLibCoin = itemView.findViewById(R.id.tvLibCoin);
                tvDate = itemView.findViewById(R.id.tvDate);
            }

            public void bind(int pos) {
               /* if (mList.get(getAdapterPosition()).getStatus().equals("0")){
                    tvTxt.setText("Redeem request has been sent.Wait for 5-7 days to add money to your bank account.");
                    tvLibCoin.setTextColor(Color.parseColor("#07950D"));
                }else {
                    tvTxt.setText("Redeem request has been canceled. Please try again.");
                    tvLibCoin.setTextColor(Color.parseColor("#D81406"));
                }*/

                if (mList.get(getAdapterPosition()).getIniate_status().equals("2")){
                    tvTxt.setText("It will take 5-7 working days for money to be added in the bank linked to your UPI ID.");
                    //tvLibCoin.setTextColor(Color.parseColor("#07950D"));
                }else if (mList.get(getAdapterPosition()).getIniate_status().equals("3")){
                    tvTxt.setText("Congratulations! you got bonus libcoins for your fist signup.");
                }else {
                    if (mList.get(getAdapterPosition()).getOrder_for().equals("rent")){
                        tvTxt.setText("You ordered a book for rent.");
                    }else {
                        tvTxt.setText("You buy a book.");
                    }
                }

                if (mList.get(getAdapterPosition()).getTransaction_type().equals("debit")){
                    tvLibCoin.setText("- Rs."+mList.get(getAdapterPosition()).getCoins());
                    tvLibCoin.setTextColor(Color.parseColor("#D81406"));
                }else {
                    tvLibCoin.setText("+ Rs."+mList.get(getAdapterPosition()).getCoins());
                    tvLibCoin.setTextColor(Color.parseColor("#07950D"));
                }

                tvDate.setText(convertTime(mList.get(getAdapterPosition()).getTransaction_date()));
            }
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