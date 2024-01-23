package av.biezbardis.mentorship.tasks.consoleapp.utility;

import av.biezbardis.mentorship.tasks.consoleapp.dao.PostgreSqlDaoFactory;
import av.biezbardis.mentorship.tasks.consoleapp.service.Service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Random;
import java.util.stream.Collectors;

public class DatabaseAdjuster {
    private static final InputStream SQL_QUERY_STREAM = DatabaseAdjuster.class
            .getClassLoader().getResourceAsStream("consoleapp/scheme.sql");
    private static final char[] ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
    private static final String[] COURSE_NAMES = {
            "Math", "Biology", "Physics", "Chemistry", "History",
            "Literature", "Geography", "Art", "Music", "Computer Science"};
    private static final String[] FIRST_NAMES = {
            "Alice", "Bob", "Charlie", "David", "Emma", "Frank", "Grace", "Henry", "Ivy", "Jack"};
    private static final String[] LAST_NAMES = {
            "Adams", "Brown", "Clark", "Davis", "Evans", "Fisher", "Garcia", "Hall", "Irwin", "Jones"};
    private static final String GROUP_MASK = "%c%c-%d";

    private final Service courseService;
    private final Service groupService;
    private final Service studentService;

    public DatabaseAdjuster(Service courseService, Service groupService, Service studentService) {
        this.courseService = courseService;
        this.groupService = groupService;
        this.studentService = studentService;
    }

    public void createTables() {
        try (Connection connection = PostgreSqlDaoFactory.getInstance().getConnection()) {
            Statement statement = connection.createStatement();

            statement.executeUpdate(
                    "drop table if exists students, groups, courses, student_courses");

            statement.executeUpdate(getSqlQuery());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void fillUpTables() {
        Random random = new Random();

        for (int i = 0; i < 10; i++) {
            String groupName = generateRandomGroupName(random);
            groupService.save(new String[]{groupName});
        }

        for (String courseName : COURSE_NAMES) {
            courseService.save(new String[]{courseName, "Description"});
        }

        for (int i = 0; i < 200; i++) {
            String firstName = FIRST_NAMES[random.nextInt(FIRST_NAMES.length)];
            String lastName = LAST_NAMES[random.nextInt(LAST_NAMES.length)];
            int groupId = random.nextInt(10) + 1;

            studentService.save(new String[]{firstName, lastName, String.valueOf(groupId)});
        }

        try (Connection connection = PostgreSqlDaoFactory.getInstance().getConnection()) {
            for (int studentId = 1; studentId <= 200; studentId++) {
                int numCourses = random.nextInt(3) + 1;

                for (int j = 0; j < numCourses; j++) {
                    int courseId = random.nextInt(10) + 1;
                    PreparedStatement studentCourseStatement = connection.prepareStatement(
                            "insert into student_courses(student_id, course_id) values (?, ?)"
                    );
                    studentCourseStatement.setInt(1, studentId);
                    studentCourseStatement.setInt(2, courseId);
                    studentCourseStatement.executeUpdate();
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static String generateRandomGroupName(Random random) {
        return String.format(GROUP_MASK,
                ALPHABET[random.nextInt(ALPHABET.length)],
                ALPHABET[random.nextInt(ALPHABET.length)],
                random.nextInt(100));
    }

    private String getSqlQuery() {
        return new BufferedReader(new InputStreamReader(SQL_QUERY_STREAM, StandardCharsets.UTF_8))
                .lines()
                .collect(Collectors.joining(System.lineSeparator()));
    }
}
