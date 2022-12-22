package com.darkmage530.BulkImager.Metadata;

import DarkMage530.BulkImager.Csv.SingleCsvEntry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by DarkMage530 on 10/15/2017. For BulkImageRenamingAndTagging
 * Current User Shirobako
 */
@Component
public class MetadataFactory {

    @Autowired
    private RatingSearch ratingSearch;

    public Metadata buildMetadata(List<SingleCsvEntry> entries) {
        return new CsvDbMetadata(ratingSearch, entries);
    }
}
