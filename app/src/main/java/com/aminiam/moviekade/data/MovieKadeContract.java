package com.aminiam.moviekade.data;

import android.net.Uri;
import android.provider.BaseColumns;

public final class MovieKadeContract {

    public static final String AUTHORITY = "com.aminiam.moviekade.provider";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    public static final String PATH_FAV = "fav";

    private MovieKadeContract() {}

    public static class FavMovies implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_FAV).build();

        public static final String TABLE_NAME = "fav_movies";
        public static final String COLUMN_MOVIE_ID = "movie_id";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_AVERAGE_VOTE = "average_vote";
        public static final String COLUMN_POSTER = "poster";
        public static final String COLUMN_BAKC_DROP = "back_drop";
    }
}
