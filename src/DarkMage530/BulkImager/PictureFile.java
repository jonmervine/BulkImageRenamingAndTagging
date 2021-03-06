package DarkMage530.BulkImager;

import DarkMage530.BulkImager.Metadata.RatingSearch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;

/**
 * Created by Shirobako on 3/5/2017.
 */
public class PictureFile {
    private final static Logger log = LoggerFactory.getLogger(PictureFile.class);


    private Path path;
    private File file;
    private ImageRatios imageRatios;
    private RatingSearch ratingSearch;
    private File moveRoot;
    private String md5;

    public PictureFile(File file, File moveRoot) {
        this.file = file;
        this.path = file.toPath();
        this.imageRatios = ImageRatios.getImageRatio(file);
        this.moveRoot = moveRoot;

        try (FileInputStream fis = new FileInputStream(file)) {
            this.md5 = org.apache.commons.codec.digest.DigestUtils.md5Hex(fis);
        } catch(IOException e) {
            log.error("IOException trying to read the md5 of " + file.getPath());
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
        return imageRatios.getWidth();
    }

    public int getHeight() {
        return imageRatios.getHeight();
    }

    public String getResolution() {
        return imageRatios.getResolution();
    }

    public ImageRatios getImageRatio() {
        return imageRatios;
    }

    public String getMd5() { return md5; }

    public boolean isWallpaper() {
        return imageRatios.isWallpaper();
    }

//    public ImageRating getRating() {
//        if (ratingSearch == null) {
//            return ImageRating.UNKNOWN;
//        }
//        else return ratingSearch.getBestRating();
//    }

    public File getMoveRoot() {
        return moveRoot;
    }

    @Override
    public String toString() {
        return "PictureFile: [" + asPath() + ", " + getResolution() + "]";
    }
}

