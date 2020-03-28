package com.example.swati.popular_movies_stage1;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import Models.Reviews;
import Models.Trailer;


public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {

    public Reviews[] mReviewData;


    //public String[] reviewsArr = {"What makes ‘Parasite’ so satisfying is that it commits neither error. It’s an engrossing, stylish and near perfect movie, and its underlying themes go beyond merely pointing out class exploitation to challenge the logic of capital. Though he is often juggling a mosaic of characters, themes and social issues, Bong never eschews his anarchic impulses and dark humour. It’s a movie that should be seen as widely as possible, if only so that Bong Joon-ho gets more chances to make movies for modern audiences that badly need them. - Jake Watt",
                                   //"The working class and down on their luck Kim family struggle to make ends meet. When a friend of the son, Ki-Woo’s, who is an English tutor for the daughter in the wealthy Park family, has to leave his position, he recommends Ki-Woo for the job. Now having an in with the wealthy family, the Kims begin plotting the downfall of the current household servants and inserting themselves into those vacant positions, making them all gainfully employed and with money finally flowing into the household. But not everything is as it seems in the Park house or with their previous servants."};
    public ReviewAdapter(Reviews[] reviews){
        mReviewData = reviews;

    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.reviews_list;
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = layoutInflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately );
        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder reviewViewHolder, final int position) {
        String reviewsAuthor  = mReviewData[position].getAuthor();
        String reviewMovie = mReviewData[position].getContent();
        reviewViewHolder.mAuthor.setText(reviewsAuthor);
        reviewViewHolder.mReview.setText(reviewMovie);
    }



    @Override
    public int getItemCount() {
        if(null == mReviewData){
            return 0;
        }

        return mReviewData.length;
    }

    public class ReviewViewHolder extends RecyclerView.ViewHolder{

        public TextView mAuthor = null;
        public TextView mReview;

        public ReviewViewHolder(View view){
            super(view);
           mAuthor = (TextView) view.findViewById(R.id.tv_author);
           mReview = (TextView) view.findViewById(R.id.tv_review);

        }
    }




}
