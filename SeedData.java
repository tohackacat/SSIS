import java.util.List;

public class SeedData {
    public static final List<Faculty> PROFS = List.of(
            new Faculty("F01234", "Kakashi Hatake", false, 60000, 40, 50),
            new Faculty("F08675", "Rachel Moore", true, 60000, 40, 50),
            new Faculty("F02864", "Kurenai Yuhi", false, 60000, 40, 50));

    public static final List<Course> COURSES = List.of(
            new Course(
                    "CS 101",
                    "Intro to Programming",
                    3,
                    "A beginner course introducing students to programming fundamentals",
                    "H-201",
                    2025,
                    15,
                    "Fall"),
            new Course(
                    "MA 101",
                    "Introductory Maths",
                    3,
                    "An introductory course on mathematical proofs and logic",
                    "B-13",
                    2025,
                    15,
                    "Fall"),
            new Course(
                    "PH 111",
                    "Intro to Quantum Physics",
                    3,
                    """
                            A course covering topics including quantum wave functions,
                            boundary states, scattering, and tunneling
                            """,
                    "M 120",
                    2025,
                    15,
                    "Spring"));

    public static final List<Student> STUDENTS = List.of(
            new Student("U123456", "Naruto Uzumaki", 2.5, 120),
            new Student("U150934", "Sakura Haruno", 3.9, 120),
            new Student("U165981", "Sasuke Uchiha", 3.7, 120));

    public static final List<Instructing> INSTRUCTINGS = List.of(
            new Instructing(COURSES.get(0).id(), PROFS.get(0).id()),
            new Instructing(COURSES.get(1).id(), PROFS.get(1).id()),
            new Instructing(COURSES.get(2).id(), PROFS.get(2).id()));

    public static final List<Enrollment> ENROLLMENTS = COURSES.stream()
            .flatMap(c -> STUDENTS.stream().map(s -> new Enrollment(c.id(), s.id())))
            .toList();

    private SeedData() {
    }

}
