package com.darkmage530.birat.BulkImager.Csv;

import com.darkmage530.birat.BulkImager.BirtConfiguration;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by DarkMage530 on 7/1/2017. For BulkImageRenamingAndTagging
 * Current User Shirobako
 */
@Lazy
@Component("allEntries")
public class AllCsvDatabase implements CsvDatabase {

    private static final Logger log = LoggerFactory.getLogger(AllCsvDatabase.class);

    @Autowired
    private BirtConfiguration config;

    @Autowired
    private CsvReaderWriter readerWriter;

    //String is md5
    private ListMultimap<String, SingleCsvEntry> entries = ArrayListMultimap.create();

    //TODO Removed a PostConstruct annotation here
    public void init() throws CsvException {
        log.info("Importing All Database");
        this.entries = readerWriter.importCsv(config.getAllDatabase());
    }

    @Override
    public void save() throws CsvException {
        readerWriter.exportCsv(entries, config.getAllDatabase());
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
