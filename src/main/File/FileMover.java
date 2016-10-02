package main.File;

import main.Image;
import main.ImageRatios;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    public void moveFile(Image image, int width, int height) {
        try {
            ImageRatios ratio = ImageRatios.getImageRatio(width + "x" + height);

            switch(ratio) {
                case CELL:
                case HOME_PC:
                case WORK_MONITORS:
                    moveWallpaper(ratio, image);
                    break;
                case UNKNOWN:
                default:
                    movePicture(image);
                    break;
            }
        } catch (FileMoverException e) {
            moveErroredFile(image.getLocation());
            log.error("shit fucked up, try to move to errored folder", e);
        }
    }

    private void moveWallpaper(ImageRatios ratio, Image image) throws FileMoverException {
        File wallpaperDirectory = createDirectoryIfNotExist(rootDirectory, WALLPAPER_DIRECTORY);
        File resolutionSpecificDirectory = createDirectoryIfNotExist(wallpaperDirectory, ratio.getResolution());
        File ratingDirectory = createDirectoryIfNotExist(resolutionSpecificDirectory, image.getRating());

        move(image.getLocation(), ratingDirectory);
    }

    private File createDirectoryIfNotExist(File parentDirectory, String newDirectoryName) throws FileMoverException {
        File newDirectory = new File(parentDirectory, newDirectoryName);
        if (!newDirectory.exists()) {
            boolean succeed = newDirectory.mkdir();
            if (!succeed) {
                throw new FileMoverException("Failed to create directory " + newDirectory.getPath());
            }
        } else if (!newDirectory.isDirectory()) {
            throw new FileMoverException("Wallpaper directory isn't a directory: " + newDirectory.getPath());
        }
        return newDirectory;
    }

    private void move(File sourceFile, File destinationDirectory) {
        try {

            File outputFile = createOutputImageFilePath(sourceFile, destinationDirectory);

            Files.copy(sourceFile.toPath(), outputFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e ) {
            log.error("could not copy file " + sourceFile.getPath() + " to location " + destinationDirectory.getPath());
        }
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

    private void movePicture(Image image){
        try {
            File originalDirectory = createDirectoryIfNotExist(rootDirectory, ORIGINAL_DIRECTORY);

            move(image.getLocation(), originalDirectory);
        } catch (FileMoverException e) {
            log.error("Error moving picture", e);
        }
    }

    public void moveErroredFile(File image) {
        try {
           File manualSortDirectory = createDirectoryIfNotExist(rootDirectory, MANUAL_SORT_DIRECTORY);

            move(image, manualSortDirectory);
        } catch (FileMoverException e) {
            log.error("error Moving Errored File", e);
        }

    }

}
