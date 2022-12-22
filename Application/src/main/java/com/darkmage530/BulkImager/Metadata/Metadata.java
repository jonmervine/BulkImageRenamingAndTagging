package com.darkmage530.BulkImager.Metadata;

import DarkMage530.BulkImager.Csv.SingleCsvEntry;
import com.darkmage530.BulkImager.ImageRating;
import com.drew.metadata.MetadataException;

import java.util.List;

public interface Metadata {

    ImageRating getRating() throws MetadataException;

    List<ImageRating> getRatings();

    boolean isEmpty();

    List<SingleCsvEntry> getEntries();

}
