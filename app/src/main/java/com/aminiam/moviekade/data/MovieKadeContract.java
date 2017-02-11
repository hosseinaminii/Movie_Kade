package com.aminiam.moviekade.data;

import android.provider.BaseColumns;

public final class MovieKadeContract {

    private MovieKadeContract() {}

    public static class FavMovies implements BaseColumns {
        public static final String TABLE_NAME = "fav_movies";
        public static final String COLUMN_MOVIE_ID = "movie_id";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_AVERAGE_VOTE = "average_vote";
        public static final String COLUMN_POSTER = "poster";
        public static final String COLUMN_BAKC_DROP = "back_drop";
    }
}
