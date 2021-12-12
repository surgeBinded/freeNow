package com.freenow.util;

import com.freenow.dataaccessobject.CarRepository;
import com.freenow.dataaccessobject.DriverRepository;
import com.freenow.domainobject.CarDO;
import com.freenow.domainobject.DriverDO;
import com.freenow.exception.CarAlreadyInUseException;
import com.freenow.exception.CarNotAssignedToDriver;
import com.freenow.exception.EntityNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class FindEntityCheckedUtil {

    private final DriverRepository driverRepository;
    private final CarRepository carRepository;

    public FindEntityCheckedUtil(final DriverRepository driverRepository, final CarRepository carRepository) {
        this.driverRepository = driverRepository;
        this.carRepository = carRepository;
    }

    public DriverDO findDriverChecked(final Long driverId) throws EntityNotFoundException {
        return driverRepository.findById(driverId)
                .orElseThrow(() -> new EntityNotFoundException("Could not find entity with id: " + driverId));
    }

    public CarDO findCarChecked(final Long carId) throws EntityNotFoundException {
        return carRepository.findById(carId)
                .orElseThrow(() -> new EntityNotFoundException("Could not find entity with id: " + carId));
    }

    public void findIfCarAlreadyAssigned(final CarDO carDO, Long driverId) throws CarAlreadyInUseException {
        final var driverDO = driverRepository.findByCarDO(carDO);
        if (driverDO.isPresent()) {
            throw new CarAlreadyInUseException("Car is already assigned to driver with id: " + driverId);
        }
    }

    public DriverDO findIfCarAssignedToDriver(final CarDO carDO, final Long driverId) throws CarNotAssignedToDriver {
        final var driverDO = driverRepository.findByCarDO(carDO);
        if (driverDO.isPresent() && Objects.equals(driverDO.get().getId(), driverId)) {
            return driverDO.get();
        } else {
            throw new CarNotAssignedToDriver("Car is not assigned to driver with id: " + driverId);
        }
    }
}