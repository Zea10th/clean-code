package av.biezbardis.mentorship.tasks.consoleapp.dao;

import av.biezbardis.mentorship.tasks.consoleapp.model.Course;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CourseDao implements Dao<Course> {
    private static final String CREATING_COURSE_FAILED = "Creating course failed, no rows affected.";
    private static final String NO_ID_OBTAINED = "Creating course failed, no ID obtained.";
    private static final String ADD_COURSE_SQL_QUERY =
            "insert into courses (course_name, course_description) values (?, ?)";
    private static final String UPDATE_COURSE_SQL_QUERY =
            "update courses set course_name = ?, course_description = ? where course_id = ?";
    private static final String DELETE_COURSE_SQL_QUERY = "delete from courses where course_id = ?";
    private static final String GET_COURSE_SQL_QUERY = "select * from courses where course_id = ?";
    private static final String GET_ALL_COURSES_SQL_QUERY = "select * from courses";
    private final PostgreSqlDaoFactory daoFactory;

    public CourseDao(PostgreSqlDaoFactory daoFactory) {
        this.daoFactory = daoFactory;
    }

    @Override
    public int save(Course course) {
        int courseId = -1;

        try (Connection connection = daoFactory.getConnection();
             PreparedStatement statement = connection
                     .prepareStatement(ADD_COURSE_SQL_QUERY, Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, course.getName());
            statement.setString(2, course.getDescription());

            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException(CREATING_COURSE_FAILED);
            }

            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (!generatedKeys.next()) {
                throw new SQLException(NO_ID_OBTAINED);
            }

            courseId = generatedKeys.getInt(1);
            course.setId(courseId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return courseId;
    }

    @Override
    public Optional<Course> get(int courseId) {
        try (Connection connection = daoFactory.getConnection();
             PreparedStatement statement = connection
                     .prepareStatement(GET_COURSE_SQL_QUERY, Statement.RETURN_GENERATED_KEYS)) {

            statement.setInt(1, courseId);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(extractCourseFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }

    @Override
    public List<Course> getAll() {
        List<Course> courses = new ArrayList<>();

        try (Connection connection = daoFactory.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(GET_ALL_COURSES_SQL_QUERY)) {

            while (resultSet.next()) {
                courses.add(extractCourseFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return courses;
    }

    @Override
    public boolean update(Course course) {
        try (Connection connection = daoFactory.getConnection();
             PreparedStatement statement = connection
                     .prepareStatement(UPDATE_COURSE_SQL_QUERY, Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, course.getName());
            statement.setString(2, course.getDescription());
            statement.setInt(3, course.getId());
            statement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean delete(int courseId) {
        try (Connection connection = daoFactory.getConnection();
             PreparedStatement statement = connection
                     .prepareStatement(DELETE_COURSE_SQL_QUERY, Statement.RETURN_GENERATED_KEYS)) {

            statement.setInt(1, courseId);
            statement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private Course extractCourseFromResultSet(ResultSet resultSet) throws SQLException {
        Course course = new Course();
        course.setId(resultSet.getInt("course_id"));
        course.setName(resultSet.getString("course_name"));
        course.setDescription(resultSet.getString("course_description"));
        return course;
    }
}
