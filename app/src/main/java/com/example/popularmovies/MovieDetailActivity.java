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
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.popularmovies.data.Movie;
import com.example.popularmovies.utilities.TMDBUtils;

public class MovieDetailActivity extends AppCompatActivity {

    private Context mContext;
    private Movie mMovieSelected;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        mContext = getApplicationContext();

        Intent receivedIntent = getIntent();
        if (receivedIntent == null || !receivedIntent.hasExtra(Intent.EXTRA_STREAM)) {
            showError();
        }
        mMovieSelected = (Movie) receivedIntent.getSerializableExtra(Intent.EXTRA_STREAM);
        if (mMovieSelected == null) {
            showError();
        }

        TMDBUtils.fetchImage(mContext,
                             ((ImageView) findViewById(R.id.iv_movie_backdrop)),
                             mMovieSelected.getBackdropPath(),
                             TMDBUtils.IMAGE_SIZE.w780);
        ((TextView) findViewById(R.id.tv_movie_title)).setText(mMovieSelected.getTitle());
        ((TextView) findViewById(R.id.tv_movie_release_date)).setText(mMovieSelected.getReleaseDate());
        ((TextView) findViewById(R.id.tv_movie_vote_average)).setText(Double.toString(mMovieSelected
                                                                                              .getVoteAverage()));
        ((TextView) findViewById(R.id.tv_movie_plot_synopsis)).setText(mMovieSelected.getOverview());
    }

    @Override
    public boolean onCreateOptionsMenu (Menu menu) {
        getMenuInflater().inflate(R.menu.detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_share:
                ShareCompat.IntentBuilder.from(this)
                                         .setType("text/plain")
                                         .setText(mMovieSelected.toString(mContext))
                                         .startChooser();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showError () {
        ((LinearLayout) findViewById(R.id.movie_details)).setVisibility(View.GONE);
        ((TextView) findViewById(R.id.error)).setVisibility(View.VISIBLE);
    }
}
