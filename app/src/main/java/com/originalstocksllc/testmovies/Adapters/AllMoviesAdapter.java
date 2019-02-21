package com.originalstocksllc.testmovies.Adapters;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.originalstocksllc.testmovies.Fragments.DetailsFragment;
import com.originalstocksllc.testmovies.Model.Movies;
import com.originalstocksllc.testmovies.R;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class AllMoviesAdapter extends RecyclerView.Adapter<AllMoviesAdapter.AllMoviesViewHolder> {

    private Context context;
    private List<Movies> moviesList;

    public AllMoviesAdapter(Context context, List<Movies> moviesList) {
        this.context = context;
        this.moviesList = moviesList;
    }

    @NonNull
    @Override
    public AllMoviesViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.all_moviesitem, viewGroup, false);
        return new AllMoviesViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final AllMoviesViewHolder holder, int pos) {

        Movies movies = moviesList.get(pos);

        holder.movieName.setText(movies.getTittle());
        holder.movieDuration.setText(movies.getDuration() + " minutes");
        Glide.with(context).load(movies.getImageUrl())
                .apply(new RequestOptions().placeholder(R.drawable.mountain))
                .into(holder.imagePoster);

        holder.mCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int pos = holder.getAdapterPosition();

                Bundle detailsIntent = new Bundle();
                for (int i = 0; i <= pos; i++) {
                    detailsIntent.putString("image_url", moviesList.get(pos).getImageUrl());
                    detailsIntent.putString("name", moviesList.get(pos).getTittle());
                    detailsIntent.putString("rating", moviesList.get(pos).getRating());
                    detailsIntent.putString("duration", moviesList.get(pos).getDuration());
                    detailsIntent.putString("description", moviesList.get(pos).getPlotDescription());
                    detailsIntent.putString("year", moviesList.get(pos).getYear());
                    detailsIntent.putString("release_date", moviesList.get(pos).getReleaseDate());
                    detailsIntent.putStringArrayList("genres", (ArrayList<String>) moviesList.get(pos).getGenres());
                    detailsIntent.putStringArrayList("actors", (ArrayList<String>) moviesList.get(pos).getActors());
                    detailsIntent.putStringArrayList("directors", (ArrayList<String>) moviesList.get(pos).getDirectors());

                }
                // Sending data to fragment
                DetailsFragment detailsFragment = new DetailsFragment();
                detailsFragment.setArguments(detailsIntent);
                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                activity.getSupportFragmentManager()
                        .beginTransaction()
                        .add(R.id.frame_frag, detailsFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }

    public void setSearchFilters(List<Movies> list) {
        moviesList = new ArrayList<>();
        moviesList.addAll(list);
        notifyDataSetChanged();
    }

    public class AllMoviesViewHolder extends RecyclerView.ViewHolder {

        ImageView imagePoster;
        TextView movieName, movieDuration;
        CardView mCardView;

        public AllMoviesViewHolder(@NonNull View itemView) {
            super(itemView);

            imagePoster = itemView.findViewById(R.id.all_image_poster);
            mCardView = itemView.findViewById(R.id.all_movies_card);
            movieName = itemView.findViewById(R.id.all_movie_name_text);
            movieDuration = itemView.findViewById(R.id.all_duration_text);
        }
    }
}
