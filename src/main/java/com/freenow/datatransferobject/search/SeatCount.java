package com.freenow.datatransferobject.search;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Getter
@Setter
@AllArgsConstructor
public class SeatCount {

    @Min(value = 2, message = "minimum seat count can't be less than 2")
    @Max(value = 7, message = "minimum seat count can't be larger than 7")
    private short min;

    @Min(value = 3, message = "maximum seat count can't be less than 3")
    @Max(value = 8, message = "maximum seat count can't be larger than 8")
    private short max;
}