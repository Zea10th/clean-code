package av.biezbardis.mentorship.tasks.consoleapp.model;

public class Student {
    private int id;
    private String firstName;
    private String lastName;
    private int groupId;

    public Student() {
    }

    public Student(int id, String firstName, String lastName, int groupId) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.groupId = groupId;
    }

    public Student(String firstName, String lastName, int groupId) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.groupId = groupId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Override
    public String toString() {
        return "Student " +
                firstName + " " +
                lastName + " " +
                "Id " + id +
                ", Group Id " + groupId;
    }
}
