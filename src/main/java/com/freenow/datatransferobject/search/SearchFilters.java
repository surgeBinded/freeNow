package com.freenow.datatransferobject.search;

import com.freenow.domainvalue.OnlineStatus;
import com.freenow.util.EngineType;
import com.freenow.util.ManufacturerName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SearchFilters {
    private Boolean deleted;
    private OnlineStatus onlineStatus;
    private Boolean convertible;
    private ManufacturerName manufacturerName;
    private EngineType engineType;
    private SeatCount seatCount;
    private Rating rating;
    private String licensePlate;
}