package com.example.swati.popular_movies_stage1;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.net.URI;

import Models.Trailer;

public class TrailerAdapter extends RecyclerView.Adapter <TrailerAdapter.TrailerViewHolder> {

    public Trailer[] mTrailerData;
    Context context;

    public static final String BASE_URL = "https://www.youtube.com/watch?v=";
    public TextView mTrailerList;

    public TrailerAdapter(Trailer[] trailer, Context mContext) {
        mTrailerData = trailer;
        context = mContext;
    }

    @NonNull
    @Override
    public TrailerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.trailer_list;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;
        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        return new TrailerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TrailerViewHolder trailerViewHolder, final int position) {
        String trailerToWatch = mTrailerData[position].getName();
        final String trailerKey = mTrailerData[position].getKey();
        trailerViewHolder.mTrailerList.setText(trailerToWatch);

        trailerViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(BASE_URL + trailerKey)));
            }
        });
    }

    @Override
    public int getItemCount() {
        return mTrailerData.length;
    }

    public class TrailerViewHolder extends RecyclerView.ViewHolder {

        public TextView mTrailerList;

        public TrailerViewHolder(View view) {
            super(view);
            mTrailerList = (TextView) view.findViewById(R.id.trailer_names_tv);
        }
    }
}
