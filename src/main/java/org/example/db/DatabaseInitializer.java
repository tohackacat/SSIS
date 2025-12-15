package org.example.db;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public record DatabaseInitializer(DatabaseProvider provider) {
    public void initialize() {
        try (Connection connection = provider.getConnection()) {
            connection.setAutoCommit(false);
            try (Statement statement = connection.createStatement()) {
                executeBatchSql(statement, sqlSchema());
                executeBatchSql(statement, seedSql());
            }
            connection.commit();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    private void executeBatchSql(Statement statement, String sql) throws SQLException {
        String[] slices = sql.split(";");
        for (String slice : slices) {
            String trimmedString = slice.trim();
            if (!trimmedString.isEmpty()) {
                statement.addBatch(trimmedString);
            }
        }
        statement.executeBatch();
    }

    private String sqlSchema() {
        return """
                PRAGMA foreign_keys = ON;
                
                CREATE TABLE IF NOT EXISTS persons (
                id TEXT PRIMARY KEY,
                full_name TEXT NOT NULL,
                email TEXT
                );
                
                CREATE TABLE IF NOT EXISTS students (
                id TEXT PRIMARY KEY,
                person_id TEXT NOT NULL,
                enrollment_year INTEGER NOT NULL,
                FOREIGN KEY (person_id) REFERENCES persons(id) ON DELETE CASCADE
                );
                
                CREATE TABLE IF NOT EXISTS faculty (
                id TEXT PRIMARY KEY,
                person_id TEXT NOT NULL,
                department TEXT NOT NULL,
                rate TEXT NOT NULL,
                hours TEXT NOT NULL,
                FOREIGN KEY (person_id) REFERENCES persons(id) ON DELETE CASCADE
                );
                
                CREATE TABLE IF NOT EXISTS courses (
                id TEXT PRIMARY KEY,
                code TEXT NOT NULL UNIQUE,
                name TEXT NOT NULL UNIQUE,
                credits INTEGER NOT NULL
                );
                
                CREATE TABLE IF NOT EXISTS enrollments (
                id TEXT PRIMARY KEY,
                student_id TEXT NOT NULL,
                course_id TEXT NOT NULL,
                grade TEXT,
                FOREIGN KEY (student_id) REFERENCES students(id) ON DELETE CASCADE,
                FOREIGN KEY (course_id) REFERENCES courses(id) ON DELETE CASCADE
                );
                
                CREATE TABLE IF NOT EXISTS teachings (
                id TEXT PRIMARY KEY,
                course_id TEXT NOT NULL,
                faculty_id TEXT NOT NULL,
                role TEXT NOT NULL,
                FOREIGN KEY (course_id) REFERENCES courses(id) ON DELETE CASCADE,
                FOREIGN KEY (faculty_id) REFERENCES faculty(id) ON DELETE CASCADE
                );
                """;
    }

    private String seedSql() {
        return """
                     INSERT OR IGNORE INTO persons (id, full_name, email) VALUES
                    ('11111111-1111-1111-1111-111111111111', 'Alice Nguyen', 'alice@example.edu'),
                    ('22222222-2222-2222-2222-222222222222', 'Bob Smith', 'bob@example.edu'),
                    ('33333333-3333-3333-3333-333333333333', 'Eve Johnson', 'eve.johnson@example.edu'),
                    ('44444444-4444-4444-4444-444444444444', 'Carlos Diaz', 'carlos.diaz@example.edu'),
                    ('55555555-5555-5555-5555-555555555555', 'Diana Perez', 'diana.perez@example.edu'),
                    ('66666666-6666-6666-6666-666666666666', 'Frank Miller', 'frank.miller@example.edu'),
                    ('77777777-7777-7777-7777-777777777777', 'Grace Lee', 'grace.lee@example.edu'),
                    ('88888888-8888-8888-8888-888888888888', 'Henry Zhou', 'henry.zhou@example.edu'),
                    ('99999999-9999-9999-9999-999999999999', 'Irene Schmidt', 'irene.schmidt@example.edu'),
                    ('aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaa01', 'Jack Taylor', 'jack.taylor@example.edu'),
                    ('aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaa02', 'Karen Adams', 'karen.adams@example.edu'),
                    ('aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaa03', 'Liam O''Connor', 'liam.oconnor@example.edu'),
                    ('aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaa04', 'Mia Rossi', 'mia.rossi@example.edu'),
                    ('aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaa05', 'Noah Becker', 'noah.becker@example.edu'),
                    ('aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaa06', 'Olivia Costa', 'olivia.costa@example.edu'),
                    ('aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaa07', 'Paul Hughes', 'paul.hughes@example.edu'),
                    ('aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaa08', 'Quinn Park', 'quinn.park@example.edu'),
                    ('aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaa09', 'Riley Kim', 'riley.kim@example.edu'),
                    ('aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaa0a', 'Sophia Novak', 'sophia.novak@example.edu'),
                    ('aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaa0b', 'Thomas Meyer', 'thomas.meyer@example.edu'),
                    ('aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaa0c', 'Uma Patel', 'uma.patel@example.edu'),
                    ('aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaa0d', 'Victor Ivanov', 'victor.ivanov@example.edu'),
                    ('aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaa0e', 'Willow Brown', 'willow.brown@example.edu'),
                    ('aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaa0f', 'Yara Cohen', 'yara.cohen@example.edu'),
                    ('aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaa10', 'Zane Foster', 'zane.foster@example.edu'),
                    ('bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbb0001', 'Prof. Alan Turing', 'alan.turing@example.edu'),
                    ('bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbb0002', 'Prof. Barbara Liskov', 'barbara.liskov@example.edu'),
                    ('bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbb0003', 'Prof. Edsger Dijkstra', 'edsger.dijkstra@example.edu'),
                    ('bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbb0004', 'Prof. Donald Knuth', 'donald.knuth@example.edu'),
                    ('bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbb0005', 'Prof. Adele Goldberg', 'adele.goldberg@example.edu'),
                    ('bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbb0006', 'Prof. Niklaus Wirth', 'niklaus.wirth@example.edu'),
                    ('bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbb0007', 'Prof. Margaret Hamilton', 'margaret.hamilton@example.edu');
                
                INSERT OR IGNORE INTO students (id, person_id, enrollment_year) VALUES
                    ('aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa', '11111111-1111-1111-1111-111111111111', 2023),
                    ('bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb', '22222222-2222-2222-2222-222222222222', 2022),
                    ('cccccccc-cccc-cccc-cccc-cccccccc0001', '44444444-4444-4444-4444-444444444444', 2021),
                    ('cccccccc-cccc-cccc-cccc-cccccccc0002', '55555555-5555-5555-5555-555555555555', 2024),
                    ('cccccccc-cccc-cccc-cccc-cccccccc0003', '66666666-6666-6666-6666-666666666666', 2020),
                    ('cccccccc-cccc-cccc-cccc-cccccccc0004', '77777777-7777-7777-7777-777777777777', 2023),
                    ('cccccccc-cccc-cccc-cccc-cccccccc0005', '88888888-8888-8888-8888-888888888888', 2022),
                    ('cccccccc-cccc-cccc-cccc-cccccccc0006', '99999999-9999-9999-9999-999999999999', 2021),
                    ('cccccccc-cccc-cccc-cccc-cccccccc0007', 'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaa01', 2023),
                    ('cccccccc-cccc-cccc-cccc-cccccccc0008', 'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaa02', 2023),
                    ('cccccccc-cccc-cccc-cccc-cccccccc0009', 'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaa03', 2024),
                    ('cccccccc-cccc-cccc-cccc-cccccccc000a', 'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaa04', 2022),
                    ('cccccccc-cccc-cccc-cccc-cccccccc000b', 'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaa05', 2021),
                    ('cccccccc-cccc-cccc-cccc-cccccccc000c', 'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaa06', 2024),
                    ('cccccccc-cccc-cccc-cccc-cccccccc000d', 'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaa07', 2023),
                    ('cccccccc-cccc-cccc-cccc-cccccccc000e', 'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaa08', 2022),
                    ('cccccccc-cccc-cccc-cccc-cccccccc000f', 'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaa09', 2020),
                    ('cccccccc-cccc-cccc-cccc-cccccccc0010', 'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaa0a', 2024),
                    ('cccccccc-cccc-cccc-cccc-cccccccc0011', 'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaa0b', 2021),
                    ('cccccccc-cccc-cccc-cccc-cccccccc0012', 'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaa0c', 2023),
                    ('cccccccc-cccc-cccc-cccc-cccccccc0013', 'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaa0d', 2022),
                    ('cccccccc-cccc-cccc-cccc-cccccccc0014', 'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaa0e', 2021),
                    ('cccccccc-cccc-cccc-cccc-cccccccc0015', 'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaa0f', 2020),
                    ('cccccccc-cccc-cccc-cccc-cccccccc0016', 'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaa10', 2024);
                
                INSERT OR IGNORE INTO faculty (id, person_id, department, rate, hours) VALUES
                    ('cccccccc-cccc-cccc-cccc-cccccccccccc', '33333333-3333-3333-3333-333333333333', 'Computer Science, 24, 40'),
                    ('dddddddd-dddd-dddd-dddd-dddddddd0001', 'bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbb0001', 'Computer Science, 24, 40'),
                    ('dddddddd-dddd-dddd-dddd-dddddddd0002', 'bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbb0002', 'Computer Science, 24, 40'),
                    ('dddddddd-dddd-dddd-dddd-dddddddd0003', 'bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbb0003', 'Mathematics, 24, 40'),
                    ('dddddddd-dddd-dddd-dddd-dddddddd0004', 'bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbb0004', 'Computer Science, 24, 40'),
                    ('dddddddd-dddd-dddd-dddd-dddddddd0005', 'bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbb0005', 'Software Engineering, 24, 40'),
                    ('dddddddd-dddd-dddd-dddd-dddddddd0006', 'bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbb0006', 'Computer Science, 24, 40'),
                    ('dddddddd-dddd-dddd-dddd-dddddddd0007', 'bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbb0007', 'Aerospace, 24, 40');
                
                INSERT OR IGNORE INTO courses (id, code, name, credits) VALUES
                    ('dddddddd-dddd-dddd-dddd-dddddddddddd', 'CS101', 'Introduction to Computer Science', 6),
                    ('eeeeeeee-eeee-eeee-eeee-eeeeeeeeeeee', 'MA201', 'Linear Algebra', 5),
                    ('ffffffff-ffff-ffff-ffff-ffffffff0001', 'CS102', 'Data Structures', 6),
                    ('ffffffff-ffff-ffff-ffff-ffffffff0002', 'CS201', 'Algorithms', 6),
                    ('ffffffff-ffff-ffff-ffff-ffffffff0003', 'CS202', 'Operating Systems', 6),
                    ('ffffffff-ffff-ffff-ffff-ffffffff0004', 'CS203', 'Computer Networks', 5),
                    ('ffffffff-ffff-ffff-ffff-ffffffff0005', 'CS204', 'Databases', 5),
                    ('ffffffff-ffff-ffff-ffff-ffffffff0006', 'CS205', 'Software Engineering', 5),
                    ('ffffffff-ffff-ffff-ffff-ffffffff0007', 'CS301', 'Compilers', 6),
                    ('ffffffff-ffff-ffff-ffff-ffffffff0008', 'CS302', 'Distributed Systems', 6),
                    ('ffffffff-ffff-ffff-ffff-ffffffff0009', 'CS303', 'Artificial Intelligence', 6),
                    ('ffffffff-ffff-ffff-ffff-ffffffff000a', 'CS304', 'Machine Learning', 6),
                    ('ffffffff-ffff-ffff-ffff-ffffffff000b', 'CS305', 'Computer Graphics', 5),
                    ('ffffffff-ffff-ffff-ffff-ffffffff000c', 'MA101', 'Calculus I', 5),
                    ('ffffffff-ffff-ffff-ffff-ffffffff000d', 'MA102', 'Calculus II', 5),
                    ('ffffffff-ffff-ffff-ffff-ffffffff000e', 'MA202', 'Discrete Mathematics', 5),
                    ('ffffffff-ffff-ffff-ffff-ffffffff000f', 'MA301', 'Probability and Statistics', 5),
                    ('ffffffff-ffff-ffff-ffff-ffffffff0010', 'SE201', 'Requirements Engineering', 4),
                    ('ffffffff-ffff-ffff-ffff-ffffffff0011', 'SE202', 'Software Testing', 4),
                    ('ffffffff-ffff-ffff-ffff-ffffffff0012', 'SE203', 'Software Architecture', 5),
                    ('ffffffff-ffff-ffff-ffff-ffffffff0013', 'PH101', 'Introduction to Philosophy of Science', 3),
                    ('ffffffff-ffff-ffff-ffff-ffffffff0014', 'CS401', 'Advanced Algorithms', 6),
                    ('ffffffff-ffff-ffff-ffff-ffffffff0015', 'CS402', 'Advanced Machine Learning', 6);
                
                INSERT OR IGNORE INTO enrollments (id, student_id, course_id, grade) VALUES
                    ('99999999-9999-9999-9999-999999999999', 'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa', 'dddddddd-dddd-dddd-dddd-dddddddddddd', 'A'),
                    ('eeeeeeee-eeee-eeee-eeee-000000000001', 'bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb', 'dddddddd-dddd-dddd-dddd-dddddddddddd', 'B'),
                    ('eeeeeeee-eeee-eeee-eeee-000000000002', 'bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb', 'eeeeeeee-eeee-eeee-eeee-eeeeeeeeeeee', 'A'),
                    ('eeeeeeee-eeee-eeee-eeee-000000000003', 'cccccccc-cccc-cccc-cccc-cccccccc0001', 'dddddddd-dddd-dddd-dddd-dddddddddddd', 'C'),
                    ('eeeeeeee-eeee-eeee-eeee-000000000004', 'cccccccc-cccc-cccc-cccc-cccccccc0002', 'ffffffff-ffff-ffff-ffff-ffffffff0001', 'B'),
                    ('eeeeeeee-eeee-eeee-eeee-000000000005', 'cccccccc-cccc-cccc-cccc-cccccccc0003', 'ffffffff-ffff-ffff-ffff-ffffffff0002', 'A'),
                    ('eeeeeeee-eeee-eeee-eeee-000000000006', 'cccccccc-cccc-cccc-cccc-cccccccc0004', 'ffffffff-ffff-ffff-ffff-ffffffff0003', 'B'),
                    ('eeeeeeee-eeee-eeee-eeee-000000000007', 'cccccccc-cccc-cccc-cccc-cccccccc0005', 'ffffffff-ffff-ffff-ffff-ffffffff0004', 'A'),
                    ('eeeeeeee-eeee-eeee-eeee-000000000008', 'cccccccc-cccc-cccc-cccc-cccccccc0006', 'ffffffff-ffff-ffff-ffff-ffffffff0005', 'B'),
                    ('eeeeeeee-eeee-eeee-eeee-000000000009', 'cccccccc-cccc-cccc-cccc-cccccccc0007', 'ffffffff-ffff-ffff-ffff-ffffffff0006', 'C'),
                    ('eeeeeeee-eeee-eeee-eeee-00000000000a', 'cccccccc-cccc-cccc-cccc-cccccccc0008', 'ffffffff-ffff-ffff-ffff-ffffffff0007', 'A'),
                    ('eeeeeeee-eeee-eeee-eeee-00000000000b', 'cccccccc-cccc-cccc-cccc-cccccccc0009', 'ffffffff-ffff-ffff-ffff-ffffffff0008', 'B'),
                    ('eeeeeeee-eeee-eeee-eeee-00000000000c', 'cccccccc-cccc-cccc-cccc-cccccccc000a', 'ffffffff-ffff-ffff-ffff-ffffffff0009', 'A'),
                    ('eeeeeeee-eeee-eeee-eeee-00000000000d', 'cccccccc-cccc-cccc-cccc-cccccccc000b', 'ffffffff-ffff-ffff-ffff-ffffffff000a', 'B'),
                    ('eeeeeeee-eeee-eeee-eeee-00000000000e', 'cccccccc-cccc-cccc-cccc-cccccccc000c', 'ffffffff-ffff-ffff-ffff-ffffffff000b', 'A'),
                    ('eeeeeeee-eeee-eeee-eeee-00000000000f', 'cccccccc-cccc-cccc-cccc-cccccccc000d', 'ffffffff-ffff-ffff-ffff-ffffffff000c', 'C'),
                    ('eeeeeeee-eeee-eeee-eeee-000000000010', 'cccccccc-cccc-cccc-cccc-cccccccc000e', 'ffffffff-ffff-ffff-ffff-ffffffff000d', 'B'),
                    ('eeeeeeee-eeee-eeee-eeee-000000000011', 'cccccccc-cccc-cccc-cccc-cccccccc000f', 'ffffffff-ffff-ffff-ffff-ffffffff000e', 'A'),
                    ('eeeeeeee-eeee-eeee-eeee-000000000012', 'cccccccc-cccc-cccc-cccc-cccccccc0010', 'ffffffff-ffff-ffff-ffff-ffffffff000f', 'B'),
                    ('eeeeeeee-eeee-eeee-eeee-000000000013', 'cccccccc-cccc-cccc-cccc-cccccccc0011', 'ffffffff-ffff-ffff-ffff-ffffffff0010', 'A'),
                    ('eeeeeeee-eeee-eeee-eeee-000000000014', 'cccccccc-cccc-cccc-cccc-cccccccc0012', 'ffffffff-ffff-ffff-ffff-ffffffff0011', 'B'),
                    ('eeeeeeee-eeee-eeee-eeee-000000000015', 'cccccccc-cccc-cccc-cccc-cccccccc0013', 'ffffffff-ffff-ffff-ffff-ffffffff0012', 'A'),
                    ('eeeeeeee-eeee-eeee-eeee-000000000016', 'cccccccc-cccc-cccc-cccc-cccccccc0014', 'ffffffff-ffff-ffff-ffff-ffffffff0013', 'B'),
                    ('eeeeeeee-eeee-eeee-eeee-000000000017', 'cccccccc-cccc-cccc-cccc-cccccccc0015', 'ffffffff-ffff-ffff-ffff-ffffffff0014', 'A'),
                    ('eeeeeeee-eeee-eeee-eeee-000000000018', 'cccccccc-cccc-cccc-cccc-cccccccc0016', 'ffffffff-ffff-ffff-ffff-ffffffff0015', 'B');
                
                INSERT OR IGNORE INTO teachings (id, course_id, faculty_id, role) VALUES
                    ('88888888-8888-8888-8888-888888888888', 'dddddddd-dddd-dddd-dddd-dddddddddddd', 'cccccccc-cccc-cccc-cccc-cccccccccccc', 'Lecturer'),
                    ('77777777-7777-7777-7777-000000000001', 'eeeeeeee-eeee-eeee-eeee-eeeeeeeeeeee', 'dddddddd-dddd-dddd-dddd-dddddddd0003', 'Lecturer'),
                    ('77777777-7777-7777-7777-000000000002', 'ffffffff-ffff-ffff-ffff-ffffffff0001', 'dddddddd-dddd-dddd-dddd-dddddddd0001', 'Lecturer'),
                    ('77777777-7777-7777-7777-000000000003', 'ffffffff-ffff-ffff-ffff-ffffffff0002', 'dddddddd-dddd-dddd-dddd-dddddddd0001', 'Lecturer'),
                    ('77777777-7777-7777-7777-000000000004', 'ffffffff-ffff-ffff-ffff-ffffffff0003', 'dddddddd-dddd-dddd-dddd-dddddddd0002', 'Lecturer'),
                    ('77777777-7777-7777-7777-000000000005', 'ffffffff-ffff-ffff-ffff-ffffffff0004', 'dddddddd-dddd-dddd-dddd-dddddddd0002', 'Lecturer'),
                    ('77777777-7777-7777-7777-000000000006', 'ffffffff-ffff-ffff-ffff-ffffffff0005', 'dddddddd-dddd-dddd-dddd-dddddddd0005', 'Lecturer'),
                    ('77777777-7777-7777-7777-000000000007', 'ffffffff-ffff-ffff-ffff-ffffffff0006', 'dddddddd-dddd-dddd-dddd-dddddddd0005', 'Lecturer'),
                    ('77777777-7777-7777-7777-000000000008', 'ffffffff-ffff-ffff-ffff-ffffffff0007', 'dddddddd-dddd-dddd-dddd-dddddddd0004', 'Lecturer'),
                    ('77777777-7777-7777-7777-000000000009', 'ffffffff-ffff-ffff-ffff-ffffffff0008', 'dddddddd-dddd-dddd-dddd-dddddddd0004', 'Lecturer'),
                    ('77777777-7777-7777-7777-00000000000a', 'ffffffff-ffff-ffff-ffff-ffffffff0009', 'dddddddd-dddd-dddd-dddd-dddddddd0002', 'Lecturer'),
                    ('77777777-7777-7777-7777-00000000000b', 'ffffffff-ffff-ffff-ffff-ffffffff000a', 'dddddddd-dddd-dddd-dddd-dddddddd0007', 'Lecturer'),
                    ('77777777-7777-7777-7777-00000000000c', 'ffffffff-ffff-ffff-ffff-ffffffff000b', 'dddddddd-dddd-dddd-dddd-dddddddd0007', 'Lecturer'),
                    ('77777777-7777-7777-7777-00000000000d', 'ffffffff-ffff-ffff-ffff-ffffffff000c', 'dddddddd-dddd-dddd-dddd-dddddddd0003', 'Lecturer'),
                    ('77777777-7777-7777-7777-00000000000e', 'ffffffff-ffff-ffff-ffff-ffffffff000d', 'dddddddd-dddd-dddd-dddd-dddddddd0003', 'Lecturer'),
                    ('77777777-7777-7777-7777-00000000000f', 'ffffffff-ffff-ffff-ffff-ffffffff000e', 'dddddddd-dddd-dddd-dddd-dddddddd0001', 'Lecturer'),
                    ('77777777-7777-7777-7777-000000000010', 'ffffffff-ffff-ffff-ffff-ffffffff000f', 'dddddddd-dddd-dddd-dddd-dddddddd0006', 'Lecturer'),
                    ('77777777-7777-7777-7777-000000000011', 'ffffffff-ffff-ffff-ffff-ffffffff0010', 'dddddddd-dddd-dddd-dddd-dddddddd0005', 'Lecturer'),
                    ('77777777-7777-7777-7777-000000000012', 'ffffffff-ffff-ffff-ffff-ffffffff0011', 'dddddddd-dddd-dddd-dddd-dddddddd0005', 'Lecturer'),
                    ('77777777-7777-7777-7777-000000000013', 'ffffffff-ffff-ffff-ffff-ffffffff0012', 'dddddddd-dddd-dddd-dddd-dddddddd0006', 'Lecturer'),
                    ('77777777-7777-7777-7777-000000000014', 'ffffffff-ffff-ffff-ffff-ffffffff0013', 'dddddddd-dddd-dddd-dddd-dddddddd0004', 'Lecturer'),
                    ('77777777-7777-7777-7777-000000000015', 'ffffffff-ffff-ffff-ffff-ffffffff0014', 'dddddddd-dddd-dddd-dddd-dddddddd0001', 'Lecturer'),
                    ('77777777-7777-7777-7777-000000000016', 'ffffffff-ffff-ffff-ffff-ffffffff0015', 'dddddddd-dddd-dddd-dddd-dddddddd0002', 'Lecturer');
                
                """;
    }

}
