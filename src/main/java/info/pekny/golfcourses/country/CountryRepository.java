package info.pekny.golfcourses.country;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CountryRepository extends JpaRepository<Country, Long> {

    List<Country> findAllByOrderByNameAsc();

    boolean existsByName(String name);
}
