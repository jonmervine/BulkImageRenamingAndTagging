package main.BooruScraper.Boorus;

import com.google.common.collect.Lists;
import main.Image;
import main.BooruScraper.IqdbImage;

import java.io.File;
import java.util.List;

/**
 * Created by Shirobako on 10/1/2016.
 */
public class ZerochanImage implements Image {

    private IqdbImage iqdbImage;
    private List<String> parsedTags;
    private File location;

    public ZerochanImage(IqdbImage image) {
        this.iqdbImage = image;
        this.parsedTags = Lists.newArrayList(image.getRawTags().split(","));
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
