package main.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Created by Shirobako on 1/2/2017.
 */
public class OutputName {

    private static final Logger log = LoggerFactory.getLogger(OutputName.class);

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
}
