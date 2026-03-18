package info.pekny.golfcourses.course;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GolfCourseRepository extends JpaRepository<GolfCourse, Long> {

    List<GolfCourse> findByUserIdOrderByNameAsc(Long userId);

    java.util.Optional<GolfCourse> findByIdAndUserId(Long id, Long userId);
}
