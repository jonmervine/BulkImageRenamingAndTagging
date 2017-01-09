package main.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

/**
 * Created by Shirobako on 1/2/2017.
 */
public class MoveFile {

    private static final Logger log = LoggerFactory.getLogger(MoveFile.class);

    /**
     * Will create any missing directories, if directories can't be created, FileMoverException will be thrown
     * @param sourceFile Must be a file not directory
     * @param outputFile Must be a file not directory
     * @return Will return false if file was not moved, true if it was moved
     * @throws FileMoverException
     */
    public boolean moveFile(File sourceFile, File outputFile) throws FileMoverException {
        if (!sourceFile.isFile() || !outputFile.isFile()) {
            log.warn("Either sourceFile " + sourceFile.getPath() + " or outputFile " + outputFile.getPath() + " is not actually a file.");
            return false;
        }
        createDirectoryIfNotExist(outputFile);
        try {
            Files.move(sourceFile.toPath(), outputFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            log.error("Could not move file " + sourceFile.getPath() + " to " + outputFile.getPath(), e);
            throw new FileMoverException("Could not move file " + sourceFile.getPath() + " to " + outputFile.getPath(), e);
        }
        return true;
    }

    /**
     * Will create any missing directories, if directories can't be created, FileMoverException will be thrown
     * @param sourceFile Must be a file not directory
     * @param outputFile Must be a file not directory
     * @return Will return false if file was not copied, true if it was copied
     * @throws FileMoverException
     */
    public boolean copyFile(File sourceFile, File outputFile) throws FileMoverException {
        if (!sourceFile.isFile() || !outputFile.isFile()) {
            log.warn("Either sourceFile " + sourceFile.getPath() + " or outputFile " + outputFile.getPath() + " is not actually a file.");
            return false;
        }
        createDirectoryIfNotExist(outputFile);
        try {
            Files.copy(sourceFile.toPath(), outputFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            log.error("Could not copy file " + sourceFile.getPath() + " to " + outputFile.getPath(), e);
            throw new FileMoverException("Could not copy file " + sourceFile.getPath() + " to " + outputFile.getPath(), e);
        }
        return true;
    }

    private void createDirectoryIfNotExist(File outputFile) throws FileMoverException {
        File directory = outputFile.getParentFile();

        if (!directory.exists()) {
            boolean succeed = directory.mkdirs();
            if (!succeed) {
                log.error("Failed to create directory(ies): "  + directory.getPath() + " Trying again.");
                succeed = directory.mkdirs();
                if (!succeed) {
                    log.error("Failed again to create directory(ies): " + directory.getPath());
                    throw new FileMoverException("Failed to create directories for output: " +  directory.getPath());
                }
            }
        }
    }
}
