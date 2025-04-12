package ru.otus.hw.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.DynamicPropertyRegistrar;
import org.testcontainers.containers.PostgreSQLContainer;

@TestConfiguration
public class TestContainersConfig {

    @ServiceConnection
    @Bean
    public static PostgreSQLContainer<?> postgresContainer() {
        return TestContainerRegistry.getPostgres();
    }

    @Bean
    public static DynamicPropertyRegistrar postgresDynamicPropertyRegistrar(PostgreSQLContainer<?> postgresContainer) {
        return registry -> {
            registry.add("spring.datasource.url", postgresContainer::getJdbcUrl);
            registry.add("spring.datasource.username", postgresContainer::getUsername);
            registry.add("spring.datasource.password", postgresContainer::getPassword);
        };
    }

//    @Bean(name = "keycloakPostgresContainer")
//    public static PostgreSQLContainer<?> keycloakPostgresContainer() {
//        return TestContainerRegistry.createKeycloakPostgres();
//    }
//
//    @Bean
//    public static KeycloakContainer keycloakContainer(
//        @Qualifier(value = "keycloakPostgresContainer") PostgreSQLContainer<?> keycloakPostgresContainer) {
//        return TestContainerRegistry.createKeycloakContainer(keycloakPostgresContainer);
//    }
//
//    @Bean
//    public static DynamicPropertyRegistrar keycloakDynamicPropertyRegistrar(KeycloakContainer keycloakContainer) {
//        return registry -> {
//            registry.add("${keycloak.server.base-url}", keycloakContainer::getAuthServerUrl);
//        };
//    }


}
