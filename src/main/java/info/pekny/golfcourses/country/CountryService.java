package info.pekny.golfcourses.country;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class CountryService {

    private final CountryRepository countryRepository;

    public CountryService(CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }

    public List<Country> findAll() {
        return countryRepository.findAllByOrderByNameAsc();
    }

    public Optional<Country> findById(Long id) {
        return countryRepository.findById(id);
    }

    public boolean nameExists(String name) {
        return countryRepository.existsByName(name);
    }

    @Transactional
    public Country create(String name) {
        return countryRepository.save(new Country(name));
    }

    @Transactional
    public Country update(Long id, String name) {
        Country country = countryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Country not found: " + id));
        country.setName(name);
        return countryRepository.save(country);
    }

    @Transactional
    public void delete(Long id) {
        countryRepository.deleteById(id);
    }
}
