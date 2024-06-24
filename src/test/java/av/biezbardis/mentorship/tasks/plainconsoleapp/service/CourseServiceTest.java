package av.biezbardis.mentorship.tasks.plainconsoleapp.service;

import av.biezbardis.mentorship.tasks.plainconsoleapp.dao.CourseDao;
import av.biezbardis.mentorship.tasks.plainconsoleapp.model.Course;
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
class CourseServiceTest {
    @InjectMocks
    private CourseService service;
    @Mock
    private CourseDao dao;

    @Test
    void shouldSaveEntityWhenCalledSaveWithCorrectFields() {
        ArgumentCaptor<Course> valueCapture = ArgumentCaptor.forClass(Course.class);
        doNothing().when(dao).save(valueCapture.capture());

        Course course = new Course();
        course.setName("GPP");
        course.setDescription("General physical preparedness");

        service.save(course);

        verify(dao, times(1)).save(valueCapture.getValue());
    }

    @Test
    void shouldThrowNoSuchElementExceptionWhenDaoReturnsOptionalIsEmptyWhileSaving() {
        when(dao.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> service.findById(21));
    }

    @Test
    void shouldReturnCorrectEntityWhenCalledGetWithValidId() {
        Course course = new Course();
        course.setId(21L);
        course.setName("GPP");
        course.setDescription("General physical preparedness");

        when(dao.findById(21L)).thenReturn(Optional.of(course));

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

        when(dao.findAll()).thenReturn(courses);

        List<Course> actual = service.findAll();

        assertEquals(3, actual.size());
        assertEquals("CourseName2", actual.get(1).getName());
        assertEquals("Description3", actual.get(2).getDescription());
    }

    @Test
    void shouldThrowNoSuchElementExceptionWhenDaoReturnsEmptyOptionalWhileUpdating() {
        when(dao.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> service.update(new Course()));
    }

    @Test
    void shouldUpdateEntityWhenCalledUpdateWithCorrectArgs() {
        Course course = new Course();
        course.setId(17L);
        course.setName("GPP");
        course.setDescription("General physical preparedness");

        when(dao.findById(17L)).thenReturn(Optional.of(course));
        doNothing().when(dao).update(course);

        service.update(course);

        verify(dao, times(1)).update(course);
    }

    @Test
    void shouldDeleteEntityWhenCalledDeleteWithCorrectId() {
        doNothing().when(dao).delete(13L);

        service.delete(13L);

        verify(dao, times(1)).delete(13L);
    }
}