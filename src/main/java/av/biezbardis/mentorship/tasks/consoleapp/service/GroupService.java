package av.biezbardis.mentorship.tasks.consoleapp.service;

import av.biezbardis.mentorship.tasks.consoleapp.model.Group;
import av.biezbardis.mentorship.tasks.consoleapp.dao.Dao;

import java.util.Comparator;
import java.util.Optional;
import java.util.stream.Collectors;

public class GroupService implements Service {
    private static final String FAILED =
            "Operation failed. Please check the fields being sent (group name) and try again.";
    private static final String GROUP_NOT_FOUND = "Group with this Id not found.";

    private final Dao<Group> groupDao;

    public GroupService(Dao<Group> groupDao) {
        this.groupDao = groupDao;
    }

    @Override
    public String save(String[] args) {
        Group group = new Group(args[0]);
        int possibleId = groupDao.save(group);
        return (possibleId > 0) ?
                groupDao.get(possibleId) + " was added successfully." : FAILED;
    }

    @Override
    public String get(int id) {
        Optional<Group> group = groupDao.get(id);
        return (group.isPresent()) ? group.get().toString() : GROUP_NOT_FOUND;
    }

    @Override
    public String getAll() {
        return "List of groups:\n" +
                groupDao.getAll().stream()
                        .sorted(Comparator.comparingInt(Group::getId))
                        .map(group -> group.getId() + " " + group.getName())
                        .collect(Collectors.joining(System.lineSeparator()));
    }

    @Override
    public String update(String[] args) {
        Group group = new Group(Integer.parseInt(args[0]), args[1]);
        return (groupDao.update(group)) ? group + " was updated." : FAILED;
    }

    @Override
    public String delete(int id) {
        String group = get(id);
        return (groupDao.delete(id)) ? group + "successfully deleted." : FAILED;
    }
}
