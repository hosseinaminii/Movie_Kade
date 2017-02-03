package com.aminiam.moviekade.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.aminiam.moviekade.R;
import com.aminiam.moviekade.fragment.MovieDetailFragment;

public class MovieDetailActivity extends AppCompatActivity {
    private static final String LOG_TAG = MovieDetailActivity.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        if(savedInstanceState == null) {
            long movieId = getIntent().getExtras().getLong(MainActivity.MOVIE_ID_KEY);
            Bundle args = new Bundle();
            args.putLong(MainActivity.MOVIE_ID_KEY, movieId);
            MovieDetailFragment movieDetailFragment = new MovieDetailFragment();
            movieDetailFragment.setArguments(args);

            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container,
                    movieDetailFragment).commit();
        }

    }
}
