package DarkMage530.BulkImager.Csv;

import DarkMage530.BulkImager.BirtConfiguration;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger log = LoggerFactory.getLogger(CsvReaderWriter.class);

    @Autowired
    private BirtConfiguration config;

    public ListMultimap<String, SingleCsvEntry> importCsv(File csvFile) throws CsvException {
        if (!csvFile.exists()) {
            try {
                csvFile.createNewFile();
            } catch (IOException e) {
                throw new CsvException("Attempted to create " + csvFile.getPath() + " but IOException, throwing CsvExceptino", e);
            }
        }
        try (CSVReader reader = new CSVReader(new FileReader(csvFile), config.getSeperator(), config.getQuoteChar(), 1)) {

            List<String[]> myEntries = reader.readAll();

            List<SingleCsvEntry> entries = myEntries.stream()
                    .map(SingleCsvEntry::new)
                    .collect(Collectors.toList());

            ListMultimap<String, SingleCsvEntry> multiMap = ArrayListMultimap.create();
            for (SingleCsvEntry entry : entries) {
                multiMap.put(entry.getMd5(), entry);
            }

            return multiMap;

        } catch (FileNotFoundException e) {
            throw new CsvException(csvFile.getName() + " not found trying to import CSV", e);
        } catch (IOException e) {
            throw new CsvException("IOException trying to import " + csvFile.getName(), e);
        }
    }

    public boolean exportCsv(ListMultimap<String, SingleCsvEntry> entries, File csvOutputFile) throws CsvException {
        if (!csvOutputFile.exists()) {
            try {
                csvOutputFile.createNewFile();
            } catch (IOException e) {
                throw new CsvException("Attempted to create " + csvOutputFile.getPath() + " for export but IOException, throwing CsvException", e);
            }
        }

        try (CSVWriter writer = new CSVWriter(new FileWriter(csvOutputFile), config.getSeperator())) {

            for (Map.Entry<String, SingleCsvEntry> entry : entries.entries()) {
                writer.writeNext(entry.getValue().csvLine());
            }

            return true;
        } catch (IOException e) {
            throw new CsvException("IOException while trying to export csv file " + csvOutputFile.getName(), e);
        }
    }
}
