package av.biezbardis.mentorship.tasks.consoleapp.model;

public class Student {
    private long id;
    private String firstName;
    private String lastName;
    private long groupId;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getGroupId() {
        return groupId;
    }

    public void setGroupId(long groupId) {
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
        return firstName + " " +
                lastName + " " +
                "ID " + id + " " +
                "group Id " + groupId;
    }
}
