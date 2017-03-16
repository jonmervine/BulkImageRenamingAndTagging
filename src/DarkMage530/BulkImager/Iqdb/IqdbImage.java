package DarkMage530.BulkImager.Iqdb;

import DarkMage530.BulkImager.Constants;
import DarkMage530.BulkImager.ImageRating;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import java.util.List;
import java.util.stream.Collectors;

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

    public ImageRating getAverageRating() {
        int ratingSum = additionalMatches.stream()
                .filter(match -> match.getSimilarity() > Constants.AcceptedSimilarity)
                .mapToInt(match -> match.getRating().ordinal()).sum();
        if (bestMatch.getSimilarity() > Constants.AcceptedSimilarity) {
            ratingSum += bestMatch.getRating().ordinal();
        }
        return ImageRating.values()[(int) Math.round(ratingSum / (additionalMatches.size() + 1 * 1.0))];
    }

    public ImageRating getHarshestRating() {
        ImageRating harshestAdditional = additionalMatches.stream()
                .filter(match -> match.getSimilarity() > Constants.AcceptedSimilarity)
                .max((match1, match2) -> Integer.compare(match1.getRating().ordinal(), match2.getRating().ordinal()))
                .get().getRating();
        if (bestMatch.getSimilarity() > Constants.AcceptedSimilarity && bestMatch.getRating().ordinal() > harshestAdditional.ordinal()) {
            return bestMatch.getRating();
        } else
            return harshestAdditional;
    }

    public ImageRating getBestRating() {
        return bestMatch.getRating();
    }

    public List<ImageRating> getAllRatings() {
        List<ImageRating> ratings = Lists.newArrayList(bestMatch.getRating());
        ratings.addAll(additionalMatches.stream().map(match -> match.getRating()).collect(Collectors.toList()));
        return ratings;
    }

}
