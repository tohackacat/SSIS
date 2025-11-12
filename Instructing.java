public class Instructing {
    //Attributes
    private String courseId;
    private String facultyId;

    //Constructor
    public Instructing(String courseId, String facultyId){
        this.courseId = courseId;
        this.facultyId = facultyId;
    }
    
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("Course ID: ").append(courseId)
          .append("\nInstructor ID:").append(facultyId);

        return sb.toString();
    }
}
