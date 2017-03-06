package DarkMage530.BulkImager.Output;

import DarkMage530.BulkImager.PictureFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

/**
 * Created by Shirobako on 10/2/2016.
 */
public class PictureOutput {

    private static final Logger log = LoggerFactory.getLogger(PictureOutput.class);


    public PictureFile move(PictureFile pictureFile, File moveRoot) {
        //TODO  not implemented
        return null;
    }

    public PictureFile copyWallpaper(PictureFile pictureFile, File moveRoot) {
        //where file should go in root
        OutputLocator locator = new OutputLocator();
        File destinationDirectory = locator.getWallpaperOutputLocation(pictureFile, moveRoot);
        log.debug("pictureFile's destination is: " + destinationDirectory.getPath());

        //what new file name is in root (duplicates)
        OutputNamer namer = new OutputNamer();
        File destination = namer.createSimpleOutputImageName(pictureFile, destinationDirectory);
        log.debug("pictureFile's name will be: " + destination.toPath().getFileName().toString());

        try {
            //copy file to new location
            OutputMover mover = new OutputMover();
            mover.copyFile(pictureFile, destination);
            log.info("Copied pictureFile to " + destination.getPath());
        } catch (OutputException e) {
            //TODO we need to handle if we can't move the file to the location we desired maybe the moveErroerd file method?
            log.error("OutputException attempting to copyFile. ", e);
            //temporary solution of setting destination to the current current dictory so we don't break anything else
            destination = pictureFile.asPath().toFile();
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
