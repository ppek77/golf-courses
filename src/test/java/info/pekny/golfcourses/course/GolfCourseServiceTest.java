package info.pekny.golfcourses.course;

import info.pekny.golfcourses.country.Country;
import info.pekny.golfcourses.country.CountryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GolfCourseServiceTest {

    @Mock
    private GolfCourseRepository courseRepository;

    @Mock
    private TeeRepository teeRepository;

    @Mock
    private HoleRepository holeRepository;

    @Mock
    private CountryRepository countryRepository;

    private GolfCourseService courseService;

    @BeforeEach
    void setUp() {
        courseService = new GolfCourseService(courseRepository, teeRepository, holeRepository, countryRepository);
    }

    @Test
    void createCourse() {
        Country country = new Country("Czech Republic");
        when(countryRepository.findById(1L)).thenReturn(Optional.of(country));
        when(courseRepository.save(any(GolfCourse.class))).thenAnswer(inv -> inv.getArgument(0));

        GolfCourse course = courseService.create("Karlštejn", 1L, LengthUnit.METERS);

        assertThat(course.getName()).isEqualTo("Karlštejn");
        assertThat(course.getCountry()).isEqualTo(country);
        assertThat(course.getLengthUnit()).isEqualTo(LengthUnit.METERS);
    }

    @Test
    void createCourseWithInvalidCountryThrows() {
        when(countryRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> courseService.create("Test", 99L, LengthUnit.METERS))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Country not found: 99");
    }

    @Test
    void updateCourse() {
        Country country = new Country("Czech Republic");
        GolfCourse existing = new GolfCourse("Old Name", country, LengthUnit.METERS);
        when(courseRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(countryRepository.findById(1L)).thenReturn(Optional.of(country));
        when(courseRepository.save(any(GolfCourse.class))).thenAnswer(inv -> inv.getArgument(0));

        GolfCourse updated = courseService.update(1L, "New Name", 1L, "A great course",
                8.5, 7.0, true, LengthUnit.YARDS);

        assertThat(updated.getName()).isEqualTo("New Name");
        assertThat(updated.getDescription()).isEqualTo("A great course");
        assertThat(updated.getOfficialRating()).isEqualTo(8.5);
        assertThat(updated.getPersonalRating()).isEqualTo(7.0);
        assertThat(updated.isLogoBall()).isTrue();
        assertThat(updated.getLengthUnit()).isEqualTo(LengthUnit.YARDS);
    }

    @Test
    void deleteCourse() {
        courseService.delete(1L);
        verify(courseRepository).deleteById(1L);
    }

    @Test
    void addTee() {
        Country country = new Country("Czech Republic");
        GolfCourse course = new GolfCourse("Karlštejn", country, LengthUnit.METERS);
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(teeRepository.save(any(Tee.class))).thenAnswer(inv -> inv.getArgument(0));

        Tee tee = courseService.addTee(1L, "White", 72.1, 131.0);

        assertThat(tee.getName()).isEqualTo("White");
        assertThat(tee.getCourseRating()).isEqualTo(72.1);
        assertThat(tee.getSlopeRating()).isEqualTo(131.0);
    }

    @Test
    void addHole() {
        Country country = new Country("Czech Republic");
        GolfCourse course = new GolfCourse("Karlštejn", country, LengthUnit.METERS);
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(holeRepository.save(any(Hole.class))).thenAnswer(inv -> inv.getArgument(0));

        Hole hole = courseService.addHole(1L, 1, 4, 7);

        assertThat(hole.getNumber()).isEqualTo(1);
        assertThat(hole.getPar()).isEqualTo(4);
        assertThat(hole.getHcp()).isEqualTo(7);
    }
}
