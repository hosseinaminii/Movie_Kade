package com.aminiam.moviekade.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aminiam.moviekade.databinding.FragmentMoreDataBinding;

public class MoreDataFragment extends Fragment {

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
                             Bundle savedInstanceState) {
        mBinding = FragmentMoreDataBinding.inflate(inflater, container, false);

        mBinding.icon1.setImageResource(mIcon1);
        mBinding.icon2.setImageResource(mIcon2);
        mBinding.icon3.setImageResource(mIcon3);
        mBinding.txt1.setText(mContent1);
        mBinding.txt2.setText(mContent2);
        mBinding.txt3.setText(mContent3);

        return mBinding.getRoot();
    }

}
