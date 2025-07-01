# Title: Config Business Logic
# Authors: Sarah Taylor and Diego Cicotoste
# Semester: Summer I 2025

library(DBI)
library(RMySQL)

# Connect to the database
conn <- dbConnect(
  RMySQL::MySQL(),
  user = "avnadmin",
  password = "AVNS_C18DfZKwt6EZpZbhpOv",
  dbname = "defaultdb",
  host = "mysql-377c8670-northeastern-d772.d.aivencloud.com",
  port = 23993,
)

# --- storeIncident ---

# Drop the table if it exists already
dbExecute(conn, "DROP PROCEDURE IF EXISTS storeIncident;")

# Create the stored procedure
dbExecute(
  conn,
  "
CREATE PROCEDURE storeIncident(
    IN p_iid VARCHAR(50),
    IN p_flight_number VARCHAR(50),
    IN p_date DATE,
    IN p_incident_type VARCHAR(50),
    IN p_severity VARCHAR(20),
    IN p_delay_mins INT,
    IN p_num_injuries INT,
    IN p_reported_by VARCHAR(50)
)
BEGIN
    IF EXISTS (SELECT 1 FROM Flights WHERE flight_number = p_flight_number) THEN
        INSERT INTO Incidents (
            iid, flight_number, date, incident_type,
            severity, delay_mins, num_injuries, reported_by
        ) VALUES (
            p_iid, p_flight_number, p_date, p_incident_type,
            p_severity, p_delay_mins, p_num_injuries, p_reported_by
        );
    END IF;
END
")

# Test the stored procedure by creating example
dbExecute(conn, "DELETE FROM Incidents WHERE iid = 'i00002'")
dbExecute(
  conn,
  "
  CALL storeIncident(
  'i00002',
  '1111',
  '2025-06-14',
  'faa',
  'major',
  60,
  3,
  'mechanic'
);"
)

# Check that the stored procedure added this to the database.
dbGetQuery(conn, "SELECT * FROM Incidents WHERE iid = 'i00002'")

# ---- storeNewIncident ----

# Drop the table if exists - storeNewIncident
dbExecute(conn, "DROP PROCEDURE IF EXISTS storeNewIncident")

# Creating the stored procedure
dbExecute(
  conn,
  "CREATE PROCEDURE storeNewIncident(
    IN p_iid VARCHAR(50),
    IN p_flight_number VARCHAR(50),
    IN p_date DATE,
    IN p_incident_type VARCHAR(50),
    IN p_severity VARCHAR(20),
    IN p_delay_mins INT,
    IN p_num_injuries INT,
    IN p_reported_by VARCHAR(50),
    IN p_airline VARCHAR(50),
    IN p_aircraft VARCHAR(50),
    IN p_dep_airport VARCHAR(50)
)
BEGIN
    IF NOT EXISTS (SELECT 1 FROM Flights WHERE flight_number = p_flight_number) THEN
        INSERT INTO Flights (flight_number, airline, aircraft, dep_airport)
        VALUES (p_flight_number, p_airline, p_aircraft, p_dep_airport);
    END IF;
    INSERT INTO Incidents (
        iid, flight_number, date, incident_type,
        severity, delay_mins, num_injuries, reported_by
    ) VALUES (
        p_iid, p_flight_number, p_date, p_incident_type,
        p_severity, p_delay_mins, p_num_injuries, p_reported_by
    );
END;")

# Calling the stored procedure to ensure that it creates a new incident AND flight

# Delete the existing data so that this file can be run multiple times without error
dbExecute(conn, "DELETE FROM Incidents WHERE iid = 'i00003'")
dbExecute(conn,
          "DELETE FROM Flights WHERE flight_number = '123123123123123'")

dbExecute(
  conn,
  "
CALL storeNewIncident(
  'i00003',
  '123123123123123',
  '2025-06-14',
  'faa',
  'major',
  60,
  5,
  'mechanic',
  'NZ',
  '747-8',
  'JFK'
);")


# Check that the data was properly added into the database
dbGetQuery(conn, "SELECT * FROM Incidents WHERE iid = 'i00003'")
dbGetQuery(conn,
           "SELECT * FROM Flights WHERE flight_number = '123123123123123'")


dbDisconnect(conn)