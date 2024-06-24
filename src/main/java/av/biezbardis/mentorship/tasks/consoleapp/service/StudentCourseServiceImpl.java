package av.biezbardis.mentorship.tasks.consoleapp.service;

import av.biezbardis.mentorship.tasks.consoleapp.dao.StudentCourseDao;
import av.biezbardis.mentorship.tasks.consoleapp.model.Course;
import av.biezbardis.mentorship.tasks.consoleapp.model.Student;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentCourseServiceImpl implements StudentCourseService {
    private final StudentCourseDao repository;

    public StudentCourseServiceImpl(StudentCourseDao repository) {
        this.repository = repository;
    }

    @Override
    public void enrollStudentInCourse(long studentId, long courseId) {
        repository.enrollStudentInCourse(studentId, courseId);
    }

    @Override
    public void unenrollStudentFromCourse(long studentId, long courseId) {
        repository.unenrollStudentFromCourse(studentId, courseId);
    }

    @Override
    public List<Course> getCoursesByStudentId(long studentId) {
        return repository.getCoursesByStudentId(studentId);
    }

    @Override
    public List<Student> getStudentsByCourseId(long courseId) {
        return repository.getStudentsByCourseId(courseId);
    }
}
