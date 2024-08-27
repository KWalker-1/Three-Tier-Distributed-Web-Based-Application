# Three-Tier-Distributed-Web-Based-Application
Developed a three-tier web application using servlets and JSP technology on a Tomcat server, with MySQL database as the backend, effectively separating concerns between the client interface, business logic, and data storage.

## Overview

This project is a three-tier distributed web-based application developed to demonstrate the effective separation of concerns between the client interface, business logic, and data storage. The application leverages servlets and JSP technology on an Apache Tomcat server, with MySQL as the backend database.

## Architecture

#### Client Tier: 
The user interface is built using JSP pages, providing a dynamic and responsive user experience.  
#### Business Logic Tier: 
Implemented using Java Servlets, the business logic is encapsulated on the server side to manage application functionality, such as user authentication, role management, and dynamic supplier status adjustments.  
#### Data Storage Tier: 
A MySQL database serves as the backend, storing all application data, including user information, supplier details, and transaction records.


## Features

### User Authentication and Role Management
Robust Authentication System: Secure login system that authenticates users and routes them to different JSP pages based on their roles.
Role-Specific Functionalities:
Root: Full access, including the execution of SQL commands and database management.
Client: Access to specific client-related functions and views.
Data Entry: Restricted access, focusing on data entry tasks.
Accountant: Access to financial data and report generation.
### Business Logic Implementation
Dynamic Supplier Status Adjustment: The application automatically adjusts supplier status based on shipment quantities. This logic is embedded within the servlets, allowing the application to dynamically respond to database updates without relying on database triggers.

## Installation and Setup

### 1. Prerequisites:
Apache Tomcat Server
MySQL Database Server
Java Development Kit (JDK)
IDE (Eclipse recommended)
### 2. Database Setup:
Import the provided SQL scripts to set up the necessary database schema and initial data.
### 3. Configuration:
Modify the database.properties file with your MySQL database credentials.
Ensure the Tomcat server is properly configured to run the application.
### 4. Deployment:
Deploy the WAR file to the Tomcat server or use your IDE for deployment.
Access the application through the designated URL provided by the Tomcat server.

## Usage

Login: Access the application via the login page, where users are authenticated based on their credentials.
Role Navigation: Once authenticated, users will be directed to their respective role-based JSP pages.
Dynamic Interaction: Engage with the application’s dynamic features, such as automatic supplier status updates and role-based functionalities.
