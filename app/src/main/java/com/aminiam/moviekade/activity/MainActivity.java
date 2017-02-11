package com.aminiam.moviekade.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.databinding.DataBindingUtil;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.aminiam.moviekade.R;
import com.aminiam.moviekade.databinding.ActivityMainBinding;
import com.aminiam.moviekade.fragment.BookmarkFragment;
import com.aminiam.moviekade.fragment.MovieFragment;
import com.aminiam.moviekade.other.Callback;
import com.aminiam.moviekade.other.UiUpdaterListener;
import com.aminiam.moviekade.utility.NetworkUtility;

public class MainActivity extends AppCompatActivity implements UiUpdaterListener, View.OnClickListener, Callback {
    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    public static final String PATH_KEY = "path_key";
    public static final String LOADER_ID_KEY = "loader_id_key";
    public static final String MOVIE_ID_KEY = "movie_id_key";
    public static final String MOVIE_TITLE_KEY = "movie_title_key";
    public static final String MOVIE_POSTER_KEY = "movie_poster_key";
    public static final String MOVIE_BACKDROP_KEY = "movie_backdrop_key";

    private static final int PLAYING_FRAGMENT_LOADER_ID = 9000;
    private static final int POPULAR_FRAGMENT_LOADER_ID = 9001;
    private static final int TOP_RATE_FRAGMENT_LOADER_ID = 9002;
    private static final int UPCOMMING_FRAGMENT_LOADER_ID = 9003;
    private int mLoaderId = PLAYING_FRAGMENT_LOADER_ID;
    private String mPath = NetworkUtility.NOW_PLAYING_PATH;

    private static final String BUNDLE_KEY = "bundle_key";
    private static final String TAG_NOW_PLAYING = "now_playing";
    private static final String TAG_POPULAR = "popular";
    private static final String TAG_TOP_RATED = "top_rated";
    private static final String TAG_UP_COMING = "up_coming";
    private static final String TAG_BOOKMARK = "bookmark";
    private String mCurrentTag = TAG_NOW_PLAYING;

    private ActivityMainBinding mBinding;
    private NetworkReceiver mNetworkReceiver;
    private IntentFilter mNetworkIntentFilter;
    private Toast mToast;

    private String[] titles;
    private static int mNavItemIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            mCurrentTag = savedInstanceState.getString(BUNDLE_KEY);
        }
        setContentView(R.layout.activity_main);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        setSupportActionBar(mBinding.appBarMain.toolbar);

        titles = getResources().getStringArray(R.array.titles);
        mNetworkIntentFilter = new IntentFilter();
        mNetworkIntentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        mNetworkReceiver = new NetworkReceiver();

        mBinding.appBarMain.btnTryAgain.setOnClickListener(this);

        initNav();
        loadFragment();
    }

    @Override
    public void onResume() {
        super.onResume();
        registerReceiver(mNetworkReceiver, mNetworkIntentFilter);
    }

    @Override
    public void onPause() {
        super.onPause();
        unregisterReceiver(mNetworkReceiver);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnTryAgain: {
                if (NetworkUtility.isNetworkAvailable(this)) {
                    Fragment currentFragment = getSupportFragmentManager().findFragmentByTag(mCurrentTag);
                    if (!mCurrentTag.equals(TAG_BOOKMARK)) {
                        if (currentFragment != null) {
                            MovieFragment movieFragment = (MovieFragment) currentFragment;
                            movieFragment.initLoader();
                        }
                    }
                } else {
                    showToast(getString(R.string.error_message_internet));
                }
                break;
            }
        }
    }

    private void initNav() {
        mBinding.navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navPlaying: {
                        mNavItemIndex = 0;
                        mCurrentTag = TAG_NOW_PLAYING;
                        mLoaderId = PLAYING_FRAGMENT_LOADER_ID;
                        mPath = NetworkUtility.NOW_PLAYING_PATH;
                        break;
                    }
                    case R.id.navPopular: {
                        mNavItemIndex = 1;
                        mCurrentTag = TAG_POPULAR;
                        mLoaderId = POPULAR_FRAGMENT_LOADER_ID;
                        mPath = NetworkUtility.POPULAR_PATH;
                        break;
                    }
                    case R.id.navTopRate: {
                        mNavItemIndex = 2;
                        mCurrentTag = TAG_TOP_RATED;
                        mLoaderId = TOP_RATE_FRAGMENT_LOADER_ID;
                        mPath = NetworkUtility.TOP_RATE_PATH;
                        break;
                    }
                    case R.id.navUpComing: {
                        mNavItemIndex = 3;
                        mCurrentTag = TAG_UP_COMING;
                        mLoaderId = UPCOMMING_FRAGMENT_LOADER_ID;
                        mPath = NetworkUtility.UPCOMING_PATH;
                        break;
                    }
                    case R.id.navBookmark: {
                        mNavItemIndex = 4;
                        mCurrentTag = TAG_BOOKMARK;
                        break;
                    }
                    case R.id.navAbout: {
                        startActivity(new Intent(MainActivity.this, AboutActivity.class));
                        mBinding.drawerLayout.closeDrawers();
                        return true;
                    }
                    case R.id.navSetting: {
                        startActivity(new Intent(MainActivity.this, SettingActivity.class));
                        mBinding.drawerLayout.closeDrawers();
                        return true;
                    }
                    default: {
                        mNavItemIndex = 0;
                        mCurrentTag = TAG_NOW_PLAYING;
                        mLoaderId = PLAYING_FRAGMENT_LOADER_ID;
                    }
                }
                if (item.isChecked()) {
                    item.setCheckable(false);
                } else {
                    item.setChecked(true);
                }
                item.setCheckable(true);
                loadFragment();

                return true;
            }
        });

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, mBinding.drawerLayout,
                mBinding.appBarMain.toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };
        mBinding.drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
    }

    private void loadFragment() {
        mBinding.navigationView.getMenu().getItem(mNavItemIndex).setChecked(true);
        getSupportActionBar().setTitle(titles[mNavItemIndex]);

        if (getSupportFragmentManager().findFragmentByTag(mCurrentTag) != null) {
            mBinding.drawerLayout.closeDrawers();
            return;
        }

        Fragment fragment = getCorrespondingFragment();
//        MovieFragment fragment = new MovieFragment();
        Bundle args = new Bundle();
        args.putString(PATH_KEY, mPath);
        args.putInt(LOADER_ID_KEY, mLoaderId);
        fragment.setArguments(args);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
        fragmentTransaction.replace(R.id.frmContent, fragment, mCurrentTag);
        fragmentTransaction.commit();

        mBinding.drawerLayout.closeDrawers();
        invalidateOptionsMenu();
    }

    private Fragment getCorrespondingFragment() {
        switch (mNavItemIndex) {
            case 4: {
                return new BookmarkFragment();
            }
            default: {
                return new MovieFragment();
            }
        }
    }


    private void showError(String errMessage) {
        int iconResource = R.drawable.ic_alert;
        if (errMessage.equals(getString(R.string.error_message_internet))) {
            iconResource = R.drawable.ic_wifi_off;
        }
        mBinding.appBarMain.loadingIndicator.setVisibility(View.INVISIBLE);
        mBinding.appBarMain.frmContent.setVisibility(View.INVISIBLE);
        mBinding.appBarMain.lneError.setVisibility(View.VISIBLE);

        mBinding.appBarMain.txtError.setText(errMessage);
        mBinding.appBarMain.imgErrorIcon.setImageResource(iconResource);
    }

    private void showToast(String message) {
        if (mToast != null) {
            mToast.cancel();
        }
        mToast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
        mToast.show();
    }

    @Override
    public void onBackPressed() {
        if (mBinding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            mBinding.drawerLayout.closeDrawers();
            return;
        }
        mNavItemIndex = 0;
        super.onBackPressed();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(BUNDLE_KEY, mCurrentTag);
    }

    @Override
    public void error(String errorMessage) {
        showError(errorMessage);
    }

    @Override
    public void updateViews(boolean isLoading) {
        if (isLoading) {
            mBinding.appBarMain.loadingIndicator.setVisibility(View.VISIBLE);
        } else {
            mBinding.appBarMain.loadingIndicator.setVisibility(View.INVISIBLE);
            if (mBinding.appBarMain.frmContent.getVisibility() == View.INVISIBLE) {
                mBinding.appBarMain.frmContent.setVisibility(View.VISIBLE);
            }
        }
        mBinding.appBarMain.lneError.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onItemSelected(long movieId, String movieTitle, String posterPath, String backdropPath) {
        Intent intent = new Intent(this, MovieDetailActivity.class);
        intent.putExtra(MOVIE_ID_KEY, movieId);
        intent.putExtra(MOVIE_TITLE_KEY, movieTitle);
        intent.putExtra(MOVIE_POSTER_KEY, posterPath);
        intent.putExtra(MOVIE_BACKDROP_KEY, backdropPath);
        startActivity(intent);
    }

    private class NetworkReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            if (NetworkUtility.isNetworkAvailable(context)) {
                Fragment currentFragment = getSupportFragmentManager().findFragmentByTag(mCurrentTag);
                if (!mCurrentTag.equals(TAG_BOOKMARK)) {
                    if (currentFragment != null) {
                        MovieFragment movieFragment = (MovieFragment) currentFragment;
                        movieFragment.initLoader();
                    }
                }

            } else {
                showError(getString(R.string.error_message_internet));
            }
        }
    }

}
