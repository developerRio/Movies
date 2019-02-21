package com.originalstocksllc.testmovies.Fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.originalstocksllc.testmovies.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class DetailsFragment extends Fragment {

    private View nestedScrollView;
    private BottomSheetBehavior mSheetBehaviour;
    private TextView movieName, movieRating, movieDuration, movieDescription, movieReleaseDate, movieYear, movieGenres, moviesActors, movieDirectors;
    private ImageView posterImageView;


    public DetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_details, container, false);

        posterImageView = view.findViewById(R.id.movie_poster);
        movieName = view.findViewById(R.id.detail_movie_name);
        movieRating = view.findViewById(R.id.detail_movie_rating);
        movieDuration = view.findViewById(R.id.detail_movie_duration);
        movieDescription = view.findViewById(R.id.detail_movie_description);
        movieReleaseDate = view.findViewById(R.id.detail_movie_release_date);
        movieYear = view.findViewById(R.id.detail_movie_year);
        movieGenres = view.findViewById(R.id.detail_movie_genre);
        moviesActors = view.findViewById(R.id.detail_movie_actors);
        movieDirectors = view.findViewById(R.id.detail_movie_directors);

        nestedScrollView = view.findViewById(R.id.nested_movie_scroll);
        mSheetBehaviour = BottomSheetBehavior.from(nestedScrollView);
        mSheetBehaviour.setState(BottomSheetBehavior.STATE_HALF_EXPANDED);
        mSheetBehaviour.setPeekHeight(280);    //peek height

        // getting data from Bundle
        Bundle mData = this.getArguments();
        if (mData != null) {

            String name = mData.get("name").toString();
            String imageUrl = mData.get("image_url").toString();
            String rating = mData.get("rating").toString();
            String duration = mData.get("duration").toString();
            String description = mData.get("description").toString();
            String year = mData.get("year").toString();
            String release_date = mData.get("release_date").toString();

            ArrayList<String> genreList = mData.getStringArrayList("genres");
            ArrayList<String> actorsList = mData.getStringArrayList("actors");
            ArrayList<String> directorsList = mData.getStringArrayList("directors");

           /* Log.i("Detailed_Values", "onCreate: " + "\n" + name + "\n" + imageUrl + "\n" + rating + "\n" + duration
                    + "\n" + description + "\n" + year + "\n" + release_date + "\n" + genreList + "\n" + actorsList + "\n" + directorsList);*/


            movieName.setText(name);
            movieRating.setText(rating);
            movieDuration.setText(duration + " mins");
            movieDescription.setText(description);
            movieYear.setText("(" + year + ")");
            movieReleaseDate.setText("Released on : " + release_date);

            if (genreList != null) {
                movieGenres.setText("");
                for (int i = 0; i < genreList.size(); i++) {
                    movieGenres.append(genreList.get(i));
                }
            } else {
                movieGenres.append("NA");
            }

            if (actorsList != null) {
                if (!actorsList.isEmpty()) {
                    moviesActors.setText("");
                    for (int i = 0; i < actorsList.size(); i++) {
                        moviesActors.append("Actor(s): " + actorsList.get(i));
                    }
                } else {
                    moviesActors.append("NA");
                }
            }

            if (directorsList != null) {
                movieDirectors.setText("");
                for (int i = 0; i < directorsList.size(); i++) {
                    movieDirectors.append("Director(s): " + directorsList.get(i));
                }
            } else {
                movieDirectors.append("NA");
            }

            Glide.with(this).load(imageUrl)
                    .apply(new RequestOptions().centerCrop().placeholder(R.drawable.mountain))
                    .into(posterImageView);

        }

        mSheetBehaviour.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {

                String state = "";
                switch (newState) {
                    case BottomSheetBehavior.STATE_DRAGGING: {
                        mSheetBehaviour.setState(BottomSheetBehavior.STATE_DRAGGING);
                        state = "DRAGGING";
                        break;
                    }
                    case BottomSheetBehavior.STATE_SETTLING: {
                        state = "SETTLING";
                        break;
                    }
                    case BottomSheetBehavior.STATE_EXPANDED: {
                        mSheetBehaviour.setState(BottomSheetBehavior.STATE_EXPANDED);

                        state = "EXPANDED";
                        break;
                    }
                    case BottomSheetBehavior.STATE_COLLAPSED: {

                        state = "COLLAPSED";
                        break;
                    }
                    case BottomSheetBehavior.STATE_HALF_EXPANDED:
                        mSheetBehaviour.setState(BottomSheetBehavior.STATE_HALF_EXPANDED);
                        break;

                    case BottomSheetBehavior.STATE_HIDDEN: {
                        state = "HIDDEN";
                        break;
                    }
                }

            }

            @Override
            public void onSlide(@NonNull View view, float v) {

            }
        });


        return view;
    }

}
