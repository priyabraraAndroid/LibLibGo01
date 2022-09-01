package com.lib.liblibgo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.lib.liblibgo.R;
import com.lib.liblibgo.listner.OnItemClickListener;
import com.lib.liblibgo.model.ApartmentNewModel;


import java.util.List;


public class ApartAdapter extends RecyclerView.Adapter<ApartAdapter.ApartHolder> {
    List<ApartmentNewModel.ApartNewRes.AprtList> mList;
    Context mCtx;
    OnItemClickListener listener;

    public ApartAdapter(List<ApartmentNewModel.ApartNewRes.AprtList> mList, Context mCtx) {
        this.mList = mList;
        this.mCtx = mCtx;
    }

    @NonNull
    @Override
    public ApartAdapter.ApartHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(mCtx).inflate(R.layout.apart_row, parent, false);
        return new ApartAdapter.ApartHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull ApartAdapter.ApartHolder holder, int position) {
        holder.bind();
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class ApartHolder extends RecyclerView.ViewHolder {
        TextView tvApartName,tvPin;

        public ApartHolder(@NonNull View itemView) {
            super(itemView);
            tvApartName = itemView.findViewById(R.id.tvApart);
            tvPin = itemView.findViewById(R.id.tvPin);
        }

        public void bind() {
            tvApartName.setText(mList.get(getAdapterPosition()).getApartment_name());
            tvPin.setText(mList.get(getAdapterPosition()).getPincode());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null){
                        listener.onItemClick(getAdapterPosition());
                    }
                }
            });
        }
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }
    
}
