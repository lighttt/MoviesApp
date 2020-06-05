package com.example.trymoviesapp.data;

import android.content.Context;

public class MoviePreferences {

    private static final String PREF_POPULAR = "popular";

    public static String getPreferredSortCriteria(Context context) {
        return PREF_POPULAR;
    }
}
