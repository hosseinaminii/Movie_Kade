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
import com.aminiam.moviekade.utility.Utility;

public class ExpandableTextView extends LinearLayout implements View.OnClickListener {
    private static final String LOG_TAG = ExpandableTextView.class.getSimpleName();

    private boolean mIsCollapsed;
    private String mContent;
    private int mMaxLines;
    private String mButtonPrimaryText;
    private String mButtonSecondaryText;
    private int mButtonColor;
    private float descTextSize;

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
            descTextSize = typedArray.getDimension(R.styleable.ExpandableTextView_descTextSize, 20);
        } finally {
            typedArray.recycle();
        }

        init(context);
    }

    private void init(Context context) {
        setOrientation(LinearLayout.VERTICAL);
        mIsCollapsed = true;

        mTxtContent = new TextView(context);
        mButton = new Button(context);

        setContent(mContent);
        mTxtContent.setTextSize(Utility.convertPixelsToDp(context, descTextSize));
        mTxtContent.setMaxLines(mMaxLines);
        addView(mTxtContent);

        mButton.setTextColor(mButtonColor);
        mButton.setBackgroundResource(0);
        mButton.setText(mButtonPrimaryText);
        mButton.setOnClickListener(this);
        addView(mButton);

    }

    public void setMaxLines(int maxLines) {
        mMaxLines = maxLines;
        mTxtContent.setMaxLines(mMaxLines);

        refresh();
    }

    public void setContent(String content) {
        mButton.setVisibility(VISIBLE);
        mContent = content;
        mTxtContent.setText(mContent);

        refresh();

        mTxtContent.post(new Runnable() {
            @Override
            public void run() {
                if(mTxtContent.getLineCount() <= mMaxLines) { mButton.setVisibility(INVISIBLE);
                } else { mButton.setVisibility(VISIBLE); }
            }
        });
    }

    public String getContent() {
        return mContent;
    }

    public int getMaxLines() {
        return mMaxLines;
    }

    public void setCollapsed(boolean isCollapsed) {
        if (isCollapsed) {
            mTxtContent.setMaxLines(mMaxLines);
        } else {
            mTxtContent.setMaxLines(mTxtContent.getLineCount());
        }

        refresh();
    }

    public boolean isCollapsed() {
        return mIsCollapsed;
    }

    public void setButtonPrimaryText(String value) {
        mButtonPrimaryText = value;
        if (mIsCollapsed) {
            mButton.setText(mButtonPrimaryText);

            refresh();
        }
    }

    public String getButtonPrimaryText() {
        return mButtonPrimaryText;
    }

    public void setButtonSecondaryText(String value) {
        mButtonSecondaryText = value;
        if (!isCollapsed()) {
            mButton.setText(mButtonSecondaryText);

            refresh();
        }
    }

    public void setButtonColor(int color) {
        mButtonColor = color;
        mButton.setTextColor(mButtonColor);

        refresh();
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
        refresh();
    }

    private void expandTextView(TextView tv) {
        ObjectAnimator animation = ObjectAnimator.ofInt(tv, "maxLines", tv.getLineCount());
        animation.setDuration(100).start();
    }

    private void collapseTextView(TextView tv) {
        ObjectAnimator animation = ObjectAnimator.ofInt(tv, "maxLines", mMaxLines);
        animation.setDuration(100).start();
    }

    private void refresh() {
        invalidate();
        requestLayout();
    }
}
