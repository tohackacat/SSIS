package org.example.ui;

import org.example.course.CourseController;
import org.example.enrollment.EnrollmentController;
import org.example.faculty.FacultyController;
import org.example.student.StudentController;
import org.example.teaching.TeachingController;
import org.example.tui.Frame;
import org.example.tui.Screen;
import org.example.tui.UiContext;
import org.example.tui.input.InputEvent;
import org.example.tui.input.Key;
import org.example.tui.input.KeyStroke;
import org.example.tui.widget.Block;
import org.example.tui.widget.Canvas;
import org.example.tui.widget.ListWidget;
import org.example.tui.widget.Rect;
import org.jline.utils.AttributedStyle;

import java.util.ArrayList;
import java.util.List;

public final class MainMenuScreen implements Screen {
    private final StudentController studentController;
    private final FacultyController facultyController;
    private final CourseController courseController;
    private final EnrollmentController enrollmentController;
    private final TeachingController teachingController;

    private final ArrayList<String> items = new ArrayList<>(List.of(
            "Enrollments",
            "Teachings",
            "Students",
            "Faculty",
            "Courses"
    ));

    private final ListWidget menu = new ListWidget(
            "main.menu",
            items,
            AttributedStyle.DEFAULT,
            AttributedStyle.DEFAULT.inverse()
    );

    public MainMenuScreen(
            StudentController studentController,
            FacultyController facultyController,
            CourseController courseController,
            EnrollmentController enrollmentController,
            TeachingController teachingController
    ) {
        this.studentController = studentController;
        this.facultyController = facultyController;
        this.courseController = courseController;
        this.enrollmentController = enrollmentController;
        this.teachingController = teachingController;
    }

    @Override
    public int zIndex() {
        return 0;
    }

    @Override
    public boolean blocksInput() {
        return false;
    }

    @Override
    public boolean onInput(InputEvent event, UiContext ctx) {
        if (event instanceof InputEvent.Key(KeyStroke stroke)) {
            if (stroke.key() instanceof Key.Named e) {
                if (e == Key.Named.Escape) {
                    ctx.host().pop();
                    ctx.host().requestRedraw();
                    return true;
                } else if (e == Key.Named.Enter) {
                    int sel = ctx.state().getInt("main.menu.sel", 0);
                    if (sel == 0) {
                        ctx.host().push(new EnrollmentScreen(enrollmentController, studentController, courseController));
                    } else if (sel == 1) {
                        ctx.host().push(new TeachingScreen(teachingController, courseController, facultyController));
                    } else if (sel == 2) {
                        ctx.host().push(new StudentScreen(studentController));
                    } else if (sel == 3) {
                        ctx.host().push(new FacultyScreen(facultyController));
                    } else if (sel == 4) {
                        ctx.host().push(new CourseScreen(courseController));
                    }
                    ctx.host().requestRedraw();
                    return true;
                }
            }
        }
        return menu.onInput(event, ctx);
    }

    @Override
    public void render(Frame frame, UiContext ctx) {
        Rect full = new Rect(0, 0, frame.width(), frame.height());
        Canvas root = new Canvas(frame, full);
        root.fill(" ", AttributedStyle.DEFAULT);

        int h = frame.height();
        Rect body = new Rect(0, 0, frame.width(), Math.max(0, h));

        Block block = new Block(
                "University",
                menu,
                AttributedStyle.DEFAULT,
                AttributedStyle.DEFAULT.bold()
        );

        block.render(new Canvas(frame, body), ctx);
    }
}
