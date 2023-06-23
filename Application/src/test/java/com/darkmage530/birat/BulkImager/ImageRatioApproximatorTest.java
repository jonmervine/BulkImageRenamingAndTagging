package com.darkmage530.birat.BulkImager;

import com.google.common.collect.Sets;
import org.junit.Test;

import java.util.Set;

import static org.junit.Assert.assertEquals;

public class ImageRatioApproximatorTest {

    Set<String> acceptedRatio = Sets.newHashSet("5:4", "3:4", "4:3", "3:2", "7:5", "8:5", "5:3", "16:9", "9:16", "17:9", "21:9");


    @Test
    public void testderp() {
        assertEquals("16:9", dodo(1920, 1080));
        assertEquals("9:16", dodo(1080, 1920));
        assertEquals("16:9", dodo(2560, 1440));
        assertEquals("3:4", dodo(1200, 1600));
        assertEquals("16:9", dodo(1280, 720));
        assertEquals("8:5", dodo(1920, 1200));
        assertEquals("16:9", dodo(1366, 768));
        assertEquals("7:5", dodo(1440, 1024));
        assertEquals("16:9", dodo(1925, 1077));

    }

    public String dodo(int screenWidth, int screenHeight) {
        int factor = greatestCommonFactor(screenWidth, screenHeight);
        int widthRatio = screenWidth / factor;
        int heightRatio = screenHeight / factor;
        String ratio = widthRatio + ":" + heightRatio;
//        if (!acceptedRatio.contains(ratio)) {
//            return approximate(screenWidth, screenHeight);
//        }
        return ratio;
    }

    public String approximateRatio(int width, int height) {
        float lesserError = 999;
        String closestRatio = "";
        float myRatio = (float) width / (float) height;
        float ratioError = 0.0f;

        for (String goodRatio : acceptedRatio) {
            String[] split = goodRatio.split(":");
            int w = Integer.parseInt(split[0]);
            int h = Integer.parseInt(split[1]);
            float knownRatio = (float) w / (float) h;
            float distance = Math.abs(myRatio - knownRatio);
            if (distance < lesserError) {
                closestRatio = goodRatio;
                lesserError = distance;
                ratioError = distance;
            }
        }

        if (Math.floor(ratioError * 100.0f) / 100.0f > 0.0f) {
            closestRatio = width + ":" + height;
        }

        return closestRatio;
    }

    public int greatestCommonFactor(int width, int height) {
        return (height == 0) ? width : greatestCommonFactor(height, width % height);
    }
}
