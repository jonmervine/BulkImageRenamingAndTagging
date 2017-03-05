package DarkMage530.BulkImager;

import DarkMage530.BulkImager.BooruScraper.IqdbException;
import DarkMage530.BulkImager.BooruScraper.IqdbSearcher;
import DarkMage530.BulkImager.Metadata.MetadataExecption;
import DarkMage530.BulkImager.Metadata.XMPManager;
import DarkMage530.BulkImager.Output.PictureOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

/**
 * Created by Shirobako on 4/23/2016.
 */
public class Driver {

    private static final Logger log = LoggerFactory.getLogger(Driver.class);

    //    private static final Output testRoot = new Output("D:\\Downloads\\derp\\test");
    private static final File testRoot = new File("D:\\Downloads\\derp - Copy");
    //    private static final Output moveRoot = new Output("D:\\Downloads\\derp\\moved");
    private static final File moveRoot = new File("D:\\Downloads\\moved");

    private IqdbSearcher searcher = new IqdbSearcher();
    private PictureOutput mover = new PictureOutput();

    public static void main(String[] args) {
        try {
            Driver driver = new Driver();
            driver.recursivelyScanDirectories(testRoot);
        } catch (Exception e) {
            log.error("exception:", e);
        }
    }

    private void recursivelyScanDirectories(File recusiveRoot) {
        for (File file : recusiveRoot.listFiles()) {
            if (file.isDirectory()) {
                recursivelyScanDirectories(file);
                file.delete();
            } else if (file.getName().endsWith(".jpeg") ||
                    file.getName().endsWith(".jpg") ||
                    file.getName().endsWith(".png") ||
                    file.getName().endsWith(".gif")) {

//                foundPicture(file);
                processFile(file);
            } else if (file.getName().endsWith("picasa.ini")) {
                file.delete();
            }
        }
    }

    private void processFile(File foundPicture) {
        log.info("Found file " + foundPicture.getPath());
        PictureFile pictureFile = new PictureFile(foundPicture);

        if (pictureFile.isWallpaper()) {
            log.info("pictureFile is wallpaper");
            pictureFile = mover.copyWallpaper(pictureFile, moveRoot);
        }
    }

    private void foundPicture(File file) {
        log.info("Processing file " + file.getPath());

        Image image = null;
        try {
            image = searcher.processFile(file);
        } catch (IqdbException e) {
            log.error("IqdbException", e);
        }

        if (image != null) {
            File outputFile = null;//mover.getOutputFile(image);

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
//            mover.moveErroredFile(file);
            log.info("Image was null, moved to Manual sort");
        }
    }
}
