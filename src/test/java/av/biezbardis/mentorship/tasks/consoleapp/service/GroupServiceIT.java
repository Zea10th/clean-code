package av.biezbardis.mentorship.tasks.consoleapp.service;

import av.biezbardis.mentorship.tasks.consoleapp.SchoolManagementSystem;
import av.biezbardis.mentorship.tasks.consoleapp.model.Group;
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
class GroupServiceIT {
    private final GroupService service;

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
    GroupServiceIT(GroupService service) {
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
        Group group = new Group();
        group.setName("G1-test");
        service.save(group);

        Group storedGroup = service.findById(1L);
        assertEquals(group.getName(), storedGroup.getName());
    }

    @Test
    void shouldUpdateEntity() {
        String initialName = "G1-test";
        String updatedName = "G1.1-test";


        Group group = new Group();
        group.setName(initialName);
        service.save(group);

        group.setId(1L);
        group.setName(updatedName);
        service.update(group);

        Group updatedGroup = service.findById(1L);
        assertEquals(updatedName, updatedGroup.getName());
    }

    @Test
    void shouldDeleteEntity() {
        Group group = new Group();
        group.setName("Test group");
        service.save(group);
        List<Group> initGroups = service.findAll();
        service.delete(1L);
        List<Group> oppositeCourses = service.findAll();

        assertNotEquals(initGroups, oppositeCourses);
        assertTrue(oppositeCourses.isEmpty());
    }

    @Test
    void shouldFindAllEntities() {
        for (int i = 1; i < 4; i++) {
            Group group = new Group();
            group.setName("Group No " + i);
            service.save(group);
        }

        List<Group> savedGroups = service.findAll();

        assertEquals(3, savedGroups.size());
        assertEquals("Group No 1", savedGroups.get(0).getName());
        assertEquals(2, savedGroups.get(1).getId());
    }
}