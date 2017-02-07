package com.aminiam.moviekade.fragment;

import android.content.Intent;
import android.net.Uri;
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
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aminiam.moviekade.R;
import com.aminiam.moviekade.activity.AllReviewActivity;
import com.aminiam.moviekade.activity.MainActivity;
import com.aminiam.moviekade.adapter.TrailerAdapter;
import com.aminiam.moviekade.databinding.FragmentMovieDetailBinding;
import com.aminiam.moviekade.other.MovieInformationStructure;
import com.aminiam.moviekade.utility.JsonUtility;
import com.aminiam.moviekade.utility.NetworkUtility;
import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;

public class MovieDetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<String>,
        TrailerAdapter.TrailerClickListener, View.OnClickListener {
    private static final String LOG_TAG = MovieDetailFragment.class.getSimpleName();

    public static final String MORE_DATA_ICON_KEY = "more_data_icon_key";
    public static final String MORE_DATA_CONTENT_KEY = "more_data_content_key";
    public static final String REVIEW_CONTENT_KEY = "review_content_key";
    public static final String REVIEW_AUTHOR_KEY = "review_author_key";
    public static final String ALL_REVIEWS_KEY = "all_reviews_key";

    private static final int MOVIE_DETAIL_LOADER_ID = 9999;
    private static final String DATA_INDICATOR_KEY = "data_indicator_key";

    private long mMovieId = -1;
    private String mMoviePoster;
    private String mMovieBackdrop;
    private static final int NUM_PAGES = 2;
    private String[][] mReviews;
    private int mActiveIndicatorNum = 0;

    private FragmentMovieDetailBinding mBinding;
    private TrailerAdapter mTrailerAdatper;

    public MovieDetailFragment() {
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        mMovieId = args.getLong(MainActivity.MOVIE_ID_KEY);
        mMoviePoster = args.getString(MainActivity.MOVIE_POSTER_KEY);
        mMovieBackdrop = args.getString(MainActivity.MOVIE_BACKDROP_KEY);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentMovieDetailBinding.inflate(inflater, container, false);
        setupToolbar("This is Title");

        Picasso.with(getActivity()).load(NetworkUtility.buildBackdropPath(mMovieBackdrop))
                .placeholder(R.drawable.toolbar_plceholder).into(
                mBinding.imgToolbarImage);

        Picasso.with(getActivity()).load(NetworkUtility.buildPosterPath(mMoviePoster))
                .placeholder(R.drawable.thumbnail_plceholder).into(mBinding.imgPoster);

        mTrailerAdatper = new TrailerAdapter(getActivity(),this,  mBinding.lneNoTrailer);
        mBinding.recTrailer.setAdapter(mTrailerAdatper);
        mBinding.recTrailer.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.HORIZONTAL, false));
        mBinding.recTrailer.setAdapter(mTrailerAdatper);
        mBinding.moreDateIndicator.setActiveDot(0);

        if(savedInstanceState != null) {
            mActiveIndicatorNum = savedInstanceState.getInt(DATA_INDICATOR_KEY);
        }

        return mBinding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(LOG_TAG, "Active dot= " + mActiveIndicatorNum);
        mBinding.moreDateIndicator.setActiveDot(mActiveIndicatorNum);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getActivity().getSupportLoaderManager().initLoader(MOVIE_DETAIL_LOADER_ID, null, this);

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
                    mBinding.collapsingToolbar.setTitle(" ");
                    isShow = false;
                }
            }
        });
    }

    @Override
    public Loader<String> onCreateLoader(int id, Bundle args) {
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

            String[] dataParams = new String[6];

            final MovieInformationStructure movieInformationStructure =
                    JsonUtility.getMovieInformationFromJson(getActivity(), data);

            String title = movieInformationStructure.title;
            String status = movieInformationStructure.status;
            String overview = movieInformationStructure.overview;
            String runtime = movieInformationStructure.runtime;
            String language = movieInformationStructure.language;
            boolean adult = movieInformationStructure.adult;
            String releaseDate = movieInformationStructure.releaseDate;
            int voteCount = movieInformationStructure.voteCount;
            long revenue = movieInformationStructure.revenue;
            String genres = movieInformationStructure.genres;
            mReviews = movieInformationStructure.reviews;
            float voteAverage = movieInformationStructure.voteAverage;

            mBinding.txtTitle.setText(title);
            mBinding.txtStatus.setText(status);
            mBinding.expOverview.setContent(overview);
            mBinding.txtGenre.setText(genres);
            mBinding.voteAverage.setRating(voteAverage / 2);
            dataParams[0] = runtime;
            dataParams[1] = language;
            dataParams[2] = adult ? "YES" : "NO";
            dataParams[3] = releaseDate;
            dataParams[4] = String.valueOf(voteCount);
            dataParams[5] = String.valueOf(revenue);

            if(!status.equals(getString(R.string.released))) {
                mBinding.imgStatus.setImageResource(R.drawable.ic_circle_alert);
            }

            PagerAdapter mMoreDataPagerAdapter =
                    new MoreDataAdapter(getActivity().getSupportFragmentManager(), dataParams);
            PagerAdapter mReviewPagerAdapter = new ReviewAdapter(
                    getActivity().getSupportFragmentManager(), mReviews);

            mBinding.moreDataPager.setAdapter(mMoreDataPagerAdapter);
            mBinding.reviewPager.setAdapter(mReviewPagerAdapter);
            mBinding.txtReviewPageNumber.setText(String.format(getString(R.string.review_page_number), 1,
                    mReviews.length));
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
                    mBinding.txtReviewPageNumber.setText(String.format(getString(R.string.review_page_number), position + 1,
                            mReviews.length));
                }

                @Override
                public void onPageScrollStateChanged(int state) {
                }
            });

            mTrailerAdatper.populateData(movieInformationStructure.trailers);
            mBinding.txtReadMore.setOnClickListener(this);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onLoaderReset(Loader<String> loader) {}

    @Override
    public void onTrailerClick(String key) {
        String youtubeBaseUrl = "http://www.youtube.com/watch?v=";
        Intent youtubeIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(youtubeBaseUrl + key));
        Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(youtubeBaseUrl + key));
        if(youtubeIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivity(youtubeIntent);
        } else {
            startActivity(webIntent);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.txtReadMore: {
                Intent intent = new Intent(getActivity(), AllReviewActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable(ALL_REVIEWS_KEY, mReviews);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            }
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        mActiveIndicatorNum = 0;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(DATA_INDICATOR_KEY, mBinding.moreDateIndicator.getActiveDot());
    }

    // Adapter for more data
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
                icon2 = R.drawable.ic_star;
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

    // Adapter for reviews
    private class ReviewAdapter extends FragmentStatePagerAdapter {

        String[][] mReviews;

        public ReviewAdapter(FragmentManager fm, String[][] reviews) {
            super(fm);
            mReviews = reviews;
        }

        @Override
        public Fragment getItem(int position) {
            Bundle args = new Bundle();
            args.putString(REVIEW_AUTHOR_KEY, mReviews[position][0]);
            args.putString(REVIEW_CONTENT_KEY, mReviews[position][1]);
            ReviewFragment reviewFragment = new ReviewFragment();
            reviewFragment.setArguments(args);
            return reviewFragment;
        }

        @Override
        public int getCount() {
            if(mReviews.length == 0) {
                mBinding.txtNoReview.setVisibility(View.VISIBLE);
                mBinding.imgReview.setVisibility(View.VISIBLE);
                mBinding.txtReviewPageNumber.setVisibility(View.INVISIBLE);
                mBinding.txtReadMore.setVisibility(View.INVISIBLE);
            }
            return mReviews.length;
        }

    }
}
