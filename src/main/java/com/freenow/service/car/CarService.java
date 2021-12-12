package com.freenow.service.car;

import com.freenow.datatransferobject.response.ResponseCarDTO;
import com.freenow.domainobject.CarDO;
import com.freenow.exception.ConstraintsViolationException;
import com.freenow.exception.EntityNotFoundException;
import com.freenow.util.EngineType;

import java.util.List;

public interface CarService {

    CarDO find(Long driverId) throws EntityNotFoundException;

    CarDO create(CarDO carDO) throws ConstraintsViolationException;

    void delete(Long carId) throws EntityNotFoundException;

    List<CarDO> find(EngineType engineType);

    ResponseCarDTO getAllCars(final int pageNo, final int pageSize, final String sortBy, final String sortDir);

    void updateCarRating(long carId, double carRating) throws EntityNotFoundException;
}