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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {
    @Mock
    private StudentDao repository;
    @InjectMocks
    private StudentService service;

    @Test
    void shouldSaveEntityWhenCalledSaveWithCorrectFields() {
        ArgumentCaptor<Student> valueCapture = ArgumentCaptor.forClass(Student.class);
        doNothing().when(repository).save(valueCapture.capture());

        Student student = new Student();
        student.setFirstName("John");
        student.setLastName("Doe");
        student.setGroupId(1L);

        service.save(student);

        verify(repository, times(1)).save(valueCapture.getValue());
    }

    @Test
    void shouldReturnCorrectEntityWhenCalledGetWithValidId() {
        Student student = new Student();
        student.setId(21L);
        student.setFirstName("John");
        student.setLastName("Doe");
        student.setGroupId(1L);

        when(repository.findById(21L)).thenReturn(student);

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

        when(repository.findAll()).thenReturn(students);

        List<Student> actual = service.findAll();

        assertEquals(3, actual.size());
        assertEquals("John2", actual.get(1).getFirstName());
        assertEquals("Doe3", actual.get(2).getLastName());
        assertEquals(1L, actual.get(0).getGroupId());
    }

    @Test
    void shouldUpdateEntityWhenCalledUpdateWithCorrectArgs() {
        Student student = new Student();
        student.setId(17L);
        student.setFirstName("John");
        student.setLastName("Doe");
        student.setGroupId(1L);

        when(repository.findById(17L)).thenReturn(student);
        doNothing().when(repository).update(student);

        service.update(student);

        verify(repository, times(1)).update(student);
    }

    @Test
    void shouldDeleteEntityWhenCalledDeleteWithCorrectId() {
        doNothing().when(repository).delete(13L);

        service.delete(13L);

        verify(repository, times(1)).delete(13L);
    }
}