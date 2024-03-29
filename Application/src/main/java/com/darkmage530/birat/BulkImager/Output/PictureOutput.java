package com.darkmage530.birat.BulkImager.Output;

import com.darkmage530.birat.BulkImager.PictureFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

/**
 * Created by Shirobako on 10/2/2016.
 */
@Component
public class PictureOutput {

    private final OutputLocator locator;

    private final OutputMover mover;

    private final OutputNamer namer;

    private static final Logger log = LoggerFactory.getLogger(PictureOutput.class);

    public PictureOutput(OutputLocator locator, OutputMover mover, OutputNamer namer) {
        this.locator = locator;
        this.mover = mover;
        this.namer = namer;
    }

    //janking this
    public PictureFile move(PictureFile pictureFile, String directoryName) {
        File destinationFile = new File(pictureFile.getMoveRoot().toPath().resolve(directoryName).resolve(pictureFile.getFileName()).toUri());

        try {
            mover.copyFile(pictureFile, destinationFile);
            Files.delete(pictureFile.asPath());
        } catch (OutputException e) {
            System.out.println("error copying file " + pictureFile.getFileName());
        } catch (IOException e) {
            System.out.println("error deleting file " + pictureFile.getFileName());
        }
        return null;
    }

    public PictureFile copy(PictureFile pictureFile) {
        //where file should go in root
        File destinationDirectory = locator.getWallpaperOutputLocation(pictureFile);
        log.debug("pictureFile's destination is: " + destinationDirectory.getPath());

        //what new file name is in root (duplicates)
        File destination = namer.createSimpleOutputImageName(pictureFile, destinationDirectory);
        log.debug("pictureFile's name will be: " + destination.toPath().getFileName().toString());

        try {
            //copy file to new location
            mover.copyFile(pictureFile, destination);
            log.info("Copied pictureFile to " + destination.getPath());
        } catch (OutputException e) {
            //TODO we need to handle if we can't move the file to the location we desired maybe the moveErroerd file method?
            log.error("OutputException attempting to copyFile. ", e);
            //temporary solution of setting destination to the current dictory so we don't break anything else
            destination = pictureFile.asFile();
        }

        //update picturefile with new location
        pictureFile.updateFileLocation(destination);

        //return file
        return pictureFile;
    }



   /* public void moveErroredFile(Output file) {
            Output destinationDirectory = createDirectoryIfNotExist(rootDirectory, MANUAL_SORT_DIRECTORY);
            Output outputFile = createOutputImageFilePath(file, destinationDirectory);
            move(file, outputFile);
    }*/


}
