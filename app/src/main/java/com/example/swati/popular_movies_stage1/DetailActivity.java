package com.example.swati.popular_movies_stage1;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.swati.popular_movies_stage1.data.FavoriteMoviesContentProvider;
import com.example.swati.popular_movies_stage1.data.FavoriteMoviesContract;
import com.example.swati.popular_movies_stage1.utilities.JSONMovieUtils;
import com.example.swati.popular_movies_stage1.utilities.MovieUtils;
import com.squareup.picasso.Picasso;

import com.example.swati.popular_movies_stage1.data.FavoriteMoviesContract.AddFavoriteMovies;

import java.net.URL;

import Models.Movie;
import Models.Reviews;
import Models.Trailer;

public class DetailActivity extends AppCompatActivity {

    public static final String EXTRA_POSITION = "extra_position";
    private static final int DEFAULT_POSITION = -1;

    private TextView mMovieTitle;
    private ImageView mMoviePoster;
    private TextView mPlotSynposis;
    private TextView mUserRating;
    private TextView mReleaseDate;


    private TrailerAdapter mTrailerAdapter;
    private TextView mTrailerErrorMessage;
    private TextView mReviewErrorMessage;

    private RecyclerView mTrailerRecyclerView;

    private RecyclerView mReviewRecyclerView;

    private ReviewAdapter mReviewAdapter;

    FloatingActionButton floatingActionButton;

    public static final int NEW_FAVORITES_REQUEST_CODE = 200;
    public static final int UPDATE_FAVORITES_REQUEST_CODE = 300;

    private int movieId;

    public String title;
    public String poster;
    public String overview;
    public String userRating;
    public String releaseDate;

    private String mSelectionClause;

    String[] mProjection = {
            AddFavoriteMovies._ID,
            AddFavoriteMovies.COLUMN_MOVIES_ID

    };

    private String[] mSelectionArgs = {""};



    String[] movieObj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mMovieTitle = (TextView) findViewById(R.id.movieTitle_tv);
        mMoviePoster = (ImageView) findViewById(R.id.poster_iv);
        mPlotSynposis = (TextView) findViewById(R.id.plot_synopsis_tv);
        mUserRating = (TextView) findViewById(R.id.user_rating_tv);
        mReleaseDate = (TextView) findViewById(R.id.release_date_tv);



        mTrailerErrorMessage = (TextView) findViewById(R.id.tv_error_message_display);


        mTrailerRecyclerView = (RecyclerView) findViewById(R.id.watch_trailer_rv);

        mTrailerRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mTrailerRecyclerView.setAdapter(mTrailerAdapter);


        mReviewErrorMessage = (TextView) findViewById(R.id.tv_error_message_display_reviews);


        mReviewRecyclerView = (RecyclerView) findViewById(R.id.reviews_rv);

        mReviewRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mReviewRecyclerView.setAdapter(mReviewAdapter);

        Intent intent = getIntent();
        if (intent == null) {
            closeOnError();
        }
        int adapterPosition = intent.getIntExtra(EXTRA_POSITION, DEFAULT_POSITION);
        if (adapterPosition == DEFAULT_POSITION) {
            closeOnError();
            return;
        }

        title = getIntent().getStringExtra("title");
        mMovieTitle.setText(title);

        poster = getIntent().getStringExtra("poster");
        Picasso.get().load(poster).placeholder(R.drawable.movies_placeholder)
                .error(R.drawable.movies_placeholder_error)
                .into(mMoviePoster);

        overview = getIntent().getStringExtra("overview");
        mPlotSynposis.setText(overview);

        userRating = getIntent().getStringExtra("userRating");
        mUserRating.setText(userRating);

        releaseDate = getIntent().getStringExtra("releaseDate");
        mReleaseDate.setText(releaseDate);

        movieId = getIntent().getIntExtra("id", 0);


        floatingActionButton = (FloatingActionButton) findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(isMovieFavorited(String.valueOf(movieId))){
                    Context context = getApplicationContext();
                    mSelectionClause = AddFavoriteMovies.COLUMN_MOVIES_ID + " LIKE ?";
                    String[] selectionArgs = new String[] {String.valueOf(movieId)};
                    getContentResolver().delete(AddFavoriteMovies.CONTENT_URI, mSelectionClause, selectionArgs);

                    Toast toast = Toast.makeText(context, "Removed From Favorites", Toast.LENGTH_SHORT);
                    toast.show();
                }
                else{
                    ContentValues values = new ContentValues();
                    Context context = getApplicationContext();

                    values.put(AddFavoriteMovies.COLUMN_MOVIES_ID, movieId);
                    values.put(AddFavoriteMovies.COLUMN_MOVIES_NAME, title);
                    values.put(AddFavoriteMovies.COLUMN_MOVIES_POSTER, poster);
                    values.put(AddFavoriteMovies.COLUMN_MOVIES_USER_RATING, userRating);
                    values.put(AddFavoriteMovies.COLUMN_MOVIES_RELEASE_DATE, releaseDate);
                    values.put(AddFavoriteMovies.COLUMN_MOVIES_OVERVIEW, overview);

                    ContentResolver contentResolver = context.getContentResolver();
                    contentResolver.insert(AddFavoriteMovies.CONTENT_URI, values);

                    Toast toast = Toast.makeText(context, "Add to Favorites", Toast.LENGTH_SHORT);
                    toast.show();

                }
            }
        });


        loadTrailerData();
        loadReviewsData();
    }


    private void loadTrailerData(){
        String trailerId = String.valueOf(movieId);
        new FetchTrailers().execute(trailerId);
    }

    private void loadReviewsData(){
        String reviewId = String.valueOf(movieId);
        new FetchReviews().execute(reviewId);
    }

    public class FetchTrailers extends AsyncTask<String, Void, Trailer[]>{

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
        }

        @Override
        protected Trailer[] doInBackground(String... params) {
            if(params.length == 0){
                return null;
            }
            URL movieRequestUrl = MovieUtils.buildTrailerUrl(movieId);
            try{
                String jsonTrailerResponse = MovieUtils.getResponseFromHttpUrl(movieRequestUrl);

                Trailer[] movieTrailersJSONData = JSONMovieUtils
                        .getTrailerInformationFromJSON(DetailActivity.this, jsonTrailerResponse);

                return movieTrailersJSONData;

            }
            catch(Exception e){
                e.printStackTrace();
                return null;
            }

        }

        @Override
        protected void onPostExecute(Trailer[] trailerData){
           if(trailerData != null) {
               mTrailerAdapter = new TrailerAdapter(trailerData, DetailActivity.this);
               mTrailerRecyclerView.setAdapter(mTrailerAdapter);
           }
           else{
               mTrailerErrorMessage.setVisibility(View.VISIBLE);
           }


        }
    }

    public class FetchReviews extends AsyncTask<String, Void, Reviews[]>{

        @Override
        protected void onPreExecute(){
            super.onPreExecute();


        }

        @Override
        protected Reviews[] doInBackground(String... params) {
            if (params.length == 0) {
                return null;
            }

            URL movieRequestUrl2 = MovieUtils.buildReviewUrl(movieId);
            try {
                String jsonReviewResponse = MovieUtils.getResponseFromHttpUrl(movieRequestUrl2);

                Reviews[] movieReviewsJSONData = JSONMovieUtils
                        .getReviewsInformationFromJSON(DetailActivity.this, jsonReviewResponse);


                return movieReviewsJSONData;


            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Reviews[] reviewData){
            if(reviewData != null){
                mReviewAdapter = new ReviewAdapter(reviewData);
                mReviewRecyclerView.setAdapter(mReviewAdapter);

            }
            else {
                mReviewErrorMessage.setVisibility(View.VISIBLE);
            }

        }


    }




    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.error_message, Toast.LENGTH_SHORT).show();
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean isMovieFavorited(String id){
        mSelectionClause = AddFavoriteMovies.COLUMN_MOVIES_ID + " = ?";
        mSelectionArgs[0] = id;
        Cursor mCursor = getContentResolver().query(
                FavoriteMoviesContract.AddFavoriteMovies.CONTENT_URI,
                mProjection,
                mSelectionClause,
                mSelectionArgs,
                null);

        if(mCursor.getCount() <= 0){
            mCursor.close();
            return false;
        }
        mCursor.close();
        return true;
    }






}
