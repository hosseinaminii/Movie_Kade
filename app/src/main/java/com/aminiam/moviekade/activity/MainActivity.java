package com.aminiam.moviekade.activity;

import android.os.Bundle;
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
import com.aminiam.moviekade.fragment.PopularFragment;
import com.aminiam.moviekade.fragment.TopRateFragment;
import com.aminiam.moviekade.fragment.UpcommingFragment;

import static com.aminiam.moviekade.R.id.navigationView;

public class MainActivity extends AppCompatActivity {

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
                        break;
                    } case R.id.navPopular: {
                        mNavItemIndex = 1;
                        mCurrentTag = TAG_POPULAR;
                        break;
                    } case R.id.navTopRate: {
                        mNavItemIndex = 2;
                        mCurrentTag = TAG_TOP_RATED;
                        break;
                    }case R.id.navUpComing: {
                        mNavItemIndex = 3;
                        mCurrentTag = TAG_UP_COMING;
                        break;
                    } case R.id.navBookmark: {
                        mNavItemIndex = 4;
                        mCurrentTag = TAG_BOOKMARK;
                        break;
                    } default: {
                        mNavItemIndex = 0;
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

        Fragment currentFragment = getCorrespondingFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
        fragmentTransaction.replace(R.id.frmContent, currentFragment, mCurrentTag);
        fragmentTransaction.commit();

        mDrawerLayout.closeDrawers();
        invalidateOptionsMenu();
    }

    private Fragment getCorrespondingFragment() {
        switch (mNavItemIndex) {
            case 0: {
                return new PlayingFragment();
            }   case 1: {
                return new PopularFragment();
            } case 2: {
                return new TopRateFragment();
            }case 3: {
                return new UpcommingFragment();
            } case 4: {
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


}
