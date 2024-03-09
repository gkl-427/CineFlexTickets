package com.example.Entity;
import java.sql.Date;

public class Movies {
    protected int movieid;
    protected String moviename;
    protected String description;
    protected String casting;
    protected String language;
    protected String genre;
    protected Date moviedate;

    public Movies() {
    }

    public Movies(int movieid, String moviename, String description, String casting, String language, String genre,Date moviedate) {
        this.movieid = movieid;
        this.moviename = moviename;
        this.description = description;
        this.casting = casting;
        this.language = language;
        this.genre = genre;
        this.moviedate = moviedate;
    }

    public int getMovieid() {
        return this.movieid;
    }

    public void setMovieid(int movieid) {
        this.movieid = movieid;
    }

    public String getMoviename() {
        return this.moviename;
    }

    public void setMoviename(String moviename) {
        this.moviename = moviename;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCasting() {
        return this.casting;
    }

    public void setCasting(String casting) {
        this.casting = casting;
    }

    public String getLanguage() {
        return this.language;
    }

    public Date getMoviedate() {
        return this.moviedate;
    }

    public void setMoviedate(Date moviedate) {
        this.moviedate = moviedate;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getGenre() {
        return this.genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    @Override
    public String toString() {
        return "{" +
            " movieid='" + getMovieid() + "'" +
            ", moviename='" + getMoviename() + "'" +
            ", description='" + getDescription() + "'" +
            ", casting='" + getCasting() + "'" +
            ", language='" + getLanguage() + "'" +
            ", genre='" + getGenre() + "'" +
            "}";
    }
}