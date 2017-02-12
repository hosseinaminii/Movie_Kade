package com.aminiam.moviekade.fragment;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.aminiam.moviekade.R;
import com.aminiam.moviekade.adapter.MovieAdapter;
import com.aminiam.moviekade.data.MovieKadeContract;
import com.aminiam.moviekade.data.MovieKadeContract.FavMovies;
import com.aminiam.moviekade.databinding.FragmentBookmarkBinding;
import com.aminiam.moviekade.other.Callback;
import com.aminiam.moviekade.other.GridSpacingItemDecoration;
import com.aminiam.moviekade.other.MovieStructure;
import com.aminiam.moviekade.other.UiUpdaterListener;
import com.aminiam.moviekade.utility.NetworkUtility;
import com.aminiam.moviekade.utility.Utility;

public class BookmarkFragment extends Fragment implements MovieAdapter.MovieClickListener,
        LoaderManager.LoaderCallbacks<Cursor> {
    private static final String LOG_TAG = BookmarkFragment.class.getSimpleName();

    public BookmarkFragment() {}

    private static final String[] PROJECTION = new String[]{
            FavMovies._ID,
            FavMovies.COLUMN_MOVIE_ID,
            FavMovies.COLUMN_TITLE,
            FavMovies.COLUMN_AVERAGE_VOTE,
            FavMovies.COLUMN_POSTER,
            FavMovies.COLUMN_BAKC_DROP};

    private static final int COLUMN_ID = 0;
    private static final int COLUMN_MOVIE_ID = 1;
    private static final int COLUMN_TITLE = 2;
    private static final int COLUMN_AVERAGE_VOTE = 3;
    private static final int COLUMN_POSTER = 4;
    private static final int COLUMN_BAKC_DROP = 5;

    private final static int LOADER_ID = 1000;
    private MovieAdapter mAdapter;

    private FragmentBookmarkBinding mBinding;
    private UiUpdaterListener mListener;
    private Toast mToast;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (UiUpdaterListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.getClass().getName() + " must implements UiUpdaterListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentBookmarkBinding.inflate(inflater, container, false);

        mAdapter = new MovieAdapter(getActivity(), this);
        int spanCount = Utility.calculateNoOfColumns(getActivity());
        mBinding.recMovies.setLayoutManager(new GridLayoutManager(getContext(), spanCount));
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.grid_layout_margin);
        mBinding.recMovies.addItemDecoration(new GridSpacingItemDecoration(spanCount,
                spacingInPixels, true, 0));

        return mBinding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();

        if (NetworkUtility.isNetworkAvailable(getContext())) {
            getActivity().getSupportLoaderManager().initLoader(LOADER_ID, null, this);
        } else {
            mListener.error(getString(R.string.error_message_internet));
        }
    }

    @Override
    public void onMovieClick(long movieId, String movieTitle, String posterPath, String backdropPath) {
        ((Callback) getActivity()).onItemSelected(movieId,movieTitle, posterPath, backdropPath);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(),
                MovieKadeContract.FavMovies.CONTENT_URI,
                PROJECTION,
                null,
                null,
                MovieKadeContract.FavMovies._ID + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, final Cursor data) {
        if (data == null) {
            String errorMessage = getResources().getString(R.string.error_message_failed);
            if(!NetworkUtility.isNetworkAvailable(getActivity())) {
                getString(R.string.error_message_internet);
            }
            mListener.error(errorMessage);
            showToast(errorMessage);
            return;
        }

        if(data.getCount() <=0) {
            mBinding.lneNoFav.setVisibility(View.VISIBLE);
        }

        new AsyncTask<Void, Void, MovieStructure[]>() {

            @Override
            protected MovieStructure[] doInBackground(Void... params) {
                MovieStructure[] movieStructures = new MovieStructure[data.getCount()];
                data.moveToFirst();
                for(int i = 0; i < movieStructures.length; i++) {
                    MovieStructure movieStructure = new MovieStructure();
                    movieStructure.id = data.getLong(COLUMN_MOVIE_ID);
                    movieStructure.title = data.getString(COLUMN_TITLE);
                    movieStructure.averageVote = data.getDouble(COLUMN_AVERAGE_VOTE);
                    movieStructure.posterName = data.getString(COLUMN_POSTER);
                    movieStructure.backdropPath = data.getString(COLUMN_BAKC_DROP);

                    movieStructures[i] = movieStructure;
                    data.moveToNext();
                }

                return movieStructures;
            }

            @Override
            protected void onPostExecute(MovieStructure[] movieStructures) {
                super.onPostExecute(movieStructures);
                mAdapter.populateDate(movieStructures);
                mBinding.recMovies.setAdapter(mAdapter);
            }
        }.execute();

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.populateDate(null);
    }

    private void showToast(String message) {
        if(mToast != null) {
            mToast.cancel();
        }
        mToast = Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT);
        mToast.show();
    }
}
