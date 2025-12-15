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
    DatabaseInitializer.java:  a record that takes a provider. Creates and executes sql queries, creates a sample database with sample entities.

course:
    Course.java: entity record with parameters for id, code, string name, credits. 
    CourseDto.java: Data Transfer Object record with same parameters.
    CourseRepository.java: course repo interface with CRUD methods findALL, findById, insert, update, delete for course entity.
    CourseService.java: course service layer interface with CRUD methods findALL, findById, insert, update, delete for course DTO.
    CourseController.java: a class to interact with DTO's through the course service methods (i.e. creates wrapper methods that  return DTO's through service method calls).
    JdbcCourseRepository: a class that implements repository interface. Implements the methods through SQL queries to the the database. employs exception handling for connectivity and input errors.
    DefaultCourseService.java: a class that implements service interface. Takes entities into data transfer object manipulation 

enrollment:
    Enrollment.java: entity record with parameters for id, studentId, CourseId, grade. 
    EnrollmentDto.java: Data Transfer Object record with same parameters.
    EnrollmentRepository.java: course repo interface with CRUD methods findALL, findById, insert, update, delete for enrollment entity.
    EnrollmentService.java: course service layer interface with CRUD methods findALL, findById, insert, update, delete for enrollment DTO.
    EnrollmentController.java: a class to interact with DTO's through the course service methods (i.e. creates wrapper methods that  return DTO's through service method calls).
    JdbcEnrollmentRepository: a class that implements repository interface. Implements the methods through SQL queries to the the database. employs exception handling for connectivity and input errors.
    DefaultEnrollmentService.java: a class that implements service interface. Takes entities into data transfer object manipulation 

faculty:
    Faculty.java: entity record with parameters for id, personId, department. 
    FacultyDto.java: Data Transfer Object record with same parameters.
    FacultyRepository.java: course repo interface with CRUD methods findALL, findById, insert, update, delete for faculty entity.
    FacultyService.java: course service layer interface with CRUD methods findALL, findById, insert, update, delete for faculty DTO.
    FacultyController.java: a class to interact with DTO's through the service methods (i.e. creates wrapper methods that  return DTO's through service method calls).
    JdbcFacultyRepository: a class that implements repository interface. Implements the methods through SQL queries to the the database. employs exception handling for connectivity and input errors.
    DefaultFacultyService.java: a class that implements service interface. Takes entities into data transfer object manipulation 

person:
    Person.java: entity record with parameters for id, fullName, and Optional email. 
    PersonDto.java: Data Transfer Object record with same parameters.
    PersonRepository.java: course repo interface with CRUD methods findALL, findById, insert, update, delete for person entity.
    PersonService.java: course service layer interface with CRUD methods findALL, findById, insert, update, delete for person DTO.
    PersonController.java: a class to interact with DTO's through the service methods (i.e. creates wrapper methods that return DTO's through service method calls).
    JdbcPersonRepository: a class that implements repository interface. Implements the methods through SQL queries to the the database. employs exception handling for connectivity and input errors.
    DefaultPersonService.java: a class that implements service interface. Takes entities into data transfer object manipulation 

student:
    Student.java: entity record with parameters for id, personId, enrollmentYear. 
    StudentDto.java: Data Transfer Object record with same parameters.
    StudentRepository.java: course repo interface with CRUD methods findALL, findById, insert, update, delete for student entity.
    StudentService.java: course service layer interface with CRUD methods findALL, findById, insert, update, delete for student DTO.
    StudentController.java: a class to interact with DTO's through service methods (i.e. creates wrapper methods that  return DTO's through service method calls).
    JdbcStudentRepository: a class that implements repository interface. Implements the methods through SQL queries to the the database. employs exception handling for connectivity and input errors.
    DefaultStudentService.java: a class that implements service interface. Takes entities into data transfer object manipulation 

teaching:
    Teaching.java: entity record with parameters for id, courseId, facaultyId, role. 
    TeachingDto.java: Data Transfer Object record with same parameters.
    TeachingRepository.java: repo interface with CRUD methods findALL, findById, insert, update, delete for teaching entity.
    TeachingService.java: service layer interface with CRUD methods findALL, findById, insert, update, delete for teaching DTO.
    TeachingController.java: a class to interact with DTO's through the service methods (i.e. creates wrapper methods that  return DTO's through service method calls).
    JdbcTeachingRepository: a class that implements repository interface. Implements the methods through SQL queries to the the database. employs exception handling for connectivity and input errors.
    DefaultTeachingService.java: a class that implements service interface. Takes entities into data transfer object manipulation 

Tui:
    holds the text user interface classes and methods. Uses the jline package to create interactable text base interface in terminal.

App: 
    has the main method


## Technologies Used
Java
Github
Lists and ArrayLists
Loops and Conditionals
Verifications and Exception Handling
SQL
Database implementation
Object Oriented Programming Concepts

## How to Run
./gradlew installDist && ./build/install/SSIS/bin/SSIS