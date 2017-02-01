package com.aminiam.moviekade.custom_view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.aminiam.moviekade.R;

public class DotIndicator extends LinearLayout {
    private static final String LOG_TAG = DotIndicator.class.getSimpleName();

    private int mWidth;
    private int mHeight;
    private int mActiveColor;
    private int mDeactivateColor;
    private int mDotCount;
    private int mDotSize;
    private int mActiveDot;
    private int mDotMargin;

    private Paint mActivePaint;
    private Paint mDeactivatePaint;

    public DotIndicator(Context context) {
        super(context);

        mActiveColor = ContextCompat.getColor(context, R.color.dot_indicator_active);
        mDeactivateColor = ContextCompat.getColor(context, R.color.dot_indicator_deactivate);
        mDotCount = 2;
        mDotSize = getResources().getDimensionPixelSize(R.dimen.indicator_size);
        mActiveDot = 0;
        mDotMargin = getResources().getDimensionPixelSize(R.dimen.indicator_dot_margin);

        init(context);
    }

    public DotIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs,
                R.styleable.DotIndicator, 0, 0);
        try {
            mActiveColor = typedArray.getColor(R.styleable.DotIndicator_activeColor,
                    ContextCompat.getColor(context, R.color.dot_indicator_active));
            mDeactivateColor = typedArray.getColor(R.styleable.DotIndicator_deactivateColor,
                    ContextCompat.getColor(context, R.color.dot_indicator_deactivate));
            mDotCount = typedArray.getInteger(R.styleable.DotIndicator_dotCount, 2);
            mDotSize = typedArray.getInteger(R.styleable.DotIndicator_dotSize,
                    getResources().getDimensionPixelSize(R.dimen.indicator_size));
            mActiveDot = typedArray.getInteger(R.styleable.DotIndicator_activeDot, 0);
            mDotMargin = typedArray.getInteger(R.styleable.DotIndicator_dotMargin,
                    getResources().getDimensionPixelSize(R.dimen.indicator_dot_margin));
        } finally {
            typedArray.recycle();
        }

        init(context);
    }

    private void init(Context context) {
        setWillNotDraw(false);
        mActivePaint = new Paint();
        mActivePaint.setColor(mActiveColor);

        mDeactivatePaint = new Paint();
        mDeactivatePaint.setColor(mDeactivateColor);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mWidth = getWidth();
        mHeight = getHeight();

        float startX = (mWidth / 2) - (((mDotSize + mDotMargin) * (mDotCount-1)) / 2);

        for (int i = 0; i < mDotCount; i++) {
            Paint paint;
            if(i == mActiveDot) {
                paint = mActivePaint;
            } else {
                paint = mDeactivatePaint;
            }
            canvas.drawCircle(startX + (i * mDotSize) + (i * mDotMargin), getHeight() / 2,
                    mDotSize/2, paint);
        }
    }

    public void setActiveDot(int position) {
        mActiveDot = position;
        requestLayout();
        invalidate();
    }
}
