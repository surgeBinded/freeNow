package com.freenow.datatransferobject.response;

import com.freenow.datatransferobject.DriverDTO;
import lombok.Getter;

import java.util.List;

@Getter
public class ResponseDriverDTO {
    private final List<DriverDTO> drivers;
    private final int pageNo;
    private final int pageSize;
    private final long totalElements;
    private final int totalPages;
    private final boolean last;

    public ResponseDriverDTO(final List<DriverDTO> drivers,
                             final int pageNo,
                             final int pageSize,
                             final long totalElements,
                             final int totalPages,
                             final boolean last) {
        this.drivers = drivers;
        this.pageNo = pageNo;
        this.pageSize = pageSize;
        this.totalElements = totalElements;
        this.totalPages = totalPages;
        this.last = last;
    }
}