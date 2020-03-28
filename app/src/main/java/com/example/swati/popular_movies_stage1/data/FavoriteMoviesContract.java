package com.example.swati.popular_movies_stage1.data;

import android.net.Uri;
import android.provider.BaseColumns;

public class FavoriteMoviesContract {

    public static final String CONTENT_AUTHORITY = "com.example.swati.popular_movies_stage1";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_FAVORITEMOVIES = "favoriteMovies";

    public static final class AddFavoriteMovies implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_FAVORITEMOVIES)
                .build();

        public static final String TABLE_NAME = "favoriteMovies";
        public static final String COLUMN_MOVIES_ID = "movieId";
        public static final String COLUMN_MOVIES_NAME = "movieName";
        public static final String COLUMN_MOVIES_POSTER = "moviePoster";
        public static final String COLUMN_MOVIES_USER_RATING = "moviesRating";
        public static final String COLUMN_MOVIES_RELEASE_DATE = "moviesRelaeseDate";
        public static final String COLUMN_MOVIES_OVERVIEW = "moviesOverview";

    }

}
