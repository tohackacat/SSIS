package org.example.ui;

import org.example.course.CourseController;
import org.example.course.dto.CourseDto;
import org.example.faculty.FacultyController;
import org.example.faculty.dto.FacultyDto;
import org.example.teaching.TeachingController;
import org.example.teaching.dto.TeachingDto;
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

public final class TeachingScreen implements Screen {
    private final TeachingController teachingController;
    private final CourseController courseController;
    private final FacultyController facultyController;

    private boolean dirty = true;
    private List<TeachingDto> cached = List.of();

    public TeachingScreen(TeachingController teachingController, CourseController courseController, FacultyController facultyController) {
        this.teachingController = teachingController;
        this.courseController = courseController;
        this.facultyController = facultyController;
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

        new Breadcrumbs(List.of("University", "Teachings", "N new, E edit role, D delete, R refresh, Esc back"), strong)
                .render(new Canvas(frame, top), ctx);

        int leftW = Math.max(30, (body.w() * 2) / 3);
        Rect left = new Rect(body.x(), body.y(), leftW, body.h());
        Rect right = new Rect(body.x() + leftW, body.y(), body.w() - leftW, body.h());

        Widget leftPanel = new Block("Teachings", buildTable(), base, base);
        leftPanel.render(new Canvas(frame, left), ctx);

        TeachingDto selected = selectedTeaching(ctx);
        String details = selected == null
                ? "No rows."
                : "Teaching ID: " + selected.id()
                + "\nCourse: " + selected.courseCode() + "  " + selected.courseName()
                + "\nFaculty: " + selected.facultyName()
                + "\nRole: " + selected.role();

        Widget rightPanel = new Block("Details", new Paragraph(details, base), base, base);
        rightPanel.render(new Canvas(frame, right), ctx);

        new Paragraph("Up/Down selects. N creates with pickers. E edits role. D deletes.", sel)
                .render(new Canvas(frame, bottom), ctx);
    }

    private void ensureLoaded() {
        if (!dirty) {
            return;
        }
        try {
            cached = teachingController.listTeachings();
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
        for (TeachingDto t : cached) {
            rows.add(List.of(
                    safe(t.courseCode()),
                    safe(t.courseName()),
                    safe(t.facultyName()),
                    safe(t.role())
            ));
        }

        return new TableWidget(
                "teachings.table",
                List.of("Course", "Name", "Faculty", "Role"),
                rows,
                header,
                base,
                sel
        );
    }

    private TeachingDto selectedTeaching(UiContext ctx) {
        if (cached.isEmpty()) {
            return null;
        }
        int sel = ctx.state().getInt("teachings.table.sel", 0);
        sel = Math.max(0, Math.min(cached.size() - 1, sel));
        return cached.get(sel);
    }

    private void createFlow(UiContext ctx) {
        List<CourseDto> courses = courseController.listCourses();
        List<String> courseIds = new ArrayList<>();
        List<String> courseLabels = new ArrayList<>();
        for (CourseDto c : courses) {
            courseIds.add(c.id());
            courseLabels.add(c.code() + "  " + c.name());
        }

        ctx.host().push(UiOverlays.pickOne("Pick course", courseIds, courseLabels, (ui, courseId) -> {
            List<FacultyDto> faculty = facultyController.listFaculty();
            List<String> facIds = new ArrayList<>();
            List<String> facLabels = new ArrayList<>();
            for (FacultyDto f : faculty) {
                facIds.add(f.id());
                facLabels.add(f.fullName() + "  (" + f.department() + ")");
            }

            ui.host().push(UiOverlays.pickOne("Pick faculty", facIds, facLabels, (ui2, facultyId) -> {
                ui2.host().push(UiOverlays.textInput("Role", "Type role (e.g. Lecturer) and press Enter:", "", (ui3, role) -> {
                    try {
                        teachingController.createTeaching(courseId, facultyId, role);
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
        TeachingDto t = selectedTeaching(ctx);
        if (t == null) {
            ctx.host().push(UiOverlays.message("Edit role", "No selection."));
            ctx.host().requestRedraw();
            return;
        }

        ctx.host().push(UiOverlays.textInput("Edit role", "Update role and press Enter:", t.role(), (ui, role) -> {
            try {
                teachingController.updateTeaching(t.id(), role);
                dirty = true;
            } catch (RuntimeException e) {
                ui.host().push(UiOverlays.message("Error", String.valueOf(e.getMessage())));
            }
            ui.host().requestRedraw();
        }));
        ctx.host().requestRedraw();
    }

    private void deleteSelected(UiContext ctx) {
        TeachingDto t = selectedTeaching(ctx);
        if (t == null) {
            ctx.host().push(UiOverlays.message("Delete", "No selection."));
            ctx.host().requestRedraw();
            return;
        }

        ctx.host().push(UiOverlays.confirm("Delete teaching",
                "Delete teaching:\n" + t.courseCode() + " / " + t.facultyName() + " / " + t.role() + " ?",
                (ui, ok) -> {
                    if (!ok) {
                        return;
                    }
                    try {
                        teachingController.deleteTeaching(t.id());
                        dirty = true;
                    } catch (RuntimeException e) {
                        ui.host().push(UiOverlays.message("Error", String.valueOf(e.getMessage())));
                    }
                }));
        ctx.host().requestRedraw();
    }
}
