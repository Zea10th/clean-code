package av.biezbardis.mentorship.tasks.consoleapp.service;

import av.biezbardis.mentorship.tasks.consoleapp.SchoolManagementSystem;
import av.biezbardis.mentorship.tasks.consoleapp.model.Course;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.wait.strategy.LogMessageWaitStrategy;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Testcontainers
class CourseServiceIT {
    private final CourseService service;

    @MockBean
    SchoolManagementSystem schoolManagementSystem;

    @Container
    private static PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>(DockerImageName.parse("postgres:16-alpine"))
                    .withExposedPorts(5432)
                    .withUsername("test")
                    .withPassword("test")
                    .withDatabaseName("test_db")
                    .waitingFor(
                            new LogMessageWaitStrategy()
                                    .withRegEx(".*database system is ready to accept connections.*\\s")
                                    .withTimes(2).withStartupTimeout(Duration.of(60L, ChronoUnit.SECONDS)));

    @Autowired
    CourseServiceIT(CourseService service) {
        this.service = service;
    }

    @DynamicPropertySource
    static void registerProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url",
                () -> String.format("jdbc:postgresql://localhost:%d/%s",
                        postgres.getFirstMappedPort(),
                        postgres.getDatabaseName()));
        registry.add("spring.datasource.username", () -> postgres.getUsername());
        registry.add("spring.datasource.password", () -> postgres.getPassword());
        registry.add("spring.flyway.clean-disabled", () -> "false");
        registry.add("spring.flyway.locations", () -> "filesystem:src/test/resources/consoleapp/db/migration");
    }

    @BeforeAll
    static void startContainers() {
        postgres.start();
    }

    @AfterAll
    static void stopContainers() {
        postgres.stop();
    }

    @BeforeEach
    void dropData(@Autowired Flyway flyway) {
        flyway.clean();
        flyway.migrate();
    }

    @Test
    void shouldSaveEntityAndFindEntityById() {
        Course course = new Course();
        course.setName("Astrophysics");
        course.setDescription("Astrophysics is a science that employs the methods and principles of " +
                "physics and chemistry in the study of astronomical objects and phenomena.");
        service.save(course);

        Course storedCourse = service.findById(1L);
        assertEquals(course.getName(), storedCourse.getName());
        assertEquals(course.getDescription(), storedCourse.getDescription());
    }

    @Test
    void shouldUpdateEntity() {
        String initialDescription = "Astrophysics is a science that employs the methods and principles of " +
                "physics and chemistry in the study of astronomical objects and phenomena.";
        String updatedDescription = "Astrophysics is a branch of space science that applies the laws of " +
                "physics and chemistry to seek to understand the universe and our place in it.";


        Course course = new Course();
        course.setName("Astrophysics");
        course.setDescription(initialDescription);
        service.save(course);

        course.setId(1L);
        course.setDescription(updatedDescription);
        service.update(course);

        Course updatedCourse = service.findById(1L);
        assertEquals(course.getName(), updatedCourse.getName());
        assertEquals(updatedDescription, updatedCourse.getDescription());
    }

    @Test
    void shouldDeleteEntity() {
        Course course = new Course();
        course.setName("Test course");
        course.setDescription("Description of test course");
        service.save(course);
        List<Course> initCourses = service.findAll();
        service.delete(1L);
        List<Course> oppositeCourses = service.findAll();

        assertNotEquals(initCourses, oppositeCourses);
        assertTrue(oppositeCourses.isEmpty());
    }

    @Test
    void shouldFindAllEntities() {
        for (int i = 1; i < 4; i++) {
            Course course = new Course();
            course.setName("Course No " + i);
            course.setDescription("Description of course No " + i);
            service.save(course);
        }

        List<Course> savedCourses = service.findAll();

        assertEquals(3, savedCourses.size());
        assertEquals("Course No 1", savedCourses.get(0).getName());
        assertEquals("Description of course No 2", savedCourses.get(1).getDescription());
        assertEquals(3, savedCourses.get(2).getId());
    }
}