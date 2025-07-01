# Title: Testing Data Loading Process
# Authors: Sarah Taylor and Diego Cicotoste
# Semester: Summer I 2025
library(RMySQL)
library(readr)
library(DBI)
library(RSQLite)

# Each test in this file adheres to the following format:
  # - Definition of the db value, determined from a sql query
  # - Definition of the csv value, determined by the csv query
  # - Checking equality of the csv and db value and printing either:
    # <test name>: PASS: the values are equal, meaning the DB and CSV are correctly configured
    # <test name>: FAIL: the values are not equal, meaning there is a discrepancy between the DB and CSV
# Note: in the sum/averages there are multiple suppressed warnings about the decimals being, truncated,
# but these do not matter since the numbers are rounded either way.

# Read in the csv into a dataframe
df <- read.csv("https://s3.us-east-2.amazonaws.com/artificium.us/datasets/incidents.csv")

# Connection to the MySQL Database:
conn <- dbConnect(
  RMySQL::MySQL(),
  user = "avnadmin",
  password = "AVNS_C18DfZKwt6EZpZbhpOv",
  dbname = "defaultdb",
  host = "mysql-377c8670-northeastern-d772.d.aivencloud.com",
  port = 23993,
)



# --- Unique Airlines --- 
db_airlines <- dbGetQuery(conn, "SELECT COUNT(DISTINCT f.airline) AS airlines
                                  FROM Incidents as i
                                  JOIN Flights as f ON i.flight_number = f.flight_number;")$airlines
csv_airlines <- length(unique(df$airline))

if(db_airlines == csv_airlines) {
  print(paste0("Unique Airlines: PASS, db = ", db_airlines, ", csv = ", csv_airlines))
} else {
  print(paste0("Unique Airlines: FAIL, db = ", db_airlines, ", csv = ", csv_airlines))
}

# --- Unique Flights ---
db_flights <- dbGetQuery(conn, "SELECT COUNT(DISTINCT `flight_number`) AS unique_flights FROM Incidents;")$unique_flights
csv_flights <- length(unique(df$flight.number))

if(db_flights == csv_flights) {
  print(paste0("Unique Flights: PASS, db = ", db_flights, ", csv = ", csv_flights))
} else {
  print(paste0("Unique Flights: FAIL, db = ", db_flights, ", csv = ", csv_flights))
}

# --- Unique Incidents ---
db_incidents <- dbGetQuery(conn, "SELECT COUNT(DISTINCT iid) AS unique_incidents FROM Incidents;")$unique_incidents
csv_incidents <- length(unique(df$iid))

if(db_incidents == csv_incidents) {
  print(paste0("Unique Incidents: PASS, db = ", db_incidents, ", csv = ", csv_incidents))
} else {
  print(paste0("Unique Incidents: FAIL, db = ", db_incidents, ", csv = ", csv_incidents))
}

# --- First Date ---
db_first_date <- dbGetQuery(conn, "SELECT MIN(date) AS first_date FROM Incidents;")$first_date
# Convert the dates in the df into date object
df$date <- as.Date(df$date, format = "%d.%m.%Y")
csv_first_date <- min(df$date)

if(db_first_date == csv_first_date) {
  print(paste0("First Date: PASS, db = ", db_first_date, ", csv = ", csv_first_date))
} else {
  print(paste0("First Date: FAIL, db = ", db_first_date, ", csv = ", csv_first_date))
}

# --- Last Date ---
db_last_date <- dbGetQuery(conn, "SELECT MAX(date) AS last_date FROM Incidents;")$last_date
csv_last_date <- max(df$date)

if(db_last_date == csv_last_date) {
  print(paste0("Last Date: PASS, db = ", db_last_date, ", csv = ", csv_last_date))
} else {
  print(paste0("Last Date: FAIL, db = ", db_last_date, ", csv = ", csv_last_date))
}

# Sums and Aggregates:

# --- Sum of Injuries ---

# Suppress warning that was showing up about decimals. 
db_injury_sum <- suppressWarnings(dbGetQuery(conn, "SELECT SUM(num_injuries) AS total_injuries FROM Incidents;")$total_injuries)
csv_injury_sum <- sum(df$num.injuries)

if(db_injury_sum == csv_injury_sum) {
  print(paste0("Sum of Injuries: PASS, db = ", db_injury_sum, ", csv = ", csv_injury_sum))
} else {
  print(paste0("Sum of Injuries: FAIL, db = ", db_injury_sum, ", csv = ", csv_injury_sum))
}

# --- Average of Injuries ---
# Round numbers to ensure proper equality
db_injury_avg <- suppressWarnings(round(dbGetQuery(conn, "SELECT AVG(num_injuries) AS total_injuries FROM Incidents;")$total_injuries, 3))
csv_injury_avg <- round(mean(df$num.injuries),3)

if(db_injury_avg == csv_injury_avg) {
  print(paste0("Average of Injuries: PASS, db = ", db_injury_avg, ", csv = ", csv_injury_avg))
} else {
  print(paste0("Average of Injuries: FAIL, db = ", db_injury_avg, ", csv = ", csv_injury_avg))
}

# --- Sum of Delays ---
db_delay_sum <- suppressWarnings(dbGetQuery(conn, "SELECT SUM(delay_mins) AS sum_delay FROM Incidents;")$sum_delay)
csv_delay_sum <- sum(df$delay.mins)

if(db_delay_sum == csv_delay_sum) {
  print(paste0("Sum of Injuries: PASS, db = ", db_delay_sum, ", csv = ", csv_delay_sum))
} else {
  print(paste0("Sum of Injuries: FAIL, db = ", db_delay_sum, ", csv = ", csv_delay_sum))
}

# --- Total number of Flights ---
db_num_flights <- dbGetQuery(conn, "SELECT COUNT(*) AS count FROM Flights;")$count
csv_num_flights <- length(unique(df$flight.number))

if(db_num_flights == csv_num_flights) {
  print(paste0("Number of Flights: PASS, db = ", db_num_flights, ", csv = ", csv_num_flights))
} else {
  print(paste0("Number of Flights: FAIL, db = ", db_num_flights, ", csv = ", csv_num_flights))
}

dbDisconnect(conn)


