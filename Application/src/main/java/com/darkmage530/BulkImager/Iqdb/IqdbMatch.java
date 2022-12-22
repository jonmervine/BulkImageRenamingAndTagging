package com.darkmage530.BulkImager.Iqdb;

import com.darkmage530.BulkImager.ImageRating;
import com.darkmage530.BulkImager.ImageDimensions;

/**
 * Created by DarkMage530 on 3/5/2017. For BulkImageRenamingAndTagging
 * Current User Shirobako
 */
//package-private
class IqdbMatch {

    private ImageRating rating;
    private ImageDimensions ratios;
    private String url;
    private String rawTags;
    private String sourceName;
    private int similarity;
    private IqdbMatchType matchType;

    public IqdbMatch(IqdbMatchType matchType, String sourceName, ImageRating rating, ImageDimensions resolution, String url, String rawTags, int similarity) {
        this.rating = rating;
        this.ratios = resolution;
        this.url = url;
        this.rawTags = rawTags;
        this.sourceName = sourceName;
        this.similarity = similarity;
        this.matchType = matchType;
    }

    public IqdbMatchType getMatchType() { return matchType; }

    public String getSource() { return sourceName; }

    public ImageRating getRating() { return rating; }

    public ImageDimensions getRatios() {
        return ratios;
    }

    public String getUrl() {
        return url;
    }

    public String getRawTags() {
        return rawTags;
    }

    public int getSimilarity() { return similarity; }
}
