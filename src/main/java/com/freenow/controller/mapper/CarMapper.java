package com.freenow.controller.mapper;

import com.freenow.datatransferobject.CarDTO;
import com.freenow.domainobject.CarDO;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class CarMapper {

    private CarMapper() {

    }

    public static CarDO makeCarDO(CarDTO carDTO) {
        return new CarDO(
                carDTO.getLicensePlate(),
                carDTO.getSeatCount(),
                carDTO.getConvertible(),
                carDTO.getRating(),
                carDTO.getManufacturerName(),
                carDTO.getEngineType()
        );
    }

    public static CarDTO makeCarDTO(CarDO carDO) {
        CarDTO.CarDTOBuilder carDTOBuilder = CarDTO.newBuilder()
                .setId(carDO.getId())
                .setDeleted(carDO.getDeleted())
                .setLicensePlate(carDO.getLicensePlate())
                .setRating(carDO.getRating())
                .setManufacturerName(carDO.getManufacturer())
                .setConvertible(carDO.getConvertible())
                .setEngineType(carDO.getEngineType())
                .setSeatCount(carDO.getSeatCount());
        return carDTOBuilder.createCarDTO();
    }

    public static List<CarDTO> makeCarDTOList(Collection<CarDO> cars) {
        if (cars != null) {
            return cars.stream()
                    .map(CarMapper::makeCarDTO)
                    .collect(Collectors.toList());
        }
        return List.of();
    }

    public static Set<CarDTO> makeCarDTOSet(Collection<CarDO> cars) {
        if (cars != null) {
            return cars.stream()
                    .map(CarMapper::makeCarDTO)
                    .collect(Collectors.toSet());
        }
        return Set.of();
    }
}