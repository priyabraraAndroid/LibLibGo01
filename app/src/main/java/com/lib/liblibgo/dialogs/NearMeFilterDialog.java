package com.lib.liblibgo.dialogs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.lib.liblibgo.R;
import com.lib.liblibgo.adapter.NearMeFilterAdapter;
import com.lib.liblibgo.common.UserDatabase;
import com.lib.liblibgo.dashboard.book_details.BookCatActivity;
import com.lib.liblibgo.listner.OnItemClickListener;
import com.lib.liblibgo.model.NearMeFilterModel;

import java.util.ArrayList;
import java.util.List;

public class NearMeFilterDialog extends BottomSheetDialogFragment {

    private RecyclerView rvLocation;
    private List<NearMeFilterModel> myList = new ArrayList<>();
    private NearMeFilterAdapter adapter;
    private ImageView ivClose;
    private UserDatabase dataBase;

    public static NearMeFilterDialog newInstance() {
        return new NearMeFilterDialog();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.nearme_filter_dialog, container, false);

        dataBase = new UserDatabase(getContext());

        rvLocation = (RecyclerView)view.findViewById(R.id.rvLocation);
        ivClose = (ImageView)view.findViewById(R.id.ivClose);

        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        setFilterData();

        return view;
    }

    @Override
    public int getTheme() {
        return R.style.BottomSheetDialogTheme;
    }

    private void setFilterData() {
        myList.add(new NearMeFilterModel("Within 2km"));
        myList.add(new NearMeFilterModel("Within 3km"));
        myList.add(new NearMeFilterModel("Within 5km"));
        myList.add(new NearMeFilterModel("Within City"));
        myList.add(new NearMeFilterModel("Within Apartment"));
        myList.add(new NearMeFilterModel("Within Area"));
        myList.add(new NearMeFilterModel("All"));

        adapter = new NearMeFilterAdapter(myList, getContext());
        GridLayoutManager manager = new GridLayoutManager(getContext(),3);
        rvLocation.setLayoutManager(manager);
        rvLocation.setAdapter(adapter);

        /*adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                if (myList.get(position).getFilter().equals("Within Apartment")){
                    if (dataBase.getApartmentName().equals("") || dataBase.getApartmentName().equals("null")){
                        Toast.makeText(getContext(), "You are not in any apartment.", Toast.LENGTH_SHORT).show();
                    }else {
                        addItemClick(position);
                    }
                }else {
                    addItemClick(position);
                }
            }
        });*/
    }

    private void addItemClick(int position) {
       // ((BookCatActivity)getActivity()).setData(myList.get(position).getFilter());
        dismiss();
    }
}
