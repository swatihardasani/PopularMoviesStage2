package com.example.swati.popular_movies_stage1.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.swati.popular_movies_stage1.data.FavoriteMoviesContract.AddFavoriteMovies;

public class FavoritesMoviesDBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "favoriteMovies.db";

    private static final int DATABASE_VERSION = 1;

    public FavoritesMoviesDBHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        final String SQL_CREATE_FAVORITE_MOVIE_TABLE =
                "CREATE TABLE " + AddFavoriteMovies.TABLE_NAME + "(" +
                        AddFavoriteMovies._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                        AddFavoriteMovies.COLUMN_MOVIES_ID + " INTEGER NOT NULL, " +
                        AddFavoriteMovies.COLUMN_MOVIES_NAME + " TEXT NOT NULL," +
                        AddFavoriteMovies.COLUMN_MOVIES_POSTER + " TEXT NOT NULL," +
                        AddFavoriteMovies.COLUMN_MOVIES_USER_RATING + " TEXT NOT NULL," +
                        AddFavoriteMovies.COLUMN_MOVIES_RELEASE_DATE + " TEXT NOT NULL," +
                        AddFavoriteMovies.COLUMN_MOVIES_OVERVIEW + " TEXT NOT NULL" + "); ";

        db.execSQL(SQL_CREATE_FAVORITE_MOVIE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + AddFavoriteMovies.TABLE_NAME);
        onCreate(db);

    }
}
