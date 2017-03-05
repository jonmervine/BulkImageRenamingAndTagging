package DarkMage530.BulkImager.Output;

import DarkMage530.BulkImager.ImageRating;
import DarkMage530.BulkImager.ImageRatios;
import DarkMage530.BulkImager.PictureFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

import static DarkMage530.BulkImager.Constants.MANUAL_SORT_DIRECTORY;
import static DarkMage530.BulkImager.Constants.WALLPAPER_DIRECTORY;

/**
 * Created by Shirobako on 1/2/2017.
 */
//Package-private
class OutputLocator {
    private static final Logger log = LoggerFactory.getLogger(OutputLocator.class);

    public File getWallpaperOutputLocation(PictureFile pictureFile, File rootDirectory) {
        return getWallpaperOutputLocation(pictureFile, ImageRating.UNKNOWN, rootDirectory);
    }

    public File getWallpaperOutputLocation(PictureFile pictureFile, ImageRating rating, File rootDirectory) {
        File destinationDirectory;
        destinationDirectory = new File(rootDirectory, WALLPAPER_DIRECTORY);
        return buildWallpaperDirectory(destinationDirectory, pictureFile.getImageRatio(), rating);
    }

    private File buildWallpaperDirectory(File destinationDirectory, ImageRatios ratio, ImageRating rating) {
        File resolutionDirectory;
        switch (ratio) {
            case CELL:
            case HOME_PC:
            case WORK_MONITORS:
                resolutionDirectory = new File(destinationDirectory, ratio.getResolution());
                break;
            default:
                resolutionDirectory = new File(destinationDirectory, MANUAL_SORT_DIRECTORY);
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
