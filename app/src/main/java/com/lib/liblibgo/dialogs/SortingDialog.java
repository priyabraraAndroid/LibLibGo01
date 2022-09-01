package com.lib.liblibgo.dialogs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.lib.liblibgo.R;
import com.lib.liblibgo.common.Constants;
import com.lib.liblibgo.listner.SortListener;

public class SortingDialog extends BottomSheetDialogFragment {

    private RadioGroup rgSort;
    private String sortingValue = "";
    SortListener callback;
    private RadioButton rb_1,rb_2,rb_3,rb_4,rb_5;
    private View view;


    public SortingDialog newInstance(SortListener callback) {
        this.callback=callback;
        return new SortingDialog();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.sorting_dialog, container, false);

        rgSort = (RadioGroup)view.findViewById(R.id.rgSort);
        rb_1 = (RadioButton)view.findViewById(R.id.rb_1);
        rb_2 = (RadioButton)view.findViewById(R.id.rb_2);
        rb_3 = (RadioButton)view.findViewById(R.id.rb_3);
        rb_4 = (RadioButton)view.findViewById(R.id.rb_4);
        rb_5 = (RadioButton)view.findViewById(R.id.rb_5);


        if (Constants.sortingValueCommunity.equals("popular")){
            rb_1.setChecked(true);
            rb_2.setChecked(false);
            rb_3.setChecked(false);
            rb_4.setChecked(false);
            rb_5.setChecked(false);
        }else if (Constants.sortingValueCommunity.equals("atoz")){
            rb_1.setChecked(false);
            rb_2.setChecked(true);
            rb_3.setChecked(false);
            rb_4.setChecked(false);
            rb_5.setChecked(false);
        }else if (Constants.sortingValueCommunity.equals("ltoh")){
            rb_1.setChecked(false);
            rb_2.setChecked(false);
            rb_3.setChecked(true);
            rb_4.setChecked(false);
            rb_5.setChecked(false);
        }else if (Constants.sortingValueCommunity.equals("htol")){
            rb_1.setChecked(false);
            rb_2.setChecked(false);
            rb_3.setChecked(false);
            rb_4.setChecked(true);
            rb_5.setChecked(false);
        }else {
            rb_1.setChecked(false);
            rb_2.setChecked(false);
            rb_3.setChecked(false);
            rb_4.setChecked(false);
            rb_5.setChecked(false);
        }

        rgSort.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                dismiss();
            }
        });

        return view;
    }

    @Override
    public int getTheme() {
        return R.style.BottomSheetDialogTheme;
    }

    @Override
    public void onDestroy() {
        if (rb_1.isChecked()){
            sortingValue = "popular";
        }else if (rb_2.isChecked()){
            sortingValue = "atoz";
        }else if (rb_3.isChecked()){
            sortingValue = "ltoh";
        }else if (rb_4.isChecked()){
            sortingValue = "htol";
        }else if (rb_5.isChecked()){
            sortingValue = "";
        }else {
            sortingValue = "";
        }
        callback.onShoring(sortingValue);
        super.onDestroy();
    }
}
