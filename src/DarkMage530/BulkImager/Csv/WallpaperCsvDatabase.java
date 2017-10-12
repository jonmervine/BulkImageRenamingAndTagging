package DarkMage530.BulkImager.Csv;

import DarkMage530.BulkImager.BirtConfiguration;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("wallpaperDatabase")
public class WallpaperCsvDatabase implements CsvDatabase {

    @Autowired
    private BirtConfiguration config;

    @Autowired
    private CsvReaderWriter readerWriter;

    //String is md5
    private ListMultimap<String, SingleCsvEntry> entries = ArrayListMultimap.create();

    public WallpaperCsvDatabase() {
        this.entries = readerWriter.importCsv(config.getWallpaperDatabase());
    }

    @Override
    public void save() {
        readerWriter.exportCsv(entries, config.getWallpaperDatabase());
    }

    @Override
    public List<SingleCsvEntry> get(String md5) {
        return entries.get(md5);
    }

    @Override
    public void update(String md5, List<SingleCsvEntry> entry) {
        entries.replaceValues(md5, entry);
    }
}
