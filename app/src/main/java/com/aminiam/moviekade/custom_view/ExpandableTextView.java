package com.aminiam.moviekade.custom_view;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aminiam.moviekade.R;

public class ExpandableTextView extends LinearLayout implements View.OnClickListener {
    private boolean mIsCollapsed;
    private String mContent;
    private int mMaxLines;
    private String mButtonPrimaryText;
    private String mButtonSecondaryText;
    private int mButtonColor;

    private TextView mTxtContent;
    private TextView mButton;

    public ExpandableTextView(Context context) {
        super(context);
        mContent = "";
        mMaxLines = 2;
        mButtonPrimaryText = "";
        mButtonSecondaryText = "";
        mButtonColor = ContextCompat.getColor(context, R.color.colorAccent);

        init(context);
    }

    public ExpandableTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs,
                R.styleable.ExpandableTextView, 0, 0);

        try {
            mContent = typedArray.getString(R.styleable.ExpandableTextView_text);
            mMaxLines = typedArray.getInteger(R.styleable.ExpandableTextView_maxLines, 2);
            mButtonPrimaryText = typedArray.getString(R.styleable.ExpandableTextView_button_textPrimary);
            mButtonSecondaryText = typedArray.getString(R.styleable.ExpandableTextView_button_textSecondary);
            mButtonColor = typedArray.getColor(R.styleable.ExpandableTextView_buttonColor,
                    ContextCompat.getColor(context, R.color.colorAccent));
        } finally {
            typedArray.recycle();
        }

        init(context);
    }

    private void init(Context context) {
        setOrientation(LinearLayout.VERTICAL);
        mIsCollapsed = true;

        mTxtContent = new TextView(context);
        mTxtContent.setText(mContent);
        mTxtContent.setMaxLines(mMaxLines);
        addView(mTxtContent);

        mButton = new Button(context);
        mButton.setTextColor(mButtonColor);
        mButton.setBackgroundResource(0);
        mButton.setText(mButtonPrimaryText);
        mButton.setOnClickListener(this);
        addView(mButton);
    }

    public void setMaxLines(int maxLines) {
        mMaxLines = maxLines;
        mTxtContent.setMaxLines(mMaxLines);

        requestLayout();
        invalidate();
    }

    public void setContent(String content) {
        mContent = content;
        mTxtContent.setText(mContent);
    }

    public String getContent() {
        return mContent;
    }

    public int getMaxLines() {
        return mMaxLines;
    }

    public void setCollapsed(boolean isCollapsed) {
        if(isCollapsed) {
            mTxtContent.setMaxLines(mMaxLines);
        } else {
            mTxtContent.setMaxLines(mTxtContent.getLineCount());
        }

        requestLayout();
        invalidate();
    }

    public boolean isCollapsed() {
        return mIsCollapsed;
    }

    public void setButtonPrimaryText(String value) {
        mButtonPrimaryText = value;
        if(mIsCollapsed) {
            mButton.setText(mButtonPrimaryText);

            requestLayout();
            invalidate();
        }
    }

    public String getButtonPrimaryText() {
        return mButtonPrimaryText;
    }

    public void setButtonSecondaryText(String value) {
        mButtonSecondaryText = value;
        if(!isCollapsed()) {
            mButton.setText(mButtonSecondaryText);

            requestLayout();
            invalidate();
        }
    }

    public void setButtonColor(int color) {
        mButtonColor = color;
        mButton.setTextColor(mButtonColor);

        requestLayout();
        invalidate();
    }

    public int getButtonColor() {
        return mButtonColor;
    }

    @Override
    public void onClick(View v) {
        if (mIsCollapsed) {
            expandTextView(mTxtContent);
            mButton.setText(mButtonSecondaryText);
        } else {
            collapseTextView(mTxtContent);
            mButton.setText(mButtonPrimaryText);
        }

        mIsCollapsed = !mIsCollapsed;
        invalidate();
        requestLayout();
    }

    private void expandTextView(TextView tv){
        ObjectAnimator animation = ObjectAnimator.ofInt(tv, "maxLines", tv.getLineCount());
        animation.setDuration(200).start();
    }

    private void collapseTextView(TextView tv){
        ObjectAnimator animation = ObjectAnimator.ofInt(tv, "maxLines", mMaxLines);
        animation.setDuration(200).start();
    }
}
