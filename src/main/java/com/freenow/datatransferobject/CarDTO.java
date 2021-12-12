package com.freenow.datatransferobject;

import com.freenow.util.EngineType;
import com.freenow.util.ManufacturerName;

import javax.validation.constraints.*;

public class CarDTO {

    private Long id;
    private Boolean deleted = false;

    @NotEmpty(message = "license plate cannot be null or empty!")
    private String licensePlate;

    @Min(value = 2, message = "maximum seat count can't be less than 2")
    @Max(value = 8, message = "maximum seat count can't be larger than 8")
    private short seatCount;
    private Boolean convertible;

    @DecimalMin(value = "0.0", message = "value can not be lower than 0")
    @DecimalMax(value = "5.0", message = "value can not be larger than 5")
    private Double rating;
    private ManufacturerName manufacturerName;
    private EngineType engineType;

    private CarDTO() {

    }

    private CarDTO(final Long id,
                   final Boolean deleted,
                   final String licensePlate,
                   final short seatCount,
                   final Boolean convertible,
                   final Double rating,
                   final ManufacturerName manufacturerName,
                   final EngineType engineType) {
        this.id = id;
        this.deleted = deleted;
        this.licensePlate = licensePlate;
        this.seatCount = seatCount;
        this.convertible = convertible;
        this.rating = rating;
        this.manufacturerName = manufacturerName;
        this.engineType = engineType;
    }

    public static CarDTOBuilder newBuilder() {
        return new CarDTOBuilder();
    }

    public Long getId() {
        return id;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public Double getRating() {
        return rating;
    }

    public ManufacturerName getManufacturerName() {
        return manufacturerName;
    }

    public short getSeatCount() {
        return seatCount;
    }

    public Boolean getConvertible() {
        return convertible;
    }

    public EngineType getEngineType() {
        return engineType;
    }

    public static class CarDTOBuilder {
        private Long id;
        private Boolean deleted;
        private String licensePlate;
        private short seatCount;
        private Boolean convertible;
        private Double rating;
        private ManufacturerName manufacturerName;
        private EngineType engineType;

        public CarDTOBuilder setId(final Long id) {
            this.id = id;
            return this;
        }

        public CarDTOBuilder setDeleted(final Boolean deleted) {
            this.deleted = deleted;
            return this;
        }

        public CarDTOBuilder setLicensePlate(final String licensePlate) {
            this.licensePlate = licensePlate;
            return this;
        }

        public CarDTOBuilder setRating(final Double rating) {
            this.rating = rating;
            return this;
        }

        public CarDTOBuilder setManufacturerName(final ManufacturerName manufacturerName) {
            this.manufacturerName = manufacturerName;
            return this;
        }

        public CarDTOBuilder setSeatCount(final short seatCount) {
            this.seatCount = seatCount;
            return this;
        }

        public CarDTOBuilder setConvertible(final Boolean convertible) {
            this.convertible = convertible;
            return this;
        }

        public CarDTOBuilder setEngineType(final EngineType engineType) {
            this.engineType = engineType;
            return this;
        }

        public CarDTO createCarDTO() {
            return new CarDTO(id, deleted, licensePlate, seatCount, convertible, rating, manufacturerName, engineType);
        }

    }
}