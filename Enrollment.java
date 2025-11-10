/*
 * An Enrollment class that holds a course, an instructor,
 * and a dynamically resizeable ArrayList of students.
 * It contains a method to add a student and set a new instructor.
 */
import java.util.ArrayList;

public class Enrollment {
    //Attributes
    private Faculty instructor;
    private Course course;
    private ArrayList<Student> students; 
    
    //Constructor
    public Enrollment(Faculty instructor, Course course){
        this.instructor = instructor;
        this.course = course;
    }

    //Getters
    public Faculty getFaculty(){
        return instructor;
    }

    public Course getCourse(){
        return course;
    }

    public ArrayList<Student> getStudents(){
        return students;
    }

    //Add Students
    public void addStudent(Student student){
        students.add(student); 
    }

    //Set a new Instructor
    public void setFaculty(Faculty instructor){
        this.instructor = instructor;
    }

    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("Course: ").append(course.getName())
          .append("\nCourse ID: ").append(course.getId())
          .append("\nInstructor: ").append(instructor.getName())
          .append("\n Students: ");
        
        for(Student s: students){
            sb.append(s.getName());
        }

        return sb.toString();
    }
}
