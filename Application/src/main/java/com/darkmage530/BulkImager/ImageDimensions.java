package com.darkmage530.BulkImager;

import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Shirobako on 4/24/2016.
 */
public enum ImageDimensions {
    //    HOME_PC(1920, 1080, "16:9"),
//    DUAL_HOME_PC(3840, 1080, "16:9"),
    CELL(1080, 1920, "9:16"),
    PIXEL5a(1080, 2400, "9:20"),
    //    WORK_MONITORS(2560, 1440, "16:9"),
//    DUAL_WORK_MONITORS(5120, 1440, "16:9"),
    UNKNOWN(0, 0, "0:0");
    private int width, height;
    private String aspectRatio;

    private static final Logger log = LoggerFactory.getLogger(ImageDimensions.class);
    private static final Map<String, ImageDimensions> dimensionMap = Maps.newHashMap();
    private static final Map<String, ImageDimensions> aspectRatiosMap = Maps.newHashMap();

    static {
        for (ImageDimensions dimension : ImageDimensions.values()) {
            aspectRatiosMap.put(dimension.aspectRatio, dimension);
            dimensionMap.put(dimension.getResolution(), dimension);
        }
    }

    ImageDimensions(int width, int height, String ratio) {
        this.width = width;
        this.height = height;
        this.aspectRatio = ratio;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public String getResolution() {
        return width + "x" + height;
    }

    public String getAspectRatio() {
        return aspectRatio;
    }

    public String getName() {
        return this.name();
    }

    public boolean isWallpaper() {
        return this != UNKNOWN;
    }

    public static ImageDimensions getDimensionOrAspect(int width, int height) {
        String givenResolution = width + "x" + height;
        if (dimensionMap.containsKey(givenResolution)) {
            return dimensionMap.get(givenResolution);
        }

        double ratioDivision = (double) width / height;
        double lowBound = 0.6;
        double highBound = 0.45;

        if (height > width && width > 1000 && height > 1900 && ratioDivision < lowBound && ratioDivision >= highBound) {
//            int gcdResult = ImageDimensions.gcd(width, height);
//            String myAspectRatio = (width / gcdResult) + ":" + (height / gcdResult);
//            if (aspectRatiosMap.containsKey(myAspectRatio)) {
                return CELL;
//            }
        }
        return UNKNOWN;
    }

    private static int gcd(int a, int b) {
        return (b == 0) ? a : gcd(b, a % b);
    }

    public static ImageDimensions getImageDimensions(String givenResolution) {

//        try {
//            throw new IOException("");
//        } catch (RuntimeException ex) {
//
//        }

        for (ImageDimensions dimension : ImageDimensions.values()) {
            if (dimension.getResolution().equalsIgnoreCase(givenResolution)) {
                return dimension;
            }
        }
        return UNKNOWN;
    }

    public static ImageDimensions getImageDimensions(File file) {
        try (ImageInputStream in = ImageIO.createImageInputStream(file)) {
            final Iterator<ImageReader> readers = ImageIO.getImageReaders(in);
            if (readers.hasNext()) {
                ImageReader reader = readers.next();
                try {
                    reader.setInput(in);
//                    return getImageDimensions(reader.getWidth(0) + "x" + reader.getHeight(0));
                    return getDimensionOrAspect(reader.getWidth(0), reader.getHeight(0));
                } finally {
                    reader.dispose();
                }
            }
        } catch (IOException ex) {
            log.error("Could not get imageRatio for file.path: " + file.getPath() + " Setting to UNKNOWN");
        }
        return UNKNOWN;
    }

    @Override
    public String toString() {
        return "ImageRatios: [" + getName() + ", " + getResolution() + "]";
    }
}
