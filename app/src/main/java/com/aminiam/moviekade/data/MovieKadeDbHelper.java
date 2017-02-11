package com.aminiam.moviekade.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.aminiam.moviekade.data.MovieKadeContract.FavMovies;

public class MovieKadeDbHelper extends SQLiteOpenHelper{

    private static final String LOG_TAG = MovieKadeDbHelper.class.getSimpleName();

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "MovieKade.db";

    public MovieKadeDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_TABLE = "CREATE TABLE " + FavMovies.TABLE_NAME + "(" +
                FavMovies._ID + " INTEGER PRIMARY KEY," +
                FavMovies.COLUMN_MOVIE_ID + " INTEGER NOT NULL UNIQUE," +
                FavMovies.COLUMN_TITLE + " TEXT NOT NULL," +
                FavMovies.COLUMN_AVERAGE_VOTE + " REAL NOT NULL," +
                FavMovies.COLUMN_POSTER + " TEXT NOT NULL," +
                FavMovies.COLUMN_BAKC_DROP + " TEXT NOT NULL)";

        db.execSQL(SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        final String SQL_DELETE_TABLE = "DROP TABLE IF EXISTS " + FavMovies.TABLE_NAME;
        db.execSQL(SQL_DELETE_TABLE);
        onCreate(db);
    }
}
