package av.biezbardis.mentorship.tasks.consoleapp.dao;

import av.biezbardis.mentorship.tasks.consoleapp.model.Course;
import av.biezbardis.mentorship.tasks.consoleapp.model.Student;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class StudentCourseDaoImpl implements StudentCourseDao {
    protected static final String ENROLL_IN_SQL_QUERY = """
            INSERT INTO course_students (course_id, student_id)
            VALUES (:course_id, :student_id);""";
    protected static final String FIND_COURSES_BY_STUDENT_ID_SQL_QUERY = """
            SELECT c.*
            FROM courses c
            INNER JOIN course_students cs ON c.course_id = cs.course_id
            WHERE cs.student_id = :student_id;""";
    protected static final String FIND_STUDENTS_BY_COURSE_ID_SQL_QUERY = """
            SELECT s.*
            FROM students s
            INNER JOIN course_students cs ON s.student_id = cs.student_id
            WHERE cs.course_id = :course_id;""";
    protected static final String UNENROLL_IN_SQL_QUERY = """
            DELETE FROM course_students
            WHERE course_id = :course_id AND student_id = :student_id;""";
    private static final String COURSE_ID_LABEL = "course_id";
    private static final String STUDENT_ID_LABEL = "student_id";
    private final NamedParameterJdbcTemplate jdbcTemplate;

    public StudentCourseDaoImpl(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void enrollStudentInCourse(Long studentId, Long courseId) {
        jdbcTemplate.update(
                ENROLL_IN_SQL_QUERY,
                Map.of(STUDENT_ID_LABEL, studentId,
                        COURSE_ID_LABEL, courseId)
        );
    }

    @Override
    public void unenrollStudentFromCourse(Long studentId, Long courseId) {
        jdbcTemplate.update(
                UNENROLL_IN_SQL_QUERY,
                Map.of(STUDENT_ID_LABEL, studentId,
                        COURSE_ID_LABEL, courseId)
        );
    }

    @Override
    public List<Course> getCoursesByStudentId(Long id) {
        Map<String, Long> parameters = Map.of(STUDENT_ID_LABEL, id);
        return jdbcTemplate.query(
                FIND_COURSES_BY_STUDENT_ID_SQL_QUERY,
                parameters,
                new CourseDao.CourseRowMapper()
        );
    }

    @Override
    public List<Student> getStudentsByCourseId(Long id) {
        Map<String, Long> parameters = Map.of(COURSE_ID_LABEL, id);
        return jdbcTemplate.query(
                FIND_STUDENTS_BY_COURSE_ID_SQL_QUERY,
                parameters,
                new StudentDao.StudentRowMapper()
        );
    }
}
