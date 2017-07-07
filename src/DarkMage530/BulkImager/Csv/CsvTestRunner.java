package DarkMage530.BulkImager.Csv;

import DarkMage530.BulkImager.BirtConfiguration;
import DarkMage530.BulkImager.Driver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.io.*;
import java.util.Properties;

/**
 * Created by DarkMage530 on 7/1/2017. For BulkImageRenamingAndTagging
 * Current User Shirobako
 */
@Configuration
@ComponentScan("DarkMage530.BulkImager")
public class CsvTestRunner {

    private static final Logger log = LoggerFactory.getLogger(Driver.class);

    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(CsvTestRunner.class);
        BirtConfiguration config = context.getBean(BirtConfiguration.class);
        Properties prop = new Properties();
        try (InputStream input = new FileInputStream("config.properties")) {
            prop.load(input);
        } catch (IOException ex) {
            log.error("Issue loading config.properties", ex);
            throw new RuntimeException(ex);
        }
        config.build(prop);

        CsvReaderWriter importer = context.getBean(CsvReaderWriter.class);
        ConsolidatedCsvEntries entries = importer.importCsv(config.getLocalDatabase());
        importer.exportCsv(entries, new File("D:\\Downloads\\myoutput.csv"));

        try {
//            importer.drive();
        } catch (Exception e) {
            log.error("Failure", e);
//            importer.stopThreads();
            throw new RuntimeException(e);
        }
    }

}
