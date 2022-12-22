package com.darkmage530.birat.BulkImager.Output;

import com.darkmage530.birat.BulkImager.PictureFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

/**
 * Created by Shirobako on 1/2/2017.
 */
@Component
//Package-private
class OutputMover {

    private static final Logger log = LoggerFactory.getLogger(OutputMover.class);

    /**
     * Will create any missing directories, if directories can't be created, OutputException will be thrown
     * @param sourceFile Must be a file not directory
     * @param outputFile Must be a file not directory
     * @return Will return false if file was not moved, true if it was moved
     * @throws OutputException
     */
    public boolean moveFile(File sourceFile, File outputFile) throws OutputException {
        if (!sourceFile.isFile() || !outputFile.isFile()) {
            log.warn("Either sourceFile " + sourceFile.getPath() + " or outputFile " + outputFile.getPath() + " is not actually a file.");
            return false;
        }
        createDirectoryIfNotExist(outputFile);
        try {
            Files.move(sourceFile.toPath(), outputFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new OutputException("Could not move file " + sourceFile.getPath() + " to " + outputFile.getPath(), e);
        }
        return true;
    }

    /**
     * Will create any missing directories, if directories can't be created, OutputException will be thrown
     * @param pictureFile Must be a file not directory, the picture you want to copy to a new location
     * @param outputFile Must be a file not directory, the directory you want to move the image to
     * @return Will return false if file was not copied, true if it was copied
     * @throws OutputException
     */
    public boolean copyFile(PictureFile pictureFile, File outputFile) throws OutputException {
        createDirectoryIfNotExist(outputFile);

        try {
            Files.copy(pictureFile.asPath(), outputFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            log.error("Could not copy file " + pictureFile.asPath() + " to " + outputFile.getPath(), e);
            throw new OutputException("Could not copy file " + pictureFile.asPath() + " to " + outputFile.getPath(), e);
        }

        return true;
    }

    private void createDirectoryIfNotExist(File outputFile) throws OutputException {
        File directory = outputFile.getParentFile();

        if (!directory.exists()) {
            boolean succeed = directory.mkdirs();
            if (!succeed) {
                log.warn("First Failure to create directory(ies): " + directory.getPath() + " Trying again.");
                succeed = directory.mkdirs();
                if (!succeed) {
                    log.error("Failed to create directories for output: " +  directory.getPath());
                    throw new OutputException("Failed to create directories for output: " +  directory.getPath());
                }
            }
        }
    }
}
