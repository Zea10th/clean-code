package av.biezbardis.mentorship.tasks.plainconsoleapp;

import av.biezbardis.mentorship.tasks.plainconsoleapp.dao.ConnectionUtil;
import av.biezbardis.mentorship.tasks.plainconsoleapp.dao.CourseDao;
import av.biezbardis.mentorship.tasks.plainconsoleapp.dao.GroupDao;
import av.biezbardis.mentorship.tasks.plainconsoleapp.dao.StudentCourseDaoImpl;
import av.biezbardis.mentorship.tasks.plainconsoleapp.dao.StudentDao;
import av.biezbardis.mentorship.tasks.plainconsoleapp.model.Course;
import av.biezbardis.mentorship.tasks.plainconsoleapp.model.Group;
import av.biezbardis.mentorship.tasks.plainconsoleapp.model.Student;
import av.biezbardis.mentorship.tasks.plainconsoleapp.service.CourseService;
import av.biezbardis.mentorship.tasks.plainconsoleapp.service.GroupService;
import av.biezbardis.mentorship.tasks.plainconsoleapp.service.StudentCourseServiceImpl;
import av.biezbardis.mentorship.tasks.plainconsoleapp.service.StudentService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class SchoolManagementSystem {
    private final BufferedReader reader;
    private final CourseService courseService;
    private final GroupService groupService;
    private final StudentCourseServiceImpl studentCourseService;
    private final StudentService studentService;

    public SchoolManagementSystem(ConnectionUtil connectionUtil) {
        this.reader = new BufferedReader(new InputStreamReader(System.in));
        this.courseService = new CourseService(new CourseDao(connectionUtil));
        this.groupService = new GroupService(new GroupDao(connectionUtil));
        this.studentCourseService = new StudentCourseServiceImpl(new StudentCourseDaoImpl(connectionUtil));
        this.studentService = new StudentService(new StudentDao(connectionUtil));
    }

    public void run() {
        String input;
        do {
            displayMenu();
            System.out.print("Enter your choice: ");
            try {
                input = reader.readLine();
                switch (input) {
                    case "a" -> findGroupsWithLessOrEqualStudents();
                    case "b" -> findStudentsByCourse();
                    case "c" -> addNewStudent();
                    case "d" -> deleteStudent();
                    case "e" -> addStudentToCourse();
                    case "f" -> removeStudentFromCourse();
                    case "exit" -> System.out.println("Exiting program...");
                    default -> System.out.println("Invalid choice. Please try again.");
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } while (!input.equals("exit"));
    }

    private void displayMenu() {
        System.out.println("""
                Options:
                a. Find all groups with less or equal students’ quantity
                b. Find all students related to the course with the given name
                c. Add a new student
                d. Delete a student by the student Id
                e. Add a student to the course (from a list)
                f. Remove the student from one of their courses
                Enter 'exit' to quit.""");
    }

    private void findGroupsWithLessOrEqualStudents() {
        System.out.print("Please enter quantity: ");
        int maxStudents;
        try {
            maxStudents = Integer.parseInt(reader.readLine());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        List<Group> groups = groupService.findAll();
        List<Student> students = studentService.findAll();

        List<Group> groupsWithMaxStudents = groups.stream()
                .filter(group -> students.stream()
                        .collect(Collectors.groupingBy(Student::getGroupId, Collectors.counting()))
                        .getOrDefault(group.getId(), 0L) <= maxStudents)
                .sorted(Comparator.comparingLong(Group::getId))
                .toList();

        if (groupsWithMaxStudents.isEmpty()) {
            System.out.println("No groups matching your requirement were found.");
        } else {
            System.out.println("Here are the groups that meet your requirement.");
            groupsWithMaxStudents.forEach(System.out::println);
        }
    }

    private void findStudentsByCourse() {
        System.out.print("Please enter course name: ");

        String courseName;
        try {
            courseName = reader.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Optional<Course> optionalCourse = courseService.findAll().stream()
                .filter(course -> course.getName().equals(courseName))
                .findFirst();
        if (optionalCourse.isEmpty()) {
            System.out.println("No course with that name was found.");
            return;
        }
        List<Student> students = studentCourseService.getStudentsByCourseId(optionalCourse.get().getId()).stream()
                .sorted(Comparator.comparingLong(Student::getId))
                .toList();

        if (students.isEmpty()) {
            System.out.println("No students matching your requirement were found.");
        } else {
            System.out.println("Here are students that meet your requirement.");
            students.forEach(System.out::println);
        }
    }

    private void addNewStudent() {
        System.out.print("Please enter firstname, lastname and group Id separated by a space: ");

        String[] studentArgs;
        try {
            studentArgs = reader.readLine().split(" ");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Student student = new Student();
        student.setFirstName(studentArgs[0]);
        student.setLastName(studentArgs[1]);
        student.setGroupId(Long.parseLong(studentArgs[2]));
        studentService.save(student);
    }

    private void deleteStudent() {
        System.out.print("Please enter student ID to delete from storage: ");
        long studentId;
        try {
            studentId = Long.parseLong(reader.readLine());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        studentService.delete(studentId);
    }

    private void addStudentToCourse() {
        System.out.print("Please enter student ID to enroll in the course: ");
        long studentId;
        try {
            studentId = Long.parseLong(reader.readLine());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        List<Course> coursesByStudentId = studentCourseService.getCoursesByStudentId(studentId);
        List<Course> courses = courseService.findAll();
        coursesByStudentId.forEach(
                courseToRemove -> courses.removeIf(
                        course -> course.getId() == courseToRemove.getId())
        );

        if (courses.isEmpty()) {
            System.out.println("There are no courses available for student enrollment.");
            return;
        }
        System.out.println("Here is a list of courses a student can enroll in:");
        courses.stream()
                .sorted(Comparator.comparingLong(Course::getId))
                .forEach(System.out::println);

        System.out.print("Please select a course ID for student enrollment: ");
        long courseId;
        try {
            courseId = Long.parseLong(reader.readLine());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        studentCourseService.enrollStudentInCourse(studentId, courseId);
    }

    private void removeStudentFromCourse() {
        System.out.print("Please enter student ID to withdraw from the course: ");
        long studentId;
        try {
            studentId = Long.parseLong(reader.readLine());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        List<Course> coursesByStudentId = studentCourseService.getCoursesByStudentId(studentId);
        if (coursesByStudentId.isEmpty()) {
            System.out.println("There are no courses available to expel a student.");
            return;
        }

        System.out.println("Here is a list of courses the student is enrolled:");
        coursesByStudentId.stream()
                .sorted(Comparator.comparingLong(Course::getId))
                .forEach(System.out::println);

        System.out.print("Please enter a course ID to withdraw a student: ");
        long courseId;
        try {
            courseId = Long.parseLong(reader.readLine());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        studentCourseService.unenrollStudentFromCourse(studentId, courseId);
    }
}
