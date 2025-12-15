package org.example.ui;

import org.example.course.CourseController;
import org.example.course.dto.CourseDto;
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

public final class CourseScreen implements Screen {
    private final CourseController courseController;

    public CourseScreen(CourseController courseController) {
        this.courseController = courseController;
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
        new Breadcrumbs(List.of("University", "Courses"), AttributedStyle.DEFAULT.bold())
                .render(new Canvas(frame, new Rect(0, 0, frame.width(), 1)), ctx);

        Rect body = new Rect(0, 1, frame.width(), Math.max(0, frame.height() - 2));
        new Block("Courses", buildTable(ctx), AttributedStyle.DEFAULT, AttributedStyle.DEFAULT.bold())
                .render(new Canvas(frame, body), ctx);

        new Paragraph("N = new, Enter = edit, D = delete, Esc = back", AttributedStyle.DEFAULT.faint())
                .render(new Canvas(frame, new Rect(0, frame.height() - 1, frame.width(), 1)), ctx);
    }

    private TableWidget buildTable(UiContext ctx) {
        List<CourseDto> items = courseController.listCourses();
        List<List<String>> rows = new ArrayList<>();
        for (CourseDto c : items) {
            rows.add(List.of(
                    c.code(),
                    c.name(),
                    Integer.toString(c.credits())
            ));
        }

        return new TableWidget(
                "uni.course.table",
                List.of("Code", "Name", "Credits"),
                rows,
                AttributedStyle.DEFAULT.bold(),
                AttributedStyle.DEFAULT,
                AttributedStyle.DEFAULT.inverse()
        );
    }

    private CourseDto selected(UiContext ctx) {
        List<CourseDto> items = courseController.listCourses();
        if (items.isEmpty()) {
            return null;
        }
        int sel = ctx.state().getInt("uni.course.table.sel", 0);
        if (sel < 0) sel = 0;
        if (sel >= items.size()) sel = items.size() - 1;
        return items.get(sel);
    }

    private void createNew(UiContext ctx) {
        ctx.host().push(UiOverlays.textInput("New course", "Course code:", "", (ui, code0) -> {
            String code = (code0 == null) ? "" : code0.trim();
            if (code.isEmpty()) {
                ui.host().push(UiOverlays.message("Error", "Course code is required."));
                ui.host().requestRedraw();
                return;
            }

            ui.host().push(UiOverlays.textInput("New course", "Course name:", "", (ui2, name0) -> {
                String name = (name0 == null) ? "" : name0.trim();
                if (name.isEmpty()) {
                    ui2.host().push(UiOverlays.message("Error", "Course name is required."));
                    ui2.host().requestRedraw();
                    return;
                }

                ui2.host().push(UiOverlays.textInput("New course", "Credits:", "", (ui3, credits0) -> {
                    String creditsText = (credits0 == null) ? "" : credits0.trim();
                    int credits;
                    try {
                        credits = Integer.parseInt(creditsText);
                    } catch (RuntimeException e) {
                        ui3.host().push(UiOverlays.message("Error", "Credits must be an integer."));
                        ui3.host().requestRedraw();
                        return;
                    }

                    try {
                        courseController.createCourse(code, name, credits);
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
        CourseDto c = selected(ctx);
        if (c == null) {
            ctx.host().push(UiOverlays.message("Edit", "No selection."));
            ctx.host().requestRedraw();
            return;
        }

        ctx.host().push(UiOverlays.textInput("Edit course", "Course code:", c.code(), (ui, code0) -> {
            String code = (code0 == null) ? "" : code0.trim();
            if (code.isEmpty()) {
                ui.host().push(UiOverlays.message("Error", "Course code is required."));
                ui.host().requestRedraw();
                return;
            }

            ui.host().push(UiOverlays.textInput("Edit course", "Course name:", c.name(), (ui2, name0) -> {
                String name = (name0 == null) ? "" : name0.trim();
                if (name.isEmpty()) {
                    ui2.host().push(UiOverlays.message("Error", "Course name is required."));
                    ui2.host().requestRedraw();
                    return;
                }

                ui2.host().push(UiOverlays.textInput("Edit course", "Credits:", Integer.toString(c.credits()), (ui3, credits0) -> {
                    String creditsText = (credits0 == null) ? "" : credits0.trim();
                    int credits;
                    try {
                        credits = Integer.parseInt(creditsText);
                    } catch (RuntimeException e) {
                        ui3.host().push(UiOverlays.message("Error", "Credits must be an integer."));
                        ui3.host().requestRedraw();
                        return;
                    }

                    try {
                        courseController.updateCourse(c.id(), code, name, credits);
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
        CourseDto c = selected(ctx);
        if (c == null) {
            ctx.host().push(UiOverlays.message("Delete", "No selection."));
            ctx.host().requestRedraw();
            return;
        }

        ctx.host().push(UiOverlays.confirm(
                "Delete course",
                "Delete course:\n" + c.code() + " - " + c.name() + " ?",
                (ui, ok) -> {
                    if (!ok) {
                        return;
                    }
                    try {
                        courseController.deleteCourse(c.id());
                    } catch (RuntimeException e) {
                        ui.host().push(UiOverlays.message("Error", String.valueOf(e.getMessage())));
                    }
                    ui.host().requestRedraw();
                }
        ));
        ctx.host().requestRedraw();
    }
}
