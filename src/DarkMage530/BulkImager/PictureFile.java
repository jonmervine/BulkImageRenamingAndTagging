package DarkMage530.BulkImager;

import DarkMage530.BulkImager.Iqdb.IqdbImage;
import com.google.common.collect.Lists;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

/**
 * Created by Shirobako on 3/5/2017.
 */
public class PictureFile {
    private Path path;
    private File file;
    private ImageRatios imageRatios;
    private IqdbImage iqdbImage;
    private File moveRoot;
    private String md5;

    public PictureFile(File file, File moveRoot) {
        this.file = file;
        this.path = file.toPath();
        this.imageRatios = ImageRatios.getImageRatio(file);
        this.moveRoot = moveRoot;

        try {
            FileInputStream fis = new FileInputStream(file);
            this.md5 = org.apache.commons.codec.digest.DigestUtils.md5Hex(fis);
            fis.close();
        } catch(IOException e) {
            System.out.println("couldn't read md5");
        }
    }

    public void updateFileLocation(File file) {
        this.file = file;
        this.path = file.toPath();
    }

    public void setIqdbImage(IqdbImage iqdbImage) {
        this.iqdbImage = iqdbImage;
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

    public String getMd5() { return md5; }

    public boolean isWallpaper() {
        return imageRatios.isWallpaper();
    }

    public ImageRating getRating() {
        if (iqdbImage == null) {
            return ImageRating.UNKNOWN;
        }
        else return iqdbImage.getBestRating();
    }

    public File getMoveRoot() {
        return moveRoot;
    }

    @Override
    public String toString() {
        return "PictureFile: [" + asPath() + ", " + getResolution() + "]";
    }
}
