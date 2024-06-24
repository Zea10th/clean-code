package av.biezbardis.mentorship.tasks.consoleapp.dao;

import av.biezbardis.mentorship.tasks.consoleapp.model.Course;
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
class CourseDaoTest {
    @Mock
    private NamedParameterJdbcTemplate jdbcTemplate;
    @InjectMocks
    private CourseDao dao;

    @Test
    void shouldSaveWhenProvidedCorrectEntity() {
        Course course = new Course();
        course.setName("Sample Course");
        course.setDescription("This is a test course");

        dao.save(course);

        verify(jdbcTemplate).update(
                eq(CourseDao.INSERT_COURSE_SQL_QUERY),
                ArgumentMatchers.<Map<String, Long>>any()
        );
    }

    @Test
    void shouldReturnEntityWhenProvidedCorrectId() {
        dao.findById(15L);

        verify(jdbcTemplate).queryForObject(
                eq(CourseDao.FIND_COURSE_SQL_QUERY),
                ArgumentMatchers.<Map<String, Long>>any(),
                ArgumentMatchers.<RowMapper<Course>>any()
        );
    }

    @Test
    void shouldReturnListOfEntitiesWhenCallFindAll() {
        dao.findAll();

        verify(jdbcTemplate).query(
                eq(CourseDao.FIND_ALL_COURSES_SQL_QUERY),
                ArgumentMatchers.<RowMapper<Course>>any()
        );
    }

    @Test
    void shouldUpdateWhenProvidedCorrectEntity() {
        Course course = new Course();
        course.setId(13L);
        course.setName("MyCourse");
        course.setDescription("This is a my course");

        dao.update(course);

        verify(jdbcTemplate).update(
                eq(CourseDao.UPDATE_COURSE_SQL_QUERY),
                ArgumentMatchers.<Map<String, Long>>any()
        );
    }

    @Test
    void shouldDeleteWhenProvidedCorrectEntityId() {
        dao.delete(12L);

        verify(jdbcTemplate).update(
                eq(CourseDao.DELETE_COURSE_SQL_QUERY),
                ArgumentMatchers.<Map<String, Long>>any()
        );
    }
}