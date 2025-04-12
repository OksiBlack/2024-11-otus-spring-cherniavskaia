package ru.otus.hw.config;

import dasniko.testcontainers.keycloak.KeycloakContainer;
import lombok.Getter;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;
import org.testcontainers.utility.MountableFile;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

@Getter
public class TestContainerRegistry {

    public static final String KEYCLOAK_DB = "keycloak_db";
    public static final String KEYCLOAK_DB_USER = "keycloak_user";
    public static final String KEYCLOAK_DB_PASSWORD = "keycloak_password";
    public static final String KEYCLOAK_ADMIN_NAME = "admin";
    public static final String KEYCLOAK_ADMIN_PASSWORD = "nimda";

    private TestContainerRegistry() {
    }

    @Getter
    private static final PostgreSQLContainer<?> postgres =  new PostgreSQLContainer<>(DockerImageName.parse("postgres:latest"))
        .withDatabaseName("bookstore")
            .withUsername("admin")
            .withPassword("nimda");

    private final static PostgreSQLContainer<?> keycloakPostgres = createKeycloakPostgres();

    @Getter
    private final static KeycloakContainer keycloakContainer = createKeycloakContainer(keycloakPostgres);

    public static KeycloakContainer createKeycloakContainer(PostgreSQLContainer<?> postgresContainer) {


        KeycloakContainer keycloakContainer = new KeycloakContainer("quay.io/keycloak/keycloak:latest")
            .withExposedPorts(8080, 8443, 9000)
            .withEnv("DB_VENDOR", "POSTGRES")
            .withEnv("KEYCLOAK_DATABASE", postgresContainer.getDatabaseName())
            .withEnv("DB_VENDOR", "POSTGRES")
            .withEnv("KEYCLOAK_DATABASE", postgresContainer.getDatabaseName())
            .withEnv("KC_DB_USERNAME", KEYCLOAK_DB_USER)
            .withEnv("KC_DB_PASSWORD", KEYCLOAK_DB_PASSWORD)
            .withEnv("KC_BOOTSTRAP_ADMIN_USERNAME", KEYCLOAK_ADMIN_NAME)
            .withEnv("KC_BOOTSTRAP_ADMIN_PASSWORD", KEYCLOAK_ADMIN_PASSWORD)
            .withDebug()
            .withAdminPassword(KEYCLOAK_ADMIN_PASSWORD)
            .withAdminUsername(KEYCLOAK_ADMIN_NAME)
            .withCopyFileToContainer(MountableFile.forHostPath("keycloak/import/bookstore-realm.json"),
                "/opt/keycloak/data/import/bookstore-realm.json")
            .withRealmImportFile("keycloak/import/bookstore-realm.json")
            .withStartupTimeout(Duration.of(2, ChronoUnit.MINUTES))
            .withReuse(true);

        return keycloakContainer;
    }

    public static PostgreSQLContainer<?> createKeycloakPostgres() {
        PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>(DockerImageName.parse("postgres:latest"))
            .withDatabaseName(KEYCLOAK_DB)
            .withUsername(KEYCLOAK_DB_USER)
            .withPassword(KEYCLOAK_DB_PASSWORD);
        return postgresContainer;
    }

}
