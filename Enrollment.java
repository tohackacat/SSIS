/*
 * An Enrollment class that holds a course, an instructor,
 * and a dynamically resizeable ArrayList of students.
 * It contains a method to add a student and set a new instructor.
 */

public class Enrollment {
    //Attributes
    private String courseId;
    private String studentId; 
    
    //Constructor
    public Enrollment(String courseId, String studentId){
        this.courseId = courseId;
        this.studentId = studentId;
    }

    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("Course ID: ").append(courseId)
          .append("\nStudent ID:").append(studentId);

        return sb.toString();
    }
}
