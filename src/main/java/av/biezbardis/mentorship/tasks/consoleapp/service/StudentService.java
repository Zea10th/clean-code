package av.biezbardis.mentorship.tasks.consoleapp.service;

import av.biezbardis.mentorship.tasks.consoleapp.model.Student;
import av.biezbardis.mentorship.tasks.consoleapp.dao.Dao;

import java.util.Comparator;
import java.util.Optional;
import java.util.stream.Collectors;

public class StudentService implements Service {
    private static final String FAILED =
            "Operation failed. Please check the fields being sent (first name, last name, group Id) and try again.";
    private static final String STUDENT_NOT_FOUND = "Student with this Id not found.";
    private final Dao<Student> studentDao;

    public StudentService(Dao<Student> studentDao) {
        this.studentDao = studentDao;
    }

    @Override
    public String save(String[] args) {
        Student student = new Student(args[0], args[1], Integer.parseInt(args[2]));
        int possibleId = studentDao.save(student);
        return (possibleId > 0) ?
                studentDao.get(possibleId) + " was added successfully." : FAILED;
    }

    @Override
    public String get(int id) {
        Optional<Student> student = studentDao.get(id);
        return (student.isPresent()) ? student.get().toString() : STUDENT_NOT_FOUND;
    }

    @Override
    public String getAll() {
        return "List of students:\n" +
                studentDao.getAll().stream()
                        .sorted(Comparator.comparingInt(Student::getId))
                        .map(student -> student.getId() + " " +
                                student.getFirstName() + " " +
                                student.getLastName() + " " +
                                "Group Id - " + student.getGroupId())
                        .collect(Collectors.joining(System.lineSeparator()));
    }

    @Override
    public String update(String[] args) {
        Student student = new Student(Integer.parseInt(args[0]), args[1], args[2], Integer.parseInt(args[3]));
        return (studentDao.update(student)) ? student + " was updated." : FAILED;
    }

    @Override
    public String delete(int id) {
        String student = get(id);
        return (studentDao.delete(id)) ? student + "successfully deleted." : FAILED;
    }
}
