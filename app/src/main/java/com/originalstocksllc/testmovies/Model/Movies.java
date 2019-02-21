package com.originalstocksllc.testmovies.Model;

import java.util.List;

public class Movies {

    private String year;
    private String tittle;
    private String imageUrl;
    private String releaseDate;
    private String plotDescription;
    private String rank;
    private String rating;
    private String duration;
    private List<String> directors;
    private List<String> genres;
    private List<String> actors;
    private int position;


    public Movies(int position) {
        this.position = position;
    }

    public List<String> getDirectors() {
        return directors;
    }

    public void setDirectors(List<String> directors) {
        this.directors = directors;
    }

    public List<String> getGenres() {
        return genres;
    }

    public void setGenres(List<String> genres) {
        this.genres = genres;
    }

    public List<String> getActors() {
        return actors;
    }

    public void setActors(List<String> actors) {
        this.actors = actors;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getTittle() {
        return tittle;
    }

    public void setTittle(String tittle) {
        this.tittle = tittle;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getPlotDescription() {
        return plotDescription;
    }

    public void setPlotDescription(String plotDescription) {
        this.plotDescription = plotDescription;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }
}
