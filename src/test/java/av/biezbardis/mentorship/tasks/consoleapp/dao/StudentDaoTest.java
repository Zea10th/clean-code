package av.biezbardis.mentorship.tasks.consoleapp.dao;

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
class StudentDaoTest {
    @Mock
    private NamedParameterJdbcTemplate jdbcTemplate;
    @InjectMocks
    private StudentDao dao;

    @Test
    void shouldSaveWhenProvidedCorrectEntity() {
        Student student = new Student();
        student.setFirstName("John");
        student.setLastName("Smith");
        student.setGroupId(13L);

        dao.save(student);

        verify(jdbcTemplate).update(
                eq(StudentDao.INSERT_STUDENT_SQL_QUERY),
                ArgumentMatchers.<Map<String, Long>>any()
        );
    }

    @Test
    void shouldReturnEntityWhenProvidedCorrectId() {
        dao.findById(15L);

        verify(jdbcTemplate).queryForObject(
                eq(StudentDao.FIND_STUDENT_SQL_QUERY),
                ArgumentMatchers.<Map<String, Long>>any(),
                ArgumentMatchers.<RowMapper<Student>>any()
        );
    }

    @Test
    void shouldReturnListOfEntitiesWhenCallFindAllMethod() {
        dao.findAll();

        verify(jdbcTemplate).query(
                eq(StudentDao.FIND_ALL_STUDENTS_SQL_QUERY),
                ArgumentMatchers.<RowMapper<Student>>any()
        );
    }

    @Test
    void shouldUpdateWhenProvidedCorrectEntity() {
        Student student = new Student();
        student.setId(15L);
        student.setFirstName("John");
        student.setLastName("Smith");
        student.setGroupId(13L);

        dao.update(student);

        verify(jdbcTemplate).update(
                eq(StudentDao.UPDATE_STUDENT_SQL_QUERY),
                ArgumentMatchers.<Map<String, Long>>any()
        );
    }

    @Test
    void shouldDeleteWhenProvidedCorrectEntityId() {
        dao.delete(1L);

        verify(jdbcTemplate).update(
                eq(StudentDao.DELETE_STUDENT_SQL_QUERY),
                ArgumentMatchers.<Map<String, Long>>any()
        );
    }
}