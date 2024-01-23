package av.biezbardis.mentorship.tasks.consoleapp.dao;

import av.biezbardis.mentorship.tasks.consoleapp.model.Group;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class GroupDao implements Dao<Group> {
    private static final String CREATING_GROUP_FAILED = "Creating group failed, no rows affected.";
    private static final String NO_ID_OBTAINED = "Creating group failed, no ID obtained.";
    private static final String ADD_GROUP_SQL_QUERY = "insert into groups (group_name) values (?)";
    private static final String UPDATE_GROUP_SQL_QUERY = "update groups set group_name = ? where group_id = ?";
    private static final String DELETE_GROUP_SQL_QUERY = "delete from groups where group_id = ?";
    private static final String GET_GROUP_SQL_QUERY = "select * from groups where group_id = ?";
    private static final String GET_ALL_GROUPS_SQL_QUERY = "select * from groups";
    private final PostgreSqlDaoFactory daoFactory;

    public GroupDao(PostgreSqlDaoFactory daoFactory) {
        this.daoFactory = daoFactory;
    }

    @Override
    public int save(Group group) {
        int groupId = -1;

        try (Connection connection = daoFactory.getConnection();
             PreparedStatement statement = connection
                     .prepareStatement(ADD_GROUP_SQL_QUERY, Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, group.getName());

            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException(CREATING_GROUP_FAILED);
            }

            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (!generatedKeys.next()) {
                throw new SQLException(NO_ID_OBTAINED);
            }

            groupId = generatedKeys.getInt(1);
            group.setId(groupId);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return groupId;
    }

    @Override
    public Optional<Group> get(int groupId) {
        try (Connection connection = daoFactory.getConnection();
             PreparedStatement statement = connection
                     .prepareStatement(GET_GROUP_SQL_QUERY, Statement.RETURN_GENERATED_KEYS)) {

            statement.setInt(1, groupId);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(extractGroupFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }

    @Override
    public List<Group> getAll() {
        List<Group> groups = new ArrayList<>();

        try (Connection connection = daoFactory.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(GET_ALL_GROUPS_SQL_QUERY)) {

            while (resultSet.next()) {
                groups.add(extractGroupFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return groups;
    }

    @Override
    public boolean update(Group group) {
        try (Connection connection = daoFactory.getConnection();
             PreparedStatement statement = connection
                     .prepareStatement(UPDATE_GROUP_SQL_QUERY, Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, group.getName());
            statement.setInt(2, group.getId());
            statement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean delete(int groupId) {
        try (Connection connection = daoFactory.getConnection();
             PreparedStatement statement = connection
                     .prepareStatement(DELETE_GROUP_SQL_QUERY, Statement.RETURN_GENERATED_KEYS)) {

            statement.setInt(1, groupId);
            statement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private Group extractGroupFromResultSet(ResultSet resultSet) throws SQLException {
        Group group = new Group();
        group.setId(resultSet.getInt("group_id"));
        group.setName(resultSet.getString("group_name"));
        return group;
    }
}
