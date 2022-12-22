package DarkMage530.BulkImager;

import org.junit.Test;

import static org.junit.Assert.*;

public class ImageDimensionsTest {

    @Test
    public void testAsepctRatio() {
        assertEquals(ImageDimensions.PIXEL5a, ImageDimensions.getDimensionOrAspect(1080, 2400));
        assertEquals(ImageDimensions.UNKNOWN, ImageDimensions.getDimensionOrAspect(2160, 2400));
        assertEquals(ImageDimensions.UNKNOWN, ImageDimensions.getDimensionOrAspect(1080, 4800));
        assertEquals(ImageDimensions.CELL, ImageDimensions.getDimensionOrAspect(1197, 2660));

    }

}