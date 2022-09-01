package com.lib.liblibgo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.lib.liblibgo.R;
import com.lib.liblibgo.listner.OnItemClickListener;
import com.lib.liblibgo.model.FlatListData;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FlatAdapter extends RecyclerView.Adapter<FlatAdapter.MyViewHolder>  {
    List<FlatListData> mList = Collections.emptyList();
    Context context;
    private OnItemClickListener listener;

    public FlatAdapter(List<FlatListData> mList, Context context) {
        this.mList = mList;
        this.context = context;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView tvApartmentName;
        LinearLayout ll_click;

        public MyViewHolder(View view) {
            super(view);
            tvApartmentName = (TextView)view.findViewById(R.id.tvApartmentName);
            ll_click = (LinearLayout) view.findViewById(R.id.ll_click);
        }

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_spinner_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final int i = position;
        //holder.tv_noti.setText(mList.get(position).content);
        //holder.tvApartmentName.setSelected(true);
        holder.tvApartmentName.setText(mList.get(position).getFlat_no());

        holder.ll_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null){
                    listener.onItemClick(position);
                }
            }
        });
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }

    @Override
    public int getItemCount()
    {
        return mList.size();
    }

    public void setFilter(List<FlatListData> arrayList) {
        mList = new ArrayList<>();
        mList.addAll(arrayList);
        notifyDataSetChanged();
    }
}
