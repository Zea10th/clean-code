package av.biezbardis.mentorship.tasks.consoleapp.dao;

import av.biezbardis.mentorship.tasks.consoleapp.model.Course;
import av.biezbardis.mentorship.tasks.consoleapp.model.Student;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.Map;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class StudentCourseDaoImplTest {
    @Mock
    private NamedParameterJdbcTemplate jdbcTemplate;
    @InjectMocks
    private StudentCourseDaoImpl repository;

    @Test
    void shouldEnrollStudentWhenProvidedCorrectIds() {
        repository.enrollStudentInCourse(1L, 2L);

        verify(jdbcTemplate).update(
                eq(StudentCourseDaoImpl.ENROLL_IN_SQL_QUERY),
                ArgumentMatchers.<Map<String, Long>>any()
        );
    }

    @Test
    void shouldUnenrollStudentWhenProvidedCorrectIds() {
        repository.unenrollStudentFromCourse(2L, 3L);

        verify(jdbcTemplate).update(
                eq(StudentCourseDaoImpl.UNENROLL_IN_SQL_QUERY),
                ArgumentMatchers.<Map<String, Long>>any()
        );
    }

    @Test
    void shouldReturnCoursesByStudentWhenProvidedCorrectStudentId() {
        repository.getCoursesByStudentId(1L);

        verify(jdbcTemplate).query(
                eq(StudentCourseDaoImpl.FIND_COURSES_BY_STUDENT_ID_SQL_QUERY),
                ArgumentMatchers.<Map<String, Long>>any(),
                ArgumentMatchers.<RowMapper<Course>>any()
        );
    }

    @Test
    void shouldReturnStudentsByCourseWhenProvidedCorrectCourseId() {
        repository.getStudentsByCourseId(15L);

        verify(jdbcTemplate).query(
                eq(StudentCourseDaoImpl.FIND_STUDENTS_BY_COURSE_ID_SQL_QUERY),
                ArgumentMatchers.<Map<String, Long>>any(),
                ArgumentMatchers.<RowMapper<Student>>any()
        );
    }
}
