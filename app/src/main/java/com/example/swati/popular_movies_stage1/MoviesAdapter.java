package com.example.swati.popular_movies_stage1;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import Models.Movie;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MovieViewHolder> {
      public Movie[] moviePosters;

      private final MoviesAdapterOnClickHandler mClickHandler;

      public interface MoviesAdapterOnClickHandler{
          void onClick(int adapterPosition);
      }

      public MoviesAdapter(MoviesAdapterOnClickHandler clickHandler){
          mClickHandler = clickHandler;
      }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.movies_list;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;
        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder movieViewHolder, int position) {
        String movieBinder = moviePosters[position].getPoster();
        Picasso.get().load(movieBinder).placeholder(R.drawable.movies_placeholder)
                .error(R.drawable.movies_placeholder_error)
                .into(movieViewHolder.mMovieImageView);




    }

    @Override
    public int getItemCount() {
        if(null == moviePosters) return 0;
        return moviePosters.length;
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView mMovieImageView;

        public MovieViewHolder(View view){
            super(view);

            mMovieImageView = (ImageView) view.findViewById(R.id.movies_display);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view){
            int adpaterPosition = getAdapterPosition();
            mClickHandler.onClick(adpaterPosition);
        }


    }

    public void setMovieData(Movie[] movieData) {
        moviePosters = movieData;
        notifyDataSetChanged();
    }
}
