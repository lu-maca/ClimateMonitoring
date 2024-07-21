import mysql.connector
import csv

DB_ACTIONS = True

if DB_ACTIONS:
    mydb = mysql.connector.connect(
    host="localhost",
    username="luca",
    password="ClimateMonitoring",
    database="ClimateMonitoring"
    )

    mycursor = mydb.cursor()

"""create table "monitoring_coordinates" from monitoring_coordinates.csv"""
# create the table
if DB_ACTIONS:
    mycursor.execute(
        # the combination of 4 columns shall be unique! the primary key is id
        """CREATE TABLE locations 
            (id VARCHAR(12) PRIMARY KEY, 
            name VARCHAR(120), 
            ascii_name VARCHAR(120) NOT NULL, 
            state_code VARCHAR(3) NOT NULL, 
            state VARCHAR(40) NOT NULL, 
            latitude DECIMAL(8, 5) NOT NULL,
            longitude DECIMAL(8, 5) NOT NULL,
            UNIQUE(name, state, latitude, longitude)
        )""" 
    )

    # read monitoring_coordinates.csv
    command = "INSERT INTO locations (id, name, ascii_name, state_code, state, latitude, longitude) VALUES (%s, %s, %s, %s, %s, %s, %s)"

with open("monitoring_coordinates.csv", "r") as f:
    locations = csv.reader(f, delimiter=';')
    i = 0
    
    entries = []
    
    for location in locations:
        if i == 0: 
            i += 1
            continue
        coordinates = location.pop()
        location += coordinates.split(",")
        location[-1] = location[-1].replace(" ", "")
        entries.append(tuple(location))   
    
    if DB_ACTIONS:
        mycursor.executemany(command, entries)
        mydb.commit()
        
        print(mycursor.rowcount, "was inserted.") 
        
# create other tables
if DB_ACTIONS:
    # monitoring_centers
    mycursor.execute(
        """CREATE TABLE monitoring_centers
            (name VARCHAR(50) UNIQUE NOT NULL,
            address VARCHAR(70) UNIQUE NOT NULL,            
            monitored_area_id VARCHAR(12),
            id VARCHAR(10),
            PRIMARY KEY (id, monitored_area_id)
            )"""
    )
    
    # registered_operators
    mycursor.execute(
        """CREATE TABLE operators
            (name VARCHAR (50) NOT NULL,
            tax_code VARCHAR(20),
            email VARCHAR(30) NOT NULL,
            username VARCHAR(20) UNIQUE,
            pwd VARCHAR(20) NOT NULL,
            center VARCHAR(10) NOT NULL,
            PRIMARY KEY (tax_code, center),
            FOREIGN KEY (center) REFERENCES monitoring_centers(id)
            )"""
    )
    
    # # climate_parameters
    mycursor.execute(
        """CREATE TABLE climate_parameters 
            (geoname_id VARCHAR(12) NOT NULL, 
            wind TINYINT NOT NULL,
            humidity TINYINT NOT NULL,
            pressure TINYINT NOT NULL,
            temperature TINYINT NOT NULL,
            rainfall TINYINT NOT NULL,
            glaciers_alt TINYINT NOT NULL,
            glaciers_mass TINYINT NOT NULL,
            notes VARCHAR(255) DEFAULT "",
            date DATE,
            center_id VARCHAR(10) NOT NULL,
            who VARCHAR(50) NOT NULL,
            PRIMARY KEY (geoname_id, date),
            FOREIGN KEY (geoname_id) REFERENCES locations(id),
            FOREIGN KEY (center_id) REFERENCES monitoring_centers(id),
            FOREIGN KEY (who) REFERENCES operators(tax_code)
            )"""
    )