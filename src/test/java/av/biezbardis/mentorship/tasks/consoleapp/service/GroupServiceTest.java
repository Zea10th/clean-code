package av.biezbardis.mentorship.tasks.consoleapp.service;

import av.biezbardis.mentorship.tasks.consoleapp.dao.GroupDao;
import av.biezbardis.mentorship.tasks.consoleapp.model.Group;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GroupServiceTest {
    @Mock
    private GroupDao repository;
    @InjectMocks
    private GroupService service;


    @Test
    void shouldSaveEntityWhenCalledSaveWithCorrectFields() {
        ArgumentCaptor<Group> valueCapture = ArgumentCaptor.forClass(Group.class);
        doNothing().when(repository).save(valueCapture.capture());

        Group group = new Group();
        group.setName("GPP");

        service.save(group);

        verify(repository, times(1)).save(valueCapture.getValue());
    }

    @Test
    void shouldReturnCorrectEntityWhenCalledGetWithValidId() {
        Group group = new Group();
        group.setId(21L);
        group.setName("GPP");

        when(repository.findById(21L)).thenReturn(group);

        Group actual = service.findById(21);

        assertEquals(21, actual.getId());
        assertEquals("GPP", actual.getName());
    }

    @Test
    void shouldReturnEntityListWhenCalledGetAll() {
        List<Group> groups = new ArrayList<>();
        for (long groupId = 1L; groupId < 4L; groupId++) {
            Group group = new Group();
            group.setName("Group " + groupId);
            groups.add(group);
        }

        when(repository.findAll()).thenReturn(groups);

        List<Group> actual = service.findAll();

        assertEquals(3, actual.size());
        assertEquals("Group 2", actual.get(1).getName());
    }

    @Test
    void shouldUpdateEntityWhenCalledUpdateWithCorrectArgs() {
        Group group = new Group();
        group.setId(17L);
        group.setName("Group A");


        when(repository.findById(17L)).thenReturn(group);
        doNothing().when(repository).update(group);

        service.update(group);

        verify(repository, times(1)).update(group);
    }

    @Test
    void shouldDeleteEntityWhenCalledDeleteWithCorrectId() {
        doNothing().when(repository).delete(13L);

        service.delete(13L);

        verify(repository, times(1)).delete(13L);
    }
}