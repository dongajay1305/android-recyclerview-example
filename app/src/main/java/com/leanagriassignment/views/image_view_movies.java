package com.leanagriassignment.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by jaydonga on 9/21/16.
 */
public class image_view_movies extends android.support.v7.widget.AppCompatImageView {

    public image_view_movies(Context context) {
        super(context);
    }

    public image_view_movies(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public image_view_movies(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int width = getMeasuredWidth();
        //Logs.e("my log", "custim image view width : " + width);
        //int height = getMeasuredWidth();
        setMeasuredDimension(width, (int) (width * 1.4));

    }
}


