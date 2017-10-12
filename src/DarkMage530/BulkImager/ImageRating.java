package DarkMage530.BulkImager;

import java.util.List;

/**
 * Created by Shirobako on 1/2/2017.
 */
public enum ImageRating {

    SAFE("safe", "s"),
    ERO("ero", "q"),
    EXPLICIT("explicit", "e"),
    UNKNOWN("unknown", "u");

    private String name;
    private String abbrv;

    ImageRating(String name, String abbrv) {
        this.name = name;
        this.abbrv = abbrv;
    }

    public String getName() {
        return name;
    }

    public String getAbbrv() {
        return abbrv;
    }

    public static ImageRating getImageRating(String givenRating) {
        for (ImageRating rating : ImageRating.values()) {
            if (rating.getName().equalsIgnoreCase(givenRating) || rating.getAbbrv().equalsIgnoreCase(givenRating)) {
                return rating;
            }
        }
        return UNKNOWN;
    }
}
