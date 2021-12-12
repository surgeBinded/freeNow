package com.freenow.service.driver;

import com.freenow.datatransferobject.response.ResponseDriverDTO;
import com.freenow.datatransferobject.search.SearchFilters;
import com.freenow.domainobject.DriverDO;
import com.freenow.domainvalue.OnlineStatus;
import com.freenow.exception.*;

public interface DriverService {

    DriverDO find(Long driverId) throws EntityNotFoundException;

    DriverDO create(DriverDO driverDO) throws ConstraintsViolationException;

    void delete(Long driverId) throws EntityNotFoundException;

    void updateLocation(long driverId, double longitude, double latitude) throws EntityNotFoundException;

    ResponseDriverDTO find(final OnlineStatus onlineStatus, final int pageNo, final int pageSize, final String sortBy, final String sortDir);

    void assignCarToDriver(long driverId, long carId) throws EntityNotFoundException, CarAlreadyInUseException, EntityDeletedException;

    void unassignCarFromDriver(final long driverId, final long carId) throws EntityNotFoundException, CarNotAssignedToDriver;

    ResponseDriverDTO getAllDrivers(final int pageNo, final int pageSize, final String sortBy, final String sortDir);

    ResponseDriverDTO search(SearchFilters searchFilters, final int pageNo, final int pageSize, final String sortBy, final String sortDir);

}