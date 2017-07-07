package DarkMage530.BulkImager.Md5;

import DarkMage530.BulkImager.Csv.ConsolidatedCsvEntries;
import DarkMage530.BulkImager.Csv.SingleCsvEntry;
import DarkMage530.BulkImager.Iqdb.SearchIqdb;
import DarkMage530.BulkImager.PictureFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class Md5Lookup {

    @Autowired
    private ConsolidatedCsvEntries entries;

    @Autowired
    private SearchIqdb searcher;

    public void lookup(PictureFile pictureFile) {
        List<SingleCsvEntry> locallyKnown = entries.get(pictureFile.getMd5());
        if (locallyKnown == null || locallyKnown.isEmpty()) {
            pictureFile.addNextAction(() -> searcher.searchBoorus(pictureFile) );
        }


    }
}
