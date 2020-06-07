package com.example.trymoviesapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.trymoviesapp.model.Movie;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    /** Member variable for the list of {@link Movie}s */
    private List<Movie> mMovies;
    private final MovieAdpaterOnClickHandler mOnClickHandler;

    public MovieAdapter(List<Movie> movies,MovieAdpaterOnClickHandler onClickHandler) {
        mMovies = movies;
        mOnClickHandler = onClickHandler;
    }

    public interface MovieAdpaterOnClickHandler{
        void onItemClick(Movie movie);
    }

    @NonNull
    @Override
    public MovieAdapter.MovieViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.movie_list_item, viewGroup, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieAdapter.MovieViewHolder holder, int position) {
        Movie movie = mMovies.get(position);
        String title = movie.getTitle();
        String thumbnail = movie.getThumbnail();
        double voteAvg = movie.getVoteAverage();
        holder.titleTextView.setText(title);
        Picasso.get()
                .load(thumbnail)
                .into(holder.thumbnailImageView);
        holder.voteAvgTextView.setText(String.valueOf(voteAvg));
    }

    @Override
    public int getItemCount() {
        if (null == mMovies) return 0;
        return mMovies.size();
    }

    public void setMovies(List<Movie> movies) {
        mMovies = movies;
        notifyDataSetChanged();
    }

    public void clearAll() {
        mMovies.clear();
        notifyDataSetChanged();
    }

    public void addAll(List<Movie> movies) {
        mMovies.clear();
        mMovies.addAll(movies);
        notifyDataSetChanged();
    }


    public class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView thumbnailImageView;
        TextView titleTextView;
        TextView voteAvgTextView;

        public MovieViewHolder(View itemView) {
            super(itemView);
            thumbnailImageView = itemView.findViewById(R.id.iv_thumbnail);
            titleTextView = itemView.findViewById(R.id.tv_title);
            voteAvgTextView = itemView.findViewById(R.id.tv_vote_average);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            Movie movie = mMovies.get(adapterPosition);
            mOnClickHandler.onItemClick(movie);
        }
    }
}