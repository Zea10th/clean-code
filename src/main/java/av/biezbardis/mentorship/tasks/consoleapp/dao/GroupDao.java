package av.biezbardis.mentorship.tasks.consoleapp.dao;

import av.biezbardis.mentorship.tasks.consoleapp.model.Group;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@Repository
public class GroupDao implements GenericDao<Group> {
    protected static final String DELETE_GROUP_SQL_QUERY = "DELETE FROM groups WHERE group_id = :group_id;";
    protected static final String FIND_ALL_GROUPS_SQL_QUERY = "SELECT * FROM groups;";
    protected static final String FIND_GROUP_SQL_QUERY = "SELECT * FROM groups WHERE group_id = :group_id;";
    protected static final String INSERT_GROUP_SQL_QUERY = "INSERT INTO groups (group_name) VALUES (:group_name);";
    protected static final String UPDATE_GROUP_SQL_QUERY = """
            UPDATE groups
            SET group_name = :group_name
            WHERE group_id = :group_id;""";
    private static final String ID_LABEL = "group_id";
    private static final String NAME_LABEL = "group_name";
    private final NamedParameterJdbcTemplate jdbcTemplate;

    public GroupDao(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static class GroupRowMapper implements RowMapper<Group> {
        @Override
        public Group mapRow(ResultSet resultSet, int rowNum) throws SQLException {
            Group group = new Group();
            group.setId(resultSet.getLong(ID_LABEL));
            group.setName(resultSet.getString(NAME_LABEL));
            return group;
        }
    }

    @Override
    public void save(Group group) {
        Map<String, ?> parameters = Map.of(
                NAME_LABEL, group.getName());
        jdbcTemplate.update(INSERT_GROUP_SQL_QUERY, parameters);
    }

    @Override
    public Group findById(Long id) {
        return jdbcTemplate.queryForObject(
                FIND_GROUP_SQL_QUERY,
                Map.of(ID_LABEL, id),
                new GroupRowMapper()
        );
    }

    @Override
    public List<Group> findAll() {
        return jdbcTemplate.query(
                FIND_ALL_GROUPS_SQL_QUERY,
                new GroupRowMapper()
        );
    }

    @Override
    public void update(Group group) {
        Map<String, ?> parameters = Map.of(
                ID_LABEL, group.getId(),
                NAME_LABEL, group.getName());
        jdbcTemplate.update(UPDATE_GROUP_SQL_QUERY, parameters);
    }

    @Override
    public void delete(Long id) {
        jdbcTemplate.update(
                DELETE_GROUP_SQL_QUERY,
                Map.of(ID_LABEL, id)
        );
    }
}
