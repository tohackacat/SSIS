# Student
Student(String id, String name, double gpa, int credReqs)
String getId()
String getName ()
double getGpa()
int getCredReqs() 

# Course
Course(String id, String name, int credits, String description, String location, int year, String Season, int length)
int getCredits()
String getName()
String getID()
String getDescription ()
String getLocation ()
int getYear()
String getSeason()
int getLength() //in weeks
toString()

# Faculty
Faculty(String id, String name, boolean tenure, double salary, double hours, double hourlyRate)
String getName()
String getID()
Boolean getTenure()
double getSalary()
double getHours()
double getHourlyRate()
toString()

# Enrollment
Enrollment(Faculty instructor, Course course)
Faculty getFaculty()
Course getCourse()
ArrayList<Student> getStudents()
addStudent(Student student)
setFaculty(Faculty instructor)
toString()