package com.darkmage530.birat.BulkImager.Output;

/**
 * Created by Shirobako on 9/3/2016.
 */
//Package-private
class OutputException extends Exception {

    public OutputException() {}

    public OutputException(String message) {
        super(message);
    }

    public OutputException(Throwable cause) { super(cause); }

    public OutputException(String message, Throwable e) {
        super(message, e);
    }
}
