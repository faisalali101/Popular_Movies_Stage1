/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.popularmovies.utilities;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.widget.ImageView;

import com.example.popularmovies.data.Movie;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class TMDBUtils {
    private static final IMAGE_SIZE IMAGE_SIZE_DEFAULT = IMAGE_SIZE.w185;

    private static final String MOVIE_POSTER_BASE_URL = "http://image.tmdb.org/t/p/";
    private static final String MOVIE_LIST_BASE_URL = "http://api.themoviedb.org/3/movie/";

    private static final String API_KEY_PARAM = "api_key";
    private static final String POPULAR = "popular";
    private static final String TOP_RATED = "top_rated";
    private static final String TAG = TMDBUtils.class.getSimpleName();
    private static String API_KEY = null;

    private static void fetchAPIKey (Context context) {
        if (API_KEY != null) {
            return;
        }

        Scanner fileScanner = null;
        try {
            fileScanner = new Scanner(context.getAssets().open("API_KEY"));
            fileScanner.useDelimiter("\\A");

            if (fileScanner.hasNext()) {
                API_KEY = fileScanner.next();
            }
        } catch (IOException e) {
            Log.e(TAG, "fetchAPIKey -> \n " + e.getMessage());
        } finally {
            if (fileScanner != null) {
                fileScanner.close();
            }
        }
    }

    public static URL buildURLFor (Context context, LIST_TYPE type) {
        fetchAPIKey(context);
        String mainURL = null;
        switch (type) {
            case Popular:
                mainURL = MOVIE_LIST_BASE_URL + POPULAR;
                break;
            case Top_Rated:
                mainURL = MOVIE_LIST_BASE_URL + TOP_RATED;
                break;
            default:
        }
        Uri
                movieUri =
                Uri.parse(mainURL).buildUpon().appendQueryParameter(API_KEY_PARAM, API_KEY).build();
        URL movieURL = null;
        try {
            movieURL = new URL(movieUri.toString());
        } catch (MalformedURLException e) {
            Log.e(TAG, "buildURLFor -> \n" + e.getMessage());
        }
        return movieURL;
    }

    public static String getResponseFromTMDB (URL url) {
        HttpURLConnection urlConnection = null;
        Scanner urlScanner = null;
        try {
            urlConnection =
                    (HttpURLConnection) url.openConnection();
            urlScanner = new Scanner(urlConnection.getInputStream());
            urlScanner.useDelimiter("\\A");
            if (urlScanner.hasNext()) {
                return urlScanner.next();
            }
        } catch (IOException e) {
            Log.e(TAG, "getResponseFromTMDB -> \n " + e.getMessage());
        } finally {
            if (urlScanner != null) {
                urlScanner.close();
            }
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
        return null;
    }

    public static Movie[] getMovieDetailsfromJSON (String response) {
        if (response == null || response.trim().isEmpty()) {
            return null;
        }
        final String page = "page";
        final String totalPages = "total_pages";
        final String results = "results";
        final String totalResults = "total_results";
        final String posterPath = "poster_path";
        final String adult = "adult";
        final String overview = "overview";
        final String releaseDate = "release_date";
        final String genreIDs = "genre_ids";
        final String ID = "id";
        final String originalTitle = "original_title";
        final String originalLanguage = "original_language";
        final String title = "title";
        final String language = "language";
        final String backdropPath = "backdrop_path";
        final String popularity = "popularity";
        final String voteCount = "vote_count";
        final String video = "video";
        final String voteAverage = "vote_average";

        Movie[] movieList = null;
        try {
            JSONObject responseJSONObject = new JSONObject(response);
            JSONArray resultsJSONArray = responseJSONObject.getJSONArray(results);

            int noOfEntries = resultsJSONArray.length();
            movieList = new Movie[noOfEntries];
            for (int i = 0; i < noOfEntries; i++) {
                JSONObject movieEntry = resultsJSONArray.getJSONObject(i);

                JSONArray movieGenreArray = movieEntry.getJSONArray(genreIDs);
                int genreLength = movieGenreArray.length();
                int[] genreIds = new int[genreLength];
                for (int j = 0; j < genreLength; j++) {
                    genreIds[j] = movieGenreArray.getInt(j);
                }

                movieList[i] =
                        new Movie(movieEntry.getString(originalTitle),
                                  movieEntry.getString(overview),
                                  movieEntry.getString(releaseDate),
                                  movieEntry.getString(posterPath),
                                  movieEntry.getString(backdropPath),
                                  movieEntry.getString(originalLanguage),
                                  movieEntry.getString(title),
                                  movieEntry.getInt(ID),
                                  movieEntry.getInt(voteCount),
                                  movieEntry.getDouble(voteAverage),
                                  movieEntry.getDouble(popularity),
                                  movieEntry.getBoolean(video),
                                  movieEntry.getBoolean(adult),
                                  genreIds);
            }
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
        }
        return movieList;
    }

    public static void fetchImage (Context context, ImageView imageView, String imageName) {
        fetchImage(context, imageView, imageName, IMAGE_SIZE_DEFAULT);
    }

    public static void fetchImage (Context context,
                                   ImageView imageView,
                                   String imageName,
                                   IMAGE_SIZE imageSize) {
        String imageURL = MOVIE_POSTER_BASE_URL + imageSize.name() + "/" + imageName;
        Picasso.with(context).load(imageURL).into(imageView);
    }

    public enum LIST_TYPE {Popular, Top_Rated}

    public enum IMAGE_SIZE {w92, w154, w185, w342, w500, w780, original}
}
