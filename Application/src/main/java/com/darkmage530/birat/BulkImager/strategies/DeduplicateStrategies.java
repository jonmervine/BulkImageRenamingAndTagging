package com.darkmage530.birat.BulkImager.strategies;

import com.darkmage530.birat.BulkImager.BirtConfiguration;
import com.darkmage530.birat.BulkImager.Driver;
import com.darkmage530.birat.tags.TagCategory;
import com.darkmage530.birat.tags.TagRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.Map;
import java.util.Set;

@Component
public class DeduplicateStrategies implements Activity {

    private static final Logger log = LoggerFactory.getLogger(Driver.class);

    private BirtConfiguration config;

    public DeduplicateStrategies(BirtConfiguration config) {
        this.config = config;
    }

    @Override
    public void foundPicture(File file) {

    }
}
