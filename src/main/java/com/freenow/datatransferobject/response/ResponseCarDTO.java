package com.freenow.datatransferobject.response;

import com.freenow.datatransferobject.CarDTO;
import lombok.Data;

import java.util.List;

@Data
public class ResponseCarDTO {
    private final List<CarDTO> cars;
    private final int pageNo;
    private final int pageSize;
    private final long totalElements;
    private final int totalPages;
    private final boolean last;

    public ResponseCarDTO(final List<CarDTO> cars,
                          final int pageNo,
                          final int pageSize,
                          final long totalElements,
                          final int totalPages,
                          final boolean last) {
        this.cars = cars;
        this.pageNo = pageNo;
        this.pageSize = pageSize;
        this.totalElements = totalElements;
        this.totalPages = totalPages;
        this.last = last;
    }
}