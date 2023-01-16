package com.darkmage530.birat.BulkImager.strategies;

import com.darkmage530.birat.BulkImager.BirtConfiguration;
import com.darkmage530.birat.BulkImager.Driver;
import com.darkmage530.birat.BulkImager.Metadata.DatabaseManager;
import com.darkmage530.birat.BulkImager.Output.PictureOutput;
import com.darkmage530.birat.BulkImager.PictureFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
public class WallpaperStrategies implements Activity {

    private static final Logger log = LoggerFactory.getLogger(Driver.class);

    private BirtConfiguration config;
    private DatabaseManager dbManager;
    private PictureOutput outputResult;

    public WallpaperStrategies(BirtConfiguration config, DatabaseManager dbManager, PictureOutput outputResult) {
        this.config = config;
        this.dbManager = dbManager;
        this.outputResult = outputResult;
    }

    @Override
    public void foundPicture(File file) {
        File moveRoot;
        if (config.isDefaultLocation()) {
            moveRoot = config.getEndLocation();
        } else {
            moveRoot = config.getWallpaperLocationOverride();
        }
        log.info("moveRoot=" + moveRoot);
        determineIfWallpaper(file, moveRoot);
    }

    private void determineIfWallpaper(File foundPicture, File moveRoot) {
//        log.info("Found file " + foundPicture.getPath());
        final PictureFile pictureFile = new PictureFile(foundPicture, moveRoot);

        if (pictureFile.isWallpaper()) {
            log.info("pictureFile is wallpaper");
            processWallpaper(pictureFile);
        }
    }

    private void processWallpaper(PictureFile pictureFile) {
        if (!config.isSkipDatabase()) {
            pictureFile = dbManager.wallpaperLookup(pictureFile);

            if (!pictureFile.getMetadata().isPresent()) {
                //handle foundwallpapers, already in wallpaper directory
                return;
            }

            pictureFile = dbManager.allLookup(pictureFile);

        }

        //need to copy to wallpaper directory
        outputResult.copy(pictureFile);

        //need to tag file if jpg
        // need to rename file

        if (!config.isSkipDatabase()) {
            // need to update wallpaper database
            dbManager.updateWallpaper(pictureFile);
            // need to delete leftover
        }

    }
}
