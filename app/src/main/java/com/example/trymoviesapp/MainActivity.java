package com.example.trymoviesapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.trymoviesapp.data.MoviePreferences;
import com.example.trymoviesapp.model.Movie;
import com.example.trymoviesapp.utilities.JsonUtils;
import com.example.trymoviesapp.utilities.NetworkUtils;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MovieAdapter.MovieAdpaterOnClickHandler {

    private static final String TAG = "MainActivity";

    private MovieAdapter mMovieAdapter;
    private static final int GRID_SPAN_COUNT = 3;
    RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRecyclerView = findViewById(R.id.rv_movie);


        GridLayoutManager layoutManager = new GridLayoutManager(this, GRID_SPAN_COUNT);
        mRecyclerView.setLayoutManager(layoutManager);

        mRecyclerView.setHasFixedSize(true);

        List<Movie> movies = new ArrayList<>();
        mMovieAdapter = new MovieAdapter(movies,this);
        mRecyclerView.setAdapter(mMovieAdapter);
        loadMovieData();
    }
    private void loadMovieData() {
        String sort = MoviePreferences.getPreferredSortCriteria(this);
        new FetchMovieTask().execute(sort);
    }

    @Override
    public void onItemClick(Movie movie) {
        Intent intent = new Intent(MainActivity.this, DetailActivity.class);
        // Pass the selected Movie object through Intent
        intent.putExtra(DetailActivity.EXTRA_MOVIE, movie);
        // Once the Intent has been created, start the DetailActivity
        startActivity(intent);

        Toast.makeText(this, "toast:"  + movie.getId(), Toast.LENGTH_SHORT).show();
    }


    public class FetchMovieTask extends AsyncTask<String, Void, List<Movie>> {

        @Override
        protected List<Movie> doInBackground(String... params) {
            if (params.length == 0) {
                return null;
            }

            String url = params[0];
            URL movieRequestUrl = NetworkUtils.buildMovieUrl(url);

            try {
                String jsonMovieResponse = NetworkUtils.getResponseFromHttpUrl(movieRequestUrl);

                List<Movie> jsonMovieData = JsonUtils.parseMovieJson(jsonMovieResponse);

                return jsonMovieData;
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                Log.e(TAG, "Problem retrieving the Movie Database JSON results.");
            }
            return null;
        }

        /**
         * Display the result fo the network request
         */
        @Override
        protected void onPostExecute(List<Movie> movies) {
            // Clear the adapter of the previous movie data
            mMovieAdapter.clearAll();

            // Add the movie data
            if (movies != null && !movies.isEmpty()) {
                mMovieAdapter.addAll(movies);
            }
        }
    }
}