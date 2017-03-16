package DarkMage530.BulkImager.Iqdb;

import DarkMage530.BulkImager.ImageRating;
import DarkMage530.BulkImager.ImageRatios;
import com.google.common.collect.Lists;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

/**
 * Created by DarkMage530 on 3/15/2017. For BulkImageRenamingAndTagging
 * Current User Shirobako
 */
public class IqdbImageTest {

    @Test
    public void testGetAverageRating() throws Exception {
        IqdbMatch bestMatch = new IqdbMatch(IqdbMatchType.BEST, "source", ImageRating.SAFE, ImageRatios.DUAL_WORK_MONITORS, "url", "tags", 75);
        List<IqdbMatch> additional = Lists.newArrayList(
                new IqdbMatch(IqdbMatchType.ADDITIONAL, "source", ImageRating.EXPLICIT, ImageRatios.DUAL_WORK_MONITORS, "url", "tags", 75),
                new IqdbMatch(IqdbMatchType.ADDITIONAL, "source", ImageRating.UNKNOWN, ImageRatios.DUAL_WORK_MONITORS, "url", "tags", 75),
                new IqdbMatch(IqdbMatchType.ADDITIONAL, "source", ImageRating.ERO, ImageRatios.DUAL_WORK_MONITORS, "url", "tags", 75));

        IqdbImage testImage = new IqdbImage(bestMatch, additional);

        Assert.assertEquals(ImageRating.EXPLICIT, testImage.getAverageRating());
    }

    @Test
         public void testGetHarshestRating() throws Exception {
        IqdbMatch bestMatch = new IqdbMatch(IqdbMatchType.BEST, "source", ImageRating.SAFE, ImageRatios.DUAL_WORK_MONITORS, "url", "tags", 75);
        List<IqdbMatch> additional = Lists.newArrayList(
                new IqdbMatch(IqdbMatchType.ADDITIONAL, "source", ImageRating.EXPLICIT, ImageRatios.DUAL_WORK_MONITORS, "url", "tags", 75),
                new IqdbMatch(IqdbMatchType.ADDITIONAL, "source", ImageRating.ERO, ImageRatios.DUAL_WORK_MONITORS, "url", "tags", 75));

        IqdbImage testImage = new IqdbImage(bestMatch, additional);

        Assert.assertEquals(ImageRating.EXPLICIT, testImage.getHarshestRating());
    }

    @Test
    public void testGetHarshestRatingWithUnknown() throws Exception {
        IqdbMatch bestMatch = new IqdbMatch(IqdbMatchType.BEST, "source", ImageRating.SAFE, ImageRatios.DUAL_WORK_MONITORS, "url", "tags", 75);
        List<IqdbMatch> additional = Lists.newArrayList(
                new IqdbMatch(IqdbMatchType.ADDITIONAL, "source", ImageRating.EXPLICIT, ImageRatios.DUAL_WORK_MONITORS, "url", "tags", 75),
                new IqdbMatch(IqdbMatchType.ADDITIONAL, "source", ImageRating.UNKNOWN, ImageRatios.DUAL_WORK_MONITORS, "url", "tags", 75),
                new IqdbMatch(IqdbMatchType.ADDITIONAL, "source", ImageRating.ERO, ImageRatios.DUAL_WORK_MONITORS, "url", "tags", 75));

        IqdbImage testImage = new IqdbImage(bestMatch, additional);

        Assert.assertEquals(ImageRating.UNKNOWN, testImage.getHarshestRating());
    }

    @Test
    public void testGetHarshestRatingWithBestAsHarshest() throws Exception {
        IqdbMatch bestMatch = new IqdbMatch(IqdbMatchType.BEST, "source", ImageRating.ERO, ImageRatios.DUAL_WORK_MONITORS, "url", "tags", 75);
        List<IqdbMatch> additional = Lists.newArrayList(
                new IqdbMatch(IqdbMatchType.ADDITIONAL, "source", ImageRating.SAFE, ImageRatios.DUAL_WORK_MONITORS, "url", "tags", 75),
                new IqdbMatch(IqdbMatchType.ADDITIONAL, "source", ImageRating.SAFE, ImageRatios.DUAL_WORK_MONITORS, "url", "tags", 75),
                new IqdbMatch(IqdbMatchType.ADDITIONAL, "source", ImageRating.SAFE, ImageRatios.DUAL_WORK_MONITORS, "url", "tags", 75));

        IqdbImage testImage = new IqdbImage(bestMatch, additional);

        Assert.assertEquals(ImageRating.ERO, testImage.getHarshestRating());
    }

    @Test
    public void testGetBestRating() throws Exception {
            IqdbMatch bestMatch = new IqdbMatch(IqdbMatchType.BEST, "source", ImageRating.SAFE, ImageRatios.DUAL_WORK_MONITORS, "url", "tags", 75);
            List<IqdbMatch> additional = Lists.newArrayList(
                    new IqdbMatch(IqdbMatchType.ADDITIONAL, "source", ImageRating.EXPLICIT, ImageRatios.DUAL_WORK_MONITORS, "url", "tags", 75),
                    new IqdbMatch(IqdbMatchType.ADDITIONAL, "source", ImageRating.UNKNOWN, ImageRatios.DUAL_WORK_MONITORS, "url", "tags", 75),
                    new IqdbMatch(IqdbMatchType.ADDITIONAL, "source", ImageRating.ERO, ImageRatios.DUAL_WORK_MONITORS, "url", "tags", 75));

            IqdbImage testImage = new IqdbImage(bestMatch, additional);

            Assert.assertEquals(ImageRating.SAFE, testImage.getBestRating());
    }

    @Test
    public void testGetAllRatings() throws Exception {
        IqdbMatch bestMatch = new IqdbMatch(IqdbMatchType.BEST, "source", ImageRating.SAFE, ImageRatios.DUAL_WORK_MONITORS, "url", "tags", 75);
        List<IqdbMatch> additional = Lists.newArrayList(
                new IqdbMatch(IqdbMatchType.ADDITIONAL, "source", ImageRating.EXPLICIT, ImageRatios.DUAL_WORK_MONITORS, "url", "tags", 75),
                new IqdbMatch(IqdbMatchType.ADDITIONAL, "source", ImageRating.UNKNOWN, ImageRatios.DUAL_WORK_MONITORS, "url", "tags", 75),
                new IqdbMatch(IqdbMatchType.ADDITIONAL, "source", ImageRating.ERO, ImageRatios.DUAL_WORK_MONITORS, "url", "tags", 75));

        IqdbImage testImage = new IqdbImage(bestMatch, additional);

        Assert.assertEquals(4, testImage.getAllRatings().size());
    }
}