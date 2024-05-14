package av.biezbardis.mentorship.tasks.consoleapp.service;

import av.biezbardis.mentorship.tasks.consoleapp.dao.StudentDao;
import av.biezbardis.mentorship.tasks.consoleapp.model.Student;
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
class StudentServiceTest {
    @InjectMocks
    private StudentService service;
    @Mock
    private StudentDao dao;

    @Test
    void shouldSaveEntityWhenCalledSaveWithCorrectFields() {
        ArgumentCaptor<Student> valueCapture = ArgumentCaptor.forClass(Student.class);
        doNothing().when(dao).save(valueCapture.capture());

        Student student = new Student();
        student.setFirstName("John");
        student.setLastName("Doe");
        student.setGroupId(1L);

        service.save(student);

        verify(dao, times(1)).save(valueCapture.getValue());
    }

    @Test
    void shouldThrowNoSuchElementExceptionWhenDaoReturnsOptionalIsEmptyWhileSaving() {
        when(dao.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> service.findById(21));
    }

    @Test
    void shouldReturnCorrectEntityWhenCalledGetWithValidId() {
        Student student = new Student();
        student.setId(21L);
        student.setFirstName("John");
        student.setLastName("Doe");
        student.setGroupId(1L);

        when(dao.findById(21L)).thenReturn(Optional.of(student));

        Student actual = service.findById(21L);

        assertEquals(21L, actual.getId());
        assertEquals("John", actual.getFirstName());
        assertEquals("Doe", actual.getLastName());
        assertEquals(1L, actual.getGroupId());
    }

    @Test
    void shouldReturnEntityListWhenCalledGetAll() {
        List<Student> students = new ArrayList<>();
        for (int studentId = 1; studentId < 4; studentId++) {
            Student student = new Student();
            student.setFirstName("John" + studentId);
            student.setLastName("Doe" + studentId);
            student.setGroupId(studentId);
            students.add(student);
        }

        when(dao.findAll()).thenReturn(students);

        List<Student> actual = service.findAll();

        assertEquals(3, actual.size());
        assertEquals("John2", actual.get(1).getFirstName());
        assertEquals("Doe3", actual.get(2).getLastName());
        assertEquals(1L, actual.get(0).getGroupId());
    }

    @Test
    void shouldThrowServiceExceptionWhenDaoReturnsEmptyOptionalWhileUpdating() {
        when(dao.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> service.update(new Student()));
    }

    @Test
    void shouldUpdateEntityWhenCalledUpdateWithCorrectArgs() {
        Student student = new Student();
        student.setId(17L);
        student.setFirstName("John");
        student.setLastName("Doe");
        student.setGroupId(1L);

        when(dao.findById(17L)).thenReturn(Optional.of(student));
        doNothing().when(dao).update(student);

        service.update(student);

        verify(dao, times(1)).update(student);
    }

    @Test
    void shouldDeleteEntityWhenCalledDeleteWithCorrectId() {
        doNothing().when(dao).delete(13L);

        service.delete(13L);

        verify(dao, times(1)).delete(13L);
    }
}