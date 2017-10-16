package DarkMage530.BulkImager.Metadata;

import DarkMage530.BulkImager.Csv.CsvDatabase;
import DarkMage530.BulkImager.Csv.SingleCsvEntry;
import DarkMage530.BulkImager.PictureFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DatabaseManager {

    @Autowired
    @Lazy
    private CsvDatabase allEntries;

    @Autowired
    @Lazy
    private CsvDatabase wallpaperEntries;

    @Autowired
    private MetadataFactory metadataFactory;

    public Metadata allLookup(PictureFile pictureFile) {
        List<SingleCsvEntry> locallyKnown = allEntries.get(pictureFile.getMd5());
        Metadata metadata = metadataFactory.buildMetadata(locallyKnown);
        return metadata;
    }

    public Metadata wallpaperLookup(PictureFile pictureFile) {
        List<SingleCsvEntry> localWallpaper = wallpaperEntries.get(pictureFile.getMd5());
        Metadata metadata = metadataFactory.buildMetadata(localWallpaper);
        return metadata;
    }

    public void updateWallpaper(PictureFile pictureFile, Metadata metadata) {
        wallpaperEntries.update(pictureFile.getMd5(), metadata.getEntries());
    }

    public void updateAllDb(PictureFile pictureFile, Metadata metadata) {
        allEntries.update(pictureFile.getMd5(), metadata.getEntries());
    }
}
