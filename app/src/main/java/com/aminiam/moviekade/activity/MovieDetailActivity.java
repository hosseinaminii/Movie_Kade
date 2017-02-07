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
            Bundle bundle = getIntent().getExtras();
            long movieId = bundle.getLong(MainActivity.MOVIE_ID_KEY);
            String movieTitle = bundle.getString(MainActivity.MOVIE_TITLE_KEY);
            String poster = bundle.getString(MainActivity.MOVIE_POSTER_KEY);
            String backdrop = bundle.getString(MainActivity.MOVIE_BACKDROP_KEY);

            Bundle args = new Bundle();
            args.putLong(MainActivity.MOVIE_ID_KEY, movieId);
            args.putString(MainActivity.MOVIE_TITLE_KEY, movieTitle);
            args.putString(MainActivity.MOVIE_POSTER_KEY, poster);
            args.putString(MainActivity.MOVIE_BACKDROP_KEY, backdrop);
            MovieDetailFragment movieDetailFragment = new MovieDetailFragment();
            movieDetailFragment.setArguments(args);

            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container,
                    movieDetailFragment).commit();
        }

    }
}
