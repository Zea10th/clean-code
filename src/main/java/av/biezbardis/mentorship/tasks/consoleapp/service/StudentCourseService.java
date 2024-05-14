package av.biezbardis.mentorship.tasks.consoleapp.service;

import av.biezbardis.mentorship.tasks.consoleapp.model.Course;
import av.biezbardis.mentorship.tasks.consoleapp.model.Student;

import java.util.List;

/**
 * Classes that implement this interface ensure the creation of many-to-many relationships
 * between students and courses, as well as obtaining lists of objects relative to the dependent object.
 * enrollStudentInCourse(Long studentId, Long courseId) - assigns the student to the course
 * unenrollStudentFromCourse(Long studentId, Long courseId) - excludes a student from a course
 * getCoursesByStudentId(Long studentId) - returns list of courses which the student attends
 * getStudentsByCourseId(Long courseId) - returns list of students who attends the course
 */
public interface StudentCourseService {
    /**
     * Creates a bond between objects of Student and Course
     * @param studentId - identifier of student
     * @param courseId - identifier of course
     */
    void enrollStudentInCourse(long studentId, long courseId);

    /**
     * Destroys a bond between objects of Student and Course
     * @param studentId - identifier of student
     * @param courseId - identifier of course
     */
    void unenrollStudentFromCourse(long studentId, long courseId);

    /**
     * Looking for courses by student Id
     * @param studentId - identifier of student
     * @return list of courses which the student attends
     */
    List<Course> getCoursesByStudentId(long studentId);

    /**
     * Looking for students by course Id
     * @param courseId - identifier of course
     * @return list of students who attends the course
     */
    List<Student> getStudentsByCourseId(long courseId);
}
