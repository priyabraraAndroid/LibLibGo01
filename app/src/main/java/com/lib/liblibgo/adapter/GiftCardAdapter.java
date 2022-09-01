package com.lib.liblibgo.adapter;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.lib.liblibgo.R;
import com.lib.liblibgo.listner.OnItemClickListener;
import com.lib.liblibgo.model.GiftCardModel;

import java.util.Collections;
import java.util.List;

public class GiftCardAdapter extends RecyclerView.Adapter<GiftCardAdapter.MyViewHolder> {
    List<GiftCardModel.ResModelData.RequestList> mList = Collections.emptyList();
    Context context;
    private OnItemClickListener listener;

    public GiftCardAdapter(List<GiftCardModel.ResModelData.RequestList> mList, Context context) {
        this.mList = mList;
        this.context = context;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvValue;
        TextView tvCoupon;
        ImageView ivCopy;

        public MyViewHolder(View view) {
            super(view);
            tvValue = (TextView) view.findViewById(R.id.tvValue);
            tvCoupon = (TextView) view.findViewById(R.id.tvCoupon);
            ivCopy = (ImageView) view.findViewById(R.id.ivCopy);
        }

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.gift_card_row, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final int i = position;
        holder.tvValue.setText("\u20B9 "+mList.get(position).getGift_value());

        if (mList.get(position).getCoupon_code().equals("")){
            holder.tvCoupon.setText("TSTAXVTYSW01VG");
        }else {
            holder.tvCoupon.setText(mList.get(position).getCoupon_code());
        }


        holder.ivCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("txt", holder.tvCoupon.getText().toString());
                clipboard.setPrimaryClip(clip);
                Toast.makeText(context, "Copied", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }
}
