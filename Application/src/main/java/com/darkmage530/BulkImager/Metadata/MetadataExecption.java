package com.darkmage530.BulkImager.Metadata;

/**
 * Created by Shirobako on 9/3/2016.
 */
public class MetadataExecption extends Exception {
    public MetadataExecption() {}

    public MetadataExecption(String message) {
        super(message);
    }

    public MetadataExecption(Throwable cause) {
        super(cause);
    }

    public MetadataExecption(String message, Throwable e) {
        super(message, e);
    }
}
