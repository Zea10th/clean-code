package av.biezbardis.mentorship.tasks.plainconsoleapp;

import av.biezbardis.mentorship.tasks.plainconsoleapp.dao.ConnectionUtil;
import av.biezbardis.mentorship.tasks.plainconsoleapp.utility.DatabaseManager;

public class ConsoleApp {
    public static void main(String[] args) {
        DatabaseManager adjuster = new DatabaseManager();
        adjuster.execute();

        SchoolManagementSystem system = new SchoolManagementSystem(ConnectionUtil.getInstance());
        system.run();
    }
}
