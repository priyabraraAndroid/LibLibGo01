package com.lib.liblibgo.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.lib.liblibgo.R;
import com.lib.liblibgo.common.Constants;
import com.lib.liblibgo.listner.OnItemClickListener;
import com.lib.liblibgo.listner.OnItemClickListenerTow;
import com.lib.liblibgo.model.CategoryListData;

import java.util.Collections;
import java.util.List;

public class BookCatAdapter extends RecyclerView.Adapter<BookCatAdapter.MyViewHolder>  {
    List<CategoryListData> mList = Collections.emptyList();
    Context context;
    private OnItemClickListenerTow listener;
    private boolean isSelected;

    public BookCatAdapter(List<CategoryListData> mList, Context context) {
        this.mList = mList;
        this.context = context;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView tvLoc;
        RelativeLayout rlView;
        public MyViewHolder(View view) {
            super(view);
            tvLoc = (TextView)view.findViewById(R.id.tvLoc);
            rlView = (RelativeLayout)view.findViewById(R.id.rlView);
        }

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.filter_category_row, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final int i = position;
        holder.tvLoc.setText(mList.get(position).getCategory_name());
        holder.rlView.setBackgroundResource(mList.get(position).getIsSelected() ? R.drawable.btn_bg : R.drawable.btn_bg_grey);
        //isSelected = mList.get(position).getIsSelected() ? true : false;
        Log.d("catttt_id",Constants.selectedCategoriesCommunity);
        Log.d("catttt_id",mList.get(position).getCategory_id());

        /*if (Constants.selectedCategoriesCommunity.equals(mList.get(position).getCategory_id())){
            Log.d("aabbcc",""+mList.get(position).getCategory_name());
            isSelected = mList.get(position).getIsSelected() ? false : true;
            if (isSelected){
                Constants.selectedCatListCommunity.add(Constants.catListCommunity.get(position).getCategory_id());
            }else {
                Constants.selectedCatListCommunity.remove(Constants.catListCommunity.get(position).getCategory_id());
            }
           //mList.get(position).setIsSelected(!mList.get(position).getIsSelected());
            holder.rlView.setBackgroundColor(mList.get(position).getIsSelected() ? R.drawable.btn_bg : R.drawable.btn_bg_grey);

        }*/

        holder.rlView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("click item","aa : "+mList.get(position).getCategory_name());
                if (listener != null){
                    isSelected = mList.get(position).getIsSelected() ? false : true;
                    listener.onItemClick(position,isSelected);
                    mList.get(position).setIsSelected(!mList.get(position).getIsSelected());
                    holder.rlView.setBackgroundColor(mList.get(position).getIsSelected() ? R.drawable.btn_bg : R.drawable.btn_bg_grey);
                    notifyDataSetChanged();
                }
            }
        });
    }

    public void setOnItemClickListener(OnItemClickListenerTow listener){
        this.listener = listener;
    }

    @Override
    public int getItemCount()
    {
        return mList.size();
    }
}
