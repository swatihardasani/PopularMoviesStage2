package com.example.swati.popular_movies_stage1;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.swati.popular_movies_stage1.data.FavoriteMoviesContract;
import com.example.swati.popular_movies_stage1.utilities.JSONMovieUtils;
import com.example.swati.popular_movies_stage1.utilities.MovieUtils;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import Models.Movie;

public class MainActivity extends AppCompatActivity implements MoviesAdapter.MoviesAdapterOnClickHandler {
    private RecyclerView mRecyclerView;

    private MoviesAdapter mMoviesAdapter;

    private TextView mErrorMessageDisplay;

    private ProgressBar mLoadingIndicator;

    String sortMovies = "api_key";

    Movie[] simpleJSONMovieData;

    String sortBy = "popular";

    int numOfColumns = 2;

    boolean isConnected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_movies);
        mErrorMessageDisplay = (TextView) findViewById(R.id.tv_error_message_display);
        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);

        GridLayoutManager layoutManager = new GridLayoutManager(this, numOfColumns);

        mMoviesAdapter = new MoviesAdapter(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mMoviesAdapter);
        mRecyclerView.setHasFixedSize(true);

        loadMovieData();
    }

    @Override
    public void onClick(int adapterPosition){
        Context context = this;
        Class destinationClass = DetailActivity.class;

        Intent intentToStartDetailActivity = new Intent(context, destinationClass);

        intentToStartDetailActivity.putExtra(DetailActivity.EXTRA_POSITION, adapterPosition);
        intentToStartDetailActivity.putExtra("title", simpleJSONMovieData[adapterPosition].getNameOfMovie());
        intentToStartDetailActivity.putExtra("poster", simpleJSONMovieData[adapterPosition].getPoster());
        intentToStartDetailActivity.putExtra("overview", simpleJSONMovieData[adapterPosition].getPlotSynopsis());
        intentToStartDetailActivity.putExtra("userRating", simpleJSONMovieData[adapterPosition].getUserRating());
        intentToStartDetailActivity.putExtra("releaseDate",simpleJSONMovieData[adapterPosition].getReleaseDate());
        intentToStartDetailActivity.putExtra("id", simpleJSONMovieData[adapterPosition].getId());

        startActivity(intentToStartDetailActivity);

    }

    private void loadMovieData(){
        //showMovieDataView();

        String ApiKey = sortMovies;
        String SortByFromWeb = sortBy;
        if(isConnected()) {
            new FetchMovieData().execute(SortByFromWeb, ApiKey);
        }
        else{
            showErrorMessage();
            Toast.makeText(getApplicationContext(),"No Internet Connection", Toast.LENGTH_LONG).show();
        }

    }

    private void loadFavoriteMovieData(){
        // algorithm
        // sql reading

        List<Movie> movieList = new ArrayList();
        Context context = getApplicationContext();
        ContentResolver contentResolver = context.getContentResolver();
        Cursor c = contentResolver.query(FavoriteMoviesContract.AddFavoriteMovies.CONTENT_URI,null,null,null,null);
        if (c.moveToFirst()) {
            do{
                int movieId = c.getInt(c.getColumnIndex((FavoriteMoviesContract.AddFavoriteMovies.COLUMN_MOVIES_ID)));
                String movieName = c.getString(c.getColumnIndex(FavoriteMoviesContract.AddFavoriteMovies.COLUMN_MOVIES_NAME));
                String moviePoster = c.getString(c.getColumnIndex(FavoriteMoviesContract.AddFavoriteMovies.COLUMN_MOVIES_POSTER));
                String movieUserRating = c.getString(c.getColumnIndex(FavoriteMoviesContract.AddFavoriteMovies.COLUMN_MOVIES_USER_RATING));
                String movieReleaseDate = c.getString(c.getColumnIndex(FavoriteMoviesContract.AddFavoriteMovies.COLUMN_MOVIES_RELEASE_DATE));
                String movieOverview = c.getString(c.getColumnIndex(FavoriteMoviesContract.AddFavoriteMovies.COLUMN_MOVIES_OVERVIEW));

                movieList.add(new Movie(movieId, movieName, moviePoster, movieReleaseDate, movieOverview, movieUserRating));

            } while (c.moveToNext());
        }

       simpleJSONMovieData = movieList.toArray(new Movie[movieList.size()]);


        mMoviesAdapter.setMovieData(simpleJSONMovieData);
    }


    private void showMovieDataView() {
        /* First, make sure the error is invisible */
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        // COMPLETED (44) Show mRecyclerView, not mWeatherTextView
        /* Then, make sure the weather com.example.swati.popular_movies_stage1.data is visible */
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    private void showErrorMessage() {
        // COMPLETED (44) Hide mRecyclerView, not mWeatherTextView
        /* First, hide the currently visible com.example.swati.popular_movies_stage1.data */
        mRecyclerView.setVisibility(View.INVISIBLE);
        /* Then, show the error */
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
    }

    public class FetchMovieData extends AsyncTask<String, Void, Movie[]> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected Movie[] doInBackground(String... params){
            if(params.length == 0){
                return null;
            }

            String sort_by = params[0];
            String api_key = params[1];
            URL moviesRequestURL = MovieUtils.buildUrl(sort_by,api_key);
            try{
                String jsonMovieResponse = MovieUtils.getResponseFromHttpUrl(moviesRequestURL);
                simpleJSONMovieData = JSONMovieUtils
                        .getMovieInformationFromJSON(MainActivity.this, jsonMovieResponse);

                return simpleJSONMovieData;
            }
            catch (Exception e){
                e.printStackTrace();
                return null;
            }
        }
        @Override
        protected void onPostExecute(Movie[] movieData) {
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            if (movieData != null) {
                showMovieDataView();
                // COMPLETED (45) Instead of iterating through every string, use mForecastAdapter.setWeatherData and pass in the weather com.example.swati.popular_movies_stage1.data
                mMoviesAdapter.setMovieData(movieData);
            } else {
                showErrorMessage();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.sort_options, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.sort_popular_movies){
            sortBy = "popular";
            loadMovieData();
            return true;
        }

        if(id == R.id.sort_topRated_movies) {
            sortBy = "top_rated";
            loadMovieData();
            return true;
        }

        // todo: add favorite sort
        if(id == R.id.sort_favorite_movies){
            sortBy = "favorite";
            loadFavoriteMovieData();
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    public boolean isConnected() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        isConnected = netInfo != null && netInfo.isConnected();
        return isConnected;
    }


}
