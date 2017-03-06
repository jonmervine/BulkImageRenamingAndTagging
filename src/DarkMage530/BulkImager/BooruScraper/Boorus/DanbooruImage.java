package DarkMage530.BulkImager.BooruScraper.Boorus;

import com.google.common.collect.Lists;
import DarkMage530.BulkImager.Image;
import DarkMage530.BulkImager.IQDB.IqdbImage;

import java.io.File;
import java.util.List;

/**
 * Created by Shirobako on 10/1/2016.
 */
public class DanbooruImage implements Image {

    private IqdbImage iqdbImage;
    private List<String> parsedTags;
    private File location;

    public DanbooruImage(IqdbImage image) {
        this.iqdbImage = image;
        this.parsedTags = Lists.newArrayList(image.getRawTags().split(" "));
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
        return iqdbImage.getRating();
    }

}
