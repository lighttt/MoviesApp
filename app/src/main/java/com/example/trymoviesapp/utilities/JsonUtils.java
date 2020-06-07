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
    private static final String TAG = JsonUtils.class.getSimpleName();

    private static final String KEY_RESULTS = "results";
    private static final String KEY_ID = "id";


    private static final String KEY_ORIGINAL_TITLE = "original_title";

    private static final String KEY_POSTER_PATH = "poster_path";

    private static final String KEY_OVERVIEW = "overview";

    private static final String KEY_VOTE_AVERAGE = "vote_average";

    private static final String KEY_RELEASE_DATE = "release_date";

    /** Status code */
    private static final String KEY_STATUS_CODE = "status_code";

    private static final String IMAGE_BASE_URL = "https://image.tmdb.org/t/p/";

    private static final String IMAGE_FILE_SIZE = "w185";


    public static List<Movie> parseMovieJson(String movieJsonStr) throws JSONException {
        // If the JSON String is empty or null, then return early
        if (TextUtils.isEmpty(movieJsonStr)) {
            return null;
        }

        // Create an empty List of movies to hold each movie object
        List<Movie> movies = new ArrayList<>();

        // Create a JSONObject from the JSON response string
        JSONObject movieBaseJson = new JSONObject(movieJsonStr);

        /* Check if there is an error */
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

        JSONArray resultsArray = movieBaseJson.getJSONArray(KEY_RESULTS);
        for (int i = 0; i < resultsArray.length(); i ++) {
            JSONObject currentMovie = resultsArray.getJSONObject(i);

            int id = 0;
            if (currentMovie.has(KEY_ID)) {
                // Extract the value for the key called "id"
                id = currentMovie.getInt(KEY_ID);
            }

            String posterPath = null;
            if (currentMovie.has(KEY_POSTER_PATH)) {
                // Extract the value for the key called "poster_path"
                posterPath = currentMovie.getString(KEY_POSTER_PATH);
            }
            String thumbnailUrl = IMAGE_BASE_URL + IMAGE_FILE_SIZE + posterPath;


            String originalTitle = null;
            if (currentMovie.has(KEY_ORIGINAL_TITLE)) {
                originalTitle = currentMovie.getString(KEY_ORIGINAL_TITLE);
            }

            String overView = null;
            if (currentMovie.has(KEY_OVERVIEW)) {
                // Extract the value for the key called "overview"
                overView = currentMovie.getString(KEY_OVERVIEW);
            }

            double voteAverage = 0;
            if (currentMovie.has(KEY_VOTE_AVERAGE)) {
                // Extract the value for the key called "vote_average"
                voteAverage = currentMovie.getDouble(KEY_VOTE_AVERAGE);
            }

            String releaseDate = null;
            if (currentMovie.has(KEY_RELEASE_DATE)) {
                releaseDate = currentMovie.getString(KEY_RELEASE_DATE);
            }

            Movie movie = new Movie(id,originalTitle, thumbnailUrl, overView, voteAverage, releaseDate);
            movies.add(movie);
        }

        // Return the list of movies
        return movies;
    }

}
