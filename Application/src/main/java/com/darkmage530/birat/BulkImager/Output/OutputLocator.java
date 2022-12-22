package com.darkmage530.birat.BulkImager.Output;

import com.darkmage530.birat.BulkImager.BirtConfiguration;
import com.darkmage530.birat.BulkImager.ImageRating;
import com.darkmage530.birat.BulkImager.PictureFile;
import com.drew.metadata.MetadataException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.File;

import static com.darkmage530.birat.BulkImager.Constants.MANUAL_SORT_DIRECTORY;
import static com.darkmage530.birat.BulkImager.Constants.WALLPAPER_DIRECTORY;

/**
 * Created by Shirobako on 1/2/2017.
 */

@Component
//Package-private
class OutputLocator {
    private static final Logger log = LoggerFactory.getLogger(OutputLocator.class);

    private final BirtConfiguration config;

    public OutputLocator(BirtConfiguration config) {
        this.config = config;
    }

    public File getWallpaperOutputLocation(PictureFile pictureFile) {
        File destinationDirectory = new File(pictureFile.getMoveRoot(), WALLPAPER_DIRECTORY);
        return buildWallpaperDirectory(destinationDirectory, pictureFile);
    }

    private File buildWallpaperDirectory(File destinationDirectory, PictureFile pictureFile) {
        if (pictureFile.getMetadata().isPresent()) {
            ImageRating rating;
            try {
                rating = pictureFile.getMetadata().get().getRating();
            } catch (MetadataException e) {
                log.error("Error while getting the Rating from the Metadata, defaulting to UNKNOWN");
                rating = ImageRating.UNKNOWN;
            }

            destinationDirectory = new File(destinationDirectory, config.getRatingType().name());

            switch (rating) {
                case SAFE:
                case ERO:
                case EXPLICIT:
                    destinationDirectory = new File(destinationDirectory, rating.getName());
                    break;
                default:
                    destinationDirectory = new File(destinationDirectory, MANUAL_SORT_DIRECTORY);
                    break;
            }
        }


        switch (pictureFile.getImageRatio()) {
            case CELL:
            case PIXEL5a:
//            case HOME_PC:
//            case WORK_MONITORS:
                return new File(destinationDirectory, pictureFile.getImageRatio().getResolution());
            default:
               return new File(destinationDirectory, MANUAL_SORT_DIRECTORY);
        }
    }
}
