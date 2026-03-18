package info.pekny.golfcourses.round;

import info.pekny.golfcourses.country.Country;
import info.pekny.golfcourses.course.GolfCourse;
import info.pekny.golfcourses.course.GolfCourseRepository;
import info.pekny.golfcourses.course.Hole;
import info.pekny.golfcourses.course.HoleRepository;
import info.pekny.golfcourses.course.LengthUnit;
import info.pekny.golfcourses.course.Tee;
import info.pekny.golfcourses.course.TeeRepository;
import info.pekny.golfcourses.user.Role;
import info.pekny.golfcourses.user.User;
import info.pekny.golfcourses.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PlayedRoundServiceTest {

    @Mock
    private PlayedRoundRepository roundRepository;

    @Mock
    private GolfCourseRepository courseRepository;

    @Mock
    private TeeRepository teeRepository;

    @Mock
    private HoleRepository holeRepository;

    @Mock
    private UserRepository userRepository;

    private PlayedRoundService roundService;

    private GolfCourse course;
    private Tee tee;
    private User user;
    private Hole hole;

    @BeforeEach
    void setUp() throws Exception {
        roundService = new PlayedRoundService(roundRepository, courseRepository,
                teeRepository, holeRepository, userRepository);

        Country country = new Country("Czech Republic");
        course = new GolfCourse("Karlštejn", country, LengthUnit.METERS);
        setId(course, 1L);

        tee = new Tee("White", course);
        setId(tee, 10L);

        user = new User("John", "john@example.com", "hashed", Role.USER);
        setId(user, 100L);

        hole = new Hole(1, 4, 7, course);
        setId(hole, 50L);
    }

    private void setId(Object entity, Long id) throws Exception {
        Field idField = entity.getClass().getDeclaredField("id");
        idField.setAccessible(true);
        idField.set(entity, id);
    }

    @Test
    void createRound() {
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(teeRepository.findById(10L)).thenReturn(Optional.of(tee));
        when(userRepository.findById(100L)).thenReturn(Optional.of(user));
        when(holeRepository.findById(50L)).thenReturn(Optional.of(hole));
        when(roundRepository.save(any(PlayedRound.class))).thenAnswer(inv -> inv.getArgument(0));

        LocalDate date = LocalDate.of(2026, 3, 15);
        PlayedRound round = roundService.create(date, 1L, 10L, 100L, Map.of(50L, 5));

        assertThat(round.getDate()).isEqualTo(date);
        assertThat(round.getCourse()).isEqualTo(course);
        assertThat(round.getTee()).isEqualTo(tee);
        assertThat(round.getUser()).isEqualTo(user);
        assertThat(round.getScores()).hasSize(1);
        assertThat(round.getScores().get(0).getScore()).isEqualTo(5);
    }

    @Test
    void createRoundWithTeeFromDifferentCourseThrows() throws Exception {
        GolfCourse otherCourse = new GolfCourse("Other", new Country("Germany"), LengthUnit.METERS);
        setId(otherCourse, 2L);
        Tee wrongTee = new Tee("Red", otherCourse);
        setId(wrongTee, 20L);

        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(teeRepository.findById(20L)).thenReturn(Optional.of(wrongTee));

        assertThatThrownBy(() -> roundService.create(LocalDate.now(), 1L, 20L, 100L, Map.of()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Tee does not belong to the selected course");
    }

    @Test
    void deleteRound() {
        PlayedRound round = new PlayedRound(LocalDate.now(), course, tee, user);
        when(roundRepository.findByIdAndUserId(1L, 100L)).thenReturn(Optional.of(round));

        roundService.delete(1L, 100L);

        verify(roundRepository).delete(round);
    }

    @Test
    void deleteNonexistentRoundThrows() {
        when(roundRepository.findByIdAndUserId(99L, 100L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> roundService.delete(99L, 100L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Round not found: 99");
    }
}
