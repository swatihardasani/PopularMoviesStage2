package com.example.swati.popular_movies_stage1.utilities;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import Models.Movie;

public class JSONMovieUtils {
    public static Movie[] getMovieInformationFromJSON(Context context, String movieStr)
            throws JSONException {


        final String Base_URL = "https://image.tmdb.org/t/p/";

        final String URl_Results = "results";
        final String title = "title";
        final String poster_path = "poster_path";
        final String overView = "overview";
        final String user_rating = "vote_average";
        final String release_date = "release_date";


         Movie[] parsedMovieData;

        JSONObject movieJSON = new JSONObject(movieStr);

        JSONArray movieList = movieJSON.getJSONArray(URl_Results);

        parsedMovieData = new Movie[movieList.length()];

        for (int i = 0; i < movieList.length(); i++) {
            Movie movie = new Movie();

            JSONObject movieObj = movieList.getJSONObject(i);
            String movie_title = movieObj.getString(title);
            movie.setNameOfMovie(movie_title);

            String posterPath = movieObj.getString(poster_path);
            movie.setPoster(Base_URL + "w342" + "/"+ posterPath);

            String overview = movieObj.getString(overView);
            movie.setPlotSynopsis(overview);

            String userRating = movieObj.getString(user_rating);
            movie.setUserRating(userRating);

            String releaseDate = movieObj.getString(release_date);
            movie.setReleaseDate(releaseDate);

            parsedMovieData[i] = movie;


        }

        return parsedMovieData;


    }

}
