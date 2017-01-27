package com.aminiam.moviekade.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.aminiam.moviekade.R;
import com.aminiam.moviekade.adapter.MovieAdapter;
import com.aminiam.moviekade.other.GridSpacingItemDecoration;
import com.aminiam.moviekade.other.MovieStructure;
import com.aminiam.moviekade.utility.JsonUtility;
import com.aminiam.moviekade.utility.NetworkUtility;
import com.aminiam.moviekade.utility.Utility;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;

public class PlayingFragment extends Fragment implements LoaderManager.LoaderCallbacks<String> {
    private static final String LOG_TAG = PlayingFragment.class.getSimpleName();

    private static final int LOADER_ID = 99;
    private RecyclerView mRecPlayingMovie;
    private ProgressBar mLoadingIndicator;
    private TextView mTextView;
    private MovieAdapter mAdapter;

    public PlayingFragment() {
        // Required empty public constructor
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getActivity().getSupportLoaderManager().restartLoader(LOADER_ID, null, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_playing, container, false);
        mRecPlayingMovie = (RecyclerView) rootView.findViewById(R.id.recPlayingMovies);
        mLoadingIndicator = (ProgressBar) rootView.findViewById(R.id.loadingIndicator);
        mTextView = (TextView) rootView.findViewById(R.id.textView);

        return rootView;
    }

    @Override
    public Loader<String> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<String>(getActivity()) {
            @Override
            protected void onStartLoading() {
                updateView(true);
                forceLoad();
            }

            @Override
            public String loadInBackground() {
                try {
                    final URL url = NetworkUtility.getMoviesUrl(NetworkUtility.POPULAR_PATH);
                    return NetworkUtility.getResponseFromHttpUrl(url);
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<String> loader, String data) {
        if(data == null) {
            showError();
        } else {
            try {
                updateView(false);
                MovieStructure[] movieStructures = JsonUtility.getMoviesDataFromJson(data);
                int spanCount = Utility.calculateNoOfColumns(getActivity());
                mAdapter = new MovieAdapter(getActivity(), movieStructures);
                mRecPlayingMovie.setLayoutManager(new GridLayoutManager(getContext(), spanCount));
                int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.grid_layout_margin);
                mRecPlayingMovie.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacingInPixels, true, 0));
                mRecPlayingMovie.setAdapter(mAdapter);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onLoaderReset(Loader loader) {

    }

    private void updateView(boolean isLoading) {
        if(isLoading) {
            mLoadingIndicator.setVisibility(View.VISIBLE);
            mTextView.setVisibility(View.INVISIBLE);
        } else {
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            mTextView.setVisibility(View.VISIBLE);
        }
    }

    private void showError() {
        mLoadingIndicator.setVisibility(View.INVISIBLE);
        mTextView.setVisibility(View.VISIBLE);
        mTextView.setText(getResources().getString(R.string.error_message));
    }
}
