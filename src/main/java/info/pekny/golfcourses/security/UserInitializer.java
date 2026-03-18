package info.pekny.golfcourses.security;

import info.pekny.golfcourses.user.Role;
import info.pekny.golfcourses.user.User;
import info.pekny.golfcourses.user.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UserInitializer implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(UserInitializer.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AdminProperties adminProperties;
    private final StandardUserProperties standardUserProperties;

    public UserInitializer(UserRepository userRepository,
                           PasswordEncoder passwordEncoder,
                           AdminProperties adminProperties,
                           StandardUserProperties standardUserProperties) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.adminProperties = adminProperties;
        this.standardUserProperties = standardUserProperties;
    }

    @Override
    public void run(ApplicationArguments args) {
        if (!userRepository.existsByRole(Role.ADMIN)) {
            User admin = new User(
                    adminProperties.fullName(),
                    adminProperties.email(),
                    passwordEncoder.encode(adminProperties.password()),
                    Role.ADMIN
            );
            userRepository.save(admin);
            log.info("Default admin user created: {}", adminProperties.email());
        }
        if (!userRepository.existsByRole(Role.USER)) {
            User zajic = new User(
                    standardUserProperties.fullName(),
                    standardUserProperties.email(),
                    passwordEncoder.encode(standardUserProperties.password()),
                    Role.USER
            );
            userRepository.save(zajic);
            log.info("Default standard user created: {}", standardUserProperties.email());
        }
    }
}
