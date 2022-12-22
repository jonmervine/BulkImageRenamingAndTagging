package DarkMage530.BulkImager.Iqdb;

import DarkMage530.BulkImager.ImageRating;
import DarkMage530.BulkImager.ImageDimensions;
import DarkMage530.BulkImager.Metadata.RatingSearch;
import com.google.common.collect.Lists;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Collections;
import java.util.List;

/**
 * Created by DarkMage530 on 3/15/2017. For BulkImageRenamingAndTagging
 * Current User Shirobako
 */
@RunWith(MockitoJUnitRunner.class)
public class RatingSearchTest {
//
//    @InjectMocks
//    private RatingSearch ratingSearch;
//
//
//    @Test
//    public void testGetAverageRating() throws Exception {
//        List<ImageRating> ratings = Lists.newArrayList(ImageRating.ERO, ImageRating.EXPLICIT, ImageRating.UNKNOWN, ImageRating.ERO);
//        Assert.assertEquals(ImageRating.EXPLICIT, ratingSearch.getAverageRating(ratings));
//    }
//
//    @Test
//    public void testGetAverageIfUnknown() throws Exception {
//        List<ImageRating> ratings = Lists.newArrayList(ImageRating.UNKNOWN, ImageRating.UNKNOWN, ImageRating.UNKNOWN);
//        Assert.assertEquals(ImageRating.EXPLICIT, ratingSearch.getAverageRating(ratings));
//    }
//
//    @Test
//    public void testGetHarshestRating() throws Exception {
//        List<ImageRating> ratings = Lists.newArrayList(ImageRating.SAFE, ImageRating.EXPLICIT, ImageRating.ERO);
//        Assert.assertEquals(ImageRating.EXPLICIT, ratingSearch.getHarshestRating(ratings));
//    }
//
//    @Test
//    public void testGetHarshestRatingWithUnknown() throws Exception {
//        List<ImageRating> ratings = Lists.newArrayList(ImageRating.SAFE, ImageRating.EXPLICIT, ImageRating.UNKNOWN, ImageRating.ERO);
//        Assert.assertEquals(ImageRating.UNKNOWN, ratingSearch.getHarshestRating(ratings));
//    }
//
//    @Ignore
//    @Test
//    public void testGetHarshestRatingWithBestAsHarshest() throws Exception {
//        IqdbMatch bestMatch = new IqdbMatch(IqdbMatchType.BEST, "source", ImageRating.ERO, ImageDimensions.DUAL_WORK_MONITORS, "url", "tags", 75);
//        List<IqdbMatch> additional = Lists.newArrayList(
//                new IqdbMatch(IqdbMatchType.ADDITIONAL, "source", ImageRating.SAFE, ImageDimensions.DUAL_WORK_MONITORS, "url", "tags", 75),
//                new IqdbMatch(IqdbMatchType.ADDITIONAL, "source", ImageRating.SAFE, ImageDimensions.DUAL_WORK_MONITORS, "url", "tags", 75),
//                new IqdbMatch(IqdbMatchType.ADDITIONAL, "source", ImageRating.SAFE, ImageDimensions.DUAL_WORK_MONITORS, "url", "tags", 75));
//
//        Assert.assertEquals(ImageRating.ERO, ratingSearch.getHarshestRating(Collections.EMPTY_LIST));
//    }
//
//    @Ignore
//    @Test
//    public void testGetBestRating() throws Exception {
//        IqdbMatch bestMatch = new IqdbMatch(IqdbMatchType.BEST, "source", ImageRating.SAFE, ImageDimensions.DUAL_WORK_MONITORS, "url", "tags", 75);
//        List<IqdbMatch> additional = Lists.newArrayList(
//                new IqdbMatch(IqdbMatchType.ADDITIONAL, "source", ImageRating.EXPLICIT, ImageDimensions.DUAL_WORK_MONITORS, "url", "tags", 75),
//                new IqdbMatch(IqdbMatchType.ADDITIONAL, "source", ImageRating.UNKNOWN, ImageDimensions.DUAL_WORK_MONITORS, "url", "tags", 75),
//                new IqdbMatch(IqdbMatchType.ADDITIONAL, "source", ImageRating.ERO, ImageDimensions.DUAL_WORK_MONITORS, "url", "tags", 75));
//
//        Assert.assertEquals(ImageRating.SAFE, ratingSearch.getBestRating());
//    }
//
//    @Ignore
//    @Test
//    public void testGetAllRatings() throws Exception {
//        IqdbMatch bestMatch = new IqdbMatch(IqdbMatchType.BEST, "source", ImageRating.SAFE, ImageDimensions.DUAL_WORK_MONITORS, "url", "tags", 75);
//        List<IqdbMatch> additional = Lists.newArrayList(
//                new IqdbMatch(IqdbMatchType.ADDITIONAL, "source", ImageRating.EXPLICIT, ImageDimensions.DUAL_WORK_MONITORS, "url", "tags", 75),
//                new IqdbMatch(IqdbMatchType.ADDITIONAL, "source", ImageRating.UNKNOWN, ImageDimensions.DUAL_WORK_MONITORS, "url", "tags", 75),
//                new IqdbMatch(IqdbMatchType.ADDITIONAL, "source", ImageRating.ERO, ImageDimensions.DUAL_WORK_MONITORS, "url", "tags", 75));
//
//        Assert.assertEquals(4, ratingSearch.getAllRatings().size());
//    }
//
//    public class Derp {
//        private String name;
//        public Derp(String name) {
//            this.name = name;
//        }
//        public String getName() {
//            return name;
//        }
//    }
//
//    @Test
//    public void derpyderp() {
//        Derp myMock = Mockito.mock(Derp.class);
//        Mockito.when(myMock.getName()).thenReturn("Aoi miyamori");
//        System.out.println(myMock.getName());
//
//        Derp myMock2 = Mockito.mock(Derp.class);
//        Mockito.when(myMock2.getName()).thenReturn(myMock.getName());
//        System.out.println("mymock2" + myMock2.getName());
//
//    }
}