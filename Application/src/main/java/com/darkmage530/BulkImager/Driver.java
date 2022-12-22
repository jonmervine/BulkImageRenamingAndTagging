package com.darkmage530.BulkImager;

import DarkMage530.BulkImager.Iqdb.SearchIqdb;
import DarkMage530.BulkImager.Metadata.DatabaseManager;
import DarkMage530.BulkImager.Output.PictureOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.File;

/**
 * Created by DarkMage530 on 4/23/2016. For BulkImageRenamingAndTagging
 * Current User Shirobako
 */
@Component
public class Driver {

    private static final Logger log = LoggerFactory.getLogger(Driver.class);

    private final BirtConfiguration config;

    private final SearchIqdb searcher;

    private final PictureOutput outputResult;

    private final DatabaseManager dbManager;

    public Driver(BirtConfiguration config, SearchIqdb searcher, PictureOutput outputResult, DatabaseManager dbManager) {
        this.config = config;
        this.searcher = searcher;
        this.outputResult = outputResult;
        this.dbManager = dbManager;
    }

    public void drive() {
        if (config.isFindWallpaper()) {
            File moveRoot;
            if (config.isDefaultLocation()) {
                moveRoot = config.getEndLocation();
            } else {
                moveRoot = config.getWallpaperLocationOverride();
            }
            log.info("moveRoot=" + moveRoot);
            recursivelyScanDirectoriesForWallpapers(config.getScanLocation(), moveRoot);
        }
    }

    private void recursivelyScanDirectoriesForWallpapers(File recursiveRoot, File moveRoot) {
        for (File file : recursiveRoot.listFiles()) {
            if (file.isDirectory()) {
                recursivelyScanDirectoriesForWallpapers(file, moveRoot);
            } else if (file.getName().endsWith(".jpeg") ||
                    file.getName().endsWith(".jpg") ||
                    file.getName().endsWith(".png") ||
                    file.getName().endsWith(".gif")) {
                determineIfWallpaper(file, moveRoot);
            }
        }
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

    private void foundPicture(File file) {
    /*    log.info("Processing file " + file.getPath());

        Image image = null;
        try {
            image = searcher.searchBoorus(file);
        } catch (IqdbException e) {
            log.error("IqdbException", e);
        }

        if (image != null) {
            File outputFile = null;//outputResult.getOutputFile(image);

            XMPManager xmp = new XMPManager();
            try {
                if (xmp.hasTags(image.getLocation())) {
                    xmp.addXmlTags(image.getLocation(), outputFile, image.getTags());
                } else {
                    xmp.writeNewXmlTags(image.getLocation(), outputFile, image.getTags());
                }
                log.info("Wrote tags and moved to " + outputFile.getPath());
                image.getLocation().delete();
            } catch (MetadataExecption e) {
                log.error("Exception trying to write and move new tags to images");
            }
        } else {
//            outputResult.moveErroredFile(file);
            log.info("Image was null, moved to Manual sort");
        }*/
    }
}
