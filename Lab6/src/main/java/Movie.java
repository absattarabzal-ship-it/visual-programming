package kz.atu.lab6;

public class Movie {
    private String title;
    private String genre;
    private int year;
    private double rating;
    private String imageUrl;

    public Movie(String title, String genre, int year, double rating, String imageUrl) {
        this.title = title;
        this.genre = genre;
        this.year = year;
        this.rating = rating;
        this.imageUrl = imageUrl;
    }

    public String getTitle() { return title; }
    public String getGenre() { return genre; }
    public int getYear() { return year; }
    public double getRating() { return rating; }
    public String getImageUrl() { return imageUrl; }
}