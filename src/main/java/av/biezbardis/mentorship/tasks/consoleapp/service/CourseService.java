package av.biezbardis.mentorship.tasks.consoleapp.service;

import av.biezbardis.mentorship.tasks.consoleapp.model.Course;
import av.biezbardis.mentorship.tasks.consoleapp.dao.Dao;

import java.util.Comparator;
import java.util.Optional;
import java.util.stream.Collectors;

public class CourseService implements Service {
    private static final String FAILED =
            "Operation failed. Please check the fields being sent (course name, course description) and try again.";
    private static final String COURSE_NOT_FOUND = "Course with this Id not found.";

    private final Dao<Course> courseDao;

    public CourseService(Dao<Course> courseDao) {
        this.courseDao = courseDao;
    }

    @Override
    public String save(String[] args) {
        Course course = new Course(args[0], args[1]);
        int possibleId = courseDao.save(course);
        return (possibleId > 0) ?
                courseDao.get(possibleId) + " was added successfully." : FAILED;
    }

    @Override
    public String get(int id) {
        Optional<Course> course = courseDao.get(id);
        return (course.isPresent()) ? course.get().toString() : COURSE_NOT_FOUND;
    }

    @Override
    public String getAll() {
        return "List of courses:\n" +
                courseDao.getAll().stream()
                        .sorted(Comparator.comparingInt(Course::getId))
                        .map(course -> course.getId() +
                                " " + course.getName() +
                                " '" + course.getDescription() + "'")
                        .collect(Collectors.joining(System.lineSeparator()));
    }

    @Override
    public String update(String[] args) {
        Course course = new Course(Integer.parseInt(args[0]), args[1], args[2]);
        return (courseDao.update(course)) ? course + " was updated." : FAILED;
    }

    @Override
    public String delete(int id) {
        String course = get(id);
        return (courseDao.delete(id)) ? course + "successfully deleted." : FAILED;
    }
}
