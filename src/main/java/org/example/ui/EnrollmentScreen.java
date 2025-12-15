package org.example.ui;

import org.example.course.CourseController;
import org.example.course.dto.CourseDto;
import org.example.enrollment.EnrollmentController;
import org.example.enrollment.dto.EnrollmentDto;
import org.example.student.StudentController;
import org.example.student.dto.StudentDto;
import org.example.tui.Frame;
import org.example.tui.Screen;
import org.example.tui.UiContext;
import org.example.tui.input.InputEvent;
import org.example.tui.input.Key;
import org.example.tui.input.KeyStroke;
import org.example.tui.widget.*;
import org.jline.utils.AttributedStyle;

import java.util.ArrayList;
import java.util.List;

public final class EnrollmentScreen implements Screen {
    private final EnrollmentController enrollmentController;
    private final StudentController studentController;
    private final CourseController courseController;

    private boolean dirty = true;
    private List<EnrollmentDto> cached = List.of();
    private FilterKind filterKind = FilterKind.ALL;
    private String filterId = "";
    private String filterLabel = "All";

    public EnrollmentScreen(EnrollmentController enrollmentController, StudentController studentController, CourseController courseController) {
        this.enrollmentController = enrollmentController;
        this.studentController = studentController;
        this.courseController = courseController;
    }

    private static String safe(String s) {
        return s == null ? "" : s;
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
        ensureLoaded();

        if (event instanceof InputEvent.Key(KeyStroke stroke)) {
            if (stroke.key() instanceof Key.Named n && n == Key.Named.Escape) {
                ctx.host().pop();
                ctx.host().requestRedraw();
                return true;
            }
            if (stroke.key() instanceof Key.Char(int cp)) {
                if (cp == 'n' || cp == 'N') {
                    createFlow(ctx);
                    return true;
                }
                if (cp == 'e' || cp == 'E') {
                    editSelected(ctx);
                    return true;
                }
                if (cp == 'd' || cp == 'D') {
                    deleteSelected(ctx);
                    return true;
                }
                if (cp == 's' || cp == 'S') {
                    pickStudentFilter(ctx);
                    return true;
                }
                if (cp == 'c' || cp == 'C') {
                    pickCourseFilter(ctx);
                    return true;
                }
                if (cp == 'a' || cp == 'A') {
                    filterKind = FilterKind.ALL;
                    filterId = "";
                    filterLabel = "All";
                    dirty = true;
                    ctx.host().requestRedraw();
                    return true;
                }
                if (cp == 'r' || cp == 'R') {
                    dirty = true;
                    ctx.host().requestRedraw();
                    return true;
                }
            }
        }

        TableWidget table = buildTable();
        return table.onInput(event, ctx);
    }

    @Override
    public void render(Frame frame, UiContext ctx) {
        ensureLoaded();

        AttributedStyle base = AttributedStyle.DEFAULT;
        AttributedStyle strong = AttributedStyle.DEFAULT.bold();
        AttributedStyle sel = AttributedStyle.DEFAULT.inverse();

        Rect r = new Rect(0, 0, frame.width(), frame.height());
        Rect top = new Rect(r.x(), r.y(), r.w(), 1);
        Rect body = new Rect(r.x(), r.y() + 1, r.w(), Math.max(0, r.h() - 2));
        Rect bottom = new Rect(r.x(), r.y() + r.h() - 1, r.w(), 1);

        new Breadcrumbs(List.of("University", "Enrollments", "Filter: " + filterLabel, "S student, C course, A all, N new, E edit grade, D delete, Esc back"), strong)
                .render(new Canvas(frame, top), ctx);

        int leftW = Math.max(30, (body.w() * 2) / 3);
        Rect left = new Rect(body.x(), body.y(), leftW, body.h());
        Rect right = new Rect(body.x() + leftW, body.y(), body.w() - leftW, body.h());

        Widget leftPanel = new Block("Enrollments", buildTable(), base, base);
        leftPanel.render(new Canvas(frame, left), ctx);

        EnrollmentDto selected = selectedEnrollment(ctx);
        String details = selected == null
                ? "No rows."
                : "Enrollment ID: " + selected.id()
                + "\nStudent: " + selected.studentName()
                + "\nCourse: " + selected.courseCode() + "  " + selected.courseName()
                + "\nGrade: " + selected.grade();

        Widget rightPanel = new Block("Details", new Paragraph(details, base), base, base);
        rightPanel.render(new Canvas(frame, right), ctx);

        new Paragraph("Up/Down selects. S/C picks a filter. A clears filter. N creates with pickers.", sel)
                .render(new Canvas(frame, bottom), ctx);
    }

    private void ensureLoaded() {
        if (!dirty) {
            return;
        }
        try {
            cached = switch (filterKind) {
                case ALL -> enrollmentController.listEnrollments();
                case STUDENT -> enrollmentController.listEnrollmentsByStudent(filterId);
                case COURSE -> enrollmentController.listEnrollmentsByCourse(filterId);
            };
        } catch (RuntimeException e) {
            cached = List.of();
        }
        dirty = false;
    }

    private TableWidget buildTable() {
        AttributedStyle base = AttributedStyle.DEFAULT;
        AttributedStyle header = AttributedStyle.DEFAULT.bold();
        AttributedStyle sel = AttributedStyle.DEFAULT.inverse();

        List<List<String>> rows = new ArrayList<>();
        for (EnrollmentDto e : cached) {
            rows.add(List.of(
                    safe(e.studentName()),
                    safe(e.courseCode()),
                    safe(e.courseName()),
                    safe(e.grade())
            ));
        }

        return new TableWidget(
                "enroll.table",
                List.of("Student", "Course", "Name", "Grade"),
                rows,
                header,
                base,
                sel
        );
    }

    private EnrollmentDto selectedEnrollment(UiContext ctx) {
        if (cached.isEmpty()) {
            return null;
        }
        int sel = ctx.state().getInt("enroll.table.sel", 0);
        sel = Math.max(0, Math.min(cached.size() - 1, sel));
        return cached.get(sel);
    }

    private void pickStudentFilter(UiContext ctx) {
        List<StudentDto> students = studentController.listStudents();
        List<String> ids = new ArrayList<>();
        List<String> labels = new ArrayList<>();
        for (StudentDto s : students) {
            ids.add(s.id());
            labels.add(s.fullName() + "  (" + s.enrollmentYear() + ")");
        }

        ctx.host().push(UiOverlays.pickOne("Pick student filter", ids, labels, (ui, id) -> {
            filterKind = FilterKind.STUDENT;
            filterId = id;
            filterLabel = labels.isEmpty() ? "Student" : labels.get(Math.max(0, Math.min(labels.size() - 1, ui.state().getInt("ui.picker.1.sel", 0))));
            dirty = true;
            ui.host().requestRedraw();
        }));
        ctx.host().requestRedraw();
    }

    private void pickCourseFilter(UiContext ctx) {
        List<CourseDto> courses = courseController.listCourses();
        List<String> ids = new ArrayList<>();
        List<String> labels = new ArrayList<>();
        for (CourseDto c : courses) {
            ids.add(c.id());
            labels.add(c.code() + "  " + c.name());
        }

        ctx.host().push(UiOverlays.pickOne("Pick course filter", ids, labels, (ui, id) -> {
            filterKind = FilterKind.COURSE;
            filterId = id;
            filterLabel = "Course";
            dirty = true;
            ui.host().requestRedraw();
        }));
        ctx.host().requestRedraw();
    }

    private void createFlow(UiContext ctx) {
        List<StudentDto> students = studentController.listStudents();
        List<String> studentIds = new ArrayList<>();
        List<String> studentLabels = new ArrayList<>();
        for (StudentDto s : students) {
            studentIds.add(s.id());
            studentLabels.add(s.fullName() + "  (" + s.enrollmentYear() + ")");
        }

        ctx.host().push(UiOverlays.pickOne("Pick student", studentIds, studentLabels, (ui, studentId) -> {
            List<CourseDto> courses = courseController.listCourses();
            List<String> courseIds = new ArrayList<>();
            List<String> courseLabels = new ArrayList<>();
            for (CourseDto c : courses) {
                courseIds.add(c.id());
                courseLabels.add(c.code() + "  " + c.name());
            }

            ui.host().push(UiOverlays.pickOne("Pick course", courseIds, courseLabels, (ui2, courseId) -> {
                ui2.host().push(UiOverlays.textInput("Grade", "Type grade (e.g. A, B+, pass) and press Enter:", "", (ui3, grade) -> {
                    try {
                        enrollmentController.createEnrollment(studentId, courseId, grade);
                        dirty = true;
                    } catch (RuntimeException e) {
                        ui3.host().push(UiOverlays.message("Error", String.valueOf(e.getMessage())));
                    }
                    ui3.host().requestRedraw();
                }));
                ui2.host().requestRedraw();
            }));
            ui.host().requestRedraw();
        }));
        ctx.host().requestRedraw();
    }

    private void editSelected(UiContext ctx) {
        EnrollmentDto e = selectedEnrollment(ctx);
        if (e == null) {
            ctx.host().push(UiOverlays.message("Edit grade", "No selection."));
            ctx.host().requestRedraw();
            return;
        }

        ctx.host().push(UiOverlays.textInput("Edit grade", "Update grade and press Enter:", e.grade(), (ui, grade) -> {
            try {
                enrollmentController.updateEnrollment(e.id(), grade);
                dirty = true;
            } catch (RuntimeException ex) {
                ui.host().push(UiOverlays.message("Error", String.valueOf(ex.getMessage())));
            }
            ui.host().requestRedraw();
        }));
        ctx.host().requestRedraw();
    }

    private void deleteSelected(UiContext ctx) {
        EnrollmentDto e = selectedEnrollment(ctx);
        if (e == null) {
            ctx.host().push(UiOverlays.message("Delete", "No selection."));
            ctx.host().requestRedraw();
            return;
        }

        ctx.host().push(UiOverlays.confirm("Delete enrollment",
                "Delete enrollment:\n" + e.studentName() + " / " + e.courseCode() + " / " + e.grade() + " ?",
                (ui, ok) -> {
                    if (!ok) {
                        return;
                    }
                    try {
                        enrollmentController.deleteEnrollment(e.id());
                        dirty = true;
                    } catch (RuntimeException ex) {
                        ui.host().push(UiOverlays.message("Error", String.valueOf(ex.getMessage())));
                    }
                }));
        ctx.host().requestRedraw();
    }

    private enum FilterKind {ALL, STUDENT, COURSE}
}
