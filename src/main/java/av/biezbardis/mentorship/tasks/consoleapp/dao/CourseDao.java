package av.biezbardis.mentorship.tasks.consoleapp.dao;

import av.biezbardis.mentorship.tasks.consoleapp.model.Course;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@Repository
public class CourseDao implements GenericDao<Course> {
    protected static final String DELETE_COURSE_SQL_QUERY = """
            BEGIN;
            DELETE FROM course_students WHERE course_id = :course_id;
            DELETE FROM courses WHERE course_id = :course_id;
            COMMIT;""";
    protected static final String FIND_ALL_COURSES_SQL_QUERY = "SELECT * FROM courses;";
    protected static final String FIND_COURSE_SQL_QUERY = """
            SELECT *
            FROM courses
            WHERE course_id = :course_id""";
    protected static final String INSERT_COURSE_SQL_QUERY = """
            INSERT INTO courses (course_name, course_description)
            VALUES (:course_name, :course_description);""";
    protected static final String UPDATE_COURSE_SQL_QUERY = """
            UPDATE courses SET course_name = :course_name, course_description = :course_description
            WHERE course_id = :course_id;""";
    private static final String DESCRIPTION_LABEL = "course_description";
    private static final String ID_LABEL = "course_id";
    private static final String NAME_LABEL = "course_name";
    private final NamedParameterJdbcTemplate jdbcTemplate;

    public CourseDao(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    protected static class CourseRowMapper implements RowMapper<Course> {
        @Override
        public Course mapRow(ResultSet rs, int rowNum) throws SQLException {
            Course course = new Course();
            course.setId(rs.getLong(ID_LABEL));
            course.setName(rs.getString(NAME_LABEL));
            course.setDescription(rs.getString(DESCRIPTION_LABEL));
            return course;
        }
    }

    @Override
    public void save(Course course) {
        Map<String, ?> parameters = Map.of(
                NAME_LABEL, course.getName(),
                DESCRIPTION_LABEL, course.getDescription());
        jdbcTemplate.update(INSERT_COURSE_SQL_QUERY, parameters);
    }

    @Override
    public Course findById(Long id) {
        return jdbcTemplate.queryForObject(
                FIND_COURSE_SQL_QUERY,
                Map.of(ID_LABEL, id),
                new CourseRowMapper()
        );
    }

    @Override
    public List<Course> findAll() {
        return jdbcTemplate.query(
                FIND_ALL_COURSES_SQL_QUERY,
                new CourseRowMapper()
        );
    }

    @Override
    public void update(Course course) {
        Map<String, ?> parameters = Map.of(
                ID_LABEL, course.getId(),
                NAME_LABEL, course.getName(),
                DESCRIPTION_LABEL, course.getDescription());
        jdbcTemplate.update(UPDATE_COURSE_SQL_QUERY, parameters);
    }

    @Override
    public void delete(Long id) {
        jdbcTemplate.update(
                DELETE_COURSE_SQL_QUERY,
                Map.of(ID_LABEL, id)
        );
    }
}
