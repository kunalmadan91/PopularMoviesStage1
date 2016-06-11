package com.example.android.popularmoviesstage1;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by KUNAL on 24-02-2016.
 */
public class PopularMovies implements Parcelable{

    Integer movieId;
    String imageUrl;
    String title;
    String thumbNail;
    String overView;
    String average;
    String releaseDate;

    public PopularMovies(int movieId,String imageUrl,String title,
                         String thumbNail,String overView, String average, String releaseDate) {
        this.movieId = movieId;
        this.imageUrl =imageUrl;
        this.title = title;
        this.thumbNail = thumbNail;
        this.overView = overView;
        this.average = average;
        this.releaseDate = releaseDate;
    }


    protected PopularMovies(Parcel in) {
        movieId = in.readInt();
        imageUrl = in.readString();
        title = in.readString();
        thumbNail = in.readString();
        overView = in.readString();
        average = in.readString();
        releaseDate = in.readString();
    }

    public static final Parcelable.Creator<PopularMovies> CREATOR = new Parcelable.Creator<PopularMovies>() {
        @Override
        public PopularMovies createFromParcel(Parcel in) {
            return new PopularMovies(in);
        }

        @Override
        public PopularMovies[] newArray(int size) {
            return new PopularMovies[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(movieId);
        dest.writeString(imageUrl);
        dest.writeString(title);
        dest.writeString(thumbNail);
        dest.writeString(overView);
        dest.writeString(average);
        dest.writeString(releaseDate);
    }
}
