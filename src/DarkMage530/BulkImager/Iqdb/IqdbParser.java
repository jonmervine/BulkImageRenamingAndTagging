package DarkMage530.BulkImager.Iqdb;

import DarkMage530.BulkImager.Metadata.RatingSearch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Shirobako on 9/3/2016.
 */
//package-private
class IqdbParser {

    private static final Logger log = LoggerFactory.getLogger(IqdbParser.class);

    public RatingSearch parse(IqdbDocument iqdbDocument) {
        return iqdbDocument.explode();
    }
}

