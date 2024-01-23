package av.biezbardis.mentorship.tasks.consoleapp;

import av.biezbardis.mentorship.tasks.consoleapp.dao.CourseDao;
import av.biezbardis.mentorship.tasks.consoleapp.dao.GroupDao;
import av.biezbardis.mentorship.tasks.consoleapp.dao.PostgreSqlDaoFactory;
import av.biezbardis.mentorship.tasks.consoleapp.service.GroupService;
import av.biezbardis.mentorship.tasks.consoleapp.dao.StudentDao;
import av.biezbardis.mentorship.tasks.consoleapp.service.CourseService;
import av.biezbardis.mentorship.tasks.consoleapp.service.Service;
import av.biezbardis.mentorship.tasks.consoleapp.service.StudentService;
import av.biezbardis.mentorship.tasks.consoleapp.utility.DatabaseAdjuster;

public class ConsoleApp {
    public static void main(String[] args) {
        Service courseService = new CourseService(new CourseDao(PostgreSqlDaoFactory.getInstance()));
        Service groupService = new GroupService(new GroupDao(PostgreSqlDaoFactory.getInstance()));
        Service studentService = new StudentService(new StudentDao(PostgreSqlDaoFactory.getInstance()));

        DatabaseAdjuster adjuster = new DatabaseAdjuster(courseService, groupService, studentService);
        adjuster.createTables();
        adjuster.fillUpTables();
    }
}
