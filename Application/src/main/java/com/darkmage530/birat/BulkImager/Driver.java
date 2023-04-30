package com.darkmage530.birat.BulkImager;

import com.darkmage530.birat.BulkImager.strategies.DeduplicateStrategies;
import com.darkmage530.birat.BulkImager.strategies.DorobooruStrategies;
import com.darkmage530.birat.BulkImager.strategies.Strategy;
import com.darkmage530.birat.BulkImager.strategies.WallpaperStrategies;
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

    private BirtConfiguration config;

    private DeduplicateStrategies deduplicateStrategies;
    private WallpaperStrategies wallpaperStrategies;
    private DorobooruStrategies dorobooruStrategies;

    public Driver(BirtConfiguration config,
                  DorobooruStrategies dorobooruStrategies,
                  WallpaperStrategies wallpaperStrategies,
                  DeduplicateStrategies deduplicateStrategies) {
        this.config = config;
        this.dorobooruStrategies = dorobooruStrategies;
        this.wallpaperStrategies = wallpaperStrategies;
        this.deduplicateStrategies = deduplicateStrategies;

    }

    public void start() {
        switch (config.getActivityType()) {
            case FIND_WALLPAPERS:
                findWallpapers();
                break;
            case DEDUPLICATE:
                deduplicate();
                break;
            case IMPORT_TO_DOROBOORU:
                importToDorobooru();
                break;
            case UNKNOWN:
                unknown();
                break;
        }
    }

    private void findWallpapers() {
        recursivelyScanDirectories(config.getScanLocation(), processWallpaper);
    }

    private void deduplicate() {
        recursivelyScanDirectories(config.getScanLocation(), deduplicateViaMd5);
    }

    private void importToDorobooru() {
        recursivelyScanDirectories(config.getScanLocation(), dorobooruImport);
    }

    private void unknown() {
        System.out.println("unkonwn something else to do");
    }

    Strategy deduplicateViaMd5 = (file) -> deduplicateStrategies.foundPicture(file);

    Strategy dorobooruImport = (file) -> dorobooruStrategies.foundPicture(file);

    Strategy processWallpaper = (file) -> wallpaperStrategies.foundPicture(file);

    void recursivelyScanDirectories(File recursiveRoot, Strategy strategy) {
        for (File file : recursiveRoot.listFiles()) {
            if (file.isDirectory()) {
                recursivelyScanDirectories(file, strategy);
            } else if (file.getName().endsWith(".jpeg") ||
                    file.getName().endsWith(".jpg") ||
                    file.getName().endsWith(".png") ||
                    file.getName().endsWith(".gif")) {
                strategy.execute(file);
            }
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



