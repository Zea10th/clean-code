package av.biezbardis.mentorship.tasks.plainconsoleapp.dao;

import av.biezbardis.mentorship.tasks.plainconsoleapp.model.Course;
import av.biezbardis.mentorship.tasks.plainconsoleapp.model.Student;

import java.util.List;

/**
 * Classes that implement this interface ensure the creation of many-to-many relationships
 * by creating and dropping records in the database between objects of the Student and Course types.
 * Although provides possibility to obtain from the database lists of objects relative to the dependent object.
 * enrollStudentInCourse(Long studentId, Long courseId) - assigns the student to the course
 * unenrollStudentFromCourse(Long studentId, Long courseId) - excludes a student from a course
 * getCoursesByStudentId(Long studentId) - returns list of courses which the student attends
 * getStudentsByCourseId(Long courseId) - returns list of students who attends the course
 */
public interface StudentCourseDao {
    /**
     * Creates a record Student-Course in the database
     * @param studentId - identifier of student
     * @param courseId - identifier of course
     */
    void enrollStudentInCourse(Long studentId, Long courseId);

    /**
     * Deletes a record Student-Course in the database
     * @param studentId - identifier of student
     * @param courseId - identifier of course
     */
    void unenrollStudentFromCourse(Long studentId, Long courseId);

    /**
     * Deserialize objects of Course type that have relation with object of Student
     * @param studentId - identifier of student
     * @return list of courses which the student attends
     */
    List<Course> getCoursesByStudentId(Long studentId);

    /**
     * Deserialize objects of Student type that have relation with object of Course
     * @param courseId - identifier of course
     * @return list of students who attends the course
     */
    List<Student> getStudentsByCourseId(Long courseId);
}
