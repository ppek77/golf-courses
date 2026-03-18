package info.pekny.golfcourses.round;

import info.pekny.golfcourses.course.GolfCourse;
import info.pekny.golfcourses.course.GolfCourseRepository;
import info.pekny.golfcourses.course.Hole;
import info.pekny.golfcourses.course.HoleRepository;
import info.pekny.golfcourses.course.Tee;
import info.pekny.golfcourses.course.TeeRepository;
import info.pekny.golfcourses.user.User;
import info.pekny.golfcourses.user.UserRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class PlayedRoundService {

    private final PlayedRoundRepository roundRepository;
    private final GolfCourseRepository courseRepository;
    private final TeeRepository teeRepository;
    private final HoleRepository holeRepository;
    private final UserRepository userRepository;

    public PlayedRoundService(PlayedRoundRepository roundRepository,
                              GolfCourseRepository courseRepository,
                              TeeRepository teeRepository,
                              HoleRepository holeRepository,
                              UserRepository userRepository) {
        this.roundRepository = roundRepository;
        this.courseRepository = courseRepository;
        this.teeRepository = teeRepository;
        this.holeRepository = holeRepository;
        this.userRepository = userRepository;
    }

    public List<PlayedRound> findByUserId(Long userId) {
        return roundRepository.findByUserIdOrderByDateDesc(userId);
    }

    public List<PlayedRound> findByCourseAndUser(Long courseId, Long userId) {
        return roundRepository.findByCourseIdAndUserIdOrderByDateDesc(courseId, userId);
    }

    public Optional<PlayedRound> findByIdAndUserId(Long id, Long userId) {
        return roundRepository.findByIdAndUserId(id, userId);
    }

    @Transactional
    public PlayedRound create(LocalDate date, Long courseId, Long teeId, Long userId,
                              Map<Long, Integer> holeScores) {
        GolfCourse course = courseRepository.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("Course not found: " + courseId));
        Tee tee = teeRepository.findById(teeId)
                .orElseThrow(() -> new IllegalArgumentException("Tee not found: " + teeId));
        if (!tee.getCourse().getId().equals(courseId)) {
            throw new IllegalArgumentException("Tee does not belong to the selected course");
        }
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));

        PlayedRound round = new PlayedRound(date, course, tee, user);
        round = roundRepository.save(round);

        for (Map.Entry<Long, Integer> entry : holeScores.entrySet()) {
            Hole hole = holeRepository.findById(entry.getKey())
                    .orElseThrow(() -> new IllegalArgumentException("Hole not found: " + entry.getKey()));
            if (!hole.getCourse().getId().equals(courseId)) {
                throw new IllegalArgumentException("Hole does not belong to the selected course");
            }
            RoundScore score = new RoundScore(round, hole, entry.getValue());
            round.getScores().add(score);
        }

        return roundRepository.save(round);
    }

    @Transactional
    public void delete(Long id, Long userId) {
        PlayedRound round = roundRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new IllegalArgumentException("Round not found: " + id));
        roundRepository.delete(round);
    }
}
