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
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GroupServiceTest {
    @InjectMocks
    private GroupService service;
    @Mock
    private GroupDao dao;

    @Test
    void shouldSaveEntityWhenCalledSaveWithCorrectFields() {
        ArgumentCaptor<Group> valueCapture = ArgumentCaptor.forClass(Group.class);
        doNothing().when(dao).save(valueCapture.capture());

        Group group = new Group();
        group.setName("GPP");

        service.save(group);

        verify(dao, times(1)).save(valueCapture.getValue());
    }

    @Test
    void shouldThrowNoSuchElementExceptionWhenDaoReturnsOptionalIsEmptyWhileSaving() {
        when(dao.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> service.findById(21));
    }

    @Test
    void shouldReturnCorrectEntityWhenCalledGetWithValidId() {
        Group group = new Group();
        group.setId(21L);
        group.setName("GPP");

        when(dao.findById(21L)).thenReturn(Optional.of(group));

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

        when(dao.findAll()).thenReturn(groups);

        List<Group> actual = service.findAll();

        assertEquals(3, actual.size());
        assertEquals("Group 2", actual.get(1).getName());
    }

    @Test
    void shouldThrowNoSuchElementExceptionWhenDaoReturnsEmptyOptionalWhileUpdating() {
        when(dao.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> service.update(new Group()));
    }

    @Test
    void shouldUpdateEntityWhenCalledUpdateWithCorrectArgs() {
        Group group = new Group();
        group.setId(17L);
        group.setName("Group A");


        when(dao.findById(17L)).thenReturn(Optional.of(group));
        doNothing().when(dao).update(group);

        service.update(group);

        verify(dao, times(1)).update(group);
    }

    @Test
    void shouldDeleteEntityWhenCalledDeleteWithCorrectId() {
        doNothing().when(dao).delete(13L);

        service.delete(13L);

        verify(dao, times(1)).delete(13L);
    }
}