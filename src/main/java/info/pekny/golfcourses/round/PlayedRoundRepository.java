package info.pekny.golfcourses.round;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlayedRoundRepository extends JpaRepository<PlayedRound, Long> {

    List<PlayedRound> findByUserIdOrderByDateDesc(Long userId);

    List<PlayedRound> findByCourseIdAndUserIdOrderByDateDesc(Long courseId, Long userId);

    Optional<PlayedRound> findByIdAndUserId(Long id, Long userId);
}
