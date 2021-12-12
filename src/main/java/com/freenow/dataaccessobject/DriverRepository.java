package com.freenow.dataaccessobject;

import com.freenow.domainobject.CarDO;
import com.freenow.domainobject.DriverDO;
import com.freenow.domainvalue.OnlineStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

/**
 * Database Access Object for driver table.
 * <p/>
 */
public interface DriverRepository extends JpaRepository<DriverDO, Long>, JpaSpecificationExecutor<DriverDO> {

    Page<DriverDO> findByOnlineStatus(OnlineStatus onlineStatus, Pageable pageable);

    Optional<DriverDO> findByCarDO(CarDO carDO);

}