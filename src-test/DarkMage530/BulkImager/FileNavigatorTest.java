package DarkMage530.BulkImager;

import DarkMage530.BulkImager.Iqdb.IqdbException;
import DarkMage530.BulkImager.Iqdb.SearchIqdb;
import DarkMage530.BulkImager.Metadata.MetadataExecption;
import DarkMage530.BulkImager.Metadata.XMPManager;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

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
      /*  Output outputFilePath = new Output("D:\\Downloads\\derp\\derpyderp");
        PictureOutput fm = new PictureOutput(outputFilePath);

        if (!outputFilePath.exists()) {
            outputFilePath.mkdir();
            log.info("made directory");
        }
        Output myFile = fm.createOutputImageFilePath(inputFile, outputFilePath);
        try {
            Files.move(inputFile.asPath(), myFile.asPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception e) {
            log.error("exception", e);
        }
        */
    }

    @Test
    public void testStartToFinish() {
     /*   SearchIqdb searcher = new SearchIqdb();
        Image image = null;
        try {
            image = searcher.searchBoorus(inputFile);
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
        }*/
    }

    public interface Mine {
        void addNext();
    }

    @Test
    public void testListOf() {

        List<Mine> derp = new ArrayList();
        derp.add(() -> System.out.println("first"));
        derp.add(() -> System.out.println("second"));

        for (Mine mine : derp) {
            mine.addNext();
        }
    }

    @Test
    public void createcsv() {
        try {
            writer = new FileWriter("D:\\P\\images.csv");
            writer.append("md5,filename\n");
        } catch (IOException e) {
            log.error("fuck", e);
        }

        recursivelyScanDirectories(new File("D:\\P\\Fiction"));
        log.info("final count: " + total);
        try {
            writer.close();
        } catch (IOException e) {
            log.error("close:", e);
        }
    }

    private void recursivelyScanDirectories(File recusiveRoot) {
        for (File file : recusiveRoot.listFiles()) {
            if (file.isDirectory()) {
                recursivelyScanDirectories(file);
            } else if (file.getName().endsWith(".jpeg") ||
                    file.getName().endsWith(".jpg") ||
                    file.getName().endsWith(".png") ||
                    file.getName().endsWith(".gif")) {
                processFile(file);
            } else if (file.getName().endsWith("picasa.ini")) {
                file.delete();
            }
        }
    }

    private FileWriter writer;
    private int i = 0;
    private int total = 0;

    private void processFile(File file) {
        try {
            FileInputStream fis = new FileInputStream(file);
            String md5 = org.apache.commons.codec.digest.DigestUtils.md5Hex(fis);
            fis.close();

            writer.append(md5 + "," + file.toPath().getFileName() + "\n");
            i++;
            if (i > 1000) {
                total += i;
                i = 0;
                System.out.println("Total: " + total);
            }

        } catch (IOException e) {
            log.error("shit fucked up: ", e);
        }
    }
}