# Title: Delete Database
# Authors: Sarah Taylor and Diego Cicotoste
# Semester: Summer I 2025

library(DBI)
library(RMySQL)

# Connect to the database
con <- dbConnect(
  RMySQL::MySQL(),
  user = "avnadmin",
  password = "AVNS_C18DfZKwt6EZpZbhpOv",
  dbname = "defaultdb",
  host = "mysql-377c8670-northeastern-d772.d.aivencloud.com",
  port = 23993,
)

drop_order <- c("Incidents", "Flights") 

# Drop all tables
for (table in drop_order) {
  # using paste0 to combine the strings without spaces
  drop_query <- paste0("DROP TABLE IF EXISTS `", table, "`;")
  dbExecute(con, drop_query)
}

dbDisconnect(con)