package av.biezbardis.mentorship.tasks.consoleapp.service;

import av.biezbardis.mentorship.tasks.consoleapp.dao.CourseDao;
import av.biezbardis.mentorship.tasks.consoleapp.model.Course;
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
class CourseServiceTest {
    @Mock
    private CourseDao repository;
    @InjectMocks
    private CourseService service;

    @Test
    void shouldSaveEntityWhenCalledSaveWithCorrectFields() {
        ArgumentCaptor<Course> valueCapture = ArgumentCaptor.forClass(Course.class);
        doNothing().when(repository).save(valueCapture.capture());

        Course course = new Course();
        course.setName("GPP");
        course.setDescription("General physical preparedness");

        service.save(course);

        verify(repository, times(1)).save(valueCapture.getValue());
    }

    @Test
    void shouldReturnCorrectEntityWhenCalledGetWithValidId() {
        Course course = new Course();
        course.setId(21L);
        course.setName("GPP");
        course.setDescription("General physical preparedness");

        when(repository.findById(21L)).thenReturn(course);

        Course actual = service.findById(21);

        assertEquals(21L, actual.getId());
        assertEquals("GPP", actual.getName());
        assertEquals("General physical preparedness", actual.getDescription());
    }

    @Test
    void shouldReturnEntityListWhenCalledGetAll() {
        List<Course> courses = new ArrayList<>();
        for (long courseId = 1L; courseId < 4L; courseId++) {
            Course course = new Course();
            course.setId(courseId);
            course.setName("CourseName" + courseId);
            course.setDescription("Description" + courseId);
            courses.add(course);
        }

        when(repository.findAll()).thenReturn(courses);

        List<Course> actual = service.findAll();

        assertEquals(3, actual.size());
        assertEquals(1, actual.get(0).getId());
        assertEquals("CourseName2", actual.get(1).getName());
        assertEquals("Description3", actual.get(2).getDescription());
    }

    @Test
    void shouldUpdateEntityWhenCalledUpdateWithCorrectArgs() {
        Course course = new Course();
        course.setId(17L);
        course.setName("GPP");
        course.setDescription("General physical preparedness");

        when(repository.findById(17L)).thenReturn(course);
        doNothing().when(repository).update(course);

        service.update(course);

        verify(repository, times(1)).update(course);
    }

    @Test
    void shouldDeleteEntityWhenCalledDeleteWithCorrectId() {
        doNothing().when(repository).delete(13L);

        service.delete(13L);

        verify(repository, times(1)).delete(13L);
    }
}