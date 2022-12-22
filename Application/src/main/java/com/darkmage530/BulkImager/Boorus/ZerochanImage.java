package com.darkmage530.BulkImager.Boorus;

import com.darkmage530.BulkImager.Image;
import com.darkmage530.BulkImager.Metadata.RatingSearch;

import java.io.File;
import java.util.List;

/**
 * Created by Shirobako on 10/1/2016.
 */
public class ZerochanImage implements Image {

    private RatingSearch ratingSearch;
    private List<String> parsedTags;
    private File location;

    public ZerochanImage(RatingSearch image) {
        this.ratingSearch = image;
//        this.parsedTags = Lists.newArrayList(image.getRawTags().split(","));
    }

    public List<String> getTags() {
        return parsedTags;
    }

    public void setLocation(File location) {
        this.location = location;
    }

    public File getLocation() {
        return location;
    }

    public String getRating() {
        return "";//ratingSearch.getRating();
    }
}
