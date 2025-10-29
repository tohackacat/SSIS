# 2025FA-CST-162-001-Computer-Algorithms — Group Project

## Final Project: School Information System (SIS)

### Project Overview

You are to design and implement a School Information System in Java. The system should be menu-driven, allowing users to manage information about **Students, Courses, and Faculty Members**.

Your goal is to create a program that is **organized**, **modular**, and as **realistic** as possible. You are encouraged to add creative features that make your system unique, while covering all major programming concepts learned in class.

---

### Core Learning Goals

Your project must demonstrate:

* Use of multiple classes and objects

* Attributes and methods (instance and static)

* Static constants (e.g., total credits required for each major)

* Decision-making (if, switch)

* Loops for menu repetition

* Encapsulation and use of getters/setters

* Arrays or ArrayLists

* Basic exception handling

* Meaningful output formatting

---

## System Structure

### 1. Main Menu

When the program starts, display a top-level menu with three major sections:

```text
===== SCHOOL INFORMATION SYSTEM =====

1. Student Management
2. Course Management
3. Faculty Management
0. Exit

Enter your choice:
```

The program continues to display the main menu until the user chooses to exit.

---

### 2. Submenus

Each main menu section should open a submenu of related operations.

#### Student Management

Example options (students can add more):

```text
---- STUDENT MANAGEMENT ----

1. Add New Student
2. View All Students
3. Update Student GPA
4. Add Completed Course
5. Delete Student
6. Check Graduation Eligibility
0. Back to Main Menu
```

## Technical Expectations

* Organize your code into multiple classes (at least 4).

* Use `Scanner` for user input.

* Use `switch` or `if-else` to control menu options.

* Use loops to keep showing menus until the user exits.

* Use methods to break the program into clear sections.

* Use `toString()` to print object details neatly.

* Handle incorrect input gracefully (using `try/catch` or checks).

---

## Encouraged Creative Additions (optional)

Students are encouraged to go beyond the basic requirements by adding features such as:

* File I/O to save and load data.

* A simple login system (e.g., admin password).

* Displaying reports such as:

  * "List of students eligible for graduation"
  * "List of courses taught by a faculty member"

* Allowing search by student ID or name.

---

## Submission Requirements

1. All Java source files (.java) must be included.

2. Include a short README file explaining your program's features and how to use it.

3. Team Participation Report.

4. The code must compile and run without errors.

5. Creativity and usability count - neat output and logical design will earn extra credit.

---
---

## README - \[Project Title]

**Course:** CST 162 001 - Computer Algorithms
**Instructor:** Prof. Ibtisam Ali
**Team Members:** \[List names here]

### Description

A short paragraph describing what the project does and its main goal.

Example:

```text
The Store Inventory Management System allows users to manage store products through a menu-driven program. It demonstrates the use of classes, methods, conditionals, and arrays in Java.
```

### Program Structure

Briefly describe the classes and their roles:

```text
Main.java      - Contains main method and program menu.
Product.java   - Defines product attributes and methods.
Inventory.java - Manages product list, including add, update, and delete functions.
```

### Main Methods and Features

List the most important methods and their purpose:

```text
addProduct()    - Adds a new product to inventory.
viewProducts()  - Displays all products.
updateProduct() - Updates product information.
deleteProduct() - Removes a product from inventory.
```

---


## How to Run

```text
javac Main.java
java Main
```

---

## Example Output (optional)

```text
Enter choice: 1
Add Product -> Enter name: Laptop, Price: 999.99, Quantity: 5
Product added successfully!
```

---

## Technologies Used

* Java
* Arrays or ArrayList
* Loops and conditionals
* Object-Oriented Programming concepts

---

## Notes

This project was created as part of the end-of-semester assignment to apply the Java concepts covered in class.
