package com.aminiam.moviekade.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aminiam.moviekade.R;
import com.aminiam.moviekade.utility.JsonUtility;
import com.aminiam.moviekade.utility.NetworkUtility;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;

public class PlayingFragment extends Fragment {
    private static final String LOG_TAG = PlayingFragment.class.getSimpleName();

    public PlayingFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final URL url = NetworkUtility.getMoviesUrl(NetworkUtility.POPULAR_PATH);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String result = NetworkUtility.getResponseFromHttpUrl(url);
                    JsonUtility.getMoviesDataFromJson(result);
                    Log.d(LOG_TAG, result);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        return inflater.inflate(R.layout.fragment_playing, container, false);
    }
}
