package av.biezbardis.mentorship.tasks.plainconsoleapp.service;

import av.biezbardis.mentorship.tasks.plainconsoleapp.dao.CourseDao;
import av.biezbardis.mentorship.tasks.plainconsoleapp.dao.GenericDao;
import av.biezbardis.mentorship.tasks.plainconsoleapp.model.Course;

import java.util.List;
import java.util.Optional;

public class CourseService implements Service<Course> {
    private final CourseDao courseDao;

    public CourseService(GenericDao<Course> courseDao) {
        this.courseDao = (CourseDao) courseDao;
    }

    @Override
    public void save(Course course) {
        courseDao.save(course);
    }

    @Override
    public Course findById(long id) {
        return courseDao.findById(id).orElseThrow();
    }

    @Override
    public List<Course> findAll() {
        return courseDao.findAll();
    }

    @Override
    public void update(Course course) {
        Optional<Course> optionalCourse = courseDao.findById(course.getId());
        Course storedCourse = optionalCourse.orElseThrow();
        storedCourse.setName(course.getName());
        storedCourse.setDescription(course.getDescription());
        courseDao.update(storedCourse);
    }

    @Override
    public void delete(long id) {
        courseDao.delete(id);
    }
}
