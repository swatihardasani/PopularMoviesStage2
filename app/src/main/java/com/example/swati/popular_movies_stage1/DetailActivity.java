package com.example.swati.popular_movies_stage1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {

    public static final String EXTRA_POSITION = "extra_position";
    private static final int DEFAULT_POSITION = -1;

    private TextView mMovieTitle;
    private ImageView mMoviePoster;
    private TextView mPlotSynposis;
    private TextView mUserRating;
    private TextView mReleaseDate;

    String[] movieObj;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mMovieTitle = (TextView) findViewById(R.id.movieTitle_tv);
        mMoviePoster = (ImageView) findViewById(R.id.poster_iv);
        mPlotSynposis = (TextView) findViewById(R.id.plot_synopsis_tv);
        mUserRating = (TextView) findViewById(R.id.user_rating_tv);
        mReleaseDate = (TextView) findViewById(R.id.release_date_tv);

        Intent intent = getIntent();
        if(intent == null){
            closeOnError();
        }
        int adapterPosition = intent.getIntExtra(EXTRA_POSITION, DEFAULT_POSITION);
        if(adapterPosition == DEFAULT_POSITION){
            closeOnError();
            return;
        }

        String title = getIntent().getStringExtra("title");
        mMovieTitle.setText(title);

        String poster = getIntent().getStringExtra("poster");
        Picasso.get().load(poster).placeholder(R.drawable.movies_placeholder)
                .error(R.drawable.movies_placeholder_error)
                .into(mMoviePoster);

        String overview = getIntent().getStringExtra("overview");
        mPlotSynposis.setText(overview);

        String userRating = getIntent().getStringExtra("userRating");
        mUserRating.setText(userRating);

        String releaseDate = getIntent().getStringExtra("releaseDate");
        mReleaseDate.setText(releaseDate);














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


}
