package com.example.lab4;

import javafx.beans.property.SimpleStringProperty;

public class Movie {
    private final SimpleStringProperty title;
    private final SimpleStringProperty genre;
    private final SimpleStringProperty releaseYear;
    private final SimpleStringProperty posterUrl;

    public Movie(String title, String genre, String releaseYear, String posterUrl) {
        this.title = new SimpleStringProperty(title);
        this.genre = new SimpleStringProperty(genre);
        this.releaseYear = new SimpleStringProperty(releaseYear);
        this.posterUrl = new SimpleStringProperty(posterUrl);
    }

    public String getTitle() { return title.get(); }
    public String getGenre() { return genre.get(); }
    public String getReleaseYear() { return releaseYear.get(); }
    public String getPosterUrl() { return posterUrl.get(); }
}