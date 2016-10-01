package main;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Created by Shirobako on 4/24/2016.
 */
public class FileNavigator {
    private File rootDirectory;
    private int fileCount = 0;
    private int ratioFoundCount = 0;
    private int ratioNotFoundCount = 0;

    private static final int SIXTEEN_NINE_WIDTH = 1920, SIXTEEN_NINE_HEIGHT = 1080;
    private static final String WALLPAPER_DIRECTORY = "D:\\Downloads\\1920x1080 Wallpapers";
    private static final String OTHER_DIRECTORY = "D:\\Downloads\\Others";


    public FileNavigator(File rootDirectory) {
        this.rootDirectory = rootDirectory;
    }

    private void recursivelyScanDirectories(File recusiveRoot) throws FileMoverException {
        for (File file : recusiveRoot.listFiles()) {
            if (file.isDirectory()) {
                recursivelyScanDirectories(file);
            } else if (file.getName().endsWith(".jpeg") ||
                    file.getName().endsWith(".jpg") ||
//                    file.getName().endsWith(".tiff") ||
                    file.getName().endsWith(".png") ||
                    file.getName().endsWith(".gif")) {
                fileCount++;
                try {
                    BufferedImage bimg = ImageIO.read(file);
                    int width = bimg.getWidth();
                    int height = bimg.getHeight();

                    if (width == SIXTEEN_NINE_WIDTH && height == SIXTEEN_NINE_HEIGHT) {
                        ratioFoundCount++;
                        found(file, bimg);
                    } else {
                        notFound(file, bimg);
                    }

                } catch (IOException ex) {
                    System.out.println("shit fucked up reading file");
                }
            }
        }
    }

    private void notFound(File file, BufferedImage bimg) throws FileMoverException {
        File otherDirectory = new File(OTHER_DIRECTORY);
        if (!otherDirectory.exists()) {
            boolean succeed = otherDirectory.mkdir();
            if (!succeed) {
                throw new FileMoverException("Failed to create otherDirectory");
            }
        }

        try {
            Path fileCopyTo = otherDirectory.toPath().resolve(file.toPath().getFileName());
            int i = 2;
            while (Files.exists(fileCopyTo)) {
                String fileName = file.toPath().getFileName().toString();
                int indexPeriod = fileName.lastIndexOf(".");
                String newFileName = fileName.substring(0, indexPeriod) + " (" + i + ")" + fileName.substring(indexPeriod, fileName.length());
                fileCopyTo = otherDirectory.toPath().resolve(newFileName);
                i++;
            }

            Driver.moveAndWriteFile(file, fileCopyTo.toFile());
//            Files.copy(file.toPath(), fileCopyTo, StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception ex) {
            throw new FileMoverException("shit fucked", ex);
        }
    }

    private void found(File file, BufferedImage bimg) throws FileMoverException {
        File wallpaperDirectory = new File(WALLPAPER_DIRECTORY);
        if (!wallpaperDirectory.exists()) {
            boolean succeed = wallpaperDirectory.mkdir();
            if (!succeed) {
                throw new FileMoverException("Failed to create wallpaperDirectory");
            }
        }

        try {
            Path fileCopyTo = wallpaperDirectory.toPath().resolve(file.toPath().getFileName());
            int i = 2;
            while (Files.exists(fileCopyTo)) {
                String fileName = file.toPath().getFileName().toString();
                int indexPeriod = fileName.lastIndexOf(".");
                String newFileName = fileName.substring(0, indexPeriod) + " (" + i + ")" + fileName.substring(indexPeriod, fileName.length());
                fileCopyTo = wallpaperDirectory.toPath().resolve(newFileName);
                i++;
            }

            Driver.moveAndWriteFile(file, fileCopyTo.toFile());
//            Files.copy(file.toPath(), fileCopyTo, StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception ex) {
            throw new FileMoverException("Failed somewhere", ex);
        }
    }

    public int scanDirectories() throws FileMoverException{
        recursivelyScanDirectories(rootDirectory);
        System.out.println("Filecount: " + fileCount);
        System.out.println("FileRatioFoundCount: " + ratioFoundCount);
        System.out.println("FileRatioNotFoundCount: " + ratioNotFoundCount);
        return fileCount;
    }

}
