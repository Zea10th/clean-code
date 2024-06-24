package av.biezbardis.mentorship.tasks.consoleapp.service;

import av.biezbardis.mentorship.tasks.consoleapp.dao.GenericDao;
import av.biezbardis.mentorship.tasks.consoleapp.dao.GroupDao;
import av.biezbardis.mentorship.tasks.consoleapp.model.Group;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GroupService implements GenericService<Group> {
    private final GroupDao repository;

    public GroupService(GenericDao<Group> repository) {
        this.repository = (GroupDao) repository;
    }

    @Override
    public void save(Group group) {
        repository.save(group);
    }

    @Override
    public Group findById(long id) {
        return repository.findById(id);
    }

    @Override
    public List<Group> findAll() {
        return repository.findAll();
    }

    @Override
    public void update(Group group) {
        Group storedGroup = repository.findById(group.getId());
        storedGroup.setName(group.getName());
        repository.update(storedGroup);
    }

    @Override
    public void delete(long id) {
        repository.delete(id);
    }
}
