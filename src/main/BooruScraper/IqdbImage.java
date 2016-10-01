package main.BooruScraper;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * Created by Shirobako on 9/3/2016.
 */
public class IqdbImage {

    private String rating;
    private String resolution;
    private String url;
    private List<String> tags;

    public IqdbImage(String rating, String resolution, String url, String[] tags) {
        this.rating = rating;
        this.resolution = resolution;
        this.url = url;
        this.tags = Lists.newArrayList(tags);
    }

    public String getRating() {
        return rating;
    }

    public String getResolution() {
        return resolution;
    }

    public String getUrl() {
        return url;
    }

    public List<String> getTags() {
        return tags;
    }

    public void cleanImage() {

    }

}
