# Title: Creating DB
# Authors: Sarah Taylor and Diego Cicotoste
# Semester: Summer I 2025

library(DBI)
library(RMySQL)

con <- dbConnect(
  RMySQL::MySQL(),
  dbname = "defaultdb",
  host = "mysql-377c8670-northeastern-d772.d.aivencloud.com",
  port = 23993,
  user = "avnadmin",
  password = "AVNS_C18DfZKwt6EZpZbhpOv"
)

# Creates the Flights table
dbExecute(con, "
  CREATE TABLE Flights (
    flight_number VARCHAR(50) PRIMARY KEY,
    airline VARCHAR(50) NOT NULL,
    aircraft VARCHAR(50) NOT NULL,
    dep_airport VARCHAR(50) NOT NULL
  );
")

# Creates the Incidents table
dbExecute(con, "
  CREATE TABLE Incidents (
    iid VARCHAR(50) PRIMARY KEY,
    flight_number VARCHAR(50),
    date DATE NOT NULL,
    incident_type VARCHAR(50) NOT NULL,
    severity VARCHAR(20) NOT NULL,
    delay_mins INT DEFAULT 0,
    num_injuries INT DEFAULT 0,
    reported_by VARCHAR(50),
    FOREIGN KEY (flight_number) REFERENCES Flights(flight_number)
  );
")

dbDisconnect(con)
