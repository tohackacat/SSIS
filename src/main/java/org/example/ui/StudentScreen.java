package org.example.ui;

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

public final class StudentScreen implements Screen {
    private final StudentController studentController;

    public StudentScreen(StudentController studentController) {
        this.studentController = studentController;
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
            if (stroke.key() instanceof Key.Named n && n == Key.Named.Escape) {
                ctx.host().pop();
                ctx.host().requestRedraw();
                return true;
            }

            if (stroke.key() instanceof Key.Named n && n == Key.Named.Enter) {
                editSelected(ctx);
                return true;
            }

            if (stroke.key() instanceof Key.Char(int cp)) {
                if (cp == 'n' || cp == 'N') {
                    createNew(ctx);
                    return true;
                }
                if (cp == 'd' || cp == 'D') {
                    deleteSelected(ctx);
                    return true;
                }
            }
        }

        TableWidget table = buildTable(ctx);
        if (table.onInput(event, ctx)) {
            ctx.host().requestRedraw();
            return true;
        }

        return false;
    }

    @Override
    public void render(Frame frame, UiContext ctx) {
        new Breadcrumbs(List.of("University", "Students"), AttributedStyle.DEFAULT.bold())
                .render(new Canvas(frame, new Rect(0, 0, frame.width(), 1)), ctx);

        Rect body = new Rect(0, 1, frame.width(), Math.max(0, frame.height() - 2));
        new Block("Students", buildTable(ctx), AttributedStyle.DEFAULT, AttributedStyle.DEFAULT.bold())
                .render(new Canvas(frame, body), ctx);

        new Paragraph("N = new, Enter = edit, D = delete, Esc = back", AttributedStyle.DEFAULT.faint())
                .render(new Canvas(frame, new Rect(0, frame.height() - 1, frame.width(), 1)), ctx);
    }

    private TableWidget buildTable(UiContext ctx) {
        List<StudentDto> items = studentController.listStudents();
        List<List<String>> rows = new ArrayList<>();
        for (StudentDto s : items) {
            rows.add(List.of(
                    s.fullName(),
                    s.email() == null ? "" : s.email(),
                    Integer.toString(s.enrollmentYear())
            ));
        }

        return new TableWidget(
                "uni.student.table",
                List.of("Name", "Email", "Year"),
                rows,
                AttributedStyle.DEFAULT.bold(),
                AttributedStyle.DEFAULT,
                AttributedStyle.DEFAULT.inverse()
        );
    }

    private StudentDto selected(UiContext ctx) {
        List<StudentDto> items = studentController.listStudents();
        if (items.isEmpty()) {
            return null;
        }
        int sel = ctx.state().getInt("uni.student.table.sel", 0);
        if (sel < 0) sel = 0;
        if (sel >= items.size()) sel = items.size() - 1;
        return items.get(sel);
    }

    private void createNew(UiContext ctx) {
        ctx.host().push(UiOverlays.textInput("New student", "Full name:", "", (ui, fullName0) -> {
            String fullName = (fullName0 == null) ? "" : fullName0.trim();
            if (fullName.isEmpty()) {
                ui.host().push(UiOverlays.message("Error", "Full name is required."));
                ui.host().requestRedraw();
                return;
            }

            ui.host().push(UiOverlays.textInput("New student", "Email (optional):", "", (ui2, email0) -> {
                String email = (email0 == null) ? "" : email0.trim();

                ui2.host().push(UiOverlays.textInput("New student", "Enrollment year:", "", (ui3, year0) -> {
                    String yearText = (year0 == null) ? "" : year0.trim();
                    int year;
                    try {
                        year = Integer.parseInt(yearText);
                    } catch (RuntimeException e) {
                        ui3.host().push(UiOverlays.message("Error", "Enrollment year must be an integer."));
                        ui3.host().requestRedraw();
                        return;
                    }

                    try {
                        studentController.createStudent(fullName, email, year);
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
        StudentDto s = selected(ctx);
        if (s == null) {
            ctx.host().push(UiOverlays.message("Edit", "No selection."));
            ctx.host().requestRedraw();
            return;
        }

        ctx.host().push(UiOverlays.textInput("Edit student", "Full name:", s.fullName(), (ui, fullName0) -> {
            String fullName = (fullName0 == null) ? "" : fullName0.trim();
            if (fullName.isEmpty()) {
                ui.host().push(UiOverlays.message("Error", "Full name is required."));
                ui.host().requestRedraw();
                return;
            }

            ui.host().push(UiOverlays.textInput("Edit student", "Email (optional):", s.email() == null ? "" : s.email(), (ui2, email0) -> {
                String email = (email0 == null) ? "" : email0.trim();

                ui2.host().push(UiOverlays.textInput("Edit student", "Enrollment year:", Integer.toString(s.enrollmentYear()), (ui3, year0) -> {
                    String yearText = (year0 == null) ? "" : year0.trim();
                    int year;
                    try {
                        year = Integer.parseInt(yearText);
                    } catch (RuntimeException e) {
                        ui3.host().push(UiOverlays.message("Error", "Enrollment year must be an integer."));
                        ui3.host().requestRedraw();
                        return;
                    }

                    try {
                        studentController.updateStudent(s.id(), fullName, email, year);
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

    private void deleteSelected(UiContext ctx) {
        StudentDto s = selected(ctx);
        if (s == null) {
            ctx.host().push(UiOverlays.message("Delete", "No selection."));
            ctx.host().requestRedraw();
            return;
        }

        ctx.host().push(UiOverlays.confirm(
                "Delete student",
                "Delete student:\n" + s.fullName() + " (" + s.id() + ") ?",
                (ui, ok) -> {
                    if (!ok) {
                        return;
                    }
                    try {
                        studentController.deleteStudent(s.id());
                    } catch (RuntimeException e) {
                        ui.host().push(UiOverlays.message("Error", String.valueOf(e.getMessage())));
                    }
                    ui.host().requestRedraw();
                }
        ));
        ctx.host().requestRedraw();
    }
}
