import java.util.List;

public interface DataStore{
    void addStudent(Student student);
    Student findStudentById(String id);
    List<Student> listStudents();

    // Course Operations
    void addCourse(Course course);
    Course findCourseById(String id);
    List<Course> listCourses();
}