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

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.popularmovies.data.Movie;
import com.example.popularmovies.task.FetchMovieListTask;
import com.example.popularmovies.task.OnMovieListFetchComplete;
import com.example.popularmovies.utilities.TMDBUtils;

public class MainActivity extends AppCompatActivity
        implements MovieListAdapter.MovieListOnClickHandler, OnMovieListFetchComplete {
    private static final String PREFERENCE_NAME = "POPULAR_MOVIES";
    private static final String SORT_TYPE = "SORT_TYPE";

    private static TMDBUtils.LIST_TYPE sType;

    private Context mContext;
    private ProgressBar mProgressBar;
    private TextView mErrorTextView;
    private RecyclerView mRecyclerView;

    private SharedPreferences mSharedPreferences;

    private MovieListAdapter mMovieListAdapter;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = getApplicationContext();
        mProgressBar = (ProgressBar) findViewById(R.id.pb_loading_content);
        mErrorTextView = (TextView) findViewById(R.id.tv_error_display);
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_popular_movies);

        mSharedPreferences = getSharedPreferences(PREFERENCE_NAME, MODE_PRIVATE);

        mMovieListAdapter = new MovieListAdapter(this);
        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2,
                                                                      StaggeredGridLayoutManager.VERTICAL));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mMovieListAdapter);

        setListType(mSharedPreferences.getInt(SORT_TYPE, 0));
        fetchMovieList(sType);
    }

    public void setListType (int listType) {
        switch (listType) {
            default:
            case 0:
                sType =
                        TMDBUtils.LIST_TYPE.Popular;
                break;
            case 1:
                sType =
                        TMDBUtils.LIST_TYPE.Top_Rated;
                break;
        }
        mSharedPreferences.edit().putInt(SORT_TYPE, sType.ordinal()).commit();
    }

    public void refresh () {
        mMovieListAdapter.setList(null);
        fetchMovieList(sType);
    }

    @Override
    public boolean onCreateOptionsMenu (Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_refresh:
                refresh();
                return true;
            case R.id.action_sort_by:
                (new AlertDialog.Builder(this).setTitle(getString(R.string.sort_by))
                                              .setSingleChoiceItems(new CharSequence[] {"Popular",
                                                                                        "Top Rated"},
                                                                    (sType ==
                                                                     TMDBUtils.LIST_TYPE.Popular
                                                                     ? 0
                                                                     : 1),
                                                                    new DialogInterface.OnClickListener() {
                                                                        @Override
                                                                        public void onClick (
                                                                                DialogInterface dialogInterface,
                                                                                int item) {
                                                                            setListType(item);
                                                                            refresh();
                                                                            dialogInterface.dismiss();
                                                                        }
                                                                    }).create()).show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick (Movie movie) {
        Intent
                movieDetailIntent =
                new Intent(mContext, MovieDetailActivity.class);
        movieDetailIntent.putExtra(Intent.EXTRA_STREAM, movie);
        startActivity(movieDetailIntent);
    }

    private void fetchMovieList (TMDBUtils.LIST_TYPE type) {
        new FetchMovieListTask(mContext, mProgressBar, this).execute(type);
    }

    private void showErrorMessage () {
        mErrorTextView.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.INVISIBLE);
    }

    private void showResult () {
        mErrorTextView.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onFetchComplete (Movie[] movies) {
        if (movies == null || movies.length == 0) {
            showErrorMessage();
        } else {
            showResult();
            mMovieListAdapter.setList(movies);
        }
    }
}
