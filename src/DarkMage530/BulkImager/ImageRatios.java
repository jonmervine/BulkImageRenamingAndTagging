package DarkMage530.BulkImager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;

/**
 * Created by Shirobako on 4/24/2016.
 */
public enum ImageRatios {
    HOME_PC(1920, 1080),
    DUAL_HOME_PC(3840, 1080),
    CELL(1080, 1920),
    WORK_MONITORS(2560, 1440),
    DUAL_WORK_MONITORS(5120, 1440),
    UNKNOWN(0, 0);
    private int width, height;

    private static final Logger log = LoggerFactory.getLogger(ImageRatios.class);

    ImageRatios(int width, int height) {
        this.width = width;
        this.height = height;
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

    public String getName() {
        return this.name();
    }

    public boolean isWallpaper() {
        return this != UNKNOWN;
    }

    public static ImageRatios getImageRatio(String resolution) {
        for (ImageRatios ratio : ImageRatios.values()) {
            if (ratio.getResolution().equalsIgnoreCase(resolution)) {
                return ratio;
            }
        }
        return UNKNOWN;
    }

    public static ImageRatios getImageRatio(File file) {
        try (ImageInputStream in = ImageIO.createImageInputStream(file)) {
            final Iterator<ImageReader> readers = ImageIO.getImageReaders(in);
            if (readers.hasNext()) {
                ImageReader reader = readers.next();
                try {
                    reader.setInput(in);
                    return getImageRatio(reader.getWidth(0) + "x" + reader.getHeight(0));
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
