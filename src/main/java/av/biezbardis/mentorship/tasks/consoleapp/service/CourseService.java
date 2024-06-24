package av.biezbardis.mentorship.tasks.consoleapp.service;

import av.biezbardis.mentorship.tasks.consoleapp.dao.CourseDao;
import av.biezbardis.mentorship.tasks.consoleapp.dao.GenericDao;
import av.biezbardis.mentorship.tasks.consoleapp.model.Course;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseService implements GenericService<Course> {
    private final CourseDao repository;

    public CourseService(GenericDao<Course> repository) {
        this.repository = (CourseDao) repository;
    }

    @Override
    public void save(Course course) {
        repository.save(course);
    }

    @Override
    public Course findById(long id) {
        return repository.findById(id);
    }

    @Override
    public List<Course> findAll() {
        return repository.findAll();
    }

    @Override
    public void update(Course course) {
        Course storedCourse = repository.findById(course.getId());
        storedCourse.setName(course.getName());
        storedCourse.setDescription(course.getDescription());
        repository.update(course);
    }

    @Override
    public void delete(long id) {
        repository.delete(id);
    }
}
