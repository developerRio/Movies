package com.originalstocksllc.testmovies;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Cache;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.originalstocksllc.testmovies.Adapters.AllMoviesAdapter;
import com.originalstocksllc.testmovies.Model.Movies;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "ViewAllMoviesResponse";
    StaggeredGridLayoutManager staggeredGridLayoutManager;
    private RecyclerView gridRecyclerView;
    private List<Movies> moviesList = new ArrayList<>();
    private List<String> directorsList;
    private List<String> genresList;
    private List<String> actorsList;
    private AllMoviesAdapter moviesAdapter;
    private LayoutAnimationController animationController;
    private SearchView mSearchView;
    private Spinner mSpinner;
    private ProgressBar mProgressBar;
    private List<String> emptyList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mProgressBar = findViewById(R.id.progress_circular_bar);
        mProgressBar.setVisibility(View.VISIBLE);
        mSpinner = findViewById(R.id.sort_spinner);

        loadMoviesData();

        emptyList.add("NA");


    }// onCreate Closes

    private void sortByTittle() {
        Collections.sort(moviesList, new Comparator<Movies>() {
            @Override
            public int compare(Movies obj1, Movies obj2) {
                return obj1.getTittle().compareToIgnoreCase(obj2.getTittle()); // To sort alphabetically
            }
        });
    }

    private void sortByRating() {
        Collections.sort(moviesList, new Comparator<Movies>() {
            @Override
            public int compare(Movies obj1, Movies obj2) {
                return obj2.getRating().compareToIgnoreCase(obj1.getRating()); // To sort numerically Descending order
            }
        });
    }

    private void sortByYear() {
        Collections.sort(moviesList, new Comparator<Movies>() {
            @Override
            public int compare(Movies obj1, Movies obj2) {
                return obj1.getYear().compareToIgnoreCase(obj2.getYear()); // To sort numerically Ascending order
            }
        });

    }

    private void loadMoviesData() {

        String url = "http://test.terasol.in/moviedata.json";

        StringRequest rootStringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    mProgressBar.setVisibility(View.GONE);

                    JSONArray rootArray = new JSONArray(response);

                    for (int i = 0; i < rootArray.length(); i++) {
                        JSONObject movieListJSON = rootArray.getJSONObject(i);

                        Movies movies = new Movies(i);
                        movies.setYear(movieListJSON.getString("year"));
                        movies.setTittle(movieListJSON.getString("title"));

                        String year = movieListJSON.getString("year");
                        String tittle = movieListJSON.getString("title");

                        JSONObject infoObject = movieListJSON.getJSONObject("info");
                        if (!infoObject.isNull("release_date")) {
                            movies.setReleaseDate(infoObject.getString("release_date"));
                            String releaseDate = infoObject.getString("release_date");

                            // Log.i(TAG, "onResponse: " + releaseDate);
                        } else {
                            movies.setReleaseDate("Not Available");
                        }
                        if (!infoObject.isNull("rating")) {
                            movies.setRating(infoObject.getString("rating"));
                            String rating = infoObject.getString("rating");
                            // Log.i(TAG, "onResponse: " + rating);
                        } else {
                            movies.setRating("Not Available");
                        }

                        if (!infoObject.isNull("image_url")) {
                            movies.setImageUrl(infoObject.getString("image_url"));
                            String ImageUrl = infoObject.getString("image_url");
                            // Log.i(TAG, "onResponse: " + ImageUrl);

                        } else {
                            movies.setImageUrl(String.valueOf(R.drawable.mountain));
                        }
                        if (!infoObject.isNull("plot")) {
                            movies.setPlotDescription(infoObject.getString("plot"));
                            String plot = infoObject.getString("plot");
                            // Log.i(TAG, "onResponse: " + plot);

                        } else {
                            movies.setPlotDescription("Not Available");
                        }
                        if (!infoObject.isNull("rank")) {
                            movies.setRank(infoObject.getString("rank"));
                            String Rank = infoObject.getString("rank");
                            // Log.i(TAG, "onResponse: " + Rank);
                        } else {
                            movies.setRank("Not Available");
                        }

                        if (!infoObject.isNull("running_time_secs")) {
                            movies.setDuration(infoObject.getString("running_time_secs"));
                            movies.setDuration(setTimeInMin(infoObject.getString("running_time_secs")));
                            String duration = infoObject.getString("running_time_secs");
                            //  Log.i(TAG, "onResponse: " + duration);

                        } else {
                            movies.setDuration("150+");
                        }

                        if (!infoObject.isNull("directors")) {
                            JSONArray directorsArray = infoObject.getJSONArray("directors");
                            for (int j = 0; j < directorsArray.length(); j++) {
                                String directors = directorsArray.getString(j);
                                Log.i(TAG, "onResponse: Directors: " + directors);
                                directorsList = new ArrayList<>();
                                directorsList.add(directors);
                                //  Log.i(TAG, "onResponse: DirectorsList: " + directorsList);
                                movies.setDirectors(directorsList);

                            }
                        } else {
                            movies.setDirectors(emptyList);
                        }

                        if (!infoObject.isNull("genres")) {
                            JSONArray genresArray = infoObject.getJSONArray("genres");
                            for (int j = 0; j < genresArray.length(); j++) {
                                String genres = genresArray.getString(j);
                                genresList = new ArrayList<>();
                                genresList.add(genres);
                                // Log.i(TAG, "onResponse: DirectorsList: " + genresList);
                                movies.setGenres(genresList);
                            }
                        } else {
                            movies.setGenres(emptyList);
                        }

                        if (!infoObject.isNull("actors")) {
                            JSONArray actorsArray = infoObject.getJSONArray("actors");
                            for (int j = 0; j < actorsArray.length(); j++) {
                                String actors = actorsArray.getString(j);
                                //  Log.i(TAG, "onResponse: Actors: " + actors);
                                actorsList = new ArrayList<>();
                                actorsList.add(actors);
                                //  Log.i(TAG, "onResponse: DirectorsList: " + actorsList);
                                movies.setActors(actorsList);
                            }
                        } else {
                            movies.setActors(emptyList);
                        }

                        moviesList.add(movies);
                    }

                    setupRecyclerContent(moviesList);

                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        MySingleton.getInstance(this).addToRequestQueue(rootStringRequest);

        CacheRequest cacheRequest = new CacheRequest(0, url, new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse response) {

                try {

                    JSONArray rootArray = new JSONArray(response);
                    for (int i = 0; i < rootArray.length(); i++) {
                        JSONObject movieListJSON = rootArray.getJSONObject(i);

                        Movies movies = new Movies(i);
                        movies.setYear(movieListJSON.getString("year"));
                        movies.setTittle(movieListJSON.getString("title"));

                        String year = movieListJSON.getString("year");
                        String tittle = movieListJSON.getString("title");

                        JSONObject infoObject = movieListJSON.getJSONObject("info");
                        if (!infoObject.isNull("release_date")) {
                            movies.setReleaseDate(infoObject.getString("release_date"));
                            String releaseDate = infoObject.getString("release_date");

                            // Log.i(TAG, "onResponse: " + releaseDate);
                        } else {
                            movies.setReleaseDate("Not Available");
                        }
                        if (!infoObject.isNull("rating")) {
                            movies.setRating(infoObject.getString("rating"));
                            String rating = infoObject.getString("rating");
                            // Log.i(TAG, "onResponse: " + rating);
                        } else {
                            movies.setRating("Not Available");
                        }

                        if (!infoObject.isNull("image_url")) {
                            movies.setImageUrl(infoObject.getString("image_url"));
                            String ImageUrl = infoObject.getString("image_url");
                            // Log.i(TAG, "onResponse: " + ImageUrl);

                        } else {
                            movies.setImageUrl(String.valueOf(R.drawable.mountain));
                        }
                        if (!infoObject.isNull("plot")) {
                            movies.setPlotDescription(infoObject.getString("plot"));
                            String plot = infoObject.getString("plot");
                            // Log.i(TAG, "onResponse: " + plot);

                        } else {
                            movies.setPlotDescription("Not Available");
                        }
                        if (!infoObject.isNull("rank")) {
                            movies.setRank(infoObject.getString("rank"));
                            String Rank = infoObject.getString("rank");
                            // Log.i(TAG, "onResponse: " + Rank);
                        } else {
                            movies.setRank("Not Available");
                        }

                        if (!infoObject.isNull("running_time_secs")) {
                            movies.setDuration(infoObject.getString("running_time_secs"));
                            movies.setDuration(setTimeInMin(infoObject.getString("running_time_secs")));
                            String duration = infoObject.getString("running_time_secs");
                            //  Log.i(TAG, "onResponse: " + duration);

                        } else {
                            movies.setDuration("150+");
                        }

                        if (!infoObject.isNull("directors")) {
                            JSONArray directorsArray = infoObject.getJSONArray("directors");
                            for (int j = 0; j < directorsArray.length(); j++) {
                                String directors = directorsArray.getString(j);
                                Log.i(TAG, "onResponse: Directors: " + directors);
                                directorsList = new ArrayList<>();
                                directorsList.add(directors);
                                //  Log.i(TAG, "onResponse: DirectorsList: " + directorsList);
                                movies.setDirectors(directorsList);

                            }
                        } else {
                            movies.setActors(emptyList);
                        }

                        if (!infoObject.isNull("genres")) {
                            JSONArray genresArray = infoObject.getJSONArray("genres");
                            for (int j = 0; j < genresArray.length(); j++) {
                                String genres = genresArray.getString(j);
                                genresList = new ArrayList<>();
                                genresList.add(genres);
                                // Log.i(TAG, "onResponse: DirectorsList: " + genresList);
                                movies.setGenres(genresList);
                            }
                        } else {
                            movies.setGenres(emptyList);
                        }

                        if (!infoObject.isNull("actors")) {
                            JSONArray actorsArray = infoObject.getJSONArray("actors");
                            for (int j = 0; j < actorsArray.length(); j++) {
                                String actors = actorsArray.getString(j);
                                //  Log.i(TAG, "onResponse: Actors: " + actors);
                                actorsList = new ArrayList<>();
                                actorsList.add(actors);
                                //  Log.i(TAG, "onResponse: DirectorsList: " + actorsList);
                                movies.setActors(actorsList);
                            }
                        } else {
                            movies.setActors(emptyList);
                        }

                        moviesList.add(movies);

                    }
                    setupRecyclerContent(moviesList);

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
        MySingleton.getInstance(this).addToRequestQueue(cacheRequest);
    }

    private String setTimeInMin(String duration) {
        long minutes = TimeUnit.SECONDS.toMinutes(Long.parseLong(duration));
        return String.valueOf(minutes);
    }

    private void setupRecyclerContent(final List<Movies> moviesList) {

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(MainActivity.this,
                R.array.status, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(adapter);

        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String selectedItem = mSpinner.getSelectedItem().toString();
                // Toast.makeText(MainActivity.this, "Selected : " + selectedItem, Toast.LENGTH_SHORT).show();

                if (position == 1) {
                    sortByYear();
                } else if (position == 2) {
                    sortByTittle();
                } else if (position == 0) {
                    sortByRating();
                }
                moviesAdapter.notifyDataSetChanged();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        animationController = AnimationUtils.loadLayoutAnimation(this, R.anim.layout_slide_fom_bottom_effect);
        mSearchView = findViewById(R.id.search_view_movies);

        moviesAdapter = new AllMoviesAdapter(this, moviesList);

        gridRecyclerView = findViewById(R.id.all_movies_recycler);
        gridRecyclerView.setHasFixedSize(true);
        // Staggered Layout
        staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL);
        staggeredGridLayoutManager.setSpanCount(2);
        gridRecyclerView.setLayoutManager(staggeredGridLayoutManager);
        gridRecyclerView.setAdapter(moviesAdapter);
        gridRecyclerView.setLayoutAnimation(animationController);
        gridRecyclerView.getAdapter().notifyDataSetChanged();
        gridRecyclerView.scheduleLayoutAnimation();

        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                List<Movies> filteredModelList = filter(moviesList, newText);
                moviesAdapter.setSearchFilters(filteredModelList);

                return true;
            }
        });
    }

    private List<Movies> filter(List<Movies> myMovieList, String query) {
        query = query.toLowerCase();

        final List<Movies> filteredMovieList = new ArrayList<>();
        for (Movies model : myMovieList) {
            String filterTittle = model.getTittle().toLowerCase();
            String filterRating = model.getRating().toLowerCase();
            String filterYear = model.getYear().toLowerCase();
            String filterGenres = model.getGenres().get(0);
            String filterDirectors = model.getDirectors().get(0);


            if (filterTittle.startsWith(query)
                    || filterRating.startsWith(query)
                    || filterYear.startsWith(query)
                    || filterGenres.startsWith(query)
                    || filterDirectors.startsWith(query)) {

                filteredMovieList.add(model);
            }

        }
        return filteredMovieList;
    }

    private class CacheRequest extends Request<NetworkResponse> {
        private final Response.Listener<NetworkResponse> mListener;
        private final Response.ErrorListener mErrorListener;

        public CacheRequest(int method, String url, Response.Listener<NetworkResponse> listener, Response.ErrorListener errorListener) {
            super(method, url, errorListener);
            this.mListener = listener;
            this.mErrorListener = errorListener;
        }

        @Override
        protected Response<NetworkResponse> parseNetworkResponse(NetworkResponse response) {
            Cache.Entry cacheEntry = HttpHeaderParser.parseCacheHeaders(response);
            if (cacheEntry == null) {
                cacheEntry = new Cache.Entry();
            }
            final long cacheHitButRefreshed = 3 * 60 * 1000; // in 3 minutes cache will be hit, but also refreshed on background
            final long cacheExpired = 24 * 60 * 60 * 1000; // in 24 hours this cache entry expires completely
            long now = System.currentTimeMillis();
            final long softExpire = now + cacheHitButRefreshed;
            final long ttl = now + cacheExpired;
            cacheEntry.data = response.data;
            cacheEntry.softTtl = softExpire;
            cacheEntry.ttl = ttl;
            String headerValue;
            headerValue = response.headers.get("Date");
            if (headerValue != null) {
                cacheEntry.serverDate = HttpHeaderParser.parseDateAsEpoch(headerValue);
            }
            headerValue = response.headers.get("Last-Modified");
            if (headerValue != null) {
                cacheEntry.lastModified = HttpHeaderParser.parseDateAsEpoch(headerValue);
            }
            cacheEntry.responseHeaders = response.headers;
            return Response.success(response, cacheEntry);
        }

        @Override
        protected void deliverResponse(NetworkResponse response) {
            mListener.onResponse(response);
        }

        @Override
        public void deliverError(VolleyError error) {
            mErrorListener.onErrorResponse(error);
        }
    }

}
