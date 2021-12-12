package com.freenow.datatransferobject;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.freenow.domainvalue.GeoCoordinate;

import javax.validation.constraints.NotEmpty;
import java.util.Set;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class DriverDTO {
    private Long id;

    @NotEmpty(message = "Username can not be empty or null!!")
    private String username;

    @NotEmpty(message = "Password can not be empty or null!")
    private String password;

    private GeoCoordinate coordinate;

    private Set<CarDTO> carDTO;


    private DriverDTO() {
    }


    private DriverDTO(final Long id,
                      final String username,
                      final String password,
                      final GeoCoordinate coordinate,
                      final Set<CarDTO> carDTO) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.coordinate = coordinate;
        this.carDTO = carDTO;
    }


    public static DriverDTOBuilder newBuilder() {
        return new DriverDTOBuilder();
    }


    @JsonProperty
    public Long getId() {
        return id;
    }


    public String getUsername() {
        return username;
    }


    public String getPassword() {
        return password;
    }


    public GeoCoordinate getCoordinate() {
        return coordinate;
    }

    public Set<CarDTO> getCarDTO() {
        return carDTO;
    }

    public static class DriverDTOBuilder {
        private Long id;
        private String username;
        private String password;
        private GeoCoordinate coordinate;
        private Set<CarDTO> carDTO;

        public DriverDTOBuilder setId(Long id) {
            this.id = id;
            return this;
        }


        public DriverDTOBuilder setUsername(String username) {
            this.username = username;
            return this;
        }


        public DriverDTOBuilder setPassword(String password) {
            this.password = password;
            return this;
        }


        public DriverDTOBuilder setCoordinate(GeoCoordinate coordinate) {
            this.coordinate = coordinate;
            return this;
        }

        public DriverDTOBuilder setCarDTO(final Set<CarDTO> carDTO) {
            this.carDTO = carDTO;
            return this;
        }

        public DriverDTO createDriverDTO() {
            return new DriverDTO(id, username, password, coordinate, carDTO);
        }

    }
}