package com.darkmage530.birat.BulkImager;

import java.io.File;
import java.util.Properties;

import static com.darkmage530.birat.BulkImager.Metadata.RatingSearch.SearchConfigType;

/**
 * Created by DarkMage530 on 5/21/2017. For BulkImageRenamingAndTagging
 * Current User Shirobako
 */
public class BirtConfiguration {

    private File scanLocation;
    private File endLocation;
    private boolean rename;
    private boolean tag;

    private boolean findWallpaper;
    private boolean defaultLocation;
    private File wallpaperLocationOverride;

    private File allDatabase;
    private File wallpaperDatabase;
    private char seperator;
    private char quoteChar;

    private boolean skipDatabase;

    private SearchConfigType searchConfigType;

    private Boolean dorobooruUpload;

    public BirtConfiguration(Properties prop) {
        scanLocation = new File(prop.getProperty("scan.location"));
        endLocation = new File(prop.getProperty("end.location"));
        rename = Boolean.parseBoolean(prop.getProperty("rename"));
        tag = Boolean.parseBoolean(prop.getProperty("tag"));
        findWallpaper = Boolean.parseBoolean(prop.getProperty("wallpaper.find"));
        defaultLocation = Boolean.parseBoolean(prop.getProperty("wallpaper.default.location"));
        wallpaperLocationOverride = new File(prop.getProperty("wallpaper.location.override"));

        this.allDatabase = new File(prop.getProperty("csv.local.database"));
        this.wallpaperDatabase = new File(prop.getProperty("csv.wallpaper.database"));
        seperator = prop.getProperty("csv.seperator").charAt(1);
        quoteChar = prop.getProperty("csv.quote.char").charAt(1);

        skipDatabase = Boolean.parseBoolean(prop.getProperty("skip.database"));

        searchConfigType = SearchConfigType.valueOf(prop.getProperty("wallpaper.rating.search").toUpperCase());

        dorobooruUpload = Boolean.parseBoolean(prop.getProperty("dorobooruUpload"));
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

    public File getAllDatabase() {
        return allDatabase;
    }

    public File getWallpaperDatabase() {
        return wallpaperDatabase;
    }

    public char getSeperator() {
        return seperator;
    }

    public char getQuoteChar() {
        return quoteChar;
    }

    public SearchConfigType getRatingType() {
        return searchConfigType;
    }

    public boolean isSkipDatabase() {
        return skipDatabase;
    }

    public boolean dorobooruUploads() {return dorobooruUpload;}
}
