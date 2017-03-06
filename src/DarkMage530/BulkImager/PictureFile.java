package DarkMage530.BulkImager;

import java.io.File;
import java.nio.file.Path;

/**
 * Created by Shirobako on 3/5/2017.
 */
public class PictureFile {
    private Path path;
    private File file;
    private ImageRatios imageRatios;

    public PictureFile(File file) {
        this.file = file;
        this.path = file.toPath();
        this.imageRatios = ImageRatios.getImageRatio(file);
    }

    public void updateFileLocation(File file) {
        this.file = file;
        this.path = file.toPath();
    }

    public File asFile() { return file; }

    public Path getFileName() {
        return path.getFileName();
    }

    public Path asPath() {
        return path;
    }

    public int getWidth() {
        return imageRatios.getWidth();
    }

    public int getHeight() {
        return imageRatios.getHeight();
    }

    public String getResolution() {
        return imageRatios.getResolution();
    }

    public ImageRatios getImageRatio() {
        return imageRatios;
    }

    public boolean isWallpaper() {
        return imageRatios.isWallpaper();
    }

    @Override
    public String toString() {
        return "PictureFile: [" + asPath() + ", " + getResolution() + "]";
    }
}
