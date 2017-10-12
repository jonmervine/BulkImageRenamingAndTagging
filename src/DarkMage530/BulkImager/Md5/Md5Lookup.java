package DarkMage530.BulkImager.Md5;

import DarkMage530.BulkImager.Csv.AllCsvDatabase;
import DarkMage530.BulkImager.Csv.SingleCsvEntry;
import DarkMage530.BulkImager.Csv.WallpaperCsvDatabase;
import DarkMage530.BulkImager.Metadata.CsvDbMetadata;
import DarkMage530.BulkImager.Metadata.Metadata;
import DarkMage530.BulkImager.PictureFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class Md5Lookup {

    @Autowired
    private AllCsvDatabase allEntries;

    @Autowired
    private WallpaperCsvDatabase wallpaperEntries;

    public Metadata allLookup(PictureFile pictureFile) {
        List<SingleCsvEntry> locallyKnown = allEntries.get(pictureFile.getMd5());
        Metadata metadata = new CsvDbMetadata(locallyKnown);
        return metadata;
    }

    public Metadata wallpaperLookup(PictureFile pictureFile) {
        List<SingleCsvEntry> localWallpaper = wallpaperEntries.get(pictureFile.getMd5());
        Metadata metadata = new CsvDbMetadata(localWallpaper);
        return metadata;
    }
}
