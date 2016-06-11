package com.example.android.popularmoviesstage1;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by KUNAL on 24-02-2016.
 */
public class PopularMovieAdapter extends ArrayAdapter<PopularMovies> {

    private final String LOG_TAG = PopularMovieAdapter.class.getSimpleName();

    public PopularMovieAdapter(Context context, ArrayList<PopularMovies> popularMovies) {
        super(context, 0, popularMovies);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        PopularMovies popularMovies= getItem(position);
        Log.v(LOG_TAG,"position>>"+position);

        if(convertView==null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.movieitem, parent, false);
        }

        final String POSTER_BASE_URL = "http://image.tmdb.org/t/p/";
        final String SIZE_KEY = "w185/";
       // final String POSTER_PATH = "poster_path";
        String posterPath = popularMovies.imageUrl;
        String Url = POSTER_BASE_URL+SIZE_KEY+posterPath;

        Log.v(LOG_TAG, "url for poster is>> " + Url);

        ImageView imageView = (ImageView) convertView.findViewById(R.id.poster_image);

        Picasso.with(getContext())
                .load(Url)
                .placeholder(R.drawable.icon)
                .error(R.drawable.icon)
                .into(imageView);


        //Picasso.with(getContext()).load(Url).into(imageView);

        return convertView;


    }


}
