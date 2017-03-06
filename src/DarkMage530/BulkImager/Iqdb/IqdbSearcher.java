package DarkMage530.BulkImager.Iqdb;

import DarkMage530.BulkImager.PictureFile;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;

/**
 * Created by DarkMage530 on 3/5/2017. For BulkImageRenamingAndTagging
 * Current User Shirobako
 */
//package-private
class IqdbSearcher {

    private static final Logger log = LoggerFactory.getLogger(IqdbSearcher.class);

    public static final String IQDB_URL = "http://iqdb.org/";

    public IqdbDocument search(PictureFile pictureFile) throws IqdbException {
        try (FileInputStream fin = new FileInputStream(pictureFile.asFile())) {
            Document doc = Jsoup.connect(IQDB_URL).data("file", pictureFile.asFile().getName(), fin).post();
            return new IqdbDocument(doc);
        } catch (IOException e) {
            throw new IqdbException("Exception while connecting to Iqdb", e);
        }
    }
}
