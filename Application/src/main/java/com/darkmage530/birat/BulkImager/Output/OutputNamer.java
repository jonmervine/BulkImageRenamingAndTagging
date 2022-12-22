package com.darkmage530.birat.BulkImager.Output;

import com.darkmage530.birat.BulkImager.Metadata.Metadata;
import com.darkmage530.birat.BulkImager.PictureFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Created by Shirobako on 1/2/2017.
 */
@Component
//Package-private
class OutputNamer {

    private static final Logger log = LoggerFactory.getLogger(OutputNamer.class);

    public File createSimpleOutputImageName(PictureFile pictureFile, File destinationDirectory) {
        Path fileCopyTo = destinationDirectory.toPath().resolve(pictureFile.getFileName());
        int i = 2;
        while (Files.exists(fileCopyTo)) {
            String fileName = pictureFile.getFileName().toString();
            int indexPeriod = fileName.lastIndexOf(".");
            String newFileName = fileName.substring(0, indexPeriod) + " (" + i + ")" + fileName.substring(indexPeriod, fileName.length());
            fileCopyTo = destinationDirectory.toPath().resolve(newFileName);
            i++;
        }
        return fileCopyTo.toFile();
    }

    public File createOutputImageNameWithTags(PictureFile pictureFile, File destinationDirectory, Metadata metadata) {
        //construct filename with tags


        Path fileCopyTo = destinationDirectory.toPath().resolve(pictureFile.getFileName());
        int i = 2;
        while (Files.exists(fileCopyTo)) {
            String fileName = pictureFile.getFileName().toString();
            int indexPeriod = fileName.lastIndexOf(".");
            String newFileName = fileName.substring(0, indexPeriod) + " (" + i + ")" + fileName.substring(indexPeriod, fileName.length());
            fileCopyTo = destinationDirectory.toPath().resolve(newFileName);
            i++;
        }
        return fileCopyTo.toFile();
    }
}
