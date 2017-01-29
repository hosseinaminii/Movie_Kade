package com.aminiam.moviekade.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.aminiam.moviekade.R;
import com.aminiam.moviekade.activity.MainActivity;
import com.aminiam.moviekade.adapter.MovieAdapter;
import com.aminiam.moviekade.databinding.FragmentPlayingBinding;
import com.aminiam.moviekade.other.GridSpacingItemDecoration;
import com.aminiam.moviekade.other.MovieStructure;
import com.aminiam.moviekade.utility.JsonUtility;
import com.aminiam.moviekade.utility.NetworkUtility;
import com.aminiam.moviekade.utility.Utility;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;

public class PlayingFragment extends Fragment implements LoaderManager.LoaderCallbacks<String>, View.OnClickListener {
    private static final String LOG_TAG = PlayingFragment.class.getSimpleName();

    private MovieAdapter mAdapter;
    private Toast mToast;
    private String mPath;
    private int mLoaderId;

    private FragmentPlayingBinding mBinding;
    private NetworkReceiver mNetworkReceiver;
    private IntentFilter mNetworkIntentFilter;

    public PlayingFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        mPath = args.getString(MainActivity.PATH_KEY);
        mLoaderId = args.getInt(MainActivity.LOADER_ID_KEY);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentPlayingBinding.inflate(inflater, container, false);
        mBinding.btnTryAgain.setOnClickListener(this);

        return mBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(savedInstanceState == null) {
            int spanCount = Utility.calculateNoOfColumns(getActivity());
            mBinding.recPlayingMovies.setLayoutManager(new GridLayoutManager(getContext(), spanCount));
            int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.grid_layout_margin);
            mBinding.recPlayingMovies.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacingInPixels, true, 0));
        }

        mNetworkIntentFilter = new IntentFilter();
        mNetworkIntentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        mNetworkReceiver = new NetworkReceiver();

        getActivity().getSupportLoaderManager().initLoader(mLoaderId, null, this);
    }

    @Override
    public void onResume() {
        super.onResume();

        getActivity().registerReceiver(mNetworkReceiver, mNetworkIntentFilter);
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(mNetworkReceiver);
    }

    @Override
    public Loader<String> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<String>(getActivity()) {
            @Override
            protected void onStartLoading() {
                updateViews(true);
                forceLoad();
            }

            @Override
            public String loadInBackground() {
                try {
                    final URL url = NetworkUtility.getMoviesUrl(mPath);
                    return NetworkUtility.getResponseFromHttpUrl(url);
                } catch (IOException e) {
                    e.printStackTrace();
//                    showError(getResources().getString(R.string.error_message));
                    return null;
                }
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<String> loader, String data) {
        if (data == null) {
            if(!NetworkUtility.isNetworkAvailable(getActivity())) {
                showError(getString(R.string.error_message_internet));
            } else {
                showError(getResources().getString(R.string.error_message_failed));
            }
        } else {
            try {
                updateViews(false);
                MovieStructure[] movieStructures = JsonUtility.getMoviesDataFromJson(data);

                mAdapter = new MovieAdapter(getActivity(), movieStructures);



                mBinding.recPlayingMovies.setAdapter(mAdapter);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onLoaderReset(Loader loader) {
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnTryAgain: {
                if(NetworkUtility.isNetworkAvailable(getActivity())) {
                    getActivity().getSupportLoaderManager().initLoader(mLoaderId, null, this);
                } else {
                    showToast(getString(R.string.error_message_internet));
                }
                break;
            }
        }
    }

    private void updateViews(boolean isLoading) {
        if (isLoading) {
            mBinding.loadingIndicator.setVisibility(View.VISIBLE);
            mBinding.recPlayingMovies.setVisibility(View.INVISIBLE);
        } else {
            mBinding.loadingIndicator.setVisibility(View.INVISIBLE);
            mBinding.recPlayingMovies.setVisibility(View.VISIBLE);
        }
        mBinding.lneError.setVisibility(View.INVISIBLE);
    }

    private void showError(String errMessage) {
        int iconResource = R.drawable.ic_alert;
        if (errMessage.equals(getString(R.string.error_message_internet))) {
            iconResource = R.drawable.ic_wifi_off;
        }
        mBinding.loadingIndicator.setVisibility(View.INVISIBLE);
        mBinding.recPlayingMovies.setVisibility(View.INVISIBLE);
        mBinding.lneError.setVisibility(View.VISIBLE);

        mBinding.txtError.setText(errMessage);
        mBinding.imgErrorIcon.setImageResource(iconResource);
    }

    private void showToast(String message) {
        if(mToast != null) {
            mToast.cancel();
        }
        mToast = Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT);
        mToast.show();
    }

    private class NetworkReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if(NetworkUtility.isNetworkAvailable(context)) {
                getActivity().getSupportLoaderManager().initLoader(mLoaderId, null, PlayingFragment.this);
            } else {
                showError(getString(R.string.error_message_internet));
            }
        }
    }

    @Override
    public void onStop() {
        super.onStop();
//        getActivity().getSupportLoaderManager().destroyLoader(mLoaderId);
    }

}
