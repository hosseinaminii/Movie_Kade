package com.aminiam.moviekade.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aminiam.moviekade.R;
import com.aminiam.moviekade.activity.MainActivity;
import com.aminiam.moviekade.databinding.FragmentMovieDetailBinding;
import com.aminiam.moviekade.other.MovieInformationStructure;
import com.aminiam.moviekade.utility.JsonUtility;
import com.aminiam.moviekade.utility.NetworkUtility;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;

public class MovieDetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<String> {
    private static final String LOG_TAG = MovieDetailFragment.class.getSimpleName();

    private static final int MODVIE_DETAIL_LOADER_ID = 9999;
    public static final String MORE_DATA_ICON_KEY = "more_data_icon_key";
    public static final String MORE_DATA_CONTENT_KEY = "more_data_content_key";
    public static final String REVIEW_KEY = "review_key";

    private long mMovieId = -1;
    private static final int NUM_PAGES = 2;

    private String[] mDataContents = new String[6];
    private String[] mReviews = new String[10];

    private FragmentMovieDetailBinding mBinding;

    public MovieDetailFragment() {
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        mMovieId = args.getLong(MainActivity.MOVIE_ID_KEY);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentMovieDetailBinding.inflate(inflater, container, false);
        setupToolbar("This is Title");

        return mBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(LOG_TAG, "onActivityCreated");
        getActivity().getSupportLoaderManager().initLoader(MODVIE_DETAIL_LOADER_ID, null, this);
    }

    private void setupToolbar(final String title) {
        ((AppCompatActivity) getActivity()).setSupportActionBar(mBinding.toolbar);
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

    @Override
    public Loader<String> onCreateLoader(int id, Bundle args) {
        Log.d(LOG_TAG, "onCreateLoader");
        return new AsyncTaskLoader<String>(getActivity()) {
            @Override
            protected void onStartLoading() {
                super.onStartLoading();
                forceLoad();
            }

            @Override
            public String loadInBackground() {
                if(mMovieId == -1) {
                    return null;
                }
                URL url = NetworkUtility.getMovieDataUrl(mMovieId);
                try {
                    return NetworkUtility.getResponseFromHttpUrl(url);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<String> loader, String data) {
        if(data == null) {
            return;
        }
        try {
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

            String[] dataParams = new String[6];

            MovieInformationStructure movieInformationStructure =
                    JsonUtility.getMovieInformationFromJson(getActivity(), data);

            String title = movieInformationStructure.title;
            String status = movieInformationStructure.status;
            String overview = movieInformationStructure.overview;
            String runtime = movieInformationStructure.runtime;
            String language = movieInformationStructure.language;
            boolean adult = movieInformationStructure.adult;
            String releaseDate = movieInformationStructure.releaseDate;
            String website = movieInformationStructure.website;
            long revenue = movieInformationStructure.revenue;

            mBinding.txtTitle.setText(title);
            mBinding.txtStatus.setText(status);
            mBinding.expOverview.setContent(overview);
            dataParams[0] = runtime;
            dataParams[1] = language;
            dataParams[2] = adult ? "YES" : "NO";
            dataParams[3] = releaseDate;
            dataParams[4] = website;
            dataParams[5] = String.valueOf(revenue);

            PagerAdapter mMoreDataPagerAdapter =
                    new MoreDataAdapter(getActivity().getSupportFragmentManager(), dataParams);
            PagerAdapter mReviewPagerAdapter = new ReviewAdapter(getActivity().getSupportFragmentManager());

            mBinding.moreDataPager.setAdapter(mMoreDataPagerAdapter);
            mBinding.reviewPager.setAdapter(mReviewPagerAdapter);
            mBinding.txtReviewPageNumber.setText(String.format(getString(R.string.review_page_number), 1, mReviews.length));

            mBinding.moreDataPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                }

                @Override
                public void onPageSelected(int position) {
                    mBinding.moreDateIndicator.setActiveDot(position);
                }

                @Override
                public void onPageScrollStateChanged(int state) {
                }
            });
            mBinding.reviewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                }

                @Override
                public void onPageSelected(int position) {
                    mBinding.txtReviewPageNumber.setText(String.format(getString(R.string.review_page_number), position + 1, mReviews.length));
                }

                @Override
                public void onPageScrollStateChanged(int state) {
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onLoaderReset(Loader<String> loader) {}

    private class MoreDataAdapter extends FragmentStatePagerAdapter {

        String[] mParams = new String[6];

        public MoreDataAdapter(FragmentManager fm, String[] params) {
            super(fm);
            mParams = params;
        }

        @Override
        public Fragment getItem(int position) {
            int icon1 = R.drawable.ic_clock;
            int icon2 = R.drawable.ic_translate;
            int icon3 = R.drawable.ic_block;
            String content1 = mParams[0];
            String content2 = mParams[1];
            String content3 = mParams[2];
            if (position == 1) {
                icon1 = R.drawable.ic_calendar;
                icon2 = R.drawable.ic_web;
                icon3 = R.drawable.ic_cash;
                content1 = mParams[3];
                content2 = mParams[4];
                content3 = mParams[5];
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

        public ReviewAdapter(FragmentManager fm) {
            super(fm);
        }

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
