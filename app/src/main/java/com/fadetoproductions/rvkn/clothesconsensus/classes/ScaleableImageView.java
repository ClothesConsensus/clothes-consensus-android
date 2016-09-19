package com.fadetoproductions.rvkn.clothesconsensus.classes;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by rnewton on 9/19/16.
 */
public class ScaleableImageView extends ImageView implements View.OnTouchListener, ScaleGestureDetector.OnScaleGestureListener {

    ScaleGestureDetector mScaleDetector =
            new ScaleGestureDetector(getContext(), this);

    public ScaleableImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onScale(ScaleGestureDetector detector) {
        // Code for scale here
        return true;
    }

    @Override
    public boolean onScaleBegin(ScaleGestureDetector detector) {
        // Code for scale begin here
        return true;
    }

    @Override
    public void onScaleEnd(ScaleGestureDetector detector) {
        // Code for scale end here
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (mScaleDetector.onTouchEvent(event))
            return true;
        return super.onTouchEvent(event);
    }
}