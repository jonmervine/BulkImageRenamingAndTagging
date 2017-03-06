package DarkMage530.BulkImager.Iqdb;

import DarkMage530.BulkImager.ImageRating;
import DarkMage530.BulkImager.ImageRatios;

import java.util.List;

/**
 * Created by Shirobako on 9/3/2016.
 */
public class IqdbImage {

    IqdbMatch bestMatch;
    List<IqdbMatch> additionalMatches;

    public IqdbImage(IqdbMatch bestMatch, List<IqdbMatch> additionalMatches) {
        this.bestMatch = bestMatch;
        this.additionalMatches = additionalMatches;
    }
}
