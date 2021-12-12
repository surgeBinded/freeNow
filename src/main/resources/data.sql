/**
 * CREATE Script for init of DB
 */

-- Create 3 OFFLINE drivers

insert into driver (id, date_created, deleted, online_status, password, username) values (1, now(), false, 'OFFLINE',
'driver01pw', 'driver01');

insert into driver (id, date_created, deleted, online_status, password, username) values (2, now(), false, 'OFFLINE',
'driver02pw', 'driver02');

insert into driver (id, date_created, deleted, online_status, password, username) values (3, now(), false, 'OFFLINE',
'driver03pw', 'driver03');


-- Create 3 ONLINE drivers

insert into driver (id, date_created, deleted, online_status, password, username) values (4, now(), false, 'ONLINE',
'driver04pw', 'driver04');

insert into driver (id, date_created, deleted, online_status, password, username) values (5, now(), true, 'ONLINE',
'driver05pw', 'driver05');

insert into driver (id, date_created, deleted, online_status, password, username) values (6, now(), false, 'ONLINE',
'driver06pw', 'driver06');

-- Create 1 OFFLINE driver with coordinate(longitude=9.5&latitude=55.954)

insert into driver (id, coordinate, date_coordinate_updated, date_created, deleted, online_status, password, username)
values
 (7,
 'aced0005737200226f72672e737072696e676672616d65776f726b2e646174612e67656f2e506f696e7431b9e90ef11a4006020002440001784400017978704023000000000000404bfa1cac083127', now(), now(), false, 'OFFLINE',
'driver07pw', 'driver07');

-- Create 1 ONLINE driver with coordinate(longitude=9.5&latitude=55.954)

insert into driver (id, coordinate, date_coordinate_updated, date_created, deleted, online_status, password, username)
values
 (8,
 'aced0005737200226f72672e737072696e676672616d65776f726b2e646174612e67656f2e506f696e7431b9e90ef11a4006020002440001784400017978704023000000000000404bfa1cac083127', now(), now(), false, 'ONLINE',
'driver08pw', 'driver08');

-- Create 6 available cars
insert into car (id, date_created, deleted, license_plate, seat_count, convertible, rating, manufacturer, engine_type) values (1, now(), false, 'HH AA 6574', 5, false, 4.5, 1, 2);
insert into car (id, date_created, deleted, license_plate, seat_count, convertible, rating, manufacturer, engine_type) values (2, now(), false, 'HH AS 7983', 4, false, 4.3, 2, 1);
insert into car (id, date_created, deleted, license_plate, seat_count, convertible, rating, manufacturer, engine_type) values (3, now(), false, 'HH AS 2283', 3, true, 4.0, 2, 1);
insert into car (id, date_created, deleted, license_plate, seat_count, convertible, rating, manufacturer, engine_type) values (4, now(), false, 'HH AS 3283', 2, false, 4.9, 1, 1);
insert into car (id, date_created, deleted, license_plate, seat_count, convertible, rating, manufacturer, engine_type) values (5, now(), false, 'HH AS 9883', 7, true, 4.7, 0, 1);
insert into car (id, date_created, deleted, license_plate, seat_count, convertible, rating, manufacturer, engine_type) values (6, now(), true, 'HH AS 9773', 6, true, 4.2, 0, 2);


-- Assign cars to drivers
insert into drivers_cars (driver_id, car_id) values (1, 1);
insert into drivers_cars (driver_id, car_id) values (1, 3);
insert into drivers_cars (driver_id, car_id) values (2, 2);