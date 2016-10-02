package main;

/**
 * Created by Shirobako on 4/24/2016.
 */
public enum ImageRatios {

    HOME_PC(1920, 1080),
    CELL(1080, 1920),
    WORK_MONITORS(2560, 1440),
    UNKNOWN(0,0);

    private int width, height;

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

    public String getResolution() { return width + "x" + height; }

    public static ImageRatios getImageRatio(String resolution) {
        for (ImageRatios ratio : ImageRatios.values()) {
            if (ratio.getResolution().equalsIgnoreCase(resolution)) {
                return ratio;
            }
        }
        return UNKNOWN;
    }
}
