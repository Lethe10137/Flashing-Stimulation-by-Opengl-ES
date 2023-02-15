package com.java.lichenghao.eltext;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.util.Log;

public class GLSUrfaceVIewFixedRatio extends GLSurfaceView {
    public GLSUrfaceVIewFixedRatio(Context context) {
        super(context);
    }

    public GLSUrfaceVIewFixedRatio(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected  void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        Log.d("MyGLView", "specheight"+height);
        Log.d("MyGLView", "width"+width);
        setMeasuredDimension(width, (int)(width * 0.6));
    }
}
