package av.biezbardis.mentorship.tasks.plainconsoleapp.dao;

import av.biezbardis.mentorship.tasks.plainconsoleapp.exception.DataAccessException;
import av.biezbardis.mentorship.tasks.plainconsoleapp.model.Course;
import av.biezbardis.mentorship.tasks.plainconsoleapp.model.Student;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class StudentCourseDaoImpl implements StudentCourseDao {
    protected static final String CREATE_MANY_TO_MANY_FAILED = "Failed to create many-to-many relation";
    protected static final String FAILED = "Failed to get entities";
    private static final String ENROLL_IN_SQL_QUERY =
            "INSERT INTO course_students (course_id, student_id) VALUES (?, ?);";
    private static final String FIND_COURSES_BY_STUDENT_ID_SQL_QUERY = """
            SELECT c.*
            FROM courses c
            INNER JOIN course_students cs ON c.course_id = cs.course_id
            WHERE cs.student_id = ?;""";
    private static final String FIND_STUDENTS_BY_COURSE_ID_SQL_QUERY = """
            SELECT s.*
            FROM students s
            INNER JOIN course_students cs ON s.student_id = cs.student_id
            WHERE cs.course_id = ?;""";
    private static final String UNENROLL_IN_SQL_QUERY =
            "DELETE FROM course_students WHERE course_id = ? AND student_id = ?;";
    private final ConnectionUtil connectionUtil;

    public StudentCourseDaoImpl(ConnectionUtil connectionUtil) {
        this.connectionUtil = connectionUtil;
    }

    @Override
    public void enrollStudentInCourse(Long studentId, Long courseId) {
        try (Connection connection = connectionUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(ENROLL_IN_SQL_QUERY)) {

            statement.setLong(1, courseId);
            statement.setLong(2, studentId);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException(CREATE_MANY_TO_MANY_FAILED, e);
        }
    }

    @Override
    public void unenrollStudentFromCourse(Long studentId, Long courseId) {
        try (Connection connection = connectionUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(UNENROLL_IN_SQL_QUERY)) {

            statement.setLong(1, courseId);
            statement.setLong(2, studentId);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException(CREATE_MANY_TO_MANY_FAILED, e);
        }
    }

    @Override
    public List<Course> getCoursesByStudentId(Long id) {
        try (Connection connection = connectionUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_COURSES_BY_STUDENT_ID_SQL_QUERY)) {

            List<Course> entities = new ArrayList<>();
            statement.setLong(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Course course = new Course();
                    course.setId(resultSet.getLong("course_id"));
                    course.setName(resultSet.getString("course_name"));
                    course.setDescription(resultSet.getString("course_description"));
                    entities.add(course);
                }
            }
            return entities;
        } catch (SQLException e) {
            throw new DataAccessException(FAILED, e);
        }
    }

    @Override
    public List<Student> getStudentsByCourseId(Long id) {
        try (Connection connection = connectionUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_STUDENTS_BY_COURSE_ID_SQL_QUERY)) {

            List<Student> entities = new ArrayList<>();
            statement.setLong(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Student student = new Student();
                    student.setId(resultSet.getLong("student_id"));
                    student.setFirstName(resultSet.getString("first_name"));
                    student.setLastName(resultSet.getString("last_name"));
                    student.setGroupId(resultSet.getLong("group_id"));
                    entities.add(student);
                }
            }
            return entities;
        } catch (SQLException e) {
            throw new DataAccessException(FAILED, e);
        }
    }

}
