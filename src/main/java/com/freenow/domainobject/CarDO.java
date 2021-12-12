package com.freenow.domainobject;

import com.freenow.util.EngineType;
import com.freenow.util.ManufacturerName;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.time.ZonedDateTime;

@Entity
@Table(
        name = "car",
        uniqueConstraints = @UniqueConstraint(name = "uc_licensePlate", columnNames = {"licensePlate"})
)
public class CarDO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private ZonedDateTime dateCreated = ZonedDateTime.now();

    @Column(nullable = false)
    private Boolean deleted = false;

    @Column(nullable = false)
    @NotEmpty(message = "license plate cannot be null or empty!")
    private String licensePlate;

    @Column
    private short seatCount;

    @Column
    private Boolean convertible;

    @Column
    @DecimalMin(value = "0.0", message = "rating value can not be lower than 0")
    @DecimalMax(value = "5.0", message = "rating value can not be larger than 5")
    private Double rating;

    @Column
    private ManufacturerName manufacturer;

    @Column
    private EngineType engineType;

    private CarDO() {
    }

    public CarDO(String licensePlate,
                 short seatCount,
                 Boolean convertible,
                 Double rating,
                 ManufacturerName manufacturer,
                 EngineType engineType) {
        this.licensePlate = licensePlate;
        this.seatCount = seatCount;
        this.convertible = convertible;
        this.rating = rating;
        this.manufacturer = manufacturer;
        this.engineType = engineType;
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public ZonedDateTime getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(final ZonedDateTime dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(final Boolean deleted) {
        this.deleted = deleted;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(final String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public short getSeatCount() {
        return seatCount;
    }

    public void setSeatCount(final short seatCount) {
        this.seatCount = seatCount;
    }

    public Boolean getConvertible() {
        return convertible;
    }

    public void setConvertible(final Boolean convertible) {
        this.convertible = convertible;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(final Double rating) {
        this.rating = rating;
    }

    public ManufacturerName getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(final ManufacturerName manufacturer) {
        this.manufacturer = manufacturer;
    }

    public EngineType getEngineType() {
        return engineType;
    }

    public void setEngineType(final EngineType engineType) {
        this.engineType = engineType;
    }
}