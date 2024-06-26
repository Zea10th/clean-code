package av.biezbardis.mentorship.tasks.consoleapp.service;

import av.biezbardis.mentorship.tasks.consoleapp.dao.GenericDao;
import av.biezbardis.mentorship.tasks.consoleapp.dao.StudentDao;
import av.biezbardis.mentorship.tasks.consoleapp.model.Student;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentService implements GenericService<Student> {
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
        return studentDao.findById(id);
    }

    @Override
    public List<Student> findAll() {
        return studentDao.findAll();
    }

    @Override
    public void update(Student student) {
        Student storedStudent = studentDao.findById(student.getId());
        storedStudent.setFirstName(student.getFirstName());
        storedStudent.setLastName(student.getLastName());
        storedStudent.setGroupId(storedStudent.getGroupId());
        studentDao.update(storedStudent);
    }

    @Override
    public void delete(long id) {
        studentDao.delete(id);
    }
}
