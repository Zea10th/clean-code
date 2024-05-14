package av.biezbardis.mentorship.tasks.consoleapp.service;

import av.biezbardis.mentorship.tasks.consoleapp.dao.GenericDao;
import av.biezbardis.mentorship.tasks.consoleapp.dao.GroupDao;
import av.biezbardis.mentorship.tasks.consoleapp.model.Group;

import java.util.List;
import java.util.Optional;

public class GroupService implements Service<Group> {
    private final GroupDao groupDao;

    public GroupService(GenericDao<Group> groupDao) {
        this.groupDao = (GroupDao) groupDao;
    }

    @Override
    public void save(Group group) {
        groupDao.save(group);
    }

    @Override
    public Group findById(long id) {
        return groupDao.findById(id).orElseThrow();
    }

    @Override
    public List<Group> findAll() {
        return groupDao.findAll();
    }

    @Override
    public void update(Group group) {
        Optional<Group> optionalGroup = groupDao.findById(group.getId());
        Group storedGroup = optionalGroup.orElseThrow();
        storedGroup.setName(group.getName());
        groupDao.update(storedGroup);
    }

    @Override
    public void delete(long id) {
        groupDao.delete(id);
    }
}
