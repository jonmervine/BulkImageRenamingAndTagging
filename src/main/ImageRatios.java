package main;

/**
 * Created by Shirobako on 4/24/2016.
 */
public enum ImageRatios {

//    FIVE_FOUR(1280, 1024, 5, 4),
//    FOUR_THREE(1600, 1200, 4, 3),
//    THREE_TWO(1440, 960, 3, 2),
//    EIGHT_FIVE(2560, 1600, 8, 5),
    SIXTEEN_NINE(1920, 1080, "16:9");
//    FIVE_THREE(1280, 768, 5, 3),
//    SEVENTEEN_NINE(2048, 1080, 17, 9),
//    TWENTYONE_NINE(2560, 1080, 21, 9);

    private int width, height, ratioWidth, ratioHeight;
    String ratio;

    ImageRatios(int width, int height, String ratio) {
        this.width = width;
        this.height = height;
        this.ratio = ratio;
//        this.ratioWidth = ratioWidth;
//        this.ratioHeight = ratioHeight;
    }

    public double getDividedRatio() {
        return ((width * 1.0 / height * 1.0) * 1000) / 1000;
    }

    public String getRatio() { return ratio; }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public int getRatioWidth() { return ratioWidth; }

    public int getRatioHeight() { return ratioHeight; }

}
