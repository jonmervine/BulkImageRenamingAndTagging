package com.darkmage530.birat.BulkImager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Configuration
public class ConfigurationBuilder {

    private static final Logger log = LoggerFactory.getLogger(ConfigurationBuilder.class);

    @Bean
    public BirtConfiguration buildConfiguration() {
        log.info("buildConfiguration");
        Properties prop = new Properties();
        try (InputStream input = new FileInputStream("config.properties")) {
            prop.load(input);
        } catch (IOException ex) {
            log.error("Issue loading config.properties", ex);
            throw new RuntimeException(ex);
        }
        return new BirtConfiguration(prop);
    }
}
