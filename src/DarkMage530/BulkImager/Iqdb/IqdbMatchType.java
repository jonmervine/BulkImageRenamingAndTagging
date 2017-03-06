package DarkMage530.BulkImager.Iqdb;

/**
 * Created by DarkMage530 on 3/5/2017. For BulkImageRenamingAndTagging
 * Current User Shirobako
 */
public enum IqdbMatchType {

    BEST("best"),
    ADDITIONAL("additional"),
    NO_RELEVANT_BUT_POSSIBLE("possible"),
    OTHER("other"),
    UNKNOWN("unknown");


    private String type;
    IqdbMatchType(String type) {
        this.type = type;
    }
}
