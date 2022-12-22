package com.darkmage530.birat.BulkImager.Metadata;

import com.darkmage530.birat.BulkImager.Csv.SingleCsvEntry;
import com.darkmage530.birat.BulkImager.ImageRating;
import com.drew.metadata.MetadataException;

import java.util.List;

public interface Metadata {

    ImageRating getRating() throws MetadataException;

    List<ImageRating> getRatings();

    boolean isEmpty();

    List<SingleCsvEntry> getEntries();

}
