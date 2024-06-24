package av.biezbardis.mentorship.tasks.plainconsoleapp.dao;

import av.biezbardis.mentorship.tasks.plainconsoleapp.model.Course;
import av.biezbardis.mentorship.tasks.plainconsoleapp.model.Student;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StudentCourseDaoImplTest {
    @Mock
    private ConnectionUtil daoFactory;
    @Mock
    private Connection mockConnection;
    @Mock
    private PreparedStatement mockStatement;
    @Mock
    private ResultSet mockResultSet;
    @InjectMocks
    private StudentCourseDaoImpl dao;

    @BeforeEach
    void setUp() throws SQLException {
        when(daoFactory.getConnection()).thenReturn(mockConnection);
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
    }

    @Test
    void shouldEnrollStudentWhenProvidedCorrectIds() throws SQLException {
        when(mockStatement.executeUpdate()).thenReturn(1);

        dao.enrollStudentInCourse(1L, 2L);

        verify(mockStatement).setLong(1, 2L);
        verify(mockStatement).setLong(2, 1L);
        verify(mockStatement).executeUpdate();
    }

    @Test
    void shouldUnenrollStudentWhenProvidedCorrectIds() throws SQLException {
        when(mockStatement.executeUpdate()).thenReturn(1);

        dao.unenrollStudentFromCourse(2L, 3L);

        verify(mockStatement).setLong(1, 3L);
        verify(mockStatement).setLong(2, 2L);
        verify(mockStatement).executeUpdate();
    }

    @Test
    void shouldReturnCoursesByStudentWhenProvidedCorrectStudentId() throws SQLException {
        when(mockStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next())
                .thenReturn(true).thenReturn(true).thenReturn(true).thenReturn(false);
        when(mockResultSet.getLong("course_id")).thenReturn(1L, 2L, 3L);
        when(mockResultSet.getString("course_name"))
                .thenReturn("Course1", "Course2", "Course3");
        when(mockResultSet.getString("course_description"))
                .thenReturn("Description of the Course1", "Description of the Course2",
                        "Description of the Course3");

        List<Course> actual = dao.getCoursesByStudentId(1L);

        assertEquals(3, actual.size());
        assertEquals(1L, actual.get(0).getId());
        assertEquals("Course2", actual.get(1).getName());
        assertEquals("Description of the Course3", actual.get(2).getDescription());
    }

    @Test
    void shouldReturnStudentsByCourseWhenProvidedCorrectCourseId() throws SQLException {
        when(mockStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next())
                .thenReturn(true).thenReturn(true).thenReturn(true).thenReturn(false);
        when(mockResultSet.getLong("student_id")).thenReturn(1L, 2L, 3L);
        when(mockResultSet.getString("first_name"))
                .thenReturn("John", "Beth", "Monty");
        when(mockResultSet.getString("last_name"))
                .thenReturn("Smith", "Gray", "Python");
        when(mockResultSet.getLong("group_id")).thenReturn(1L, 2L, 3L);

        List<Student> actual = dao.getStudentsByCourseId(15L);

        assertEquals(3, actual.size());
        assertEquals(1L, actual.get(0).getId());
        assertEquals(1L, actual.get(0).getGroupId());
        assertEquals("Beth", actual.get(1).getFirstName());
        assertEquals("Python", actual.get(2).getLastName());
    }
}
