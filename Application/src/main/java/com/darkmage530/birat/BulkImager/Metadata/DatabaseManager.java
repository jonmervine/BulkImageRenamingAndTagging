package com.darkmage530.birat.BulkImager.Metadata;

import com.darkmage530.birat.BulkImager.Csv.CsvDatabase;
import com.darkmage530.birat.BulkImager.Csv.SingleCsvEntry;
import com.darkmage530.birat.BulkImager.PictureFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DatabaseManager {

    private static final Logger log = LoggerFactory.getLogger(DatabaseManager.class);

    @Autowired
    @Lazy
    private CsvDatabase allEntries;

    @Autowired
    @Lazy
    private CsvDatabase wallpaperEntries;

    @Autowired
    private MetadataFactory metadataFactory;

    public PictureFile allLookup(PictureFile pictureFile) {
        List<SingleCsvEntry> locallyKnown = allEntries.get(pictureFile.getMd5());
        Metadata metadata = metadataFactory.buildMetadata(locallyKnown);
        pictureFile.setMetadata(metadata);
        return pictureFile;
    }

    public PictureFile wallpaperLookup(PictureFile pictureFile) {
        List<SingleCsvEntry> localWallpaper = wallpaperEntries.get(pictureFile.getMd5());
        Metadata metadata = metadataFactory.buildMetadata(localWallpaper);
        pictureFile.setMetadata(metadata);
        return pictureFile;
    }

    public void updateWallpaper(PictureFile pictureFile) {
        if (pictureFile.getMetadata().isPresent()) {
            wallpaperEntries.update(pictureFile.getMd5(), pictureFile.getMetadata().get().getEntries());
        } else {
            log.error("We wanted to try to update wallpaper database but a metadata didn't exist for it");
        }
    }

    public void updateAllDb(PictureFile pictureFile, Metadata metadata) {
        allEntries.update(pictureFile.getMd5(), metadata.getEntries());
    }
}
