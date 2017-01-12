package com.example.popularmovies.task;

import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ProgressBar;

import com.example.popularmovies.data.Movie;
import com.example.popularmovies.utilities.TMDBUtils;

public class FetchMovieListTask extends AsyncTask<TMDBUtils.LIST_TYPE, Void, Movie[]> {
    private Context mContext;
    private ProgressBar mProgressBar;
    private OnMovieListFetchComplete mOnMovieListFetchCompletedListener;

    public FetchMovieListTask (Context mContext, ProgressBar mProgressBar,
                               OnMovieListFetchComplete mOnMovieListFetchCompletedListener) {
        this.mContext = mContext;
        this.mProgressBar = mProgressBar;
        this.mOnMovieListFetchCompletedListener = mOnMovieListFetchCompletedListener;
    }

    @Override
    protected void onPreExecute () {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    protected Movie[] doInBackground (TMDBUtils.LIST_TYPE... list_types) {
        if (list_types == null || list_types.length == 0) {
            return null;
        }

        String
                responseFromTMDB =
                TMDBUtils.getResponseFromTMDB(TMDBUtils.buildURLFor(mContext,
                                                                    list_types[0]));
        Movie[]
                movies = TMDBUtils.getMovieDetailsfromJSON(responseFromTMDB);
        return movies;
    }

    @Override
    protected void onPostExecute (Movie[] movies) {
        mProgressBar.setVisibility(View.INVISIBLE);
        mOnMovieListFetchCompletedListener.onFetchComplete(movies);
    }
}
