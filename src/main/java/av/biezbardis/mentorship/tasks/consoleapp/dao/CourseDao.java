package av.biezbardis.mentorship.tasks.consoleapp.dao;

import av.biezbardis.mentorship.tasks.consoleapp.exception.DataAccessException;
import av.biezbardis.mentorship.tasks.consoleapp.model.Course;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CourseDao implements GenericDao<Course> {
    private static final String DELETE_COURSE_SQL_QUERY = """
            DELETE FROM course_students WHERE course_id = ?;
            DELETE FROM courses WHERE course_id = ?;""";
    private static final String FIND_ALL_COURSES_SQL_QUERY = "SELECT * FROM courses;";
    private static final String FIND_COURSE_SQL_QUERY = "SELECT * FROM courses WHERE course_id = ?;";
    private static final String INSERT_COURSE_SQL_QUERY = """
            INSERT INTO courses (course_name, course_description)
            VALUES (?, ?);""";
    private static final String UPDATE_COURSE_SQL_QUERY = """
            UPDATE courses SET course_name = ?, course_description = ?
            WHERE course_id = ?;""";
    private final ConnectionUtil connectionUtil;

    public CourseDao(ConnectionUtil connectionUtil) {
        this.connectionUtil = connectionUtil;
    }

    @Override
    public void save(Course course) {
        try (Connection connection = connectionUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(INSERT_COURSE_SQL_QUERY)) {

            statement.setString(1, course.getName());
            statement.setString(2, course.getDescription());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Failed to save entity", e);
        }
    }

    @Override
    public Optional<Course> findById(Long id) {
        try (Connection connection = connectionUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_COURSE_SQL_QUERY)) {

            statement.setLong(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                Optional<Course> optionalCourse = Optional.empty();
                while (resultSet.next()) {
                    Course course = new Course();
                    course.setId(resultSet.getLong("course_id"));
                    course.setName(resultSet.getString("course_name"));
                    course.setDescription(resultSet.getString("course_description"));
                    optionalCourse = Optional.of(course);
                }
                return optionalCourse;
            }
        } catch (SQLException e) {
            throw new DataAccessException("Failed to find entity", e);
        }
    }

    @Override
    public List<Course> findAll() {
        try (Connection connection = connectionUtil.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(FIND_ALL_COURSES_SQL_QUERY)) {

            List<Course> entities = new ArrayList<>();
            while (resultSet.next()) {
                Course course = new Course();
                course.setId(resultSet.getLong("course_id"));
                course.setName(resultSet.getString("course_name"));
                course.setDescription(resultSet.getString("course_description"));
                entities.add(course);
            }
            return entities;
        } catch (SQLException e) {
            throw new DataAccessException("Failed to find entities", e);
        }
    }

    @Override
    public void update(Course course) {
        try (Connection connection = connectionUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(UPDATE_COURSE_SQL_QUERY)) {

            statement.setString(1, course.getName());
            statement.setString(2, course.getDescription());
            statement.setLong(3, course.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Failed to update entity", e);
        }
    }

    @Override
    public void delete(Long courseId) {
        try (Connection connection = connectionUtil.getConnection();
             PreparedStatement statement = connection
                     .prepareStatement(DELETE_COURSE_SQL_QUERY)) {

            statement.setLong(1, courseId);
            statement.setLong(2, courseId);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Failed to delete entity", e);
        }
    }
}
