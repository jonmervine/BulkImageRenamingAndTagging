package DarkMage530.BulkImager;

import org.springframework.stereotype.Component;

import java.io.File;
import java.util.Properties;

/**
 * Created by DarkMage530 on 5/21/2017. For BulkImageRenamingAndTagging
 * Current User Shirobako
 */
@Component
public class BirtConfiguration {

    private File scanLocation;
    private File endLocation;
    private boolean rename;
    private boolean tag;

    private boolean findWallpaper;
    private boolean defaultLocation;
    private File wallpaperLocationOverride;

    public void build(Properties prop) {
        scanLocation = new File(prop.getProperty("scan.location"));
        endLocation = new File(prop.getProperty("end.location"));
        rename = Boolean.valueOf(prop.getProperty("rename"));
        tag = Boolean.valueOf(prop.getProperty("tag"));
        findWallpaper = Boolean.valueOf(prop.getProperty("wallpaper.find"));
        defaultLocation = Boolean.valueOf(prop.getProperty("wallpaper.default.location"));
        wallpaperLocationOverride = new File(prop.getProperty("wallpaper.location.override"));
    }

    public File getScanLocation() {
        return scanLocation;
    }

    public File getEndLocation() {
        return endLocation;
    }

    public boolean isRename() {
        return rename;
    }

    public boolean isTag() {
        return tag;
    }

    public boolean isFindWallpaper() {
        return findWallpaper;
    }

    public boolean isDefaultLocation() {
        return defaultLocation;
    }

    public File getWallpaperLocationOverride() {
        return wallpaperLocationOverride;
    }
}
