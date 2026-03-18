package info.pekny.golfcourses.course;

import info.pekny.golfcourses.country.Country;
import info.pekny.golfcourses.country.CountryRepository;
import info.pekny.golfcourses.user.User;
import info.pekny.golfcourses.user.UserRepository;
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
    private final UserRepository userRepository;

    public GolfCourseService(GolfCourseRepository courseRepository,
                             TeeRepository teeRepository,
                             HoleRepository holeRepository,
                             CountryRepository countryRepository,
                             UserRepository userRepository) {
        this.courseRepository = courseRepository;
        this.teeRepository = teeRepository;
        this.holeRepository = holeRepository;
        this.countryRepository = countryRepository;
        this.userRepository = userRepository;
    }

    public List<GolfCourse> findByUserId(Long userId) {
        return courseRepository.findByUserIdOrderByNameAsc(userId);
    }

    public Optional<GolfCourse> findByIdAndUserId(Long id, Long userId) {
        return courseRepository.findByIdAndUserId(id, userId);
    }

    public List<Tee> findTeesByCourseId(Long courseId) {
        return teeRepository.findByCourseId(courseId);
    }

    public List<Hole> findHolesByCourseId(Long courseId) {
        return holeRepository.findByCourseIdOrderByNumberAsc(courseId);
    }

    @Transactional
    public GolfCourse create(String name, Long countryId, LengthUnit lengthUnit, Long userId) {
        Country country = countryRepository.findById(countryId)
                .orElseThrow(() -> new IllegalArgumentException("Country not found: " + countryId));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));
        GolfCourse course = new GolfCourse(name, country, lengthUnit, user);
        return courseRepository.save(course);
    }

    @Transactional
    public GolfCourse update(Long id, Long userId, String name, Long countryId, String description,
                             Double officialRating, Double personalRating,
                             boolean logoBall, LengthUnit lengthUnit) {
        GolfCourse course = courseRepository.findByIdAndUserId(id, userId)
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
    public void delete(Long id, Long userId) {
        GolfCourse course = courseRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new IllegalArgumentException("Course not found: " + id));
        courseRepository.delete(course);
    }

    @Transactional
    public Tee addTee(Long courseId, Long userId, String name, Double courseRating, Double slopeRating) {
        GolfCourse course = courseRepository.findByIdAndUserId(courseId, userId)
                .orElseThrow(() -> new IllegalArgumentException("Course not found: " + courseId));
        Tee tee = new Tee(name, course);
        tee.setCourseRating(courseRating);
        tee.setSlopeRating(slopeRating);
        return teeRepository.save(tee);
    }

    @Transactional
    public Hole addHole(Long courseId, Long userId, int number, int par, int hcp) {
        GolfCourse course = courseRepository.findByIdAndUserId(courseId, userId)
                .orElseThrow(() -> new IllegalArgumentException("Course not found: " + courseId));
        Hole hole = new Hole(number, par, hcp, course);
        return holeRepository.save(hole);
    }
}
