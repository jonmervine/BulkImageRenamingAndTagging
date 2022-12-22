package com.darkmage530.birat.BulkImager;

import com.darkmage530.birat.BulkImager.Metadata.Metadata;
import com.darkmage530.birat.BulkImager.Metadata.RatingSearch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

/**
 * Created by Shirobako on 3/5/2017.
 */
public class PictureFile {
    private final static Logger log = LoggerFactory.getLogger(PictureFile.class);

    private Path path;
    private File file;
    private ImageDimensions imageDimensions;
    private RatingSearch ratingSearch;
    private File moveRoot;
    private String md5;
    private Metadata metadata;

    public PictureFile(File file, File moveRoot) {
        this.file = file;
        this.path = file.toPath();
        this.imageDimensions = ImageDimensions.getImageDimensions(file);
        this.moveRoot = moveRoot;

        try (FileInputStream fis = new FileInputStream(file)) {
            this.md5 = org.apache.commons.codec.digest.DigestUtils.md5Hex(fis);
        } catch(IOException e) {
            log.error("IOException trying to read the md5 of " + file.getPath());
            this.md5 = "";
        }
    }

    public void updateFileLocation(File file) {
        this.file = file;
        this.path = file.toPath();
    }

//    public void setIqdbImage(RatingSearch ratingSearch) {
//        this.ratingSearch = ratingSearch;
//    }

    public File asFile() { return file; }

    public Path getFileName() {
        return path.getFileName();
    }

    public Path asPath() {
        return path;
    }

    public int getWidth() {
        return imageDimensions.getWidth();
    }

    public int getHeight() {
        return imageDimensions.getHeight();
    }

    public String getResolution() {
        return imageDimensions.getResolution();
    }

    public ImageDimensions getImageRatio() {
        return imageDimensions;
    }

    public String getMd5() { return md5; }

    public boolean isWallpaper() {
        return imageDimensions.isWallpaper();
    }

    public ImageRating getRating() {
        if (ratingSearch == null) {
            return ImageRating.UNKNOWN;
        }
        else return ratingSearch.getBestRating();
    }


    public Optional<Metadata> getMetadata() {
        return Optional.ofNullable(metadata);
    }

    public void setMetadata(Metadata metadata) {
        this.metadata = metadata;
    }

    public File getMoveRoot() {
        return moveRoot;
    }

    @Override
    public String toString() {
        return "PictureFile: [" + asPath() + ", " + getResolution() + "]";
    }
}

