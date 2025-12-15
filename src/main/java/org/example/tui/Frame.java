package org.example.tui;

import org.jline.terminal.Size;
import org.jline.utils.AttributedString;
import org.jline.utils.AttributedStringBuilder;
import org.jline.utils.AttributedStyle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class Frame {
    private final Size size;
    private final Cell[][] cells;

    public Frame(Size size) {
        this.size = size;
        this.cells = new Cell[size.getRows()][size.getColumns()];
        clear();
    }

    public Position center() {
        return new Position(size.getColumns() / 2, size.getRows() / 2);
    }

    public int centerRow() {
        return size.getRows() / 2;
    }

    public int centerColumn() {
        return size.getColumns() / 2;
    }

    public Size size() {
        return size;
    }

    public int width() {
        return size.getColumns();
    }

    public int height() {
        return size.getRows();
    }

    public Cell get(int x, int y) {
        return cells[y][x];
    }

    public void set(int x, int y, Cell cell) {
        cells[y][x] = cell;
    }

    public void putAt(int x, int y, Cell[][] block) {
        int blockRows = block.length;
        for (int by = 0; by < blockRows; by++) {
            int fy = y + by;
            if (fy < 0 || fy >= height()) {
                continue;
            }

            Cell[] blockRow = block[by];
            int blockCols = blockRow.length;

            for (int bx = 0; bx < blockCols; bx++) {
                int fx = x + bx;
                if (fx < 0 || fx >= width()) {
                    continue;
                }

                Cell cell = blockRow[bx];
                if (cell != null) {
                    cells[fy][fx] = cell;
                }
            }
        }
    }

    public void putAt(int x, int y, Cell[] rowCells) {
        if (y < 0 || y >= height()) {
            return;
        }
        int cols = width();
        for (int i = 0; i < rowCells.length; i++) {
            int fx = x + i;
            if (fx < 0) {
                continue;
            }
            if (fx >= cols) {
                break;
            }
            Cell cell = rowCells[i];
            if (cell != null) {
                cells[y][fx] = cell;
            }
        }
    }

    public List<AttributedString> prepare() {
        int rows = height();
        int cols = width();
        List<AttributedString> list = new ArrayList<>(rows);

        for (int y = 0; y < rows; y++) {
            AttributedStringBuilder builder = new AttributedStringBuilder(cols);
            int colPos = 0;

            for (int x = 0; x < cols; x++) {
                Cell cell = cells[y][x];
                if (cell == null) {
                    cell = Cell.empty();
                }
                if (cell.continuation()) {
                    continue;
                }
                if (!builder.style().equals(cell.style())) {
                    builder.style(cell.style());
                }
                builder.append(cell.asString());
                colPos += Math.max(1, cell.width());
            }

            if (colPos < cols) {
                if (!builder.style().equals(AttributedStyle.DEFAULT)) {
                    builder.style(AttributedStyle.DEFAULT);
                }
                builder.append(" ".repeat(cols - colPos));
            }

            AttributedString row = builder.toAttributedString();
            if (row.columnLength() > cols) {
                row = row.columnSubSequence(0, cols);
            }
            list.add(row);
        }

        return list;
    }

    public void clear() {
        Cell empty = Cell.empty();
        for (int y = 0; y < height(); y++) {
            Arrays.fill(cells[y], empty);
        }
    }
}
