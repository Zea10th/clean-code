package av.biezbardis.mentorship.tasks.plainconsoleapp.service;

import av.biezbardis.mentorship.tasks.plainconsoleapp.dao.StudentCourseDao;
import av.biezbardis.mentorship.tasks.plainconsoleapp.model.Course;
import av.biezbardis.mentorship.tasks.plainconsoleapp.model.Student;

import java.util.List;

public class StudentCourseServiceImpl implements StudentCourseService {
    private final StudentCourseDao dao;

    public StudentCourseServiceImpl(StudentCourseDao dao) {
        this.dao = dao;
    }

    @Override
    public void enrollStudentInCourse(long studentId, long courseId) {
        dao.enrollStudentInCourse(studentId, courseId);
    }

    @Override
    public void unenrollStudentFromCourse(long studentId, long courseId) {
        dao.unenrollStudentFromCourse(studentId, courseId);
    }

    @Override
    public List<Course> getCoursesByStudentId(long studentId) {
        return dao.getCoursesByStudentId(studentId);
    }

    @Override
    public List<Student> getStudentsByCourseId(long courseId) {
        return dao.getStudentsByCourseId(courseId);
    }
}
