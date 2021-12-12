package com.freenow.controller;

import com.freenow.controller.mapper.DriverMapper;
import com.freenow.datatransferobject.DriverDTO;
import com.freenow.datatransferobject.response.ResponseDriverDTO;
import com.freenow.datatransferobject.search.SearchFilters;
import com.freenow.domainobject.DriverDO;
import com.freenow.domainvalue.OnlineStatus;
import com.freenow.exception.*;
import com.freenow.service.driver.DriverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.freenow.util.AppConstants.*;

/**
 * All operations with a driver will be routed by this controller.
 * <p/>
 */
@RestController
@RequestMapping("v1/drivers")
public class DriverController {

    private final DriverService driverService;

    @Autowired
    public DriverController(final DriverService driverService) {
        this.driverService = driverService;
    }

    @GetMapping("/all")
    public ResponseDriverDTO getAllDrivers(
            @RequestParam(value = "pageNo", defaultValue = DEFAULT_PAGE_NUMBER, required = false) final int pageNo,
            @RequestParam(value = "pageSize", defaultValue = DEFAULT_PAGE_SIZE, required = false) final int pageSize,
            @RequestParam(value = "sortBy", defaultValue = DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = DEFAULT_SORT_DIRECTION, required = false) String sortDir
    ) {
        return driverService.getAllDrivers(pageNo, pageSize, sortBy, sortDir);
    }

    @GetMapping("/{driverId}")
    public DriverDTO getDriver(@PathVariable long driverId) throws EntityNotFoundException {
        return DriverMapper.makeDriverDTO(driverService.find(driverId));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DriverDTO createDriver(@Valid @RequestBody DriverDTO driverDTO) throws ConstraintsViolationException {
        DriverDO driverDO = DriverMapper.makeDriverDO(driverDTO);
        return DriverMapper.makeDriverDTO(driverService.create(driverDO));
    }

    @DeleteMapping("/{driverId}")
    public void deleteDriver(@PathVariable long driverId) throws EntityNotFoundException {
        driverService.delete(driverId);
    }

    @PutMapping("/{driverId}")
    public void updateLocation(
            @PathVariable long driverId, @RequestParam double longitude, @RequestParam double latitude)
            throws EntityNotFoundException {
        driverService.updateLocation(driverId, longitude, latitude);
    }

    @GetMapping
    public ResponseDriverDTO findDrivers(@RequestParam OnlineStatus onlineStatus,
                                         @RequestParam(value = "pageNo", defaultValue = DEFAULT_PAGE_NUMBER, required = false) final int pageNo,
                                         @RequestParam(value = "pageSize", defaultValue = DEFAULT_PAGE_SIZE, required = false) final int pageSize,
                                         @RequestParam(value = "sortBy", defaultValue = DEFAULT_SORT_BY, required = false) String sortBy,
                                         @RequestParam(value = "sortDir", defaultValue = DEFAULT_SORT_DIRECTION, required = false) String sortDir) {
        return driverService.find(onlineStatus, pageNo, pageSize, sortBy, sortDir);
    }

    @PatchMapping("assignCar/{driverId}/{carId}")
    public ResponseEntity<String> assignCarToDriver(
            @PathVariable long driverId, @PathVariable long carId) throws CarAlreadyInUseException, EntityNotFoundException, EntityDeletedException {
        driverService.assignCarToDriver(driverId, carId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PatchMapping("unassignCar/{driverId}/{carId}")
    public ResponseEntity<String> unassignCarToDriver(
            @PathVariable long driverId, @PathVariable long carId) throws EntityNotFoundException, CarNotAssignedToDriver {
        driverService.unassignCarFromDriver(driverId, carId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    /**
     * This is a search method that takes in searchFilters and returns the results that correspond to the said filters.
     * The filters are combined by an AND condition, meaning all the filters shall be true in order to have a result.
     *
     * @param searchFilters - the filters by which to search for drivers.
     *                      Example values for the search filters:
     *                      <p>
     *                      {
     *                      "deleted": false,
     *                      "onlineStatus": "OFFLINE",
     *                      "convertible": false,
     *                      "manufacturerName": "RENAULT",
     *                      "engineType": "GAS",
     *                      "seatCount": {
     *                      "min": 1,
     *                      "max": 5
     *                      },
     *                      "rating": {
     *                      "min": 3.0,
     *                      "max": 5.0
     *                      },
     *                      "licensePlate": "HH AS 2283"
     *                      }
     * @return a set of drivers that have the corresponding filters
     */
    @PostMapping("/search")
    public ResponseDriverDTO search(@RequestBody SearchFilters searchFilters,
                                    @RequestParam(value = "pageNo", defaultValue = DEFAULT_PAGE_NUMBER, required = false) final int pageNo,
                                    @RequestParam(value = "pageSize", defaultValue = DEFAULT_PAGE_SIZE, required = false) final int pageSize,
                                    @RequestParam(value = "sortBy", defaultValue = DEFAULT_SORT_BY, required = false) String sortBy,
                                    @RequestParam(value = "sortDir", defaultValue = DEFAULT_SORT_DIRECTION, required = false) String sortDir) {
        return driverService.search(searchFilters, pageNo, pageSize, sortBy, sortDir);
    }
}