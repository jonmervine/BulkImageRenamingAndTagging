package DarkMage530.BulkImager.Boorus;

import DarkMage530.BulkImager.Image;
import DarkMage530.BulkImager.Metadata.RatingSearch;

import java.io.File;
import java.util.List;

/**
 * Created by Shirobako on 10/1/2016.
 */
public class DanbooruImage implements Image {

    private RatingSearch ratingSearch;
    private List<String> parsedTags;
    private File location;

    public DanbooruImage(RatingSearch image) {
        this.ratingSearch = image;
//        this.parsedTags = Lists.newArrayList(image.getRawTags().split(" "));
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
