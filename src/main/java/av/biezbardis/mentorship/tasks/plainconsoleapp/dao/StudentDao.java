package av.biezbardis.mentorship.tasks.plainconsoleapp.dao;

import av.biezbardis.mentorship.tasks.plainconsoleapp.exception.DataAccessException;
import av.biezbardis.mentorship.tasks.plainconsoleapp.model.Student;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class StudentDao implements GenericDao<Student> {
    private static final String DELETE_STUDENT_SQL_QUERY = """
                    DELETE FROM course_students WHERE student_id = ?;
                    DELETE FROM students WHERE student_id = ?;""";
    private static final String FIND_ALL_STUDENTS_SQL_QUERY = "SELECT * FROM students;";
    private static final String FIND_STUDENT_SQL_QUERY = "SELECT * FROM students WHERE student_id = ?;";
    private static final String INSERT_STUDENT_SQL_QUERY =
            "INSERT INTO students (first_name, last_name, group_id) VALUES (?, ?, ?);";
    private static final String UPDATE_STUDENT_SQL_QUERY =
            "UPDATE students SET first_name = ?, last_name = ?, group_id = ? WHERE student_id = ?;";
    private final ConnectionUtil connectionUtil;

    public StudentDao(ConnectionUtil connectionUtil) {
        this.connectionUtil = connectionUtil;
    }

    @Override
    public void save(Student student) {
        try (Connection connection = connectionUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(INSERT_STUDENT_SQL_QUERY)) {

            statement.setString(1, student.getFirstName());
            statement.setString(2, student.getLastName());
            statement.setLong(3, student.getGroupId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Failed to save entity", e);
        }
    }

    @Override
    public Optional<Student> findById(Long id) {
        try (Connection connection = connectionUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_STUDENT_SQL_QUERY)) {

            statement.setLong(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                Optional<Student> optionalStudent = Optional.empty();
                while (resultSet.next()) {
                    Student student = new Student();
                    student.setId(resultSet.getLong("student_id"));
                    student.setFirstName(resultSet.getString("first_name"));
                    student.setLastName(resultSet.getString("last_name"));
                    student.setGroupId(resultSet.getLong("group_id"));
                    optionalStudent = Optional.of(student);
                }
                return optionalStudent;
            }
        } catch (SQLException e) {
            throw new DataAccessException("Failed to find entity", e);
        }
    }

    @Override
    public List<Student> findAll() {
        try (Connection connection = connectionUtil.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(FIND_ALL_STUDENTS_SQL_QUERY)) {

            List<Student> entities = new ArrayList<>();
            while (resultSet.next()) {
                Student student = new Student();
                student.setId(resultSet.getLong("student_id"));
                student.setFirstName(resultSet.getString("first_name"));
                student.setLastName(resultSet.getString("last_name"));
                student.setGroupId(resultSet.getLong("group_id"));
                entities.add(student);
            }
            return entities;
        } catch (SQLException e) {
            throw new DataAccessException("Failed to find entities", e);
        }
    }

    @Override
    public void update(Student student) {
        try (Connection connection = connectionUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(UPDATE_STUDENT_SQL_QUERY)) {

            statement.setString(1, student.getFirstName());
            statement.setString(2, student.getLastName());
            statement.setLong(3, student.getGroupId());
            statement.setLong(4, student.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Failed to update entity", e);
        }
    }

    @Override
    public void delete(Long id) {
        try (Connection connection = connectionUtil.getConnection();
             PreparedStatement statement = connection
                     .prepareStatement(DELETE_STUDENT_SQL_QUERY)) {

            statement.setLong(1, id);
            statement.setLong(2, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Failed to delete entity", e);
        }
    }
}
