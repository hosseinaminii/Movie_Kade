package com.aminiam.moviekade.utility;


import android.content.Context;

import com.aminiam.moviekade.other.MovieInformationStructure;
import com.aminiam.moviekade.other.MovieStructure;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JsonUtility {
    private static final String LOG_TAG = JsonUtility.class.getSimpleName();

    private static final String ARRAY_RESULT = "results";
    private static final String ID = "id";
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
        JSONArray results = moviesJson.getJSONArray(ARRAY_RESULT);

        MovieStructure[] movieStructureList = new MovieStructure[results.length()];
        for(int i = 0; i < results.length(); i++) {
            MovieStructure movieStructure = new MovieStructure();
            JSONObject movieJsonObject = results.getJSONObject(i);

            long id = movieJsonObject.getLong(ID);
            String title = movieJsonObject.getString(ORIGINAL_TITLE);
            String posterPath = movieJsonObject.getString(POSTER_PATH);
            double averageVote = movieJsonObject.getDouble(AVERAGE_VOTE);

            movieStructure.id = id;
            movieStructure.title = title;
            movieStructure.posterName = posterPath;
            movieStructure.averageVote = averageVote;

            movieStructureList[i] = movieStructure;
        }

        return movieStructureList;
    }

    private static String TITLE = "title";
    private static String ARRAY_GENRES = "genres";
    private static String GENER_NAME = "name";
    private static String STATUS = "status";
    private static String OVERVIEW = "overview";
    private static String RUNTIME = "runtime";
    private static String LANGUAGE = "original_language";
    private static String ADULT = "adult";
    private static String RELEASE_DATE = "release_date";
    private static String WEBSITE = "homepage";
    private static String REVENU = "revenue";
    private static String ARRAY_VIDEOS = "videos";
    private static String VIDEO_KEY = "key";

    public static MovieInformationStructure getMovieInformationFromJson(Context context, String movieInfoJsonStr)throws JSONException {
        MovieInformationStructure movieInformationStructure = new MovieInformationStructure();
        JSONObject jsonObject = new JSONObject(movieInfoJsonStr);

        long id = jsonObject.getLong(ID);
        String title = jsonObject.getString(TITLE);
        String status = jsonObject.getString(STATUS);
        String overview = jsonObject.getString(OVERVIEW);
        int runtime = jsonObject.getInt(RUNTIME);
        String language = jsonObject.getString(LANGUAGE);
        boolean adult = jsonObject.getBoolean(ADULT);
        String releaseDate = jsonObject.getString(RELEASE_DATE);
        String website = jsonObject.getString(WEBSITE);
        long revenue = jsonObject.getLong(REVENU);

        movieInformationStructure.id = id;
        movieInformationStructure.title = title;
        movieInformationStructure.status = status;
        movieInformationStructure.overview = overview;
        movieInformationStructure.runtime = Utility.convertMinToHour(context, runtime);
        movieInformationStructure.language = language;
        movieInformationStructure.adult = adult;
        movieInformationStructure.releaseDate = releaseDate;
        movieInformationStructure.website = website;
        movieInformationStructure.revenue = revenue;

        JSONObject trailers = jsonObject.getJSONObject(ARRAY_VIDEOS);
        JSONArray trailersResults = trailers.getJSONArray(ARRAY_RESULT);
        for(int i = 0; i < trailersResults.length(); i++) {
            JSONObject videoObject = (JSONObject) trailersResults.get(i);

            String trailerKey = videoObject.getString(VIDEO_KEY);
            movieInformationStructure.trailers.add(trailerKey);
        }

        return movieInformationStructure;
    }

}
