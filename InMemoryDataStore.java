import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
//Now we are implementing the data store interface
public class InMemoryDataStore implements DataStore {
    private final Map<String, Student> studentMap = new HashMap<>();
    private final Map<String, Course> CourseMap = new HashMap<>();

    //now the methods defined on DataStore for students
    @Override
    public void addStudent(Student student){
        studentMap.put(student.getId(), student);
    }
    @Override
    public Student findStudentById(String id){
        return studentMap.get(id);
    }
    @Override
    public List<Student> listStudents(){
        return new ArrayList<>(studentMap.values());
    }
    //now for courses
    @Override
    public void addCourse(Course course){
        CourseMap.put(course.getId(), course);
    }
    @Override
    public Course findCourseById(String id){
        return CourseMap.get(id);
    }
    @Override
    public List<Course> listCourses(){
        return new ArrayList<>(CourseMap.values());
    }
}


