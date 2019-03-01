package com.highlatencygames.laags.cinemo;

public class Movie {

    private String id;
    private String title;
    private String Description;
    private String videoName;
    private String imgName;
    private int Thumbnail;
    private int rating;

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public Movie(){

    }

    public Movie(String id, String title, String description, int thumbnail, int rating, String videoName) {
        this.id = id;
        this.title = title;
        this.Description = description;
        this.Thumbnail = thumbnail;
        this.rating = rating;
        this.videoName = videoName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVideoName() {
        return videoName;
    }

    public void setVideoName(String videoName) {
        this.videoName = videoName;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return Description;
    }

    public int getThumbnail() {
        return Thumbnail;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public void setThumbnail(int thumbnail) {
        Thumbnail = thumbnail;
    }

    public String getImgName() {
        return imgName;
    }

    public void setImgName(String imgName) {
        this.imgName = imgName;
    }
}
