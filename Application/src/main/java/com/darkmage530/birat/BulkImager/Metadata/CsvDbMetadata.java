package com.darkmage530.birat.BulkImager.Metadata;

import com.darkmage530.birat.BulkImager.Csv.SingleCsvEntry;
import com.darkmage530.birat.BulkImager.ImageRating;
import com.drew.metadata.MetadataException;

import java.util.List;
import java.util.stream.Collectors;

public class CsvDbMetadata implements Metadata {

    private RatingSearch ratingSearch;

    private List<SingleCsvEntry> csvEntries;

    public CsvDbMetadata(RatingSearch ratingSearch, List<SingleCsvEntry> csvEntries) {
        this.csvEntries = csvEntries;
        this.ratingSearch = ratingSearch;
    }

    @Override
    public ImageRating getRating() throws MetadataException {
        return ratingSearch.getRatingByConfig(getRatings());
    }

    @Override
    public List<ImageRating> getRatings() {
        return csvEntries.stream().map( csv -> csv.getRating()).collect(Collectors.toList());
    }

    @Override
    public boolean isEmpty() {
        return csvEntries.isEmpty();
    }

    @Override
    public List<SingleCsvEntry> getEntries() {
        return csvEntries;
    }


}
