package com.freenow.datatransferobject.response;

import com.freenow.domainobject.DriverDO;
import lombok.Data;

import java.util.Set;

@Data
public class SearchResponse {
    private int pageNo;
    private int pageSize;
    private long totalElements;
    private int totalPages;
    private boolean last;
    private Set<DriverDO> foundDrivers;
}