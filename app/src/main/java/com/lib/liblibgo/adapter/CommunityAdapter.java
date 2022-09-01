package com.lib.liblibgo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.lib.liblibgo.R;
import com.lib.liblibgo.common.Constants;
import com.lib.liblibgo.listner.MoveToCommunityClickListener;
import com.lib.liblibgo.model.CommunityListModel;
import com.lib.liblibgo.model.MyOwnBooksModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class CommunityAdapter extends RecyclerView.Adapter<CommunityAdapter.LibraryHolder> {
    List<CommunityListModel.ResDataBooks.CommunityList> mList;
    Context mCtx;
    MoveToCommunityClickListener listener;
    private boolean isSelected = true;

    public CommunityAdapter(List<CommunityListModel.ResDataBooks.CommunityList> mList, Context mCtx) {
        this.mList = mList;
        this.mCtx = mCtx;
    }

    @NonNull
    @Override
    public LibraryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(mCtx).inflate(R.layout.community_list_row, parent, false);
        return new LibraryHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull LibraryHolder holder, int position) {
        holder.bind();
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class LibraryHolder extends RecyclerView.ViewHolder {
        TextView tvLibraryName,tvLibraryAdr;
        ImageView iv_img;
        CardView card;
        CheckBox cbSelectHolder;

        public LibraryHolder(@NonNull View itemView) {
            super(itemView);
            tvLibraryName = itemView.findViewById(R.id.tvLibraryName);
            tvLibraryAdr = itemView.findViewById(R.id.tvLibraryAdr);
            card = itemView.findViewById(R.id.card);
            iv_img = itemView.findViewById(R.id.iv_img);
            cbSelectHolder = itemView.findViewById(R.id.cbSelectHolder);

        }

        public void bind() {
            tvLibraryName.setText(mList.get(getAdapterPosition()).getCommunity_library_name());
            tvLibraryAdr.setText(mList.get(getAdapterPosition()).getCommunity_library_address());

            if (mList.get(getAdapterPosition()).getCommunity_library_image().equals("")){
                Glide.with(mCtx).load("https://sales.arecontvision.com/images/products/img_placeholder_50618_xl.jpg").into(iv_img);
            }else {
                Glide.with(mCtx).load(mList.get(getAdapterPosition()).getCommunity_library_image()).into(iv_img);
            }

            card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null){
                        listener.onItemClick(getAdapterPosition(),isSelected);
                    }
                }
            });

            Constants.selectedCommunityList.add(mList.get(getAdapterPosition()).getCommunity_id());
            Constants.selectedCommunityIds =  android.text.TextUtils.join(",",Constants.selectedCommunityList);

            cbSelectHolder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //isSelected = mList.get(getAdapterPosition()).getIsSelected() ? true : false;
                    if (cbSelectHolder.isChecked()){
                        Constants.selectedCommunityList.add(mList.get(getAdapterPosition()).getCommunity_id());
                    }else {
                        Constants.selectedCommunityList.remove(mList.get(getAdapterPosition()).getCommunity_id());
                    }

                    Constants.selectedCommunityIds =  android.text.TextUtils.join(",",Constants.selectedCommunityList);
                }
            });

        }
    }

    public void setOnItemClickListener(MoveToCommunityClickListener listener){
        this.listener = listener;
    }

}
