package main.File;

import main.Image;
import main.ImageRatios;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

/**
 * Created by Shirobako on 10/2/2016.
 */
public class FileMover {

    private static final Logger log = LoggerFactory.getLogger(FileMover.class);

    private File rootDirectory;

    private static final String WALLPAPER_DIRECTORY = "#Wallpapers";
    private static final String ORIGINAL_DIRECTORY = "#Originals";
    private static final String CROSSOVER_DIRECTORY = "#Crossovers";
    private static final String MANUAL_SORT_DIRECTORY = "#ManualSort";

    public FileMover(File rootDirectory) {
        this.rootDirectory = rootDirectory;
    }

    public File getOutputFile(Image image) {
        File destinationDirectory;
        try {
            ImageRatios ratio = ImageRatios.getImageRatio(image.getLocation());
            switch (ratio) {
                case CELL:
                case HOME_PC:
                case WORK_MONITORS:
                    File wallpaperDirectory = createDirectoryIfNotExist(rootDirectory, WALLPAPER_DIRECTORY);
                    File resolutionSpecificDirectory = createDirectoryIfNotExist(wallpaperDirectory, ratio.getResolution());
                    destinationDirectory = createDirectoryIfNotExist(resolutionSpecificDirectory, image.getRating());
                    break;
                case UNKNOWN:
                default:
                    destinationDirectory = createDirectoryIfNotExist(rootDirectory, ORIGINAL_DIRECTORY);
                    break;
            }

            return createOutputImageFilePath(image.getLocation(), destinationDirectory);
        } catch (FileMoverException e) {
            log.error("shit fucked up, try to move to errored folder", e);
            moveErroredFile(image.getLocation());
            return null;
        }
    }

    public File createDirectoryIfNotExist(File parentDirectory, String newDirectoryName) {
        return createDirectoryIfNotExist(new File(parentDirectory, newDirectoryName));
    }

    public File createDirectoryIfNotExist(File newDirectory) {
        if (!newDirectory.exists()) {
            boolean succeed = newDirectory.mkdirs();
            if (!succeed) {
                log.error("Failed to create directory "  + newDirectory.getPath());
            }
        }
        return newDirectory;
    }

    public File createOutputImageFilePath(File sourceFile, File destinationDirectory) {
        Path fileCopyTo = destinationDirectory.toPath().resolve(sourceFile.toPath().getFileName());
        int i = 2;
        while (Files.exists(fileCopyTo)) {
            String fileName = sourceFile.toPath().getFileName().toString();
            int indexPeriod = fileName.lastIndexOf(".");
            String newFileName = fileName.substring(0, indexPeriod) + " (" + i + ")" + fileName.substring(indexPeriod, fileName.length());
            fileCopyTo = destinationDirectory.toPath().resolve(newFileName);
            i++;
        }
        return fileCopyTo.toFile();
    }


    public void moveErroredFile(File file) {
            File destinationDirectory = createDirectoryIfNotExist(rootDirectory, MANUAL_SORT_DIRECTORY);
            File outputFile = createOutputImageFilePath(file, destinationDirectory);
            move(file, outputFile);
    }

    private void move(File sourceFile, File outputFile) {
        try {
            Files.move(sourceFile.toPath(), outputFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            log.error("Could not move file" + sourceFile.getPath() + " to  " + outputFile.getPath(), e);
        }
    }
}
