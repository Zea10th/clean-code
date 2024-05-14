package av.biezbardis.mentorship.tasks.consoleapp.dao;

import av.biezbardis.mentorship.tasks.consoleapp.model.Student;
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
class StudentDaoTest {
    @InjectMocks
    private StudentDao dao;
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

        Student student = new Student();
        student.setFirstName("John");
        student.setLastName("Smith");
        student.setGroupId(13L);
        dao.save(student);

        verify(mockStatement).setString(1, "John");
        verify(mockStatement).setString(2, "Smith");
        verify(mockStatement).setLong(3, 13L);
        verify(mockStatement).executeUpdate();
    }

    @Test
    void shouldReturnNotEmptyOptionalWhenProvidedCorrectId() throws SQLException {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
        when(mockStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true, false);
        when(mockResultSet.getLong("student_id")).thenReturn(15L);
        when(mockResultSet.getString("first_name")).thenReturn("John");
        when(mockResultSet.getString("last_name")).thenReturn("Smith");
        when(mockResultSet.getLong("group_id")).thenReturn(13L);

        Optional<Student> actual = dao.findById(15L);

        assertTrue(actual.isPresent());
        assertEquals(15L, actual.get().getId());
        assertEquals("John", actual.get().getFirstName());
        assertEquals("Smith", actual.get().getLastName());
        assertEquals(13L, actual.get().getGroupId());
        verify(mockStatement).setLong(1, 15L);
        verify(mockStatement).executeQuery();
    }

    @Test
    void shouldReturnListOfEntitiesWhenCallGetAllMethod() throws SQLException {
        when(mockConnection.createStatement()).thenReturn(mockStatement);
        when(mockStatement.executeQuery(anyString())).thenReturn(mockResultSet);
        when(mockResultSet.next())
                .thenReturn(true).thenReturn(true).thenReturn(true).thenReturn(false);
        when(mockResultSet.getLong("student_id")).thenReturn(1L, 2L, 3L);
        when(mockResultSet.getString("first_name"))
                .thenReturn("John", "Beth", "Monty");
        when(mockResultSet.getString("last_name"))
                .thenReturn("Smith", "Gray", "Python");
        when(mockResultSet.getLong("group_id")).thenReturn(1L, 2L, 3L);

        List<Student> actual = dao.findAll();

        assertEquals(3, actual.size());
        assertEquals(1L, actual.get(0).getId());
        assertEquals(1L, actual.get(0).getGroupId());
        assertEquals("Beth", actual.get(1).getFirstName());
        assertEquals("Python", actual.get(2).getLastName());
    }

    @Test
    void shouldUpdateWhenProvidedCorrectEntity() throws SQLException {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
        when(mockStatement.executeUpdate()).thenReturn(1);

        Student student = new Student();
        student.setId(15L);
        student.setFirstName("John");
        student.setLastName("Smith");
        student.setGroupId(13L);

        dao.update(student);

        verify(mockStatement).setString(1, "John");
        verify(mockStatement).setString(2, "Smith");
        verify(mockStatement).setLong(3, 13L);
        verify(mockStatement).setLong(4, 15L);
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