package av.biezbardis.mentorship.tasks.consoleapp.dao;

import av.biezbardis.mentorship.tasks.consoleapp.model.Group;
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
class GroupDaoTest {
    @Mock
    private NamedParameterJdbcTemplate jdbcTemplate;
    @InjectMocks
    private GroupDao dao;

    @Test
    void shouldSaveWhenProvidedCorrectEntity() {

        Group group = new Group();
        group.setName("GPP");

        dao.save(group);

        verify(jdbcTemplate).update(
                eq(GroupDao.INSERT_GROUP_SQL_QUERY),
                ArgumentMatchers.<Map<String, Long>>any()
        );
    }

    @Test
    void shouldReturnEntityWhenProvidedCorrectId() {
        dao.findById(15L);

        verify(jdbcTemplate).queryForObject(eq(GroupDao.FIND_GROUP_SQL_QUERY),
                ArgumentMatchers.<Map<String, Long>>any(),
                ArgumentMatchers.<RowMapper<Group>>any()
        );
    }

    @Test
    void shouldReturnListOfEntitiesWhenCallFindAllMethod() {
        dao.findAll();

        verify(jdbcTemplate).query(
                eq(GroupDao.FIND_ALL_GROUPS_SQL_QUERY),
                ArgumentMatchers.<RowMapper<Group>>any()
        );
    }

    @Test
    void shouldUpdateWhenProvidedCorrectEntity() {
        Group group = new Group();
        group.setId(13L);
        group.setName("GPP");

        dao.update(group);

        verify(jdbcTemplate).update(
                eq(GroupDao.UPDATE_GROUP_SQL_QUERY),
                ArgumentMatchers.<Map<String, Long>>any()
        );
    }

    @Test
    void shouldDeleteWhenProvidedCorrectEntityId() {
        Long groupId = 1L;

        dao.delete(groupId);

        verify(jdbcTemplate).update(
                eq(GroupDao.DELETE_GROUP_SQL_QUERY),
                ArgumentMatchers.<Map<String, Long>>any()
        );
    }
}