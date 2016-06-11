package com.example.android.popularmoviesstage1;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {


    PopularMovieAdapter popularMovieAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        popularMovieAdapter = new PopularMovieAdapter(getActivity(),new ArrayList<PopularMovies>());
        GridView gridView = (GridView) rootView.findViewById(R.id.movies_grid);


        gridView.setAdapter(popularMovieAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                PopularMovies movies = popularMovieAdapter.getItem(position);

                Intent movieDetailActivityIntent = new Intent(getActivity(), MovieDetailActivity.class);
                movieDetailActivityIntent.putExtra("MOVIE_DATA_KEY", movies);


                startActivity(movieDetailActivityIntent);

            }
        });

        return rootView;
    }

    public boolean isConnectingToInternet(){

        ConnectivityManager cm =
                (ConnectivityManager)getContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }

    public void onStart() {

        if(isConnectingToInternet()) {
            Log.v("Connected to internet","Connected to internet");
            super.onStart();
            FetchMoviesTask moviesTask = new FetchMoviesTask();
            moviesTask.execute();
        }
        else {

            super.onStart();
            Log.v("Connected to internet", " not Connected to internet");
            Toast.makeText(getContext(),"You are not connected to Internet!!" +
                    "Please connect to Internet and try again",Toast.LENGTH_SHORT).show();
           //System.exit(0);
        }


    }

    public class FetchMoviesTask extends AsyncTask<Void, Void, ArrayList<PopularMovies>> {

        private final String LOG_TAG = FetchMoviesTask.class.getSimpleName();

        @Override
        protected ArrayList<PopularMovies> doInBackground(Void... params) {

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            String movieDataString = null;

            String sortVal = getSortparam();

            try {
                final String MOVIE_BASE_URL =
                        "http://api.themoviedb.org/3/movie/"+sortVal+"?";

                final String API_KEY = "api_key";

                Uri uri = Uri.parse(MOVIE_BASE_URL).buildUpon()
                        .appendQueryParameter(API_KEY, BuildConfig.MOVIE_DB_API_KEY).build();

                URL url = new URL(uri.toString());

                Log.v(LOG_TAG, "URL>>>" + url);

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer stringBuffer = new StringBuffer();
                if (inputStream == null) {

                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;

                while ((line = reader.readLine()) != null) {
                    stringBuffer.append(line + "\n");
                }


                if (stringBuffer.length() == 0) {
                    return null;
                }
                movieDataString = stringBuffer.toString();

                Log.v(LOG_TAG, "movieDataString>>>" + movieDataString);

            } catch (IOException exception) {

                Log.e(LOG_TAG, "Error " + exception);

            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }

            try {
                return getMovieDataFromJson(movieDataString);
            } catch (JSONException e) {
                e.printStackTrace();
            }

             return null;
        }

        public String getSortparam() {

            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

            String sortType = preferences.getString(getString(R.string.pref_sort_key)
                    ,getString(R.string.pref_units_popular));

            Log.v(LOG_TAG,"sorttype "+sortType);

            return sortType;
        }

        public ArrayList<PopularMovies> getMovieDataFromJson(String movieData) throws JSONException {

            final String MOVIE_LIST_RESULT = "results";

            JSONObject jsonObject = new JSONObject(movieData);

            JSONArray movieArray = jsonObject.getJSONArray(MOVIE_LIST_RESULT);

            Log.v(LOG_TAG, "json array>>" + movieArray);

            ArrayList<PopularMovies> pictureCollection = new ArrayList<PopularMovies>();

            for (int i = 0; i < movieArray.length(); i++) {
                String posterPath;
                Integer movieId;
                String title;
                String thumbNail;
                String releaseDate;
                String overView;
                Double average;


                JSONObject data = movieArray.getJSONObject(i);

                posterPath = data.getString("poster_path");
                movieId = data.getInt("id");
                title = data.getString("title");
                thumbNail = data.getString("backdrop_path");
                releaseDate = data.getString("release_date");
                overView = data.getString("overview");
                average = data.getDouble("vote_average");

                String avgRating = average.toString().concat("/10");


                String date[] = releaseDate.split("-");
                String year = date[0];
                //String p2 = date[1];

                Log.v(LOG_TAG,"average data>>"+average);
                PopularMovies movies = new PopularMovies(movieId,posterPath, title, thumbNail, overView, avgRating, year);



                Log.v(LOG_TAG, "image id>>" + movieId);
                Log.v(LOG_TAG, "Movies data>>" + movies);
                pictureCollection.add(movies);
            }


            return pictureCollection;
        }


        @Override
        protected void onPostExecute(ArrayList<PopularMovies> result) {
            super.onPostExecute(result);

            if (result != null) {
                popularMovieAdapter.clear();
                popularMovieAdapter.addAll(result);

            }
        }
    }


}
