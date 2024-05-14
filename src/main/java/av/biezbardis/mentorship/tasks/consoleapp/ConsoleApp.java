package av.biezbardis.mentorship.tasks.consoleapp;

import av.biezbardis.mentorship.tasks.consoleapp.dao.ConnectionUtil;
import av.biezbardis.mentorship.tasks.consoleapp.utility.DatabaseManager;

public class ConsoleApp {
    public static void main(String[] args) {
        DatabaseManager adjuster = new DatabaseManager();
        adjuster.execute();

        SchoolManagementSystem system = new SchoolManagementSystem(ConnectionUtil.getInstance());
        system.run();
    }
}
