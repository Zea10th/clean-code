package av.biezbardis.mentorship.tasks.consoleapp.dao;

import av.biezbardis.mentorship.tasks.consoleapp.model.Student;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@Repository
public class StudentDao implements GenericDao<Student> {
    protected static final String DELETE_STUDENT_SQL_QUERY = """
            BEGIN;
            DELETE FROM course_students WHERE student_id = :student_id;
            DELETE FROM students WHERE student_id = :student_id;
            COMMIT;""";
    protected static final String FIND_ALL_STUDENTS_SQL_QUERY = "SELECT * FROM students;";
    protected static final String FIND_STUDENT_SQL_QUERY = "SELECT * FROM students WHERE student_id = :student_id;";
    protected static final String INSERT_STUDENT_SQL_QUERY = """
            INSERT INTO students (first_name, last_name, group_id)
            VALUES (:first_name, :last_name, :group_id);""";
    protected static final String UPDATE_STUDENT_SQL_QUERY = """
            UPDATE students
            SET first_name = :first_name, last_name = :last_name, group_id = :group_id
            WHERE student_id = :student_id;""";
    private static final String FIRST_NAME_LABEL = "first_name";
    private static final String GROUP_ID_LABEL = "group_id";
    private static final String ID_LABEL = "student_id";
    private static final String LAST_NAME_LABEL = "last_name";
    private final NamedParameterJdbcTemplate jdbcTemplate;

    public StudentDao(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    protected static class StudentRowMapper implements RowMapper<Student> {
        @Override
        public Student mapRow(ResultSet resultSet, int rowNum) throws SQLException {
            Student student = new Student();
            student.setId(resultSet.getLong(ID_LABEL));
            student.setFirstName(resultSet.getString(FIRST_NAME_LABEL));
            student.setLastName(resultSet.getString(LAST_NAME_LABEL));
            student.setGroupId(resultSet.getLong(GROUP_ID_LABEL));
            return student;
        }
    }

    @Override
    public void save(Student student) {
        Map<String, ?> parameters = Map.of(
                FIRST_NAME_LABEL, student.getFirstName(),
                LAST_NAME_LABEL, student.getLastName(),
                GROUP_ID_LABEL, student.getGroupId());
        jdbcTemplate.update(INSERT_STUDENT_SQL_QUERY, parameters);
    }

    @Override
    public Student findById(Long id) {
        return jdbcTemplate.queryForObject(
                FIND_STUDENT_SQL_QUERY,
                Map.of(ID_LABEL, id),
                new StudentRowMapper()
        );
    }

    @Override
    public List<Student> findAll() {
        return jdbcTemplate.query(
                FIND_ALL_STUDENTS_SQL_QUERY,
                new StudentRowMapper()
        );
    }

    @Override
    public void update(Student student) {
        Map<String, ?> parameters = Map.of(
                ID_LABEL, student.getId(),
                FIRST_NAME_LABEL, student.getFirstName(),
                LAST_NAME_LABEL, student.getLastName(),
                GROUP_ID_LABEL, student.getGroupId());
        jdbcTemplate.update(UPDATE_STUDENT_SQL_QUERY, parameters);
    }

    @Override
    public void delete(Long id) {
        jdbcTemplate.update(
                DELETE_STUDENT_SQL_QUERY,
                Map.of(ID_LABEL, id)
        );
    }
}
