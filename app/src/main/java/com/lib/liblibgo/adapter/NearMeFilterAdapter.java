package com.lib.liblibgo.adapter;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.lib.liblibgo.R;
import com.lib.liblibgo.common.Constants;
import com.lib.liblibgo.common.UserCurrentLocation;
import com.lib.liblibgo.common.UserDatabase;
import com.lib.liblibgo.listner.OnItemClickListener;
import com.lib.liblibgo.listner.OnItemClickListenerThree;
import com.lib.liblibgo.model.NearMeFilterModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class NearMeFilterAdapter extends RecyclerView.Adapter<NearMeFilterAdapter.MyViewHolder>  {
    List<NearMeFilterModel> mList = Collections.emptyList();
    Context context;
    private OnItemClickListenerThree listener;
    private String nearByCityValue="";
    private String nearByAreaValue="";
    private String nearByApartmentValue="";
    private String nearByDistanceValue="";
    int row_index = -1;

    public NearMeFilterAdapter(List<NearMeFilterModel> mList, Context context) {
        this.mList = mList;
        this.context = context;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView tvLoc;
        RelativeLayout rlLayout;

        public MyViewHolder(View view) {
            super(view);
            tvLoc = (TextView)view.findViewById(R.id.tvLoc);
            rlLayout = (RelativeLayout)view.findViewById(R.id.rlLayout);
        }

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.filter_location_row, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        //final int i = position;
        holder.tvLoc.setText(mList.get(position).getFilter());

        /*if (!Constants.selectedNearByCity.equals("")){
            holder.rlLayout.setBackgroundResource(R.drawable.btn_bg);
        }else {
            holder.rlLayout.setBackgroundResource(R.drawable.btn_bg_grey);
        }*/

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                row_index=position;
                notifyDataSetChanged();
            }
        });

        if(row_index==position){
            holder.rlLayout.setBackgroundResource(R.drawable.btn_bg);
            UserCurrentLocation location = new UserCurrentLocation(context);
            UserDatabase database = new UserDatabase(context);
            if (listener != null){
                if (mList.get(position).getFilter().equals("Within City")){
                    nearByCityValue = getUserCity(location.latitude,location.longitude);
                    nearByDistanceValue = "";
                    nearByAreaValue = "";
                    nearByApartmentValue = "";
                }else if (mList.get(position).getFilter().equals("Within Area")){
                    nearByAreaValue = getUserPinCode(location.latitude,location.longitude);
                    nearByDistanceValue = "";
                    nearByCityValue = "";
                    nearByApartmentValue = "";
                }else if (mList.get(position).getFilter().equals("Within Community")){
                    nearByApartmentValue = database.getUserId();
                    nearByDistanceValue = "";
                    nearByCityValue = "";
                    nearByAreaValue = "";
                }else if (mList.get(position).getFilter().equals("Within 2km")){
                    nearByDistanceValue = "2";
                    nearByCityValue = "";
                    nearByApartmentValue = "";
                    nearByAreaValue = "";
                }else if (mList.get(position).getFilter().equals("Within 3km")){
                    nearByDistanceValue = "5";
                    nearByCityValue = "";
                    nearByApartmentValue = "";
                    nearByAreaValue = "";
                }else if (mList.get(position).getFilter().equals("Within 10km")){
                    nearByDistanceValue = "10";
                    nearByCityValue = "";
                    nearByApartmentValue = "";
                    nearByAreaValue = "";
                }
                listener.onItemClick(position,nearByCityValue,nearByAreaValue,nearByApartmentValue,nearByDistanceValue);
            }
        }else {
            holder.rlLayout.setBackgroundResource(R.drawable.btn_bg_grey);
        }


    }

    public void setOnItemClickListener(OnItemClickListenerThree listener){
        this.listener = listener;
    }

    @Override
    public int getItemCount()
    {
        return mList.size();
    }

    public String getUserCity(double lat,double lon){
        //location = new UserCurrentLocation(this);
        String city="";
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        List<Address> addresses = new ArrayList<>();
        try {
            addresses = geocoder.getFromLocation(lat, lon, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (!addresses.isEmpty()){
            city = addresses.get(0).getSubAdminArea();
        }else {
            city = "";
        }
        return city;
    }

    public String getUserPinCode(double lat,double lon){
        //location = new UserCurrentLocation(this);
        String area = "";
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        List<Address> addresses = new ArrayList<>();
        try {
            addresses = geocoder.getFromLocation(lat, lon, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (!addresses.isEmpty()){
            //city = addresses.get(0).getLocality();
            area = addresses.get(0).getPostalCode();
        }else {
            area = "";
        }
        return area;
    }

}
