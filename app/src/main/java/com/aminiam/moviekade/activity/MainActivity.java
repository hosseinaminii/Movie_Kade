package com.aminiam.moviekade.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.aminiam.moviekade.R;
import com.aminiam.moviekade.fragment.BookmarkFragment;
import com.aminiam.moviekade.fragment.PlayingFragment;
import com.aminiam.moviekade.utility.NetworkUtility;

import static com.aminiam.moviekade.R.id.navigationView;

public class MainActivity extends AppCompatActivity {

    public static final String PATH_KEY = "path_key";
    public static final String LOADER_ID_KEY = "loader_id_key";

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

    private NavigationView mNavigationView;
    private DrawerLayout mDrawerLayout;
    private Toolbar mToolbar;

    private String[] titles;
    private static int mNavItemIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(savedInstanceState != null) {
            mCurrentTag = savedInstanceState.getString(BUNDLE_KEY);
        }

        setContentView(R.layout.activity_main);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        mNavigationView = (NavigationView) findViewById(R.id.navigationView);

        setSupportActionBar(mToolbar);

        titles = getResources().getStringArray(R.array.titles);

        mNavigationView = (NavigationView) findViewById(navigationView);

        initNav();
        loadFragment();
    }

    private void initNav() {
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navPlaying: {
                        mNavItemIndex = 0;
                        mCurrentTag = TAG_NOW_PLAYING;
                        mLoaderId = PLAYING_FRAGMENT_LOADER_ID;
                        mPath = NetworkUtility.NOW_PLAYING_PATH;
                        break;
                    } case R.id.navPopular: {
                        mNavItemIndex = 1;
                        mCurrentTag = TAG_POPULAR;
                        mLoaderId = POPULAR_FRAGMENT_LOADER_ID;
                        mPath = NetworkUtility.POPULAR_PATH;
                        break;
                    } case R.id.navTopRate: {
                        mNavItemIndex = 2;
                        mCurrentTag = TAG_TOP_RATED;
                        mLoaderId = TOP_RATE_FRAGMENT_LOADER_ID;
                        mPath = NetworkUtility.TOP_RATE_PATH;
                        break;
                    }case R.id.navUpComing: {
                        mNavItemIndex = 3;
                        mCurrentTag = TAG_UP_COMING;
                        mLoaderId = UPCOMMING_FRAGMENT_LOADER_ID;
                        mPath = NetworkUtility.UPCOMING_PATH;
                        break;
                    } case R.id.navBookmark: {
                        mNavItemIndex = 4;
                        mCurrentTag = TAG_BOOKMARK;
                        break;
                    } case R.id.navAbout: {
                        startActivity(new Intent(MainActivity.this, AboutActivity.class));
                        mDrawerLayout.closeDrawers();
                        return true;
                    } case R.id.navSetting: {
                        startActivity(new Intent(MainActivity.this, SettingActivity.class));
                        mDrawerLayout.closeDrawers();
                        return true;
                    } default: {
                        mNavItemIndex = 0;
                        mCurrentTag = TAG_NOW_PLAYING;
                        mLoaderId = PLAYING_FRAGMENT_LOADER_ID;
                    }
                }
                if(item.isChecked()) {
                    item.setCheckable(false);
                } else {
                    item.setChecked(true);
                }
                item.setCheckable(true);
                loadFragment();

                return true;
            }
        });

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };
        mDrawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
    }

    private void loadFragment() {
        mNavigationView.getMenu().getItem(mNavItemIndex).setChecked(true);
        getSupportActionBar().setTitle(titles[mNavItemIndex]);

        if(getSupportFragmentManager().findFragmentByTag(mCurrentTag) != null) {
            mDrawerLayout.closeDrawers();
            return;
        }

        Fragment fragment = getCorrespondingFragment();
//        PlayingFragment fragment = new PlayingFragment();
        Bundle args = new Bundle();
        args.putString(PATH_KEY, mPath);
        args.putInt(LOADER_ID_KEY, mLoaderId);
        fragment.setArguments(args);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
        fragmentTransaction.replace(R.id.frmContent, fragment, mCurrentTag);
        fragmentTransaction.commit();

        mDrawerLayout.closeDrawers();
        invalidateOptionsMenu();
    }

    private Fragment getCorrespondingFragment() {
        switch (mNavItemIndex) {
            case 4: {
                return new BookmarkFragment();
            } default: {
                return new PlayingFragment();
            }
        }
    }

    @Override
    public void onBackPressed() {
        if(mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawers();
            return;
        }
        super.onBackPressed();
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putString(BUNDLE_KEY, mCurrentTag);
    }
}
