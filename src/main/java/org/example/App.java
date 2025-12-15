package org.example;

import org.example.course.CourseController;
import org.example.course.CourseRepository;
import org.example.course.CourseService;
import org.example.course.repository.JdbcCourseRepository;
import org.example.course.service.DefaultCourseService;
import org.example.db.DatabaseInitializer;
import org.example.db.DatabaseProvider;
import org.example.enrollment.EnrollmentController;
import org.example.enrollment.EnrollmentRepository;
import org.example.enrollment.EnrollmentService;
import org.example.enrollment.repository.JdbcEnrollmentRepository;
import org.example.enrollment.service.DefaultEnrollmentService;
import org.example.faculty.FacultyController;
import org.example.faculty.FacultyRepository;
import org.example.faculty.FacultyService;
import org.example.faculty.repository.JdbcFacultyRepository;
import org.example.faculty.service.DefaultFacultyService;
import org.example.person.PersonRepository;
import org.example.person.repository.JdbcPersonRepository;
import org.example.student.StudentController;
import org.example.student.StudentRepository;
import org.example.student.StudentService;
import org.example.student.repository.JdbcStudentRepository;
import org.example.student.service.DefaultStudentService;
import org.example.teaching.TeachingController;
import org.example.teaching.TeachingRepository;
import org.example.teaching.TeachingService;
import org.example.teaching.repository.JdbcTeachingRepository;
import org.example.teaching.service.DefaultTeachingService;
import org.example.tui.EventLoop;
import org.example.tui.input.TerminalInput;
import org.example.ui.MainMenuScreen;
import org.jline.terminal.Attributes;
import org.jline.terminal.Size;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.jline.utils.InfoCmp;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public final class App {
    public static void main(String[] args) throws IOException {
        Terminal terminal = TerminalBuilder.builder()
                .system(true)
                .dumb(false)
                .build();

        AtomicReference<Size> pendingResize = new AtomicReference<>();
        terminal.handle(Terminal.Signal.WINCH, signal -> pendingResize.set(terminal.getSize()));

        Attributes original = terminal.enterRawMode();

        TerminalInput input = new TerminalInput(terminal);
        input.enableBracketedPaste();

        AtomicBoolean cleaned = new AtomicBoolean(false);

        Runnable cleanup = () -> {
            if (!cleaned.compareAndSet(false, true)) {
                return;
            }

            try {
                input.disableBracketedPaste();
            } catch (Exception ignored) {
            }

            try {
                terminal.puts(InfoCmp.Capability.exit_attribute_mode);
                terminal.writer().write("\u001b[?25h");
                terminal.flush();

                terminal.puts(InfoCmp.Capability.cursor_visible);
                terminal.puts(InfoCmp.Capability.keypad_local);
                terminal.puts(InfoCmp.Capability.exit_ca_mode);
                terminal.flush();

                terminal.setAttributes(original);
                terminal.flush();
            } catch (Exception ignored) {
            }
        };

        try {
            DatabaseProvider provider = new DatabaseProvider("jdbc:sqlite:university.db");
            new DatabaseInitializer(provider).initialize();

            PersonRepository personRepository = new JdbcPersonRepository(provider);
            StudentRepository studentRepository = new JdbcStudentRepository(provider);
            FacultyRepository facultyRepository = new JdbcFacultyRepository(provider);
            CourseRepository courseRepository = new JdbcCourseRepository(provider);
            EnrollmentRepository enrollmentRepository = new JdbcEnrollmentRepository(provider);
            TeachingRepository teachingRepository = new JdbcTeachingRepository(provider);

            StudentService studentService = new DefaultStudentService(studentRepository, personRepository);
            FacultyService facultyService = new DefaultFacultyService(facultyRepository, personRepository);
            CourseService courseService = new DefaultCourseService(courseRepository);
            EnrollmentService enrollmentService = new DefaultEnrollmentService(enrollmentRepository, studentRepository, personRepository, courseRepository);
            TeachingService teachingService = new DefaultTeachingService(teachingRepository, courseRepository, facultyRepository, personRepository);

            StudentController studentController = new StudentController(studentService);
            FacultyController facultyController = new FacultyController(facultyService);
            CourseController courseController = new CourseController(courseService);
            EnrollmentController enrollmentController = new EnrollmentController(enrollmentService);
            TeachingController teachingController = new TeachingController(teachingService);

            MainMenuScreen root = new MainMenuScreen(
                    studentController,
                    facultyController,
                    courseController,
                    enrollmentController,
                    teachingController
            );
            terminal.puts(InfoCmp.Capability.keypad_xmit);
            terminal.puts(InfoCmp.Capability.cursor_invisible);
            terminal.puts(InfoCmp.Capability.enter_ca_mode);
            terminal.flush();
            UiApp app = new UiApp(terminal, root);
            EventLoop loop = new EventLoop(app, input, pendingResize);
            loop.run();
        } finally {
            cleanup.run();
        }
    }
}
