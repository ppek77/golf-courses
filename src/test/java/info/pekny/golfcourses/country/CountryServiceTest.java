package info.pekny.golfcourses.country;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CountryServiceTest {

    @Mock
    private CountryRepository countryRepository;

    private CountryService countryService;

    @BeforeEach
    void setUp() {
        countryService = new CountryService(countryRepository);
    }

    @Test
    void findAllReturnsOrderedList() {
        List<Country> countries = List.of(new Country("Austria"), new Country("Czech Republic"));
        when(countryRepository.findAllByOrderByNameAsc()).thenReturn(countries);

        List<Country> result = countryService.findAll();

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getName()).isEqualTo("Austria");
    }

    @Test
    void createCountry() {
        when(countryRepository.save(any(Country.class))).thenAnswer(inv -> inv.getArgument(0));

        Country created = countryService.create("Czech Republic");

        assertThat(created.getName()).isEqualTo("Czech Republic");
    }

    @Test
    void updateCountry() {
        Country existing = new Country("Czechia");
        when(countryRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(countryRepository.save(any(Country.class))).thenAnswer(inv -> inv.getArgument(0));

        Country updated = countryService.update(1L, "Czech Republic");

        assertThat(updated.getName()).isEqualTo("Czech Republic");
    }

    @Test
    void updateCountryNotFoundThrows() {
        when(countryRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> countryService.update(99L, "Nonexistent"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Country not found: 99");
    }

    @Test
    void deleteCountry() {
        countryService.delete(1L);
        verify(countryRepository).deleteById(1L);
    }
}
