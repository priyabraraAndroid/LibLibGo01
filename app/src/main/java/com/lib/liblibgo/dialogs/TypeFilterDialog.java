package com.lib.liblibgo.dialogs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.lib.liblibgo.R;
import com.lib.liblibgo.dashboard.book_details.BookCatActivity;

public class TypeFilterDialog extends BottomSheetDialogFragment {

    private TextView tvForRent,tvForSale,tvForBoth,tvForGiveaway;
    private String selectedFilterValue = "";
    private int selectedFilterOption = 0;
    private ImageView ivClose;

    public static TypeFilterDialog newInstance() {
        return new TypeFilterDialog();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.type_filter_dialog, container, false);

        tvForRent = (TextView)view.findViewById(R.id.tvForRent);
        tvForSale = (TextView)view.findViewById(R.id.tvForSale);
        tvForBoth = (TextView)view.findViewById(R.id.tvForBoth);
        tvForGiveaway = (TextView)view.findViewById(R.id.tvForGiveaway);

        ivClose = (ImageView)view.findViewById(R.id.ivClose);

        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        tvForRent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedFilterValue = "For Rent";
                selectedFilterOption = 1;
                getData(selectedFilterValue,selectedFilterOption);
            }
        });

        tvForSale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedFilterValue = "For Sale";
                selectedFilterOption = 1;
                getData(selectedFilterValue,selectedFilterOption);
            }
        });

        tvForBoth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedFilterValue = "Both";
                selectedFilterOption = 1;
                getData(selectedFilterValue,selectedFilterOption);
            }
        });

        tvForGiveaway.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedFilterValue = "yes";
                selectedFilterOption = 2;
                getData(selectedFilterValue,selectedFilterOption);
            }
        });

        return view;
    }

    private void getData(String value, int option) {
        //((BookCatActivity)getActivity()).setDataFilterType(value,option);
        dismiss();
    }

    @Override
    public int getTheme() {
        return R.style.BottomSheetDialogTheme;
    }

}
