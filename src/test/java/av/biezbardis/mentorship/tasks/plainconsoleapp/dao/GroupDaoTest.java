package av.biezbardis.mentorship.tasks.plainconsoleapp.dao;

import av.biezbardis.mentorship.tasks.plainconsoleapp.model.Group;
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
class GroupDaoTest {
    @InjectMocks
    private GroupDao dao;
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

        Group group = new Group();
        group.setName("GPP");
        dao.save(group);

        verify(mockStatement).setString(1, "GPP");
        verify(mockStatement).executeUpdate();
    }

    @Test
    void shouldReturnNotEmptyOptionalWhenProvidedCorrectId() throws SQLException {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
        when(mockStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true, false);
        when(mockResultSet.getLong("group_id")).thenReturn(15L);
        when(mockResultSet.getString("group_name")).thenReturn("GPP");

        Optional<Group> actual = dao.findById(15L);

        assertTrue(actual.isPresent());
        assertEquals(15L, actual.get().getId());
        assertEquals("GPP", actual.get().getName());
        verify(mockStatement).setLong(1, 15L);
        verify(mockStatement).executeQuery();
    }

    @Test
    void shouldReturnListOfEntitiesWhenCallGetAllMethod() throws SQLException {
        when(mockConnection.createStatement()).thenReturn(mockStatement);
        when(mockStatement.executeQuery(anyString())).thenReturn(mockResultSet);
        when(mockResultSet.next())
                .thenReturn(true).thenReturn(true).thenReturn(true).thenReturn(false);
        when(mockResultSet.getLong("group_id")).thenReturn(1L, 2L, 3L);
        when(mockResultSet.getString("group_name"))
                .thenReturn("GPP", "Nerds", "Jocks");

        List<Group> actual = dao.findAll();

        assertEquals(3, actual.size());
        assertEquals(1L, actual.get(0).getId());
        assertEquals("Nerds", actual.get(1).getName());
    }

    @Test
    void shouldUpdateWhenProvidedCorrectEntity() throws SQLException {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
        when(mockStatement.executeUpdate()).thenReturn(1);

        Group group = new Group();
        group.setId(13L);
        group.setName("GPP");
        dao.update(group);

        verify(mockStatement).setString(1, "GPP");
        verify(mockStatement).setLong(2, 13L);
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