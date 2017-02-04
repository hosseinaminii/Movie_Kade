package com.aminiam.moviekade.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aminiam.moviekade.R;
import com.aminiam.moviekade.databinding.FragmentReviewBinding;

public class ReviewFragment extends Fragment {

    private FragmentReviewBinding mBinding;
    private String mReviewContent;
    private String mReviewAuthor;

    public ReviewFragment() {}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();
        if(bundle.containsKey(MovieDetailFragment.REVIEW_CONTENT_KEY)) {
            mReviewContent = bundle.getString(MovieDetailFragment.REVIEW_CONTENT_KEY);
            mReviewAuthor = bundle.getString(MovieDetailFragment.REVIEW_AUTHOR_KEY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentReviewBinding.inflate(inflater, container, false);

        mBinding.txtReview.setText(mReviewContent);
        mBinding.txtAuthor.setText(String.format(getString(R.string.review_author), mReviewAuthor));

        return mBinding.getRoot();
    }

}
