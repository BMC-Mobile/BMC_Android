package com.liuyufei.bmc_android.utility;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.IntegerRes;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.util.Log;

import com.liuyufei.bmc_android.R;

/**
 * Created by Administrator on 06/16/17.
 */

public class AspectRatioImageView extends android.support.v7.widget.AppCompatImageView {
    static String TAG = "AspectRatioImageView";
    double ratioAttr=1;

    public AspectRatioImageView(Context context) {
        super(context);
    }

    public AspectRatioImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.AspectRatioImageView,0,0);
        ratioAttr = a.getFloat(0,1);
        a.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        Log.i(TAG,"widthMeasure:"+getMeasuredWidth());
        int height = (int) (getMeasuredHeight() * ratioAttr);
        int width = (int) (getMeasuredWidth() * ratioAttr);
        setMeasuredDimension(width,height);
        //setMeasuredDimension(getMeasuredWidth(), (int) (getMeasuredWidth()/ratio));
    }
}
