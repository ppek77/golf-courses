package info.pekny.golfcourses.course;

import info.pekny.golfcourses.country.Country;
import info.pekny.golfcourses.country.CountryRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class GolfCourseService {

    private final GolfCourseRepository courseRepository;
    private final TeeRepository teeRepository;
    private final HoleRepository holeRepository;
    private final CountryRepository countryRepository;

    public GolfCourseService(GolfCourseRepository courseRepository,
                             TeeRepository teeRepository,
                             HoleRepository holeRepository,
                             CountryRepository countryRepository) {
        this.courseRepository = courseRepository;
        this.teeRepository = teeRepository;
        this.holeRepository = holeRepository;
        this.countryRepository = countryRepository;
    }

    public List<GolfCourse> findAll() {
        return courseRepository.findAllByOrderByNameAsc();
    }

    public Optional<GolfCourse> findById(Long id) {
        return courseRepository.findById(id);
    }

    public List<Tee> findTeesByCourseId(Long courseId) {
        return teeRepository.findByCourseId(courseId);
    }

    public List<Hole> findHolesByCourseId(Long courseId) {
        return holeRepository.findByCourseIdOrderByNumberAsc(courseId);
    }

    @Transactional
    public GolfCourse create(String name, Long countryId, LengthUnit lengthUnit) {
        Country country = countryRepository.findById(countryId)
                .orElseThrow(() -> new IllegalArgumentException("Country not found: " + countryId));
        GolfCourse course = new GolfCourse(name, country, lengthUnit);
        return courseRepository.save(course);
    }

    @Transactional
    public GolfCourse update(Long id, String name, Long countryId, String description,
                             Double officialRating, Double personalRating,
                             boolean logoBall, LengthUnit lengthUnit) {
        GolfCourse course = courseRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Course not found: " + id));
        Country country = countryRepository.findById(countryId)
                .orElseThrow(() -> new IllegalArgumentException("Country not found: " + countryId));
        course.setName(name);
        course.setCountry(country);
        course.setDescription(description);
        course.setOfficialRating(officialRating);
        course.setPersonalRating(personalRating);
        course.setLogoBall(logoBall);
        course.setLengthUnit(lengthUnit);
        return courseRepository.save(course);
    }

    @Transactional
    public void delete(Long id) {
        courseRepository.deleteById(id);
    }

    @Transactional
    public Tee addTee(Long courseId, String name, Double courseRating, Double slopeRating) {
        GolfCourse course = courseRepository.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("Course not found: " + courseId));
        Tee tee = new Tee(name, course);
        tee.setCourseRating(courseRating);
        tee.setSlopeRating(slopeRating);
        return teeRepository.save(tee);
    }

    @Transactional
    public Hole addHole(Long courseId, int number, int par, int hcp) {
        GolfCourse course = courseRepository.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("Course not found: " + courseId));
        Hole hole = new Hole(number, par, hcp, course);
        return holeRepository.save(hole);
    }
}
