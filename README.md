<div align="center">

# SSIS

### Simple School Information System · Terminal UI · SQLite

<img alt="Java 21+" src="https://img.shields.io/badge/Java-21%2B-000000?style=for-the-badge&logo=openjdk&logoColor=white">
<img alt="Gradle" src="https://img.shields.io/badge/Build-Gradle-000000?style=for-the-badge&logo=gradle&logoColor=white">
<img alt="SQLite" src="https://img.shields.io/badge/Database-SQLite-000000?style=for-the-badge&logo=sqlite&logoColor=white">
<img alt="JLine" src="https://img.shields.io/badge/TUI-JLine-000000?style=for-the-badge&logo=windowsterminal&logoColor=white">
<img alt="Course Project" src="https://img.shields.io/badge/Project-CST%20162-000000?style=for-the-badge">

**Course:** CST 162 - Computer Algorithms  
**Instructor:** Prof. Ibtisam Ali  
**Team Members:** Kostiantyn, Camila, Omar

A menu-driven School Information System written in Java. SSIS runs as a terminal UI and lets you manage students,
faculty, and courses, plus the relationships between them (enrollments and teaching assignments). Data is stored in a
local SQLite database file to keep the system realistic and persistent.

</div>

## What you can do

SSIS supports CRUD-style management for these areas:

| Area        | What it manages                          | Notes                              |
|-------------|------------------------------------------|------------------------------------|
| Students    | Name, optional email, enrollment year    | Create, edit, delete               |
| Faculty     | Name, optional email, department         | Create, edit, delete               |
| Courses     | Course code, name, credits               | Create, edit, delete               |
| Enrollments | Student ↔ course, with an optional grade | Create, edit grade, delete, filter |
| Teachings   | Faculty ↔ course, with a required role   | Create, edit role, delete          |

## Requirements

Java 21+ is recommended (the codebase uses modern Java features such as records and newer switch/instanceof forms).

Your build must include dependencies for JLine (terminal handling) and the SQLite JDBC driver (for the `jdbc:sqlite:`
connection).

## How to run

Main class:

`org.example.App`

### To build

```bash
./gradlew installDist
````

### To run

```bash
./build/SSIS/bin/SSIS
```

If your run script is not executable on your system, make it executable and run again:

```bash
chmod +x ./build/SSIS/bin/SSIS
./build/SSIS/bin/SSIS
```

## Navigation and controls

SSIS is keyboard-driven and designed to feel like a compact terminal app instead of a plain text prompt.

| Context      | Keys           | Behavior             |
|--------------|----------------|----------------------|
| Menus        | Up/Down, Enter | Move selection, open |
| Anywhere     | Esc            | Back / Exit          |
| List screens | N, Enter, D    | New, edit, delete    |

Enrollments and teachings also include actions for filtering and editing relationship-specific fields (grade, role),
shown in the screen footer.

## Data storage

SSIS stores data in a local SQLite database file created next to the app at runtime.

Default file name:

`university.db`

Deleting this file resets the system back to a fresh state on the next run.

## Program structure

The project is organized by responsibility so each piece stays small and testable.

| Layer        | Purpose                             | Examples                                  |
|--------------|-------------------------------------|-------------------------------------------|
| UI           | Terminal screens and input handling | `*Screen`, overlays, pickers              |
| Controllers  | Screen-facing API (glue layer)      | `StudentController`, `FacultyController`  |
| Services     | Business rules and validation       | `Default*Service`                         |
| Repositories | Persistence logic (JDBC + SQL)      | `Jdbc*Repository`                         |
| DB utilities | Connections and schema setup        | `DatabaseProvider`, `DatabaseInitializer` |

## Techniques and concepts demonstrated

| Concept             | Where it shows up in SSIS                                        |
|---------------------|------------------------------------------------------------------|
| Classes and objects | Students, faculty, courses, and relationship models              |
| Encapsulation       | Getters/setters or record accessors, validation at service level |
| Decision-making     | Menu routing and action handling                                 |
| Loops               | Continuous menus and screen refresh behavior                     |
| Collections         | Managing and rendering lists of entities                         |
| Exceptions          | Input validation and safe handling of invalid actions            |
| Formatting          | Consistent table-like output in the terminal                     |
| Persistence         | SQLite database with repositories and SQL                        |

## Team contributions

> Kostiantyn served as the team lead and overall architect. He designed the project structure (UI, controllers,
> services,
> repositories, database utilities) and implemented most of the terminal UI (TUI), including the menu navigation and
> screen
> flows.

> Camila contributed to the backend architecture and implementation, working on core logic and persistence-related parts
> of
> the system. She collaborated closely with the team to ensure the architecture stayed consistent and the features
> matched
> the requirements.

> Omar contributed to the backend architecture and implementation, helping build and integrate parts of the service and
> repository layers. He worked with the team on debugging, refining behavior, and making sure features were complete and
> reliable.

All team members supported each other through review, pair-debugging, and coordinating changes across multiple parts of
the codebase to keep the system stable and cohesive.

## Notes

This project was created as part of the end-of-semester assignment to apply the Java concepts covered in class.
=======
## Team Info

Final Java Project for CST-162.
Konstaniyn, Camila, and Omar.

## Project Description

A School Information System implementing layered architecture to communicate with database data and display relevant information.
It demonstrates uses of classes, records, interfaces, inheritance, methods, conditionals, and collections.

Program Structure: 
core:
    EntityID.java: a record holding a UUID (from the java.util library). Has methods for random generation or reading from a String.

db:
    DatabaseProvider.java: a record holding a String url. Uses java.sql library to get the Java Database Connectivity API to access data in a relational database.
    DatabaseInitializer.java:  a record that takes a provider. Creates and ex

course:
    Course.java: entity record with parameters for id, code, string name, credits 
    CourseDto.java: Data Transfer Object record with same parameters
    CourseRepository.java
    CourseService.java
enrollment
faculty
person
student
teaching

## Technologies Used
Java
Github
Lists and ArrayLists
Loops and Conditionals
Verifications and Exception Handling
SQL
Database implementation
Object Oriented Programming Concepts
