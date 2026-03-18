package info.pekny.golfcourses.course;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GolfCourseRepository extends JpaRepository<GolfCourse, Long> {

    List<GolfCourse> findAllByOrderByNameAsc();
}
