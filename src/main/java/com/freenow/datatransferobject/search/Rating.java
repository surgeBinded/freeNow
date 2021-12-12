package com.freenow.datatransferobject.search;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;

@Getter
@Setter
@AllArgsConstructor
public class Rating {

    @DecimalMin(value = "0.0", message = "value can not be lower than 0")
    @DecimalMax(value = "5.0", message = "value can not be larger than 5")
    private Double min;

    @DecimalMin(value = "0.0", message = "value can not be lower than 0")
    @DecimalMax(value = "5.0", message = "value can not be larger than 5")
    private Double max;
}