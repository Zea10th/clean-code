package av.biezbardis.mentorship.tasks.consoleapp.dao;

import av.biezbardis.mentorship.tasks.consoleapp.model.Course;
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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CourseDaoTest {
    @InjectMocks
    private CourseDao dao;
    @Mock
    private ConnectionUtil daoFactory;
    @Mock
    private Connection mockConnection;
    @Mock
    private PreparedStatement mockStatement;
    @Mock
    private ResultSet mockResultSet;

    @BeforeEach
    void setUp() throws SQLException {
        when(daoFactory.getConnection()).thenReturn(mockConnection);
    }

    @Test
    void shouldSaveWhenProvidedCorrectEntity() throws SQLException {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
        when(mockStatement.executeUpdate()).thenReturn(1);

        Course course = new Course();
        course.setName("Sample Course");
        course.setDescription("This is a test course");
        dao.save(course);

        verify(mockStatement).setString(1, "Sample Course");
        verify(mockStatement).setString(2, "This is a test course");
        verify(mockStatement).executeUpdate();
    }

    @Test
    void shouldReturnNotEmptyOptionalWhenProvidedCorrectId() throws SQLException {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
        when(mockStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true, false);
        when(mockResultSet.getLong("course_id")).thenReturn(15L);
        when(mockResultSet.getString("course_name")).thenReturn("Sample Course");
        when(mockResultSet.getString("course_description")).thenReturn("This is a test course");

        Optional<Course> actual = dao.findById(15L);

        assertTrue(actual.isPresent());
        assertEquals(15L, actual.get().getId());
        assertEquals("Sample Course", actual.get().getName());
        assertEquals("This is a test course", actual.get().getDescription());
        verify(mockStatement).setLong(1, 15L);
        verify(mockStatement).executeQuery();
    }

    @Test
    void shouldReturnListOfEntitiesWhenCallGetAllMethod() throws SQLException {
        when(mockConnection.createStatement()).thenReturn(mockStatement);
        when(mockStatement.executeQuery(anyString())).thenReturn(mockResultSet);
        when(mockResultSet.next())
                .thenReturn(true).thenReturn(true).thenReturn(true).thenReturn(false);
        when(mockResultSet.getLong("course_id")).thenReturn(1L, 2L, 3L);
        when(mockResultSet.getString("course_name"))
                .thenReturn("Course1", "Course2", "Course3");
        when(mockResultSet.getString("course_description"))
                .thenReturn("Description of the Course1", "Description of the Course2",
                        "Description of the Course3");

        List<Course> actual = dao.findAll();

        assertEquals(3, actual.size());
        assertEquals(1, actual.get(0).getId());
        assertEquals("Course2", actual.get(1).getName());
        assertEquals("Description of the Course3", actual.get(2).getDescription());
    }

    @Test
    void shouldUpdateWhenProvidedCorrectEntity() throws SQLException {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
        when(mockStatement.executeUpdate()).thenReturn(1);

        Course course = new Course();
        course.setId(13L);
        course.setName("MyCourse");
        course.setDescription("This is a my course");
        dao.update(course);

        verify(mockStatement).setString(1, "MyCourse");
        verify(mockStatement).setString(2, "This is a my course");
        verify(mockStatement).setLong(3, 13L);
        verify(mockStatement).executeUpdate();
    }

    @Test
    void shouldDeleteWhenProvidedCorrectEntityId() throws SQLException {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
        when(mockStatement.executeUpdate()).thenReturn(1);

        dao.delete(1L);

        verify(mockStatement).setLong(1, 1L);
        verify(mockStatement).executeUpdate();
    }
}