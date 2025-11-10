public class SeedData {
    public static void main(String[] args) {
        //Test Faculty
        Faculty prof1 = new Faculty("F01234", "Kakashi Hatake", false,
         60000, 40, 50);
        Faculty prof2 = new Faculty("F08675", "Rachel Moore", true,
         60000, 40, 50);
        Faculty prof3 = new Faculty("F02864", "Kurenai Yuhi", false,
         60000, 40, 50);

        //Test Courses
        String course1Desc = "A beginner course introducing students to programming fundamentals";
        Course course1 = new Course("CS 101", "Intro to Programming", 3, 
         course1Desc, "H-201", 2025, "Fall", 15);
        
        String course2Desc = "An introductory course on mathematical proofs and logic";
        Course course2 = new Course("MA 101", "Introductory Maths", 3,
         course2Desc, "B-13", 2025, "Fall", 15);
        
        String course3Desc = """
            A course covering topics including quantum wave functions,
            boundary states, scattering, and tunneling
            """;
        Course course3 = new Course("PH 111", "Intro to Quantum Physics", 3, 
                                course1Desc, "M 120", 2025, "Spring", 15);

        
        //Test Students
        Student student1 = new Student("U123456", "Naruto Uzumaki", 2.5, 120);
        Student student2 = new Student("U150934", "Sakura Haruno", 3.9, 120);
        Student student3 = new Student("U165981", "Sasuke Uchiha", 3.7, 120);

        //Enrollments
        Enrollment enroll1 = new Enrollment(prof1, course1);
        Enrollment enroll2 = new Enrollment(prof2, course3);
        Enrollment enroll3 = new Enrollment(prof3, course2);

        Student [] students = {student1, student2, student3};
        for(Student s: students){
            enroll1.addStudent(s);
            enroll2.addStudent(s);
            enroll3.addStudent(s);
        }

        System.out.println(enroll1.toString());
        System.out.println(enroll2.toString());
        System.out.println(enroll3.toString());
    }
    
}
