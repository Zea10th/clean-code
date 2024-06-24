package av.biezbardis.mentorship.tasks.plainconsoleapp.service;

import av.biezbardis.mentorship.tasks.plainconsoleapp.dao.GenericDao;
import av.biezbardis.mentorship.tasks.plainconsoleapp.dao.StudentDao;
import av.biezbardis.mentorship.tasks.plainconsoleapp.model.Student;

import java.util.List;
import java.util.Optional;

public class StudentService implements Service<Student> {
    private final StudentDao studentDao;

    public StudentService(GenericDao<Student> studentDao) {
        this.studentDao = (StudentDao) studentDao;
    }

    @Override
    public void save(Student student) {
        studentDao.save(student);
    }

    @Override
    public Student findById(long id) {
        return studentDao.findById(id).orElseThrow();
    }

    @Override
    public List<Student> findAll() {
        return studentDao.findAll();
    }

    @Override
    public void update(Student student) {
        Optional<Student> optionalStudent = studentDao.findById(student.getId());
        Student storedStudent = optionalStudent.orElseThrow();
        storedStudent.setFirstName(storedStudent.getFirstName());
        storedStudent.setLastName(student.getLastName());
        storedStudent.setGroupId(storedStudent.getGroupId());
        studentDao.update(storedStudent);
    }

    @Override
    public void delete(long id) {
        studentDao.delete(id);
    }
}
