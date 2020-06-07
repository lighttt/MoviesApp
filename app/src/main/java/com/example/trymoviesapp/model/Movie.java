package com.example.trymoviesapp.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Movie implements Parcelable {
    // movie id
    private int mId;
    private String mTitle;
    private String mThumbnail;
    private String mOverview;
    private double mVoteAverage;
    private String mReleaseDate;

    public Movie(int id, String title, String thumbnail, String overview, double voteAverage, String releaseDate) {
        mId = id;
        mTitle = title;
        mThumbnail = thumbnail;
        mOverview = overview;
        mVoteAverage = voteAverage;
        mReleaseDate = releaseDate;
    }


    private Movie(Parcel in) {
        mId = in.readInt();
        mTitle = in.readString();
        mThumbnail = in.readString();
        mOverview = in.readString();
        mVoteAverage = in.readDouble();
        mReleaseDate = in.readString();
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        /**
         * Creates a new instance of Movie, instantiating it from the given Parcel whose data had
         * previously been written by Parcelable.writeToParcel().
         *
         * @param in The Parcel to read the movie object's data from
         * @return a new instance of Movie
         */
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        /**
         * Creates a new array of Movie.
         *
         * @param size size of the array
         * @return an array of Movie, with every entry initialized to null
         */
        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    public String getTitle() {
        return mTitle;
    }


    public String getThumbnail() {
        return mThumbnail;
    }


    public String getOverview() {
        return mOverview;
    }


    public double getVoteAverage() {
        return mVoteAverage;
    }

    public String getReleaseData() {
        return mReleaseDate;
    }

    public int getId() {
        return mId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Flatten Movie object in to a Parcel
     *
     * @param parcel The Parcel in which the object should be written
     * @param i      Additional flags about how the object should be written.
     */
    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(mId);
        parcel.writeString(mTitle);
        parcel.writeString(mThumbnail);
        parcel.writeString(mOverview);
        parcel.writeDouble(mVoteAverage);
        parcel.writeString(mReleaseDate);
    }
}
