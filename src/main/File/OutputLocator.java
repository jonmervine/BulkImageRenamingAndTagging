package main.File;

import main.ImageRating;
import main.ImageRatios;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

import static main.Constants.MANUAL_SORT_DIRECTORY;
import static main.Constants.ORIGINAL_DIRECTORY;
import static main.Constants.WALLPAPER_DIRECTORY;

/**
 * Created by Shirobako on 1/2/2017.
 */
public class OutputLocator {
    private static final Logger log = LoggerFactory.getLogger(OutputLocator.class);

    private File rootDirectory;

    public OutputLocator(File rootDirectory) {
        this.rootDirectory = rootDirectory;
    }

    public File getOutputLocation(File image, boolean onlyWallpapers) throws FileMoverException {
        return getOutputLocation(image, ImageRating.UNKNOWN, onlyWallpapers);
    }

    public File getOutputLocation(File image, ImageRating rating, boolean onlyWallpapers) throws FileMoverException {
        File destinationDirectory;
        ImageRatios ratio = ImageRatios.getImageRatio(image);
        if (ratio.isWallpaper()) {
            destinationDirectory = new File(rootDirectory, WALLPAPER_DIRECTORY);
            return buildWallpaperDirectory(destinationDirectory, ratio, rating);
        }

        if (onlyWallpapers) {
            return image;
        }

        //rest
        return image;
    }

    private File buildWallpaperDirectory(File image, ImageRatios ratio, ImageRating rating) {
        File resolutionDirectory;
        switch (ratio) {
            case CELL:
            case HOME_PC:
            case WORK_MONITORS:
                resolutionDirectory = new File(image, ratio.getResolution());
                break;
            default:
                resolutionDirectory = new File(image, MANUAL_SORT_DIRECTORY);
                return resolutionDirectory;
        }

        switch (rating) {
            case SAFE:
            case ERO:
            case EXPLICIT:
                return new File(resolutionDirectory, rating.getName());
            default:
                return new File(resolutionDirectory, MANUAL_SORT_DIRECTORY);
        }
    }
}
