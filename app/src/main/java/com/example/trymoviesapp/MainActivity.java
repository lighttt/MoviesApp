package com.example.trymoviesapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
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
    private TextView mOfflineTextView;
    private static final int GRID_SPAN_COUNT = 3;
    RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRecyclerView = findViewById(R.id.rv_movie);
        mOfflineTextView = findViewById(R.id.tv_offline);

        GridLayoutManager layoutManager = new GridLayoutManager(this, GRID_SPAN_COUNT);
        mRecyclerView.setLayoutManager(layoutManager);

        mRecyclerView.setHasFixedSize(true);

        List<Movie> movies = new ArrayList<>();
        mMovieAdapter = new MovieAdapter(movies, this);
        mRecyclerView.setAdapter(mMovieAdapter);

        //show a dialog when there is no internet connection
        showNetworkDialog(isOnline());
        loadMovieData();
    }

    // ------------------------ Network Checking -----------------------------

    private boolean isOnline() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnectedOrConnecting();
    }

    private void showNetworkDialog(final boolean isOnline) {
        if (!isOnline) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.Theme_AppCompat_Dialog_Alert);
            //set an incon
            builder.setIcon(R.drawable.ic_warning);
            builder.setTitle(getString(R.string.no_network_title));
            builder.setTitle(getString(R.string.no_network_message));
            builder.setPositiveButton(getString(R.string.go_to_settings), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    startActivityForResult(new Intent(Settings.ACTION_SETTINGS), 0);
                }
            });
            builder.setNegativeButton(getString(R.string.cancel), null);

            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
    }


    // ------------------------ Background Functions -----------------------------

    private void showMovieDataView() {
        mOfflineTextView.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    private void showOfflineMessage() {
        mRecyclerView.setVisibility(View.INVISIBLE);
        mOfflineTextView.setVisibility(View.VISIBLE);
    }

    private void loadMovieData() {
        showMovieDataView();
        String sort = MoviePreferences.getPreferredSortCriteria(this);
        new FetchMovieTask().execute(sort);
    }


    // ------------------------ ASYNC TASK -----------------------------

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
                showMovieDataView();
                mMovieAdapter.addAll(movies);
            }
            if (!isOnline()) {
                showOfflineMessage();
            }
        }
    }

    // ------------------------ RECYCLERVIEW -----------------------------
    @Override
    public void onItemClick(Movie movie) {
        Intent intent = new Intent(MainActivity.this, DetailActivity.class);
        // Pass the selected Movie object through Intent
        intent.putExtra(DetailActivity.EXTRA_MOVIE, movie);
        // Once the Intent has been created, start the DetailActivity
        startActivity(intent);

        Toast.makeText(this, "toast:" + movie.getId(), Toast.LENGTH_SHORT).show();
    }
}