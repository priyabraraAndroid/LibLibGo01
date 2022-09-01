package com.lib.liblibgo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.lib.liblibgo.R;
import com.lib.liblibgo.views.MyTextView;

import java.util.ArrayList;

public class ApartSpAdapter extends BaseAdapter {
    private Context mCtx;
    private ArrayList<String> mList;
    private LayoutInflater inflter;

    public ApartSpAdapter(Context mCtx, ArrayList<String> mList) {
        this.mCtx = mCtx;
        this.mList = mList;
        inflter = (LayoutInflater.from(mCtx));
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflter.inflate(R.layout.row_spinner_item, null);
        MyTextView tvApartmentName = (MyTextView) view.findViewById(R.id.tvApartmentName);
        tvApartmentName.setText(mList.get(i));
        return view;
    }
}
