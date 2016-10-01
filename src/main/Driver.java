package main;

import main.BooruScraper.IqdbException;
import main.BooruScraper.IqdbImage;
import main.BooruScraper.IqdbSearcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

/**
 * Created by Shirobako on 4/23/2016.
 */
public class Driver {

    private static final Logger log = LoggerFactory.getLogger(Driver.class);

    public static void main(String[] args) {
        try {
            File root = new File("D:\\Downloads\\Derp");
            FileNavigator fileNavigator = new FileNavigator(root);
            fileNavigator.scanDirectories();
        } catch (Exception e) {
            log.error("exception:", e);
        }
    }

    public static void moveAndWriteFile(File inputFile, File outputFile) {
        if (inputFile.getAbsolutePath().endsWith(".png") || inputFile.getAbsolutePath().endsWith(".gif")) {
            return;
        }
        IqdbSearcher searcher = new IqdbSearcher();
        IqdbImage image = null;
        try {
            image = searcher.processFile(inputFile);
        } catch (IqdbException e) {
            log.error("Exception trying to process iqdb file");
        }
        if (image != null) {
            XMPManager xmp = new XMPManager();
            try {
                if (xmp.hasTags(inputFile)) {
                    xmp.addXmlTags(inputFile, outputFile, image.getTags());
                } else {
                    xmp.writeNewXmlTags(inputFile, outputFile, image.getTags());
                }
            } catch (MetadataExecption e) {
                log.error("Exception trying to write new tags to images");
            }
        }
    }

}
