package DarkMage530.BulkImager.Csv;

import DarkMage530.BulkImager.BirtConfiguration;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by DarkMage530 on 7/1/2017. For BulkImageRenamingAndTagging
 * Current User Shirobako
 */
@Component("readerWriter")
public class CsvReaderWriter {

    @Autowired
    private BirtConfiguration config;

    public ListMultimap<String, SingleCsvEntry> importCsv(File csvFile) {
        try (CSVReader reader = new CSVReader(new FileReader(csvFile), config.getSeperator(), config.getQuoteChar(), 1)) {

            List<String[]> myEntries = reader.readAll();

            List<SingleCsvEntry> entries = myEntries.stream()
                    .map(entry -> new SingleCsvEntry(entry))
                    .collect(Collectors.toList());

            ListMultimap<String, SingleCsvEntry> multiMap = ArrayListMultimap.create();
            for (SingleCsvEntry entry : entries) {
                multiMap.put(entry.getMd5(), entry);
            }

            return multiMap;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        throw new RuntimeException("fuckedup");
    }

    public boolean exportCsv(ListMultimap<String, SingleCsvEntry> entries, File csvOutputFile) {
        try (CSVWriter writer = new CSVWriter(new FileWriter(csvOutputFile), config.getSeperator())){

            for (Map.Entry<String, SingleCsvEntry> entry : entries.entries()) {
                writer.writeNext(entry.getValue().csvLine());
            }

            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
