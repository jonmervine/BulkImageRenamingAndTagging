package com.darkmage530.BulkImager.Csv;

/**
 * Created by DarkMage530 on 10/15/2017. For BulkImageRenamingAndTagging
 * Current User Shirobako
 */
public class CsvException extends Exception {
    public CsvException() {}

    public CsvException(String message) {
        super(message);
    }

    public CsvException(Throwable cause) {
        super(cause);
    }

    public CsvException(String message, Throwable e) {
        super(message, e);
    }
}
