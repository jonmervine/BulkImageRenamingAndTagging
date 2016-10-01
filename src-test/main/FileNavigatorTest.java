package main;

import main.BooruScraper.IqdbException;
import main.BooruScraper.IqdbImage;
import main.BooruScraper.IqdbSearcher;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

/**
 * Created by Shirobako on 4/24/2016.
 */
public class FileNavigatorTest {

    private static final Logger log = LoggerFactory.getLogger(FileNavigatorTest.class);
    private static final File testFile = new File("D:\\Downloads\\derp\\Album Fqedd - Imgur\\029 - UESTINJ.jpg");

    private static final File inputFile = new File("D:\\Downloads\\derp\\101 - htXCja4.jpg");
    private static final File inputFile2 = new File("D:\\Downloads\\derp\\101 - htXCja4 - Copy.jpg");

    private static final File outputFile = new File("D:\\Downloads\\derp\\outputFile.jpg");
    private static final File outputFile2 = new File("D:\\Downloads\\derp\\outputFile2.jpg");

    @Test
    public void testStartToFinish() {
        IqdbSearcher searcher = new IqdbSearcher();
        IqdbImage image = null;
        try {
            image = searcher.processFile(inputFile2);
        }catch (IqdbException e) {
            log.error("Exception trying to process iqdb file");
        }

        if (image != null) {
            XMPManager xmp = new XMPManager();
            try {
                if (xmp.hasTags(inputFile2)) {
                    xmp.addXmlTags(inputFile2, outputFile2, image.getTags());
                } else {
                    xmp.writeNewXmlTags(inputFile2, outputFile2, image.getTags());
                }
                xmp.printMetadata(outputFile2);
            } catch (MetadataExecption e) {
                log.error("Exception trying to write new tags to images");
            }
        }
    }
    }