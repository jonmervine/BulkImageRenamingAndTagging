package DarkMage530.BulkImager.Csv;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Created by DarkMage530 on 7/1/2017. For BulkImageRenamingAndTagging
 * Current User Shirobako
 */
@Component
public class ConsolidatedCsvEntries {

    //String is md5
    private ListMultimap<String, SingleCsvEntry> entries = ArrayListMultimap.create();

    public void consolidate(SingleCsvEntry entry) {
        entries.put(entry.getMd5(), entry);
    }

    public Collection<Map.Entry<String, SingleCsvEntry>> getEntrySet() {
        return entries.entries();
    }

    public List<SingleCsvEntry> get(String md5) { return entries.get(md5); }

}
