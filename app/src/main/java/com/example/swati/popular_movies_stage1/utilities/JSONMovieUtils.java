package com.example.swati.popular_movies_stage1.utilities;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import Models.Movie;
import Models.Reviews;
import Models.Trailer;

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
        final String id = "id";


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

            int movie_id = movieObj.getInt(id);
            movie.setId(movie_id);



            parsedMovieData[i] = movie;


        }

        return parsedMovieData;


    }

    public static Trailer[] getTrailerInformationFromJSON(Context context, String trailerStr)
            throws JSONException {

        Trailer[] parsedTrailerData;

        final String TRAILER_Results = "results";
        final String TRAILER_Name  = "name";
        final String TRAILER_Key = "key";

        JSONObject trailerJSON = new JSONObject(trailerStr);
        JSONArray trailerList = trailerJSON.getJSONArray(TRAILER_Results);
        parsedTrailerData = new Trailer[trailerList.length()];

        for(int i = 0; i < trailerList.length(); i++){
            Trailer trailer = new Trailer();

            JSONObject trailerObj = trailerList.getJSONObject(i);
            String trailer_key = trailerObj.getString(TRAILER_Key);
            trailer.setKey(trailer_key);

            String trailer_name = trailerObj.getString(TRAILER_Name);
            trailer.setName(trailer_name);

           parsedTrailerData[i] = trailer;

        }

        return parsedTrailerData;




    }

    public static Reviews[] getReviewsInformationFromJSON(Context context, String reviewStr)
            throws JSONException{

        Reviews[] parsedReviewData;

        final String REVIEW_Results = "results";
        final String REVIEW_author = "author";
        final String REVIEW_content = "content";

        JSONObject reviewJSON = new JSONObject(reviewStr);
        JSONArray reviewList = reviewJSON.getJSONArray(REVIEW_Results);
        parsedReviewData = new Reviews[reviewList.length()];

        for(int i = 0; i < reviewList.length(); i++){
            Reviews reviews = new Reviews();

            JSONObject reviewObj = reviewList.getJSONObject(i);
            String author = reviewObj.getString(REVIEW_author);
            reviews.setAuthor(author);

            String content = reviewObj.getString(REVIEW_content);
            reviews.setContent(content);

            parsedReviewData[i] = reviews;

        }
        return parsedReviewData;

    }



}
