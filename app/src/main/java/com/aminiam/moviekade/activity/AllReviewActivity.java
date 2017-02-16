package com.aminiam.moviekade.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.aminiam.moviekade.R;
import com.aminiam.moviekade.fragment.AllReviewFragment;
import com.aminiam.moviekade.fragment.MovieDetailFragment;

public class AllReviewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_review);

        if(savedInstanceState == null) {
            Bundle bundle = getIntent().getExtras();
            String[][] allReviews = (String[][]) bundle.getSerializable(MovieDetailFragment.ALL_REVIEWS_KEY);

            AllReviewFragment allReviewFragment = new AllReviewFragment();
            Bundle args = new Bundle();
            args.putSerializable(MovieDetailFragment.ALL_REVIEWS_KEY, allReviews);
            allReviewFragment.setArguments(args);

            getSupportFragmentManager().beginTransaction().add(R.id.frmContent, allReviewFragment)
                    .commit();
        }
    }
}
