# SSIS

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