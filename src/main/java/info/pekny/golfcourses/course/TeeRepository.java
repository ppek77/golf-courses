package info.pekny.golfcourses.course;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeeRepository extends JpaRepository<Tee, Long> {

    List<Tee> findByCourseId(Long courseId);
}
