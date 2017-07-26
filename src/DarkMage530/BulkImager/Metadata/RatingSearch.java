package DarkMage530.BulkImager.Metadata;

import DarkMage530.BulkImager.BirtConfiguration;
import DarkMage530.BulkImager.ImageRating;
import com.drew.metadata.MetadataException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

/**
 * Created by Shirobako on 9/3/2016.
 */
@Component
public class RatingSearch {

    @Autowired
    private BirtConfiguration config;

    public enum SearchConfigType {
        AVERAGE, HARSHEST, BEST, ALL;
    }


    public ImageRating getRatingByConfig(List<ImageRating> ratings) throws MetadataException {
        switch (config.getRatingType()) {
            case AVERAGE:
                return getAverageRating(ratings);
            case HARSHEST:
                return getHarshestRating(ratings);
            default:
                throw new MetadataException("Config did type doesn't work currently, requested: " + config.getRatingType());
        }

    }

    public ImageRating getAverageRating(List<ImageRating> ratings) {
        int ratingSum = ratings.stream()
                .mapToInt(rating -> rating.ordinal()).sum();

        return ImageRating.values()[(int) Math.round(ratingSum / (ratings.size() + 1 * 1.0))];
    }

    public ImageRating getHarshestRating(List<ImageRating> ratings) {
        ImageRating harshestRating = ratings.stream()
                .max((rating1, rating2) -> Integer.compare(rating1.ordinal(), rating2.ordinal()))
                .get();
        return harshestRating;
    }


    @Deprecated
    public List<ImageRating> getAllRatings() {
//        List<ImageRating> ratings = Lists.newArrayList(bestMatch.getRating());
//        ratings.addAll(additionalMatches.stream().map(match -> match.getRating()).collect(Collectors.toList()));
//        return ratings;
        return Collections.EMPTY_LIST;
    }

    @Deprecated
    public ImageRating getBestRating() {
//        return bestMatch.getRating();
        return ImageRating.UNKNOWN;
    }

}
