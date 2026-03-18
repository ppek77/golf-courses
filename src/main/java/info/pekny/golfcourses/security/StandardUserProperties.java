package info.pekny.golfcourses.security;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.standard-user")
public record StandardUserProperties(String email, String password, String fullName) {
}
