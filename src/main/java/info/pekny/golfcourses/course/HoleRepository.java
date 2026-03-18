package info.pekny.golfcourses.course;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HoleRepository extends JpaRepository<Hole, Long> {

    List<Hole> findByCourseIdOrderByNumberAsc(Long courseId);
}
