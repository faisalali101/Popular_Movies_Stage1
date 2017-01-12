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
package com.example.popularmovies;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.popularmovies.data.Movie;
import com.example.popularmovies.utilities.TMDBUtils;

public class MovieListAdapter
        extends RecyclerView.Adapter<MovieListAdapter.MovieListAdapterViewHolder> {
    private final MovieListOnClickHandler mClickHandler;
    private Movie[] mMovieList;

    public MovieListAdapter (MovieListOnClickHandler mClickHandler) {
        this.mClickHandler = mClickHandler;
    }

    @Override
    public MovieListAdapterViewHolder onCreateViewHolder (ViewGroup parent,
                                                          int viewType) {
        return new MovieListAdapterViewHolder(LayoutInflater.from(parent.getContext())
                                                            .inflate(R.layout.list_item_popular_movie,
                                                                     parent,
                                                                     false));
    }

    @Override
    public void onBindViewHolder (MovieListAdapterViewHolder holder,
                                  int position) {
        TMDBUtils.fetchImage(holder.mMoviePosterIV.getContext().getApplicationContext(),
                             holder.mMoviePosterIV, mMovieList[position].getPosterPath(),
                             TMDBUtils.IMAGE_SIZE.w500);
    }

    @Override
    public int getItemCount () {
        if (mMovieList != null) {
            return mMovieList.length;
        }
        return 0;
    }

    public void setList (Movie[] mMovieList) {
        this.mMovieList = mMovieList;
        notifyDataSetChanged();
    }

    public interface MovieListOnClickHandler {
        void onClick (Movie movie);
    }

    public class MovieListAdapterViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {
        public final ImageView mMoviePosterIV;

        public MovieListAdapterViewHolder (View itemView) {
            super(itemView);
            mMoviePosterIV = (ImageView) itemView.findViewById(R.id.iv_movie_poster);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick (View view) {
            mClickHandler.onClick(mMovieList[getAdapterPosition()]);
        }
    }
}
