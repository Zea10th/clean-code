package av.biezbardis.mentorship.tasks.plainconsoleapp.dao;

import av.biezbardis.mentorship.tasks.plainconsoleapp.exception.DataAccessException;
import av.biezbardis.mentorship.tasks.plainconsoleapp.model.Group;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class GroupDao implements GenericDao<Group> {
    private static final String DELETE_GROUP_SQL_QUERY = "DELETE FROM groups WHERE group_id = ?;";
    private static final String FIND_ALL_GROUPS_SQL_QUERY = "SELECT * FROM groups;";
    private static final String FIND_GROUP_SQL_QUERY = "SELECT * FROM groups WHERE group_id = ?;";
    private static final String INSERT_GROUP_SQL_QUERY = "INSERT INTO groups (group_name) VALUES (?);";
    private static final String UPDATE_GROUP_SQL_QUERY = "UPDATE groups SET group_name = ? WHERE group_id = ?;";
    private final ConnectionUtil connectionUtil;

    public GroupDao(ConnectionUtil connectionUtil) {
        this.connectionUtil = connectionUtil;
    }

    @Override
    public void save(Group group) {
        try (Connection connection = connectionUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(INSERT_GROUP_SQL_QUERY)) {

            statement.setString(1, group.getName());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Failed to save entity", e);
        }
    }

    @Override
    public Optional<Group> findById(Long id) {
        try (Connection connection = connectionUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_GROUP_SQL_QUERY)) {

            statement.setLong(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                Optional<Group> optionalGroup = Optional.empty();
                while (resultSet.next()) {
                    Group group = new Group();
                    group.setId(resultSet.getLong("group_id"));
                    group.setName(resultSet.getString("group_name"));
                    optionalGroup = Optional.of(group);
                }
                return optionalGroup;
            }
        } catch (SQLException e) {
            throw new DataAccessException("Failed to find entity", e);
        }
    }

    @Override
    public List<Group> findAll() {
        try (Connection connection = connectionUtil.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(FIND_ALL_GROUPS_SQL_QUERY)) {

            List<Group> entities = new ArrayList<>();
            while (resultSet.next()) {
                Group group = new Group();
                group.setId(resultSet.getLong("group_id"));
                group.setName(resultSet.getString("group_name"));
                entities.add(group);
            }
            return entities;
        } catch (SQLException e) {
            throw new DataAccessException("Failed to find entities", e);
        }
    }

    @Override
    public void update(Group group) {
        try (Connection connection = connectionUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(UPDATE_GROUP_SQL_QUERY)) {

            statement.setString(1, group.getName());
            statement.setLong(2, group.getId());

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Failed to update entity", e);
        }
    }

    @Override
    public void delete(Long id) {
        try (Connection connection = connectionUtil.getConnection();
             PreparedStatement statement = connection
                     .prepareStatement(DELETE_GROUP_SQL_QUERY)) {

            statement.setLong(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Failed to delete entity", e);
        }
    }
}
