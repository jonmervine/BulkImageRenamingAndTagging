package DarkMage530.BulkImager.Output;

import DarkMage530.BulkImager.ImageRating;
import DarkMage530.BulkImager.ImageRatios;
import DarkMage530.BulkImager.Metadata.Metadata;
import DarkMage530.BulkImager.PictureFile;
import com.drew.metadata.MetadataException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.File;

import static DarkMage530.BulkImager.Constants.MANUAL_SORT_DIRECTORY;
import static DarkMage530.BulkImager.Constants.WALLPAPER_DIRECTORY;

/**
 * Created by Shirobako on 1/2/2017.
 */

@Component
//Package-private
class OutputLocator {
    private static final Logger log = LoggerFactory.getLogger(OutputLocator.class);

    public File getWallpaperOutputLocation(PictureFile pictureFile, Metadata metadata) {
        File destinationDirectory;
        destinationDirectory = new File(pictureFile.getMoveRoot(), WALLPAPER_DIRECTORY);
        return buildWallpaperDirectory(destinationDirectory, pictureFile.getImageRatio(), metadata);
    }

    private File buildWallpaperDirectory(File destinationDirectory, ImageRatios ratio, Metadata metadata) {
        ImageRating rating;
        try {
            rating = metadata.getRating();
        } catch (MetadataException e) {
            log.error("Error while getting the Rating from the Metadata, defaulting to UNKNOWN");
            rating = ImageRating.UNKNOWN;
        }

        File resolutionDirectory;
        switch (rating) {
            case SAFE:
            case ERO:
            case EXPLICIT:
                resolutionDirectory = new File(destinationDirectory, rating.getName());
                break;
            default:
                resolutionDirectory = new File(destinationDirectory, MANUAL_SORT_DIRECTORY);
                break;
        }

        switch (ratio) {
            case CELL:
            case HOME_PC:
            case WORK_MONITORS:
                return new File(resolutionDirectory, ratio.getResolution());
            default:
               return new File(resolutionDirectory, MANUAL_SORT_DIRECTORY);
        }

    }
}
