package av.biezbardis.mentorship.tasks.consoleapp.dao;

import av.biezbardis.mentorship.tasks.consoleapp.model.Student;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class StudentDao implements Dao<Student> {
    private static final String CREATING_STUDENT_FAILED = "Creating student failed, no rows affected.";
    private static final String NO_ID_OBTAINED = "Creating student failed, no ID obtained.";
    private static final String ADD_STUDENT_SQL_QUERY =
            "insert into students (first_name, last_name, group_id) values (?, ?, ?)";
    private static final String UPDATE_STUDENT_SQL_QUERY =
            "update students set first_name = ?, last_name = ?, group_id = ? where student_id = ?";
    private static final String DELETE_STUDENT_SQL_QUERY = "delete from students where student_id = ?";
    private static final String GET_STUDENT_SQL_QUERY = "select * from students where student_id = ?";
    private static final String GET_ALL_STUDENTS_SQL_QUERY = "select * from students";
    private final PostgreSqlDaoFactory daoFactory;

    public StudentDao(PostgreSqlDaoFactory daoFactory) {
        this.daoFactory = daoFactory;
    }

    @Override
    public int save(Student student) {
        int studentId = -1;

        try (Connection connection = daoFactory.getConnection();
             PreparedStatement statement = connection
                     .prepareStatement(ADD_STUDENT_SQL_QUERY, Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, student.getFirstName());
            statement.setString(2, student.getLastName());
            statement.setInt(3, student.getGroupId());

            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException(CREATING_STUDENT_FAILED);
            }

            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (!generatedKeys.next()) {
                throw new SQLException(NO_ID_OBTAINED);
            }

            studentId = generatedKeys.getInt(1);
            student.setId(studentId);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return studentId;
    }

    @Override
    public Optional<Student> get(int studentId) {
        try (Connection connection = daoFactory.getConnection();
             PreparedStatement statement = connection
                     .prepareStatement(GET_STUDENT_SQL_QUERY, Statement.RETURN_GENERATED_KEYS)) {

            statement.setInt(1, studentId);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(extractStudentFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }

    @Override
    public List<Student> getAll() {
        List<Student> students = new ArrayList<>();

        try (Connection connection = daoFactory.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(GET_ALL_STUDENTS_SQL_QUERY)) {

            while (resultSet.next()) {
                students.add(extractStudentFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return students;
    }

    @Override
    public boolean update(Student student) {
        try (Connection connection = daoFactory.getConnection();
             PreparedStatement statement = connection
                     .prepareStatement(UPDATE_STUDENT_SQL_QUERY, Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, student.getFirstName());
            statement.setString(2, student.getLastName());
            statement.setInt(3, student.getGroupId());
            statement.setInt(4, student.getId());
            statement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean delete(int studentId) {
        try (Connection connection = daoFactory.getConnection();
             PreparedStatement statement = connection
                     .prepareStatement(DELETE_STUDENT_SQL_QUERY, Statement.RETURN_GENERATED_KEYS)) {

            statement.setInt(1, studentId);
            statement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private Student extractStudentFromResultSet(ResultSet resultSet) throws SQLException {
        Student student = new Student();
        student.setId(resultSet.getInt("student_id"));
        student.setGroupId(resultSet.getInt("group_id"));
        student.setFirstName(resultSet.getString("first_name"));
        student.setLastName(resultSet.getString("last_name"));
        return student;
    }
}
