package com.aminiam.moviekade.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;

import com.aminiam.moviekade.data.MovieKadeContract.FavMovies;

public class MovieKadeProvider extends ContentProvider{

    private MovieKadeDbHelper mDbHelpr;

    private static final int FAV_MOVIE = 100;
    private static final int FAV_MOVIE_WITH_ID = 101;

    private static UriMatcher sUriMatcher = buildUriMatcher();

    public static UriMatcher buildUriMatcher() {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(MovieKadeContract.AUTHORITY, MovieKadeContract.PATH_FAV, FAV_MOVIE);
        uriMatcher.addURI(MovieKadeContract.AUTHORITY,
                MovieKadeContract.PATH_FAV + "/#", FAV_MOVIE_WITH_ID);
        return uriMatcher;
    }

    @Override
    public boolean onCreate() {
        mDbHelpr = new MovieKadeDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteDatabase database = mDbHelpr.getReadableDatabase();
        int match = sUriMatcher.match(uri);

        Cursor retCursor;
        switch (match) {
            case FAV_MOVIE: {
                retCursor = database.query(FavMovies.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            } default: {
                throw new UnsupportedOperationException("Unknown uri: " + uri);
            }
        }

        retCursor.setNotificationUri(getContext().getContentResolver(), uri);

        return retCursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SQLiteDatabase database = mDbHelpr.getWritableDatabase();
        int match = sUriMatcher.match(uri);

        Uri returnUri;

        switch (match) {
            case FAV_MOVIE: {
                long id = database.insert(MovieKadeContract.FavMovies.TABLE_NAME, null, values);
                if(id > 0) {
                    returnUri = ContentUris.withAppendedId(MovieKadeContract.FavMovies.CONTENT_URI, id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            } default: {
                throw new UnsupportedOperationException("Unknown Uri: " + uri);
            }
        }

        getContext().getContentResolver().notifyChange(uri, null);

        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase database = mDbHelpr.getWritableDatabase();
        int match = sUriMatcher.match(uri);
        int deletedRows;

        switch (match) {
            case FAV_MOVIE_WITH_ID: {
                long id = ContentUris.parseId(uri);

                deletedRows = database.delete(
                        FavMovies.TABLE_NAME,
                        FavMovies._ID + " =? ",
                        new String[]{String.valueOf(id)});
                break;
            } default: {
                throw new UnsupportedOperationException("Unknown uri: " + uri);
            }
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return deletedRows;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        SQLiteDatabase database = mDbHelpr.getWritableDatabase();
        int match = sUriMatcher.match(uri);
        int updatedRow;

        switch (match) {
            case FAV_MOVIE_WITH_ID: {
                updatedRow = database.update(
                        FavMovies.TABLE_NAME,
                        values,
                        selection,
                        selectionArgs);
                break;
            } default: {
                throw new UnsupportedOperationException("Unknown uri: " + uri);
            }
        }

        getContext().getContentResolver().notifyChange(uri, null);

        return updatedRow;
    }
}
