package com.leanagriassignment.views;

import android.content.Context;
import android.util.AttributeSet;

/**
 * Created by jaydonga on 9/21/16.
 */

public class ImageView_Detail extends android.support.v7.widget.AppCompatImageView {

    public ImageView_Detail(Context context) {
        super(context);
    }

    public ImageView_Detail(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ImageView_Detail(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int width = getMeasuredWidth();
        //Logs.e("my log", "custim image view width : " + width);
        //int height = getMeasuredWidth();
        setMeasuredDimension(width, (int) (width*1));
        //Log.e("webservice","image width : "+width);
        //Log.e("webservice","image height : "+(int) (width * 0.6));

    }
}


