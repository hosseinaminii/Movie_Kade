package com.aminiam.moviekade.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.aminiam.moviekade.R;
import com.aminiam.moviekade.adapter.ReviewAdapter;
import com.aminiam.moviekade.databinding.FragmentAllReviewBinding;

public class AllReviewFragment extends Fragment {
    private static final String LOG_TAG = AllReviewFragment.class.getSimpleName();

    private FragmentAllReviewBinding mBinding;
    private String[][] mReviews;
    ReviewAdapter mAdapter;

    public AllReviewFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        Bundle bundle = getArguments();
        mReviews = (String[][]) bundle.getSerializable(MovieDetailFragment.ALL_REVIEWS_KEY);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentAllReviewBinding.inflate(inflater, container, false);
        setupToolbar(getString(R.string.reviews));

        mAdapter = new ReviewAdapter(getActivity());

        mBinding.recReview.setLayoutManager(new LinearLayoutManager(getActivity()));
        mBinding.recReview.setAdapter(mAdapter);

        showReviews();

        return mBinding.getRoot();
    }

    private void setupToolbar(String title) {
        mBinding.toolbar.setTitle(title);
        ((AppCompatActivity) getActivity()).setSupportActionBar(mBinding.toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home) {
            getActivity().supportFinishAfterTransition();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void showReviews() {
        mAdapter.populateData(mReviews);
    }
}
