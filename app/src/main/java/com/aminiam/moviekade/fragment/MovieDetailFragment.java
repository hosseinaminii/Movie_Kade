package com.aminiam.moviekade.fragment;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.NavUtils;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.aminiam.moviekade.R;
import com.aminiam.moviekade.activity.MainActivity;
import com.aminiam.moviekade.adapter.TrailerAdapter;
import com.aminiam.moviekade.data.MovieKadeContract;
import com.aminiam.moviekade.data.MovieKadeContract.FavMovies;
import com.aminiam.moviekade.databinding.FragmentMovieDetailBinding;
import com.aminiam.moviekade.other.AllReviewsListener;
import com.aminiam.moviekade.other.MovieInformationStructure;
import com.aminiam.moviekade.other.UiUpdaterListener;
import com.aminiam.moviekade.utility.JsonUtility;
import com.aminiam.moviekade.utility.NetworkUtility;
import com.aminiam.moviekade.utility.Utility;
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
    private String mMovieTitle;
    private String mMoviePoster;
    private String mMovieBackdrop;
    private float mAverageVote;
    private static final int NUM_PAGES = 2;
    private String[][] mReviews;
    private int mActiveIndicatorNum = 0;
    private Toast mToast;
    private boolean mLandscape;
    private boolean mIsFav;

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
        mMovieTitle = args.getString(MainActivity.MOVIE_TITLE_KEY);
        mMoviePoster = args.getString(MainActivity.MOVIE_POSTER_KEY);
        mMovieBackdrop = args.getString(MainActivity.MOVIE_BACKDROP_KEY);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentMovieDetailBinding.inflate(inflater, container, false);
        setupToolbar(mMovieTitle);

        // Load poster image
        Picasso.with(getActivity()).load(NetworkUtility.buildPosterPath(mMoviePoster))
                .placeholder(R.drawable.thumbnail_plceholder).into(mBinding.imgPoster);
        prepareTrailer();

        if (savedInstanceState != null) {
            mActiveIndicatorNum = savedInstanceState.getInt(DATA_INDICATOR_KEY);
        }

        mLandscape = getResources().getBoolean(R.bool.landscape);
        checkIsFab();
        mBinding.fabFavorite.setOnClickListener(this);
        return mBinding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        mBinding.reviewPager.getLayoutParams().height = Utility.convertDpToPx(getActivity(), 130);
        mBinding.moreDataPager.getLayoutParams().height = Utility.convertDpToPx(getActivity(), 120);
        mBinding.moreDateIndicator.setActiveDot(mActiveIndicatorNum);
        if (!NetworkUtility.isNetworkAvailable(getActivity())) {
            ((UiUpdaterListener) getActivity()).error(getString(R.string.error_message_internet));
        }
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
                if (mLandscape) {
                    if (scrollRange + verticalOffset < 140) {
                        mBinding.fabFavorite.hide();
                    } else {
                        mBinding.fabFavorite.show();
                    }
                }

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

    private void prepareTrailer() {
        mTrailerAdatper = new TrailerAdapter(getActivity(), this, mBinding.lneNoTrailer);
        mBinding.recTrailer.setAdapter(mTrailerAdatper);
        mBinding.recTrailer.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.HORIZONTAL, false));
        mBinding.recTrailer.setAdapter(mTrailerAdatper);
        mBinding.moreDateIndicator.setActiveDot(0);
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
                if (mMovieId == -1) {
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
        if (data == null) {
            ((UiUpdaterListener) getActivity()).error(getString(R.string.error_message_failed));
            showToast(getString(R.string.error_message_failed));
            return;
        }
        try {
            ((UiUpdaterListener) getActivity()).updateViews(false);
            // load toolbar image
            Picasso.with(getActivity()).load(NetworkUtility.buildBackdropPath(mMovieBackdrop))
                    .placeholder(R.drawable.toolbar_plceholder).into(
                    mBinding.imgToolbarImage);

            MovieInformationStructure movieInformationStructure =
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
            mAverageVote = movieInformationStructure.voteAverage;

            mBinding.txtTitle.setText(title);
            mBinding.txtStatus.setText(status);
            mBinding.expOverview.setContent(overview);
            mBinding.txtGenre.setText(genres);
            mBinding.voteAverage.setRating(mAverageVote / 2);

            // Set status Image
            if (!status.equals(getString(R.string.released))) {
                mBinding.imgStatus.setImageResource(R.drawable.ic_circle_alert);
            }

            String[] moreDataParams = new String[6];
            moreDataParams[0] = runtime;
            moreDataParams[1] = language;
            moreDataParams[2] = adult ? "YES" : "NO";
            moreDataParams[3] = releaseDate;
            moreDataParams[4] = String.valueOf(voteCount);
            moreDataParams[5] = String.valueOf(revenue);

            PagerAdapter moreDataPagerAdapter =
                    new MoreDataAdapter(getChildFragmentManager(), moreDataParams);
            PagerAdapter reviewPagerAdapter = new ReviewAdapter(
                    getChildFragmentManager(), mReviews);

<<<<<<< Updated upstream
            mBinding.moreDataPager.setAdapter(moreDataPagerAdapter);
            mBinding.reviewPager.setAdapter(reviewPagerAdapter);

||||||| merged common ancestors
            mBinding.moreDataPager.setAdapter(mMoreDataPagerAdapter);
            mBinding.reviewPager.setAdapter(mReviewPagerAdapter);
            Log.d(LOG_TAG, "Height= " + mBinding.reviewPager.getHeight());
=======
            mBinding.moreDataPager.setAdapter(mMoreDataPagerAdapter);
            mBinding.reviewPager.setAdapter(mReviewPagerAdapter);

            Log.d(LOG_TAG, "Height= " + mBinding.reviewPager.getHeight());

>>>>>>> Stashed changes
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
    public void onLoaderReset(Loader<String> loader) {
    }

    @Override
    public void onTrailerClick(String key) {
        String youtubeBaseUrl = "http://www.youtube.com/watch?v=";
        Intent youtubeIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(youtubeBaseUrl + key));
        Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(youtubeBaseUrl + key));
        if (youtubeIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivity(youtubeIntent);
        } else {
            startActivity(webIntent);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.txtReadMore: {
                ((AllReviewsListener) getActivity()).onReadMoreClick(mReviews);
                break;
            }case R.id.fabFavorite: {
                updateFav();
                break;
            }
        }
    }

    private void updateFav() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {

                if(!mIsFav) {
                    ContentValues values = new ContentValues();
                    values.put(MovieKadeContract.FavMovies.COLUMN_MOVIE_ID, String.valueOf(mMovieId));
                    values.put(MovieKadeContract.FavMovies.COLUMN_TITLE, mMovieTitle);
                    values.put(MovieKadeContract.FavMovies.COLUMN_AVERAGE_VOTE, mAverageVote);
                    values.put(MovieKadeContract.FavMovies.COLUMN_POSTER, mMoviePoster);
                    values.put(MovieKadeContract.FavMovies.COLUMN_BAKC_DROP, mMovieBackdrop);

                    values.put(FavMovies.COLUMN_MOVIE_ID, String.valueOf(mMovieId));
                    getContext().getContentResolver().insert(
                            FavMovies.CONTENT_URI,
                            values);
                    mIsFav = true;
                } else {
                    getContext().getContentResolver().delete(
                            FavMovies.CONTENT_URI,
                            FavMovies.COLUMN_MOVIE_ID + " =? ",
                            new String[] {String .valueOf(mMovieId)});
                    mIsFav = false;
                }

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                updateFabIcon();
            }
        }.execute();
    }

    private void checkIsFab() {

        new AsyncTask<Void, Void, Cursor>(){
            @Override
            protected Cursor doInBackground(Void... params) {

                String selection = FavMovies.COLUMN_MOVIE_ID + " =? ";
                String[] selectionArgs = new String[]{String.valueOf(mMovieId)};

                Cursor cursor = getContext().getContentResolver().query(
                        FavMovies.CONTENT_URI,
                        null,
                        selection,
                        selectionArgs,
                        null);
                if(cursor != null) { mIsFav = cursor.moveToNext(); }

                return cursor;
            }

            @Override
            protected void onPostExecute(Cursor cursor) {
                super.onPostExecute(cursor);
                updateFabIcon();
                if(cursor != null) {
                    cursor.close();
                }
            }
        }.execute();
    }

    private void updateFabIcon() {
        if (mIsFav) {
            mBinding.fabFavorite.setImageResource(R.drawable.ic_favorite_on);
        } else {
            mBinding.fabFavorite.setImageResource(R.drawable.ic_favorite_off);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(getActivity());
        }
        return super.onOptionsItemSelected(item);
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
            if (mReviews.length == 0) {
                mBinding.txtNoReview.setVisibility(View.VISIBLE);
                mBinding.imgReview.setVisibility(View.VISIBLE);
                mBinding.txtReviewPageNumber.setVisibility(View.INVISIBLE);
                mBinding.txtReadMore.setVisibility(View.INVISIBLE);
            }
            return mReviews.length;
        }

    }

    public void initLoader() {
        getActivity().getSupportLoaderManager().initLoader(MOVIE_DETAIL_LOADER_ID, null, this);
    }

    private void showToast(String message) {
        if (mToast != null) {
            mToast.cancel();
        }
        mToast = Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT);
        mToast.show();
    }
}
