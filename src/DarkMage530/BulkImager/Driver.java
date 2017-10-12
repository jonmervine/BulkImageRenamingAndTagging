package DarkMage530.BulkImager;

import DarkMage530.BulkImager.Iqdb.SearchIqdb;
import DarkMage530.BulkImager.Md5.Md5Lookup;
import DarkMage530.BulkImager.Metadata.Metadata;
import DarkMage530.BulkImager.Output.PictureOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;

/**
 * Created by DarkMage530 on 4/23/2016. For BulkImageRenamingAndTagging
 * Current User Shirobako
 */
@Component
public class Driver {

    private static final Logger log = LoggerFactory.getLogger(Driver.class);

    @Autowired
    private BirtConfiguration config;

    @Autowired
    private SearchIqdb searcher;

    @Autowired
    private PictureOutput outputResult;

    @Autowired
    private Md5Lookup lookup;

    public void drive() {
        if (config.isFindWallpaper()) {
            File moveRoot;
            if (config.isDefaultLocation()) {
                moveRoot = config.getEndLocation();
            } else {
                moveRoot = config.getWallpaperLocationOverride();
            }
            recursivelyScanDirectories(config.getScanLocation(), moveRoot);
        }
    }

    private void recursivelyScanDirectories(File recusiveRoot, File moveRoot) {
        for (File file : recusiveRoot.listFiles()) {
            if (file.isDirectory()) {
                recursivelyScanDirectories(file, moveRoot);
            } else if (file.getName().endsWith(".jpeg") ||
                    file.getName().endsWith(".jpg") ||
                    file.getName().endsWith(".png") ||
                    file.getName().endsWith(".gif")) {
                processFile(file, moveRoot);
            } else if (file.getName().endsWith("picasa.ini")) {
                file.delete();
            }
        }
    }

    private void processFile(File foundPicture, File moveRoot) {
        log.info("Found file " + foundPicture.getPath());
        final PictureFile pictureFile = new PictureFile(foundPicture, moveRoot);

        if (pictureFile.isWallpaper() && config.isFindWallpaper()) {
            log.info("pictureFile is wallpaper");
            processWallpapers(pictureFile);
        }
    }

    private void processWallpapers(PictureFile pictureFile) {
        Metadata foundWallpapers = lookup.wallpaperLookup(pictureFile);

        if (!foundWallpapers.isEmpty()) {
            //handle foundwallpapers, already in wallpaper directory
            return;
        }

        foundWallpapers = lookup.allLookup(pictureFile);

        if (!foundWallpapers.isEmpty()) {

            //need to copy to wallpaper directory
            outputResult.copy(pictureFile, foundWallpapers);

            //need to tag file if jpg
            // need to rename file
            // need to update wallpaper database
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
