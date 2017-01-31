package com.aminiam.moviekade.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.aminiam.moviekade.R;
import com.aminiam.moviekade.fragment.MovieDetailFragment;

public class MovieDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        if(savedInstanceState == null) {
            MovieDetailFragment movieDetailFragment = new MovieDetailFragment();

            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container,
                    movieDetailFragment).commit();
        }

    }
}
