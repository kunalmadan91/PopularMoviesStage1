package com.example.android.popularmoviesstage1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * A placeholder fragment containing a simple view.
 */
public class MovieDetailActivityFragment extends Fragment {

    private final String LOG_TAG = MovieDetailActivity.class.getSimpleName();

    public MovieDetailActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Intent intent = getIntent();
        // Movie movie = getArguments().getParcelable("Movie");
        View rootView = inflater.inflate(R.layout.fragment_movie_detail, container, false);
        Intent intent = getActivity().getIntent();

        PopularMovies movie = null;
        if(intent!= null ) {
            movie = intent.getExtras().getParcelable("MOVIE_DATA_KEY");
        }


        String thumbNailUrl = movie.thumbNail;
        String title = movie.title;
        String releaseDate = movie.releaseDate;
        Integer movieId = movie.movieId;
        String average = movie.average;
        String overView = movie.overView;

        TextView titleView  = (TextView) rootView.findViewById(R.id.detail_title);
        TextView release = (TextView) rootView.findViewById(R.id.detail_date);
        ImageView imageView = (ImageView) rootView.findViewById(R.id.detail_image);
        TextView avg = (TextView) rootView.findViewById(R.id.detail_rating);
        TextView summary = (TextView) rootView.findViewById(R.id.detail_summary);



        //avg.setText(average);

        avg.setText(average);
        titleView.setText(title);
        summary.setText(overView);
        release.setText(releaseDate);

        final String THUMBNAIL_BASE_URL = "http://image.tmdb.org/t/p/w185/";
       // final String SIZE_KEY = "w185";
       // final String POSTER_PATH = "poster_path";

        String url = THUMBNAIL_BASE_URL.concat(thumbNailUrl);



        Picasso.with(getContext()).load(url).into(imageView);
        //imageView.setImageResource();


        Picasso.with(getContext())
                .load(url)
                .placeholder(R.drawable.icon)
                .error(R.drawable.icon)
                .into(imageView);


        //  titleview.setText(title);

        Log.v(LOG_TAG,"movie detal data "+thumbNailUrl+title+releaseDate);


        // Log.v(LOG_TAG,"");

        return rootView;
    }


}
