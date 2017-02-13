package com.aminiam.moviekade.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aminiam.moviekade.R;
import com.aminiam.moviekade.databinding.FragmentMoreDataBinding;

public class MoreDataFragment extends Fragment implements View.OnClickListener{

    private int mIcon1;
    private int mIcon2;
    private int mIcon3;
    private String mContent1;
    private String mContent2;
    private String mContent3;

    private FragmentMoreDataBinding mBinding;

    public MoreDataFragment() {}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        if(args.containsKey(MovieDetailFragment.MORE_DATA_ICON_KEY)) {
            int[] icons = args.getIntArray(MovieDetailFragment.MORE_DATA_ICON_KEY);
            String[] contents = args.getStringArray(MovieDetailFragment.MORE_DATA_CONTENT_KEY);
            mIcon1 = icons[0];
            mIcon2 = icons[1];
            mIcon3 = icons[2];
            mContent1 = contents[0];
            mContent2 = contents[1];
            mContent3 = contents[2];
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)  {
        mBinding = FragmentMoreDataBinding.inflate(inflater, container, false);

        mBinding.icon1.setImageResource(mIcon1);
        mBinding.icon2.setImageResource(mIcon2);
        mBinding.icon3.setImageResource(mIcon3);
        mBinding.txt1.setText(mContent1);
        mBinding.txt2.setText(mContent2);
        mBinding.txt3.setText(mContent3);

        mBinding.lne1.setOnClickListener(this);
        mBinding.lne2.setOnClickListener(this);
        mBinding.lne3.setOnClickListener(this);

        return mBinding.getRoot();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.lne1: {
                String message = String.format(getString(R.string.movie_length), mContent1) ;
                if(mIcon1 == R.drawable.ic_calendar) {
                     message = String.format(getString(R.string.movie_released_data), mContent1);
                }
                showSnackbar(message);
                break;
            }
            case R.id.lne2: {
                String message = String.format(getString(R.string.movie_language), mContent2);
                if(mIcon2 == R.drawable.ic_star) {
                    message = String.format(getString(R.string.movie_vote_count), mContent2);
                }
                showSnackbar(message);
                break;
            }
            case R.id.lne3: {
                String message = String.format(getString(R.string.movie_vote_adult), mContent3);

                if(mIcon3 == R.drawable.ic_cash) {
                    message = String.format(getString(R.string.movie_vote_budget), mContent3);
                }
                showSnackbar(message);
                break;
            }
        }

    }

    private void showSnackbar(String message) {
        final Snackbar snackbar = Snackbar
                .make(mBinding.getRoot(), message, Snackbar.LENGTH_LONG)
                .setAction("OK", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                    }
                });

        snackbar.setActionTextColor(ContextCompat.getColor(getActivity(), R.color.colorAccent));

        View sbView = snackbar.getView();
        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);

        textView.setTextColor(ContextCompat.getColor(getActivity(), R.color.light_yellow));
        snackbar.show();
    }
}
