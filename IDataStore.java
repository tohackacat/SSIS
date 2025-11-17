import java.util.List;

public interface IDataStore{
    void addStudent(Student student);
    void removeStudent(String studentId);
    void updateStudent(String studentId, Student student);
    Student getStudentById(String studentId);
    List<Student> listStudents();

    // Course Operations
    void addCourse(Course course);
    void removeCourse(String courseId);
    void updateCourse(String courseId, Course course);
    Course getCourseById(String studentId);
    List<Course> listCourses();
}
