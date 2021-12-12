package com.freenow.domainobject;

import com.freenow.domainvalue.GeoCoordinate;
import com.freenow.domainvalue.OnlineStatus;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.time.ZonedDateTime;
import java.util.Set;

@Entity
@Table(
        name = "driver",
        uniqueConstraints = @UniqueConstraint(name = "uc_username", columnNames = {"username"})
)
public class DriverDO {
    @Column(nullable = false)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private final ZonedDateTime dateCreated = ZonedDateTime.now();

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @NotEmpty(message = "Username can not be empty or null!")
    private String username;

    @Column(nullable = false)
    @NotEmpty(message = "Password can not be empty or null!")
    private String password;

    @Column(nullable = false)
    private Boolean deleted = false;

    @Embedded
    private GeoCoordinate coordinate;

    @Column
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private ZonedDateTime dateCoordinateUpdated = ZonedDateTime.now();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OnlineStatus onlineStatus;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinTable(name = "drivers_cars",
            joinColumns = @JoinColumn(
                    name = "driver_id",
                    referencedColumnName = "id"
            ),
            inverseJoinColumns = @JoinColumn(
                    name = "car_id",
                    referencedColumnName = "id"
            )
    )
    private Set<CarDO> carDO;

    private DriverDO() {
    }

    public DriverDO(String username, String password) {
        this.username = username;
        this.password = password;
        this.deleted = false;
        this.coordinate = null;
        this.dateCoordinateUpdated = null;
        this.onlineStatus = OnlineStatus.OFFLINE;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public OnlineStatus getOnlineStatus() {
        return onlineStatus;
    }

    public void setOnlineStatus(OnlineStatus onlineStatus) {
        this.onlineStatus = onlineStatus;
    }

    public GeoCoordinate getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(GeoCoordinate coordinate) {
        this.coordinate = coordinate;
        this.dateCoordinateUpdated = ZonedDateTime.now();
    }

    public Set<CarDO> getCarDO() {
        return carDO;
    }

    public void setCarDO(final Set<CarDO> carDO) {
        this.carDO = carDO;
    }
}