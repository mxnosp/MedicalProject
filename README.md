# MedicalProject

A desktop medical management application built with **Java**, **JavaFX**, and **SQLite**.
The app is designed for managing patient records, visits, medical information, and local database backups.

## Overview

MedicalProject is a JavaFX desktop application for small-scale patient management. It allows users to store and manage patient data, visit history, medical notes, and related health information in a local SQLite database.

The project focuses on:

* clean desktop UI with JavaFX
* persistent storage using SQLite
* patient and visit management
* validation checks for safer data entry
* database backup support
* OneDrive-based database location for use across two computers, assuming the app is never used on both machines at the same time

## Features

### Patient Management

* Add new patients
* Edit existing patient information
* Delete patients
* Search patients by name or AMKA
* Prevent duplicate AMKA entries
* Display detailed patient information

Patient fields include:

* first name
* last name
* AMKA
* phone number
* age / birth-related information
* height
* weight
* BMI calculation
* smoking status
* medical history
* chronic medication
* notes

### Visit Management

* Add visits for patients
* Edit visit information
* View patient visit history
* Store visit-related notes and medical data
* Support for additional medical measurements such as spirometry-related values

### Validation

The app includes validation logic for safer input handling, such as:

* required field checks
* numeric input validation
* AMKA uniqueness checks
* null-safe model fields
* safer form handling for optional patient and visit data

### SQLite Database

The application uses SQLite for local persistence.

The database file is stored under:

```text
medical-app/medical_app.db
```

If OneDrive is available, the app stores the database inside the user's OneDrive directory:

```text
OneDrive/medical-app/medical_app.db
```

Otherwise, it falls back to:

```text
C:\Users\<user>\medical-app\medical_app.db
```

### Backup System

The app supports automatic database backups.

Backups are stored under:

```text
medical-app/backups/
```

Example:

```text
medical-app/
├── medical_app.db
└── backups/
    ├── 2026-07-05_16-30-00.db
    ├── 2026-07-05_18-10-42.db
    └── ...
```

The backup system is designed to create backups when the app closes, if database changes were made.

## Important OneDrive Usage Note

This project can store the SQLite database in OneDrive so the same database can be used from two computers, for example one office PC and one home PC.

This works only under this rule:

```text
Do not use the app on both computers at the same time.
```

Recommended workflow:

```text
1. Use the app on PC A.
2. Close the app.
3. Wait for OneDrive to finish syncing.
4. Open the app on PC B.
```

## Technologies Used

* Java
* JavaFX
* FXML
* SQLite
* JDBC
* Maven
* OneDrive environment path detection
* IntelliJ IDEA

## Project Structure

```text
MedicalProject/
├── src/
│   └── main/
│       ├── java/
│       │   ├── db/
│       │   │   ├── DBConnector.java
│       │   │   └── DBInitializer.java
│       │   ├── gui/
│       │   │   └── controllers/
│       │   ├── model/
│       │   ├── repository/
│       │   ├── service/
│       │   │   └── BackupService.java
│       │   ├── utils/
│       │   │   ├── OneDriveLocator.java
│       │   │   ├── DatabaseChangeTracker.java
│       │   │   └── DirectoryUtils.java
│       │   └── Main.java
│       └── resources/
│           ├── images/
│           ├── styles/
│           └── views/
├── pom.xml
└── README.md
```

## Running the Project

### Requirements

* Java 17 or newer
* Maven
* JavaFX dependencies configured through Maven
* SQLite JDBC dependency

The project currently includes:

* patient creation, editing, deletion, and search
* visit creation and editing
* SQLite persistence
* null-safe model improvements
* AMKA uniqueness validation
* OneDrive database path detection
* backup service support
* JavaFX UI screens for patient and visit workflows

## Author

Developed by [mxnosp](https://github.com/mxnosp).
