package com.freenow.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class CarNotAssignedToDriver extends Exception {

    static final long serialVersionUID = -3387516993224229948L;


    public CarNotAssignedToDriver(String message) {
        super(message);
    }

}