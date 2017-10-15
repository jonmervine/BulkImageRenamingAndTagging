package DarkMage530.BulkImager.Csv;

import java.util.List;

public interface CsvDatabase {

    void save() throws CsvException;

    List<SingleCsvEntry> get(String md5);

    void update(String md5, List<SingleCsvEntry> entry);

}
