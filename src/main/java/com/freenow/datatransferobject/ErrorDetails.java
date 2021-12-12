package com.freenow.datatransferobject;

import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;

@Getter
@Setter
public class ErrorDetails {
    private ZonedDateTime timeStamp;
    private String message;
    private String details;

    public ErrorDetails(ZonedDateTime timeStamp,
                        String message,
                        String details) {
        this.timeStamp = timeStamp;
        this.message = message;
        this.details = details;
    }
}