package com.freenow.dataaccessobject;

import com.freenow.domainobject.CarDO;
import com.freenow.util.EngineType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CarRepository extends JpaRepository<CarDO, Long> {
    List<CarDO> findByEngineType(EngineType engineType);
}