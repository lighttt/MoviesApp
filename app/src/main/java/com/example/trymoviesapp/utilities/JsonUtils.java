package com.example.trymoviesapp.utilities;

import android.text.TextUtils;

import com.example.trymoviesapp.model.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

public class JsonUtils {
    /** Tag for the log message */
    private static final String TAG = JsonUtils.class.getSimpleName();

    /** Constants for the Json key.
     * Each movie in the TheMovieDb JSON data is an element of the "results" array */
    private static final String KEY_RESULTS = "results";

    /** Original title for the movie */
    private static final String KEY_ORIGINAL_TITLE = "original_title";

    /** Poster path for the movie */
    private static final String KEY_POSTER_PATH = "poster_path";

    /** A plot synopsis (called overview in the api) for the movie */
    private static final String KEY_OVERVIEW = "overview";

    /** User rating (called vote_average in the api) for the movie */
    private static final String KEY_VOTE_AVERAGE = "vote_average";

    /** Release data for the movie */
    private static final String KEY_RELEASE_DATE = "release_date";

    /** Status code */
    private static final String KEY_STATUS_CODE = "status_code";

    /** The base image URL to build the complete url that is necessary for fetching the image */
    private static final String IMAGE_BASE_URL = "https://image.tmdb.org/t/p/";

    /** The image file size to build the complete url that is necessary for fetching the image*/
    private static final String IMAGE_FILE_SIZE = "w185";

    /**
     * This method parses JSON from a web response and returns a List of Movies describing the movie
     *
     * @param movieJsonStr JSON response from server
     * @return List of Movies describing movie data
     */
    public static List<Movie> parseMovieJson(String movieJsonStr) throws JSONException {
        // If the JSON String is empty or null, then return early
        if (TextUtils.isEmpty(movieJsonStr)) {
            return null;
        }

        // Create an empty List of movies to hold each movie object
        List<Movie> movies = new ArrayList<>();

        // Create a JSONObject from the JSON response string
        JSONObject movieBaseJson = new JSONObject(movieJsonStr);

        /** Check if there is an error */
        if (movieBaseJson.has(KEY_STATUS_CODE)) {
            int errorCode = movieBaseJson.getInt(KEY_STATUS_CODE);

            switch (errorCode) {
                case HttpURLConnection.HTTP_OK:
                    break;
                case HttpURLConnection.HTTP_NOT_FOUND:
                    /* Invalid id: The pre-requisite id is invalid or not found.*/
                    return null;
                default:
                    return null;
            }
        }

        // Get the JSON array representing the result
        JSONArray resultsArray = movieBaseJson.getJSONArray(KEY_RESULTS);
        for (int i = 0; i < resultsArray.length(); i ++) {
            // Get a single movie at position i within the list of movies
            JSONObject currentMovie = resultsArray.getJSONObject(i);

            // For a given movie, if it contains the key called "poster_path", extract the value for
            // the key
            String posterPath = null;
            if (currentMovie.has(KEY_POSTER_PATH)) {
                // Extract the value for the key called "poster_path"
                posterPath = currentMovie.getString(KEY_POSTER_PATH);
            }
            // Combining base image url, image file size and poster path to get a final thumbnail url
            String thumbnailUrl = IMAGE_BASE_URL + IMAGE_FILE_SIZE + posterPath;

            // For a given movie, if it contains the key called "original_title", extract the value for
            // the key
            String originalTitle = null;
            if (currentMovie.has(KEY_ORIGINAL_TITLE)) {
                // Extract the value for the key called "original_title"
                originalTitle = currentMovie.getString(KEY_ORIGINAL_TITLE);
            }

            // For a given movie, if it contains the key called "overview", extract the value for the key
            String overView = null;
            if (currentMovie.has(KEY_OVERVIEW)) {
                // Extract the value for the key called "overview"
                overView = currentMovie.getString(KEY_OVERVIEW);
            }

            // For a given movie, if it contains the key called "vote_average", extract the value for the key
            double voteAverage = 0;
            if (currentMovie.has(KEY_VOTE_AVERAGE)) {
                // Extract the value for the key called "vote_average"
                voteAverage = currentMovie.getDouble(KEY_VOTE_AVERAGE);
            }

            // For a given movie, if it contains the key called "release_date", extract the value for the key
            String releaseDate = null;
            if (currentMovie.has(KEY_RELEASE_DATE)) {
                // Extract the value for the key called "release_date"
                releaseDate = currentMovie.getString(KEY_RELEASE_DATE);
            }

            // Create a new {@link Movie} object
            Movie movie = new Movie(originalTitle, thumbnailUrl, overView, voteAverage, releaseDate);
            // Add the new {@link Movie} to the list of movies
            movies.add(movie);
        }

        // Return the list of movies
        return movies;
    }

}
