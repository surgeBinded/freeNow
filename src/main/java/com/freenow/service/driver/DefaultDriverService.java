package com.freenow.service.driver;

import com.freenow.controller.mapper.DriverMapper;
import com.freenow.dataaccessobject.CustomDriverRepository;
import com.freenow.dataaccessobject.DriverRepository;
import com.freenow.datatransferobject.response.ResponseDriverDTO;
import com.freenow.datatransferobject.search.SearchFilters;
import com.freenow.domainobject.CarDO;
import com.freenow.domainobject.DriverDO;
import com.freenow.domainvalue.GeoCoordinate;
import com.freenow.domainvalue.OnlineStatus;
import com.freenow.exception.*;
import com.freenow.util.FindEntityCheckedUtil;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

/**
 * Service to encapsulate the link between DAO and controller and to have business logic for some driver specific things.
 * <p/>
 */
@Service
public class DefaultDriverService implements DriverService {

    private final DriverRepository driverRepository;
    private final FindEntityCheckedUtil entityCheckedUtil;


    public DefaultDriverService(final DriverRepository driverRepository,
                                final FindEntityCheckedUtil entityCheckedUtil) {
        this.driverRepository = driverRepository;
        this.entityCheckedUtil = entityCheckedUtil;
    }


    /**
     * Selects a driver by id.
     *
     * @param driverId
     * @return found driver
     * @throws EntityNotFoundException if no driver with the given id was found.
     */
    @Override
    public DriverDO find(Long driverId) throws EntityNotFoundException {
        return entityCheckedUtil.findDriverChecked(driverId);
    }


    /**
     * Creates a new driver.
     *
     * @param driverDO
     * @return
     * @throws ConstraintsViolationException if a driver already exists with the given username, ... .
     */
    @Override
    public DriverDO create(DriverDO driverDO) throws ConstraintsViolationException {
        DriverDO driver;
        try {
            driver = driverRepository.save(driverDO);
        } catch (DataIntegrityViolationException e) {
            throw new ConstraintsViolationException(e.getMessage());
        }
        return driver;
    }


    /**
     * Deletes an existing driver by id.
     *
     * @param driverId
     * @throws EntityNotFoundException if no driver with the given id was found.
     */
    @Override
    @Transactional
    public void delete(Long driverId) throws EntityNotFoundException {
        DriverDO driverDO = entityCheckedUtil.findDriverChecked(driverId);
        driverDO.setDeleted(true);
    }


    /**
     * Update the location for a driver.
     *
     * @param driverId
     * @param longitude
     * @param latitude
     * @throws EntityNotFoundException
     */
    @Override
    @Transactional
    public void updateLocation(long driverId, double longitude, double latitude) throws EntityNotFoundException {
        DriverDO driverDO = entityCheckedUtil.findDriverChecked(driverId);
        driverDO.setCoordinate(new GeoCoordinate(latitude, longitude));
    }


    /**
     * Find all drivers by online state.
     *
     * @param onlineStatus
     */
    @Override
    public ResponseDriverDTO find(final OnlineStatus onlineStatus, final int pageNo, final int pageSize, final String sortBy, final String sortDir) {

        final var sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        final var pageable = PageRequest.of(pageNo, pageSize, sort);
        final var drivers = driverRepository.findByOnlineStatus(onlineStatus, pageable);
        final var content = DriverMapper.makeDriverDTOList(drivers.getContent());

        return new ResponseDriverDTO(content, drivers.getNumber(), drivers.getSize(), drivers.getTotalElements(), drivers.getTotalPages(), drivers.isLast());
    }


    @Override
    @Transactional
    public void assignCarToDriver(final long driverId, final long carId) throws EntityNotFoundException, CarAlreadyInUseException, EntityDeletedException {
        var driverDO = entityCheckedUtil.findDriverChecked(driverId);
        var carDO = entityCheckedUtil.findCarChecked(carId);

        checkIfDriverDeleted(driverDO);
        checkIfCarDeleted(carDO);

        entityCheckedUtil.findIfCarAlreadyAssigned(carDO, driverId);
        driverDO.getCarDO().add(carDO);
    }

    private void checkIfCarDeleted(final CarDO carDO) throws EntityDeletedException {
        if (Boolean.TRUE.equals(carDO.getDeleted())) {
            throw new EntityDeletedException("Car with id " + carDO.getId() + " is deleted and cannot be assigned");
        }
    }

    private void checkIfDriverDeleted(final DriverDO driverDO) throws EntityDeletedException {
        if (Boolean.TRUE.equals(driverDO.getDeleted())) {
            throw new EntityDeletedException("Driver with id " + driverDO.getId() + " is deleted and cannot be assigned a car");
        }
    }

    @Override
    @Transactional
    public void unassignCarFromDriver(final long driverId, final long carId) throws EntityNotFoundException, CarNotAssignedToDriver {
        var carDO = entityCheckedUtil.findCarChecked(carId);
        entityCheckedUtil.findDriverChecked(driverId); // this is needed to ensure that driver with such id exists.
        entityCheckedUtil.findIfCarAssignedToDriver(carDO, driverId)
                .getCarDO()
                .remove(carDO);
    }

    @Override
    public ResponseDriverDTO getAllDrivers(final int pageNo, final int pageSize, final String sortBy, final String sortDir) {

        final var sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        final var pageable = PageRequest.of(pageNo, pageSize, sort);
        final var drivers = driverRepository.findAll(pageable);
        final var content = DriverMapper.makeDriverDTOList(drivers.getContent());

        return new ResponseDriverDTO(content, drivers.getNumber(), drivers.getSize(), drivers.getTotalElements(), drivers.getTotalPages(), drivers.isLast());
    }

    @Override
    public ResponseDriverDTO search(final SearchFilters searchFilters, final int pageNo, final int pageSize, final String sortBy, final String sortDir) {
        final var sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        final var pageable = PageRequest.of(pageNo, pageSize, sort);
        final var drivers = driverRepository.findAll(CustomDriverRepository.getSpecification(searchFilters), pageable);
        final var content = DriverMapper.makeDriverDTOList(drivers.getContent().stream().distinct().collect(Collectors.toList()));

        return new ResponseDriverDTO(content, drivers.getNumber(), drivers.getSize(), content.size(), drivers.getTotalPages(), drivers.isLast());
    }
}