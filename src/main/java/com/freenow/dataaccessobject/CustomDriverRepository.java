package com.freenow.dataaccessobject;

import com.freenow.datatransferobject.search.Rating;
import com.freenow.datatransferobject.search.SearchFilters;
import com.freenow.datatransferobject.search.SeatCount;
import com.freenow.domainobject.DriverDO;
import com.freenow.domainvalue.OnlineStatus;
import com.freenow.util.EngineType;
import com.freenow.util.ManufacturerName;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static org.springframework.data.jpa.domain.Specification.where;

@Component
@RequiredArgsConstructor
public class CustomDriverRepository {

    private static final String CRITERIA_ATTRIBUTE_CAR_DO = "carDO";

    public Specification<DriverDO> getSpecification(final SearchFilters searchFilters) {
        return where(isDeleted(searchFilters.getDeleted()))
                .and(where(onlineStatus(searchFilters.getOnlineStatus()))
                        .and(where(manufacturer(searchFilters.getManufacturerName()))
                                .and(where(seatCount(searchFilters.getSeatCount()))
                                        .and(where(isConvertible(searchFilters.getConvertible()))
                                                .and(where(engineType(searchFilters.getEngineType()))
                                                        .and(where(rating(searchFilters.getRating()))
                                                                .and(where(licensePlate(searchFilters.getLicensePlate())))
                                                        )
                                                )
                                        )
                                )
                        )
                );
    }

    private Specification<DriverDO> isDeleted(Boolean isDeleted) {
        if (Optional.ofNullable(isDeleted).isPresent()) {
            return (root, query, criteriaBuilder) -> criteriaBuilder.in(root.get("deleted")).value(isDeleted);
        }
        return null;
    }

    private Specification<DriverDO> onlineStatus(OnlineStatus status) {
        if (Optional.ofNullable(status).isPresent()) {
            return (root, query, criteriaBuilder) -> criteriaBuilder.in(root.get("onlineStatus")).value(status);
        }
        return null;
    }

    private Specification<DriverDO> manufacturer(ManufacturerName manufacturerName) {
        if (Optional.ofNullable(manufacturerName).isPresent()) {
            return (root, query, criteriaBuilder) -> criteriaBuilder.and(
                    criteriaBuilder.equal(root.joinSet(CRITERIA_ATTRIBUTE_CAR_DO)
                            .get("manufacturer"), manufacturerName));
        }
        return null;
    }

    private Specification<DriverDO> seatCount(SeatCount seatCount) {
        if (Optional.ofNullable(seatCount).isPresent()) {
            return (root, query, criteriaBuilder) -> criteriaBuilder
                    .between(root.joinSet(CRITERIA_ATTRIBUTE_CAR_DO).get("seatCount"), seatCount.getMin(), seatCount.getMax());
        }
        return null;
    }

    private Specification<DriverDO> isConvertible(Boolean isConvertible) {
        if (Optional.ofNullable(isConvertible).isPresent()) {
            return (root, query, criteriaBuilder) ->
                    criteriaBuilder.in(root.joinSet(CRITERIA_ATTRIBUTE_CAR_DO).get("convertible")).value(isConvertible);
        }
        return null;
    }

    private Specification<DriverDO> engineType(EngineType engineType) {
        if (Optional.ofNullable(engineType).isPresent()) {
            return (root, query, criteriaBuilder) -> criteriaBuilder.and(
                    criteriaBuilder.in(root.joinSet(CRITERIA_ATTRIBUTE_CAR_DO).get("engineType")).value(engineType));
        }
        return null;
    }

    private Specification<DriverDO> rating(Rating rating) {
        if (Optional.ofNullable(rating).isPresent()) {
            return (root, query, criteriaBuilder) -> criteriaBuilder
                    .between(root.joinSet(CRITERIA_ATTRIBUTE_CAR_DO).get("rating"), rating.getMin(), rating.getMax());
        }
        return null;
    }

    private Specification<DriverDO> licensePlate(String licensePlate) {
        if (Optional.ofNullable(licensePlate).isPresent()) {
            return (root, query, criteriaBuilder) -> criteriaBuilder
                    .like(root.joinSet(CRITERIA_ATTRIBUTE_CAR_DO).get("licensePlate"), licensePlate);
        }
        return null;
    }
}