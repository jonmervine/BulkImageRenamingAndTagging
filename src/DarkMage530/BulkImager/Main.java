package DarkMage530.BulkImager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by DarkMage530 on 5/21/2017. For BulkImageRenamingAndTagging
 * Current User Shirobako
 */
@Configuration
@ComponentScan
public class Main {
    private static final Logger log = LoggerFactory.getLogger(Driver.class);

    public static void main(String[] args) {

        ApplicationContext context = new AnnotationConfigApplicationContext(Main.class);
        BirtConfiguration config = context.getBean(BirtConfiguration.class);
        Properties prop = new Properties();
        try (InputStream input = new FileInputStream("config.properties")) {
            prop.load(input);
        } catch (IOException ex) {
            log.error("Issue loading config.properties", ex);
            throw new RuntimeException(ex);
        }
        config.build(prop);

        Driver driver = context.getBean(Driver.class);
        driver.startThreads();

        try {
            driver.drive();
        } catch (Exception e) {
            log.error("Failure", e);
            driver.stopThreads();
            throw new RuntimeException(e);
        }
    }
}
