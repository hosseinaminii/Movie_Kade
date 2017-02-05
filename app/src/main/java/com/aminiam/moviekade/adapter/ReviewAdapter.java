package com.aminiam.moviekade.adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aminiam.moviekade.R;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder>{

    private Context mContext;
    private String[][] mReviews;

    public ReviewAdapter(Context context) {
        mContext = context;
    }

    @Override
    public ReviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.review_list_item, parent, false);
        return new ReviewViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(ReviewViewHolder holder, int position) {
        if(mReviews == null) return;

        String author = mReviews[position][0];
        String review = mReviews[position][1];
        holder.mTxtAuthor.setText(String.format(mContext.getString(R.string.review_author), author));
        holder.mTxtReview.setText(review);
    }

    public void populateData(String[][] reviews) {
        mReviews = reviews;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mReviews != null ? mReviews.length : 0;
    }

    class ReviewViewHolder extends RecyclerView.ViewHolder {
        TextView mTxtAuthor;
        TextView mTxtReview;

        ReviewViewHolder(View itemView) {
            super(itemView);

            mTxtAuthor = (TextView) itemView.findViewById(R.id.txtAuthor);
            mTxtReview = (TextView) itemView.findViewById(R.id.txtReview);
        }
    }
}
