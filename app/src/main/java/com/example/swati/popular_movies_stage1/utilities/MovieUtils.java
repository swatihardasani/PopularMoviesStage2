package com.example.swati.popular_movies_stage1.utilities;

import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class MovieUtils {
    private static final String Movie_URL_Popular = "https://api.themoviedb.org/3/movie";



    private static final String Movie_Base_Url = Movie_URL_Popular;



    final static String PARAM_APIKey = "api_key";


    public static URL buildUrl(String sortByStr, String APIkeyStr){
        Uri builtUri = Uri.parse(Movie_Base_Url).buildUpon()
                .appendEncodedPath(sortByStr)
                .appendQueryParameter(PARAM_APIKey,APIkeyStr).build();



        URL url = null;
        try{
            url = new URL(builtUri.toString());
        }
        catch(MalformedURLException e){
            e.printStackTrace();
        }

        return url;
    }


    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try{
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if(hasInput){
                return scanner.next();
            }
            else{
                return null;
            }
        }
        finally{
            urlConnection.disconnect();
        }
    }
}
