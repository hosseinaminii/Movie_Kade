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
    private static final String BACKDROP_PATH = "backdrop_path";

    /**
     * This method parses JSON from a web response and return an array of MoviesStructure describing
     * data over various movies.
     *
     * @param moviesJsonStr JSON response from server for fetching movies data
     * @return Array of movieStructure describing movies data
     * @throws JSONException
     */
    public static MovieStructure[] getMoviesDataFromJson(String moviesJsonStr)
            throws JSONException {

        JSONObject moviesJson = new JSONObject(moviesJsonStr);
        JSONArray results = moviesJson.getJSONArray(ARRAY_RESULT);

        MovieStructure[] movieStructureList = new MovieStructure[results.length()];
        for (int i = 0; i < results.length(); i++) {
            MovieStructure movieStructure = new MovieStructure();
            JSONObject movieJsonObject = results.getJSONObject(i);

            long id = movieJsonObject.getLong(ID);
            String title = movieJsonObject.getString(ORIGINAL_TITLE);
            String posterPath = movieJsonObject.getString(POSTER_PATH);
            String backdropPath = movieJsonObject.getString(BACKDROP_PATH);
            double averageVote = movieJsonObject.getDouble(AVERAGE_VOTE);

            movieStructure.id = id;
            movieStructure.title = title;
            movieStructure.posterName = posterPath;
            movieStructure.backdropPath = backdropPath;
            movieStructure.averageVote = averageVote;

            movieStructureList[i] = movieStructure;
        }

        return movieStructureList;
    }

    private final static String TITLE = "title";
    private final static String ARRAY_GENRES = "genres";
    private final static String GENRE_NAME = "name";
    private final static String STATUS = "status";
    private final static String OVERVIEW = "overview";
    private final static String RUNTIME = "runtime";
    private final static String LANGUAGE = "original_language";
    private final static String ADULT = "adult";
    private final static String RELEASE_DATE = "release_date";
    private final static String VOTE_COUNT = "vote_count";
    private final static String REVENUE = "revenue";
    private final static String VIDEOS = "videos";
    private final static String VIDEO_KEY = "key";
    private final static String REVIEWS = "reviews";
    private final static String REVIEW_AUTHOR = "author";
    private final static String REVIEW_CONTENT = "content";

    final
    public static MovieInformationStructure getMovieInformationFromJson(Context context, String movieInfoJsonStr) throws JSONException {
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
        int voteCount = jsonObject.getInt(VOTE_COUNT);
        long revenue = jsonObject.getLong(REVENUE);

        movieInformationStructure.id = id;
        movieInformationStructure.title = title;
        movieInformationStructure.status = status;
        movieInformationStructure.overview = overview;
        movieInformationStructure.runtime = Utility.convertMinToHour(context, runtime);
        movieInformationStructure.language = language;
        movieInformationStructure.adult = adult;
        movieInformationStructure.releaseDate = releaseDate;
        movieInformationStructure.voteCount = voteCount;
        movieInformationStructure.revenue = revenue;

        JSONObject trailersObject = jsonObject.getJSONObject(VIDEOS);
        JSONArray trailers = trailersObject.getJSONArray(ARRAY_RESULT);
        for (int i = 0; i < trailers.length(); i++) {
            JSONObject video = (JSONObject) trailers.get(i);

            String key = video.getString(VIDEO_KEY);
            movieInformationStructure.trailers.add(key);
        }

        JSONObject reviewsObject = jsonObject.getJSONObject(REVIEWS);
        JSONArray reviews = reviewsObject.getJSONArray(ARRAY_RESULT);
        String[][] reviewsArray = new String[reviews.length()][2];
        for (int i = 0; i < reviews.length(); i++) {
            JSONObject review = reviews.getJSONObject(i);

            String author = review.getString(REVIEW_AUTHOR);
            String content = review.getString(REVIEW_CONTENT);
            reviewsArray[i][0] = author;
            reviewsArray[i][1] = content;
        }
        movieInformationStructure.reviews = reviewsArray;

        JSONArray genres = jsonObject.getJSONArray(ARRAY_GENRES);
        StringBuilder strGenres = new StringBuilder();
        String divider = ". ";
        for (int i = 0; i < genres.length(); i++) {
            JSONObject genre = genres.getJSONObject(i);

            String name = genre.getString(GENRE_NAME);

            if (i == genres.length() - 1) {
                divider = "";
            }
            strGenres.append(name);
            strGenres.append(divider);
        }
        movieInformationStructure.genres = strGenres.toString();

        return movieInformationStructure;
    }


}
