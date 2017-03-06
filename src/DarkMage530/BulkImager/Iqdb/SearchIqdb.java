package DarkMage530.BulkImager.Iqdb;

import DarkMage530.BulkImager.Image;
import DarkMage530.BulkImager.PictureFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Shirobako on 6/20/2016.
 */
public class SearchIqdb {

    private static final Logger log = LoggerFactory.getLogger(SearchIqdb.class);

    public static final String IQDB_URL = "http://iqdb.org/";

    public Image searchBoorus(PictureFile file) throws IqdbException {
        //Take image and use jsoup to get html of the search
        IqdbSearcher searcher = new IqdbSearcher();
        searcher.search(file);

        //parse out booru results

        //compile booru results into object

        //return booru result object


    }
}
