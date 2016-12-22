package main;

import main.BooruScraper.IqdbException;
import main.BooruScraper.IqdbSearcher;
import main.File.FileMover;
import main.Metadata.MetadataExecption;
import main.Metadata.XMPManager;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

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
    public void testMoveFile() {
        File outputFilePath = new File("D:\\Downloads\\derp\\derpyderp");
        FileMover fm = new FileMover(outputFilePath);

        if (!outputFilePath.exists()) {
            outputFilePath.mkdir();
            log.info("made directory");
        }
        File myFile = fm.createOutputImageFilePath(inputFile, outputFilePath);
        try {
            Files.move(inputFile.toPath(), myFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception e) {
            log.error("exception", e);
        }
    }

    @Test
    public void testStartToFinish() {
        IqdbSearcher searcher = new IqdbSearcher();
        Image image = null;
        try {
            image = searcher.processFile(inputFile);
        }catch (IqdbException e) {
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
//                xmp.printMetadata(outputFile2);
            } catch (MetadataExecption e) {
                log.error("Exception trying to write new tags to images");
            }
        }
    }

    @Test
    public void testMoveFiles() {

    }
}