package com.example.swati.popular_movies_stage1;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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

import com.example.swati.popular_movies_stage1.utilities.JSONMovieUtils;
import com.example.swati.popular_movies_stage1.utilities.MovieUtils;
import java.net.URL;

import Models.Movie;

public class MainActivity extends AppCompatActivity implements MoviesAdapter.MoviesAdapterOnClickHandler {
    private RecyclerView mRecyclerView;

    private MoviesAdapter mMoviesAdapter;

    private TextView mErrorMessageDisplay;

    private ProgressBar mLoadingIndicator;

    String sortMovies = "373df7aa401df4855afbd8d2f2ed83ad";

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


    private void showMovieDataView() {
        /* First, make sure the error is invisible */
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        // COMPLETED (44) Show mRecyclerView, not mWeatherTextView
        /* Then, make sure the weather data is visible */
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    private void showErrorMessage() {
        // COMPLETED (44) Hide mRecyclerView, not mWeatherTextView
        /* First, hide the currently visible data */
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
                // COMPLETED (45) Instead of iterating through every string, use mForecastAdapter.setWeatherData and pass in the weather data
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
