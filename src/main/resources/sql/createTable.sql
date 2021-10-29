CREATE TABLE hunter
(
    id                   INTEGER PRIMARY KEY,
    name                 VARCHAR(255),
    death_location_x     INTEGER   DEFAULT 0,
    death_location_y     INTEGER   DEFAULT 0,
    death_location_z     INTEGER   DEFAULT 0,
    number_of_times_died INTEGER   DEFAULT 0,
    hunt_start_time      TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);