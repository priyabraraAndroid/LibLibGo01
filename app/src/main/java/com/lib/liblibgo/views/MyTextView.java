package com.lib.liblibgo.views;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;

public class MyTextView extends AppCompatTextView {
    public MyTextView(Context context) {
        super(context);
        initView();
    }

    public MyTextView(Context context,AttributeSet attrs) {
        super(context, attrs);
    }

    public MyTextView( Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    private void initView() {
        if (!isInEditMode()) {
            Typeface tf = Typeface.createFromAsset(getContext().getAssets(),"font/poppins.ttf");
            setTypeface(tf);
        }
    }
}
