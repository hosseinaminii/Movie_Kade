package com.aminiam.moviekade.fragment;


import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
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
    public static final String REVIEW_KEY = "review_key";

    private static final int NUM_PAGES = 2;

    private String[] mDataContents = new String[6];
    private String[] mReviews = new String[10];

    private FragmentMovieDetailBinding mBinding;

    public MovieDetailFragment() {setHasOptionsMenu(true);}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentMovieDetailBinding.inflate(inflater, container, false);
        setupToolbar("This is Title");

        PagerAdapter mMoreDataPagerAdapter = new MoreDataAdapter(getActivity().getSupportFragmentManager());
        PagerAdapter mReviewPagerAdapter = new ReviewAdapter(getActivity().getSupportFragmentManager());

        mBinding.moreDataPager.setAdapter(mMoreDataPagerAdapter);
        mBinding.reviewPager.setAdapter(mReviewPagerAdapter);

        mDataContents[0] = "1:45";
        mDataContents[1] = "en";
        mDataContents[2] = "Adoult";
        mDataContents[3] = "2017-06-10";
        mDataContents[4] = "www.website.com";
        mDataContents[5] = "15000000";
        mReviews[0] = "Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Aenean commodo ligula eget dolor. Aenean massa. Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Donec quam felis, ultricies nec, pellentesque eu, pretium quis, sem.Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Aenean commodo ligula eget dolor. Aenean massa. Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Donec quam felis, ultricies nec, pellentesque eu, pretium quis, sem. Hello ";
        mReviews[1] = "Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Aenean commodo ligula eget dolor. Aenean massa. Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Donec quam felis, ultricies nec, pellentesque eu, pretium quis, sem. ";
        mReviews[2] = "Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Aenean commodo ligula eget dolor. Aenean massa. Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Donec quam felis, ultricies nec, pellentesque eu, pretium quis, sem. ";
        mReviews[3] = "Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Aenean commodo ligula eget dolor. Aenean massa. Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Donec quam felis, ultricies nec, pellentesque eu, pretium quis, sem. ";
        mReviews[4] = "Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Aenean commodo ligula eget dolor. Aenean massa. Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Donec quam felis, ultricies nec, pellentesque eu, pretium quis, sem. ";
        mReviews[5] = "Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Aenean commodo ligula eget dolor. Aenean massa. Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Donec quam felis, ultricies nec, pellentesque eu, pretium quis, sem. ";
        mReviews[6] = "Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Aenean commodo ligula eget dolor. Aenean massa. Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Donec quam felis, ultricies nec, pellentesque eu, pretium quis, sem. ";
        mReviews[7] = "Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Aenean commodo ligula eget dolor. Aenean massa. Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Donec quam felis, ultricies nec, pellentesque eu, pretium quis, sem. ";
        mReviews[8] = "Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Aenean commodo ligula eget dolor. Aenean massa. Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Donec quam felis, ultricies nec, pellentesque eu, pretium quis, sem. ";
        mReviews[9] = "Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Aenean commodo ligula eget dolor. Aenean massa. Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Donec quam felis, ultricies nec, pellentesque eu, pretium quis, sem. ";

        mBinding.txtReviewPageNumber.setText(String.format(getString(R.string.review_page_number), 1, mReviews.length));

        mBinding.moreDataPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            @Override
            public void onPageSelected(int position) {
                mBinding.moreDateIndicator.setActiveDot(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {}
        });
        mBinding.reviewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            @Override
            public void onPageSelected(int position) {
                mBinding.txtReviewPageNumber.setText(String.format(getString(R.string.review_page_number), position + 1, mReviews.length));
            }

            @Override
            public void onPageScrollStateChanged(int state) {}
        });

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
            String content1 = mDataContents[0];
            String content2 = mDataContents[1];
            String content3 = mDataContents[2];
            if(position == 1) {
                icon1 = R.drawable.ic_calendar;
                icon2 = R.drawable.ic_web;
                icon3 = R.drawable.ic_cash;
                content1 = mDataContents[3];
                content2 = mDataContents[4];
                content3 = mDataContents[5];
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

    private class ReviewAdapter extends FragmentStatePagerAdapter {

        public ReviewAdapter(FragmentManager fm) { super(fm); }

        @Override
        public Fragment getItem(int position) {
            Bundle args = new Bundle();
            args.putString(REVIEW_KEY, mReviews[position]);
            ReviewFragment reviewFragment = new ReviewFragment();
            reviewFragment.setArguments(args);
            return reviewFragment;
        }

        @Override
        public int getCount() {
            return mReviews.length;
        }
    }
}
