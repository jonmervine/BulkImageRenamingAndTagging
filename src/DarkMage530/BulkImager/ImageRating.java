package DarkMage530.BulkImager;

/**
 * Created by Shirobako on 1/2/2017.
 */
public enum ImageRating {

    SAFE("safe"),
    ERO("ero"),
    EXPLICIT("explicit"),
    UNKNOWN("unknown");

    private String name;

    ImageRating(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static ImageRating getImageRating(String givenRating) {
        for (ImageRating rating : ImageRating.values()) {
            if (rating.getName().equalsIgnoreCase(givenRating)) {
                return rating;
            }
        }
        return UNKNOWN;
    }
}
