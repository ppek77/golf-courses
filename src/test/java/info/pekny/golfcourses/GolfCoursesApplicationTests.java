package info.pekny.golfcourses;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@EnabledIfSystemProperty(named = "spring.profiles.active", matches = ".*")
class GolfCoursesApplicationTests {

    @Test
    void contextLoads() {
    }

}
