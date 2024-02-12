package com.freenow.service.car;

import com.freenow.controller.mapper.CarMapper;
import com.freenow.dataaccessobject.CarRepository;
import com.freenow.datatransferobject.response.ResponseCarDTO;
import com.freenow.domainobject.CarDO;
import com.freenow.exception.ConstraintsViolationException;
import com.freenow.exception.EntityNotFoundException;
import com.freenow.util.EngineType;
import com.freenow.util.FindEntityCheckedUtil;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DefaultCarService implements
        CarService {

    private final CarRepository carRepository;
    private final FindEntityCheckedUtil entityCheckedUtil;

    public DefaultCarService(final CarRepository carRepository,
                             final FindEntityCheckedUtil entityCheckedUtil) {
        this.carRepository = carRepository;
        this.entityCheckedUtil = entityCheckedUtil;
    }

    @Override
    public CarDO find(Long carId) throws EntityNotFoundException {
        return entityCheckedUtil.findCarChecked(carId);
    }

    @Override
    public CarDO create(CarDO carDO) throws ConstraintsViolationException {
        CarDO car;
        try {
            car = carRepository.save(carDO);
        } catch (DataIntegrityViolationException e) {
            throw new ConstraintsViolationException(e.getMessage());
        }
        return car;
    }

    @Override
    @Transactional
    public void delete(Long carId) throws EntityNotFoundException {
        final var carDO = entityCheckedUtil.findCarChecked(carId);
        carDO.setDeleted(true);
    }


    @Override
    public List<CarDO> find(EngineType engineType) {
        return carRepository.findByEngineType(engineType);
    }

    @Override
    public ResponseCarDTO getAllCars(final int pageNo, final int pageSize, final String sortBy, final String sortDir) {
        final var sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        final var pageable = PageRequest.of(pageNo, pageSize, sort);
        final var cars = carRepository.findAll(pageable);
        final var content = CarMapper.makeCarDTOList(cars.getContent());

        return new ResponseCarDTO(content, cars.getNumber(), cars.getSize(), cars.getTotalElements(), cars.getTotalPages(), cars.isLast());
    }

    @Override
    @Transactional
    public void updateCarRating(long carId, double carRating) throws EntityNotFoundException {
        CarDO carDO = entityCheckedUtil.findCarChecked(carId);
        carDO.setRating(carRating);
    }

}