package main;

import java.io.File;
import java.util.List;

/**
 * Created by Shirobako on 10/1/2016.
 */
public interface Image {

    List<String> getTags();

    void setLocation(File location);

    File getLocation();

    String getRating();
}
