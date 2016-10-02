package main;

import main.BooruScraper.IqdbException;
import main.BooruScraper.IqdbSearcher;
import main.File.FileMover;
import main.File.FileMoverException;
import main.Metadata.MetadataExecption;
import main.Metadata.XMPManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Created by Shirobako on 4/23/2016.
 */
public class Driver {

    private static final Logger log = LoggerFactory.getLogger(Driver.class);

    private static final File testRoot = new File("D:\\Downloads\\derp\\test");
    private static final File moveRoot = new File("D:\\Downloads\\derp\\moved");
    private IqdbSearcher searcher = new IqdbSearcher();
    private FileMover mover = new FileMover(moveRoot);

    public static void main(String[] args) {
        try {
            Driver driver = new Driver();
            driver.recursivelyScanDirectories(testRoot);
        } catch (Exception e) {
            log.error("exception:", e);
        }
    }

    private void recursivelyScanDirectories(File recusiveRoot) throws FileMoverException {

        for (File file : recusiveRoot.listFiles()) {
            if (file.isDirectory()) {
                recursivelyScanDirectories(file);
            } else if (file.getName().endsWith(".jpeg") ||
                    file.getName().endsWith(".jpg") ||
                    file.getName().endsWith(".png") ||
                    file.getName().endsWith(".gif")) {
                try {
                    BufferedImage bimg = ImageIO.read(file);
                    int width = bimg.getWidth();
                    int height = bimg.getHeight();

                    Image image = searcher.processFile(file);

                    if (image != null) {
                        XMPManager xmp = new XMPManager();
                        try {

                            File outputFile = mover.createOutputImageFilePath(file, new File(testRoot, "copy"));

                            if (xmp.hasTags(image.getLocation())) {
                                xmp.addXmlTags(image.getLocation(), outputFile, image.getTags());
                            } else {
                                xmp.writeNewXmlTags(image.getLocation(), outputFile, image.getTags());
                            }

                            image.setLocation(outputFile);

                            mover.moveFile(image, width, height);

                        } catch (MetadataExecption e) {
                            log.error("Exception trying to write new tags to images");
                        }
                    } else {
                        mover.moveErroredFile(file);
                    }


                } catch (IOException ex) {
                    log.error("IOException", ex);
                } catch (IqdbException e) {
                    log.error("IqdbException", e);
                }
            }
        }
    }
}
