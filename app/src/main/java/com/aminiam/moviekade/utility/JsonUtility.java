package com.aminiam.moviekade.utility;


import android.util.Log;

import com.aminiam.moviekade.other.MovieStructure;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JsonUtility {
    private static final String LOG_TAG = JsonUtility.class.getSimpleName();

    private static final String ARR_RESULT = "results";
    private static final String POSTER_PATH = "poster_path";
    private static final String ORIGINAL_TITLE = "original_title";
    private static final String AVERAGE_VOTE = "vote_average";

    /**
     * This method parses JSON from a web response and return an array of MoviesStructure describing
     * data over various movies.
     * @param moviesJsonStr JSON response from server for fetching movies data
     * @return Array of movieStructure describing movies data
     * @throws JSONException
     */
    public static MovieStructure[] getMoviesDataFromJson(String moviesJsonStr)
        throws JSONException{

        JSONObject moviesJson = new JSONObject(moviesJsonStr);
        JSONArray results = moviesJson.getJSONArray(ARR_RESULT);

        MovieStructure[] movieStructureList = new MovieStructure[results.length()];
        for(int i = 0; i < results.length(); i++) {
            MovieStructure movieStructure = new MovieStructure();
            JSONObject movieJsonObject = results.getJSONObject(i);

            String title = movieJsonObject.getString(ORIGINAL_TITLE);
            String posterPath = movieJsonObject.getString(POSTER_PATH);
            double averageVote = movieJsonObject.getDouble(AVERAGE_VOTE);

            movieStructure.title = title;
            movieStructure.posterPath = posterPath;
            movieStructure.avarageVote = averageVote;

            movieStructureList[0] = movieStructure;
            Log.d(LOG_TAG, "Title= " + title + " Poster Path= " + posterPath +
                    " Average Vote= " + averageVote);
        }

        return movieStructureList;
    }


}
