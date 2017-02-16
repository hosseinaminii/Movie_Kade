package com.aminiam.moviekade.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.databinding.DataBindingUtil;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.aminiam.moviekade.R;
import com.aminiam.moviekade.databinding.ActivityMovieDetailBinding;
import com.aminiam.moviekade.fragment.AllReviewFragment;
import com.aminiam.moviekade.fragment.MovieDetailFragment;
import com.aminiam.moviekade.other.AllReviewsListener;
import com.aminiam.moviekade.other.UiUpdaterListener;
import com.aminiam.moviekade.utility.NetworkUtility;

import static com.aminiam.moviekade.fragment.MovieDetailFragment.ALL_REVIEWS_KEY;

public class MovieDetailActivity extends AppCompatActivity implements AllReviewsListener,
        UiUpdaterListener, View.OnClickListener {
    private static final String LOG_TAG = MovieDetailActivity.class.getSimpleName();

    private static String TAG_DETAIL = "detail";
    private static String TAG_REVIEW = "review";
    private String currentTag;

    private ActivityMovieDetailBinding mBinding;
    private NetworkReceiver mNetworkReceiver;
    private IntentFilter mNetworkIntentFilter;

    private long mMovieId;
    private String mMovieTitle;
    private String mPoster;
    private String mBackdrop;
    private Toast mToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_movie_detail);

        if (savedInstanceState == null) {
            Bundle bundle = getIntent().getExtras();
            mMovieId = bundle.getLong(MainActivity.MOVIE_ID_KEY);
            mMovieTitle = bundle.getString(MainActivity.MOVIE_TITLE_KEY);
            mPoster = bundle.getString(MainActivity.MOVIE_POSTER_KEY);
            mBackdrop = bundle.getString(MainActivity.MOVIE_BACKDROP_KEY);

            Bundle args = new Bundle();
            args.putLong(MainActivity.MOVIE_ID_KEY, mMovieId);
            args.putString(MainActivity.MOVIE_TITLE_KEY, mMovieTitle);
            args.putString(MainActivity.MOVIE_POSTER_KEY, mPoster);
            args.putString(MainActivity.MOVIE_BACKDROP_KEY, mBackdrop);
            MovieDetailFragment movieDetailFragment = new MovieDetailFragment();
            movieDetailFragment.setArguments(args);

            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container,
                    movieDetailFragment, TAG_DETAIL).commit();
        }

        mNetworkReceiver = new NetworkReceiver();
        mNetworkIntentFilter = new IntentFilter();
        mNetworkIntentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);

        mBinding.btnTryAgain.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(mNetworkReceiver, mNetworkIntentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mNetworkReceiver);
    }

    @Override
    public void onReadMoreClick(String[][] reviews) {

//        AllReviewFragment allReviewFragment = new AllReviewFragment();
//        Bundle bundle = new Bundle();
//        bundle.putSerializable(ALL_REVIEWS_KEY, reviews);
//        allReviewFragment.setArguments(bundle);
//        FragmentTransaction transition = getSupportFragmentManager().beginTransaction();
//        transition.replace(R.id.fragment_container, allReviewFragment, TAG_REVIEW);
//        transition.addToBackStack(null);
//        transition.commit();
        Intent intent = new Intent(this, AllReviewActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(ALL_REVIEWS_KEY, reviews);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private void showError(String errMessage) {
        int iconResource = R.drawable.ic_alert;
        if (errMessage.equals(getString(R.string.error_message_internet))) {
            iconResource = R.drawable.ic_wifi_off;
        }
//        mBinding.appBarMain.loadingIndicator.setVisibility(View.INVISIBLE);
        mBinding.fragmentContainer.setVisibility(View.INVISIBLE);
        mBinding.lneError.setVisibility(View.VISIBLE);
//
        mBinding.txtError.setText(errMessage);
        mBinding.imgErrorIcon.setImageResource(iconResource);
    }

    @Override
    public void error(String errorMessage) {
        showError(errorMessage);
    }

    @Override
    public void updateViews(boolean isLoading) {
        mBinding.lneError.setVisibility(View.INVISIBLE);
        mBinding.fragmentContainer.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnTryAgain: {
                if (NetworkUtility.isNetworkAvailable(this)) {
                    Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
                    if (!(currentFragment instanceof AllReviewFragment)) {
                        ((MovieDetailFragment) currentFragment).initLoader();
                    }
                } else {
                    showToast(getString(R.string.error_message_internet));
                }
                break;
            }
        }
    }

    private class NetworkReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            if (NetworkUtility.isNetworkAvailable(context)) {
                Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
                if (!(currentFragment instanceof AllReviewFragment)) {
                    mBinding.lneError.setVisibility(View.INVISIBLE);
                    mBinding.fragmentContainer.setVisibility(View.VISIBLE);
                    ((MovieDetailFragment) currentFragment).initLoader();
                }
            } else {
                Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
                if (!(currentFragment instanceof AllReviewFragment)) {
                    showError(getString(R.string.error_message_internet));
                }
            }
        }
    }

    private void showToast(String message) {
        if (mToast != null) {
            mToast.cancel();
        }
        mToast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
        mToast.show();
    }
}
