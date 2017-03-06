package DarkMage530.BulkImager.Iqdb;

import DarkMage530.BulkImager.Boorus.BooruImageFactory;
import DarkMage530.BulkImager.Image;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by Shirobako on 9/3/2016.
 */
//package-private
class IqdbParser {

    private static final Logger log = LoggerFactory.getLogger(IqdbParser.class);

    public IqdbImage parse(IqdbDocument iqdbDocument) {
        return iqdbDocument.explode();
    }
}

