# MyLeague

This project implements a set of entities to simulate a sport score information system, in JAVA (https://www.java.com). The system relies on a Vert.x server(https://vertx.io/) and an API (following MVC pattern design) to comunicate with the webapp, developed with bootstrap 4 (https://getbootstrap.com/). The data is stored in a MySQL Database (https://www.mysql.com/). The project was implemented with the Apache NetBeans IDE (https://netbeans.apache.org/).

## Requirements

- Database: Local instance of MySQL Server running (tested with 8.0.19 - Homebrew).
- Language: JAVA (Tested with JDK 14.01)
- IDE: Apache NetBeans IDE (tested with version 11.2)

## Install and Tests

After the download, the instalation process is quite simple. You have to (1) configure your database connection settings, (2) launch the server, (3) execute a test program (to create the tables and populate de database) and (4) test the webapplication.

### Database configuration

First you have to create a database, i.e. myleague, on your local

Open the file com.myleague.core.DatabaseConfig.java and fill the setting with you local mysql server access settings. You have to set your hostname (DBHOST), database name (DBNAME), user name (DBUSER) and password (DBPASS):

    public static String DBHOST = "localhost";
    public static String DBNAME = "myleague";
    public static String DBUSER = "root";
    public static String DBPASS = "*******";


### Launching the server

Compile and run the file com.myleague.Server.java to launch the application server.

### Execute the test program

Compile and run the file com.myleague.MyLeagueTests.java to create the database table and to populate tem with some data.

### Browsing the webapplication

Open your browser and access the aplication on the URL http://localhost:8080 and test the webapplication.
