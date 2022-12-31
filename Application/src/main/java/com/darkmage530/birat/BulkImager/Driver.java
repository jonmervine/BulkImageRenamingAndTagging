package com.darkmage530.birat.BulkImager;

import com.darkmage530.birat.BulkImager.Boorus.DanbooruApi;
import com.darkmage530.birat.BulkImager.Iqdb.SearchIqdb;
import com.darkmage530.birat.BulkImager.Metadata.DatabaseManager;
import com.darkmage530.birat.BulkImager.Output.PictureOutput;
import com.darkmage530.birat.DorobooruService;
import com.darkmage530.birat.posts.PostFile;
import com.darkmage530.birat.posts.PostRequest;
import com.darkmage530.birat.tags.TagCategory;
import com.darkmage530.birat.tags.TagRequest;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.List;
import java.util.Map;

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

    private DorobooruService dorobooruService = new DorobooruService();

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
        } else {
            if (config.dorobooruUploads()) {
                recursivelyScanDirectories(config.getScanLocation(), config.getEndLocation());
            }
        }
    }

    private void recursivelyScanDirectories(File recursiveRoot, File moveRoot) {
        for (File file : recursiveRoot.listFiles()) {
            if (file.isDirectory()) {
                recursivelyScanDirectories(file, moveRoot);
            } else if (file.getName().endsWith(".jpeg") ||
                    file.getName().endsWith(".jpg") ||
                    file.getName().endsWith(".png")) {
                foundPicture(file, moveRoot);
            }
        }
    }

    private final DanbooruApi danbooruApi = new DanbooruApi();

    private void foundPicture(File file, File moveRoot) {
        PictureFile pictureFile = new PictureFile(file, moveRoot);
        Pair<PostRequest, Map<TagCategory, List<String>>> pairOfPostInfo = danbooruApi.getByMd5(pictureFile.getMd5());
        if (pairOfPostInfo == null) {
            return;
        }
        PostFile postFile = PostFile.Companion.invoke(pictureFile.asPath().toString());
        dorobooruService.createPost(pairOfPostInfo.getLeft(), postFile);

        for (Map.Entry<TagCategory, List<String>> tagCategory : pairOfPostInfo.getRight().entrySet()) {
            for (String tag : tagCategory.getValue()) {
                dorobooruService.updateTag(tag, new TagRequest(tagCategory.getKey(), 1));
            }
        }
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
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
