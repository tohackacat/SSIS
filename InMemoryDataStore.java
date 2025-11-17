import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

public class InMemoryDataStore implements IDataStore {
    private final Map<String, Student> studentMap = new HashMap<>();
    private final Map<String, Course> courseMap = new HashMap<>();

    @Override
    public void addStudent(Student student) {
        if (studentMap.putIfAbsent(student.id(), student) != null) {
            throw new IllegalStateException("Student already exists: " + student.id());
        }
    }

    @Override
    public void removeStudent(String studentId) {
        studentMap.remove(studentId);
    }

    @Override
    public void updateStudent(String studentId, Student student) {
        if (!student.id().equals(studentId))
            throw new IllegalArgumentException("Mismatched student id");

        if (studentMap.replace(studentId, student) == null)
            throw new NoSuchElementException("No student with id: " + studentId);

    }

    @Override
    public Student getStudentById(String studentId) {
        return studentMap.get(studentId);
    }

    @Override
    public List<Student> listStudents() {
        return List.copyOf(studentMap.values());
    }

    @Override
    public void addCourse(Course course) {
        if (courseMap.putIfAbsent(course.id(), course) != null) {
            throw new IllegalStateException("Course already exists: " + course.id());
        }
    }

    @Override
    public void removeCourse(String courseId) {
        courseMap.remove(courseId);
    }

    @Override
    public void updateCourse(String courseId, Course course) {
        if (!course.id().equals(courseId))
            throw new IllegalArgumentException("Mismatched course id");

        if (courseMap.replace(courseId, course) == null)
            throw new NoSuchElementException("No course with id: " + courseId);

    }

    @Override
    public Course getCourseById(String studentId) {
        return courseMap.get(studentId);
    }

    @Override
    public List<Course> listCourses() {
        return List.copyOf(courseMap.values());
    }
}
