package DarkMage530.BulkImager.Iqdb;

import DarkMage530.BulkImager.Metadata.RatingSearch;
import DarkMage530.BulkImager.PictureFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Created by Shirobako on 6/20/2016.
 */
@Component
public class SearchIqdb {

    private static final Logger log = LoggerFactory.getLogger(SearchIqdb.class);

    public PictureFile searchBoorus(PictureFile pictureFile) {
        //Take image and use jsoup to get html of the search
        IqdbDocument iqdbDocument;
        try {
            IqdbSearcher searcher = new IqdbSearcher();
            iqdbDocument = searcher.search(pictureFile);
        } catch (IqdbException e) {
            log.error("Exception while searching Iqdb", e);
            return pictureFile;
        }

        //parse out booru results
        IqdbParser parser = new IqdbParser();
        RatingSearch ratingSearch = parser.parse(iqdbDocument);
        pictureFile.setIqdbImage(ratingSearch);

        //compile booru results into object

        //return booru result object

        return pictureFile;
    }
}
