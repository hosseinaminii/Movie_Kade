package com.aminiam.moviekade.fragment;


import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aminiam.moviekade.R;
import com.aminiam.moviekade.databinding.FragmentMovieDetailBinding;

public class MovieDetailFragment extends Fragment {
    private static final String LOG_TAG = MovieDetailFragment.class.getSimpleName();

    public static final String MORE_DATA_ICON_KEY = "more_data_icon_key";
    public static final String MORE_DATA_CONTENT_KEY = "more_data_content_key";

    private static final int NUM_PAGES = 2;

    private PagerAdapter mPagerAdapter;
    private String[] mDataContnets = new String[6];

    private FragmentMovieDetailBinding mBinding;

    public MovieDetailFragment() {setHasOptionsMenu(true);}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentMovieDetailBinding.inflate(inflater, container, false);
        setupToolbar("This is Title");

        mPagerAdapter = new MoreDataAdapter(getActivity().getSupportFragmentManager());
        mBinding.moreDataPager.setAdapter(mPagerAdapter);

        mDataContnets[0] = "1:45";
        mDataContnets[1] = "en";
        mDataContnets[2] = "Adoult";
        mDataContnets[3] = "2017-06-10";
        mDataContnets[4] = "www.website.com";
        mDataContnets[5] = "15000000";

        return mBinding.getRoot();
    }

    private void setupToolbar(final String title) {
        ((AppCompatActivity)getActivity()).setSupportActionBar(mBinding.toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mBinding.collapsingToolbar.setTitle(" ");


        mBinding.appBar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {

                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset <= scrollRange / 2) {
                    mBinding.collapsingToolbar.setTitle(title);
                    isShow = true;
                } else if (isShow) {
                    mBinding.collapsingToolbar.setTitle(" ");//carefull there should a space between double quote otherwise it wont work
                    isShow = false;
                }
            }
        });
    }


    private class MoreDataAdapter extends FragmentStatePagerAdapter {

        public MoreDataAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            int icon1 = R.drawable.ic_clock;
            int icon2 = R.drawable.ic_translate;
            int icon3 = R.drawable.ic_block;
            String content1 = mDataContnets[0];
            String content2 = mDataContnets[1];
            String content3 = mDataContnets[2];
            if(position == 1) {
                icon1 = R.drawable.ic_calendar;
                icon2 = R.drawable.ic_web;
                icon3 = R.drawable.ic_cash;
                content1 = mDataContnets[3];
                content2 = mDataContnets[4];
                content3 = mDataContnets[5];
            }

            int[] icons = {icon1, icon2, icon3};
            String[] contents = {content1, content2, content3};
            MoreDataFragment moreDataFragment = new MoreDataFragment();
            Bundle args = new Bundle();
            args.putIntArray(MORE_DATA_ICON_KEY, icons);
            args.putStringArray(MORE_DATA_CONTENT_KEY, contents);
            moreDataFragment.setArguments(args);

            return moreDataFragment;
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }
}
