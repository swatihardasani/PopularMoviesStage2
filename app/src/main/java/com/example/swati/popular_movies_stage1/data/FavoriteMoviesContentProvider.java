package com.example.swati.popular_movies_stage1.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.swati.popular_movies_stage1.data.FavoritesMoviesDBHelper;

public class FavoriteMoviesContentProvider extends ContentProvider {

    private static final int favoriteMovies = 200;

    private static final int favoriteMovies_ID = 201;

    private static final UriMatcher uriMatcher =  buildUriMatcher();

    private FavoritesMoviesDBHelper mFavoriteMovieHelper;

    private static UriMatcher buildUriMatcher(){
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(FavoriteMoviesContract.CONTENT_AUTHORITY, FavoriteMoviesContract.PATH_FAVORITEMOVIES, favoriteMovies);
        uriMatcher.addURI(FavoriteMoviesContract.CONTENT_AUTHORITY, FavoriteMoviesContract.PATH_FAVORITEMOVIES + "/#", favoriteMovies);
        return uriMatcher;
    }
    @Override
    public boolean onCreate() {
        Context context = getContext();
        mFavoriteMovieHelper = new FavoritesMoviesDBHelper(context);

        SQLiteDatabase db = mFavoriteMovieHelper.getWritableDatabase();
        return (db == null)? false:true;
    }

    @Nullable
    @Override
    public Cursor query(@ NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
       final SQLiteDatabase db = mFavoriteMovieHelper.getReadableDatabase();

       Cursor cursor;

       int match = uriMatcher.match(uri);
       switch(match){
           case favoriteMovies:
               cursor = db.query(FavoriteMoviesContract.AddFavoriteMovies.TABLE_NAME, projection, selection,
                       selectionArgs,null, null, sortOrder);
               break;

           case favoriteMovies_ID:
               String id = uri.getPathSegments().get(1);
               selection = FavoriteMoviesContract.AddFavoriteMovies._ID + "=?";
               selectionArgs = new String[]{id};

               cursor = db.query(FavoriteMoviesContract.AddFavoriteMovies.TABLE_NAME, projection, selection,
                       selectionArgs,null, null, sortOrder);

               break;

           default:
               throw new IllegalArgumentException("Cannot query unknown URI" + uri);
       }

       cursor.setNotificationUri(getContext().getContentResolver(), uri);

       return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        int match = uriMatcher.match(uri);

        switch (match) {
            case favoriteMovies:
                return "vnd.android.cursor.dir" + FavoriteMoviesContract.CONTENT_AUTHORITY + "/" + FavoriteMoviesContract.PATH_FAVORITEMOVIES;

            case favoriteMovies_ID:
                return "vnd.android.cursor.dir" + FavoriteMoviesContract.CONTENT_AUTHORITY + "/" + FavoriteMoviesContract.PATH_FAVORITEMOVIES;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI" + uri);


        }

    }


    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        final SQLiteDatabase db = mFavoriteMovieHelper.getReadableDatabase();
        int match = uriMatcher.match(uri);
        Uri returnUri;

        switch (match) {
            case favoriteMovies:
                long id = db.insert(FavoriteMoviesContract.AddFavoriteMovies.TABLE_NAME, null, values);
                if (id > 0) {
                    returnUri = ContentUris.withAppendedId(FavoriteMoviesContract.AddFavoriteMovies.CONTENT_URI, id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into" + uri);
                }

                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);

        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        final SQLiteDatabase db = mFavoriteMovieHelper.getReadableDatabase();
        int match = uriMatcher.match(uri);
        int favoritesDeleted;

        switch (match) {
            case favoriteMovies:
                favoritesDeleted = db.delete(FavoriteMoviesContract.AddFavoriteMovies.TABLE_NAME, selection, selectionArgs);
                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (favoritesDeleted != 0){
            getContext().getContentResolver().notifyChange(uri,null);
        }
        return favoritesDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        int favoriteUpdated;
        int match = uriMatcher.match(uri);

        switch (match){
            case favoriteMovies_ID:
                String id = uri.getPathSegments().get(1);
                favoriteUpdated = mFavoriteMovieHelper.getWritableDatabase()
                        .update(FavoriteMoviesContract.AddFavoriteMovies.TABLE_NAME, values, "_id=?", new String[]{id});
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (favoriteUpdated != 0){
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return favoriteUpdated;
    }
}
