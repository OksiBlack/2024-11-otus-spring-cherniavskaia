package ru.otus.hw.config;

import lombok.Data;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Setter
@Data
@ConfigurationProperties(prefix = "keycloak")
public class KeycloakProperties {
    private String realm;

    private Admin admin;

    private Server server;

    @Data
    public static class Admin {
        private String clientId;

        private String username;

        private String password;
    }


    @Data
    public static class Server {
        private String baseUrl;
    }
}

