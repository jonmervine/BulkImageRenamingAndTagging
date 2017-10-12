package DarkMage530.BulkImager.Metadata;

import DarkMage530.BulkImager.Csv.SingleCsvEntry;
import DarkMage530.BulkImager.ImageRating;
import com.drew.metadata.MetadataException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

public class CsvDbMetadata implements Metadata {

    @Autowired
    private RatingSearch ratingSearch;

    private List<SingleCsvEntry> csvEntries;

    public CsvDbMetadata(List<SingleCsvEntry> csvEntries) {
        this.csvEntries = csvEntries;
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
}
