CREATE TABLE hunter
(
    id                   INTEGER PRIMARY KEY NOT NULL AUTOINCREMENT,
    name                 VARCHAR(255),
    death_location_x     REAL      DEFAULT 0.0,
    death_location_y     REAL      DEFAULT 0.0,
    death_location_z     REAL      DEFAULT 0.0,
    number_of_times_died INTEGER   DEFAULT 0,
    hunt_start_time      TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);