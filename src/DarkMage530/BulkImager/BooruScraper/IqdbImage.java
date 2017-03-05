package DarkMage530.BulkImager.BooruScraper;

/**
 * Created by Shirobako on 9/3/2016.
 */
public class IqdbImage {

    private String rating;
    private String resolution;
    private String url;
    private String rawTags;
    private String source;

    public IqdbImage(String source, String rating, String resolution, String url, String rawTags) {
        this.rating = rating;
        this.resolution = resolution;
        this.url = url;
        this.rawTags = rawTags;
        this.source = source;
    }

    public String getSource() { return source; }

    public String getRating() {
        return rating;
    }

    public String getResolution() {
        return resolution;
    }

    public String getUrl() {
        return url;
    }

    public String getRawTags() {
        return rawTags;
    }
}
