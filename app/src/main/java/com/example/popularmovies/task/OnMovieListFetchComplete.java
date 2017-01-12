package com.example.popularmovies.task;

import com.example.popularmovies.data.Movie;

public interface OnMovieListFetchComplete {
    public void onFetchComplete(Movie[] movies);
}
