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
package com.example.popularmovies.data;

import android.content.Context;

import com.example.popularmovies.R;

import java.io.Serializable;

public class Movie implements Serializable {
    private String originalTitle, overview, releaseDate, posterPath, backdropPath,
            originalLanguage, title;
    private int id, voteCount;
    private double voteAverage, popularity;
    private boolean video, adult;
    private int[] genreIds;

    public Movie (String originalTitle,
                  String overview,
                  String releaseDate,
                  String posterPath,
                  String backdropPath,
                  String originalLanguage,
                  String title,
                  int id,
                  int voteCount,
                  double voteAverage,
                  double popularity, boolean video, boolean adult, int[] genreIds) {
        this.originalTitle = originalTitle;
        this.overview = overview;
        this.releaseDate = releaseDate;
        this.posterPath = posterPath;
        this.backdropPath = backdropPath;
        this.originalLanguage = originalLanguage;
        this.title = title;
        this.id = id;
        this.voteCount = voteCount;
        this.voteAverage = voteAverage;
        this.popularity = popularity;
        this.video = video;
        this.adult = adult;
        this.genreIds = genreIds;
    }

    public String getOriginalTitle () {
        return originalTitle;
    }

    public String getOverview () {
        return overview;
    }

    public String getReleaseDate () {
        return releaseDate;
    }

    public String getPosterPath () {
        return posterPath;
    }

    public String getBackdropPath () {
        return backdropPath;
    }

    public String getOriginalLanguage () {
        return originalLanguage;
    }

    public String getTitle () {
        return title;
    }

    public int getId () {
        return id;
    }

    public int getVoteCount () {
        return voteCount;
    }

    public double getVoteAverage () {
        return voteAverage;
    }

    public double getPopularity () {
        return popularity;
    }

    public boolean isVideo () {
        return video;
    }

    public boolean isAdult () {
        return adult;
    }

    public int[] getGenreIds () {
        return genreIds;
    }

    public String toString (Context context) {
        return context.getString(R.string.title) +
               ": " +
               title +
               "\n" +
               context.getString(R.string.release_date) +
               ": " +
               releaseDate +
               "\n" +
               context.getString(R.string.vote_average) +
               ": " +
               voteAverage +
               "\n" +
               context.getString(R.string.plot_synopsis) +
               ": " +
               overview;
    }
}
